package dev.evelyn.taskscheduler.tests;

import dev.evelyn.taskscheduler.Duration;
import dev.evelyn.taskscheduler.tasks.SimpleTask;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

// Tests all functionality of the SimpleTask class

class SimpleTaskTest {

    @Test
    void getId() {
        // Create a task with ID "1" and estimated duration of 1000ms
        SimpleTask task = new SimpleTask("1", Duration.ofMillis(1000));
        // Check that the ID is correct
        assertEquals("1", task.getId());
    }

    @Test
    void execute() {
        // Create a task with ID "1" and estimated duration of 1000ms
        SimpleTask task = new SimpleTask("1", Duration.ofMillis(1000));
        // Execute the task
        try {
            task.execute();
        } catch (Exception e) {
            fail("Failed to execute task");
        }
        // Check that the task is completed
        assertTrue(task.isCompleted());
    }

    @Test
    void isCompleted() {
        // Create a task with ID "1" and estimated duration of 1000ms
        SimpleTask task = new SimpleTask("1", Duration.ofMillis(1000));
        // Check that the task is not completed
        assertFalse(task.isCompleted());
        // Execute the task
        try {
            task.execute();
        } catch (Exception e) {
            fail("Failed to execute task");
        }
        // Check that the task is completed
        assertTrue(task.isCompleted());
    }

    @Test
    void getEstimatedDuration() {
        // Create a task with ID "1" and estimated duration of 1000ms
        SimpleTask task = new SimpleTask("1", Duration.ofMillis(1000));
        // Check that the estimated duration is correct
        assertEquals(new BigInteger("1000"), task.getEstimatedDuration().getDurationMs());
    }

}