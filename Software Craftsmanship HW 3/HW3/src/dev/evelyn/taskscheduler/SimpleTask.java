package dev.evelyn.taskscheduler;

import dev.evelyn.taskscheduler.exceptions.TaskException;

// Because this class must be immutable, we can make it final
public final class SimpleTask implements Task {
    private final String id;
    private final Duration estimatedDuration;
    private boolean completed;

    public SimpleTask(String id, Duration estimatedDuration) {
        this.id = id;
        this.estimatedDuration = estimatedDuration;
        this.completed = false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void execute() throws TaskException {
        // Execute the task
        System.out.println("Executing task " + id + "...");
        try { // Simulate task execution
            completed = true;
            System.out.println("Task " + id + " completed successfully");
        } catch (Exception e) { // Catch any exceptions that occur during task execution
            throw new TaskException("Failed to execute task", e);
        }
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public Duration getEstimatedDuration() {
        return estimatedDuration;
    }
}
