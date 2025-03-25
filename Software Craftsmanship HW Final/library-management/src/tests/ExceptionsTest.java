package tests;

import exceptions.HoldException;
import exceptions.LoanException;
import exceptions.MediaException;
import media.Media;
import org.junit.Test;

public class ExceptionsTest {
    @Test
    public void testExceptions() {
        HoldException holdException = new HoldException("message");
        LoanException loanException = new LoanException("message");
        MediaException mediaException = new MediaException("message");
    }
}
