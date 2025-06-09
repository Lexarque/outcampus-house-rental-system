package payment.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Payment {
    private String paymentId;
    private String tenantId;
    private String propertyId;
    private double amount;
    private Date date;
    private static List<Payment> paymentHistory = new ArrayList<>();

    public Payment(String paymentId, String tenantId, String propertyId, double amount, Date date) {
        this.paymentId = paymentId;
        this.tenantId = tenantId;
        this.propertyId = propertyId;
        this.amount = amount;
        this.date = date;
        recordPayment();
    }

    /** Records the payment in the payment history */
    private void recordPayment() {
        paymentHistory.add(this);
        System.out.println("Recorded payment: Payment ID [" + paymentId + "], Tenant ID [" + tenantId +
                "], Property ID [" + propertyId + "], Amount: " + amount + ", Date: " + date);
    }

    /** Displays all payment history */
    public static void trackPaymentHistory() {
        System.out.println("\nPayment History:");
        for (Payment payment : paymentHistory) {
            System.out.println("Payment ID [" + payment.paymentId + "], Tenant ID [" + payment.tenantId +
                    "], Property ID [" + payment.propertyId + "], Amount: " + payment.amount + ", Date: " + payment.date);
        }
    }

    /** Tracks a specific payment */
    public void trackPayment() {
        System.out.println("Tracking payment of RM " + amount + " for property [" + propertyId + 
                "] made by tenant [" + tenantId + "] on " + date);
    }

    /** Sends a reminder to the tenant */
    public void sendReminder() {
        System.out.println("Payment Reminder: Tenant [" + tenantId + "], please pay your rent for property [" + propertyId + "].");
    }

    // Getters and Setters

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getPropertyId() { return propertyId; }
    public void setPropertyId(String propertyId) { this.propertyId = propertyId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}