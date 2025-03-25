package dev.evelyn.taskscheduler.tests;

import dev.evelyn.taskscheduler.CircuitBreaker;
import dev.evelyn.taskscheduler.Duration;
import dev.evelyn.taskscheduler.RetryPolicy;
import dev.evelyn.taskscheduler.TaskScheduler;
import dev.evelyn.taskscheduler.exceptions.SchedulerException;
import dev.evelyn.taskscheduler.exceptions.SchedulerFullException;
import dev.evelyn.taskscheduler.exceptions.ServerException;
import dev.evelyn.taskscheduler.exceptions.TaskException;
import dev.evelyn.taskscheduler.servers.RemoteServer;
import dev.evelyn.taskscheduler.servers.RemoteServerProcessor;
import dev.evelyn.taskscheduler.servers.Server;
import dev.evelyn.taskscheduler.tasks.FailingTask;
import dev.evelyn.taskscheduler.tasks.SimpleTask;
import dev.evelyn.taskscheduler.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;

public class RemoteServerTest {

    private RemoteServerProcessor remoteServerProcessor;
    private ExecutorService mockExecutorService;
    private CircuitBreaker mockCircuitBreaker;
    private Socket mockSocket;
    private Task mockTask;

    // I ran into issues with the ports not being freed by my OS in time for the next tests,
    // so I used different ports for each server instance

    @Test
    public void testProcessTask() {
        // Make a remote server
        RemoteServer server = new RemoteServer("localhost", 8081);
        Task task1 = new SimpleTask("1", Duration.ofMillis(100));
        server.addTask(task1);
        Task task2 = new SimpleTask("2", Duration.ofMillis(200));
        server.addTask(task2);
        List<Task> completedTasks;
        try {
            completedTasks = server.executeTasks();
        } catch (ServerException e) {
            throw new RuntimeException(e);
        }
        // Check that the tasks were completed
        assert completedTasks != null;
        assert completedTasks.size() == 2;
        // Stop the server
        server.stop();
    }

