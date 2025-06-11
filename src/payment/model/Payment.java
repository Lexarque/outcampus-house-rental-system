package payment.model;

import property.model.Property;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Payment {

    private String paymentId;
    private String tenantId;
    private String propertyId;
    private double amount;
    private LocalDate date;

    private static final String PAYMENT_CSV_PATH = "src/file/payment/payments.csv";

    public Payment() {}

    public Payment(String paymentId, String tenantId, String propertyId, double amount, LocalDate date) {
        this.paymentId = paymentId;
        this.tenantId = tenantId;
        this.propertyId = propertyId;
        this.amount = amount;
        this.date = date;
    }

    public static void setPaymentHistory(String tenantId, List<Payment> paymentHistory) {
        File file = new File(PAYMENT_CSV_PATH);
        if (!file.exists()) {
            System.out.println("Payment history file does not exist.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // skip header
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String paymentTenantId = parts[1];

                    if (paymentTenantId.equals(tenantId)) {
                        paymentHistory.add(new Payment(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), LocalDate.parse(parts[4])));

                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading payment history: " + e.getMessage());
        }
    }

    public void recordPayment(List<Payment> paymentHistory) {
        paymentHistory.add(this);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENT_CSV_PATH, true))) {
            writer.write(String.join(",", paymentId, tenantId, propertyId, String.valueOf(amount), date.toString()));
            writer.newLine();
        } catch (Exception e) {
            System.err.println("Error recording payment: " + e.getMessage());
        }
    }

//    public static void trackPaymentHistory() {
//        System.out.println("\nPayment History:");
//        for (Payment payment : paymentHistory) {
//            System.out.println("Payment ID [" + payment.paymentId + "], Tenant ID [" + payment.tenantId +
//                    "], Property ID [" + payment.propertyId + "], Amount: RM" + payment.amount + ", Date: "
//                    + payment.date);
//        }
//    }

    public static void sendReminder(String tenantId, String propertyId) {
        System.out.println(
                "Payment Reminder: Tenant [" + tenantId + "], please pay your rent for property [" + propertyId + "].");
    }

    public void printBasicPayments() {
        System.out.println("Property ID: " + propertyId);
        System.out.println("Amount: RM" + amount);
        System.out.println("Date: " + date);
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
