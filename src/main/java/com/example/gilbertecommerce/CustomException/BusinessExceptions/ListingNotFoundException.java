package com.example.gilbertecommerce.CustomException.BusinessExceptions;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class ListingNotFoundException extends AAGilbertException {
    public ListingNotFoundException(String message, String details) {
        super(message, "BIS_001", details, "Listing Service");
    }
}
