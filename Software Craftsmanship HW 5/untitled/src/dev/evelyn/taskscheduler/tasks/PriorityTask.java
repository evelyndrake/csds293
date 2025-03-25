package dev.evelyn.taskscheduler.tasks;

import dev.evelyn.taskscheduler.Duration;

public class PriorityTask extends SimpleTask {
    private final TaskPriority priority;

    public PriorityTask(String id, Duration estimatedDuration, TaskPriority priority) {
        super(id, estimatedDuration);
        this.priority = priority;
    }

    public PriorityTask(String id, Duration estimatedDuration, long timeout, TaskPriority priority) {
        super(id, estimatedDuration, timeout);
        this.priority = priority;
    }

    public PriorityTask(String id, Duration estimatedDuration, Duration timeout, TaskPriority priority) {
        super(id, estimatedDuration, timeout);
        this.priority = priority;
    }

    @Override
    public TaskPriority getPriority() {
        return priority;
    }
}
