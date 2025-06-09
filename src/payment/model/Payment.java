package payment.model;

import java.util.ArrayList;
import java.util.List;

public class Payment {
    private String paymentId;
    private String tenantId;
    private String propertyId;
    private double amount;
    private static List<Payment> paymentHistory = new ArrayList<>();

    public Payment(String paymentId, String tenantId, String propertyId, double amount) {
        this.paymentId = paymentId;
        this.tenantId = tenantId;
        this.propertyId = propertyId;
        this.amount = amount;
        recordPayment();
    }

    private void recordPayment() {
        paymentHistory.add(this);
        System.out.println("Recorded rent payment: Payment ID [" + paymentId + "], Tenant ID [" 
                + tenantId + "], Property ID [" + propertyId + "], Amount: " + amount);
    }

    public static List<Payment> getPaymentHistory() {
        return paymentHistory;
    }

    public static void trackPaymentHistory() {
        System.out.println("\nPayment History:");
        for (Payment payment : paymentHistory) {
            System.out.println("Payment ID [" + payment.paymentId + "], Tenant ID [" + payment.tenantId 
                    + "], Property ID [" + payment.propertyId + "], Amount: " + payment.amount);
        }
    }

    public void sendPaymentReminder() {
        System.out.println("Payment Reminder: Tenant [" + tenantId + "], please pay your rent for property [" + propertyId + "].");
    }
}