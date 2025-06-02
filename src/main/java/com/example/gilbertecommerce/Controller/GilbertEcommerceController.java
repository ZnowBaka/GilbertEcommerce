package com.example.gilbertecommerce.Controller;

import com.example.gilbertecommerce.CustomException.IncorrectPasswordException;
import com.example.gilbertecommerce.CustomException.UserNotLoggedIn;
import com.example.gilbertecommerce.Entity.LoginInfo;
import com.example.gilbertecommerce.Entity.ProductListing;
import com.example.gilbertecommerce.Entity.RegistrationForm;
import com.example.gilbertecommerce.Entity.User;
import com.example.gilbertecommerce.Framework.TagCategoryRepo;
import com.example.gilbertecommerce.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

import java.util.List;

@Controller
public class GilbertEcommerceController {

    private final LoginService loginService;
    private final UserService userService;
    private final HttpSession session;
    private final AdminService adminService;
    private final ProductListingService listingService;
    private final ProductListingService productListingService;
    private final CategoryTagMapService categoryTagMapService;
    private final TagCategoryRepo tagCategoryRepo;


    public GilbertEcommerceController(ProductListingService listingService, LoginService loginService, UserService userService, HttpSession session, AdminService adminService, ProductListingService productListingService, CategoryTagMapService categoryTagMapService, TagCategoryRepo tagCategoryRepo) {
        this.loginService = loginService;
        this.userService = userService;
        this.session = session;
        this.adminService = adminService;
        this.listingService = listingService;
        this.productListingService = productListingService;
        this.categoryTagMapService = categoryTagMapService;
        this.tagCategoryRepo = tagCategoryRepo;
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
            if(user.getRole().getRoleName().equals("Admin")) {
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
        model.addAttribute("error","all fields needed");
        try{
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
        if(listing != null && listing.getSellerID() == user.getUserID()) {
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