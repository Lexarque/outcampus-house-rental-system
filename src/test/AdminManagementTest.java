package test;

import user.model.Admin;
import user.view.admin.AdminMenuGUI;

import javax.swing.*;

public class AdminManagementTest {
    public static void main(String[] args) {
        Admin admin = new Admin();

        // Optional: simulate login if needed
        admin.login("5551234567", "password123");

        // Launch the GUI on the Event Dispatch Thread (best practice)
        SwingUtilities.invokeLater(() -> new AdminMenuGUI());
    }
}
