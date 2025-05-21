package com.example.gilbertecommerce.CustomException;

public class UserAlreadyExistException extends Exception {
    private String details;

    public UserAlreadyExistException(String message, String details) {
        super(message);
        this.details = details;
    }

    public String getDetails() {
        return this.details;
    }

}
