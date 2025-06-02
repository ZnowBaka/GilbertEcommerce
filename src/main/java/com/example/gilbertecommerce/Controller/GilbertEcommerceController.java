package com.example.gilbertecommerce.Controller;

import com.example.gilbertecommerce.CustomException.IncorrectPasswordException;
import com.example.gilbertecommerce.CustomException.UserNotLoggedIn;
import com.example.gilbertecommerce.Entity.*;
import com.example.gilbertecommerce.Framework.TagCategoryRepo;
import com.example.gilbertecommerce.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GilbertEcommerceController {

    private final LoginService loginService;
    private final UserService userService;
    private final HttpSession session;
    private final AdminService adminService;
    private final ProductListingService listingService;
    private final ProductListingService productListingService;
    private final CategoryTagMapService categoryTagMapService;


    public GilbertEcommerceController(LoginService loginService, UserService userService, HttpSession session, AdminService adminService, ProductListingService listingService, ProductListingService productListingService, CategoryTagMapService categoryTagMapService) {
        this.loginService = loginService;
        this.userService = userService;
        this.session = session;
        this.adminService = adminService;
        this.listingService = listingService;
        this.productListingService = productListingService;
        this.categoryTagMapService = categoryTagMapService;
    }


    @GetMapping("/testTags")
    public String testCategoryService(@ModelAttribute SearchForm searchForm, Model model) {
        searchForm = new SearchForm();
        Map<String, List<Tag>> mapToBeTested = categoryTagMapService.buildNormalizedCategoryTagsMap();

        model.addAttribute("TestSearchForm", searchForm);
        model.addAttribute("TestTagMap", mapToBeTested);

        return "testTags";
    }

    /*
    @GetMapping("/search")
    public String searchProducts(@ModelAttribute SearchForm form, Model model) {
        ProductSearchQueryBuilder builder = new ProductSearchQueryBuilder();
        builder.buildFromForm(form);

        // This is used for the main Query
        String sqlWhere = builder.getSql();
        List<Object> params = builder.getParams();

        // That is a cgpt explanation
        // You’d use these with a JdbcTemplate or similar:
        String fullSql = "SELECT * FROM Listings productListing " + sqlWhere;
        List<Listing> results = jdbcTemplate.query(fullSql, params.toArray(), new ListingRowMapper());

        model.addAttribute("results", results);
        return "searchResults";
    }
    */


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