import auth.view.AuthenticationMenu;
import user.view.admin.AdminMenu;
import user.view.landlord.LandlordMenu;
import user.view.tenant.TenantMenu;
import utils.SessionManager;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to Outcampus House Rentals System!");

        if (AuthenticationMenu.printAuthenticationMenu() == -1) {
            System.out.println("Exiting the system. Goodbye!");
            return;
        }

        if (SessionManager.getCurrentUser() != null) {
            switch (SessionManager.getCurrentUser().getRole()) {
                case "admin" -> AdminMenu.printAdminMenu();
                case "landlord" -> LandlordMenu.printLandlordMenu();
                case "tenant" -> TenantMenu.printTenantMenu();
            }
        } else {
            System.out.println("No user logged in.");
        }

        System.out.println("Exiting the system. Goodbye!");
    }
}
