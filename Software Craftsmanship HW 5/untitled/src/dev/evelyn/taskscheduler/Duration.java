package dev.evelyn.taskscheduler;

import java.io.Serializable;
import java.math.BigInteger;

// Because this class must be immutable, we can make it final
public final class Duration implements Serializable {
    private final BigInteger durationMs;

    public Duration(BigInteger durationMs) {
        this.durationMs = durationMs;
    }

    // Factory method to create a Duration object from milliseconds
    public static Duration ofMillis(long millis) {
        return new Duration(BigInteger.valueOf(millis));
    }

    // Addition and subtraction operations
    // (note that they return new Duration objects rather than modifying the existing one)
    public Duration add(Duration secondDuration) {
        return new Duration(durationMs.add(secondDuration.durationMs));
    }

    public Duration subtract(Duration secondDuration) {
        return new Duration(durationMs.subtract(secondDuration.durationMs));
    }

    // Comparison operation, essentially a wrapper around BigInteger's compareTo method
    public int compareTo(Duration secondDuration) {
        return durationMs.compareTo(secondDuration.durationMs);
    }

    public BigInteger getDurationMs() {
        return durationMs;
    }

}
