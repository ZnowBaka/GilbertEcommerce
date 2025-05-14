package com.example.gilbertecommerce.Controller;

import com.example.gilbertecommerce.CustomException.IncorrectPasswordException;
import com.example.gilbertecommerce.Entity.LoginInfo;
import com.example.gilbertecommerce.Entity.RegistrationForm;
import com.example.gilbertecommerce.Entity.User;
import com.example.gilbertecommerce.Service.LoginService;
import com.example.gilbertecommerce.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;
    private final HttpSession session;

    public LoginController(LoginService loginService, UserService userService, HttpSession session) {
        this.loginService = loginService;
        this.userService = userService;
        this.session = session;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("registrationFrom", new RegistrationForm());
        return "redirect:/registerNewProfile";
    }

    @PostMapping("/registerNewProfile")
    public String postNewProfile(@ModelAttribute("registrationForm") RegistrationForm registrationForm, Model model) {
        if (!loginService.doesLoginInfoExist(registrationForm.getLoginInfo().getLoginName())) {
            loginService.registerUser(registrationForm.getLoginInfo(), registrationForm.getUser());
            return "redirect:/login";
        } else {
            model.addAttribute("error", "User already exists");
            return "/registerNewProfile";
        }
    }
}
//    @GetMapping("/loginPage")
//    public String loginPage(Model model) {
//        session.invalidate();
//        model.addAttribute("user", new User());
//        return "/loginPage";
//    }
//
//    @PostMapping("/loginPage")
//    public String postLoginPage(@ModelAttribute("user") User user, Model model) throws IncorrectPasswordException {
//        User loggedInUser = userService.loginUser(user);
//        if (loggedInUser != null) {
//
//            session.setAttribute("user", loggedInUser);
//            session.setAttribute("profile", profileService.getProfileById((User) session.getAttribute("user")));
//            return "redirect:/frontPage";
//        } else if (loggedInUser == null) {
//            model.addAttribute("error", "Incorrect username or password");
//            return "/loginPage";
//        }
//        return "/loginPage";
//    }
//}
