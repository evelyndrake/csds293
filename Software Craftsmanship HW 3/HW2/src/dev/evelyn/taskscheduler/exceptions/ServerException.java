package dev.evelyn.taskscheduler.exceptions;

public class ServerException extends Exception {

    // Thrown from the Server class
    // Can take both a message and stack trace or just a message
    public ServerException(String message) {
        System.out.println(message);
    }
    public ServerException(String message, Exception e) {
        System.out.println(message);
        e.printStackTrace();
    }
}
