package library;

import exceptions.HoldException;
import exceptions.LoanException;
import info.LoggerSystem;
import media.Author;
import media.Media;
import media.MediaInstance;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Library {
    // Maps to store all media, media instances, patrons, loans, holds, and authors
    private static final Map<String, Media> inventory = new ConcurrentHashMap<>();
    private static final Map<String, Patron> patrons = new ConcurrentHashMap<>();
    private static final Map<String, Author> authors = new ConcurrentHashMap<>();
    private static final Map<String, Loan> loans = new ConcurrentHashMap<>();
    private static final Map<String, Hold> holds = new ConcurrentHashMap<>();
    private static final Map<String, MediaInstance> mediaInstances = new ConcurrentHashMap<>();
    // Executor service for handling asynchronous tasks that add/remove data
     private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    // Add media to the library inventory asynchronously
    protected static Future<?> addMedia(Media media) {
        return executor.submit(() -> {
            if (inventory.containsKey(media.getID())) {
                LoggerSystem.logWarning("Media with ID " + media.getID() + " already exists in the library.");
            } else {
                inventory.put(media.getID(), media);
                LoggerSystem.logInfo("Added media with ID: " + media.getID());
            }
        });
    }

    // Remove a media item from the library inventory by ID asynchronously
    protected static Future<?> removeMedia(String mediaID) {
        return executor.submit(() -> {
            if (inventory.containsKey(mediaID)) {
                inventory.remove(mediaID);
                LoggerSystem.logInfo("Removed media with ID: " + mediaID);
            } else {
                LoggerSystem.logWarning("Media with ID " + mediaID + " does not exist in the inventory.");
            }
        });
    }

    // Remove a media instance from the library inventory by ID asynchronously
    protected static Future<?> removeMediaInstance(String mediaInstanceID) {
        return executor.submit(() -> {
            if (mediaInstances.containsKey(mediaInstanceID)) {
                mediaInstances.remove(mediaInstanceID);
                LoggerSystem.logInfo("Removed media instance with ID: " + mediaInstanceID);
            } else {
                LoggerSystem.logWarning("Media instance with ID " + mediaInstanceID + " does not exist in the library.");
            }
        });
    }

    // Add a patron to the library asynchronously
    protected static Future<?> addPatron(Patron patron) {
        return executor.submit(() -> {
            if (patrons.containsKey(patron.getID())) {
                LoggerSystem.logWarning("Patron with ID " + patron.getID() + " already exists in the library.");
            } else {
                patrons.put(patron.getID(), patron);
                LoggerSystem.logInfo("Added patron with ID: " + patron.getID());
            }
        });
    }

    // Remove a patron from the library by patron ID asynchronously
    protected static Future<?> removePatron(String patronID) {
        return executor.submit(() -> {
            if (patrons.containsKey(patronID)) {
                patrons.remove(patronID);
                LoggerSystem.logInfo("Removed patron with ID: " + patronID);
            } else {
                LoggerSystem.logWarning("Patron with ID " + patronID + " does not exist in the library.");
            }
        });
    }

    // Find a patron by ID
    public static Patron findPatronByID(String patronID) {
        if (patrons.containsKey(patronID)) {
            return patrons.get(patronID);
        }
        return null;
    }

    // Set a patron's suspension status asynchronously
    protected static Future<?> setPatronSuspended(String patronID, boolean suspended) {
        return executor.submit(() -> {
            Patron patron = findPatronByID(patronID);
            if (patron != null) {
                patron.setSuspended(suspended);
                LoggerSystem.logInfo("Patron with ID " + patronID + " has been suspended: " + suspended);
            } else {
                LoggerSystem.logWarning("Patron with ID " + patronID + " does not exist in the library.");
            }
        });
    }

    // Add an author to the library
    protected static Future<?> addAuthor(Author author) {
        return executor.submit(() -> {
            if (authors.containsKey(author.getID())) {
                LoggerSystem.logWarning("Author with ID " + author.getID() + " already exists in the library.");
            } else {
                authors.put(author.getID(), author);
                LoggerSystem.logInfo("Added author with ID: " + author.getID());
            }
        });
    }

    // Remove an author from the library by author ID
    protected static Future<?> removeAuthor(String authorID) {
        return executor.submit(() -> {
            if (authors.containsKey(authorID)) {
                authors.remove(authorID);
                LoggerSystem.logInfo("Removed author with ID: " + authorID);
            } else {
                LoggerSystem.logWarning("Author with ID " + authorID + " does not exist in the library.");
            }
        });
    }

    // Search for a media item by title
    public static Media findMediaByTitle(String title) {
        for (Media media : inventory.values()) {
            if (media.getTitle().equals(title)) {
                return media;
            }
        }
        return null;
    }

    // Search for a media item by information type and value
    public static Media findMediaByInformation(String type, String value) {
        for (Media media : inventory.values()) {
            if (media.getInformation().containsKey(type) && media.getInformation().get(type).equals(value)) {
                return media;
            }
        }
        return null;
    }

    // Search for a media item by ID
    public static Media findMediaByID(String mediaID) {
        if (inventory.containsKey(mediaID)) {
            return inventory.get(mediaID);
        }
        return null;
    }

    // Search for a media instance by ID
    public static MediaInstance findMediaInstanceByID(String mediaInstanceID) {
        if (mediaInstances.containsKey(mediaInstanceID)) {
            return mediaInstances.get(mediaInstanceID);
        }
        return null;
    }

    // Search for a media instance by media ID
    public static MediaInstance findMediaInstanceByMediaID(String mediaID) {
        for (MediaInstance mediaInstance : mediaInstances.values()) {
            if (mediaInstance.getMedia().getID().equals(mediaID)) {
                return mediaInstance;
            }
        }
        return null;
    }

    // Check if a media item exists in the library
    public static boolean isMediaInInventory(String mediaID) {
        return inventory.containsKey(mediaID);
    }

    // Check if a media item is available
    public static boolean isMediaAvailable(String mediaID) {
        // Iterate over all media instances in the library
        if (inventory.containsKey(mediaID)) {
            for (MediaInstance mediaInstance : mediaInstances.values()) {
                // Check if the media instance corresponds to the requested mediaID and is available
                if (mediaInstance.getMedia().getID().equals(mediaID) && isMediaInstanceAvailable(mediaInstance.getID())) {
                    return true; // Media is available
                }
            }
        }
        // If no available instance is found, return false
        return false;
    }


    // Check if a media instance is available
    public static boolean isMediaInstanceAvailable(String mediaInstanceID) {
        // Check if there exists a loan or hold for the media instance
        return findLoanByInstanceID(mediaInstanceID) == null && findHoldByInstanceID(mediaInstanceID) == null;
    }

    // Return an available instance of a media item with an ID, if possible
    public static MediaInstance getAvailableMediaInstance(String mediaID) {
        // Check if the media exists in the library
        if (!inventory.containsKey(mediaID)) {
            return null;
        }
        // Check if there exists an available instance of the media
        for (MediaInstance mediaInstance : mediaInstances.values()) {
            if (mediaInstance.getMedia().getID().equals(mediaID) && isMediaInstanceAvailable(mediaInstance.getID())) {
                return mediaInstance;
            }
        }
        return null;
    }

    // Check out a media item to a patron asynchronously
    protected static Future<?> checkOutMedia(String mediaID, String patronID) {
        return executor.submit(() -> {
            if (inventory.containsKey(mediaID)) {
                MediaInstance mediaInstance = getAvailableMediaInstance(mediaID);
                if (mediaInstance != null) {
                    Loan loan = null;
                    try {
                        loan = new Loan(Integer.toString(loans.size() + 1), mediaInstance.getID(), patronID);
                    } catch (LoanException e) {
                        LoggerSystem.logError("Failed to create loan for media with ID " + mediaID);
                    }
                    if (loan != null && !patrons.get(patronID).isSuspended()) {
                        loans.put(loan.getID(), loan);
                    } else {
                        LoggerSystem.logWarning("Patron with ID " + patronID + " is suspended and cannot check out media.");
                    }
                    LoggerSystem.logInfo("Media with ID " + mediaID + " checked out to patron with ID " + patronID);
                } else {
                    LoggerSystem.logWarning("No available instances of media with ID " + mediaID);
                }
            } else {
                LoggerSystem.logWarning("Could not add the loan.");
            }
        });
    }

    // Put a media instance on hold for a patron asynchronously
    protected static Future<?> placeHold(String mediaID, String patronID) {
        return executor.submit(() -> {
            if (inventory.containsKey(mediaID)) {
                MediaInstance mediaInstance = getAvailableMediaInstance(mediaID);
                if (mediaInstance != null) {
                    Hold hold = null;
                    try {
                        hold = new Hold(Integer.toString(holds.size() + 1), mediaInstance.getID(), patronID);
                    } catch (Exception e) {
                        LoggerSystem.logError("Failed to create hold for media with ID " + mediaID);
                    }
                    if (hold != null && !patrons.get(patronID).isSuspended()) {
                        holds.put(hold.getID(), hold);
                    }
                    LoggerSystem.logInfo("Media with ID " + mediaID + " put on hold for patron with ID " + patronID);
                } else {
                    LoggerSystem.logWarning("No available instances of media with ID " + mediaID);
                }
            } else {
                LoggerSystem.logWarning("Could not place the hold.");
            }
        });
    }

    // Add a new media instance to the library asynchronously
    protected static Future<?> addMediaInstance(MediaInstance mediaInstance) {
        return executor.submit(() -> {
            if (mediaInstances.containsKey(mediaInstance.getID())) {
                LoggerSystem.logWarning("Media instance with ID " + mediaInstance.getID() + " already exists in the library.");
            } else {
                mediaInstances.put(mediaInstance.getID(), mediaInstance);
                LoggerSystem.logInfo("Added media instance with ID: " + mediaInstance.getID());
            }
        });
    }

    // Find an author by ID asynchronously
    public static Author findAuthorByID(String authorID) {
        if (authors.containsKey(authorID)) {
            return authors.get(authorID);
        }
        return null;
    }

    // Find a loan by instance ID
    public static Loan findLoanByInstanceID(String instanceID) {
        for (Loan loan : loans.values()) {
            if (loan.getMediaInstanceID().equals(instanceID)) {
                return loan;
            }
        }
        return null;
    }

    // Find a hold by instance ID
    public static Hold findHoldByInstanceID(String instanceID) {
        for (Hold hold : holds.values()) {
            if (hold.getMediaInstanceID().equals(instanceID)) {
                return hold;
            }
        }
        return null;
    }

    // Asynchronously clear all library data
    protected static Future<?> clearLibrary() {
        return executor.submit(() -> {
            inventory.clear();
            patrons.clear();
            authors.clear();
            loans.clear();
            holds.clear();
            mediaInstances.clear();
            LoggerSystem.logInfo("Cleared library data.");
        });
    }

    // Update a media instance in the library asynchronously
    protected static Future<?> updateMediaInstance(MediaInstance mediaInstance) {
        return executor.submit(() -> {
            if (mediaInstances.containsKey(mediaInstance.getID())) {
                mediaInstances.put(mediaInstance.getID(), mediaInstance);
                LoggerSystem.logInfo("Updated media instance with ID: " + mediaInstance.getID());
            } else {
                LoggerSystem.logWarning("Media instance with ID " + mediaInstance.getID() + " does not exist in the library.");
            }
        });
    }

    // Update a media in the library asynchronously
    protected static Future<?> updateMedia(Media media) {
        return executor.submit(() -> {
            if (inventory.containsKey(media.getID())) {
                inventory.put(media.getID(), media);
                LoggerSystem.logInfo("Updated media with ID: " + media.getID());
            } else {
                LoggerSystem.logWarning("Media with ID " + media.getID() + " does not exist in the library.");
            }
        });
    }

    // Update a patron in the library asynchronously
    protected static Future<?> updatePatron(Patron patron) {
        return executor.submit(() -> {
            if (patrons.containsKey(patron.getID())) {
                patrons.put(patron.getID(), patron);
                LoggerSystem.logInfo("Updated patron with ID: " + patron.getID());
            } else {
                LoggerSystem.logWarning("Patron with ID " + patron.getID() + " does not exist in the library.");
            }
        });
    }

    // Update an author in the library asynchronously
    protected static Future<?> updateAuthor(Author author) {
        return executor.submit(() -> {
            if (authors.containsKey(author.getID())) {
                authors.put(author.getID(), author);
                LoggerSystem.logInfo("Updated author with ID: " + author.getID());
            } else {
                LoggerSystem.logWarning("Author with ID " + author.getID() + " does not exist in the library.");
            }
        });
    }

    // Method which checks for overdue loans and sends notifications to their owner (would be scheduled to run daily in production)
    public static void checkOverdueLoans() {
        for (Loan loan : loans.values()) {
            if (loan.isOverdue()) {
                Patron patron = findPatronByID(loan.getPatronID());
                if (patron != null) {
                    patron.notify("Loan with ID " + loan.getID() + " is overdue, and you will be feed daily.");
                    // Give this patron a fee
                    patron.addFee(loan);
                }
            }
        }
    }

    // Method to find all of a patron's loans
    public static ArrayList<Loan> findPatronLoans(String patronID) {
        ArrayList<Loan> patronLoans = new ArrayList<>();
        for (Loan loan : loans.values()) {
            if (loan.getPatronID().equals(patronID)) {
                patronLoans.add(loan);
            }
        }
        return patronLoans;
    }

    // Method to find all of a patron's holds
    public static ArrayList<Hold> findPatronHolds(String patronID) {
        ArrayList<Hold> patronHolds = new ArrayList<>();
        for (Hold hold : holds.values()) {
            if (hold.getPatronID().equals(patronID)) {
                patronHolds.add(hold);
            }
        }
        return patronHolds;
    }



    // Method which checks for holds that are expired and removes them (would be scheduled to run daily in production)
    public static void checkExpiredHolds() {
        for (Hold hold : holds.values()) {
            if (hold.isOverdue()) {
                Patron patron = findPatronByID(hold.getPatronID());
                if (patron != null) {
                    patron.notify("Hold with ID " + hold.getID() + " has expired and been canceled.");
                }
                holds.remove(hold.getID());
            }
        }
    }

    // TEST METHOD: Add a hold with a specific return date
    public static void addHoldWithReturnDate(String id, String mediaInstanceID, String patronID, Date dateOfReturn) {
        try {
            Hold hold = new Hold(id, mediaInstanceID, patronID, dateOfReturn);
            holds.put(hold.getID(), hold);
        } catch (HoldException e) {
            LoggerSystem.logError("Failed to create hold with ID " + id);
        }
    }

    // TEST METHOD: Add a loan with a specific return date
    public static void addLoanWithReturnDate(String id, String mediaInstanceID, String patronID, Date dateOfReturn) {
        try {
            Loan loan = new Loan(id, mediaInstanceID, patronID, dateOfReturn);
            loans.put(loan.getID(), loan);
        } catch (LoanException e) {
            LoggerSystem.logError("Failed to create loan with ID " + id);
        }
    }

}
