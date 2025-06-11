package test;

import user.model.Admin;

import java.util.Scanner;

public class AdminManagementTest {
    private static final Admin admin = new Admin();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Admin login
        admin.login("5551234567", "password123");

        System.out.print("""
                \nAdmin Management Menu:
                1. View All Users
                2. View All Properties
                3. Remove a User
                4. Deactivate a Property
                Choose option (1-5):\s""");

        int option = Integer.parseInt(scanner.nextLine());

        switch (option) {
            case 1 -> admin.readAllUsers();
            case 2 -> admin.readAllProperties(false);
            case 3 -> removeUserTest();
            case 4 -> deactivatePropertyTest();
            default -> System.out.println("Invalid option!");
        }
    }

    private static void removeUserTest() {
        System.out.print("Enter the User Name to remove: ");
        String userName = scanner.nextLine();
        admin.removeUserByName(userName);
    }

    private static void deactivatePropertyTest() {
        System.out.print("Enter the Property Name to deactivate: ");
        String propertyId = scanner.nextLine();
        admin.deactivateProperty(propertyId);
    }
}