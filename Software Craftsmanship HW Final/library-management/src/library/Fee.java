package library;

public class Fee {
    private final double DAILY_FEE = 0.25;
    private final double MAX_FEE = 10.00;
    private final Expires expiring;

    public Fee(Expires expiring) {
        if (expiring == null) {
            throw new IllegalArgumentException("Fee must have an expiring object.");
        }
        this.expiring = expiring;
    }

    public double calculateFee() {
        int daysOverdue = expiring.getDaysOverdue();
        if (daysOverdue <= 0) { // Not overdue
            return 0.0;
        } else {
            // Calculate fee
            double fee = daysOverdue * DAILY_FEE;
            return Math.min(fee, MAX_FEE);
        }
    }

}
