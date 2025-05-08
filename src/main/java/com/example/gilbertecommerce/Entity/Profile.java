package com.example.gilbertecommerce.Entity;

public class Profile {

    private String email;
    private String firstName;
    private String lastName;
    private int userID;
    private int role; // For now its kept as an int, possibly used with a to string, possibly an enum.
    private int trustRating;
   // private ContactInfo contactInfo;  an object pulled when invoice is created


    public Profile() {}

    public Profile(String email, String firstName, String lastName, int userID, int role, int trustRating) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.role = role;
        this.trustRating = trustRating;
       // contanctinfo left out until implemented
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getTrustRating() {
        return trustRating;
    }

    public void setTrustRating(int trustRating) {
        this.trustRating = trustRating;
    }

}
