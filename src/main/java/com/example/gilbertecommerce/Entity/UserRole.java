package com.example.gilbertecommerce.Entity;

public enum UserRole {

    USER_ROLE00("Admin"),
    USER_ROLE01("Gilbert Team"),
    USER_ROLE02("Standard"),
    USER_ROLE03("Guest"),
    USER_ROLE004("Business");

    private String VALUE;

    private UserRole(String role) {
        this.VALUE = role;
    }

    public String getTrustRating() {
        return VALUE;
    }



}
