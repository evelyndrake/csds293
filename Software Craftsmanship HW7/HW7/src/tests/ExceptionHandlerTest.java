package tests;

import exceptions.ExceptionHandler;
import org.junit.Test;

public class ExceptionHandlerTest {
    // Although the ExceptionHandler class is a utility class, it has a public constructor that I can test to get coverage
    @Test
    public void testConstructor() { // T1
        ExceptionHandler handler = new ExceptionHandler();
    }

    @Test
    public void testHandleException() { // T2
        // TC2
        // Good data
        ExceptionHandler.handleException(new NullPointerException());
        // TC2
        // Bad data
        try {
            ExceptionHandler.handleException(null);
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
    }

    @Test
    public void testDisplayWarning() { // T3
        // TC3
        // Good data
        ExceptionHandler.displayWarning("This is a warning");
        // TC3
        // Bad data
        try {
            ExceptionHandler.displayWarning(null);
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
    }
}
