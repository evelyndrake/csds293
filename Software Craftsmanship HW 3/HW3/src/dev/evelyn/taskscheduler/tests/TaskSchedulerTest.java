package dev.evelyn.taskscheduler.tests;

import dev.evelyn.taskscheduler.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// Tests all functionality of the TaskScheduler class

class TaskSchedulerTest {

    @Test
    void addServer() {
        // Create a new task scheduler
        TaskScheduler taskScheduler = new TaskScheduler();
        // Create a new server
        Server server = new Server();
        // Add the server to the task scheduler
        try {
            taskScheduler.addServer(server);
        } catch (Exception e) {
            fail("Failed to add server");
        }
        // Check that the server was added
        assertEquals(1, taskScheduler.getServers().size());
    }

    @Test
    void scheduleTask() {
        // Create a new task scheduler
        TaskScheduler taskScheduler = new TaskScheduler();
        // Create a new server
        Server server = new Server();
        // Add the server to the task scheduler
        try {
            taskScheduler.addServer(server);
        } catch (Exception e) {
            fail("Failed to add server");
        }
        // Schedule a task on the server
        try {
            taskScheduler.scheduleTask(new SimpleTask("1", Duration.ofMillis(1000)));
        } catch (Exception e) {
            fail("Failed to schedule task");
        }
        // Get the server instance that was created in the task scheduler
        Server scheduledServer = taskScheduler.getServers().get(0);
        // Check that the server has the task
        assertEquals(1, scheduledServer.getTasks().size());
    }

    @Test
    void executeAll() {
        // Create a new task scheduler
        TaskScheduler taskScheduler = new TaskScheduler();
        // Create a new server
        Server server = new Server();
        // Add the server to the task scheduler
        try {
            taskScheduler.addServer(server);
        } catch (Exception e) {
            fail("Failed to add server");
        }
        // Schedule a task on the server
        try {
            taskScheduler.scheduleTask(new SimpleTask("1", Duration.ofMillis(1000)));
        } catch (Exception e) {
            fail("Failed to schedule task");
        }
        // Schedule another task on the server
        try {
            taskScheduler.scheduleTask(new SimpleTask("2", Duration.ofMillis(1000)));
        } catch (Exception e) {
            fail("Failed to schedule task");
        }
        // Execute all tasks on all servers
        try {
            Map<Server, List<Task>> results = taskScheduler.executeAll();
            // Print out the results, which should include 2 SimpleTask classes
            System.out.println(results);
            assertEquals(1, results.size());
        } catch (Exception e) {
            fail("Failed to execute all tasks");
        }
        // Get the server instance that was created in the task scheduler
        Server scheduledServer = taskScheduler.getServers().get(0);
        // Check that the server has no tasks
        assertEquals(0, scheduledServer.getTasks().size());
    }
}