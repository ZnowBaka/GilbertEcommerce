package com.example.gilbertecommerce.Entity;

import com.example.gilbertecommerce.Entity.ProductListing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ProductListingMapper implements org.springframework.jdbc.core.RowMapper<ProductListing> {

    @Override
    public ProductListing mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductListing listing = new ProductListing();

        listing.setListingID(rs.getInt("listing_id"));
        listing.setSellerID(rs.getInt("owner_id"));
        listing.setListingTitle(rs.getString("title"));
        listing.setListingDescription(rs.getString("description"));
        //String postingDate = rs.getString("postingDate");
        listing.setPrice(rs.getDouble("price"));
        //listing.setListingImage(rs.getString("image")); //SKAL TILFØJES I DB
        listing.setListingStatus(rs.getString("Status")); //SKAL TILFØJES I DB
        //listing.setListingDate(LocalDateTime.parse(postingDate));

        return listing;
    }
}
