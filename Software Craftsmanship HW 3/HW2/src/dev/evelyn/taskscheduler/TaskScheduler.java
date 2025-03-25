package dev.evelyn.taskscheduler;

import dev.evelyn.taskscheduler.exceptions.SchedulerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskScheduler {

    // List of servers and the index of the next free server
    private final List<Server> servers = new ArrayList<>();
    private int currentServerIndex = 0;

    // Add a server to the task scheduler
    public void addServer(Server server) throws SchedulerException {
        if (server == null) {
            throw new SchedulerException("Server cannot be null");
        }
        // Prevent modification of the original server by making a copy
        servers.add(server.copy());
    }

    // Schedule a task on the next available server
    public void scheduleTask(Task task) throws SchedulerException {
        // Throw exceptions if the task is null or there are no servers available
        if (task == null) {
            throw new SchedulerException("Task cannot be null");
        }
        if (servers.isEmpty()) {
            throw new SchedulerException("No servers available");
        }
        // Prevent modification of the original task by making a copy
        Task nextTask = new SimpleTask(task.getId(), task.getEstimatedDuration());
        servers.get(currentServerIndex).addTask(nextTask);
        // Update the index of the next free server
        // Note that the modulo operator (%) is used to wrap around the number of servers
        currentServerIndex = (currentServerIndex + 1) % servers.size();
    }

    // Execute all tasks on all servers
    public Map<Server, List<Task>> executeAll() throws SchedulerException {
        if (servers.isEmpty()) { // Ensure there are servers available
            throw new SchedulerException("No servers available");
        }
        Map<Server, List<Task>> results = new HashMap<>();
        for (Server server : servers) { // Execute tasks on each server
            try {
                List<Task> completedTasks = server.executeTasks();
                results.put(server, completedTasks);
            } catch (Exception e) {
                throw new SchedulerException("Failed to execute tasks on server", e);
            }
        }
        return results;
    }

    // Get the list of servers
    public List<Server> getServers() {
        return servers;
    }
}
