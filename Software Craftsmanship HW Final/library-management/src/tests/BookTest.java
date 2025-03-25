package tests;

import exceptions.MediaException;
import library.TransactionManager;
import media.AudioBook;
import media.Author;
import media.Book;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class BookTest {
    @Before
    public void setUp() {
        TransactionManager.clearLibrary();
    }

    @Test
    public void testConstructors() {
        // Make an author and add them to the library
        Author author = new Author("1", "author", "biography", new Date(2000,1,1));
        TransactionManager.addAuthor(author);
        // Good data
        // Make a Book with valid parameters
        try {
            Book book = new Book("1", "title", "1", "description", "genre");
        } catch (MediaException e) {
            assert false;
        }
        // Make an AudioBook with valid parameters
        try {
            AudioBook audioBook = new AudioBook("1", "title", "1", "narrator", "length", "genre");
        } catch (MediaException e) {
            assert false;
        }
        // Bad data
        // Make a Book with a nonexistent author
        try {
            Book book = new Book("1", "title", "2", "description", "genre");
            assert false;
        } catch (MediaException e) {
            assert true;
        }
        // Make an AudioBook with a nonexistent author
        try {
            AudioBook audioBook = new AudioBook("1", "title", "2", "narrator", "length", "genre");
            assert false;
        } catch (MediaException e) {
            assert true;
        }
        // Make an AudioBook with missing information
        try {
            AudioBook audioBook = new AudioBook("1", "title", "1", null, null, null);
            assert false;
        } catch (MediaException e) {
            assert true;
        }
    }

    @Test
    public void testGetters() {
        Author author = new Author("1", "author", "biography", new Date(2000,1,1));
        TransactionManager.addAuthor(author);
        // Good data
        // Make a Book with valid parameters
        try {
            Book book = new Book("1", "title", "1", "description", "genre");
            assert book.getID().equals("1");
            assert book.getTitle().equals("title");
            assert book.getAuthorID().equals("1");
            assert book.getInformation().get("description").equals("description");
            assert book.getInformation().get("genre").equals("genre");
        } catch (MediaException e) {
            assert false;
        }
        // Make an AudioBook with valid parameters
        try {
            AudioBook audioBook = new AudioBook("1", "title", "1", "narrator", "length", "genre");
            assert audioBook.getID().equals("1");
            assert audioBook.getTitle().equals("title");
            assert audioBook.getAuthorID().equals("1");
            assert audioBook.getInformation().get("narrator").equals("narrator");
            assert audioBook.getInformation().get("length").equals("length");
            assert audioBook.getInformation().get("genre").equals("genre");
        } catch (MediaException e) {
            assert false;
        }
    }
}
