package payment.model;

public class Admin {
    public void viewRentPayments() {
        System.out.println("\n[Admin] Viewing Rent Payment Records...");
        Payment.trackPaymentHistory();
    }

    public void trackPaymentHistory() {
        System.out.println("\n[Admin] Tracking Payment History...");
        Payment.trackPaymentHistory();
    }

    public void sendPaymentReminders() {
        System.out.println("\n[Admin] Sending Payment Reminders...");
        for (Payment payment : Payment.getPaymentHistory()) {
            payment.sendPaymentReminder();
        }
    }
}
