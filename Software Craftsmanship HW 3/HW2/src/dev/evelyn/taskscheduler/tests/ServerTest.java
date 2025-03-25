package dev.evelyn.taskscheduler.tests;

import dev.evelyn.taskscheduler.Duration;
import dev.evelyn.taskscheduler.Server;
import dev.evelyn.taskscheduler.SimpleTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests all functionality of the Server class

class ServerTest {

    @Test
    void addTask() {
        // Create a server
        Server server = new Server();
        // Create a task with ID "1" and estimated duration of 1000ms
        SimpleTask task = new SimpleTask("1", Duration.ofMillis(1000));
        // Add the task to the server
        server.addTask(task);
        // Check that the task was added
        assertEquals(1, server.getTasks().size());
        // Check that the task is the same as the one we added
        assertEquals(task, server.getTasks().get(0));
    }

    @Test
    void executeTask() {
        // Create a server
        Server server = new Server();
        // Create a task with ID "1" and estimated duration of 1000ms
        SimpleTask task = new SimpleTask("1", Duration.ofMillis(1000));
        // Add the task to the server
        server.addTask(task);
        // Execute the task
        try {
            server.executeTasks();
        } catch (Exception e) {
            fail("Failed to execute task");
        }
        // Check that them task is completed
        assertTrue(task.isCompleted());
        // Check that the task was removed from the server
        assertEquals(0, server.getTasks().size());
    }

    @Test
    void getFailedTasks() {
        // Create a server
        Server server = new Server();
        // Create a task with ID "1" and estimated duration of 1000ms
        SimpleTask task = new SimpleTask("1", Duration.ofMillis(1000));
        // Add the task to the server
        server.addTask(task);
        // Execute the task
        try {
            server.executeTasks();
        } catch (Exception e) {
            fail("Failed to execute task");
        }
        // Check that there are no failed tasks
        assertEquals(0, server.getFailedTasks().size());
    }
}