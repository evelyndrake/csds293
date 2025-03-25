package dev.evelyn.taskscheduler.tests;

import dev.evelyn.taskscheduler.*;
import dev.evelyn.taskscheduler.exceptions.SchedulerException;
import dev.evelyn.taskscheduler.exceptions.SchedulerFullException;
import dev.evelyn.taskscheduler.servers.Server;
import dev.evelyn.taskscheduler.tasks.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
            taskScheduler.scheduleTask(new SimpleTask("1", Duration.ofMillis(10)));
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
            taskScheduler.scheduleTask(new SimpleTask("1", Duration.ofMillis(10)));
        } catch (Exception e) {
            fail("Failed to schedule task");
        }
        // Schedule another task on the server
        try {
            taskScheduler.scheduleTask(new SimpleTask("2", Duration.ofMillis(10)));
        } catch (Exception e) {
            fail("Failed to schedule task");
        }
        // Execute all tasks on all servers
        try {
            Map<Server, List<Task>> results = taskScheduler.executeAll();
            assertEquals(1, results.size());
        } catch (Exception e) {
            fail("Failed to execute all tasks");
        }
        // Get the server instance that was created in the task scheduler
        Server scheduledServer = taskScheduler.getServers().get(0);
        // Check that the server has no tasks
        assertEquals(0, scheduledServer.getTasks().size());
    }

    @Test
    void testLoadBalancing() {
        TaskScheduler taskScheduler = new TaskScheduler();
        Server server1 = new Server();
        Server server2 = new Server();
        try {
            taskScheduler.addServer(server1);
            taskScheduler.addServer(server2);
        } catch (Exception e) {
            fail("Failed to add server");
        }
        // Schedule tasks
        try {
            taskScheduler.scheduleTask(new SimpleTask("1", Duration.ofMillis(10)));
            taskScheduler.scheduleTask(new SimpleTask("2", Duration.ofMillis(10)));
            taskScheduler.scheduleTask(new SimpleTask("3", Duration.ofMillis(10)));
        } catch (Exception e) {
            fail("Failed to schedule task");
        }
        // Check that the tasks are distributed evenly between the servers
        assertEquals(2, server1.getTasks().size());
        assertEquals(1, server2.getTasks().size());
    }

    @Test
    void testTaskExecution() {
        TaskScheduler taskScheduler = new TaskScheduler();
        Server server = new Server();
        try {
            taskScheduler.addServer(server);
        } catch (Exception e) {
            fail("Failed to add server");
        }
        // Schedule tasks
        try {
            taskScheduler.scheduleTask(new SimpleTask("1", Duration.ofMillis(10)));
            taskScheduler.scheduleTask(new SimpleTask("2", Duration.ofMillis(10)));
            taskScheduler.scheduleTask(new SimpleTask("3", Duration.ofMillis(10)));
        } catch (Exception e) {
            fail("Failed to schedule task");
        }
        // Execute all tasks on all servers
        try {
            Map<Server, List<Task>> results = taskScheduler.executeAll();
            // Print out the results, which should include 3 SimpleTask classes
            assertEquals(1, results.size());
        } catch (Exception e) {
            fail("Failed to execute all tasks");
        }
        // Get the server instance that was created in the task scheduler
        Server scheduledServer = taskScheduler.getServers().get(0);
        // Check that the server has no tasks
        assertEquals(0, scheduledServer.getTasks().size());
    }

    @Test
    void prioritizeTasks() {
        TaskScheduler taskScheduler = new TaskScheduler();
        Server server = new Server();
        try {
            taskScheduler.addServer(server);
        } catch (Exception e) {
            fail("Failed to add server");
        }

        // Schedule tasks with different priorities
        try {
            taskScheduler.scheduleTask(new PriorityTask("1", Duration.ofMillis(10), TaskPriority.LOW)); // LOW priority
            taskScheduler.scheduleTask(new PriorityTask("2", Duration.ofMillis(10), TaskPriority.HIGH)); // HIGH priority
        } catch (Exception e) {
            fail("Failed to schedule tasks");
        }
        // Execute all tasks and check that the higher priority task was executed first
        Map<Server, List<Task>> results;
        try {
            results = taskScheduler.executeAll();
            List<Task> completedTasks = results.get(server);
            assertNotNull(completedTasks);
            assertEquals(2, completedTasks.size());
            assertEquals("2", completedTasks.get(0).getId()); // Task 2 should be executed first due to HIGH priority
        } catch (Exception e) {
            fail("Failed to execute tasks");
        }
    }

    @Test
    void testConcurrency() throws InterruptedException {
        TaskScheduler taskScheduler = new TaskScheduler();
        Server server = new Server();
        try {
            taskScheduler.addServer(server);
        } catch (Exception e) {
            fail("Failed to add server");
        }
        Runnable taskSchedulerRunnable = () -> {
            try {
                for (int i = 0; i < 100; i++) {
                    taskScheduler.scheduleTask(new SimpleTask(String.valueOf(i), Duration.ofMillis(10)));
                }
            } catch (Exception e) {
                fail("Failed to schedule task in a concurrent environment");
            }
        };

        Thread thread1 = new Thread(taskSchedulerRunnable);
        Thread thread2 = new Thread(taskSchedulerRunnable);
        thread1.start();
        thread2.start();
        // Wait for the threads to finish
        thread1.join();
        thread2.join();
        // Check that all tasks were scheduled without errors
        assertEquals(200, server.getTasks().size());
    }

    @Test
    void testLoadBalancingWithDurations() {
        TaskScheduler taskScheduler = new TaskScheduler();
        Server server1 = new Server();
        Server server2 = new Server();
        try {
            taskScheduler.addServer(server1);
            taskScheduler.addServer(server2);
        } catch (Exception e) {
            fail("Failed to add server");
        }
        // Schedule tasks with different durations
        try {
            taskScheduler.scheduleTask(new SimpleTask("1", Duration.ofMillis(30)));
            taskScheduler.scheduleTask(new SimpleTask("2", Duration.ofMillis(20)));
            taskScheduler.scheduleTask(new SimpleTask("3", Duration.ofMillis(10)));
        } catch (Exception e) {
            fail("Failed to schedule task");
        }
        // The load should be distributed based on the duration of the tasks
        assertEquals(taskScheduler.getServerDurations(server1), taskScheduler.getServerDurations(server2));
    }

    @Test
    void checkTaskDependencies() {
        TaskScheduler taskScheduler = new TaskScheduler();
        Server server = new Server();
        try {
            taskScheduler.addServer(server);
        } catch (Exception e) {
            fail("Failed to add server");
        }
        // Create 3 tasks with dependencies
        Task task1 = new SimpleTask("1", Duration.ofMillis(10));
        Task task2 = new DependentTask("2", TaskPriority.MEDIUM, Duration.ofMillis(10), Set.of("1"));
        Task task3 = new DependentTask("3", TaskPriority.MEDIUM, Duration.ofMillis(10), Set.of("2"));
        // Schedule task1
        try {
            taskScheduler.scheduleTask(task1);
        } catch (Exception e) {
            fail("Failed to schedule task");
        }
        // Execute task1
        try {
            taskScheduler.executeAll();
        } catch (Exception e) {
            fail("Failed to execute task");
        }
        // Schedule task2, which depends on task1
        try {
            taskScheduler.scheduleTask(task2);
        } catch (Exception e) {
            fail("Failed to schedule task");
        }
        // Execute task2
        try {
            taskScheduler.executeAll();
        } catch (Exception e) {
            fail("Failed to execute task");
        }
        // Schedule task3, which depends on task2
        try {
            taskScheduler.scheduleTask(task3);
        } catch (Exception e) {
            fail("Failed to schedule task");
        }
        // Execute task3
        try {
            taskScheduler.executeAll();
        } catch (Exception e) {
            fail("Failed to execute task");
        }
        // Ensure all tasks were executed
        assertEquals(0, server.getTasks().size());
    }

    @Test
    void testUnmetDependencies() {
        TaskScheduler taskScheduler = new TaskScheduler();
        Server server = new Server();
        try {
            taskScheduler.addServer(server);
        } catch (Exception e) {
            fail("Failed to add server");
        }
        // Create task2 that depends on a task that hasn't been scheduled
        Task task2 = new DependentTask("2", TaskPriority.MEDIUM, Duration.ofMillis(10), Set.of("1"));
        // Try scheduling this task, which should fail due to unmet dependencies
        try {
            taskScheduler.scheduleTask(task2);
            fail("Task with unmet dependencies was scheduled");
        } catch (SchedulerException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    void testRetryPolicy() throws SchedulerException, SchedulerFullException {
        RetryPolicy retryPolicy = new RetryPolicy(3, 10, false);
        TaskScheduler taskScheduler = new TaskScheduler(retryPolicy);
        Server server = new Server();
        try {
            taskScheduler.addServer(server);
        } catch (Exception e) {
            fail("Failed to add server");
        }
        // Create a RetryTask that will succeed only on its second attempt
        Task task = new FailingTask("1", Duration.ofMillis(10), 2);
        // Schedule the task
        taskScheduler.scheduleTask(task);
        // Execute the task
        Map<Server, List<Task>> results = taskScheduler.executeAll();
        // Check that the task was executed successfully
        List<Task> completedTasks = results.get(server);
        assertNotNull(completedTasks);
        // Make another RetryTask that will succeed only on its 5th attempt
        Task task2 = new FailingTask("3", Duration.ofMillis(10), 5);
        // Schedule the task
        taskScheduler.scheduleTask(task2);
        // Execute the task
        results = taskScheduler.executeAll();
        // Ensure that the task was not executed
        completedTasks = results.get(server);
        assertEquals(0, completedTasks.size());
    }

    @Test
    void testTimeoutCancellation() throws SchedulerException, SchedulerFullException {
        // Example from the assignment (shortened times for testing)
        Task task = new SimpleTask("1", Duration.ofMillis(700), Duration.ofMillis(500));
        TaskScheduler taskScheduler = new TaskScheduler();
        Server server = new Server();
        try {
            taskScheduler.addServer(server);
        } catch (Exception e) {
            fail("Failed to add server");
        }
        // Schedule the task
        taskScheduler.scheduleTask(task);
        // Execute the task
        Map<Server, List<Task>> results = taskScheduler.executeAll();
        // Check that the task was not executed
        List<Task> completedTasks = results.get(server);
        assertEquals(0, completedTasks.size());
    }

    @Test
    void testTaskTimeoutWithDependencies() throws SchedulerException, SchedulerFullException {
        // Example from the assignment (shortened times for testing)
        TaskScheduler taskScheduler = new TaskScheduler();
        Server server = new Server();
        try {
            taskScheduler.addServer(server);
        } catch (Exception e) {
            fail("Failed to add server");
        }
        // Create task A with no dependencies and a timeout of 500ms
        // This will complete successfully
        Task taskA = new SimpleTask("A", Duration.ofMillis(100), Duration.ofMillis(500));
        // Create task B with a dependency on task A and a duration exceeding its timeout of 500ms
        // This will not complete successfully because it times out
        Task taskB = new DependentTask("B", TaskPriority.MEDIUM, Duration.ofMillis(1000), Duration.ofMillis(500), Set.of("A"));
        // Create task C with a dependency on task B and a timeout of 500ms
        // This will not complete successfully because task B will not complete
        Task taskC = new DependentTask("C", TaskPriority.MEDIUM, Duration.ofMillis(100), Duration.ofMillis(500), Set.of("B"));
        // Schedule and execute task A, which will complete successfully
        taskScheduler.scheduleTask(taskA);
        taskScheduler.executeAll();
        // Schedule and execute task B, which will not complete successfully because it times out
        taskScheduler.scheduleTask(taskB);
        Map<Server, List<Task>> results = taskScheduler.executeAll();
        List<Task> completedTasks = results.get(server);
        assertEquals(0, completedTasks.size()); // Ensures task B did not complete
        // Schedule and execute task C and ensure it doesn't run because task B timed out
        try {
            taskScheduler.scheduleTask(taskC);
            taskScheduler.executeAll();
            fail("Task C depends on task B, which should have timed out");
        } catch (SchedulerException e) {
            // Expected exception
        }
    }
}