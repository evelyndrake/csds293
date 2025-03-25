package dev.evelyn.taskscheduler.exceptions;

public class SchedulerException extends Exception {

    // Thrown from the TaskScheduler class
    // Can take both a message and stack trace or just a message
    public SchedulerException(String message) {
        System.out.println(message);
    }
    public SchedulerException(String message, Exception e) {
        System.out.println(message);
        e.printStackTrace();
    }
}
