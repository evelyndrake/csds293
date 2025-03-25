package tests;

import info.LoggerSystem;
import info.notifications.NotificationEmail;
import info.notifications.NotificationManager;
import info.notifications.NotificationSMS;
import org.junit.Test;

public class NotificationTest {
    // Create notifications of each type and test the sendNotification method
    @Test
    public void testNotificationTypes() {
        NotificationEmail email = new NotificationEmail("Test email message", "Admin");
        email.sendNotification();
        NotificationSMS sms = new NotificationSMS("Test SMS message", "Admin");
        sms.sendNotification();
    }

    @Test
    public void testNotificationManagerConstructorForCoverage() {
        new NotificationManager();
    }

    @Test
    public void testLoggerSystemConstructorForCoverage() {
        new LoggerSystem();
    }
}
