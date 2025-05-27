package com.example.gilbertecommerce.Framework;



import com.example.gilbertecommerce.CustomException.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IncorrectPasswordException.class)
    public String handleIncorrectPassword(Model model, IncorrectPasswordException e) {
        model.addAttribute("error", e.getMessage());
        return "/loginPage"; // This should return to where-ever the error happened
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public String handleUserAlreadyExist(Model model, UserAlreadyExistException e) {
        model.addAttribute("error", e.getMessage());
        return "/registerNewProfile"; // This should return to where-ever the error happened
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public String handleUserDoesNotExist(Model model, UserDoesNotExistException e) {
        model.addAttribute("ErrorMessage", e.getMessage());
        return "userDoesNotExist"; // This should return to where-ever the error happened
    }

    @ExceptionHandler(UserNameAlreadyExistException.class)
    public String handleIncorrectPassword(Model model, UserNameAlreadyExistException e) {
        model.addAttribute("ErrorMessage", e.getMessage());
        return "userNameAlreadyExist"; // This should return to where-ever the error happened
    }

    @ExceptionHandler(ListingNotFoundException.class)
    public String handleListingNotFound(Model model, ListingNotFoundException e) {
        model.addAttribute("ErrorMessage", e.getMessage());
        return ""; //Tilføje siden, hvor fejlen skete, så vi forbliver på samme
    }

    @ExceptionHandler(InvalidListingException.class)
    public String handleInvalidListing(Model model, InvalidListingException e) {
        model.addAttribute("ErrorMessage", e.getMessage());
        model.addAttribute("errorField", e.getField());
        model.addAttribute("source", e.getSource());
        return e.getSource().equals("update") ? "editListingForm" : "createListingForm"; //Defineret else if
    }

    @ExceptionHandler(UserNotLoggedIn.class)
    public String handleUserNotLoggedIn(UserNotLoggedIn e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("ErrorMessage", e.getMessage()); //Implementerede med model i starten, men model er kun "live" i den nuværende request. Flash sikrer den kun er aktiv i et request
        return "redirect:/loginPage";
    }
}
