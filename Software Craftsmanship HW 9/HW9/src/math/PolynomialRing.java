package math;

import java.util.List;
import java.util.Objects;

/**
 * @author Vidyut Veedgav and Evelyn Drake
 * A class to demonstrate the concept of Rings on Polynomials
 */
public final class PolynomialRing<T> implements Ring<WorkingPolynomial<T>> {

    // Private field to store a Ring object
    private final Ring<T> baseRing;

    /**
     * Constructor for the PolynomialRing class
     * @param ring a ring
     */
    private PolynomialRing(Ring<T> ring) {
        // Ensure that ring is not null
        Objects.requireNonNull(ring, "ring cannot be null");
        this.baseRing = ring;
    }

    /**
     * Static method to return a new polynomial
     * @param <T> a ring
     * @return a new polynomial
     */
    public static <T> PolynomialRing<T> instance(Ring<T> ring) {
        // Ensure that ring is not null
        Objects.requireNonNull(ring, "ring cannot be null");
        return new PolynomialRing<>(ring);
    }

    // Overriding the zero method
    @Override
    public WorkingPolynomial<T> zero() {
        return WorkingPolynomial.from(List.of());
    }

    // Overriding the identity method
    @Override
    public WorkingPolynomial<T> identity() {
        return WorkingPolynomial.from(List.of(baseRing.identity()));
    }

    // Overriding the sum method
    @Override
    public WorkingPolynomial<T> sum(WorkingPolynomial<T> x, WorkingPolynomial<T> y) {
        // Ensure that x and y are not null
        Objects.requireNonNull(x, "x cannot be null");
        Objects.requireNonNull(y, "y cannot be null");
        return x.plus(y, baseRing);
    }

    // Overriding the product method
    @Override
    public WorkingPolynomial<T> product(WorkingPolynomial<T> x, WorkingPolynomial<T> y) {
        //null checks
        Objects.requireNonNull(x, "x cannot be null");
        Objects.requireNonNull(y, "y cannot be null");
        return x.times(y, baseRing);
    }
}
