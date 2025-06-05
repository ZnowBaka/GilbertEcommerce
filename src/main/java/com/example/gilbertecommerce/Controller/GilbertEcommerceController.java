//package com.example.gilbertecommerce.Controller;
//
//import com.example.gilbertecommerce.CustomException.AAGilbertException;
//import com.example.gilbertecommerce.CustomException.AuthenticationException.IncorrectPasswordException;
//import com.example.gilbertecommerce.CustomException.AuthenticationException.UserAlreadyExistException;
//import com.example.gilbertecommerce.CustomException.AuthenticationException.UserDoesNotExistException;
//import com.example.gilbertecommerce.CustomException.AuthenticationException.UserNotLoggedInException;
//import com.example.gilbertecommerce.CustomException.BusinessExceptions.ListingNotFoundException;
//import com.example.gilbertecommerce.CustomException.DatabaseExceptionS.DataIntegrityException;
//import com.example.gilbertecommerce.CustomException.ValidationExceptions.PasswordMismatch;
//import com.example.gilbertecommerce.Entity.LoginInfo;
//import com.example.gilbertecommerce.Entity.ProductListing;
//import com.example.gilbertecommerce.Entity.RegistrationForm;
//import com.example.gilbertecommerce.Entity.User;
//import com.example.gilbertecommerce.Framework.TagCategoryRepo;
//import com.example.gilbertecommerce.Service.*;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.dao.DataAccessException;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.sql.SQLException;
//
//import java.util.List;
//
//@Controller
//public class GilbertEcommerceController {
//
//    private final LoginService loginService;
//    private final UserService userService;
//    private final HttpSession session;
//    private final AdminService adminService;
//    private final ProductListingService listingService;
//    private final CategoryTagMapService categoryTagMapService;
//    private final TagCategoryRepo tagCategoryRepo;
//    private final LoggerService logger;
//
//
//    public GilbertEcommerceController(LoggerService logger, ProductListingService listingService, LoginService loginService, UserService userService, HttpSession session, AdminService adminService, CategoryTagMapService categoryTagMapService, TagCategoryRepo tagCategoryRepo) {
//        this.loginService = loginService;
//        this.userService = userService;
//        this.session = session;
//        this.adminService = adminService;
//        this.listingService = listingService;
//        this.categoryTagMapService = categoryTagMapService;
//        this.tagCategoryRepo = tagCategoryRepo;
//        this.logger = logger;
//    }
//
//    @GetMapping("/")
//    public String home(Model model) {
//
//        return "redirect:/welcomePage";
//    }
//
//
//    @GetMapping("/welcomePage")
//    public String getWelcomePage(Model model) {
//
//        return "welcomePage";
//    }
//
//    // registerNewProfile GET & POST
//    @GetMapping("/registerNewProfile")
//    public String getNewProfile(Model model) {
//        model.addAttribute("registrationForm", new RegistrationForm());
//        return "registerNewProfile";
//    }
//
//
//    @PostMapping("/registerNewProfile") //Omskrevet metoden, meget validering og fejlhåndtering sker nu på servicelaget
//    public String postNewProfile(@ModelAttribute("registrationForm") RegistrationForm registrationForm, Model model) {
//
//        loginService.validateRegistrationForm(registrationForm);
//        registrationForm.getLoginInfo().setLoginEmail(registrationForm.getUser().getEmail());
//
//        loginService.registerUser(registrationForm.getLoginInfo(), registrationForm.getUser());
//        return "redirect:/welcomePage";
//    }
//
////    public String postNewProfile(@ModelAttribute("registrationForm") RegistrationForm registrationForm, Model model) {
////
////        try {
////            loginService.validateRegistrationForm(registrationForm);
////            registrationForm.getLoginInfo().setLoginEmail(registrationForm.getUser().getEmail());
////
////            if (!loginService.doesLoginInfoExist(registrationForm.getLoginInfo().getLoginEmail())) {
////                if (registrationForm.getLoginInfo().getLoginPass().equals(registrationForm.getPasswordConfirmation())) {
////                    loginService.registerUser(registrationForm.getLoginInfo(), registrationForm.getUser());
////                    return "redirect:/welcomePage";
////                } else {
////                    throw new PasswordMismatch(
////                            "Passwords do not match",
////                            "Password validation didnt match for email: "
////                                    + registrationForm.getUser().getEmail()
////                    );
////
////             {
////                return "/registerNewProfile";
////            }
////        } catch (AAGilbertException e) {
////            logger.logException(e);
////            throw new AAGilbertException(
////                    "Registration failed for unexpected reason, please try again",
////                    "UNK_001",
////                    "Unexpected error handling registration of a new user",
////                    "GilbertController.postNewProfile") {
////            };
////        }
////    }
//
//    @GetMapping("/loginPage")
//    public String getLoginPage(Model model) {
//        session.invalidate();
//        model.addAttribute("loginInfo", new LoginInfo());
//        return "/loginPage";
//    }
//
//    @PostMapping("/loginPage")
//    public String postLoginPage(@ModelAttribute("loginInfo") LoginInfo loginInfo, Model model) throws IncorrectPasswordException, SQLException {
//        try {
//            if (loginService.checkLogin(loginInfo)) {
//
//                LoginInfo actualFromDb = loginService.getLoginInfo(loginInfo);
//                session.setAttribute("loginInfo", loginInfo);
//
//                User user = userService.getUserByEmail(actualFromDb);
//                if (user == null) {
//                    System.out.println("No user found for login ID: " + loginInfo.getLoginId());
//                    model.addAttribute("error", "User not found.");
//                    return "/loginPage";
//                }
//
//                session.setAttribute("user", user);
//                System.out.println("displayName: " + user.getDisplayName());
//                return "redirect:/productListingPage";
//            } else {
//                throw new IncorrectPasswordException(
//                        "Incorrect password or username",
//                        "Failed login attempt for mail: " + loginInfo.getLoginEmail()
//                );
//            }
//        } catch (AAGilbertException e) {
//            logger.logException(e);
//            throw new AAGilbertException(
//                    "Unexpected error while logging in. Please try again.",
//                    "UNK_002",
//                    "Unexpected error posting login form",
//                    "GilberController.postLoginPage") {
//            };
//        }
//    }
//
//    @GetMapping("/productListingPage") //HOME
//    public String getProductPage() {
//        return "/productListingPage";
//    }
//
//    @GetMapping("/AdminMenu")
//    public String getAdminMenu(Model model) {
//
//            User user = (User) session.getAttribute("user");
//        try {
//            if (!user.getRole().getRoleName().equals("Admin")) {
//                throw new UserNotLoggedInException(
//                        "Access denied. Admin privilege required",
//                        "A user with non admin role attempted admin: " + user.getEmail()
//                );
//            }
//
//            List<User> users = adminService.getAllUsers();
//            List<ProductListing> pendingListings = listingService.getAllPendingProductListings();
//            model.addAttribute("users", users);
//            model.addAttribute("pendingListings", pendingListings);
//            if (user.getRole().getRoleName().equals("Admin")) {
//                return "/AdminMenu";
//            }
//        } catch (AAGilbertException e) {
//            logger.logException(e);
//            throw new AAGilbertException(
//                    "Unexpected error while logging in onto Admin menu. Please try again.",
//                    "UNK_003",
//                    "Unexpected error posting AdminMenu",
//                    "GilbertController.postAdminMenu") {
//            };
//        }
//        return "redirect:/listingView";
//    }
//
//    @GetMapping("/AdminMenu/Approve/{Id}")
//    public String postAdminMenu(@PathVariable("Id") int listingId, Model model) {
//        System.out.println("approving listing: " + listingId);
//        adminService.approveListing(listingId);
//        return "redirect:/AdminMenu";
//    }
//
//    @GetMapping("/AdminMenu/Reject/{Id}")
//    public String postAdminMenuReject(@PathVariable("Id") int listingId, Model model) {
//        System.out.println("rejecting listing: " + listingId);
//        adminService.rejectListing(listingId);
//        return "redirect:/AdminMenu";
//    }
//
//    @GetMapping("/listingView/create")
//    public String showCreateForm(Model model) {
//        try {
//            model.addAttribute("listing", new ProductListing());
//            return "/CreateNewListingForm";
//        } catch (ListingNotFoundException e) {
//            logger.logException(e);
//            throw new ListingNotFoundException(
//                    "There's no listing found with matching data",
//                    "User tried to access a listing but couldn't"
//            );
//        }
//    }
//
//    @PostMapping("/listingView/create")
//    public String postCreateForm(@ModelAttribute("listing") ProductListing listing, Model model) {
//
//        try {
//            User user = (User) session.getAttribute("user");
//            listingService.validateListing(listing, "GilbertController.postCreateForm");
//            System.out.println(user.getUserID());
//            System.out.println(listing.getListingTitle());
//            listing.setSellerID(user.getUserID());
//
//            listingService.create(listing);
//
//            return "redirect:/listingView";
//
//        } catch (DataIntegrityException e) {
//            logger.logException(e);
//            throw new DataIntegrityException(
//                    "Error while creating listing",
//                    "Database error creating listing");
//        }
//    }
//
//    @GetMapping("/listingView")
//        public String showOwnListings (Model model, HttpSession session){
//
//        User user = (User) session.getAttribute("user");
//        try{
//        if (user.getRole().getRoleName().equals("Admin")) {
//            return "redirect:/AdminMenu";
//        }
//        model.addAttribute("user", user);
//        List<ProductListing> listings = listingService.getListingsByUser(user.getUserID());
//        model.addAttribute("listings", listings);
//        return "/listingView";
//    } catch(UserNotLoggedInException e) {
//            logger.logException(e);
//            throw new UserNotLoggedInException(
//                    "You cannot acces this page without logging in",
//                    "User tried to acces without login in session"
//            );
//        }
//    }
//
//    @PostMapping("/listingView/delete/{id}")
//    public String deleteListing(@PathVariable("id") int listingID, HttpSession session, Model model) {
//        try {
//            User user = getLoggedInUser(session);
//            ProductListing listing = listingService.getProductListing(listingID);
//            if (listing == null  ) {
//               throw new ListingNotFoundException(
//                       "The listing you're trying to delete doesnt exist",
//                       "User tried deleting non existing listing: "+ + listingID);
//            }
//            if(listing.getSellerID() != user.getUserID()) {
//                throw new UserNotLoggedInException(
//                        "You're attempting to delete a listing you don't own.",
//                        "User: " + user.getUserID() + " tried deleting a listing they dont own: "+  listingID
//                );
//            }
//            listingService.delete(listingID);
//            return "redirect:/listingView";
//        } catch(DataIntegrityException e) {
//            logger.logException(e);
//            throw new DataIntegrityException(
//                    "There was a database error deleting the listing. Please try again.",
//                    "DB failure upon deleting a listing: " + listingID
//            );
//        }
//    }
//
//    private User getLoggedInUser(HttpSession session) {
//
//            User user = (User) session.getAttribute("user");
//            if (user == null) {
//                throw new UserNotLoggedInException(
//                        "You cannot acces this page as a guest. Please create an account or log in",
//                        "User didnt log in");
//            } return user;
//        }
//    }
package com.example.gilbertecommerce.Controller;

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
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GilbertEcommerceController {


    //Misforstod ideen med at centralisere exception håndteringen, fyldte controlleren med try catch, der kastede exceptions to gange.
    //Tog en ren controller og implementerede exception handling bedre
    private final LoginService loginService;
    private final UserService userService;
    private final HttpSession session;
    private final AdminService adminService;
    private final ProductListingService listingService;
    private final CategoryTagMapService categoryTagMapService;
    private final TagCategoryRepo tagCategoryRepo;
    private final LoggerService logger;

    public GilbertEcommerceController(LoggerService logger, ProductListingService listingService, LoginService loginService, UserService userService, HttpSession session, AdminService adminService, CategoryTagMapService categoryTagMapService, TagCategoryRepo tagCategoryRepo) {
        this.loginService = loginService;
        this.userService = userService;
        this.session = session;
        this.adminService = adminService;
        this.listingService = listingService;
        this.categoryTagMapService = categoryTagMapService;
        this.tagCategoryRepo = tagCategoryRepo;
        this.logger = logger;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/welcomePage";
    }

    @GetMapping("/welcomePage")
    public String getWelcomePage() {
        return "welcomePage";
    }

    @GetMapping("/registerNewProfile")
    public String getNewProfile(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "registerNewProfile";
    }

    @PostMapping("/registerNewProfile")
    public String postNewProfile(@ModelAttribute("registrationForm") RegistrationForm registrationForm) {
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
    public String postLoginPage(@ModelAttribute("loginInfo") LoginInfo loginInfo, Model model) {
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

    @GetMapping("/productListingPage")
    public String getProductPage() {
        return "/productListingPage";
    }

    @GetMapping("/AdminMenu")
    public String getAdminMenu(Model model) {
        User user = loginService.getLoggedInUser(session);

        if (user.getRole() == null || !user.getRole().getRoleName().equalsIgnoreCase("admin")) {
            throw new UserNotLoggedInException(
                    "Access denied. Admin privilege required",
                    "A user with non admin role attempted admin: " + user.getEmail()
            );
        }

        List<User> users = adminService.getAllUsers();
        List<ProductListing> pendingListings = listingService.getAllPendingProductListings();
        model.addAttribute("users", users);
        model.addAttribute("pendingListings", pendingListings);

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

    @GetMapping("/listingView/create")
    public String showCreateForm(Model model) {
        loginService.getLoggedInUser(session);
        model.addAttribute("listing", new ProductListing());
        return "/CreateNewListingForm";
    }

    @PostMapping("/listingView/create")
    public String postCreateForm(@ModelAttribute("listing") ProductListing listing) {
        User user = loginService.getLoggedInUser(session);

        listingService.validateListing(listing, "GilbertController.postCreateForm");
        listing.setSellerID(user.getUserID());
        listingService.create(listing);

        return "redirect:/listingView";
    }

    @GetMapping("/listingView")
    public String showOwnListings(Model model, HttpSession session) {
        User user = loginService.getLoggedInUser(session);

        if (user.getRole() != null && user.getRole().getRoleName().equalsIgnoreCase("admin")) {
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