package dev.evelyn.taskscheduler.tasks;

import dev.evelyn.taskscheduler.Duration;

import java.util.Collections;
import java.util.Set;

public final class DependentTask extends PriorityTask {

    public final Set<String> dependencies;

    public DependentTask(String id, TaskPriority priority, Duration estimatedDuration, Set<String> dependencies) {
        super(id, estimatedDuration, priority);
        this.dependencies = Collections.unmodifiableSet(dependencies);
    }

    public DependentTask(String id, TaskPriority priority, Duration estimatedDuration, long timeout, Set<String> dependencies) {
        super(id, estimatedDuration, timeout, priority);
        this.dependencies = Collections.unmodifiableSet(dependencies);
    }

    public DependentTask(String id, TaskPriority priority, Duration estimatedDuration, Duration timeout, Set<String> dependencies) {
        super(id, estimatedDuration, timeout, priority);
        this.dependencies = Collections.unmodifiableSet(dependencies);
    }

    @Override
    public Set<String> getDependencies() {
        return dependencies;
    }


}
