package test;

import payment.model.Payment;
import java.util.*;

public class PaymentTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String tenantId = "T001";
        String landlordId = "L001";
        String adminId = "A001";

        System.out.println("Login as:");
        System.out.println("1. Tenant");
        System.out.println("2. Landlord");
        System.out.println("3. Admin");
        System.out.print("Enter choice (1-3): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.println("\nLogged in as Tenant [" + tenantId + "]");
                System.out.println("1. Record Rent Payment");
                System.out.println("2. View Payment History");
                System.out.print("Choose an action: ");
                int tenantAction = scanner.nextInt();
                scanner.nextLine();

                if (tenantAction == 1) {
                    System.out.print("Enter Property ID: ");
                    String propertyId = scanner.nextLine();
                    System.out.print("Enter Payment Amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    String paymentId = UUID.randomUUID().toString();
                    Date date = new Date();
                    new Payment(paymentId, tenantId, propertyId, amount, date);
                } else if (tenantAction == 2) {
                    Payment.trackPaymentHistory();
                } else {
                    System.out.println("Invalid action.");
                }
                break;

            case 2:
                System.out.println("\nLogged in as Landlord [" + landlordId + "]");
                System.out.print("Enter Tenant ID to send reminder: ");
                String tId = scanner.nextLine();
                System.out.print("Enter Property ID: ");
                String pId = scanner.nextLine();
                Payment.sendReminder(tId, pId);
                break;

            case 3:
                System.out.println("\nLogged in as Admin [" + adminId + "]");
                Payment.trackPaymentHistory();
                break;

            default:
                System.out.println("Invalid choice.");
        }

        scanner.close();
    }
}
