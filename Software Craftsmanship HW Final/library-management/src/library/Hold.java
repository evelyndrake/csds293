package library;

import exceptions.HoldException;
import media.Media;
import media.MediaInstance;

import java.util.Date;

public class Hold implements Expires {
    private final String id;
    private final String mediaInstanceID;
    private final String patronID;
    private final Date dateCreated;
    private final Date dateOfReturn;

    public Hold(String id, String mediaInstanceID, String patronID) throws HoldException {
        if (id == null | mediaInstanceID == null | patronID == null) {
            throw new HoldException("Hold must have an ID, media instance ID, and patron ID.");
        } else {
            this.patronID = patronID;
        }
        this.id = id;
        // Ensure the media instance exists
        MediaInstance mediaInstance = Library.findMediaInstanceByID(mediaInstanceID);
        if (mediaInstance == null) {
            throw new HoldException("MediaInstance with ID " + mediaInstanceID + " does not exist.");
        } else {
            this.mediaInstanceID = mediaInstanceID;
        }
        // Ensure the patron exists
        Patron patron = Library.findPatronByID(patronID);
        if (patron == null) {
            throw new HoldException("Patron with ID " + patronID + " does not exist.");
        }
        this.dateCreated = new Date();
        // Date of return is 2 weeks after
        this.dateOfReturn = new Date(dateCreated.getTime() + 1209600000);
    }

    // Constructor to create a hold with a specific return date
    public Hold(String id, String mediaInstanceID, String patronID, Date dateOfReturn) throws HoldException {
        if (id == null | mediaInstanceID == null | patronID == null | dateOfReturn == null) {
            throw new HoldException("Hold must have an ID, media instance ID, patron ID, and return date.");
        } else {
            this.patronID = patronID;
        }
        this.id = id;
        // Ensure the media instance exists
        MediaInstance mediaInstance = Library.findMediaInstanceByID(mediaInstanceID);
        if (mediaInstance == null) {
            throw new HoldException("MediaInstance with ID " + mediaInstanceID + " does not exist.");
        } else {
            this.mediaInstanceID = mediaInstanceID;
        }
        this.dateCreated = new Date();
        this.dateOfReturn = dateOfReturn;
    }

    public String getID() {
        return id;
    }

    public String getMediaInstanceID() {
        return mediaInstanceID;
    }

    public String getPatronID() {
        return patronID;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateOfReturn() {
        return dateOfReturn;
    }

    public boolean isOverdue() {
        // Date of return is before the current date
        return dateOfReturn.before(new Date());
    }

    public int getDaysOverdue() {
        if (isOverdue()) {
            return (int) ((new Date().getTime() - dateOfReturn.getTime()) / 86400000);
        }
        return 0;
    }

    public Media getMedia() throws HoldException {
        MediaInstance mediaInstance = Library.findMediaInstanceByID(mediaInstanceID);
        return mediaInstance.getMedia();
    }
}
