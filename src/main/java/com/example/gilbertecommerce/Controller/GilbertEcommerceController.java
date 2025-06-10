package com.example.gilbertecommerce.Controller;

import com.example.gilbertecommerce.Entity.*;
import com.example.gilbertecommerce.CustomException.AAGilbertException;
import com.example.gilbertecommerce.CustomException.AuthenticationException.IncorrectPasswordException;
import com.example.gilbertecommerce.CustomException.AuthenticationException.UserNotLoggedInException;
import com.example.gilbertecommerce.CustomException.BusinessExceptions.ListingNotFoundException;
import com.example.gilbertecommerce.Entity.LoginInfo;
import com.example.gilbertecommerce.Entity.ProductListing;
import com.example.gilbertecommerce.Entity.RegistrationForm;
import com.example.gilbertecommerce.Entity.User;
import com.example.gilbertecommerce.Framework.TagCategoryRepo;
import com.example.gilbertecommerce.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.servlet.http.HttpSession;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class GilbertEcommerceController {

    //Misforstod ideen med at centralisere exception håndteringen, fyldte controlleren med try catch, der kastede exceptions to gange.
    //Tog en ren controller og implementerede exception handling bedre
    private final LoginService loginService;
    private final UserService userService;
    private final HttpSession session;
    private final AdminService adminService;
    private final ProductListingService listingService;
    private final InitializerService initializerService;
    private final TagCategoryRepo tagCategoryRepo;
    private final LoggerService logger;
    private SearchQueryService queryService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public GilbertEcommerceController(LoginService loginService, UserService userService, HttpSession session, AdminService adminService, ProductListingService listingService, InitializerService initializerService, JdbcTemplate jdbcTemplate, SearchQueryService queryService, LoggerService logger, TagCategoryRepo tagCategoryRepo) {
        this.loginService = loginService;
        this.userService = userService;
        this.session = session;
        this.adminService = adminService;
        this.listingService = listingService;
        this.initializerService = initializerService;
        this.tagCategoryRepo = tagCategoryRepo;
        this.logger = logger;
        this.jdbcTemplate = jdbcTemplate;
        this.queryService = queryService;
    }

    // This formats "bags_and_luggage" -> "Bags And Luggage"
    private String formatDisplayName(String key) {
        if (key == null || key.isBlank()) {
            return "Unknown";
        }

        return Arrays.stream(key.split("_"))
                .filter(part -> !part.isBlank())
                .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1))
                .collect(Collectors.joining(" "));
    }

    @GetMapping("/searchResults")
    public String showSearchResults(Model model) {

        return "searchResults";
    }

    @PostMapping("/search/")
    public String searchProducts(@ModelAttribute("TestSearchForm") SearchForm form, Model model) {

        try {
            // This should now "refresh" the custom build sql, so that we no longer get duplicates in our search.
            queryService.clear();

            form.setTagSelections(extractTagSelections(form));
            queryService.buildFromForm(form);

            // This Likely still needs to be in the service layer as it is only the form that is needed...
            String sqlWhere = queryService.getSql();
            List<Object> params = queryService.getParams();
            String fullSql = "SELECT * FROM Listings productListing " + sqlWhere;

            // We need to make a service & a Repo method for this.
            List<ProductListing> results = jdbcTemplate.query(fullSql, params.toArray(), new ProductListingMapper());

            // Gets the tags for each Listing and add their tags.
            for (ProductListing product : results) {
                List<Tag> tags = initializerService.getTagsByListingId(product.getListingID());
                product.setTags(tags);
            }

            // Maps OwnerDisplayName onto the Listing
            listingService.getListingOwnerNameByListingId(results);

            Map<String, List<Tag>> tagFilterMap = initializerService.buildNormalizedCategoryTagsMap();

            // Create a map for pretty display names
            Map<String, String> prettyNameMap = new HashMap<>();
            tagFilterMap.keySet().forEach(key -> {
                prettyNameMap.put(key, formatDisplayName(key));
            });

            model.addAttribute("results", results);
            model.addAttribute("TestSearchForm", form);
            model.addAttribute("executedSql", fullSql);
            model.addAttribute("sqlParams", params);
            model.addAttribute("TestTagMap", tagFilterMap);
            model.addAttribute("PrettyNames", prettyNameMap);

            return "searchResults";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Search failed: " + e.getMessage());
            return "productListingPage"; // Fallback page
        }
    }


    private Map<String, String> extractTagSelections(SearchForm form) {
        Map<String, String> selections = new HashMap<>();

        for (Field field : form.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String name = field.getName();
                if (!name.equals("searchText") && !name.equals("tagSelections")) {
                    Object value = field.get(form);
                    if (value != null) {
                        selections.put(name, value.toString());
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // or log properly
            }
        }
        return selections;
    }


    @GetMapping("/")
    public String home(Model model) {

        return "redirect:/welcomePage";
    }


    @GetMapping("/welcomePage")
    public String getWelcomePage(Model model) {

        return "welcomePage";
    }

    // registerNewProfile GET & POST
    @GetMapping("/registerNewProfile")
    public String getNewProfile(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "registerNewProfile";
    }

    @PostMapping("/registerNewProfile")
    public String postNewProfile(@ModelAttribute("registrationForm") RegistrationForm
                                         registrationForm) {
        loginService.validateRegistrationForm(registrationForm);
        registrationForm.getLoginInfo().setLoginEmail(registrationForm.getUser().getEmail());
        loginService.registerUser(registrationForm.getLoginInfo(), registrationForm.getUser());
        return "redirect:/welcomePage";
    }


    @GetMapping("/loginPage")
    public String getLoginPage(Model model) {
        session.invalidate();
        model.addAttribute("loginInfo", new LoginInfo());
        return "/loginPage";
    }

    @PostMapping("/loginPage")
    public String postLoginPage(@ModelAttribute("loginInfo") LoginInfo loginInfo, Model
            model) {
        if (!loginService.checkLogin(loginInfo)) {
            throw new IncorrectPasswordException(
                    "Incorrect password or username",
                    "Failed login attempt for mail: " + loginInfo.getLoginEmail()
            );
        }

        LoginInfo actualFromDb = loginService.getLoginInfo(loginInfo);
        session.setAttribute("loginInfo", loginInfo);

        User user = userService.getUserByEmail(actualFromDb);
        if (user == null) {
            throw new AAGilbertException(
                    "User account not found",
                    "AUTH_002",
                    "No user found for login email: " + loginInfo.getLoginEmail(),
                    "GilbertController.postLoginPage"
            ) {
            };
        }
        session.setAttribute("user", user);
        return "redirect:/productListingPage";
    }

    @GetMapping("/productListingPage") //HOME
    public String getProductPage(Model model) {

        // Creates a new SearchForm Object so it is empty and ready to be used
        SearchForm form = new SearchForm();
        Map<String, List<Tag>> mapToBeTested = initializerService.buildNormalizedCategoryTagsMap();

        // Gets the Specific Listing required for the page
        List<ProductListing> approvedListings = initializerService.getApprovedListings();
        List<ProductListing> featuredListings = initializerService.getFeaturedListings();

        // Create a map for pretty display names
        Map<String, String> prettyNameMap = new HashMap<>();
        mapToBeTested.keySet().forEach(key -> {
            prettyNameMap.put(key, formatDisplayName(key));
        });

        // For Listings
        model.addAttribute("featuredListings", featuredListings);
        model.addAttribute("approvedListings", approvedListings);

        // For Filter Tags
        model.addAttribute("TestSearchForm", form);
        model.addAttribute("TestTagMap", mapToBeTested);
        model.addAttribute("PrettyNames", prettyNameMap);

        return "/productListingPage";
    }

    @GetMapping("/AdminMenu")
    public String getAdminMenu(Model model) {
        List<User> users = adminService.getAllUsers();
        List<ProductListing> pendingListings = listingService.getAllPendingProductListings();
        List<ProductListing> approvedListings = listingService.getAllApprovedListings();
        model.addAttribute("users", users);
        model.addAttribute("pendingListings", pendingListings);
        model.addAttribute("approvedListings", approvedListings);
        User user = loginService.getLoggedInUser(session);


        if (user.getRole() == null || !user.getRole().getRoleName().equalsIgnoreCase("admin")) {
            throw new UserNotLoggedInException(
                    "Access denied. Admin privilege required",
                    "A user with non admin role attempted admin: " + user.getEmail()
            );
        }
        return "/AdminMenu";
    }

    @GetMapping("/AdminMenu/Approve/{Id}")
    public String approveAdminListing(@PathVariable("Id") int listingId) {
        loginService.getLoggedInUser(session);
        adminService.approveListing(listingId);
        return "redirect:/AdminMenu";
    }

    @GetMapping("/AdminMenu/Reject/{Id}")
    public String rejectAdminListing(@PathVariable("Id") int listingId) {
        loginService.getLoggedInUser(session);
        adminService.rejectListing(listingId);
        return "redirect:/AdminMenu";
    }

    @GetMapping("/AdminMenu/Feature/{Id}")
    public String featureAdminListing(@PathVariable("Id") int listingId) {
        listingService.updateFeatureStatus(listingId, true);
        return "redirect:/AdminMenu";
    }

    @GetMapping("/AdminMenu/!Feature/{Id}")
    public String unfeatureAdminListing(@PathVariable("Id") int listingId) {
        listingService.updateFeatureStatus(listingId, false);
        return "redirect:/AdminMenu";
    }

    @GetMapping("/listingView/create")
    public String showCreateForm(Model model) {
        loginService.getLoggedInUser(session);
        TagInsertForm form = new TagInsertForm();
        HashMap<String, List<Tag>> mapToBeTested = initializerService.buildNormalizedCategoryTagsMap();

        // Create a map for pretty display names
        HashMap<String, String> prettyNameMap = new HashMap<>();
        mapToBeTested.keySet().forEach(key -> {
            prettyNameMap.put(key, formatDisplayName(key));
        });

        model.addAttribute("TestSearchForm", form);
        model.addAttribute("TestTagMap", mapToBeTested);
        model.addAttribute("PrettyNames", prettyNameMap);

        model.addAttribute("listing", new ProductListing());
        return "/CreateNewListingForm";
    }

    @PostMapping("/listingView/create")
    public String postCreateForm(@ModelAttribute("listing") ProductListing listing,
                                 @ModelAttribute("TestSearchForm") TagInsertForm form,
                                 Model model) {

        User user = loginService.getLoggedInUser(session);

        HashMap<String, List<Tag>> mapWithID = initializerService.buildNormalizedCategoryTagsMap();
        listingService.validateListing(listing, "GilbertController.postCreateForm");
        listing.setSellerID(user.getUserID());
        model.addAttribute("error", "all fields needed");
        try {
            listingService.create(listing);
            List<Tag> tags = initializerService.getTagsBySelection(form, mapWithID);
            for (Tag tag : tags) {
                System.out.println(tag.getTagValue());
                System.out.println(tag.getId());
            }
            listingService.insertTags(tags, listing.getListingTitle(), user.getUserID());


            return "redirect:/listingView";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/listingView")
    public String showOwnListings(Model model, HttpSession session) {
        User user = loginService.getLoggedInUser(session);
        if (user.getRole().getRoleName().equals("Admin")) {
            return "redirect:/AdminMenu";
        }
        model.addAttribute("user", user);
        List<ProductListing> listings = listingService.getListingsByUser(user.getUserID());
        model.addAttribute("listings", listings);
        return "/listingView";
    }

    @PostMapping("/listingView/delete/{id}")
    public String deleteListing(@PathVariable("id") int listingID, HttpSession session) {
        User user = loginService.getLoggedInUser(session);

        ProductListing listing = listingService.getProductListing(listingID);
        if (listing == null) {
            throw new ListingNotFoundException(
                    "The listing you're trying to delete doesn't exist",
                    "User tried deleting non-existing listing: " + listingID
            );
        }

        if (listing.getSellerID() != user.getUserID()) {
            throw new UserNotLoggedInException(
                    "You're attempting to delete a listing you don't own.",
                    "User: " + user.getUserID() + " tried deleting listing they don't own: " + listingID
            );
        }
        listingService.delete(listingID);
        return "redirect:/listingView";
    }
}
