import javax.swing.*;

import auth.view.AuthenticationMenuGUI;
import user.model.User;
import user.view.admin.AdminMenuGUI;
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
                switch (currentUser.getRole().toLowerCase()) {
                    case "admin":
                        new AdminMenuGUI(null).setVisible(true);
                        break;
                    case "landlord":
                        new LandlordMenuGUI(null).setVisible(true);
                        break;
                    case "tenant":
                        TenantMenu.printTenantMenu();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Unknown user role: " + currentUser.getRole());
                }
            } else {
                System.out.println("Authentication cancelled. Exiting.");
            }
        });
    }
}
