package auth.view;

import user.model.Admin;
import user.model.Landlord;
import user.model.Tenant;
import user.model.User;
import user.view.landlord.LandlordMenu;
import utils.SessionManager;

import java.util.Scanner;

public class AuthenticationMenu {
    private static Scanner scanner = new Scanner(System.in);

    public static int printAuthenticationMenu() {
        while(true) {
            System.out.print("Please select an option:\n1. Register\n2. Login\n3. Exit\n: ");
            int option = Integer.parseInt(scanner.nextLine());

            if (option == 3) {
                return -1;
            }

            switch (option) {
                case 1:
                    registerPrompt();
                    break;
                case 2:
                    int loginResult = loginPrompt();
                    if (loginResult > 0) {
                        return 1;
                    }
                    break;
            }
        }
    }

    private static void registerPrompt() {
        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your role (landlord/tenant): ");
        String role = scanner.nextLine();
        switch (role.toLowerCase()) {
            case "landlord":
                User landlord = new Landlord();
                landlord.register(name, phone, password);
                break;
            case "tenant":
                User tenant = new Tenant();
                tenant.register(name, phone, password);
                break;
        }
    }

    private static int loginPrompt() {
        System.out.print("Enter your role (admin/landlord/tenant): ");
        String role = scanner.nextLine();
        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        switch (role.toLowerCase()) {
            case "admin":
                Admin admin = new Admin();
                admin.login(phone, password);
                SessionManager.setCurrentUser(admin);
            case "landlord":
                Landlord landlord = new Landlord();
                landlord.login(phone, password);
                SessionManager.setCurrentUser(landlord);
                break;
            case "tenant":
                Tenant tenant = new Tenant();
                tenant.login(phone, password);
                SessionManager.setCurrentUser(tenant);
                break;
            default:
                System.out.println("Invalid role specified.");
                return -1;
        }

        if (SessionManager.getCurrentUser().getUserId() == null) {
            return -1;
        }

        return 1;
    }
}
