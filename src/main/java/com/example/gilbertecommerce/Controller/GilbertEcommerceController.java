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

import java.sql.SQLException;

@Controller
public class GilbertEcommerceController {

    private final LoginService loginService;
    private final UserService userService;
    private final HttpSession session;

    public GilbertEcommerceController(LoginService loginService, UserService userService, HttpSession session) {
        this.loginService = loginService;
        this.userService = userService;
        this.session = session;
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

        if (loginService.doesLoginInfoExist(registrationForm.getLoginInfo().getLoginName())) {
            loginService.registerUser(registrationForm.getLoginInfo(), registrationForm.getUser());
            return "redirect:/welcomePage";
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
        session.setAttribute("loginInfo" , (LoginInfo) loginInfo);
        session.setAttribute("user", (User)userService.getUserById(loginInfo.getLoginId()));
        LoginInfo loginInfo1 = (LoginInfo) session.getAttribute("loginInfo");
        User user = (User) session.getAttribute("user");
        System.out.println("displayName" + user.getDisplayName());
        System.out.println("loginInfo" + loginInfo1.getLoginName());
        return "redirect:/welcomePage";
    } else {
        model.addAttribute("error", "Incorrect username or password");
        return "/loginPage";
    }
}
}


