package info.notifications;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

// Manages notifications, which are sent when errors occur or a patron has an overdue book or a hold that has expired
public class NotificationManager {
    // Use a ConcurrentLinkedQueue to store notifications for thread safety
    private static final ConcurrentLinkedQueue<Notification> notifications = new ConcurrentLinkedQueue<>();
    private static final boolean NOTIFICATIONS_ENABLED = false;

    // Add a notification
    public static void addNotification(String message, String recipient) {
        // Assume we want to send both an email and an SMS for each recipient, but this could easily be extended to any number of notification types
        notifications.add(new NotificationEmail(message, recipient));
        notifications.add(new NotificationSMS(message, recipient));
        processNotifications();
    }

    // Process all notifications
    public static void processNotifications() {
        if (!NOTIFICATIONS_ENABLED) {
            return;
        }
        while (!notifications.isEmpty()) {
            Notification notification = notifications.poll();
            notification.sendNotification();
        }
    }

}
