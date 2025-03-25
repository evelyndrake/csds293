package dev.evelyn.taskscheduler.tasks;

import dev.evelyn.taskscheduler.Duration;
import dev.evelyn.taskscheduler.exceptions.TaskException;

import java.io.Serializable;
import java.util.Set;

public interface Task extends Serializable {
    // Get the ID of the task
    String getId();
    // Execute the task, throwing a TaskException if the task fails
    void execute() throws TaskException;
    // Return true if the task has been completed
    boolean isCompleted();
    // Get the estimated duration of the task
    Duration getEstimatedDuration();
    // Get the priority of the task
    TaskPriority getPriority();
    // Get list of dependencies
    Set<String> getDependencies();
    // Get timeout in ms
    long getTimeout();
    // Cleanup method after task fails or is completed
    void cleanup();
}

