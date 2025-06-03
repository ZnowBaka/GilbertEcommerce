package com.example.gilbertecommerce.CustomException.ValidationExceptions;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class EmptyFieldException extends AAGilbertException {

    public EmptyFieldException(String message, String details) {
        super(message, "VAL_001", details, "Validation service");
    }

    public EmptyFieldException(String message, String details, String source) {
        super(message,"VAL_001", details, source);
    }
}
