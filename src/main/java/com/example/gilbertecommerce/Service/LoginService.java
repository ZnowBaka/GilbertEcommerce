package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.CustomException.*;
import com.example.gilbertecommerce.CustomException.AuthenticationException.UserAlreadyExistException;
import com.example.gilbertecommerce.CustomException.AuthenticationException.UserDoesNotExistException;
import com.example.gilbertecommerce.CustomException.AuthenticationException.UserNotLoggedInException;
import com.example.gilbertecommerce.CustomException.DatabaseExceptionS.DataIntegrityException;
import com.example.gilbertecommerce.CustomException.ValidationExceptions.EmptyFieldException;
import com.example.gilbertecommerce.CustomException.ValidationExceptions.EmptyPasswordException;
import com.example.gilbertecommerce.CustomException.ValidationExceptions.PasswordMismatch;
import com.example.gilbertecommerce.CustomException.ValidationExceptions.WeakPasswordException;
import com.example.gilbertecommerce.Entity.LoginInfo;
import com.example.gilbertecommerce.Entity.RegistrationForm;
import com.example.gilbertecommerce.Entity.User;
import com.example.gilbertecommerce.Framework.SecurityConfig;
import com.example.gilbertecommerce.Framework.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

    @Service
public class LoginService {
    private final UserRepo userRepo;
    private final SecurityConfig securityConfig;
    private final LoggerService logger;

    public LoginService(UserRepo userRepo, SecurityConfig securityConfig, LoggerService logger) {
        this.userRepo = userRepo;
        this.securityConfig = securityConfig;
        this.logger =  logger;

    }
    public boolean checkLogin(LoginInfo loginInfo) {
        LoginInfo loginInfo1 = userRepo.getLoginInfo(loginInfo);
        System.out.println(loginInfo1.getLoginPass());
        System.out.println(loginInfo1.getLoginEmail());
        String hashed = loginInfo1.getLoginPass();
        if(securityConfig.passwordEncoder().matches(loginInfo.getLoginPass(), hashed)){
            return true;
        }
        return false;
    }

    public LoginInfo getLoginInfo(LoginInfo loginInfo) {
        try{
            return userRepo.getLoginInfo(loginInfo);
        } catch (UserDoesNotExistException e) {
            logger.logException(e);
            throw e;
        }
    }


    public boolean registerUser(LoginInfo loginInfo, User user) {
        //hashing password
        loginInfo.setLoginPass(securityConfig.passwordEncoder().encode(loginInfo.getLoginPass()));
        System.out.println("testing registerUser");
        userRepo.createNewUser(loginInfo, user);
        return true;
    }
    public boolean doesLoginInfoExist(String loginName) {
        try{
            return userRepo.doesLoginInfoExist(loginName);

        } catch (UserAlreadyExistException e) {
            logger.logException(e);
            throw e;
        } catch (AAGilbertException e){
            logger.logException(e, "Unexpected error checking login info");
            throw new AAGilbertException("Unexpected error checking login info", "LOGIN_002", e.getMessage() ,"System level") {
            };
        }
    }

    public void validateRegistrationForm(RegistrationForm rForm) {
        try {
            validateUser(rForm.getUser());
            validatePassword(rForm.getLoginInfo().getLoginPass(), rForm.getPasswordConfirmation());
            validateLoginInfo(rForm.getLoginInfo());
        } catch (DataAccessException e){
            throw new DataIntegrityException(
                    "Unexpected database failure while fetching user info",
                    "No empty fields, unexpected data error");
        }
    }

    public void validateLoginInfo(LoginInfo loginInfo){
        if(loginInfo.getLoginEmail() == null || loginInfo.getLoginEmail().isEmpty()) {
            throw new EmptyFieldException("The email is required for a successful registration", "User tried registration without an email");
        }
        if(loginInfo.getLoginPass() == null || loginInfo.getLoginPass().isEmpty()) {
            throw new EmptyFieldException("The password is required for a successful registration", "User tried registration without a password");
        }
    }

    public void validateUser(User user) {
        if(user.getFirstName() == null || user.getFirstName().isEmpty()) {
            throw new EmptyFieldException("The first name is required for a successful registration", "User tried registration without a first name");
        }
        if(user.getLastName() == null || user.getLastName().isEmpty()) {
            throw new EmptyFieldException("The last name is required for a successful registration", "User tried registration without a last name");
        }
        if(user.getDisplayName() == null || user.getDisplayName().isEmpty()) {
            throw new EmptyFieldException("Please select a display name, it is required for a successful registration", "User tried registration without a display name");
        }
    }

    public void validatePassword(String password, String passwordConfirmation){
        if(password == null || password.isEmpty()) {
            throw new EmptyPasswordException("Password is required for a successful registration", "User tried registration without a password");
        }
        if(password.length() < 6) {
            throw new WeakPasswordException("Password must exceed length of 6 characters", "User put in too short password");
        }
        if (password.length() > 24 ){
            throw new WeakPasswordException("Password cannot be over 24 characters", "User put in too long password");
        }
        if(!(password.equals(passwordConfirmation))){
            throw new PasswordMismatch("Passwords do not match", "No match between password and control field");
        }
    }
    public User getLoggedInUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new UserNotLoggedInException(
                    "You cannot access this page as a guest. Please create an account or log in",
                    "User attempted to access protected resource without login"
            );
        }
        return user;
    }
}

