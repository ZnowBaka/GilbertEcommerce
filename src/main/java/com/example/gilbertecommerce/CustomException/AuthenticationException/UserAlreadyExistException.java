package com.example.gilbertecommerce.CustomException.AuthenticationException;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class UserAlreadyExistException extends AAGilbertException {
    public UserAlreadyExistException(String message, String details) {
        super(message, "AUTH_002", details, "Registration service");
    }
}
