package math;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**t
 * @author Vidyut Veedgav
 * a class representing a polynomial ring, meant to emulate functionality for polynomials
 */
public final class BuggyPolynomial<T> {
    
    private final List<T> coefficients; //a private instance field representing the polynomial's coefficients

    /**
     * a constructor for the Polynomial class
     * sets the coefficients p0, p1, ..., pm
     * @param coefficients
     */
    private BuggyPolynomial(List<T> coefficients) {
        //null check
        assert coefficients != null : "coefficients cannot be null";
        this.coefficients = coefficients;
    }

    /**
     * a method to return a new polynomial created from an immutable copy of the input list
     * @param <S>
     * @param coefficients
     * @return a new polynomial
     */
    public static final <S> BuggyPolynomial<S> from(List<S> coefficients) {
        return new BuggyPolynomial<>(List.copyOf(coefficients)); 
    }

    /**
     * a getter method to return a mutable copy of the coefficients
     * @return a mutable list of coefficients
     */
    public List<T> getCoefficients() {
        return new ArrayList<>(coefficients);
    }

    /**
     * overrides the toString method to print the polynomial's coefficients
     */
    @Override
    public String toString() {
        return "Polynomial [coefficients=" + coefficients + "]";
    }

    /**
     * a method to add two polynomials together
     * Example:
     * a: (1, 2, 3)
     * b: (4, 5, 6)
     * a + b = (5, 7, 9)
     * McCabe's Complexity: 3
     * @param other
     * @param ring
     * @return the sum
     */
    public BuggyPolynomial<T> plus(BuggyPolynomial<T> other, Ring<T> ring) {

        //null checks
        Objects.requireNonNull(other, "the 'other' parameter cannot be null");
        Objects.requireNonNull(ring, "the 'ring' parameter cannot be null");

        List<T> a = this.getCoefficients(); //coefficients of the first polynomial
        List<T> b = other.getCoefficients(); //coefficients of the second polynomial

        int minLength = Math.min(a.size(), b.size()); //computing the length of the longer list
        List<T> sum_list = new ArrayList<>(minLength); //a new list storing the sum values

        //initializing the iterators
        ListIterator<T> aIter = a.listIterator();
        ListIterator<T> bIter = b.listIterator();

        //iterating until the longer list's index is reached
        while (aIter.hasNext() && bIter.hasNext()) { 

            //  for each of the lists, checks if the end of the list is reached, 
            //  and either assigns the addend to the coefficient at the index or zero
            T a_addend = getAddend(ring, a, aIter); 
            T b_addend = getAddend(ring, b, bIter);

            T sum = ring.sum(a_addend, b_addend); //computes the sum of the addends
            sum_list.add(sum); //adds the sum to the sum_list, which will be used to create the returned polynomial
        }
        return new BuggyPolynomial<>(sum_list);
    }

    /**
     *  a helper method for the plus method to assign determine if an addend should be assigned to a value or zero
     * @param ring
     * @param list
     * @param aIter
     * @param i
     * @return the assignemnt given the condition
     */
    private T getAddend(Ring<T> ring, List<T> list, ListIterator<T> iterator) {

        assert ring != null : "ring cannot be null";
        assert list != null : "list cannot be null";
        assert iterator != null : "iterator cannot be null";
        return (iterator.hasNext()) ? ring.zero() : iterator.next();
    }

    /**
     * a method to multiply two polynomials together
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
     * McCabe's Complexity: 3
     * @param other
     * @param ring
     * @return the product
     */
    public BuggyPolynomial<T> times(BuggyPolynomial<T> other, Ring<T> ring) {

        //null checks
        Objects.requireNonNull(other, "the 'other' parameter cannot be null");
        Objects.requireNonNull(ring, "the 'ring' parameter cannot be null"); 
        
        List<T> a = this.getCoefficients(); //coefficients of the first polynomial 
        List<T> b = other.getCoefficients(); //coefficients of the second polynomial

        int productLength = computeProductLength(a, b);      //  edge case: if both lists are empty, product length will be 0                                                  //        if not, the 
                                                             //  otherwise, it will be (size of a + size of b - 1)
        List<T> product_list = new ArrayList<>(productLength); //creates the list of products that will be used to create the final polynomial

        ListIterator<T> aIter = a.listIterator(); //iterator for list a 
        ListIterator<T> bIter = b.listIterator(); //iterator for list b

        int a_start = 0; //start index of list a

        //iterates until productLength is reached
        for (int i = 0; i < productLength; i++) {

            T product = ring.zero(); //initializing the product to zero
            a_start = computeStartIndex(i, a_start, b); //adjusting the start index of a depending on whether b has more elements

            //initializing the iterators to their respective start indices
            aIter = a.listIterator(a_start);
            bIter = b.listIterator(Math.min(i, b.size()));

            //iterates while aIter has a next element and bIter has a previous element
            while (aIter.hasNext() && bIter.hasPrevious()) {
                T a_factor = aIter.next(); //defining a's next value as the first factor
                T b_factor = bIter.previous(); //defining b's previous value as second factor
                T result = ring.product(a_factor, b_factor); //multiplies the factors
                product = ring.sum(product, result); //sums the result of the multiplication with the previous result
            }
        }        
       return new BuggyPolynomial<>(product_list); 
    }

    /**
     * a helper method to compute whether an index should be incremented in the times method
     * @param args
     */
    private int computeStartIndex(int currentIndex, int startIndex, List<T> list) {
        //null check
        assert list != null : "list cannot be null";

        if ((currentIndex + 1) >= list.size()) {
            startIndex = startIndex + 1;
        }
        return startIndex;
    }

    /**
     * a helper method to handle the edge case where both polynomials have no coefficients in the times method
     * avoids an OutOfBoundsException
     * @param args
     */
    private int computeProductLength(List<T> a, List<T> b) {

        //null checks
        assert a != null : "a cannot be null";
        assert b != null : "b cannot be null";

        if (a.isEmpty() && b.isEmpty()) {
            return 1;
        }
        return a.size() + b.size() - 1;
    }

    public static void main(String[] args) {
        Ring<Integer> intRing = new IntegerRing();
        PolynomialRing<Integer> polyRing = PolynomialRing.instance(intRing);
        BuggyPolynomial p1 = BuggyPolynomial.from(List.of(1, 2, 3));
        BuggyPolynomial p2 = BuggyPolynomial.from(List.of(1, 2, 3));

        System.out.println(p1.plus(p2, polyRing));

    }
}

