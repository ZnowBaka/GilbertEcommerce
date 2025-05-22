package com.example.gilbertecommerce.Controller;

import com.example.gilbertecommerce.CustomException.IncorrectPasswordException;
import com.example.gilbertecommerce.Entity.LoginInfo;
import com.example.gilbertecommerce.Entity.RegistrationForm;
import com.example.gilbertecommerce.Entity.User;
import com.example.gilbertecommerce.Service.AdminService;
import com.example.gilbertecommerce.Service.LoginService;
import com.example.gilbertecommerce.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.SQLException;
import java.util.List;

@Controller
public class GilbertEcommerceController {

    private final LoginService loginService;
    private final UserService userService;
    private final HttpSession session;
    private final AdminService adminService;

    public GilbertEcommerceController(LoginService loginService, UserService userService, HttpSession session, AdminService adminService) {
        this.loginService = loginService;
        this.userService = userService;
        this.session = session;
        this.adminService = adminService;
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

            User user = userService.getUserById(actualFromDb);
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

    @GetMapping("/productListingPage")
    public String getProductPage() {
        return "/productListingPage";
    }

    @GetMapping("/AdminMenu")
    public String getAdminMenu(Model model) {
        User user = (User) session.getAttribute("user");
            List<User> users = adminService.getAllUsers();
            model.addAttribute("users", users);
            return "/AdminMenu";
    }
    @GetMapping("/ProfileView")
    public String getProfileView(Model model) {
        User user = (User) session.getAttribute("user");
        if (user.getRole().getRoleName().equals("Admin")) {
            return "redirect:/AdminMenu";
        }
        model.addAttribute("user", user);
        return "/ProfileView";
    }
}