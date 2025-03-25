package info;

import info.notifications.NotificationManager;

import java.util.logging.Logger;

// Handles the logging of information, error/warning messages, and exceptions
public class LoggerSystem {
    private static final Logger logger = Logger.getLogger(LoggerSystem.class.getName());
    // Can enable/disable info and problem logging, disabled by default to make testing faster
    private static final boolean DISPLAY_INFO = false;
    private static final boolean DISPLAY_PROBLEMS = false;

    // Logging functionality, note that the administrator is given a notification for all logs
    public static void logInfo(String message) {
        if (DISPLAY_INFO) {
            logger.info(message);
        }
        NotificationManager.addNotification(message, "Admin");
    }

    public static void logError(String message) {
        if (DISPLAY_PROBLEMS) {
            logger.severe(message);
        }
        NotificationManager.addNotification(message, "Admin");
    }

    public static void logWarning(String message) {
        if (DISPLAY_PROBLEMS) {
            logger.warning(message);
        }
        NotificationManager.addNotification(message, "Admin");
    }

    public static void handleException(Exception e) {
        if (DISPLAY_PROBLEMS) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        NotificationManager.addNotification(e.getMessage(), "Admin");
    }
}
