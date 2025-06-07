package com.example.gilbertecommerce.Framework;

import com.example.gilbertecommerce.CustomException.BusinessExceptions.ListingNotFoundException;
import com.example.gilbertecommerce.CustomException.DatabaseExceptionS.DataIntegrityException;
import com.example.gilbertecommerce.Entity.ProductListing;
import com.example.gilbertecommerce.Entity.ProductListingMapper;
import com.example.gilbertecommerce.Service.LoggerService;
import com.example.gilbertecommerce.Service.ProductListingService;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductListingRepo {

    private JdbcTemplate jdbcTemplate;
    private final LoggerService logger;

    public ProductListingRepo(JdbcTemplate jdbcTemplate, LoggerService logger) {
        this.jdbcTemplate = jdbcTemplate;
        this.logger = logger;
    }


    public List<ProductListing> findAllListings() {
        try {
            String sql = "select * from Listings";
            return jdbcTemplate.query(sql, new ProductListingMapper());
        } catch (DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Database error when fetching product listings",
                    "DB error when fetching all product listings"
            );
            logger.logException(ex);
            throw ex;
        }
    }

    public ProductListing findById(int id) {
        try{
        String sql = "select * from Listings where listing_id = ?";
        return jdbcTemplate.queryForObject(sql, new ProductListingMapper(), id);
    }catch (EmptyResultDataAccessException e){
            ListingNotFoundException ex = new ListingNotFoundException(
                    "No listing found with id " + id,
                    "Attempt at finding a non existing listing with id " + id
            );
            logger.logException(ex);
            throw ex;
        } catch (DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Database error when fetching product listing",
                    "Error retrieving product listing with ID: " + id
            );
            logger.logException(ex);
            throw ex;
        }
    }

    @Transactional
    public void save(ProductListing productListing) {
        try {
            String sql = "insert into Listings (owner_id, title, description, postingDate, price) values (?,?,?,?,?)";
            int rowsAffected = jdbcTemplate.update(sql,
                    productListing.getSellerID(),
                    productListing.getListingTitle(),
                    productListing.getListingDescription(),
                    productListing.getListingDate(),
                    productListing.getPrice());
            if(rowsAffected == 0){
                throw new DataIntegrityException(
                        "Unsuccessful attempt at saving product listing in database",
                        "Database error when saving product listing, no rows affected" + productListing.getListingID()
                );
            }
        } catch (DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Unsuccessful attempt at saving the listing in database",
                    "Database error when saving product listing with ID: " + productListing.getListingID()
            );
            logger.logException(ex);
            throw ex;
        }
    }

    public void update(ProductListing productListing) {
        try {
            String sql = "update Listings set title =?, description =?, postingDate =?, price=? where listing_id = ?";
            int rowsAffected = jdbcTemplate.update(sql,
                    productListing.getListingTitle(),
                    productListing.getListingDescription(),
                    productListing.getListingDate(),
                    productListing.getPrice(),
                    productListing.getListingID());

            if(rowsAffected == 0){
                throw new ListingNotFoundException(
                        "No listing to update with id " + productListing.getListingID(),
                        "Potentially unsuccessful attempt at editing a listing. No rows affected" + productListing.getListingID()
                );
            }
        } catch(DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Database error when updating product listing",
                    "Unsuccessful attempt at editing a listing with ID: " + productListing.getListingID()
            );
            logger.logException(ex);
            throw ex;
        }
    }

    @Transactional
    public void delete(int id) {
        try {
            String sql = "delete from Listings where listing_id = ?";
            int rowsAffected = jdbcTemplate.update(sql, id);
            if (rowsAffected == 0) {
                ListingNotFoundException ex = new ListingNotFoundException(
                        "No listing to delete with ID: " + id,
                        "Attempt at deleting a listing with ID: " + id
                );
                logger.logException(ex);
                throw ex;
            }
        } catch (DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Database error when deleting product listing",
                    "Error deleting product listing with ID: " + id
            );
            logger.logException(ex);
            throw ex;
        }
    }


    public List<ProductListing> getListingsByUserId(int userId) {
        try {
            String sql = "SELECT * FROM Listings WHERE owner_id = ?";
            return jdbcTemplate.query(sql, new ProductListingMapper(), userId);
        } catch (DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Database error when fetching all product listings",
                    "Database error when querying for all listings from user: " + userId
            );
            logger.logException(ex);
            throw ex;
        }
    }

    public List<ProductListing> getAllPendingProductListings() {
        try {
            String sql = "select * from Listings where Status = 'pending'";
            return jdbcTemplate.query(sql, new ProductListingMapper());
        } catch (DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Database error when fetching all pending product listings",
                    "Could not query for all pending product listings"
            );
            logger.logException(ex);
            throw ex;
        }
    }

    public void updateStatus(int id, String status) {
        try{
        String sql = "update Listings set Status = ? where listing_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, status, id);

        if(rowsAffected == 0){
            ListingNotFoundException ex = new ListingNotFoundException(
                    "No listing to update status on with ID: " + id,
                    "Error updating listings' status with ID: " + id
            );
            logger.logException(ex);
            throw ex;
        }
    }catch (DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Unsuccessful attempt at updating status with ID: " + id,
                    "Database error when updating status on with ID: " + id
            );
            logger.logException(ex);
            throw ex;
        }
    }
    public int findResentListing(int userId, String listingTitle) {
        String sql = "select listing_id from Listings where owner_id = ? and title = ? and status = 'pending' limit 1";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, listingTitle);
    }

    public List<ProductListing> getAllApprovedFromDB(){
        String sql = "select * from Listings where Status = 'approved'";
        return jdbcTemplate.query(sql, new ProductListingMapper());
    }
    public List<ProductListing> getAllFeaturedFromDB(){
        String sql = "select * from Listings where FeatureStatus = true";
        return jdbcTemplate.query(sql, new ProductListingMapper());
    }
    public void updateFeatureStatus(int id, boolean status) {
        try{
            String sql = "update Listings set FeatureStatus = ? where listing_id = ?";
            if (status){
                int rowsAffected = jdbcTemplate.update(sql, 1, id);
            } else{
                int rowsAffected = jdbcTemplate.update(sql, 0, id);
            }

        } catch (DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Unsuccessful attempt at updating feature status with ID: " + id,
                    "Database error when updating feature status on with ID: " + id
            );
            logger.logException(ex);
            throw ex;
        }
    }
}