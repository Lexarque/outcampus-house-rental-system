package user.view.admin;

import user.model.Admin;
import utils.SessionManager;

import java.util.Scanner;

public class AdminMenu {
    private static Admin admin = SessionManager.setAdminData();
    private final static Scanner scanner = new Scanner(System.in);

    public static void printAdminMenu() {
        while (true) {
            System.out.print("""
                    1. View All Users\

                    2. View All Properties\

                    3. Remove a User\

                    4. Deactivate a Property\

                    5. Exit

                    : \s""");

            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    admin.readAllUsers();
                    break;
                case 2:
                    admin.readAllProperties(false);
                    break;
                case 3:
                    removeUser();
                    break;
                case 4:
                    deactivateProperty();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private static void removeUser() {
        admin.readAllUsers();
        System.out.print("Enter the User Name to remove: ");
        String userName = scanner.nextLine();
        admin.removeUserByName(userName);
    }

    private static void deactivateProperty() {
        admin.readAllProperties(true);
        System.out.print("Enter the Property Name to deactivate: ");
        String propertyId = scanner.nextLine();
        admin.deactivateProperty(propertyId);
    }
}
