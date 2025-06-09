package com.example.gilbertecommerce.CustomException.ValidationExceptions;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class WeakPasswordException extends AAGilbertException {

    public WeakPasswordException(String message, String details) {
        super(message, "VAL_003", details, "Validation Service");
    }
}
