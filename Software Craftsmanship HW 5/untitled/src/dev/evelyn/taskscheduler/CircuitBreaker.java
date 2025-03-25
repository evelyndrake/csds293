package dev.evelyn.taskscheduler;

import dev.evelyn.taskscheduler.metrics.AlertSystem;

public class CircuitBreaker {
    private final int acceptableFailures;
    private final long timeout;
    private int failures;
    private long lastFailureTime;
    private boolean open;

    // This class represents a circuit breaker that can be used to prevent sending requests to a remote server if it is experiencing issues
    public CircuitBreaker(int acceptableFailures, long timeout) {
        this.acceptableFailures = acceptableFailures;
        this.timeout = timeout;
        this.failures = 0;
        this.lastFailureTime = 0;
        this.open = false;
    }

    public synchronized boolean canSendRequest() {
        if (open) { // If the circuit breaker is open, check if it should be closed
            // It should be closed if the timeout has passed since the last failure
            if (System.currentTimeMillis() - lastFailureTime > timeout) {
                open = false;
                failures = 0;
                AlertSystem.sendAlertInfo("Circuit breaker closed");
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public synchronized void reportFailure() { // Report a failure to the circuit breaker
        failures++;
        lastFailureTime = System.currentTimeMillis();
        if (failures >= acceptableFailures) { // If the number of failures exceeds the acceptable threshold, open the circuit breaker
            AlertSystem.sendAlertWarning("Circuit breaker opened");
            open = true;
        }
    }

    public long getTimeout() {
        return timeout;
    }


}
