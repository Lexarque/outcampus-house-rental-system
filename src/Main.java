import auth.view.AuthenticationMenu;
import user.view.landlord.LandlordMenu;
import user.view.tenant.TenantMenu;
import utils.SessionManager;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to Outcampus House Rentals System!");

        if (AuthenticationMenu.printAuthenticationMenu() == -1) {
            System.out.println("Exiting the system. Goodbye!");
            return;
        }

        if (SessionManager.getCurrentUser() != null) {
            if (SessionManager.isAdmin()) {
                System.out.println("Welcome Admin!");
            } else if (SessionManager.getCurrentUser().getRole().equals("landlord")) {
                LandlordMenu.printLandlordMenu();
            } else if (SessionManager.getCurrentUser().getRole().equals("tenant")) {
                TenantMenu.printTenantMenu();
            }
        } else {
            System.out.println("No user logged in.");
        }

        System.out.println("Exiting the system. Goodbye!");
    }
}
