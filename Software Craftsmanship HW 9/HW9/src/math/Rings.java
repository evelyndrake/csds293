package math;

import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;

/**
 * @author Vidyut Veedgav and Evelyn Drake
 * A class defining behavior for ring operations
 */
public final class Rings<T> {
    
    /**
     * Method to reduce elements of a list by conducting an externally defined operation
     * @param <T> the type of the elements in the list
     * @param args the list of elements to reduce
     * @param zero the initial value of the reduction
     * @param accumulator the operation to conduct on the elements
     * @return a single value which is the result of the reduction
     */
    public static <T> T reduce(List<T> args, T zero, BinaryOperator<T> accumulator) {
        // Ensure that the arguments are not null
        Objects.requireNonNull(args);
        Objects.requireNonNull(zero);
        Objects.requireNonNull(accumulator);

        // Boolean to check if the list contains any elements
        boolean foundAny = false;
        // Initialize the result as zero
        T result = zero;

        for (T element : args) { // For each element in the list
            // Ensure that the element is not null
            Objects.requireNonNull(args);

            // Check if the element is present and convert it to the result
            if (!foundAny) { // If no elements are found
                foundAny = true;
                result = element;
            } else { // Otherwise, conduct the operation on the element
                // Conduct the operation on the accumulation of previous elements and the current element
                result = accumulator.apply(result, element);
            }
        }
        return result;
    }

    /**
     * Method to reduce a list by summing its elements
     * @param <T> the type of the elements in the list
     * @param args the list of elements to reduce
     * @param ring the ring to perform the operation in
     * @return a reduction based on addition
     */
    public static <T> T sum(List<T> args, Ring<T> ring) {
        // Ensure that the arguments are not null
        Objects.requireNonNull(args);
        Objects.requireNonNull(ring);
        // Return the reduction based on addition
        return reduce(args, ring.zero(), (x, y) -> ring.sum(x, y));
    }

    /**
     * Method to reduce a list by multiplying its elements
     * @param <T> the type of the elements in the list
     * @param args the list of elements to reduce
     * @param ring the ring to perform the operation in
     * @return a reduction based on multiplication
     */
    public static <T> T product(List<T> args, Ring<T> ring) {
        // Ensure that the arguments are not null
        Objects.requireNonNull(args);
        Objects.requireNonNull(ring);
        // Return the reduction based on multiplication
        return reduce(args, ring.identity(), (x, y) -> ring.product(x, y));
    }
}
