package tests;
import exceptions.MediaException;
import info.LibraryObserver;
import library.*;

import media.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.Objects;



public class LibraryTest {

    // Member variables
    String author1;
    String author2;
    String author3;
    String author4;
    String author5;
    String media1;
    String media2;
    String media3;
    String media4;
    String media5;
    String mediaInstance1;
    String mediaInstance2;
    String mediaInstance3;
    String mediaInstance4;
    String mediaInstance5;
    String patron1;
    String patron2;
    String patron3;

    @Before
    public void setUp() throws MediaException {
        TransactionManager.clearLibrary();
        // Populate with sample data
        // Authors
        author1 = TransactionManager.addAuthor(new Author("1", "J.R.R. Tolkien", "English writer", new Date(1892, 1, 3)));
        author2 = TransactionManager.addAuthor(new Author("2", "George Orwell", "English novelist", new Date(1903, 6, 25)));
        author3 = TransactionManager.addAuthor(new Author("3", "J.K. Rowling", "British author", new Date(1965, 7, 31)));
        author4 = TransactionManager.addAuthor(new Author("4", "F. Scott Fitzgerald", "American novelist", new Date(1896, 9, 24)));
        author5 = TransactionManager.addAuthor(new Author("5", "Harper Lee", "American novelist", new Date(1926, 4, 28)));
        // Media
        media1 = TransactionManager.addMedia(new Book("B001", "The Hobbit", "1", "A fantasy novel", "Fantasy"));
        media2 = TransactionManager.addMedia(new Book("B002", "1984", "2", "A dystopian novel", "Science Fiction"));
        media3 = TransactionManager.addMedia(new Book("B003", "Harry Potter and the Sorcerer's Stone", "3", "A fantasy novel", "Fantasy"));
        media4 = TransactionManager.addMedia(new Book("B004", "The Great Gatsby", "4", "A novel", "Fiction"));
        media5 = TransactionManager.addMedia(new Book("B005", "To Kill a Mockingbird", "5", "A novel", "Fiction"));
        // Media instances
        mediaInstance1 = TransactionManager.addMediaInstance(new MediaInstance("BI001", "B001"));
        mediaInstance2 = TransactionManager.addMediaInstance(new MediaInstance("BI002", "B002"));
        mediaInstance3 = TransactionManager.addMediaInstance(new MediaInstance("BI003", "B003"));
        mediaInstance4 = TransactionManager.addMediaInstance(new MediaInstance("BI004", "B004"));
        mediaInstance5 = TransactionManager.addMediaInstance(new MediaInstance("BI005", "B005"));
        // Patrons
        patron1 = TransactionManager.addPatron(new Patron("P001", "Alice", "Smith", new Date(1990, 1, 1)));
        patron2 = TransactionManager.addPatron(new Patron("P002", "Bob", "Johnson", new Date(1985, 5, 15)));
        patron3 = TransactionManager.addPatron(new Patron("P003", "Charlie", "Brown", new Date(1970, 10, 30)));
    }

    @Test
    public void testConstructorForCoverage() {
        Library library = new Library();
    }

    @Test
    public void testAddMedia() {
        // Good data
        // Create a new media object
        Media media = null;
        try {
            media = new Book("B006", "Animal Farm", "1", "A novella", "Political satire");
        } catch (MediaException e) {
            assert false;
        }
        // Add the media object to the library inventory
        TransactionManager.addMedia(media);
        // Check if the media object was added successfully
        assertTrue(Library.isMediaInInventory("B006"));
        // Bad data
        // Try to add the same media object again
        TransactionManager.addMedia(media);
    }
    @Test
    public void testRemoveMedia() {
        // Good data
        // Remove a media object from the library inventory
        TransactionManager.removeMedia("B001");
        // Check if the media object was removed successfully
        assertFalse(Library.isMediaInInventory("B001"));
        // Bad data
        // Try to remove a non-existent media object
        TransactionManager.removeMedia("B006");
        // Check if the media object was not removed
        assertFalse(Library.isMediaInInventory("B006"));
    }

    @Test
    public void testAddMediaInstance() throws MediaException {
        // Good data
        // Create a new media instance object
        MediaInstance mediaInstance = new MediaInstance("BI006", "B001");
        // Add the media instance object to the library inventory
        TransactionManager.addMediaInstance(mediaInstance);
        // Check if the media instance object was added successfully
        assertNotNull(Library.findMediaInstanceByID("BI006"));
        // Bad data
        // Try to add the same media instance object again
        TransactionManager.addMediaInstance(mediaInstance);
    }

