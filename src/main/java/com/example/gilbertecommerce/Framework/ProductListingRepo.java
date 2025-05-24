package com.example.gilbertecommerce.Framework;

import com.example.gilbertecommerce.Entity.ProductListing;
import com.example.gilbertecommerce.Entity.ProductListingMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductListingRepo {

    private JdbcTemplate jdbcTemplate;

    public ProductListingRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<ProductListing> findAllListings() {
        String sql = "select * from Listings";
        return jdbcTemplate.query(sql, new ProductListingMapper());
    }

    public ProductListing findById(int id) {
        String sql = "select * from Listings where listing_id = ?";
        return jdbcTemplate.queryForObject(sql, new ProductListingMapper(), id);
    }

    public void save(ProductListing productListing) {
        String sql = "insert into Listings (owner_id, title, description, postingDate, price) values (?,?,?,?,?)";
        jdbcTemplate.update(sql,
                productListing.getSellerID(),
                productListing.getListingTitle(),
                productListing.getListingDescription(),
                productListing.getListingDate(),
                productListing.getPrice());
    }

    public void update(ProductListing productListing) {
        String sql = "update Listings set title =?, description =?, postingDate =?, price=? where listing_id = ?";
        jdbcTemplate.update(sql,
                productListing.getListingTitle(),
                productListing.getListingDescription(),
                productListing.getListingDate(),
                productListing.getPrice(),
                productListing.getListingID());
    }

    public void delete(int id) {
        String sql = "delete from Listings where listing_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<ProductListing> getListingsByUserId(int userId) {
        String sql = "SELECT * FROM Listings WHERE owner_id = ?";
        return jdbcTemplate.query(sql, new ProductListingMapper(), userId);
    }
    public List<ProductListing> getAllPendingProductListings() {
        String sql = "select * from Listings where Status = 'pending'";
        return jdbcTemplate.query(sql, new ProductListingMapper());
    }
}