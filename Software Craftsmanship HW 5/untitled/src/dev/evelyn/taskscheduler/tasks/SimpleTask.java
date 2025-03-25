package dev.evelyn.taskscheduler.tasks;

import dev.evelyn.taskscheduler.Duration;
import dev.evelyn.taskscheduler.exceptions.TaskException;
import dev.evelyn.taskscheduler.metrics.AlertSystem;

import java.io.Serial;
import java.util.Collections;
import java.util.Set;

public class SimpleTask implements Task {
    private final String id;
    private final Duration estimatedDuration;
    private boolean completed;
    private final Duration timeout;
    // Serialization protocol
    @Serial
    private static final long serialVersionUID = 1L;

    public SimpleTask(String id, Duration estimatedDuration) {
        this.id = id;
        this.estimatedDuration = estimatedDuration;
        this.completed = false;
        this.timeout = Duration.ofMillis(estimatedDuration.getDurationMs().longValue() * 2);
    }

    public SimpleTask(String id, Duration estimatedDuration, Duration timeout) {
        this.id = id;
        this.estimatedDuration = estimatedDuration;
        this.completed = false;
        this.timeout = timeout;
    }

    public SimpleTask(String id, Duration estimatedDuration, long timeout) {
        this.id = id;
        this.estimatedDuration = estimatedDuration;
        this.completed = false;
        this.timeout = Duration.ofMillis(timeout);
    }
    @Override
    public long getTimeout() {
        return timeout.getDurationMs().longValue();
    }

    @Override
    public Set<String> getDependencies() {
        return Collections.emptySet();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void execute() throws TaskException {
        try { // Simulate task execution
            completed = true;
            cleanup();
        } catch (Exception e) { // Catch any exceptions that occur during task execution
            throw new TaskException("Failed to execute task", e);
        }
    }

    @Override
    public void cleanup() {
        // Cleanup task resources
        AlertSystem.sendAlertInfo("Cleaning up task: " + id);
    }
    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public Duration getEstimatedDuration() {
        return estimatedDuration;
    }

    @Override
    // Because we added a getPriority method to the Task interface, we must implement it here
    public TaskPriority getPriority() {
        return TaskPriority.MEDIUM;
    }
}
