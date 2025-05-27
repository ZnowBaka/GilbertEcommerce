package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.CustomException.InvalidListingException;
import com.example.gilbertecommerce.CustomException.ListingNotFoundException;
import com.example.gilbertecommerce.CustomException.UserNotLoggedIn;
import com.example.gilbertecommerce.Entity.ProductListing;
import com.example.gilbertecommerce.Entity.ProductListingMapper;
import com.example.gilbertecommerce.Entity.User;
import com.example.gilbertecommerce.Framework.ProductListingRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductListingService {

    private ProductListingRepo repo;

    public ProductListingService(ProductListingRepo repo) {
        this.repo = repo;
    }

    public List<ProductListing> getAllListings() {
        return repo.findAllListings();
    }

    public ProductListing getProductListing(int id) {
       ProductListing listing = repo.findById(id);
       if (listing == null) {
           throw new ListingNotFoundException("The listing you're searching for could not be found");
       }
        return listing;
    }

    public void create(ProductListing listing) {
        validateListing(listing, "create");
        repo.save(listing);
    }

    public void update(int id, ProductListing listing) {
        validateListing(listing, "update");
        repo.update(listing);
    }

    public List<ProductListing> getListingsByUser(int userId) {
        return repo.getListingsByUserId(userId);
    }

    public void delete(int id) {
        repo.delete(id);
    }

    public void validateListing(ProductListing productListing, String source) {

        if(productListing.getListingTitle() == null || productListing.getListingTitle().isEmpty()) {
            throw new InvalidListingException("The title of your listing is empty, but must be provided.", "title", source);
        }

        if(productListing.getListingDescription() == null || productListing.getListingDescription().isEmpty()) {
            throw new InvalidListingException("The description of your listing is empty, but must be provided.", "description", source);
        }

        if(productListing.getPrice() <= 0){
            throw new InvalidListingException("The price of your listing is less than or equal to zero.", "price", source);
        }
    }
    public List<ProductListing> getAllPendingProductListings() {
        return repo.getAllPendingProductListings();
    }
    }

