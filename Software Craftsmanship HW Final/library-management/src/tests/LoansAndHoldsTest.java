package tests;

import exceptions.HoldException;
import exceptions.LoanException;
import exceptions.MediaException;
import library.Hold;
import library.Loan;
import library.Patron;
import library.TransactionManager;
import media.Author;
import media.Book;
import media.Media;
import media.MediaInstance;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class LoansAndHoldsTest {
    @Before
    public void setUp() {
        // Create an author and add them to the library
        Author author = new Author("1", "name", "biography", new Date(1, 1, 1));
        TransactionManager.addAuthor(author);
        // Create a media object and add it to the library
        Book book = null;
        try {
            book = new Book("1", "name", "1", "description", "genre");
        } catch (MediaException e) {
            assert false;
        }
        TransactionManager.addMedia(book);
        // Create an instance of the media object and add it to the library
        MediaInstance mediaInstance = null;
        try {
            mediaInstance = new MediaInstance("1", "1");
        } catch (MediaException e) {
            assert false;
        }
        TransactionManager.addMediaInstance(mediaInstance);
        // Create a patron and add them to the library
        Patron patron = new Patron("1", "firstname", "lastname", new Date(1, 1, 1));
        TransactionManager.addPatron(patron);
    }

    @Test
    public void testConstructors() {
        // Test the constructors for Loan and Hold
        // Good data
        try {
            Loan loan = new Loan("1", "1", "1");
            Loan loan2 = new Loan("1", "1", "1", new Date(1, 1, 1));
        } catch (LoanException e) {
            assert false;
        }
        try {
            Hold hold = new Hold("1", "1", "1");
            Hold hold2 = new Hold("1", "1", "1", new Date(1, 1, 1));
        } catch (HoldException e) {
            assert false;
        }
        // Bad data (nonexistent instance)
        try {
            Loan loan = new Loan("1", "2", "1");
            assert false;
        } catch (LoanException e) {
            assert true;
        }
        try {
            Loan loan = new Loan("1", "2", "1", new Date(1, 1, 1));
            assert false;
        } catch (LoanException e) {
            assert true;
        }
        try {
            Hold hold = new Hold("1", "2", "1");
            assert false;
        } catch (HoldException e) {
            assert true;
        }
        try {
            Hold hold = new Hold("1", "2", "1", new Date(1, 1, 1));
            assert false;
        } catch (HoldException e) {
            assert true;
        }
        // Bad data (nonexistent patron)
        try {
            Loan loan = new Loan("1", "1", "2");
            assert false;
        } catch (LoanException e) {
            assert true;
        }
        try {
            Hold hold = new Hold("1", "1", "2");
        } catch (HoldException e) {
            assert true;
        }
        // Bad data (all combinations of null input)
        try {
            Loan loan = new Loan(null, "1", "1");
            assert false;
        } catch (LoanException e) {
            assert true;
        }
        try {
            Loan loan = new Loan("1", null, "1");
            assert false;
        } catch (LoanException e) {
            assert true;
        }
        try {
            Loan loan = new Loan("1", "1", null);
            assert false;
        } catch (LoanException e) {
            assert true;
        }
        try {
            Loan loan = new Loan(null, null, null);
            assert false;
        } catch (LoanException e) {
            assert true;
        }
        try {
            Loan loan = new Loan(null, null, null, null);
            assert false;
        } catch (LoanException e) {
            assert true;
        }
        try {
            Hold hold = new Hold(null, "1", "1");
            assert false;
        } catch (HoldException e) {
            assert true;
        }
        try {
            Hold hold = new Hold("1", null, "1");
            assert false;
        } catch (HoldException e) {
            assert true;
        }
        try {
            Hold hold = new Hold("1", "1", null);
            assert false;
        } catch (HoldException e) {
            assert true;
        }
        try {
            Hold hold = new Hold(null, null, null);
            assert false;
        } catch (HoldException e) {
            assert true;
        }
        try {
            Hold hold = new Hold(null, null, null, null);
            assert false;
        } catch (HoldException e) {
            assert true;
        }
    }

    @Test
    public void testGetters() {
        // Test the getters for Loan and Hold
        Loan loan = null;
        try {
            loan = new Loan("1", "1", "1");
        } catch (LoanException e) {
            assert false;
        }
        assert loan.getID().equals("1");
        assert loan.getMediaInstanceID().equals("1");
        assert loan.getPatronID().equals("1");
        assert loan.getDateCreated() != null;
        assert loan.getDateOfReturn() != null;
        try {
            Media media = loan.getMedia();
            assert media.getID().equals("1");
        } catch (LoanException e) {
            assert false;
        }
        Hold hold = null;
        try {
            hold = new Hold("1", "1", "1");
        } catch (HoldException e) {
            assert false;
        }
        assert hold.getID().equals("1");
        assert hold.getMediaInstanceID().equals("1");
        assert hold.getPatronID().equals("1");
        assert hold.getDateCreated() != null;
        assert hold.getDateOfReturn() != null;
        try {
            Media media = hold.getMedia();
            assert media.getID().equals("1");
        } catch (HoldException e) {
            assert false;
        }
    }

    @Test
    public void testGetDaysOverdue() {
        // Test the getDaysOverdue method for Loan and Hold
        // Not overdue
        Loan loan = null;
        try {
            loan = new Loan("1", "1", "1");
        } catch (LoanException e) {
            assert false;
        }
        assert loan.getDaysOverdue() == 0;
        Hold hold = null;
        try {
            hold = new Hold("1", "1", "1");
        } catch (HoldException e) {
            assert false;
        }
        assert hold.getDaysOverdue() == 0;
        // Overdue
        try {
            loan = new Loan("1", "1", "1", new Date(1, 1, 1));
        } catch (LoanException e) {
            assert false;
        }
        try {
            hold = new Hold("1", "1", "1", new Date(1, 1, 1));
        } catch (HoldException e) {
            assert false;
        }
        assert loan.getDaysOverdue() > 0;
        assert hold.getDaysOverdue() > 0;
    }
}
