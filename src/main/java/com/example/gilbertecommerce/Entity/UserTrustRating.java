package com.example.gilbertecommerce.Entity;

public enum UserTrustRating {
    trustRating00("Not Yet Rated"),
    trustRating01("New Seller"),
    trustRating02("Novice Seller"),
    trustRating03("Expert Seller"),
    trustRating04("Gilbert Staff");

    private String value;

    private UserTrustRating(String rating) {
        this.value = rating;
    }

    public String getTrustRating() {
        return this.value;
    }

}
