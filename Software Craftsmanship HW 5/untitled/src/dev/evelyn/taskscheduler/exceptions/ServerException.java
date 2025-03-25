package dev.evelyn.taskscheduler.exceptions;

import dev.evelyn.taskscheduler.metrics.ErrorHandler;

public class ServerException extends Exception {

    // Thrown from the Server class
    // Can take both a message and stack trace or just a message

    public ServerException(String message) {
        ErrorHandler.reportError(this, message);
    }

    public ServerException(String message, Exception e) {
        ErrorHandler.reportError(e, message);
        e.printStackTrace();
    }
}
