package dev.evelyn.taskscheduler.tests;

import dev.evelyn.taskscheduler.Duration;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

// Tests all functionality of the Duration class

class DurationTest {
    @org.junit.jupiter.api.Test
    void ofMillis() {
        // Test the factory method
        Duration duration1 = Duration.ofMillis(1000);
        assert duration1.getDurationMs().equals(new BigInteger("1000"));
    }

    @org.junit.jupiter.api.Test
    void add() {
        // Test the add method
        Duration duration1 = Duration.ofMillis(1000);
        Duration duration2 = Duration.ofMillis(2000);
        Duration duration3 = duration1.add(duration2);
        assert duration3.getDurationMs().equals(new BigInteger("3000"));
    }


    @org.junit.jupiter.api.Test
    void subtract() {
        // Test the subtract method
        Duration duration1 = Duration.ofMillis(3000);
        Duration duration2 = Duration.ofMillis(2000);
        Duration duration3 = duration1.subtract(duration2);
        assert duration3.getDurationMs().equals(new BigInteger("1000"));
    }

    @org.junit.jupiter.api.Test
    void compareTo() {
        // Test the compareTo method
        Duration duration1 = Duration.ofMillis(3000);
        Duration duration2 = Duration.ofMillis(2000);
        assert duration1.compareTo(duration2) > 0;
    }
}