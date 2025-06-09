package com.example.gilbertecommerce.CustomException.AuthenticationException;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class IncorrectPasswordException extends AAGilbertException {
    public IncorrectPasswordException(String message, String details) {
        super(message, "AUTH_001", details, "Authentication Service");
    }
}
