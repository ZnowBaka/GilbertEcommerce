package com.example.gilbertecommerce.Controller;

import com.example.gilbertecommerce.CustomException.IncorrectPasswordException;
import com.example.gilbertecommerce.CustomException.UserNotLoggedIn;
import com.example.gilbertecommerce.Entity.*;
import com.example.gilbertecommerce.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.servlet.http.HttpSession;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class GilbertEcommerceController {

    private final LoginService loginService;
    private final UserService userService;
    private final HttpSession session;
    private final AdminService adminService;
    private final ProductListingService listingService;
    private final ProductListingService productListingService;
    private final CategoryTagMapService categoryTagMapService;

    private SearchQueryService queryService;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public GilbertEcommerceController(LoginService loginService, UserService userService, HttpSession session, AdminService adminService, ProductListingService listingService, ProductListingService productListingService, CategoryTagMapService categoryTagMapService,JdbcTemplate jdbcTemplate, SearchQueryService queryService) {
        this.loginService = loginService;
        this.userService = userService;
        this.session = session;
        this.adminService = adminService;
        this.listingService = listingService;
        this.productListingService = productListingService;
        this.categoryTagMapService = categoryTagMapService;
        this.jdbcTemplate = jdbcTemplate;
        this.queryService = queryService;
    }


    @GetMapping("/testTags")
    public String testCategoryService(Model model) {
        SearchForm form = new SearchForm();
        Map<String, List<Tag>> mapToBeTested = categoryTagMapService.buildNormalizedCategoryTagsMap();

        // Create a map for pretty display names
        Map<String, String> prettyNameMap = new HashMap<>();
        mapToBeTested.keySet().forEach(key -> {
            prettyNameMap.put(key, formatDisplayName(key));
        });

        model.addAttribute("TestSearchForm", form);
        model.addAttribute("TestTagMap", mapToBeTested);
        model.addAttribute("PrettyNames", prettyNameMap);


        return "testTags";
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
                List<Tag> tags = categoryTagMapService.getTagsByListingId(product.getListingID());
                product.setTags(tags);
            }

            model.addAttribute("results", results);
            model.addAttribute("TestSearchForm", form);
            model.addAttribute("executedSql", fullSql);
            model.addAttribute("sqlParams", params);

            return "searchResults";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Search failed: " + e.getMessage());
            return "testTags"; // or your fallback page
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
    public String postNewProfile(@ModelAttribute("registrationForm") RegistrationForm registrationForm, Model model) {

        registrationForm.getLoginInfo().setLoginEmail(registrationForm.getUser().getEmail());
        if (!loginService.doesLoginInfoExist(registrationForm.getLoginInfo().getLoginEmail())) {
            if (registrationForm.getLoginInfo().getLoginPass().equals(registrationForm.getPasswordConfirmation())) {
                loginService.registerUser(registrationForm.getLoginInfo(), registrationForm.getUser());
                return "redirect:/welcomePage";
            }
            model.addAttribute("error", "passwords do not match");
            return "/registerNewProfile";
        } else {
            model.addAttribute("error", "User already exists");
            return "/registerNewProfile";
        }
    }


    @GetMapping("/loginPage")
    public String getLoginPage(Model model) {
        session.invalidate();
        model.addAttribute("loginInfo", new LoginInfo());
        return "/loginPage";
    }

    @PostMapping("/loginPage")
    public String postLoginPage(@ModelAttribute("loginInfo") LoginInfo loginInfo, Model model) throws IncorrectPasswordException, SQLException {

        if (loginService.checkLogin(loginInfo)) {

            LoginInfo actualFromDb = loginService.getLoginInfo(loginInfo);
            session.setAttribute("loginInfo", loginInfo);

            User user = userService.getUserByEmail(actualFromDb);
            if (user == null) {
                System.out.println("No user found for login ID: " + loginInfo.getLoginId());
                model.addAttribute("error", "User not found.");
                return "/loginPage";
            }

            session.setAttribute("user", user);
            System.out.println("displayName: " + user.getDisplayName());
            return "redirect:/productListingPage";
        } else {
            model.addAttribute("error", "Incorrect username or password");
            return "/loginPage";
        }
    }

    @GetMapping("/productListingPage") //HOME
    public String getProductPage() {
        return "/productListingPage";
    }

    @GetMapping("/AdminMenu")
    public String getAdminMenu(Model model) {
        User user = (User) session.getAttribute("user");
        List<User> users = adminService.getAllUsers();
        List<ProductListing> pendingListings = listingService.getAllPendingProductListings();
        model.addAttribute("users", users);
        model.addAttribute("pendingListings", pendingListings);
        if (user.getRole().getRoleName().equals("Admin")) {
            return "/AdminMenu";
        }
        return "redirect:/listingView";
    }

    @GetMapping("/AdminMenu/Approve/{Id}")
    public String postAdminMenu(@PathVariable("Id") int listingId, Model model) {
        System.out.println("approving listing: " + listingId);
        adminService.approveListing(listingId);
        return "redirect:/AdminMenu";
    }

    @GetMapping("/AdminMenu/Reject/{Id}")
    public String postAdminMenuReject(@PathVariable("Id") int listingId, Model model) {
        System.out.println("rejecting listing: " + listingId);
        adminService.rejectListing(listingId);
        return "redirect:/AdminMenu";
    }

    @GetMapping("/listingView/create")
    public String showCreateForm(Model model) {
        model.addAttribute("listing", new ProductListing());
        return "/CreateNewListingForm";
    }

    @PostMapping("/listingView/create")
    public String postCreateForm(@ModelAttribute("listing") ProductListing listing, Model model) {
        User user = (User) session.getAttribute("user");
        System.out.println(user.getUserID());
        System.out.println(listing.getListingTitle());
        listing.setSellerID(user.getUserID());
        model.addAttribute("error", "all fields needed");
        try {
            productListingService.create(listing);
            return "redirect:/listingView";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/listingView")
    public String showOwnListings(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole().getRoleName().equals("Admin")) {
            return "redirect:/AdminMenu";
        }
        model.addAttribute("user", user);
        List<ProductListing> listings = listingService.getListingsByUser(user.getUserID());
        model.addAttribute("listings", listings);
        return "/listingView";
    }

    @PostMapping("/listingView/delete/{id}")
    public String deleteListing(@PathVariable("id") int listingID, HttpSession session, Model model) {

        User user = getLoggedInUser(session);
        ProductListing listing = listingService.getProductListing(listingID);
        if (listing != null && listing.getSellerID() == user.getUserID()) {
            listingService.delete(listingID);
        }
        return "redirect:/listingView";
    }

    private User getLoggedInUser(HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new UserNotLoggedIn("You cannot acces this page as a guest. Please create an account or log in");
        }
        return user;
    }

}