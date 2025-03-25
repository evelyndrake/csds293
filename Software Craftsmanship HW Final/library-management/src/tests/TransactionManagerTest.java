package tests;

import exceptions.MediaException;
import info.LibraryObserver;
import library.LoginType;
import library.TransactionManager;
import media.Author;
import media.Book;
import media.MediaInstance;
import org.junit.*;

import java.util.Date;

import static org.junit.Assert.*;
public class TransactionManagerTest {
    // Because we tested most of the TransactionManager methods in LibraryTest, we're just testing the login functionality here
    @Test
    public void testLogin() {
        TransactionManager.loginAs(LoginType.LIBRARIAN);
        assertEquals(LoginType.LIBRARIAN, TransactionManager.getLoginType());
        TransactionManager.loginAs(LoginType.PATRON);
        assertEquals(LoginType.PATRON, TransactionManager.getLoginType());
        TransactionManager.loginAs(null);
        assertEquals(LoginType.PATRON, TransactionManager.getLoginType());
    }

    @Test
    public void testConstructorForCoverage() {
        new TransactionManager();
    }

    @Test
    public void testTransactionsAsPatron() {
        TransactionManager.loginAs(LoginType.PATRON);
        assertNull(TransactionManager.addMedia(null));
        TransactionManager.removeMedia("1");
        assertNull(TransactionManager.addPatron(null));
        TransactionManager.removePatron("1");
        TransactionManager.setPatronSuspended("1", true);
        assertNull(TransactionManager.addAuthor(null));
        TransactionManager.removeAuthor("1");
        assertNull(TransactionManager.addMediaInstance(null));
        TransactionManager.checkOutMedia("1", "1");
        TransactionManager.placeHold("1", "1");
        TransactionManager.clearLibrary();
        TransactionManager.removeMediaInstance("1");
        TransactionManager.updateMediaInstance(null);
        TransactionManager.updateMedia(null);
        TransactionManager.updateAuthor(null);
        TransactionManager.updatePatron(null);
    }

    @Test
    public void stressTest() {
        TransactionManager.loginAs(LoginType.LIBRARIAN);
        // Add a new author
        Author author = new Author("1", "name", "biography", new Date(1, 1, 1));
        TransactionManager.addAuthor(author);
        // Add a new book
        Book book = null;
        try {
            book = new Book("1", "name", "1", "description", "genre");
        } catch (MediaException e) {
            assert false;
        }
        TransactionManager.addMedia(book);
        // Add 5 copies of the book
        for (int i = 0; i < 5; i++) {
            try {
                TransactionManager.addMediaInstance(new MediaInstance(Integer.toString(i), "1"));
            } catch (MediaException e) {
                assert false;
            }
        }
        // Check out all 5 copies
        for (int i = 0; i < 5; i++) {
            TransactionManager.checkOutMedia("1", "1");
        }
    }

}
