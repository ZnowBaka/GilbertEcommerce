package com.example.gilbertecommerce.CustomException.AuthenticationException;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class UserDoesNotExistException extends AAGilbertException {
    public UserDoesNotExistException(String message, String details) {
        super(message, "AUTH_003" , details, "Authetication Service");
    }
}
