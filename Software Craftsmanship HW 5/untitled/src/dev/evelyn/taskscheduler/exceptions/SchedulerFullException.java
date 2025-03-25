package dev.evelyn.taskscheduler.exceptions;

import dev.evelyn.taskscheduler.metrics.ErrorHandler;

public class SchedulerFullException extends Exception {

    // Thrown from the TaskScheduler class
    // Can take both a message and stack trace or just a message

    public SchedulerFullException(String message) {
        ErrorHandler.reportError(this, message);
    }

    public SchedulerFullException(String message, Exception e) {
        ErrorHandler.reportError(e, message);
        e.printStackTrace();
    }
}
