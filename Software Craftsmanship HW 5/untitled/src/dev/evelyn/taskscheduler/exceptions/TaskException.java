package dev.evelyn.taskscheduler.exceptions;

import dev.evelyn.taskscheduler.metrics.ErrorHandler;

public class TaskException extends Exception {

    // Thrown from any class implementing Task
    // Can take both a message and stack trace or just a message

    public TaskException(String message) {
        ErrorHandler.reportError(this, message);
    }

    public TaskException(String message, Exception e) {
        ErrorHandler.reportError(e, message);
        e.printStackTrace();
    }

}
