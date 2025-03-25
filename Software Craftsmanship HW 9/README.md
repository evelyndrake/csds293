# HW 9
Evelyn Drake

Software Craftsmanship

11/1/24
## Development processes
### Documentation
- I didn't like the existing documentation, and the comments were not formatted in the way I do mine
- Therefore, my first task was to redo all the documentation for each method and test class
- This made everything more readable for me
- This also helped me understand the existing codebase, because I was forced to read every single line and re-explain them in my own words
- Some of the variables used snake_case instead of camelCase, which I corrected for consistency
- I also renamed `RingTesting` to `RingTest` to ensure it's detected from Ant, as my `build.xml` runs tests for every class name ending with "Test"
- I moved the tests into the `tests` package and the other classes into the `math` package and reconfigured my `build.xml` to be able to detect the tests from the `test` package, which made the project more organized and manageable
### Functionality changes
- `plus`
  - I removed `getAddend` and directly implemented this functionality to increase readability and reduce complexity
  - `getAddend` was returning `ring.zero` if the iterator did have another value and the next value if it did not, when it should be the other way around
  - When it tried to find the length of the longer list, it used `Math.min` instead of `Math.max`
  - Finally, I verified the functionality was correct through a combination of the given unit tests and my own
- `times`
  - I made sure to initialize `productList` of `productLength` with each element being `ring.zero()`
  - Instead of the iterators, I used a nested loop with indices `i` and `j`
  - Also, I noticed nothing was actually being added to `productList`
  - For each `i` and `j`, I multiply the coefficient of A at `i` and the coefficient of B at `j` and add the result to the position `i+j` in `productList`
  - Because of this approach, we no longer need the `computeStartIndex` method, reducing complexity
  - I also removed `computeProductLength`, because we start by checking if either polynomial has an empty list of coefficients and returning an empty list, so by the time we get to call `computeProductLength`, it must be `a.length + b.length - 1`
### Test changes
- I re-documented the given `RingTesting` class to be more readable just like I did with the source code
- I also renamed this class to `RingTest` for consistency with my other test classes
- With this one class, I only got 30.26% instruction coverage and 29.69% branch coverage, so I set out to make more tests to cover every other class
  - `WorkingPolynomialTest`
  - `PolynomialRingTest`
  - `RingsTest`
  - _(note that there is no IntegerRingTest class because I am testing it in `RingsTest`)_
- I added labels for the specific test types (good data, bad data, boundary conditions, stress test, etc.) like we did in the last assignment, and I made sure to implement all of these tests to create a thorough test suite
### Debugging
- Creating and documenting all of the unit tests made it easy to debug the `BuggyPolynomial`/`WorkingPolynomial` class
- I mentioned my specific changes to the source code earlier in this document
- I started by addressing the issues with the `plus` method, which I did by re-documenting it, noticing the errors in the unit tests (specifically an `OutOfMemoryError` that occurred from the use of iterators), and replacing this functionality as I mentioned above
- Similarly, I debugged the `times` method which incorrectly calculated the product length, attempted to retrieve incorrect addends, and had out of bound errors because the product list wasn't padded with zeroes to match the expected length
- After these changes, all of my unit tests passed with 100% instruction coverage and 100% branch coverage, and I ensured that they tested all of the functionality that was expected in the final `WorkingPolynomial` class