    @Test
    public void testWithScheduler() throws SchedulerFullException {
        TaskScheduler scheduler = new TaskScheduler();
        RemoteServer server = new RemoteServer("localhost", 8082);
        Task task1 = new SimpleTask("1", Duration.ofMillis(100));
        server.addTask(task1);
        Task task2 = new SimpleTask("2", Duration.ofMillis(200));
        server.addTask(task2);
        try {
            scheduler.addServer(server);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        scheduler.executeAll();
        // Check that the tasks were completed
        assert server.getTasks().isEmpty();
        // Stop the server
        server.stop();
    }

    @Test
    public void testBothServerTypes() throws SchedulerException, SchedulerFullException {
        TaskScheduler scheduler = new TaskScheduler();
        RemoteServer server1 = new RemoteServer("localhost", 8083);
        Server server2 = new Server();
        scheduler.addServer(server1);
        scheduler.addServer(server2);
        Task task1 = new SimpleTask("1", Duration.ofMillis(100));
        Task task2 = new SimpleTask("2", Duration.ofMillis(200));
        try {
            scheduler.scheduleTask(task1);
            scheduler.scheduleTask(task2);
        } catch (SchedulerFullException e) {
            throw new RuntimeException(e);
        }
        scheduler.executeAll();
        // Check that the tasks were completed
        assert server1.getTasks().isEmpty();
        assert server2.getTasks().isEmpty();
        // Stop the remote server
        server1.stop();
    }

    @Test
    public void testMultipleServers() throws SchedulerException, SchedulerFullException {
        TaskScheduler scheduler = new TaskScheduler();
        RemoteServer server1 = new RemoteServer("localhost", 8084);
        RemoteServer server2 = new RemoteServer("localhost", 8085);
        scheduler.addServer(server1);
        scheduler.addServer(server2);
        Task task1 = new SimpleTask("1", Duration.ofMillis(100));
        Task task2 = new SimpleTask("2", Duration.ofMillis(200));
        try {
            scheduler.scheduleTask(task1);
            scheduler.scheduleTask(task2);
        } catch (SchedulerFullException e) {
            throw new RuntimeException(e);
        }
        scheduler.executeAll();
        // Check that the tasks were completed
        assert server1.getTasks().isEmpty();
        assert server2.getTasks().isEmpty();
        // Stop the remote servers
        server1.stop();
        server2.stop();
    }

    @Test
    public void testCircuitBreaker() throws SchedulerFullException {
        TaskScheduler scheduler = new TaskScheduler(new RetryPolicy(2, 100, false));
        RemoteServer server = new RemoteServer("localhost", 8086);
        Task task1 = new FailingTask("1", Duration.ofMillis(100), 10);
        server.addTask(task1);
        Task task2 = new FailingTask("2", Duration.ofMillis(200), 10);
        server.addTask(task2);
        try {
            scheduler.addServer(server);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        scheduler.executeAll();
    }

    @Test
    public void testTaskWithDuration() {
        // Just mentioning this here--I realized it made way more sense to have the timeout also be a Duration,
        // so I modified the constructor to allow for this
        Task task = new SimpleTask("1", Duration.ofMillis(100), Duration.ofMillis(50));
        TaskScheduler scheduler = new TaskScheduler();
        RemoteServer server = new RemoteServer("localhost", 8087);
        server.addTask(task);
        try {
            scheduler.addServer(server);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        try {
            scheduler.executeAll();
        } catch (SchedulerFullException e) {
            fail("Scheduler full exception");
        }
        // Check that the task was not completed, proving that the timeout functionality works across the network
        assert server.getTasks().isEmpty();
    }

    // MOCKITO TESTS

    @BeforeEach
    public void setUp() {
        // Create mock objects
        mockExecutorService = mock(ExecutorService.class);
        mockCircuitBreaker = mock(CircuitBreaker.class);
        mockSocket = mock(Socket.class);
        mockTask = mock(Task.class);
        // Mock task ID and other properties
        when(mockTask.getId()).thenReturn("task-123");
        when(mockTask.getEstimatedDuration()).thenReturn(Duration.ofMillis(1000));
        // Create RemoteServerProcessor with injected mocks
        remoteServerProcessor = new RemoteServerProcessor(8080, mockCircuitBreaker);
    }

    @AfterEach
    public void tearDown() {
        // Reset mock objects
        reset(mockExecutorService, mockCircuitBreaker, mockSocket, mockTask);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessTask_Success() throws Exception {
        // Mock successful task execution
        Future<Task> mockFuture = (Future<Task>) mock(Future.class);
        // Mock behavior for future.get() to return the mock task
        doReturn(mockTask).when(mockFuture).get(anyLong(), any(TimeUnit.class)); // Fix potential issue with TimeUnit
        // Mock ExecutorService to return the mock future
        when(mockExecutorService.submit(any(Callable.class))).thenReturn(mockFuture);
        // Because we want this task to succeed, we have to give it a valid timeout
        when(mockTask.getTimeout()).thenReturn(5000L);
        // Mock behavior of the task
        doNothing().when(mockTask).execute();  // Ensure execute() runs without throwing an error
        // Invoke processTask on remoteServerProcessor
        Task result = remoteServerProcessor.processTask(mockTask);
        // Assert that the task was completed successfully
        assertNotNull(result);
        assertEquals(mockTask, result);
        // Verify that execute was called on the task
        verify(mockTask, times(1)).execute();
    }

    @Test
    public void testProcessTask_ExecutionFailure() throws Exception {
        // Mock task execution failure
        Future<Task> mockFuture = (Future<Task>) mock(Future.class);
        when(mockFuture.get(anyLong(), any())).thenThrow(new ExecutionException(new TaskException("Task execution failed")));
        when(mockExecutorService.submit(any(Callable.class))).thenReturn(mockFuture);
        Task result = remoteServerProcessor.processTask(mockTask);
        // Assert that the task failed
        assertNull(result);
        verify(mockTask).cleanup();  // Ensure that cleanup is called on failure
    }

    @Test
    public void testProcessTask_Timeout() throws Exception {
        // Mock task timeout
        Future<Task> mockFuture = (Future<Task>) mock(Future.class);
        when(mockFuture.get(anyLong(), any())).thenThrow(new TimeoutException());
        when(mockExecutorService.submit(any(Callable.class))).thenReturn(mockFuture);
        Task result = remoteServerProcessor.processTask(mockTask);
        // Assert that the task failed due to timeout
        assertNull(result);
        verify(mockTask).cleanup();  // Ensure that cleanup is called on timeout
    }

    @Test
    public void testProcessTask_Interrupted() throws Exception {
        // Mock task interrupted exception
        Future<Task> mockFuture = (Future<Task>) mock(Future.class);
        when(mockFuture.get(anyLong(), any())).thenThrow(new InterruptedException());
        when(mockExecutorService.submit(any(Callable.class))).thenReturn(mockFuture);
        Task result = remoteServerProcessor.processTask(mockTask);

        // Assert that the task failed due to interruption
        assertNull(result);
        verify(mockTask).cleanup();  // Ensure that cleanup is called on interruption
    }

    @Test
    public void testProcessTask_UnknownException() throws Exception {
        // Mock unknown exception
        Future<Task> mockFuture = (Future<Task>) mock(Future.class);
        when(mockFuture.get(anyLong(), any())).thenThrow(new RuntimeException("Unknown exception"));
        when(mockExecutorService.submit(any(Callable.class))).thenReturn(mockFuture);
        Task result = remoteServerProcessor.processTask(mockTask);

        // Assert that the task failed due to an unknown exception
        assertNull(result);
        verify(mockTask).cleanup();  // Ensure that cleanup is called on unknown exception
    }

    @Test
    public void testProcessTask_ExecutionException() throws Exception {
        // Mock task execution exception
        Future<Task> mockFuture = (Future<Task>) mock(Future.class);
        when(mockFuture.get(anyLong(), any())).thenThrow(new ExecutionException(new TaskException("Task execution failed")));
        when(mockExecutorService.submit(any(Callable.class))).thenReturn(mockFuture);
        Task result = remoteServerProcessor.processTask(mockTask);
        // Assert that the task failed
        assertNull(result);
        verify(mockTask).cleanup();  // Ensure that cleanup is called on failure
    }

    @Test
    public void testProcessTask_InterruptedException() throws Exception {
        // Mock task interrupted exception
        Future<Task> mockFuture = (Future<Task>) mock(Future.class);
        when(mockFuture.get(anyLong(), any())).thenThrow(new InterruptedException());
        when(mockExecutorService.submit(any(Callable.class))).thenReturn(mockFuture);
        Task result = remoteServerProcessor.processTask(mockTask);
        // Assert that the task failed due to interruption
        assertNull(result);
        verify(mockTask).cleanup();  // Ensure that cleanup is called on interruption
    }

}
