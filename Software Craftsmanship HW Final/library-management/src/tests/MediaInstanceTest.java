package tests;

import exceptions.MediaException;
import info.LoggerSystem;
import library.TransactionManager;
import media.Author;
import media.Book;
import media.MediaInstance;
import media.MediaInstanceCondition;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class MediaInstanceTest {
    @Before
    public void setUp() {
        TransactionManager.clearLibrary();
    }

    @Test
    public void testConstructor() {
        // Good data
        // Make a MediaInstance with valid parameters
        try {
            MediaInstance mediaInstance = new MediaInstance("1", "1");
            MediaInstance mediaInstance2 = new MediaInstance("2", "2", MediaInstanceCondition.GOOD);
        } catch (MediaException e) {
            assert false;
        }
        // Bad data (null IDs)
        try {
            MediaInstance mediaInstance3 = new MediaInstance(null, null);
            assert false;
        } catch (MediaException e) {
            assert true;
        }
        try {
            MediaInstance mediaInstance4 = new MediaInstance(null, null, MediaInstanceCondition.GOOD);
            assert false;
        } catch (MediaException e) {
            assert true;
        }
    }

    @Test
    public void testGetters() {
        // Good data
        // Make a MediaInstance with valid parameters
        try {
            TransactionManager.clearLibrary();
            Author author = new Author("1", "name", "biography", new Date(1, 1, 1));
            TransactionManager.addAuthor(author);
            Book book = new Book("100", "title", "1", "description", "genre");
            TransactionManager.addMedia(book);
            MediaInstance mediaInstance = new MediaInstance("1", "100");
            TransactionManager.addMediaInstance(mediaInstance);
            assert mediaInstance.getID().equals("1");
            assert mediaInstance.getMediaID().equals("100");
            assert mediaInstance.getCondition().equals(MediaInstanceCondition.GOOD);
            assert mediaInstance.getInformation("description").equals("description");
        } catch (MediaException e) {
            LoggerSystem.handleException(e);
            assert false;
        }
        // Bad data (nonexistent media)
        try {
            MediaInstance mediaInstance2 = new MediaInstance("2", "2");
            mediaInstance2.getInformation("description");
            assert false;
        } catch (MediaException e) {
            assert true;
        }
    }
}
