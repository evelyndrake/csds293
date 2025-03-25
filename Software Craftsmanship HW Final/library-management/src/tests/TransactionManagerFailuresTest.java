package tests;

import library.TransactionManager;
import org.junit.Test;

public class TransactionManagerFailuresTest {
    /* Because the TransactionManager methods get Futures from the Library methods, exceptions like InterruptedException
    could be thrown. However, I couldn't find a way to test this without using a mocking library like Mockito (which
    the remote server doesn't have set up). Therefore, I made a shouldFail variable in TransactionManager that can be
    set to false to cause exceptions to be thrown during the blocks where future.get() is called.
    */
    @Test
    public void testFailures() {
        TransactionManager.setShouldFail(true);
        TransactionManager.addMedia(null);
        TransactionManager.removeMedia("1");
        TransactionManager.addPatron(null);
        TransactionManager.removePatron("1");
        TransactionManager.setPatronSuspended("1", true);
        TransactionManager.addAuthor(null);
        TransactionManager.removeAuthor("1");
        TransactionManager.addMediaInstance(null);
        TransactionManager.checkOutMedia("1", "1");
        TransactionManager.placeHold("1", "1");
        TransactionManager.clearLibrary();
        TransactionManager.removeMediaInstance("1");
        TransactionManager.updateMediaInstance(null);
        TransactionManager.updateMedia(null);
        TransactionManager.updateAuthor(null);
        TransactionManager.updatePatron(null);
    }
}
