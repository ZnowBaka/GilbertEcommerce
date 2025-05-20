package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.CustomException.InvalidListingException;
import com.example.gilbertecommerce.CustomException.ListingNotFoundException;
import com.example.gilbertecommerce.Entity.ProductListing;
import com.example.gilbertecommerce.Framework.ProductListingRepo;
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
        validateListing(listing);
        repo.save(listing);
    }

    public void update(int id, ProductListing listing) {
        validateListing(listing);
        repo.update(listing);
    }

    public void delete(int id) {
        repo.delete(id);
    }

    public void validateListing(ProductListing productListing) {

        if(productListing.getListingTitle() == null || productListing.getListingTitle().isEmpty()) {
            throw new InvalidListingException("The title of your listing is empty, but must be provided.", "title", "create");
        }

        if(productListing.getListingDescription() == null || productListing.getListingDescription().isEmpty()) {
            throw new InvalidListingException("The description of your listing is empty, but must be provided.", "description", "create");
        }

        if(productListing.getPrice() <= 0){
            throw new InvalidListingException("The price of your listing is less than or equal to zero.", "price", "create");
        }
    }

}
