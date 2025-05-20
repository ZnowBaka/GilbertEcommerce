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
        listing.setProductID(rs.getInt("product_id")); //SKAL TILFØJES I DB
        listing.setListingTitle(rs.getString("title"));
        listing.setListingDescription(rs.getString("description"));
        listing.setListingImage(rs.getString("image")); //SKAL TILFØJES I DB
        listing.setListingStatus(rs.getBoolean("status")); //SKAL TILFØJES I DB
        listing.setPrice(rs.getDouble("price"));

        // Antag at postingDate er gemt som VARCHAR i databasen i ISO-format (yyyy-MM-ddTHH:mm:ss)
        String postingDate = rs.getString("postingDate");
        listing.setListingDate(LocalDateTime.parse(postingDate));

        return listing;
    }
}
