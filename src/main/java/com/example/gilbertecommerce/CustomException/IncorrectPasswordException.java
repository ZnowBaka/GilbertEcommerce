package com.example.gilbertecommerce.CustomException;

public class IncorrectPasswordException extends Exception {
    private String details;

    public IncorrectPasswordException(String message, String details) {
        super(message);
        this.details = details;
    }

    public String getDetails() {
        return this.details;
    }

}
