package dev.evelyn.taskscheduler.tests;

import dev.evelyn.taskscheduler.Duration;
import dev.evelyn.taskscheduler.tasks.PriorityTask;
import dev.evelyn.taskscheduler.tasks.TaskPriority;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriorityTaskTest {

    @Test
    void getPriority() {
        // Create a new priority task
        PriorityTask priorityTask = new PriorityTask("1", Duration.ofMillis(1000), TaskPriority.HIGH);
        // Check that the priority is correct
        assertEquals(TaskPriority.HIGH, priorityTask.getPriority());
    }
}