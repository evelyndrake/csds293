package math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**t
 * @author Vidyut Veedgav and Evelyn Drake
 * A class representing a polynomial ring, meant to emulate functionality for polynomials
 */
public final class WorkingPolynomial<T> {
    
    private final List<T> coefficients; // A private instance field representing the polynomial's coefficients

    /**
     * Constructor for the Polynomial class
     * @param coefficients the coefficients p0, p1, ..., pm
     */
    private WorkingPolynomial(List<T> coefficients) {
        // Ensure that coefficients are not null
        Objects.requireNonNull(coefficients, "coefficients cannot be null");
        this.coefficients = coefficients;
    }

    /**
     * Method to return a new polynomial created from an immutable copy of the input list
     * @param <S> the type of the coefficients
     * @param coefficients the coefficients of the polynomial
     * @return a new polynomial
     */
    public static <S> WorkingPolynomial<S> from(List<S> coefficients) {
        // Ensure that coefficients are not null
        Objects.requireNonNull(coefficients, "coefficients cannot be null");
        // Return a new polynomial with an immutable copy of the coefficients
        return new WorkingPolynomial<>(List.copyOf(coefficients));
    }

    /**
     * Getter method to return a mutable copy of the coefficients
     * @return a mutable list of coefficients
     */
    public List<T> getCoefficients() {
        return new ArrayList<>(coefficients);
    }

    // Overriding the toString method
    @Override
    public String toString() {
        return "Polynomial [coefficients=" + coefficients + "]";
    }

    /**
     * Method to add two polynomials together
     * Example:
     * a: (1, 2, 3)
     * b: (4, 5, 6)
     * a + b = (5, 7, 9)
     * @param other the other polynomial
     * @param ring the ring to perform the operation in
     * @return the sum
     */
    public WorkingPolynomial<T> plus(WorkingPolynomial<T> other, Ring<T> ring) {
        // Ensure that other and ring are not null
        Objects.requireNonNull(other, "the 'other' parameter cannot be null");
        Objects.requireNonNull(ring, "the 'ring' parameter cannot be null");
        // Find the coefficients of both polynomials
        List<T> a = this.getCoefficients();
        List<T> b = other.getCoefficients();
        // Find the maximum length of the two polynomials (broken version found the min for some reason?)
        int maxLength = Math.max(a.size(), b.size());
        // Create a list to store the sum of the two polynomials
        List<T> sumList = new ArrayList<>(maxLength);

        for (int i = 0; i < maxLength; i++) { // For each coefficient in the sum
            // If the index is within the bounds of the list, get the coefficient, otherwise, assign zero
            T coefficientA = (i < a.size()) ? a.get(i) : ring.zero();
            T coefficientB = (i < b.size()) ? b.get(i) : ring.zero();
            // Add the sum of the coefficients to the sum list
            sumList.add(ring.sum(coefficientA, coefficientB));
        }
        return new WorkingPolynomial<>(sumList);
    }

    /**
     * Method to multiply two polynomials together
     * Example:
     * a: (1, 2, 3)
     * b: (4, 5, 6)
     * a * b = (1 * 4),
     *         ((1 * 5) + (2 * 4)),
     *         ((1 * 6) + (2 * 5) + (3 * 4)),
     *         ((1 * 0) + (2 * 6) + (3 * 5) + (0 * 4)),
     *         ((1 * 0) + (2 * 0), (3 * 6) + (0 * 5) + (0 * 4))
     * 
     *       = (4, 13, 28, 27, 18)
     * @param other the other polynomial
     * @param ring the ring to perform the operation in
     * @return the product
     */
    public WorkingPolynomial<T> times(WorkingPolynomial<T> other, Ring<T> ring) {
        // Ensure that other and ring are not null
        Objects.requireNonNull(other, "the 'other' parameter cannot be null");
        Objects.requireNonNull(ring, "the 'ring' parameter cannot be null"); 

        // Coefficients of both polynomials
        List<T> coefficientsA = this.getCoefficients();
        List<T> coefficientsB = other.getCoefficients();

        // Next, we need to find the product length
        // If both polynomials are empty, the product will be an empty list which we return directly
        // Changed && to & to avoid short-circuiting, allowing all branches to be covered
        if (coefficientsA.isEmpty() & coefficientsB.isEmpty()) {
            return new WorkingPolynomial<>(List.of());
        }
        // Otherwise the product will be the sum of the lengths of the two polynomials minus 1
        int productLength = coefficientsA.size() + coefficientsB.size() - 1;

        // Create the lists of products used in the final polynomial
        List<T> productList = new ArrayList<>(productLength);

        // Next, we need to initialize the product list for each term up to the product length
        for (int i = 0; i < productLength; i++) { // For each term in the product
            productList.add(ring.zero()); // Add a zero to the product list
        }

        // Multiply each term in a by each term in b and add to the appropriate position in productList
        for (int i = 0; i < coefficientsA.size(); i++) { // For each coefficient in a
            for (int j = 0; j < coefficientsB.size(); j++) { // For each coefficient in b
                T result = ring.product(coefficientsA.get(i), coefficientsB.get(j)); // Get the product of the two coefficients
                T currentSum = productList.get(i + j); // Get the current sum in the product list at the position
                productList.set(i + j, ring.sum(currentSum, result)); // Add the result to the current sum
            }
        }

        return new WorkingPolynomial<>(productList);
    }
}

