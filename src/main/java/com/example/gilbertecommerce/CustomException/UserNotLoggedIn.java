package com.example.gilbertecommerce.CustomException;

public class UserNotLoggedIn extends RuntimeException {
    public UserNotLoggedIn(String message) {
        super(message);
    }
}
