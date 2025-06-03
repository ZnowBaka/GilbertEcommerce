package com.example.gilbertecommerce.CustomException.BusinessExceptions;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class InvalidListingException extends AAGilbertException {

    private final String field;
    private final String source;

    public InvalidListingException(String message, String details, String field, String source) {
        super(message, "BIS_002", details, "Listing Service");
        this.field = field;
        this.source = source;
    }
}