    @Test
    public void testRemoveMediaInstance() {
        // Good data
        // Remove a media instance from the library inventory
        TransactionManager.removeMediaInstance("BI001");
        // Check if the media instance was removed successfully
        assertNull(Library.findMediaInstanceByID("BI001"));
        // Bad data
        // Try to remove a non-existent media instance
        TransactionManager.removeMediaInstance("BI006");
    }
    @Test
    public void testFindMediaByTitle() {
        // Good data
        // Search for a media object by title
        Media media = Library.findMediaByTitle("The Hobbit");
        // Check if the media object was found successfully
        assertNotNull(media);
        assertEquals("B001", media.getID());
        // Bad data
        // Search for a non-existent media object by title
        media = Library.findMediaByTitle("The Catcher in the Rye");
        // Check if the media object was not found
        assertNull(media);
    }

    @Test
    public void testFindAuthorByID() {
        // Good data
        // Search for an author object by ID
        Author author = Library.findAuthorByID("1");
        // Check if the author object was found successfully
        assertNotNull(author);
        assertEquals("1", author.getID());
        // Bad data
        // Search for a non-existent author object by ID
        author = Library.findAuthorByID("6");
        // Check if the author object was not found
        assertNull(author);
    }

    @Test
    public void testFindMediaByInformation() {
        // Good data
        // Search for a media object by information
        Media media = Library.findMediaByInformation("description", "A fantasy novel");
        // Check if the media object was found successfully
        assertNotNull(media);
        assertEquals("B001", media.getID());
        // Bad data (search for a non-existent media object by information)
        media = Library.findMediaByInformation("description", "A mystery novel");
        // Check if the media object was not found
        assertNull(media);
        // Bad data (search for a media object with a non-existent information type)
        media = Library.findMediaByInformation("example", "test");
        // Check if the media object was not found
        assertNull(media);
        // Bad data (search for a media object with a non-existent information value)
        media = Library.findMediaByInformation("description", "test");
        // Check if the media object was not found
        assertNull(media);
    }

    @Test
    public void testFindMediaInstanceByID() {
        // Good data
        // Search for a media instance by ID
        MediaInstance mediaInstance = Library.findMediaInstanceByID("BI001");
        // Check if the media instance was found successfully
        assertNotNull(mediaInstance);
        assertEquals("BI001", mediaInstance.getID());
        // Bad data
        // Search for a non-existent media instance by ID
        mediaInstance = Library.findMediaInstanceByID("BI006");
        // Check if the media instance was not found
        assertNull(mediaInstance);
    }

    @Test
    public void testFindMediaInstanceByMediaID() {
        // Good data
        // Search for a media instance by media ID
        MediaInstance mediaInstance = Library.findMediaInstanceByMediaID("B001");
        // Check if the media instance was found successfully
        assertNotNull(mediaInstance);
        assertEquals("BI001", mediaInstance.getID());
        // Bad data
        // Search for a non-existent media instance by media ID
        mediaInstance = Library.findMediaInstanceByMediaID("B006");
        // Check if the media instance was not found
        assertNull(mediaInstance);
    }

    @Test
    public void testIsMediaAvailable() {
        // Good data
        // Check if a media object is available (an instance of it exists in the library)
        assertTrue(Library.isMediaAvailable("B001"));
        // Remove the media instance
        TransactionManager.removeMediaInstance("BI001");
        // Check if the media object is no longer available
        assertFalse(Library.isMediaAvailable("B001"));
        // Bad data
        // Check if a non-existent media object is available
        assertFalse(Library.isMediaAvailable("B006"));
    }

    @Test
    public void testCheckOutMedia() {
        // Good data
        // Check out a media instance to a patron
        TransactionManager.checkOutMedia("B001", "P001");
        // Check if the media instance is no longer available
        assertFalse(Library.isMediaAvailable("B001"));
        // Check if the loan exists
        Loan loan = Library.findLoanByInstanceID("BI001");
        assertNotNull(loan);
        // Ensure the loan's information is correct
        assertEquals("BI001", loan.getMediaInstanceID());
        assertEquals("P001", loan.getPatronID());
        // Ensure this patron has one loan
        assertEquals(1, Library.findPatronLoans("P001").size());
        // Bad data (try to check out media with no copies)
        TransactionManager.checkOutMedia("B001", "P001");
        // Ensure this patron still has one loan
        assertEquals(1, Library.findPatronLoans("P001").size());
        // Bad data (try to check out media with a non-existent media instance)
        TransactionManager.checkOutMedia("B006", "P001");
        // Ensure this patron still has one loan
        assertEquals(1, Library.findPatronLoans("P001").size());
        // Bad data (try to check out media with a non-existent patron)
        TransactionManager.checkOutMedia("B002", null);
        // Ensure this patron still has one loan
        assertEquals(1, Library.findPatronLoans("P001").size());
        // Bad data (try to check out media with a suspended patron)
        TransactionManager.setPatronSuspended("P001", true);
        TransactionManager.checkOutMedia("B002", "P001");
        // Ensure this patron still has one loan
        assertEquals(1, Library.findPatronLoans("P001").size());
    }

