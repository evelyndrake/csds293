package dev.evelyn.taskscheduler.tasks;

import dev.evelyn.taskscheduler.Duration;
import dev.evelyn.taskscheduler.exceptions.TaskException;

public class FailingTask extends SimpleTask {
    // This task will succeed on the specified attempt which is useful for testing retry logic
    private final int succeedOnAttempt;
    private int currentAttempt;

    public FailingTask(String id, Duration estimatedDuration, int succeedOnAttempt) {
        super(id, estimatedDuration);
        this.succeedOnAttempt = succeedOnAttempt;
    }

    @Override
    public void execute() throws TaskException {
        // Only succeed on the specified attempt
        if (currentAttempt < succeedOnAttempt - 1) {
            currentAttempt++;
            throw new TaskException("Failed to execute task");
        }
        super.execute();
    }


}
