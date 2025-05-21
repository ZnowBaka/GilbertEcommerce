package com.example.gilbertecommerce.CustomException;

public class UserDoesNotExistException extends Exception {
    private String details;

    public UserDoesNotExistException(String message, String details) {
        super(message);
        this.details = details;
    }

    public String getDetails() {
        return this.details;
    }

}
