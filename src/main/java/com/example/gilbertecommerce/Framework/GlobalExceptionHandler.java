package com.example.gilbertecommerce.Framework;

import com.example.gilbertecommerce.CustomException.*;
import com.example.gilbertecommerce.CustomException.AuthenticationException.*;
import com.example.gilbertecommerce.CustomException.DatabaseExceptionS.DatabaseConnectionException;
import com.example.gilbertecommerce.CustomException.ValidationExceptions.EmptyFieldException;
import com.example.gilbertecommerce.CustomException.ValidationExceptions.EmptyPasswordException;
import com.example.gilbertecommerce.CustomException.ValidationExceptions.PasswordMismatch;
import com.example.gilbertecommerce.CustomException.ValidationExceptions.WeakPasswordException;
import com.example.gilbertecommerce.Service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private LoggerService logger;


    @ExceptionHandler(EmptyFieldException.class)
    public String handleEmptyFieldException(EmptyFieldException e, Model model) {
        logger.logException(e);
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorCode", e.getErrorCode());
        return "redirect:/registerNewProfile";
    }

    @ExceptionHandler(EmptyPasswordException.class)
    public String handleEmptyPasswordException(EmptyPasswordException e, Model model) {
        logger.logException(e);
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorCode", e.getErrorCode());
        return "redirect:/registerNewProfile";
    }

    @ExceptionHandler(WeakPasswordException.class)
    public String handleWeakPasswordException(WeakPasswordException e, Model model) {
        logger.logException(e);
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorCode", e.getErrorCode());
        return "redirect:/registerNewProfile";
    }

    @ExceptionHandler(PasswordMismatch.class)
    public String handlePasswordMismatchException(PasswordMismatch e, Model model) {
        logger.logException(e);
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorCode", e.getErrorCode());
        return "redirect:/registerNewProfile";
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public String handleIncorrectPasswordException(IncorrectPasswordException e, Model model) {
        logger.logException(e);
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorCode", e.getErrorCode());
        return "redirect:/registerNewProfile";
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public String handleUserAlreadyExistException(UserAlreadyExistException e, Model model) {
        logger.logException(e);
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorCode", e.getErrorCode());
        return "redirect:/registerNewProfile";
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public String handleUserDoesNotExistException(UserDoesNotExistException e, Model model) {
        logger.logException(e);
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorCode", e.getErrorCode());
        return "redirect:/registerNewProfile";
    }

    @ExceptionHandler(UserNotLoggedInException.class)
    public String handleException(UserNotLoggedInException e, Model model) {
        logger.logException(e);
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorCode", e.getErrorCode());
        return "redirect:/registerNewProfile";
    }

    @ExceptionHandler(InvalidEmailException.class)
    public String handleException(InvalidEmailException e, Model model) {
        logger.logException(e);
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorCode", e.getErrorCode());
        return "redirect:/loginPage";
    }

    @ExceptionHandler(DatabaseConnectionException.class)
    public String handleException(DatabaseConnectionException e, Model model) {
        logger.logException(e);
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorCode", e.getErrorCode());
        return "error/systemError";
    }

    @ExceptionHandler(AAGilbertException.class)
    public String handleException(AAGilbertException e, Model model) {
        logger.logException(e);
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorCode", e.getErrorCode());
        return "error/generalError";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        AAGilbertException wrapperException = new AAGilbertException("Unexpected system error", "SYS_001",
                "Unhandled exception: " + e.getMessage(), "SystemLevel") {
        };
        logger.logException(wrapperException, "Unhandled exception caught");
        model.addAttribute("error", e.getMessage());
        model.addAttribute("errorCode", wrapperException.getErrorCode());
        return "error/systemError";
    }
}