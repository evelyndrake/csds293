package tests;

import media.Author;
import org.junit.Test;

import java.util.Date;

public class AuthorTest {
    @Test
    public void testConstructor() {
        // Good data
        // Make an Author with valid parameters
        try {
            Author author = new Author("1", "author", "biography", new Date(2000,1,1));
        } catch (IllegalArgumentException e) {
            assert false;
        }
        // Bad data (null parameters)
        try {
            Author author = new Author(null, null, null, null);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    public void testGetters() {
        // Good data
        // Make an Author with valid parameters
        try {
            Author author = new Author("1", "author", "biography", new Date(2000,1,1));
            assert author.getID().equals("1");
            assert author.getName().equals("author");
            assert author.getBiography().equals("biography");
            assert author.getBirthDate().equals(new Date(2000,1,1));
            assert author.isActive();
        } catch (IllegalArgumentException e) {
            assert false;
        }
    }
}
