package math;

/**
 * @author Vidyut Veedgav and Evelyn Drake
 * An interface to support ring operations on a variety of set types T
 */
public interface Ring<T> {

    T zero(); // Method to represent the zero property,  a * 0 = 0
    T identity(); // Method to represent the multiplicative identity property, a * 1 = a
    T sum(T x, T y); // Method to represent the sum operation
    T product(T x, T y); // Method to represent the multiplication operation
}



