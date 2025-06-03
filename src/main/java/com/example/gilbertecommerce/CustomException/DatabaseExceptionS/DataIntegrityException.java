package com.example.gilbertecommerce.CustomException.DatabaseExceptionS;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class DataIntegrityException extends AAGilbertException {
    public DataIntegrityException(String message, String details) {
        super(message, "DB_002", details, "Database Service");
    }
}
