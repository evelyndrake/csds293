package math;

import java.util.Objects;

/**
 * @author Vidyut Veedgav and Evelyn Drake
 * A class to demonstrate the concept of Rings on Integers
 */
public class IntegerRing implements Ring<Integer>{

    // Overriding the zero method
    @Override
    public Integer zero() {
        return 0;
    }

    // Overriding the identity method
    @Override
    public Integer identity() {
        return 1;
    }

    // Overriding the sum method
    @Override
    public Integer sum(Integer x, Integer y) {
        // Ensure that x and y are not null
        Objects.requireNonNull(x, "x cannot be null");
        Objects.requireNonNull(y, "y cannot be null");
        return x + y;
    }

    // Overriding the product method
    @Override
    public Integer product(Integer x, Integer y) {
        // Ensure that x and y are not null
        Objects.requireNonNull(x, "x cannot be null");
        Objects.requireNonNull(y, "y cannot be null");
        
        return x * y;
    }
}