package library;

import exceptions.LoanException;
import media.Media;
import media.MediaInstance;

import java.util.Date;

public class Loan implements Expires {
    private final String id;
    private final String mediaInstanceID;
    private final String patronID;
    private final Date dateCreated;
    private final Date dateOfReturn;

    public Loan(String id, String mediaInstanceID, String patronID) throws LoanException {
        if (id == null | mediaInstanceID == null | patronID == null) {
            throw new LoanException("Loan must have an ID, media instance ID, and patron ID.");
        } else {
            this.patronID = patronID;
        }
        this.id = id;
        // Ensure the media instance exists
        MediaInstance mediaInstance = Library.findMediaInstanceByID(mediaInstanceID);
        if (mediaInstance == null) {
            throw new LoanException("MediaInstance with ID " + mediaInstanceID + " does not exist.");
        } else {
            this.mediaInstanceID = mediaInstanceID;
        }
        // Ensure the patron exists
        Patron patron = Library.findPatronByID(patronID);
        if (patron == null) {
            throw new LoanException("Patron with ID " + patronID + " does not exist.");
        }
        this.dateCreated = new Date();
        // Date of return is 2 weeks after
        this.dateOfReturn = new Date(dateCreated.getTime() + 1209600000);
    }

    // TESTING: Constructor to create a loan with a specific return date
    public Loan(String id, String mediaInstanceID, String patronID, Date dateOfReturn) throws LoanException {
        if (id == null | mediaInstanceID == null | patronID == null | dateOfReturn == null) {
            throw new LoanException("Loan must have an ID, media instance ID, patron ID, and return date.");
        } else {
            this.patronID = patronID;
        }
        this.id = id;
        // Ensure the media instance exists
        MediaInstance mediaInstance = Library.findMediaInstanceByID(mediaInstanceID);
        if (mediaInstance == null) {
            throw new LoanException("MediaInstance with ID " + mediaInstanceID + " does not exist.");
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

    public Media getMedia() throws LoanException {
        MediaInstance mediaInstance = Library.findMediaInstanceByID(mediaInstanceID);
        return mediaInstance.getMedia();
    }
}
