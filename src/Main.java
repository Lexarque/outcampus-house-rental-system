import javax.swing.*;

import auth.view.AuthenticationMenuGUI;
import user.model.User;
import user.view.admin.AdminMenu;
import user.view.landlord.LandlordMenuGUI;
import user.view.tenant.TenantMenu;
import user.view.tenant.TenantMenuGUI;
import utils.SessionManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AuthenticationMenuGUI authDialog = new AuthenticationMenuGUI(null);
            authDialog.setVisible(true);

            if (authDialog.isLoginSuccessful()) {
                User currentUser = SessionManager.getCurrentUser();

                switch (currentUser.getRole()) {
                    case "admin":
                        AdminMenu.printAdminMenu();
                    case "landlord":
                        LandlordMenuGUI dialog = new LandlordMenuGUI(null);
                        dialog.setVisible(true);
                        break;
                    case "tenant":
                        TenantMenuGUI tenantDialog = new TenantMenuGUI(null);
                        tenantDialog.setVisible(true);

                }
            } else {
                System.out.println("Authentication cancelled. Exiting.");
            }
        });
    }
}
