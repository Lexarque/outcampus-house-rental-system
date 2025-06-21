import javax.swing.*;

import auth.view.AuthenticationMenuGUI;
import user.model.User;
import user.view.admin.AdminMenu;
import user.view.landlord.LandlordMenuGUI;
import user.view.tenant.TenantMenu;
import utils.SessionManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AuthenticationMenuGUI authDialog = new AuthenticationMenuGUI(null);
            authDialog.setVisible(true);

            if (authDialog.isLoginSuccessful()) {
                User currentUser = SessionManager.getCurrentUser();

                switch (currentUser.getRole()) {
                    case "admin" :
                        AdminMenu.printAdminMenu();
                    case "landlord" :
                        LandlordMenuGUI dialog = new LandlordMenuGUI(null);
                        dialog.setVisible(true);
                        break;
                    case "tenant" :
                        TenantMenu.printTenantMenu();
                }
            } else {
                System.out.println("Authentication cancelled. Exiting.");
            }
        });
    }
}
