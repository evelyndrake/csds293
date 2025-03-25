package media;

import exceptions.MediaException;
import library.Library;

// This class represents a physical instance of a Media object (e.g. one Book can have many copies)
public class MediaInstance {
    private final String id;
    private final String mediaID;
    private MediaInstanceCondition condition = MediaInstanceCondition.GOOD;

    public MediaInstance(String id, String mediaID) throws MediaException {
        if (id == null | mediaID == null) {
            throw new MediaException("MediaInstance must have an ID and media ID.");
        }
        this.id = id;
        this.mediaID = mediaID;
    }

    public MediaInstance(String id, String mediaID, MediaInstanceCondition condition) throws MediaException {
        if (condition == null | id == null | mediaID == null) {
            throw new MediaException("MediaInstance must have an ID, media ID, and condition.");
        }
        this.id = id;
        this.mediaID = mediaID;
        this.condition = condition;
    }

    public MediaInstanceCondition getCondition() {
        return condition;
    }

    public String getID() {
        return id;
    }

    public String getMediaID() {
        return mediaID;
    }

    public String getInformation(String attribute) throws MediaException {
        Media media = Library.findMediaByID(mediaID);
        if (media == null) {
            throw new MediaException("Media with ID " + mediaID + " does not exist.");
        }
        return media.getInformation().get(attribute);
    }

    public Media getMedia() {
        return Library.findMediaByID(mediaID);
    }
}
