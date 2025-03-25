package library;

import info.notifications.Notification;
import info.notifications.NotificationManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

// Represents a patron of the library. Could be extended to include contact information and notification preferences
public class Patron {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final Date dateOfBirth;
    private final Date dateOfRegistration;
    private final HashMap<Expires, Fee> fees = new HashMap<>();
    private double amountPaidOff = 0.0;
    private boolean isSuspended = false;

    public Patron(String id, String firstName, String lastName, Date dateOfBirth) {
        // All information must be entered
        if (id == null || firstName == null || lastName == null || dateOfBirth == null) {
            throw new IllegalArgumentException("Patron must have an ID, first name, last name, and date of birth.");
        }
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.dateOfRegistration = new Date();
    }

    public String getID() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }
    public void notify(String message) {
        NotificationManager.addNotification(message, this.toString());
    }
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    // Gives the patron a new fee for a particular piece of media when the media is overdue
    public void addFee(Expires expires) {
        fees.putIfAbsent(expires, new Fee(expires));
    }

    // Calculates the total amount owed by the patron
    public double getTotalAmountOwed() {
        double total = 0;
        for (Fee fee : fees.values()) {
            total += fee.calculateFee();
        }
        total -= amountPaidOff;
        return total;
    }

    // Functionality to pay off fees
    public void payOff(double amount) {
        amountPaidOff += amount;
    }
    public double getAmountPaidOff() {
        return amountPaidOff;
    }
    // Patrons can be suspended, which prevents them from checking out books
    // Could automatically update if their outstanding balance surpasses a specific threshold

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

}
