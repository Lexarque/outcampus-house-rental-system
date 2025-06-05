package test;

import user.model.Landlord;
import user.model.Tenant;

import java.util.Scanner;

public class AuthenticationTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("""
                1. Register\
                
                2. Login\
                
                3.\s""");
        int option = Integer.parseInt(scanner.nextLine());

        switch (option) {
            case 1:
                registerTest();
                break;
            case 2:
                loginTest();
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    }

    private static void registerTest() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            System.out.print("Enter your phone number: ");
            String phone = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();
            System.out.print("Enter your role (landlord/tenant): ");
            String role = scanner.nextLine();
            if (role.equalsIgnoreCase("landlord")) {
                Landlord landlord = new Landlord();
                landlord.register(name, phone, password);
            } else if (role.equalsIgnoreCase("tenant")) {
                Tenant tenant = new Tenant();
                tenant.register(name, phone, password);
            } else {
                System.out.println("Invalid role specified.");
                return;
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void loginTest() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the role you want to log in as (landlord/tenant): ");
            String role = scanner.nextLine();
            System.out.print("Enter your phone number: ");
            String phone = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();
            if (role.equalsIgnoreCase("landlord")) {
                 Landlord landlord = new Landlord();
                 landlord.login(phone, password);
                 landlord.viewOwnListings();
                 landlord.postListing("Beautiful Apartment", "123 Main St", 1500, true);
            } else if (role.equalsIgnoreCase("tenant")) {
                 Tenant tenant = new Tenant();
                 tenant.login(phone, password);
            } else {
                System.out.println("Invalid role specified.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }


    }
}
