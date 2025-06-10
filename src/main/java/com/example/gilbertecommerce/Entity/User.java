package com.example.gilbertecommerce.Entity;

public class User {

    private String email;
    private String firstName;
    private String lastName;
    private String displayName;
    private int userID;
    private UserRole role; // For now its kept as an int, it could possibly be a string, possibly an enum.
    private int trustRating;
   // private ContactInfo contactInfo;  an object pulled when invoice is created


    public User() {}

    public User(String displayName,String email, String firstName, String lastName, int userID, UserRole role, int trustRating) {
        this.displayName = displayName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.role = role;
        this.trustRating = trustRating;
       // contact info left out until implemented
    }
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
    public int getTrustRating() {
        return trustRating;
    }

    public void setTrustRating(int trustRating) {
        this.trustRating = trustRating;
    }

}
