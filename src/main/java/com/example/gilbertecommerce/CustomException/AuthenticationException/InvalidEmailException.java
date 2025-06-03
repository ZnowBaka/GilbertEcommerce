package com.example.gilbertecommerce.CustomException.AuthenticationException;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class InvalidEmailException extends AAGilbertException {
    public InvalidEmailException(String message, String details) {
        super(message, "VAL_005", details, "Validation Service");
    }
}
