package tests;

import math.IntegerRing;
import math.PolynomialRing;
import math.WorkingPolynomial;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PolynomialRingTest {
    // Test the instance method
    @Test
    public void testInstance() {
        // Good data
        // Create a new PolynomialRing object with a new IntegerRing object
        PolynomialRing<Integer> ring = PolynomialRing.instance(new IntegerRing());
        // The ring should not be null
        assertNotNull(ring);
    }

    // Test the instance method with a null ring
    @Test
    public void testInstanceWithNull() {
        // Bad data
        // Try to create a new PolynomialRing object with a null parameter
        // This should throw a NullPointerException
        assertThrows(NullPointerException.class, () -> PolynomialRing.instance(null));
    }

    // Test the constructor
    @Test
    public void testConstructor() {
        // Good data
        // Create a new PolynomialRing object with a new IntegerRing object
        PolynomialRing<Integer> ring = PolynomialRing.instance(new IntegerRing());
        // The ring should not be null
        assertNotNull(ring);
    }

    // Test the constructor with a null ring
    @Test
    public void testConstructorWithNull() {
        // Bad data
        // Try to create a new PolynomialRing object with a null parameter
        // This should throw a NullPointerException
        assertThrows(NullPointerException.class, () -> PolynomialRing.instance(null));
    }

    // Test the zero method
    @Test
    public void testZero() {
        // Good data
        // Create a new PolynomialRing object with a new IntegerRing object
        PolynomialRing<Integer> ring = PolynomialRing.instance(new IntegerRing());
        // The zero method should return a WorkingPolynomial object with an empty list
        assertEquals(true, ring.zero().getCoefficients().isEmpty());
    }

    // Test the identity method
    @Test
    public void testIdentity() {
        // Good data
        // Create a new PolynomialRing object with a new IntegerRing object
        PolynomialRing<Integer> ring = PolynomialRing.instance(new IntegerRing());
        // The identity method should return a WorkingPolynomial object with a list containing the identity of the base ring
        assertEquals(1, ring.identity().getCoefficients().get(0).intValue());
    }

    // Test the sum method
    @Test
    public void testSum() {
        // Good data
        // Create a new PolynomialRing object with a new IntegerRing object
        PolynomialRing<Integer> ring = PolynomialRing.instance(new IntegerRing());
        // Create two new WorkingPolynomial objects with coefficients 1, 2, 3 and 4, 5, 6
        WorkingPolynomial<Integer> a = WorkingPolynomial.from(List.of(1, 2, 3));
        WorkingPolynomial<Integer> b = WorkingPolynomial.from(List.of(4, 5, 6));
        // The sum of the two WorkingPolynomial objects should be a new WorkingPolynomial object with coefficients 5, 7, 9
        WorkingPolynomial<Integer> result = ring.sum(a, b);
        assertEquals(5, result.getCoefficients().get(0).intValue());
        assertEquals(7, result.getCoefficients().get(1).intValue());
        assertEquals(9, result.getCoefficients().get(2).intValue());
    }

    // Test the sum method with null parameters
    @Test
    public void testSumNull() {
        // Bad data
        // Create a new PolynomialRing object with a new IntegerRing object
        PolynomialRing<Integer> ring = PolynomialRing.instance(new IntegerRing());
        // Try to sum two WorkingPolynomial objects with a null parameter, which should throw a NullPointerException
        assertThrows(NullPointerException.class, () -> ring.sum(null, WorkingPolynomial.from(List.of(1, 2, 3))));
        assertThrows(NullPointerException.class, () -> ring.sum(WorkingPolynomial.from(List.of(1, 2, 3)), null));
    }

    // Test the product method
    @Test
    public void testProduct() {
        // Good data
        // Create a new PolynomialRing object with a new IntegerRing object
        PolynomialRing<Integer> ring = PolynomialRing.instance(new IntegerRing());
        // Create two new WorkingPolynomial objects with coefficients 1, 2, 3 and 4, 5, 6
        WorkingPolynomial<Integer> a = WorkingPolynomial.from(List.of(1, 2, 3));
        WorkingPolynomial<Integer> b = WorkingPolynomial.from(List.of(4, 5, 6));
        // The product of the two WorkingPolynomial objects should be a new WorkingPolynomial object with coefficients 4, 13, 28, 27, 18
        WorkingPolynomial<Integer> result = ring.product(a, b);
        assertEquals(4, result.getCoefficients().get(0).intValue());
        assertEquals(13, result.getCoefficients().get(1).intValue());
        assertEquals(28, result.getCoefficients().get(2).intValue());
        assertEquals(27, result.getCoefficients().get(3).intValue());
        assertEquals(18, result.getCoefficients().get(4).intValue());
    }

    // Test the product method with null parameters
    @Test
    public void testProductNull() {
        // Bad data
        // Create a new PolynomialRing object with a new IntegerRing object
        PolynomialRing<Integer> ring = PolynomialRing.instance(new IntegerRing());
        // Try to multiply two WorkingPolynomial objects with a null parameter, which should throw a NullPointerException
        assertThrows(NullPointerException.class, () -> ring.product(null, WorkingPolynomial.from(List.of(1, 2, 3))));
        assertThrows(NullPointerException.class, () -> ring.product(WorkingPolynomial.from(List.of(1, 2, 3)), null));
    }
}
