package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.Entity.ProductListing;
import com.example.gilbertecommerce.Framework.ProductListingRepo;

import java.util.List;

public class ProductListingService {

    private ProductListingRepo repo;


    public ProductListingService(ProductListingRepo repo) {
        this.repo = repo;
    }

    public List<ProductListing> getAllListings() {
        return repo.findAllListings();
    }

    public ProductListing getProductListing(int id) {
        return repo.findById(id);
    }

    public void create(ProductListing listing) {
        repo.save(listing);
    }

    public void update(int id, ProductListing listing) {
        repo.update(listing);
    }

    public void delete(int id) {
        repo.delete(id);
    }
}
