package payment.model;

public class Landlord {
    public void sendPaymentReminders() {
        System.out.println("\nSending Payment Reminders...");
        for (Payment payment : Payment.getPaymentHistory()) {
            payment.sendPaymentReminder();
        }
    }
}
