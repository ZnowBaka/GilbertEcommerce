package com.example.gilbertecommerce.Controller;

import com.example.gilbertecommerce.Entity.ProductListing;
import com.example.gilbertecommerce.Entity.User;
import com.example.gilbertecommerce.Service.ProductListingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProductListingController {

    private final ProductListingService listingService;

    public ProductListingController(ProductListingService listingService) {
        this.listingService = listingService;
    }

    private User getLoggedInUserOrRedirect(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "You cannot access this page as a guest user.");
            return null;
        }
        return user;
    }

    @GetMapping("/listingView")
    public String showOwnListings(Model model, HttpSession session) {

        User user = getLoggedInUserOrRedirect(session, model);
        if(user == null) {
            return "redirect:/loginPage";
        }
        List<ProductListing> listings = listingService.getListingsByUser(user.getUserID());
        model.addAttribute("listings", listings);
        return "listingView";
    }

    @GetMapping("/listingView/create")
    public String showCreateForm(Model model) {

        model.addAttribute("listings", new ProductListing());
        return "createListingView";
    }

    @PostMapping("/create")
    public String createListing(@ModelAttribute("listings") ProductListing listing, HttpSession session, Model model) {

        User user = getLoggedInUserOrRedirect(session, model);
        if(user == null) {
            return "redirect:/loginPage";
        }
        listing.setSellerID(user.getUserID());
        listingService.create(listing);
        return "redirect:/listingView";
    }

    @PostMapping("/listingView/delete/{id}")
    public String deleteListing(@PathVariable("id") int listingID, HttpSession session, Model model) {

        User user = getLoggedInUserOrRedirect(session, model);
        if(user == null) {
            return "redirect:/loginPage"; //Burde måske bare laves som exception, der redirecter fra exceptionhandleren
        }
        ProductListing listing = listingService.getProductListing(listingID);
        if(listing != null && listing.getSellerID() == user.getUserID()) {
            listingService.delete(listingID);
        }
        return "redirect:/listingView";
    }
}
