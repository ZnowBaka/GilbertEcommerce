package com.example.gilbertecommerce.CustomException.DatabaseExceptionS;

import com.example.gilbertecommerce.CustomException.AAGilbertException;

public class DatabaseConnectionException extends AAGilbertException {

    public DatabaseConnectionException(String message, String details) {
        super(message, "DB_001", details, "Database Service");
    }

    public DatabaseConnectionException(String message, String details, Throwable cause) {
        super(message, "DB_001", details, "Database Service", cause);
    }
}

