package com.example.gilbertecommerce.Entity;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ProductListing {

    private int listingID;
    private double price;
    private int sellerID;
    private int productID;
    private LocalDateTime listingDate;


    public ProductListing() {}

    public ProductListing(int listingID, double price, int sellerID, int productID, LocalDateTime listingDate) {
        this.listingID = listingID;
        this.price = price;
        this.sellerID = sellerID;
        this.productID = productID;
        this.listingDate = listingDate;
    }

    public ProductListing(int listingID, double price, int sellerID, int productID, long epochMillis) {
        this.listingID = listingID;
        this.price = price;
        this.sellerID = sellerID;
        this.productID = productID;
        this.listingDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
    }

    public int getListingID() {
        return listingID;
    }
    public void setListingID(int listingID) {
        this.listingID = listingID;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getSellerID() {
        return sellerID;
    }
    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }
    public int getProductID() {
        return productID;
    }
    public void setProductID(int productID) {
        this.productID = productID;
    }
    public LocalDateTime getListingDate() {
        return listingDate;
    }
    public void setListingDate(LocalDateTime listingDate) {
        this.listingDate = listingDate;
    }


}
