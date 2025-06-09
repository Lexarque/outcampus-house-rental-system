package payment.model;

public class Tenant {
    public void viewRentPayments() {
        System.out.println("\nViewing Rent Payment Records...");
        Payment.trackPaymentHistory();
    }

    public void trackPaymentHistory() {
        System.out.println("\nTracking Payment History...");
        Payment.trackPaymentHistory();
    }
}
