package tests;

import math.IntegerRing;
import math.WorkingPolynomial;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WorkingPolynomialTest {
    // Test the WorkingPolynomial class with null coefficients
    @Test
    public void testNullCoefficients() {
        // Create a new WorkingPolynomial object with null coefficients
        // This should throw a NullPointerException
        assertThrows(NullPointerException.class, () -> WorkingPolynomial.from(null));
    }
    // Test the times method with null parameters
    @Test
    public void testTimesNull() {
        // Create a new WorkingPolynomial object with coefficients 1, 2, 3
        WorkingPolynomial<Integer> a = WorkingPolynomial.from(List.of(1, 2, 3));
        // Try to multiply the WorkingPolynomial object with a null parameter, which should throw a NullPointerException
        assertThrows(NullPointerException.class, () -> a.times(null, new IntegerRing()));
        assertThrows(NullPointerException.class, () -> a.times(WorkingPolynomial.from(List.of(1, 2, 3)), null));
    }

    // Test the times method with empty polynomials
    @Test
    public void testTimesEmpty() {
        // Create two new WorkingPolynomial objects with empty coefficients
        WorkingPolynomial<Integer> a = WorkingPolynomial.from(List.of());
        WorkingPolynomial<Integer> b = WorkingPolynomial.from(List.of());
        // Create a new WorkingPolynomial object with the product of the two WorkingPolynomial objects
        WorkingPolynomial<Integer> result = a.times(b, new IntegerRing());
        // The result should be an empty list
        assertEquals(true, result.getCoefficients().isEmpty());
    }

    @Test
    public void testToString() {
        // Create a new WorkingPolynomial object with coefficients 1, 2, 3
        WorkingPolynomial<Integer> a = WorkingPolynomial.from(List.of(1, 2, 3));
        // The string representation of the WorkingPolynomial object should be "1 + 2x + 3x^2"
        assertEquals("Polynomial [coefficients=[1, 2, 3]]", a.toString());
    }

    // Perform a stress test on the WorkingPolynomial class
    @Test
    public void stressTest() {
        // Stress test
        // Create two new WorkingPolynomial objects with 1000 coefficients
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }
        WorkingPolynomial<Integer> a = WorkingPolynomial.from(list);
        WorkingPolynomial<Integer> b = WorkingPolynomial.from(list);
        // The WorkingPolynomial objects should have 1000 coefficients
        assertEquals(1000, a.getCoefficients().size());
        assertEquals(1000, b.getCoefficients().size());
        // Create a new WorkingPolynomial object with the product of the two WorkingPolynomial objects
        WorkingPolynomial<Integer> result = a.times(b, new IntegerRing());
        // The result should have 1999 coefficients, as the product length should be 1000 + 1000 - 1
        assertEquals(1999, result.getCoefficients().size());
        // Create a new WorkingPolynomial object with the sum of the two WorkingPolynomial objects
        result = a.plus(b, new IntegerRing());
        // The result should have 1000 coefficients
        assertEquals(1000, result.getCoefficients().size());
    }
}
