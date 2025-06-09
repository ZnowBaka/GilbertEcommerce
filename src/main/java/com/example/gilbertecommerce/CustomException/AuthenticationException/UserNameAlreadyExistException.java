package com.example.gilbertecommerce.CustomException.AuthenticationException;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class UserNameAlreadyExistException extends AAGilbertException {
    public UserNameAlreadyExistException(String message, String details) {
        super(message, "AUTH_004", details, "Registration Service");
    }
}
