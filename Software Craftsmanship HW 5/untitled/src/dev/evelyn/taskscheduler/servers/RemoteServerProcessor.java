package dev.evelyn.taskscheduler.servers;

import dev.evelyn.taskscheduler.CircuitBreaker;
import dev.evelyn.taskscheduler.exceptions.TaskException;
import dev.evelyn.taskscheduler.metrics.AlertSystem;
import dev.evelyn.taskscheduler.tasks.Task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.*;

// This class is the representation of a remote server processor--the part that is or could be running on another machine
public final class RemoteServerProcessor {
    private final int port;
    private boolean running;
    private final Random random = new Random();
    private final CircuitBreaker circuitBreaker;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public RemoteServerProcessor(int port, CircuitBreaker circuitBreaker) {
        this.port = port;
        this.running = false;
        this.circuitBreaker = circuitBreaker;
    }

    // Start the server processor
    public void start() {
        if (running) { // Prevent multiple instances of the server processor from running
            AlertSystem.sendAlertWarning("Remote task processor is already running");
            return;
        } else {
            running = true;
        }
        // Create the server socket and listen for incoming connections
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logInfo("Remote task processor is listening on port " + port);
            while (running) { // Loop until the server is stopped
                handleClientConnection(serverSocket);
            }
        } catch (IOException e) {
            logSevere("Server exception: " + e.getMessage());
        }
    }

    // Handle a single connection
    private void handleClientConnection(ServerSocket serverSocket) {
        try (Socket socket = serverSocket.accept(); // Accept incoming connection
             // Create input and output streams for the socket
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            // Receive, process, and send a single task
            Task task = receiveTask(in, socket);
            task = processTask(task);
            sendProcessedTask(task, out, socket);
        } catch (IOException | ClassNotFoundException e) { // Handle exceptions
            logSevere("Server exception: " + e.getMessage());
            circuitBreaker.reportFailure();
        }
    }

    // Receive a task from the client
    private Task receiveTask(ObjectInputStream in, Socket socket) throws IOException, ClassNotFoundException {
        Task task = (Task) in.readObject(); // Deserialize the task object
        logInfo("Received task " + task.getId() + " from " + socket.getInetAddress());
        return task;
    }

    // Send a processed task back to the client
    private void sendProcessedTask(Task task, ObjectOutputStream out, Socket socket) throws IOException {
        out.writeObject(task); // Serialize and send the task object
        out.flush();
        assert task != null;
        logInfo("Processed and sent task " + task.getId() + " back to " + socket.getInetAddress());
    }

    // Process a single task
    public Task processTask(Task task) {
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

    // Handle a failed task
    public void handleFailedTask(Task task, String message) {
        task.cleanup(); // Ensure the task cleans up resources upon failure
        AlertSystem.sendAlertError(message + ": " + task.getId()); // Log the failure message
    }

    // Submit a task to the executor service
    private Future<?> submitTask(Task task) {
        // Returns a Future object that represents the future result of the task
        return executor.submit(() -> {
            try {
                // Simulate task execution by sleeping for the estimated duration
                Thread.sleep(task.getEstimatedDuration().getDurationMs().longValue());
                task.execute();
            } catch (TaskException | InterruptedException e) {
                AlertSystem.sendAlertError("Task failed: " + task.getId());
                throw new RuntimeException(e); // Throw an exception to signal task failure
            }
        });
    }

    // Stop the server processor
    public void stop() {
        running = false;
        logInfo("Remote task processor stopped");
    }

    // Logging methods for different log levels (I wanted to have the prefix "[SERVER PROCESSOR]" in the logs for clarity)
    private void logInfo(String message) {
        AlertSystem.sendAlertInfo("[SERVER PROCESSOR] " + message);
    }

    private void logSevere(String message) {
        AlertSystem.sendAlertError("[SERVER PROCESSOR] " + message);
    }

    public int getLatencyMs() {
        return random.nextInt(100);
    }

    // Method called from RemoteServer that returns a "pulse" to indicate that the server processor is still running
    public boolean isStillRunning() {
        return running;
    }
}
