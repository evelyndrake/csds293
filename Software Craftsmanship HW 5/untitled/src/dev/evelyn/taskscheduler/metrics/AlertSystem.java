package dev.evelyn.taskscheduler.metrics;

import java.util.logging.Logger;

public final class AlertSystem {
    // This could easily be set in the launch parameters or a configuration file in deployment
    private static final LOGGING_LEVEL level = LOGGING_LEVEL.ALL_ALERTS;
    private static final Logger logger = Logger.getLogger(AlertSystem.class.getName());

    // Simple method to send a warning message
    public static void sendAlertWarning(String message) {
        if (level == LOGGING_LEVEL.NONE || level == LOGGING_LEVEL.ERRORS) {
            return;
        }
        logger.warning(message);
    }

    // Simple method to send an error message
    public static void sendAlertError(String message) {
        if (level == LOGGING_LEVEL.NONE) {
            return;
        }
        logger.severe(message);
    }

    // Simple method to send an info message
    public static void sendAlertInfo(String message) {
        if (level == LOGGING_LEVEL.VERBOSE) {
            logger.info(message);
        }
    }

    enum LOGGING_LEVEL {
        NONE, // No logging
        ERRORS, // Log only errors
        ALL_ALERTS, // Log errors and warnings
        VERBOSE // Log errors, warnings, and info messages
    }
}