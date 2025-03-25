package library;

import info.LibraryObserver;
import info.LoggerSystem;
import media.Author;
import media.Media;
import media.MediaInstance;

import java.util.concurrent.Future;

// Interfaces with the Library class to manage transactions (adding and removing from the library)
// This handles Future objects to ensure that the transactions are completed before returning values
// If the system was extended to include multi-stage transactions, like adding both a book and its author at once,
// this class could be used to ensure that all changes are committed to the database or none of them are, rolling the library back
public class TransactionManager {

    private static LoginType loginType = LoginType.LIBRARIAN;
    private static boolean shouldFail = false;

    // Login as a librarian or patron
    public static void loginAs(LoginType loginType) {
        if (loginType == null) {
            LoggerSystem.logWarning("Login type cannot be null.");
            return;
        }
        TransactionManager.loginType = loginType;
    }

    // TESTING: Set whether transactions should fail
    public static void setShouldFail(boolean shouldFail) {
        TransactionManager.shouldFail = shouldFail;
    }

    // Add a media item to the library inventory
    public static String addMedia(Media media) {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can add media to the library.");
            return null;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.addMedia(media);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
            return null;
        }
        return media.getID();
    }

    // Remove a media item from the library inventory by ID
    public static void removeMedia(String mediaID) {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can remove media from the library.");
            return;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.removeMedia(mediaID);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
        }
    }

    // Add a patron to the library
    public static String addPatron(Patron patron) {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can add patrons to the library.");
            return null;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.addPatron(patron);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
            return null;
        }
        return patron.getID();
    }

    // Remove a patron from the library by ID
    public static void removePatron(String patronID) {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can remove patrons from the library.");
            return;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.removePatron(patronID);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
        }
    }

    // Set a patron's suspension status
    public static void setPatronSuspended(String patronID, boolean isSuspended) {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can set a patron's suspension status.");
            return;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.setPatronSuspended(patronID, isSuspended);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
        }
    }

    // Add an author to the library
    public static String addAuthor(Author author) {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can add authors to the library.");
            return null;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.addAuthor(author);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
            return null;
        }
        return author.getID();
    }

    // Remove an author from the library by ID
    public static void removeAuthor(String authorID) {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can remove authors from the library.");
            return;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.removeAuthor(authorID);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
        }
    }

    // Add a media instance to the library inventory
    public static String addMediaInstance(MediaInstance mediaInstance) {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can add media instances to the library.");
            return null;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.addMediaInstance(mediaInstance);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
            return null;
        }
        LibraryObserver.addCopy(mediaInstance);
        return mediaInstance.getID();
    }

    // Check out media item to patron
    public static void checkOutMedia(String mediaID, String patronID) {
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            LibraryObserver.checkoutMedia(Library.findMediaByID(mediaID));
            Future<?> future = Library.checkOutMedia(mediaID, patronID);
            future.get();

        } catch (Exception e) {
            LoggerSystem.handleException(e);
        }

    }

    // Put a media item on hold for a patron
    public static void placeHold(String mediaID, String patronID) {
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.placeHold(mediaID, patronID);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
        }
    }

    // Clear the library inventory
    public static void clearLibrary() {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can clear the library inventory.");
            return;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.clearLibrary();
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
        }
        LibraryObserver.reset();
    }

    // Remove a media instance from the library inventory by ID
    public static void removeMediaInstance(String mediaInstanceID) {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can remove media instances from the library.");
            return;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            LibraryObserver.removeCopy(Library.findMediaInstanceByID(mediaInstanceID));
            Future<?> future = Library.removeMediaInstance(mediaInstanceID);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
        }
    }

    // Update a media instance
    public static void updateMediaInstance(MediaInstance mediaInstance) {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can update media instances.");
            return;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.updateMediaInstance(mediaInstance);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
        }
    }

    // Update media
    public static void updateMedia(Media media) {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can update media.");
            return;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.updateMedia(media);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
        }
    }

    // Update patron
    public static void updatePatron(Patron patron) {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can update patrons.");
            return;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.updatePatron(patron);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
        }
    }

    // Update author
    public static void updateAuthor(Author author) {
        if (loginType != LoginType.LIBRARIAN) {
            LoggerSystem.logWarning("Only librarians can update authors.");
            return;
        }
        try {
            if (shouldFail) {
                throw new Exception("Transaction failed.");
            }
            Future<?> future = Library.updateAuthor(author);
            future.get();
        } catch (Exception e) {
            LoggerSystem.handleException(e);
        }
    }

    public static LoginType getLoginType() {
        return loginType;
    }

}
