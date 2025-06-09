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

    /** Records the rent payment in the payment history */
    private void recordPayment() {
        paymentHistory.add(this);
        System.out.println("Recorded rent payment: Payment ID [" + paymentId + "], Tenant ID [" 
                + tenantId + "], Property ID [" + propertyId + "], Amount: " + amount);
    }

    /** Displays all recorded payment history */
    public static void trackPaymentHistory() {
        System.out.println("\nPayment History:");
        for (Payment payment : paymentHistory) {
            System.out.println("Payment ID [" + payment.paymentId + "], Tenant ID [" + payment.tenantId 
                    + "], Property ID [" + payment.propertyId + "], Amount: " + payment.amount);
        }
    }

    /** Sends a payment reminder to the tenant */
    public void sendPaymentReminder() {
        System.out.println("Payment Reminder: Tenant [" + tenantId 
                + "], please pay your rent for property [" + propertyId + "].");
    }

    // Getters and Setters

    public String getPaymentId() { 
        return paymentId; 
    }
    public void setPaymentId(String paymentId) { 
        this.paymentId = paymentId; 
    }

    public String getTenantId() { 
        return tenantId; 
    }
    public void setTenantId(String tenantId) { 
        this.tenantId = tenantId; 
    }

    public String getPropertyId() { 
        return propertyId; 
    }
    public void setPropertyId(String propertyId) { 
        this.propertyId = propertyId; 
    }

    public double getAmount() { 
        return amount; 
    }
    public void setAmount(double amount) { 
        this.amount = amount; 
    }
}