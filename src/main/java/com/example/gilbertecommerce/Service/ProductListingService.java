package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.CustomException.BusinessExceptions.InvalidListingException;
import com.example.gilbertecommerce.CustomException.BusinessExceptions.ListingNotFoundException;
import com.example.gilbertecommerce.Entity.ProductListing;
import com.example.gilbertecommerce.Entity.Tag;
import com.example.gilbertecommerce.Framework.ProductListingRepo;
import com.example.gilbertecommerce.Framework.TagCategoryRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ProductListingService {

    private TagCategoryRepo catRepo;
    private final ProductListingRepo repo;
    private final LoggerService logger;

    public ProductListingService(ProductListingRepo repo, TagCategoryRepo catRepo, LoggerService logger) {
        this.repo = repo;
        this.catRepo = catRepo;
        this.logger = logger;
    }

    public List<ProductListing> getAllListings() {
        return repo.findAllListings();
    }

    public ProductListing getProductListing(int id) {
        ProductListing listing = repo.findById(id);
        if (listing == null) {
            throw new ListingNotFoundException("The listing you're searching for could not be found", "Listing by id in db returned null");
        }
        return listing;
    }

    public void create(ProductListing listing) {
        validateListing(listing, "create");
        repo.save(listing);
    }

    public void insertTags(List<Tag> tags, String listingTitle, int userId) {
        System.out.println(2);
        ;
        int listingId = repo.findResentListing(userId, listingTitle);
        System.out.println(3);
        if (tags.isEmpty()) {
            System.out.println("scuffed");
        }
        for (Tag tag : tags) {
            System.out.println(tag.getId());
            catRepo.addTagToListing(tag.getId(), listingId);
        }

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

        if (productListing.getListingTitle() == null || productListing.getListingTitle().isEmpty()) {
            throw new InvalidListingException("The title of your listing is empty, but must be provided.", "User tried to create listing without title", "title", "ProductListingRepo");
        }

        if (productListing.getListingDescription() == null || productListing.getListingDescription().isEmpty()) {
            throw new InvalidListingException("The description of your listing is empty, but must be provided.", "User tried to create listing without description", "description", "ProductListingRepo");
        }

        if (productListing.getPrice() <= 0) {
            throw new InvalidListingException("The price of your listing is less than or equal to zero.", "User tried to create listing without price", "price", "ProductListingRepo");
        }
    }

    public List<ProductListing> getAllPendingProductListings() {
        return repo.getAllPendingProductListings();
    }

    public List<ProductListing> getAllApprovedListings() {
        return repo.getAllApprovedFromDB();
    }

    public List<ProductListing> getAllFeaturedListings() {
        return repo.getAllFeaturedFromDB();
    }

    public void updateFeatureStatus(int id, Boolean status) {
        repo.updateFeatureStatus(id, status);
    }

    public void getListingOwnerNameByListingId(List<ProductListing> listings) {
        for (ProductListing productListing : listings) {
            int Owner_ID = productListing.getSellerID();
            String ownerName = repo.getOwnerNameByListingSellerID(Owner_ID);
            productListing.setDisplayName(ownerName);
        }
    }

    public List<ProductListing> getProductListingsHeaderSearch(List<ProductListing> productListings, String value) {
        if (value == null || value.isEmpty()) {
            List<ProductListing> originalListings = new ArrayList<>();
            originalListings = productListings;
            return originalListings;
        }

        List<ProductListing> headerProductListings = new ArrayList<>();
        List<Tag> productTags = new ArrayList<>();

        for (ProductListing productListing : productListings) {
            productTags = productListing.getTags();

            for (Tag productTag : productTags) {
                if (productTag.getTagValue().equalsIgnoreCase(value)) {
                    headerProductListings.add(productListing);
                }
            }

        }
        return headerProductListings;
    }


}

