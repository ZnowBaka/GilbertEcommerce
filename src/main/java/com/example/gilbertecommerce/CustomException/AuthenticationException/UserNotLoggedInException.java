package com.example.gilbertecommerce.CustomException.AuthenticationException;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class UserNotLoggedInException extends AAGilbertException {
    public UserNotLoggedInException(String message, String details) {
        super(message, "AUTH_005", details, "Session service");
    }
}
