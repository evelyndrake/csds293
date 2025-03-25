package tests;

import exceptions.LoanException;
import exceptions.MediaException;
import library.Fee;
import library.Loan;
import library.Patron;
import library.TransactionManager;
import media.Author;
import media.Book;
import media.MediaInstance;
import org.junit.Test;

import java.util.Date;

public class PatronTest {
    @Test
    public void testConstructor() {
        // Good data
        // Make a Patron with valid parameters
        try {
            Patron patron = new Patron("1", "firstname", "lastname", new Date(2000,1,1));
        } catch (IllegalArgumentException e) {
            assert false;
        }
        // Bad data (all combinations of null parameters)
        try {
            Patron patron = new Patron(null, "firstname", "lastname", new Date(2000,1,1));
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            Patron patron = new Patron("1", null, "lastname", new Date(2000,1,1));
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            Patron patron = new Patron("1", "firstname", null, new Date(2000,1,1));
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            Patron patron = new Patron("1", "firstname", "lastname", null);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            Patron patron = new Patron(null, null, null, null);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    public void testGetters() {
        // Good data
        // Make a Patron with valid parameters
        try {
            Patron patron = new Patron("1", "firstname", "lastname", new Date(2000,1,1));
            assert patron.getID().equals("1");
            assert patron.getFirstName().equals("firstname");
            assert patron.getLastName().equals("lastname");
            assert patron.getDateOfBirth().equals(new Date(2000,1,1));
            assert patron.getDateOfRegistration().equals(new Date());
        } catch (IllegalArgumentException e) {
            assert false;
        }
    }

    @Test
    public void testFees() {
        Patron patron = null;
        // Make a Patron and add them to the library
        try {
            patron = new Patron("1", "firstname", "lastname", new Date(2000,1,1));
            TransactionManager.addPatron(patron);
        } catch (IllegalArgumentException e) {
            assert false;
        }
        // Make a Book and add it to the library
        try {
            Author author = new Author("1", "name", "biography", new Date(1, 1, 1));
            TransactionManager.addAuthor(author);
            Book book = new Book("1", "title", "1", "description", "genre");
            TransactionManager.addMedia(book);
        } catch (MediaException e) {
            assert false;
        }
        // Make an instance of this book and add it to the library
        try {
            MediaInstance mediaInstance = new MediaInstance("1", "1");
            TransactionManager.addMediaInstance(mediaInstance);
        } catch (MediaException e) {
            assert false;
        }
        Loan loan = null;
        // Make a Date 10 days ago
        Date date = new Date();
        date.setTime(date.getTime() - 864000000);
        // Make an overdue loan
        try {
            loan = new Loan("1", "1", "1", date);
        } catch (LoanException e) {
            assert false;
        }
        Fee fee = new Fee(loan);
        patron.addFee(loan);
        // Given a rate of $0.25 a day, the fee should be $2.50
        assert fee.calculateFee() == 2.50;
        // Make another instance of the book and add it to the library
        try {
            MediaInstance mediaInstance = new MediaInstance("2", "1");
            TransactionManager.addMediaInstance(mediaInstance);
        } catch (MediaException e) {
            assert false;
        }
        // Make a loan that is not overdue
        try {
            loan = new Loan("2", "2", "1");
        } catch (LoanException e) {
            assert false;
        }
        fee = new Fee(loan);
        patron.addFee(loan);
        // The fee should be $0
        assert fee.calculateFee() == 0.0;
        // Make a fee with a null expiring object
        try {
            Fee fee2 = new Fee(null);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        // Find the total amount the patron owes (should be $2.50)
        assert patron.getTotalAmountOwed() == 2.50;
        // Attempt to pay off the fee
        patron.payOff(2.50);
        // The patron should no longer owe anything
        assert patron.getTotalAmountOwed() == 0.0;
        assert patron.getAmountPaidOff() == 2.50;
    }
}
