package com.example.gilbertecommerce.CustomException;

public class UserNameAlreadyExistException extends Exception {
    private String details;

    public UserNameAlreadyExistException(String message, String details) {
        super(message);
        this.details = details;
    }

    public String getDetails() {
        return this.details;
    }


}
