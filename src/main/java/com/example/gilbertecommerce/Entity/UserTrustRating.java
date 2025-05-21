package com.example.gilbertecommerce.Entity;

public enum UserTrustRating {

    TRUST_RATING00("Not Yet Rated"),
    TRUST_RATING01("New Seller"),
    TRUST_RATING02("Novice Seller"),
    TRUST_RATING03("Expert Seller"),
    TRUST_RATING04("Gilbert Staff");

    private String VALUE;

    private UserTrustRating(String rating) {
        this.VALUE = rating;
    }

    public String getTrustRating() {
        return VALUE;
    }

}
