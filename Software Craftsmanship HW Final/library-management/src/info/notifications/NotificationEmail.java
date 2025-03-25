package info.notifications;

public class NotificationEmail implements Notification {

    private final String recipient;
    private final String message;

    public NotificationEmail(String message, String recipient) {
        this.message = message;
        this.recipient = recipient;
    }

    @Override
    public void sendNotification() {
        System.out.println("Sending email to " + recipient + ": " + message);
    }
}
