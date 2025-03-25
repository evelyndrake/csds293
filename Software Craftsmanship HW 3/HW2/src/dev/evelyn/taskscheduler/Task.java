package dev.evelyn.taskscheduler;

import dev.evelyn.taskscheduler.exceptions.TaskException;

public interface Task {
    // Get the ID of the task
    String getId();
    // Execute the task, throwing a TaskException if the task fails
    void execute() throws TaskException;
    // Return true if the task has been completed
    boolean isCompleted();
    // Get the estimated duration of the task
    Duration getEstimatedDuration();
}
