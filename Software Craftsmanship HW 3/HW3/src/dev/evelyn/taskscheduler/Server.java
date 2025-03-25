package dev.evelyn.taskscheduler;

import dev.evelyn.taskscheduler.exceptions.ServerException;
import dev.evelyn.taskscheduler.exceptions.TaskException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Server {
    // Task queue and failed task lists
    private final List<Task> taskQueue = new ArrayList<>();
    private final List<Task> failedTasks = new ArrayList<>();

    // Add a task to the task queue
    public void addTask(Task task) {
        taskQueue.add(task);
    }

    // Execute all tasks in the task queue
    public List<Task> executeTasks() throws ServerException {
        // Execute each task using streams
        try { // Try to execute the tasks
            List<Task> completedTasks = taskQueue.stream()
                    .map(task -> {
                        try { // Try to execute the task and return it if successful
                            task.execute();
                            return task;
                        } catch (TaskException e) {
                            // Gracefully handle failed tasks by adding them to the failed tasks list and returning null
                            // This operates under the assumption that a single failed task should not prevent other tasks from executing
                            failedTasks.add(task);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) // Filter out failed tasks
                    .filter(Task::isCompleted) // Filter out tasks that are not completed
                    .collect(Collectors.toList()); // Collect the completed tasks into a list
            taskQueue.clear(); // Clear the task queue
            return completedTasks;
        } catch (Exception e) { // Catch any runtime exceptions that may occur during task execution
            throw new ServerException("Failed to execute tasks", e);
        }
    }

    // Get the list of failed tasks
    public List<Task> getFailedTasks() {
        return failedTasks;
    }

    // Get the list of tasks
    public List<Task> getTasks() {
        return taskQueue;
    }

    // Return a copy of the server
    public Server copy() {
        Server copy = new Server();
        copy.taskQueue.addAll(taskQueue);
        copy.failedTasks.addAll(failedTasks);
        return copy;
    }
}
