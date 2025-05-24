package com.example.gilbertecommerce.Entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ProductListing {

    private int listingID;
    private double price;
    private int sellerID;
    private LocalDateTime listingDate;
    private String listingTitle;
    private String listingDescription;
    private String listingImage;
    private String listingStatus;
    
    public ProductListing() {}
/*
    public ProductListing(int listingID, double price, int sellerID, int productID, LocalDateTime listingDate,String listingTitle, String ListingDescription, String ListingImage, boolean listingStatus) {
        this.listingID = listingID;
        this.price = price;
        this.sellerID = sellerID;
        this.productID = productID;
        this.listingDate = listingDate;
        this.listingTitle = listingTitle;
        this.listingDescription = ListingDescription;
        this.listingImage = ListingImage;
        this.listingStatus = listingStatus;
    }*/
    public ProductListing(String listingTitle, String listingDescription, double price){
        this.listingTitle = listingTitle;
        this.listingDescription = listingDescription;
        this.price = price;
    }

    public ProductListing(int listingID, double price, int sellerID, long epochMillis, String listingTitle, String ListingDescription, String ListingImage, String listingStatus) {
        this.listingID = listingID;
        this.price = price;
        this.sellerID = sellerID;
        this.listingStatus = listingStatus;
        this.listingDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
        this.listingTitle = listingTitle;
        this.listingDescription = ListingDescription;
        this.listingImage = ListingImage;
    }

    public int getListingID() {return listingID;}
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
    public LocalDateTime getListingDate() {
        return listingDate;
    }
    public void setListingDate(LocalDateTime listingDate) {
        this.listingDate = listingDate;
    }
    public String isListingStatus() {
        return listingStatus;
    }
    public void setListingStatus(String listingStatus) {
        this.listingStatus = listingStatus;
    }
    public String getListingImage() {
        return listingImage;
    }
    public void setListingImage(String listingImage) {
        this.listingImage = listingImage;
    }
    public String getListingDescription() {
        return listingDescription;
    }
    public void setListingDescription(String listingDescription) {
        this.listingDescription = listingDescription;
    }
    public String getListingTitle() {
        return listingTitle;
    }
    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }
}
