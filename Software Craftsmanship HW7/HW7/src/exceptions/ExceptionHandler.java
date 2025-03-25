package exceptions;

import java.util.Objects;
import java.util.logging.Logger;

public class ExceptionHandler {
    private static final Logger logger = Logger.getLogger(ExceptionHandler.class.getName());
    private static final boolean DISPLAY_WARNINGS = false;
    private static final boolean DISPLAY_STACK_TRACES = false;

    public static void handleException(Exception e) {
        Objects.requireNonNull(e, "Exception cannot be null!");
        if (DISPLAY_STACK_TRACES) { // Display stack traces if enabled
            e.printStackTrace();
        }
        if (DISPLAY_WARNINGS) { // Display warnings if enabled
            displayWarning(e.getMessage());
        }
    }

    public static void displayWarning(String message) {
        Objects.requireNonNull(message, "Message cannot be null!");
        if (DISPLAY_WARNINGS) { // Display warnings if enabled
            logger.warning(message);
        }
    }

}
