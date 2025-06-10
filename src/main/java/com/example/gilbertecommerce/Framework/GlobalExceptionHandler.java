package com.example.gilbertecommerce.Framework;

import com.example.gilbertecommerce.CustomException.*;
import com.example.gilbertecommerce.CustomException.AuthenticationException.*;
import com.example.gilbertecommerce.CustomException.BusinessExceptions.InvalidListingException;
import com.example.gilbertecommerce.CustomException.BusinessExceptions.ListingNotFoundException;
import com.example.gilbertecommerce.CustomException.DatabaseExceptionS.DataIntegrityException;
import com.example.gilbertecommerce.CustomException.DatabaseExceptionS.DatabaseConnectionException;
import com.example.gilbertecommerce.CustomException.ValidationExceptions.EmptyFieldException;
import com.example.gilbertecommerce.CustomException.ValidationExceptions.EmptyPasswordException;
import com.example.gilbertecommerce.CustomException.ValidationExceptions.PasswordMismatch;
import com.example.gilbertecommerce.CustomException.ValidationExceptions.WeakPasswordException;
import com.example.gilbertecommerce.Service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private LoggerService logger;

    @ExceptionHandler(EmptyFieldException.class)
    public String handleEmptyFieldException(EmptyFieldException e,  RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/registerNewProfile";
    }

    @ExceptionHandler(EmptyPasswordException.class)
    public String handleEmptyPasswordException(EmptyPasswordException e, RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/registerNewProfile";
    }

    @ExceptionHandler(WeakPasswordException.class)
    public String handleWeakPasswordException(WeakPasswordException e, RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/registerNewProfile";
    }

    @ExceptionHandler(PasswordMismatch.class)
    public String handlePasswordMismatchException(PasswordMismatch e, RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/loginPage";
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public String handleIncorrectPasswordException(IncorrectPasswordException e, RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/loginPage";
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public String handleUserAlreadyExistException(UserAlreadyExistException e, RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/registerNewProfile";
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public String handleUserDoesNotExistException(UserDoesNotExistException e, RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/loginPage";
    }

    @ExceptionHandler(UserNotLoggedInException.class)
    public String handleException(UserNotLoggedInException e, RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/registerNewProfile";
    }

    @ExceptionHandler(InvalidEmailException.class)
    public String handleException(InvalidEmailException e, RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/loginPage";
    }

    //Standard exception. Hierakiet går defineret ex -> Standard -> catch all
    @ExceptionHandler(AAGilbertException.class)
    public String handleException(AAGilbertException e, RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/error/systemError";
    }

    @ExceptionHandler(DatabaseConnectionException.class)
    public String handleDatabaseConnectionException(DatabaseConnectionException e, RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/error/systemError";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityException(DataIntegrityException e, RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/error/systemError";
    }

    @ExceptionHandler(InvalidListingException.class)
    public String handleException(InvalidListingException e, RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/CreateNewListingForm";
    }

    @ExceptionHandler(ListingNotFoundException.class)
    public String handleException(ListingNotFoundException e, RedirectAttributes model) {
        logger.logException(e);
        model.addFlashAttribute("error", e.getMessage());
        model.addFlashAttribute("errorCode", e.getErrorCode());
        return "redirect:/productListingPage";
    }
}