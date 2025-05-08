package com.example.gilbertecommerce.Framework;



import com.example.gilbertecommerce.CustomException.IncorrectPasswordException;
import com.example.gilbertecommerce.CustomException.UserAlreadyExistException;
import com.example.gilbertecommerce.CustomException.UserDoesNotExistException;
import com.example.gilbertecommerce.CustomException.UserNameAlreadyExistException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IncorrectPasswordException.class)
    public String incorrectPassword(Model model, IncorrectPasswordException e) {
        model.addAttribute("ErrorMessage", e.getMessage());
        return "incorrectPassword"; // This should return to where-ever the error happened
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public String incorrectPassword(Model model, UserAlreadyExistException e) {
        model.addAttribute("ErrorMessage", e.getMessage());
        return "userAlreadyExist"; // This should return to where-ever the error happened
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public String incorrectPassword(Model model, UserDoesNotExistException e) {
        model.addAttribute("ErrorMessage", e.getMessage());
        return "userDoesNotExist"; // This should return to where-ever the error happened
    }

    @ExceptionHandler(UserNameAlreadyExistException.class)
    public String incorrectPassword(Model model, UserNameAlreadyExistException e) {
        model.addAttribute("ErrorMessage", e.getMessage());
        return "userNameAlreadyExist"; // This should return to where-ever the error happened
    }









}
