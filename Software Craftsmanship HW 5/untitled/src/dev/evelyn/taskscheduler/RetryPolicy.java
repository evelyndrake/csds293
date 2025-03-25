package dev.evelyn.taskscheduler;

public class RetryPolicy {
    public final int maxRetries;
    public final long delay;
    private final boolean exponentialBackoff; // Each retry will be delayed exponentially

    public RetryPolicy(int maxRetries, long delay, boolean exponentialBackoff) {
        this.maxRetries = maxRetries;
        this.delay = delay;
        this.exponentialBackoff = exponentialBackoff;
    }

    public boolean shouldRetry(int retryCount) {
        return retryCount < maxRetries;
    }

    public long getDelay(int attempt) { // Calculate the delay for the next retry
        if (exponentialBackoff) { // 2^(attempt * delay)
            return delay * (long) Math.pow(2, attempt - 1);
        }
        return delay;
    }
}
