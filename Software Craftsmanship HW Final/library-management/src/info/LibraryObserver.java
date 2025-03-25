package info;

import media.Media;
import media.MediaInstance;

import java.util.concurrent.ConcurrentHashMap;

// Used to track analytics
public class LibraryObserver {
    // Concurrent hash map to track the popularity and availability of Media/MediaInstance objects
    private static final ConcurrentHashMap<String, Integer> popularity = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Integer> availability = new ConcurrentHashMap<>();
    // Alerts will be shown when a Media object has specific popularity of availability
    private static final int POPULARITY_THRESHOLD = 5;
    private static final int AVAILABILITY_THRESHOLD = 1;

    // Called from TransactionManager when things like checking out a Media object occur
    public static void checkMessages(MediaInstance mediaInstance) {
        if (availability.getOrDefault(mediaInstance.getMediaID(), 0) <= AVAILABILITY_THRESHOLD) {
            LoggerSystem.logWarning("Media with ID " + mediaInstance.getMediaID() + " is low in stock.");
        }
    }

    public static void checkMessages(Media media) {
        if (popularity.getOrDefault(media.getID(), 0) >= POPULARITY_THRESHOLD) {
            LoggerSystem.logInfo("Media with ID " + media.getID() + " is popular.");
        }
        if (availability.getOrDefault(media.getID(), 0) <= AVAILABILITY_THRESHOLD) {
            LoggerSystem.logWarning("Media with ID " + media.getID() + " is low in stock.");
        }
    }

    public static void addCopy(MediaInstance mediaInstance) {
        availability.put(mediaInstance.getMediaID(), availability.getOrDefault(mediaInstance.getMediaID(), 0) + 1);
        checkMessages(mediaInstance);
    }

    public static void removeCopy(MediaInstance mediaInstance) {
        if (mediaInstance == null) {
            return;
        }
        availability.put(mediaInstance.getMediaID(), availability.getOrDefault(mediaInstance.getMediaID(), 0) - 1);
        checkMessages(mediaInstance);
    }

    public static void checkoutMedia(Media media) {
        if (media == null) {
            return;
        }
        availability.put(media.getID(), availability.getOrDefault(media.getID(), 0) - 1);
        popularity.put(media.getID(), popularity.getOrDefault(media.getID(), 0) + 1);
        checkMessages(media);
    }

    public static void reset() {
        popularity.clear();
        availability.clear();
    }
}
