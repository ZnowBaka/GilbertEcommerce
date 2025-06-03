package com.example.gilbertecommerce.CustomException;

import java.time.LocalDateTime;

public abstract class AAGilbertException extends RuntimeException {

    private String errorCode;
    private String details;
    private String source;
    private LocalDateTime timestamp;

    public AAGilbertException(String message, String errorCode, String details, String source) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
        this.source = source;
        this.timestamp = LocalDateTime.now();
    }

    public AAGilbertException(String message, String errorCode, String details, String source, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = details;
        this.source = source;
        this.timestamp = LocalDateTime.now();
    }

    public String getFormattedLogMessage() {
        return String.format("[%s] %s - Error Code: %s, Source: %s, Details: %s, Message: %s",
                timestamp, this.getClass().getSimpleName(), errorCode, source, details, getMessage());
    }

    public String getSource() {
        return source;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public String getDetails() {
        return details;
    }
    public String getErrorCode() {
        return errorCode;
    }



}