    @Test
    public void testPlaceHold() {
        // Good data
        // Place a hold on a media instance
        TransactionManager.placeHold("B001", "P001");
        // Check if the hold exists
        Hold hold = Library.findHoldByInstanceID("BI001");
        assertNotNull(hold);
        // Ensure the hold's information is correct
        assertEquals("BI001", hold.getMediaInstanceID());
        assertEquals("P001", hold.getPatronID());
        // Ensure this patron has one hold
        assertEquals(1, Library.findPatronHolds("P001").size());
        // Bad data (try to place a hold on media with no copies)
        TransactionManager.placeHold("B001", "P001");
        // Ensure this patron still has one hold
        assertEquals(1, Library.findPatronHolds("P001").size());
        // Bad data (try to place a hold on a non-existent media instance)
        TransactionManager.placeHold("B006", "P001");
        // Ensure this patron still has one hold
        assertEquals(1, Library.findPatronHolds("P001").size());
        // Bad data (try to place a hold for a non-existent patron)
        TransactionManager.placeHold("B002", null);
        // Ensure this patron still has one hold
        assertEquals(1, Library.findPatronHolds("P001").size());
        // Bad data (try to place a hold for a suspended patron)
        TransactionManager.setPatronSuspended("P001", true);
        TransactionManager.placeHold("B002", "P001");
        // Ensure this patron still has one hold
        assertEquals(1, Library.findPatronHolds("P001").size());
    }

    @Test
    public void testAddPatron() {
        // Good data
        // Create a new patron object
        Patron patron = new Patron ("P004", "David", "Brown", new Date(1980, 2, 15));
        // Add the patron object to the library inventory
        TransactionManager.addPatron(patron);
        // Check if the patron object was added successfully
        assertNotNull(Library.findPatronByID("P004"));
        // Try to add the same patron object again
        TransactionManager.addPatron(patron);
    }

    @Test
    public void testRemovePatron() {
        // Good data
        // Remove a patron object from the library inventory
        TransactionManager.removePatron("P001");
        // Check if the patron object was removed successfully
        assertNull(Library.findPatronByID("P001"));
        // Bad data
        // Try to remove a non-existent patron object
        TransactionManager.removePatron("P004");
        // Check if the patron object was not removed
        assertNull(Library.findPatronByID("P004"));
    }

    @Test
    public void testSuspendPatron() {
        // Good data
        // Suspend a patron
        TransactionManager.setPatronSuspended("P001", true);
        // Ensure the patron exists
        assertNotNull(Library.findPatronByID("P001"));
        // Check if the patron is suspended
        assertTrue(Library.findPatronByID("P001").isSuspended());
        // Bad data
        // Try to suspend a non-existent patron
        TransactionManager.setPatronSuspended("P004", true);
    }

    @Test
    public void testAddAuthor() {
        // Good data
        // Create a new author object
        Author author = new Author("6", "Ernest Hemingway", "American novelist", new Date(1899, 7, 21));
        // Add the author object to the library inventory
        TransactionManager.addAuthor(author);
        // Check if the author object was added successfully
        assertNotNull(Library.findAuthorByID("6"));
        // Bad data
        // Try to add the same author object again
        TransactionManager.addAuthor(author);
    }

    @Test
    public void testRemoveAuthor() {
        // Good data
        // Remove an author object from the library inventory
        TransactionManager.removeAuthor("1");
        // Check if the author object was removed successfully
        assertNull(Library.findAuthorByID("1"));
        // Bad data
        // Try to remove a non-existent author object
        TransactionManager.removeAuthor("6");
        // Check if the author object was not removed
        assertNull(Library.findAuthorByID("6"));
    }

    @Test
    public void testGetAvailableMediaInstance() {
        // Good data
        // Get the available media instance
        MediaInstance mediaInstance = Library.getAvailableMediaInstance("B001");
        // Check if the media instance was found successfully
        assertNotNull(mediaInstance);
        assertEquals("BI001", mediaInstance.getID());
        // Bad data (media does not exist in the library)
        // Get the available media instance for a non-existent media object
        mediaInstance = Library.getAvailableMediaInstance("B006");
        // Check if the media instance was not found
        assertNull(mediaInstance);
        // Bad data (media exists in the library but there is not corresponding instance)
        // Remove the media instance
        TransactionManager.removeMediaInstance("BI001");
        // Get the available media instance
        mediaInstance = Library.getAvailableMediaInstance("B001");
        // Check if the media instance was not found
        assertNull(mediaInstance);
    }

