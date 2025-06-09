package com.example.gilbertecommerce.CustomException.ValidationExceptions;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class EmptyPasswordException extends AAGilbertException {

    public EmptyPasswordException(String message, String details) {
        super(message, "VAL_002", details, "Validation service");
    }
}
