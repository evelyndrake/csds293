package tests;

import math.IntegerRing;
import org.junit.Test;
import static org.junit.Assert.*;
import math.Rings;

import java.util.Arrays;
import java.util.List;

public class RingsTest {
    // Test method to instantiate the Rings class--even though it's a static class, I can't get full coverage otherwise
    @Test
    public void testRings() {
        Rings rings = new Rings();
        assertNotNull(rings);
    }

    // Test the reduce method with various operators
    @Test
    public void testReduce() {
        // Good data
        // Test with sum
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        Integer result = Rings.reduce(list, 0, Integer::sum);
        assertEquals(10, result.intValue());
        // Test with product
        result = Rings.reduce(list, 1, (a, b) -> a * b);
        assertEquals(24, result.intValue());
        // Test with max (max element should be 4)
        result = Rings.reduce(list, Integer.MIN_VALUE, Integer::max);
        assertEquals(4, result.intValue());
        // Test with min (min element should be 1)
        result = Rings.reduce(list, Integer.MAX_VALUE, Integer::min);
        assertEquals(1, result.intValue());
    }

    // Test the reduce method with combinations of null parameters
    @Test
    public void testReduceNull() {
        // Bad data
        // Test with a null list
        assertThrows(NullPointerException.class, () -> Rings.reduce(null, 0, Integer::sum));
        // Test with a null identity
        assertThrows(NullPointerException.class, () -> Rings.reduce(Arrays.asList(1, 2, 3, 4), null, Integer::sum));
        // Test with a null operator
        assertThrows(NullPointerException.class, () -> Rings.reduce(Arrays.asList(1, 2, 3, 4), 0, null));
    }

    // Test the sum method
    @Test
    public void testSum() {
        // Good data
        // Test with two integers
        List<Integer> intList = Arrays.asList(1, 2);
        IntegerRing ring = new IntegerRing();
        Integer result1 = Rings.sum(intList, ring);
        assertEquals(3, result1.intValue());
        // Boundary test case - single element
        intList = Arrays.asList(1);
        Integer result2 = Rings.sum(intList, ring);
        assertEquals(1, result2.intValue());
        // Bad data
        // Boundary test case - empty list
        intList = Arrays.asList();
        Integer result3 = Rings.sum(intList, ring);
        assertEquals(0, result3.intValue());
    }

    // Test the sum method with null parameters
    @Test
    public void testSumNull() {
        // Bad data
        // Test with a null list
        IntegerRing ring = new IntegerRing();
        assertThrows(NullPointerException.class, () -> Rings.sum(null, ring));
        // Test with a null ring
        List<Integer> intList = Arrays.asList(1, 2);
        assertThrows(NullPointerException.class, () -> Rings.sum(intList, null));
    }

    // Test the product method
    @Test
    public void testProduct() {
        // Good data
        // Test with two integers
        List<Integer> intList = Arrays.asList(1, 2);
        IntegerRing ring = new IntegerRing();
        Integer result1 = Rings.product(intList, ring);
        assertEquals(2, result1.intValue());
        // Boundary test case - single element
        intList = Arrays.asList(1);
        Integer result2 = Rings.product(intList, ring);
        assertEquals(1, result2.intValue());
        // Bad data
        // Boundary test case - empty list
        intList = Arrays.asList();
        Integer result3 = Rings.product(intList, ring);
        assertEquals(1, result3.intValue());
    }

    // Test the product method with null parameters
    @Test
    public void testProductNull() {
        // Bad data
        // Test with a null list
        IntegerRing ring = new IntegerRing();
        assertThrows(NullPointerException.class, () -> Rings.product(null, ring));
        // Test with a null ring
        List<Integer> intList = Arrays.asList(1, 2);
        assertThrows(NullPointerException.class, () -> Rings.product(intList, null));
    }

}
