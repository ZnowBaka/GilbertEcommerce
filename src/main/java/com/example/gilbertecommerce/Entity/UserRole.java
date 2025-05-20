package com.example.gilbertecommerce.Entity;

public enum UserRole {

    userRole00("Admin"),
    userRole01("Gilbert Team"),
    userRole02("Standard"),
    userRole03("Guest"),
    userRole04("Business");



    private String value;

    private UserRole(String role) {
        this.value = role;
    }

    public String getTrustRating() {
        return this.value;
    }



}
