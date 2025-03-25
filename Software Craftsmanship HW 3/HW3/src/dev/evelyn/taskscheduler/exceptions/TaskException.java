package dev.evelyn.taskscheduler.exceptions;

public class TaskException extends Exception {

    // Thrown from any class implementing Task
    // Can take both a message and stack trace or just a message
    public TaskException(String message) {
        System.out.println(message);
    }
    public TaskException(String message, Exception e) {
        System.out.println(message);
        e.printStackTrace();
    }
}
