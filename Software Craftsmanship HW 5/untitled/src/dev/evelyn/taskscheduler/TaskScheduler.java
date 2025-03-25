package dev.evelyn.taskscheduler;

import dev.evelyn.taskscheduler.exceptions.SchedulerException;
import dev.evelyn.taskscheduler.exceptions.SchedulerFullException;
import dev.evelyn.taskscheduler.exceptions.ServerException;
import dev.evelyn.taskscheduler.metrics.AlertSystem;
import dev.evelyn.taskscheduler.metrics.ErrorHandler;
import dev.evelyn.taskscheduler.metrics.PerformanceMonitor;
import dev.evelyn.taskscheduler.servers.Server;
import dev.evelyn.taskscheduler.tasks.Task;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class TaskScheduler {

    // Lists of local and remote servers
    private final List<Server> servers = Collections.synchronizedList(new ArrayList<>());
    // Map of servers to the total duration of the server
    // ConcurrentHashMap is a thread-safe hash map
    private final Map<Server, Long> serverDurations = new ConcurrentHashMap<>();
    // Map of servers to the number of tasks on the server
    private final Map<Server, Integer> serverTaskCount = new ConcurrentHashMap<>();
    // Priority queue of tasks to be scheduled
    // PriorityBlockingQueue is a thread-safe priority queue
    private final BlockingQueue<Task> tasksToSchedule = new PriorityBlockingQueue<>(
            1,
            Comparator.comparing(Task::getPriority) // Compare tasks based the Priority enum
    );
    // Set of completed tasks
    private final Set<String> completedTasks = Collections.synchronizedSet(new HashSet<>());
    // Retry policy
    private final RetryPolicy retryPolicy;
    // Performance monitor
    private final PerformanceMonitor performanceMonitor = new PerformanceMonitor();

    // Constructor with a custom retry policy
    public TaskScheduler(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    // Default constructor with a default retry policy
    public TaskScheduler() {
        this(new RetryPolicy(3, 1000, true));
    }


    // Add a server to the task scheduler
    // The synchronized keyword ensures that only one thread can access this method at a time
    public synchronized void addServer(Server server) throws SchedulerException {
        validateServer(server); // Ensure the server is not null
        servers.add(server);
        serverTaskCount.put(server, 0); // Initialize the number of tasks on the server to 0
        serverDurations.put(server, 0L); // Initialize the server duration
        performanceMonitor.addServer(server); // Add the server to the performance monitor
    }

    // Ensure a server is not null
    private void validateServer(Server server) throws SchedulerException {
        if (server == null) {
            throw new SchedulerException("Server cannot be null");
        }
    }

    // Ensure a task is not null (I created this method to handle validation of null tasks more consistently)
    private void validateTask(Task task) throws SchedulerException {
        if (task == null) {
            throw new SchedulerException("Task cannot be null");
        }
    }

    // Check if a task's dependencies are met
    private boolean dependenciesMet(Task task) throws SchedulerException {
        validateTask(task);
        Set<String> dependencies = task.getDependencies();
        // If the task has no dependencies or all dependencies are completed, return true
        if (dependencies == null || dependencies.isEmpty() || completedTasks.containsAll(dependencies)) {
            // Log success
            AlertSystem.sendAlertInfo("Task " + task.getId() + " has no unmet dependencies and will be scheduled");
            return true;
        } else {
            // Log a warning if the task has dependencies that are not met
            AlertSystem.sendAlertInfo("Task " + task.getId() + " will not be scheduled due to unmet dependencies: " + dependencies);
            return false;
        }
    }

    // Schedule a task on the next available server, synchronized to ensure consistency
    public synchronized void scheduleTask(Task task) throws SchedulerException, SchedulerFullException {
        validateTask(task); // Ensure the task is not null
        checkServersAvailability(); // Check for an available server
        if (!dependenciesMet(task)) { // Check if the task's dependencies are met
            throw new SchedulerException("Task dependencies not met");
        }
        try {
            // Offer the task to the thread-safe task queue
            tasksToSchedule.put(task);
        } catch (InterruptedException e) { // Handle thread interruption
            Thread.currentThread().interrupt();
            throw new SchedulerException("Task scheduling interrupted", e);
        }
        // Trigger task distribution to servers
        distributeTasks();
    }

    // Add a task to the list of completed tasks
    private synchronized void completeTask(Task task) {
        completedTasks.add(task.getId());
    }

    // Requeue all unfinished tasks from the servers back into the task queue
    private synchronized void requeueUnfinishedTasks() {
        servers.forEach(server -> {
            // Look through the server's tasks, filter unfinished ones, and add them to the task queue
            server.getTasks().stream()
                    .filter(task -> !task.isCompleted()) // Filter only unfinished tasks
                    .forEach(task -> {
                        try {
                            tasksToSchedule.put(task); // Requeue unfinished tasks
                        } catch (InterruptedException e) { // Handle thread interruption
                            Thread.currentThread().interrupt();
                        }
                    });
            // Clear tasks and reset the total duration and task count for each server
            server.clearTasks();
            serverTaskCount.put(server, 0);
            serverDurations.put(server, 0L);
        });
    }

    // Distribute tasks one by one to the least loaded server
    private synchronized void distributeTasks() throws SchedulerFullException {
        // Take all tasks out of the servers and put them back in the queue
        requeueUnfinishedTasks();
        while (!tasksToSchedule.isEmpty()) { // While there are tasks to schedule
            // Get the least loaded server
            Server leastLoadedServer = findLeastLoadedServer();
            // Assign the highest priority task to the least loaded server
            Task task = tasksToSchedule.poll();
            if (task != null) {
                leastLoadedServer.addTask(task);
                // Update the server's total load with the task's estimated duration
                long taskDuration = task.getEstimatedDuration().getDurationMs().longValue();
                serverDurations.put(leastLoadedServer, serverDurations.get(leastLoadedServer) + taskDuration);
                // Update the number of tasks on the server
                serverTaskCount.put(leastLoadedServer, serverTaskCount.get(leastLoadedServer) + 1);
            }
        }
    }

    // Find the least loaded server based on its load score (number of tasks + total duration of tasks)
    private synchronized Server findLeastLoadedServer() throws SchedulerFullException {
        return servers.stream()
                // Compare servers based on the calculated load score
                .min(Comparator.comparing(this::calculateServerLoad))
                .orElseThrow(() -> new SchedulerFullException("No servers available"));
    }

    // Calculate a load score for a server based on the number of tasks and the total duration of the tasks
    private long calculateServerLoad(Server server) {
        int numberOfTasks = serverTaskCount.getOrDefault(server, 0);
        long totalDuration = serverDurations.getOrDefault(server, 0L);
        // Weighted load score calculation
        int taskDurationWeight = 1;
        int taskAmountWeight = 1;
        return (taskAmountWeight * numberOfTasks) + (taskDurationWeight * totalDuration);
    }

    // Check if there are servers available
    private void checkServersAvailability() throws SchedulerFullException {
        if (servers.isEmpty()) {
            throw new SchedulerFullException("No servers available");
        }
    }

    // Execute tasks on a specific server
    private List<Task> executeTasksOnServer(Server server) throws SchedulerException {
        List<Task> completedTasks;
        try {
            completedTasks = server.executeTasks();
        } catch (ServerException e) {
            throw new SchedulerException("Failed to execute tasks on server", e);
        }
        // Handle tasks that have timed out by retrying them
        for (Task task : completedTasks) {
            if (!task.isCompleted()) {
                AlertSystem.sendAlertWarning("Task " + task.getId() + " timed out on server " + server);
            }
        }
        updateServerMetrics(server, completedTasks);
        return completedTasks;
    }

    // Update the server metrics after executing tasks
    private void updateServerMetrics(Server server, List<Task> completedTasks) {
        // Update the number of tasks on the server (reduce by the number of completed tasks)
        serverTaskCount.computeIfPresent(server, (s, count) -> count - completedTasks.size());
        // Update the total duration of the server
        // Subtract the total duration of the completed tasks from the server's total duration
        long totalCompletedDuration = completedTasks.stream()
                .mapToLong(t -> t.getEstimatedDuration().getDurationMs().longValue())
                .sum();
        // computeIfPresent method updates the value only if the key is present in the map
        serverDurations.computeIfPresent(server, (s, load) -> load - totalCompletedDuration);
    }

    // Retry the failed tasks on a particular server
    private void retryFailedTasks(Server server) throws SchedulerException {
        List<Task> failedTasks = server.getFailedTasks(); // Get failed tasks from the server
        for (Task task : failedTasks) {
            boolean taskSuccessful = retryTask(task, server); // Attempt to retry each failed task
            if (!taskSuccessful) {
                handleOutOfRetries(task); // Handle the case where the task fails after all retry attempts
            }
        }
    }

    // Attempt to retry a single task on a server
    private boolean retryTask(Task task, Server server) throws SchedulerException {
        int attempt = 1;
        boolean taskSuccessful = false;
        // Retry the task until it succeeds or the retry policy is exhausted
        while (retryPolicy.shouldRetry(attempt) && !taskSuccessful) {
            try {
                logRetryAttempt(task, server, attempt); // Log retry attempt
                task.execute(); // Try executing the task
                taskSuccessful = true; // Task executed successfully
            } catch (Exception e) {
                logRetryFailure(task, server, attempt, e); // Log failure of the retry attempt
                attempt = handleRetryDelay(attempt); // Handle the delay before next retry, if any
            }
        }
        return taskSuccessful;
    }

    // Log a retry attempt for a task
    private void logRetryAttempt(Task task, Server server, int attempt) {
        AlertSystem.sendAlertWarning("Retrying task " + task.getId() + " on server " + server + " (attempt " + attempt + ")");
    }

    // Log a failure during retry
    private void logRetryFailure(Task task, Server server, int attempt, Exception e) {
        AlertSystem.sendAlertError("Task " + task.getId() + " failed on server " + server + " (attempt " + attempt + ") - Error: " + e.getMessage());
    }

    // Handle delay between retry attempts, and check for interruption
    private int handleRetryDelay(int attempt) throws SchedulerException {
        if (retryPolicy.shouldRetry(attempt)) { // Check if there are more retries left
            long delay = retryPolicy.getDelay(attempt);
            try {
                Thread.sleep(delay); // Delay before the next retry
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new SchedulerException("Task retry interrupted", ex);
            }
            return attempt + 1; // Increment attempt after successful delay
        }
        return attempt;
    }

    // Handle the scenario where a task fails all retry attempts
    private void handleOutOfRetries(Task task) throws SchedulerException {
        validateTask(task);
        AlertSystem.sendAlertError("Task " + task.getId() + " failed after all retries");
        throw new SchedulerException("Task " + task.getId() + " failed after all retries");
    }

    // Execute all tasks on all servers
    public Map<Server, List<Task>> executeAll() throws SchedulerFullException {
        // Distribute the tasks and ensure servers are available
        distributeTasks();
        checkServersAvailability();
        // Execute tasks on all servers and collect the results
        Map<Server, List<Task>> results = new HashMap<>();
         servers.forEach(server -> {
             try {
                results.put(server, executeTasksOnServer(server));
                retryFailedTasks(server);
             } catch (SchedulerException e) {
                 AlertSystem.sendAlertError("Failed to execute tasks on server: " + server);
             }
         });
        // Add the completed tasks to the set of completed tasks
        results.values().forEach(completedTasks -> completedTasks.forEach(this::completeTask));
        // Display error metrics and performance metrics
        ErrorHandler.displayAllMetrics();
        performanceMonitor.checkAlerts();
        return results;
    }

    // Get the list of servers with defensive copying
    public List<Server> getServers() {
        return new ArrayList<>(servers);
    }

    // Get the load of a specific server (useful for testing)
    public long getServerDurations(Server server) {
        return serverDurations.getOrDefault(server, 0L);
    }
}
