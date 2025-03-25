import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/*
    Evelyn Drake (JCD171) - Software Craftsmanship HW 1
    Because my Case ID ends with 1, I implemented the solution recursively.
    For the extra user input part, I did use loops though; I hope that's okay.
 */

public class Main {
    public static <T> List<T> longestAlternatingSubsequence(List<T> a, List<T> b, Comparator<T> cmp) {
        // Start the recursion with the option of starting with list A or list B
        List<T> option1 = findLongestAltSubseqRecursive(a, b, 0, 0, null, true, cmp);
        List<T> option2 = findLongestAltSubseqRecursive(a, b, 0, 0, null, false, cmp);
        // Return the longer of the two options
        if (option1.size() > option2.size()) {
            return option1;
        } else {
            return option2;
        }
    }

    private static <T> List<T> findLongestAltSubseqRecursive(List<T> a, List<T> b, int i, int j, T previousElement,
                                                             boolean fromA, Comparator<T> cmp) {
        // Base case: Stop when both indices are out of bounds
        if (i >= a.size() && j >= b.size()) {
            return new ArrayList<>();
        }
        // Initialize the result lists for options
        List<T> option1 = new ArrayList<>();
        List<T> option2 = new ArrayList<>();

        // Option 1: Continue from list A
        if (fromA && i < a.size() && (previousElement == null || cmp.compare(a.get(i), previousElement) > 0)) {
            // If we are looking at list A, and we can take an element that fits the criteria, we continue
            List<T> nextOption = findLongestAltSubseqRecursive(a, b, i + 1, j, a.get(i), false, cmp);
            // Add the current element to the list
            option1.add(a.get(i));
            option1.addAll(nextOption);
        }

        // Option 2: Continue from list B
        if (!fromA && j < b.size() && (previousElement == null || cmp.compare(b.get(j), previousElement) > 0)) {
            // If we are looking at list B, and we can take an element that fits the criteria, we continue
            List<T> nextOption = findLongestAltSubseqRecursive(a, b, i, j + 1, b.get(j), true, cmp);
            // Add the current element to the list
            option2.add(b.get(j));
            option2.addAll(nextOption);
        }

        // We also need to consider the cases that arise from skipping the current element:

        // Option 3: Skip current element from list A
        if (i < a.size()) { // If the current element is not the last element in the list
            List<T> skipOption = findLongestAltSubseqRecursive(a, b, i + 1, j, previousElement, fromA, cmp);
            // If the skip option is longer than the current option from A, update the current option
            if (skipOption.size() > option1.size()) {
                option1 = skipOption;
            }
        }

        // Option 4: Skip current element from list B
        if (j < b.size()) { // If the current element is not the last element in the list
            List<T> skipOption = findLongestAltSubseqRecursive(a, b, i, j + 1, previousElement, fromA, cmp);
            // If the skip option is longer than the current option from B, update the current option
            if (skipOption.size() > option2.size()) {
                option2 = skipOption;
            }
        }


        // Return the longer of the two options
        if (option1.size() > option2.size()) {
            return option1;
        } else {
            return option2;
        }
    }

    public static void main(String[] args) {
        /* Hardcoded test case:
        List<Integer> A = List.of(1, 3, 5);
        List<Integer> B = List.of(2, 4, 6);
        List<Integer> result = longestAlternatingSubsequence(A, B, Comparator.naturalOrder());
        System.out.println(result);
         */
        // Extra user input component:
        // Get the two lists from standard input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the numbers for list A separated by commas:");
        List<Integer> A = new ArrayList<>();
        String[] numbers = scanner.nextLine().split(",");
        for (String number : numbers) { // Add the numbers to list A
            A.add(Integer.parseInt(number));
        }
        System.out.println("Enter the numbers for list B separated by commas:");
        List<Integer> B = new ArrayList<>();
        numbers = scanner.nextLine().split(",");
        for (String number : numbers) { // Add the numbers to list B
            B.add(Integer.parseInt(number));
        }
        // Find the longest alternating subsequence
        List<Integer> result = longestAlternatingSubsequence(A, B, Comparator.naturalOrder());
        System.out.println("The longest alternating subsequence is " + result + " with length " + result.size() + ".");
    }
}