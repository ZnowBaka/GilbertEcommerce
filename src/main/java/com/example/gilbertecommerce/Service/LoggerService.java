package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.CustomException.AAGilbertException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;


@Service
public class LoggerService {

    private static final Logger logger = LoggerFactory.getLogger(LoggerService.class);

    public void logException(AAGilbertException exception) {
        String logMessage = exception.getFormattedLogMessage();
        logBasedOnSeverity(logMessage, exception);
    }

    public void logException(AAGilbertException exception, String devMessage) {
        String logMessage = devMessage + " " + exception.getFormattedLogMessage();
        logBasedOnSeverity(logMessage, exception);
    }


    public void logBasedOnSeverity(String logMessage, AAGilbertException exception) {
        if (isCriticalError(exception.getErrorCode())) {
            logger.error("CRITICAL ERROR: " + logMessage, exception);
        } else if (isWarningError(exception.getErrorCode())) {
            logger.warn("WARNING ERROR: " + logMessage, exception);
        } else {
            logger.info("ERROR: " + logMessage, exception);
        }
    }


    public boolean isCriticalError(String errorCode){
        return errorCode.startsWith("DB_") || errorCode.startsWith("SYS_");
    }

    public boolean isWarningError(String errorCode){
        return errorCode.startsWith("AUTH_") || errorCode.startsWith("VAL_");
    }
}
