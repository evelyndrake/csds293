package dev.evelyn.taskscheduler.metrics;

import dev.evelyn.taskscheduler.servers.RemoteServer;
import dev.evelyn.taskscheduler.servers.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("FieldCanBeLocal")
public class PerformanceMonitor {
    // Thresholds
    private final double SUCCESS_RATE_THRESHOLD = 90.0;
    private final double FAILURE_RATE_THRESHOLD = 10.0;
    private final double AVERAGE_TIME_THRESHOLD = 100.0;
    private final double DISTRIBUTION_BALANCE_THRESHOLD = 50.0;
    // List of servers
    private final List<Server> servers;
    private boolean printMetrics = false;

    public PerformanceMonitor() {
        servers = new ArrayList<>();
    }

    // Optional constructor to add servers at initialization
    public PerformanceMonitor(List<Server> servers) {
        this.servers = servers;
    }

    public void addServer(Server server) {
        servers.add(server);
    }

    // Getters for performance metrics:
    // I used ternary operators to simplify these short calculations
    // Each one starts by ensuring that the number we're dividing by is not zero

    // Calculate average execution time
    public synchronized double getAverageExecutionTime() {
        // Use streams to get the total execution time and total tasks executed, rounded to 2 decimal places
        long totalExecutionTime = servers.stream().mapToLong(Server::getTotalExecutionTime).sum();
        long totalTasksExecuted = servers.stream().mapToLong(Server::getTotalTasksExecuted).sum();
        // Total execution time divided by total tasks executed
        return Math.round(
                ((totalTasksExecuted > 0) ? (double) totalExecutionTime / totalTasksExecuted : 0.00) * 100.00) / 100.00;
    }

    // Calculate success rate
    public synchronized double getSuccessRate() {
        // Get the total completed tasks and total tasks executed, rounded to 2 decimal places
        long totalCompletedTasks = servers.stream().mapToLong(Server::getTotalCompletedTasks).sum();
        long totalTasksExecuted = servers.stream().mapToLong(Server::getTotalTasksExecuted).sum();
        // Total completed tasks divided by total tasks executed
        return Math.round(
                ((totalTasksExecuted > 0) ? (double) totalCompletedTasks / totalTasksExecuted * 100.00 : 0.00) * 100.00) / 100.00;
    }

    // Calculate failure rate
    public synchronized double getFailureRate() {
        // Use streams to get the total failed tasks and total tasks executed, rounded to 2 decimal places
        long totalFailedTasks = servers.stream().mapToLong(Server::getTotalFailedTasks).sum();
        long totalTasksExecuted = servers.stream().mapToLong(Server::getTotalTasksExecuted).sum();
        // Total failed tasks divided by total tasks executed
        return Math.round(
                ((totalTasksExecuted > 0) ? (double) totalFailedTasks / totalTasksExecuted * 100.00 : 0.00) * 100.00) / 100.00;
    }

    // Get server task distribution deviation (coefficient of variation)
    public synchronized double getTaskDistributionBalance() {
        // Get the number of tasks executed by each server
        List<Long> tasksPerServer = servers.stream().map(Server::getTotalTasksExecuted).collect(Collectors.toList());
        // Calculate mean tasks per server
        double mean = tasksPerServer.stream().mapToLong(Long::longValue).average().orElse(0.00);
        // If there's only one server or no tasks, return 0 (no imbalance)
        if (tasksPerServer.size() <= 1 || mean == 0) {
            return 0.0;
        }
        // Calculate the variance (standard deviation squared)
        double variance = tasksPerServer.stream()
                .mapToDouble(tasks -> Math.pow(tasks - mean, 2))
                .average().orElse(0.0);
        // Calculate the standard deviation
        double standardDeviation = Math.sqrt(variance);
        // Calculate the coefficient of variation (CV = standard deviation / mean)
        return standardDeviation / mean;
    }

    // Check alerts and log metrics
    public synchronized void checkAlerts() {
        printMetrics = false;
        double successRate = getSuccessRate();
        double failureRate = getFailureRate();
        double averageTime = getAverageExecutionTime();
        double distributionBalance = getTaskDistributionBalance();

        // Log metrics
        logMetrics(successRate, failureRate, averageTime, distributionBalance);
        // Alert checks
        checkAndSendAlert(successRate < SUCCESS_RATE_THRESHOLD,
                "Success rate is below threshold of " + SUCCESS_RATE_THRESHOLD + "% at " + successRate + "%");

        checkAndSendAlert(failureRate > FAILURE_RATE_THRESHOLD,
                "Failure rate is above threshold of " + FAILURE_RATE_THRESHOLD + "% at " + failureRate + "%");

        checkAndSendAlert(averageTime > AVERAGE_TIME_THRESHOLD,
                "Average execution time is above threshold of " + AVERAGE_TIME_THRESHOLD + "ms at " + averageTime + "ms");

        checkAndSendAlert(distributionBalance > DISTRIBUTION_BALANCE_THRESHOLD,
                "Task distribution is imbalanced!");
        checkRemoteServers();
        if (printMetrics) {
            ErrorHandler.reportPerformanceWarning("Unacceptable performance metrics detected! Printing error metrics.");
            ErrorHandler.displayAllMetrics();
        }
    }

    // Helper method to check conditions and send alerts
    private void checkAndSendAlert(boolean condition, String message) {
        if (condition) {
            AlertSystem.sendAlertWarning(message);
        }
    }

    // Check for unresponsive remote servers
    private void checkRemoteServers() {
        servers.stream()
                .filter(server -> server instanceof RemoteServer)
                .map(server -> (RemoteServer) server)
                .filter(server -> !server.isResponsive())
                .forEach(server -> AlertSystem.sendAlertError("Server " + server + " is unresponsive!"));
    }

    // Log metrics for performance
    private void logMetrics(double successRate, double failureRate, double averageTime, double distributionBalance) {
        AlertSystem.sendAlertInfo("--------------------------------------");
        AlertSystem.sendAlertInfo("Success rate: " + successRate + "%");
        AlertSystem.sendAlertInfo("Failure rate: " + failureRate + "%");
        AlertSystem.sendAlertInfo("Distribution deviation: " + distributionBalance + "%");
        AlertSystem.sendAlertInfo("Average execution time: " + averageTime + "ms");
        AlertSystem.sendAlertInfo("--------------------------------------");
    }
}