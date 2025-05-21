package com.example.gilbertecommerce.Entity;

public class Bid {

    private int bidID;
    private double bidAmount;
    private int buyerID;
    private int listingID;

    public Bid() {}

    public Bid(int bidID, double bidAmount, int buyerID, int listingID) {
        this.bidID = bidID;
        this.bidAmount = bidAmount;
        this.buyerID = buyerID;
        this.listingID = listingID;
    }
}
