package info.notifications;

public class NotificationSMS implements Notification {

    private final String recipient;
    private final String message;

    public NotificationSMS(String message, String recipient) {
        this.message = message;
        this.recipient = recipient;
    }

    @Override
    public void sendNotification() {
        System.out.println("Sending SMS to " + recipient + ": " + message);
    }
}
