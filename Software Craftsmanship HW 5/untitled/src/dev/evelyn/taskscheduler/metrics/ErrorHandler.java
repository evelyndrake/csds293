package dev.evelyn.taskscheduler.metrics;

import dev.evelyn.taskscheduler.exceptions.SchedulerException;
import dev.evelyn.taskscheduler.exceptions.SchedulerFullException;
import dev.evelyn.taskscheduler.exceptions.ServerException;
import dev.evelyn.taskscheduler.exceptions.TaskException;

import java.util.HashMap;

public class ErrorHandler {

    // I didn't like the idea of having each exception taking a Logger as a parameter, so I removed them and put one
    // in the AlertSystem class

    // Map of error types to number of errors
    private static final HashMap<ErrorType, Integer> errorCounts = new HashMap<>();
    // Populate the map with all error types
    static {
        for (ErrorType errorType : ErrorType.values()) {
            errorCounts.put(errorType, 0);
        }
    }

    public static void reportError(Exception e, String message) {
        ErrorType errorType = findErrorTypeOfException(e);
        handleErrorOfType(errorType);
        AlertSystem.sendAlertError(errorType + " error: " + message);
    }

    private static void handleErrorOfType(ErrorType errorType) {
        // Add 1 to the error count for the given error type
        errorCounts.put(errorType, errorCounts.get(errorType) + 1);
    }

    // Called from PerformanceMonitor
    public static void reportPerformanceWarning(String message) {
        handleErrorOfType(ErrorType.PERFORMANCE);
        AlertSystem.sendAlertWarning("Performance warning: " + message);
    }
    private static ErrorType findErrorTypeOfException(Exception e) {
        if (e instanceof TaskException) {
            return ErrorType.TASK;
        } else if (e instanceof SchedulerException | e instanceof SchedulerFullException) {
            return ErrorType.SCHEDULER;
        } else if (e instanceof ServerException) {
            return ErrorType.SERVER;
        } else { // Never used, but we want to be able to handle any exception
            return ErrorType.OTHER;
        }
    }

    // Print all error metrics
    public static void displayAllMetrics() {
        AlertSystem.sendAlertInfo("--------------------------------------------");
        AlertSystem.sendAlertInfo("Error Metrics:");
        AlertSystem.sendAlertInfo("--------------------------------------------");
        // Display the number of errors for each error type
        for (ErrorType errorType : ErrorType.values()) {
            AlertSystem.sendAlertInfo(errorType + " errors: " + errorCounts.get(errorType));
        }
        AlertSystem.sendAlertInfo("--------------------------------------------");
    }

    private enum ErrorType {
        SERVER,
        TASK,
        SCHEDULER,
        OTHER,
        PERFORMANCE
    }

}

