package dev.evelyn.taskscheduler.servers;

import dev.evelyn.taskscheduler.exceptions.ServerException;
import dev.evelyn.taskscheduler.exceptions.TaskException;
import dev.evelyn.taskscheduler.metrics.AlertSystem;
import dev.evelyn.taskscheduler.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.concurrent.*;

public class Server {
    // Task queue and failed task lists
    final List<Task> taskQueue = new ArrayList<>();
    final List<Task> failedTasks = new ArrayList<>();
    // Executor service which manages task execution
    private final ExecutorService executor = Executors.newCachedThreadPool();
    // Add a task to the task queue
    public void addTask(Task task) {
        taskQueue.add(task);
    }
    // Per-server performance metrics
    protected long totalExecutionTime = 0;
    protected long totalTasksExecuted = 0;
    protected long totalCompletedTasks = 0;
    protected long totalFailedTasks = 0;

    public List<Task> executeTasks() throws ServerException {
        try {
            // Process each task in the queue and return a list of successfully completed tasks
            List<Task> completedTasks = taskQueue.stream()
                    .map(this::processTask)  // Process each task
                    .filter(Objects::nonNull)  // Filter out failed tasks (which return null)
                    .filter(Task::isCompleted) // Ensure only completed tasks remain
                    .collect(Collectors.toList());
            taskQueue.clear(); // Clear task queue after execution
            totalCompletedTasks += completedTasks.size(); // Update total completed tasks
            return completedTasks;
        } catch (Exception e) {
            // Log and throw a ServerException if any task processing fails unexpectedly
            throw new ServerException("Failed to execute tasks", e);
        }
    }

    // Process a single task
    Task processTask(Task task) {
        totalTasksExecuted++; // Increment total tasks executed
        Future<?> future = submitTask(task); // Submit task to the executor service
        try {
            future.get(task.getTimeout(), TimeUnit.MILLISECONDS); // Wait for task completion within its timeout
            AlertSystem.sendAlertInfo("Task " + task.getId() + " completed successfully on local server");
            return task; // Return completed task
        } catch (TimeoutException e) {
            // If the task times out, handle failure and log the timeout
            future.cancel(true);
            handleFailedTask(task, "Task timed out");
            return null;
        } catch (ExecutionException | InterruptedException e) {
            // Handle failed or interrupted task
            handleFailedTask(task, "Task failed or was interrupted");
            return null;
        }
    }

    // Submit a task to the executor service
    private Future<?> submitTask(Task task) {
        // Returns a Future object that represents the future result of the task
        return executor.submit(() -> {
            long startTime = System.currentTimeMillis(); // Track start time for performance metrics
            try {
                // Simulate task execution by sleeping for the estimated duration
                Thread.sleep(task.getEstimatedDuration().getDurationMs().longValue());
                task.execute();
            } catch (TaskException | InterruptedException e) {
                AlertSystem.sendAlertError("Task failed: " + task.getId());
                throw new RuntimeException(e); // Throw an exception to signal task failure
            } finally {
                // Always update the performance monitor after the task completes or fails
                long executionTime = System.currentTimeMillis() - startTime;
                totalExecutionTime += executionTime;
            }
        });
    }

    // Handle failed task logic (cleanup, logging, etc.)
    public void handleFailedTask(Task task, String message) {
        totalFailedTasks++; // Increment total failed tasks
        task.cleanup(); // Ensure the task cleans up resources upon failure
        AlertSystem.sendAlertError(message + ": " + task.getId()); // Log the failure message
        failedTasks.add(task); // Add the task to the failed task list
    }

    // Addressing the feedback from my previous submission, I made these return copies of the lists:
    // Get the list of failed tasks
    public List<Task> getFailedTasks() {
        return new ArrayList<>(failedTasks);
    }

    // Get the list of tasks
    public List<Task> getTasks() {
        return new ArrayList<>(taskQueue);
    }

    // Clear the task queue
    public void clearTasks() {
        taskQueue.clear();
    }

    // Getters for performance metrics
    public long getTotalExecutionTime() {
        return totalExecutionTime;
    }

    public long getTotalTasksExecuted() {
        return totalTasksExecuted;
    }

    public long getTotalCompletedTasks() {
        return totalCompletedTasks;
    }

    public long getTotalFailedTasks() {
        return totalFailedTasks;
    }
}
