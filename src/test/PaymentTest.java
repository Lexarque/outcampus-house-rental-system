package test;

import payment.model.Payment;
import payment.model.Admin;
import payment.model.Landlord;
import payment.model.Tenant;

public class PaymentTest {
    public static void main(String[] args) {
        // Dummy payment records
        Payment payment1 = new Payment("P001", "T001", "PR001", 900.00);
        Payment payment2 = new Payment("P002", "T002", "PR002", 1200.00);
        Payment payment3 = new Payment("P003", "T003", "PR003", 1050.00);

        // Simulating Tenant Role
        Tenant tenant = new Tenant();
        tenant.viewRentPayments();
        tenant.trackPaymentHistory();

        // Simulating Landlord Role
        Landlord landlord = new Landlord();
        landlord.sendPaymentReminders();

        // Simulating Admin Role
        Admin admin = new Admin();
        admin.viewRentPayments();
        admin.trackPaymentHistory();
        admin.sendPaymentReminders();
    }
}