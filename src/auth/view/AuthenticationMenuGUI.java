package auth.view;

import user.model.Admin;
import user.model.Landlord;
import user.model.Tenant;
import user.model.User;
import utils.SessionManager;

import javax.swing.*;
import java.awt.*;

public class AuthenticationMenuGUI extends JDialog {
    private boolean loginSuccessful = false;

    public AuthenticationMenuGUI(Frame parent) {
        super(parent, "Authentication Menu", true); // Modal
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        showMainMenu();
    }

    private void showMainMenu() {
        JPanel panel = new JPanel(new GridLayout(3, 1));

        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");

        registerButton.addActionListener(e -> showRegisterForm());
        loginButton.addActionListener(e -> showLoginForm());
        exitButton.addActionListener(e -> dispose());

        panel.add(registerButton);
        panel.add(loginButton);
        panel.add(exitButton);

        setContentPane(panel);
    }

    private void showRegisterForm() {
        JDialog registerDialog = new JDialog(this, "Register", true);
        registerDialog.setSize(300, 250);
        registerDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        JTextField phoneField = new JTextField();
        JTextField passwordField = new JPasswordField();
        JTextField nameField = new JTextField();
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"landlord", "tenant"});

        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Role:"));
        panel.add(roleBox);

        JButton submit = new JButton("Register");
        submit.addActionListener(e -> {
            String phone = phoneField.getText();
            String password = passwordField.getText();
            String name = nameField.getText();
            String role = (String) roleBox.getSelectedItem();

            if (role.equals("landlord")) {
                User landlord = new Landlord();
                landlord.register(name, phone, password);
            } else {
                User tenant = new Tenant();
                tenant.register(name, phone, password);
            }

            registerDialog.dispose();
            JOptionPane.showMessageDialog(this, "Registration successful.");
        });

        panel.add(new JLabel());
        panel.add(submit);

        registerDialog.setContentPane(panel);
        registerDialog.setVisible(true);
    }

    private void showLoginForm() {
        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setSize(300, 250);
        loginDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        JComboBox<String> roleBox = new JComboBox<>(new String[]{"admin", "landlord", "tenant"});
        JTextField phoneField = new JTextField();
        JTextField passwordField = new JPasswordField();

        panel.add(new JLabel("Role:"));
        panel.add(roleBox);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JButton submit = new JButton("Login");
        submit.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            String phone = phoneField.getText();
            String password = passwordField.getText();

            User user;
            try {
                Class<?> clazz = Class.forName("user.model." + capitalize(role));
                user = (User) clazz.getDeclaredConstructor().newInstance();
                user.login(phone, password);
                SessionManager.setCurrentUser(user);

                if (user.getUserId() != null) {
                    loginSuccessful = true;
                    loginDialog.dispose();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(loginDialog, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(loginDialog, "Failed to load user class.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        panel.add(new JLabel());
        panel.add(submit);

        loginDialog.setContentPane(panel);
        loginDialog.setVisible(true);
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}
