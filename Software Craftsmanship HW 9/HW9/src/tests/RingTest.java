package tests;

import math.IntegerRing;
import math.Ring;
import org.junit.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import math.WorkingPolynomial;

/**
 * @author Vidyut Veedgav and Evelyn Drake
 * A tester class for the Ring classes
 */
public class RingTest {

    // Lists to store the different types of numbers
    List<Integer> intList; 
    List<BigInteger> bigIntList;
    List<Double> doubleList = new ArrayList<>(Arrays.asList());

    // Test the sum method
    @Test
    public void testSum() {
        // Testing the Polynomial<Integer> type
        // Create a new IntegerRing object
        Ring<Integer> ring = new IntegerRing();

        // Boundary test case - empty list
        // Create two WorkingPolynomial objects with empty lists
        WorkingPolynomial<Integer> a = WorkingPolynomial.from(List.of());
        WorkingPolynomial<Integer> b = WorkingPolynomial.from(List.of());
        // Create a new WorkingPolynomial object with the sum of the two WorkingPolynomial objects
        WorkingPolynomial<Integer> result = WorkingPolynomial.from(a.plus(b, ring).getCoefficients());
        // The result should be an empty list
        assertEquals(true, result.getCoefficients().isEmpty());

        // Boundary test case - single element
        // Create two WorkingPolynomial objects with a single element
        a = WorkingPolynomial.from(List.of(1));
        b = WorkingPolynomial.from(List.of(1));
        // Create a new WorkingPolynomial object with the sum of the two WorkingPolynomial objects
        result = WorkingPolynomial.from(a.plus(b, ring).getCoefficients());
        // The result should be a list with a single element, 2
        assertEquals(true, result.getCoefficients().get(0).equals(2));

        // General test case - multiple elements
        // Create two WorkingPolynomial objects with multiple elements
        a = WorkingPolynomial.from(List.of(1, 2, 3));
        b = WorkingPolynomial.from(List.of(4, 5, 6));
        // Create a new WorkingPolynomial object with the sum of the two WorkingPolynomial objects
        result = WorkingPolynomial.from(a.plus(b, ring).getCoefficients());
        // The result should be a list with elements 5, 7, 9
        assertEquals(List.of(5, 7, 9), result.getCoefficients());

        // General test case - polynomials of different lengths
        // Create two WorkingPolynomial objects with different lengths
        a = WorkingPolynomial.from(List.of(1, 2, 3, 4, 5));
        b = WorkingPolynomial.from(List.of(4, 5, 6));
        // Create a new WorkingPolynomial object with the sum of the two WorkingPolynomial objects (a+b)
        result = WorkingPolynomial.from(a.plus(b, ring).getCoefficients());
        // The result should be a list with elements 5, 7, 9, 4, 5
        assertEquals(List.of(5, 7, 9, 4, 5), result.getCoefficients());
        // Create a new WorkingPolynomial object with the sum of the two WorkingPolynomial objects (a+b)
        result = WorkingPolynomial.from(b.plus(a, ring).getCoefficients());
        // Because addition should also work in reverse, the result should also be a list with elements 5, 7, 9, 4, 5
        assertEquals(List.of(5, 7, 9, 4, 5), result.getCoefficients());
    }

    // Test the product method
    @Test
    public void testProduct() {
        // Testing the Polynomial<Integer> type
        // Create a new IntegerRing object
        Ring<Integer> ring = new IntegerRing();

        // Boundary test case - empty list
        // Create two WorkingPolynomial objects with empty lists
        WorkingPolynomial<Integer> a = WorkingPolynomial.from(List.of());
        WorkingPolynomial<Integer> b = WorkingPolynomial.from(List.of());
        // Create a new WorkingPolynomial object with the product of the two WorkingPolynomial objects
        WorkingPolynomial<Integer> result = WorkingPolynomial.from(a.times(b, ring).getCoefficients());
        // The result should be an empty list
        assertEquals(true, result.getCoefficients().isEmpty());

        // Boundary test case - single element
        // Create two WorkingPolynomial objects with a single element
        a = WorkingPolynomial.from(List.of(1));
        b = WorkingPolynomial.from(List.of(1));
        // Create a new WorkingPolynomial object with the product of the two WorkingPolynomial objects
        result = WorkingPolynomial.from(a.times(b, ring).getCoefficients());
        // The result should be a list with a single element, 1
        assertEquals(List.of(1), result.getCoefficients());

        // General test case - multiple elements
        // Create two WorkingPolynomial objects with multiple elements
        a = WorkingPolynomial.from(List.of(1, 2, 3));
        b = WorkingPolynomial.from(List.of(4, 5, 6));
        // Create a new WorkingPolynomial object with the product of the two WorkingPolynomial objects
        result = WorkingPolynomial.from(a.times(b, ring).getCoefficients());
        // The result should be a list with elements 4, 13, 28, 27, 18
        assertEquals(List.of(4, 13, 28, 27, 18), result.getCoefficients());

        // General test case - polynomials of different lengths
        // Create two WorkingPolynomial objects with different lengths
        a = WorkingPolynomial.from(List.of(1, 2, 3, 4, 5));
        b = WorkingPolynomial.from(List.of(4, 5, 6));
        // Create a new WorkingPolynomial object with the product of the two WorkingPolynomial objects (a*b
        result = WorkingPolynomial.from(a.times(b, ring).getCoefficients());
        // The result should be a list with elements 4, 13, 28, 43, 58, 49, 30
        assertEquals(List.of(4, 13, 28, 43, 58, 49, 30), result.getCoefficients());
        // Create a new WorkingPolynomial object with the product of the two WorkingPolynomial objects (b*a)
        result = WorkingPolynomial.from(b.times(a, ring).getCoefficients());
        // Because multiplication should also work in reverse, the result should also be a list with elements 4, 13, 28, 43, 58, 49, 30
        assertEquals(List.of(4, 13, 28, 43, 58, 49, 30), result.getCoefficients());
    }
}

