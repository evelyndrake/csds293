package dev.evelyn.taskscheduler.servers;

import dev.evelyn.taskscheduler.CircuitBreaker;
import dev.evelyn.taskscheduler.metrics.AlertSystem;
import dev.evelyn.taskscheduler.tasks.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// This local class represents and handles the connection to a remote task processor, which could be hosted on another computer
public class RemoteServer extends Server {
    // Connection details
    private final String address;
    private final int port;
    /* Instance of the remote server processor:
    (think of the processor as the actual remote server that could be running on another computer--the RemoteServer
    class is just a representation of that remote server on the main computer hosting the TaskScheduler application)
    */
    private final RemoteServerProcessor processor;
    // Circuit breaker to handle server failures
    private final CircuitBreaker circuitBreaker = new CircuitBreaker(0, 100);

    public RemoteServer(String address, int port) {
        this.address = address;
        this.port = port;
        processor = new RemoteServerProcessor(port, circuitBreaker);
        // Start the processor on a new thread to listen for incoming tasks
        Thread thread = new Thread(processor::start);
        thread.start();
    }

    // Handle the case where the circuit breaker is open
    private Task handleCircuitBreaker(Task task) {
        // Sleep thread for the circuit breaker timeout
        try {
            Thread.sleep(circuitBreaker.getTimeout());
        } catch (InterruptedException e) {
            logWarning("Thread interrupted while waiting for circuit breaker to close");
        }
        // If the circuit breaker is still open, fail the task
        return failTask(task, "Circuit breaker is open, timed out for " + circuitBreaker.getTimeout() + " ms");
    }

    // Send and receive a task to and from the remote server
    private Task sendAndReceiveTask(Task task, ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException, InterruptedException {
        // Serialize and send the task to the remote server
        out.writeObject(task);
        out.flush();
        // Simulate latency from the network
        int latency = processor.getLatencyMs();
        Thread.sleep(latency);
        logInfo("Waiting " + latency + " ms for task processing on " + address + ":" + port);
        // Add latency to the task's execution time for the total time it took to process the task
        totalExecutionTime += latency;
        totalExecutionTime += task.getEstimatedDuration().getDurationMs().longValue();
        // Receive and deserialize the processed task
        Task processedTask = (Task) in.readObject();
        logInfo("Received processed task from " + address + ":" + port);
        return processedTask;
    }

    // The processTask method, which is called from the Server class in the executeTasks method (which did not change)
    @Override
    Task processTask(Task task) {
        totalTasksExecuted++; // Increment total tasks executed
        if (!circuitBreaker.canSendRequest()) { // Handle circuit breaker if necessary
            return handleCircuitBreaker(task);
        }
        // Try to connect to the remote server and send the task
        try (Socket socket = new Socket(address, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            return sendAndReceiveTask(task, out, in);
        } catch (IOException | ClassNotFoundException e) { // Handle exceptions
            return failTask(task, "Task processing failed on " + address + ":" + port + ": " + e.getMessage());
        } catch (InterruptedException e) { // Handle thread interruption
            return failTask(task, "Thread interrupted while waiting for task processing on " + address + ":" + port);
        }
    }

    // Handle a failed task
    private Task failTask(Task task, String message) {
        // Report the failure to the circuit breaker, which will open if there are too many failures in a given time frame
        circuitBreaker.reportFailure();
        logSevere(message);
        failedTasks.add(task); // Add task to failed tasks list
        totalFailedTasks++; // Increment total failed tasks
        return null; // Return null on failure
    }

    // Handle shutdown of the remote server processor
    public void stop() {
        processor.stop();
        AlertSystem.sendAlertInfo("Disconnected from server " + address + ":" + port);
    }

    // Logging methods for different log levels (I wanted to have the prefix "[REMOTE SERVER]" in the logs for clarity)
    private void logInfo(String message) {
        AlertSystem.sendAlertInfo("[REMOTE SERVER] " + message);
    }

    private void logWarning(String message) {
        AlertSystem.sendAlertWarning("[REMOTE SERVER] " + message);
    }

    private void logSevere(String message) {
        AlertSystem.sendAlertError("[REMOTE SERVER] " + message);
    }

    // Ensure that the remote server is responsive and still turned on
    public boolean isResponsive() {
        return (circuitBreaker.canSendRequest() && processor.isStillRunning());
    }

    public String toString() {
        return "RemoteServer[" + address + ":" + port + "]";
    }
}
