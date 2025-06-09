package payment;

import payment.model.Payment;

public class PaymentMain {
    public static void main(String[] args) {
        // Create Payment instances.
        // Each new object automatically records its payment entry to the payment history.
        Payment payment1 = new Payment("P001", "T001", "PR001", 900.00);
        Payment payment2 = new Payment("P002", "T002", "PR002", 1200.00);
        Payment payment3 = new Payment("P003", "T003", "PR003", 1050.00);

        // Display the cumulative payment history.
        Payment.trackPaymentHistory();

        // Send payment reminders to the tenants.
        payment1.sendPaymentReminder();
        payment2.sendPaymentReminder();
        payment3.sendPaymentReminder();
    }
}