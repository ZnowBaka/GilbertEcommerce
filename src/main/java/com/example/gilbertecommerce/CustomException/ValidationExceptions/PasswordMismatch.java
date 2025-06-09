package com.example.gilbertecommerce.CustomException.ValidationExceptions;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class PasswordMismatch extends AAGilbertException {
    public PasswordMismatch(String message, String details) {
        super(message, "VAL_004", details, "Validation Serivce");
    }
}
