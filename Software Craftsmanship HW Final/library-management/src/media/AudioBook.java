package media;

import exceptions.MediaException;
import library.Library;

import java.util.HashMap;
// Implements Media, and contains some additional information (narrator, length, genre) that is specific to real-world audiobooks.

public class AudioBook implements Media {
    private final String id;
    private final String title;
    private final String authorID;
    private final HashMap<String, String> information = new HashMap<String, String>();

    public AudioBook(String id, String title, String authorID, String narrator, String length, String genre) throws MediaException {
        this.id = id;
        this.title = title;
        if (Library.findAuthorByID(authorID) == null) {
            throw new MediaException("Author with ID " + authorID + " does not exist.");
        } else {
            this.authorID = authorID;
        }
        if (narrator == null | length == null | genre == null) {
            throw new MediaException("AudioBook must have a narrator, length, and genre.");
        }
        this.information.put("narrator", narrator);
        this.information.put("length", length);
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