    @Test
    public void testFindPatronLoansAndHolds() {
        // Good data
        // Check out a media instance to a patron
        TransactionManager.checkOutMedia("B001", "P001");
        // Place a hold on a media instance
        TransactionManager.placeHold("B002", "P001");
        // Find the patron's loans
        assertEquals(1, Library.findPatronLoans("P001").size());
        // Find the patron's holds
        assertEquals(1, Library.findPatronHolds("P001").size());
        // Bad data
        // Find the loans for a non-existent patron
        assertEquals(0, Library.findPatronLoans("P004").size());
        // Find the holds for a non-existent patron
        assertEquals(0, Library.findPatronHolds("P004").size());
    }

    @Test
    public void testUpdateMediaInstance() {
        // Good data
        // Update the media instance
        try {
            TransactionManager.updateMediaInstance(new MediaInstance("BI001", "B001", MediaInstanceCondition.POOR));
        } catch (MediaException e) {
            assert false;
        }
        // Check if the media instance was updated successfully
        assertEquals(MediaInstanceCondition.POOR, Objects.requireNonNull(Library.findMediaInstanceByID("BI001")).getCondition());
        // Bad data
        // Try to update a non-existent media instance
        try {
            TransactionManager.updateMediaInstance(new MediaInstance("BI006", "B006", MediaInstanceCondition.GOOD));
        } catch (MediaException e) {
            assert false;
        }
    }

    @Test
    public void testUpdateMedia() {
        // Good data
        // Update the media object
        try {
            TransactionManager.updateMedia(new Book("B001", "The Hobbit", "1", "A fantasy novel", "Fantasy"));
        } catch (MediaException e) {
            assert false;
        }
        // Check if the media object was updated successfully
        assertEquals("Fantasy", Objects.requireNonNull(Library.findMediaByID("B001")).getInformation().get("genre"));
        // Bad data
        // Try to update a non-existent media object
        try {
            TransactionManager.updateMedia(new Book("B006", "The Catcher in the Rye", "5", "A novel", "Fiction"));
        } catch (MediaException e) {
            assert false;
        }
    }

    @Test
    public void testUpdatePatron() {
        // Good data
        // Update the patron object
        TransactionManager.updatePatron(new Patron("P001", "Alice", "Smith", new Date(1990, 1, 1)));
        // Check if the patron object was updated successfully
        assertEquals("Alice", Objects.requireNonNull(Library.findPatronByID("P001")).getFirstName());
        // Bad data
        // Try to update a non-existent patron object
        TransactionManager.updatePatron(new Patron("P004", "David", "Brown", new Date(1980, 2, 15)));
    }

    @Test
    public void testUpdateAuthor() {
        // Good data
        // Update the author object
        TransactionManager.updateAuthor(new Author("1", "J.R.R. Tolkien", "English writer", new Date(1892, 1, 3)));
        // Check if the author object was updated successfully
        assertEquals("English writer", Objects.requireNonNull(Library.findAuthorByID("1")).getBiography());
        // Bad data
        // Try to update a non-existent author object
        TransactionManager.updateAuthor(new Author("6", "Ernest Hemingway", "American novelist", new Date(1899, 7, 21)));
    }

    @Test
    public void testCheckOverdueLoans() {
        // Add a loan
        TransactionManager.checkOutMedia("B002", "P001");
        // Add an expired loan
        Library.addLoanWithReturnDate("2", "BI001", "P001", new Date(1, 1, 1));
        // Add an expired loan with a nonexistent patron
        Library.addLoanWithReturnDate("3", "BI003", "P004", new Date(1, 1, 1));
        Library.checkOverdueLoans();
    }

    @Test
    public void testCheckExpiredHolds() {
        // Place a hold on a media instance
        TransactionManager.placeHold("B002", "P001");
        // Add an expired hold
        Library.addHoldWithReturnDate("2", "BI001", "P001", new Date(1, 1, 1));
        // Add an expired hold with a nonexistent patron
        Library.addHoldWithReturnDate("3", "BI003", "P004", new Date(1, 1, 1));
        Library.checkExpiredHolds();
    }

    @Test
    public void testAddLoanHoldWithReturnDate() {
        // Good data
        // Add a loan with a return date
        Library.addLoanWithReturnDate("1", "BI001", "P001", new Date(1, 1, 1));
        // Check if the loan was added successfully
        assertNotNull(Library.findLoanByInstanceID("BI001"));
        // Add a hold with a return date
        Library.addHoldWithReturnDate("1", "BI002", "P001", new Date(1, 1, 1));
        // Check if the hold was added successfully
        assertNotNull(Library.findHoldByInstanceID("BI002"));
        // Bad data
        // Add a loan with a return date for a non-existent patron
        Library.addLoanWithReturnDate("2", "BI003", null, new Date(1, 1, 1));
        // Add a hold with a return date for a non-existent patron
        Library.addHoldWithReturnDate("2", "BI004", null, new Date(1, 1, 1));
    }

    @Test
    public void testLibraryObserverConstructorForCoverage() {
        LibraryObserver libraryObserver = new LibraryObserver();
    }

}