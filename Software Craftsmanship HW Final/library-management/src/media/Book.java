package media;

import exceptions.MediaException;
import library.Library;

import java.util.HashMap;

// Implements media, and contains additional description and genre fields
public class Book implements Media {
    private final String id;
    private final String title;
    private final String authorID;
    private final HashMap<String, String> information = new HashMap<String, String>();

    public Book(String id, String title, String authorID, String description, String genre) throws MediaException {
        this.id = id;
        this.title = title;
        if (Library.findAuthorByID(authorID) == null) {
            throw new MediaException("Author with ID " + authorID + " does not exist.");
        } else {
            this.authorID = authorID;
        }
        this.information.put("description", description);
        this.information.put("genre", genre);
    }

    @Override
    public String getID() {
        return id;
    }
    @Override
    public String getTitle() {
        return title;
    }
    @Override
    public String getAuthorID() {
        return authorID;
    }
    @Override
    public HashMap<String, String> getInformation() {
        return information;
    }
}
