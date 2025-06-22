package user.view.admin;

import user.model.Admin;
import utils.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class AdminMenuGUI extends JFrame {
    private final Admin admin;

    public AdminMenuGUI() {
        this.admin = SessionManager.setAdminData();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Admin Panel");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton viewUsersBtn = new JButton("View All Users");
        JButton viewPropertiesBtn = new JButton("View All Properties");
        JButton removeUserBtn = new JButton("Remove a User");
        JButton deactivatePropertyBtn = new JButton("Deactivate a Property");
        JButton exitBtn = new JButton("Exit");

        viewUsersBtn.addActionListener(e -> showAllUsers());
        viewPropertiesBtn.addActionListener(e -> showAllProperties());
        removeUserBtn.addActionListener(e -> removeUser());
        deactivatePropertyBtn.addActionListener(e -> deactivateProperty());
        exitBtn.addActionListener(e -> dispose());

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(viewUsersBtn);
        panel.add(viewPropertiesBtn);
        panel.add(removeUserBtn);
        panel.add(deactivatePropertyBtn);
        panel.add(exitBtn);

        add(panel);
        setVisible(true);
    }

    private void showAllUsers() {
        StringBuilder result = new StringBuilder("Registered Users:\n");

        File file = new File("src/file/user/users.csv");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "No users registered yet.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    result.append("ID: ").append(parts[0])
                            .append(", Name: ").append(parts[1])
                            .append(", Phone: ").append(parts[2])
                            .append(", Role: ").append(parts[4])
                            .append("\n");
                }
            }
            showTextArea("All Users", result.toString());
        } catch (IOException e) {
            showError("Error reading users file: " + e.getMessage());
        }
    }

    private void showAllProperties() {
        StringBuilder result = new StringBuilder("Property Listings:\n");

        File propertyFile = new File("src/file/property/properties.csv");
        File userFile = new File("src/file/user/users.csv");

        if (!propertyFile.exists()) {
            JOptionPane.showMessageDialog(this, "No property listings found.");
            return;
        }

        try (
                BufferedReader propReader = new BufferedReader(new FileReader(propertyFile));
                BufferedReader userReader = new BufferedReader(new FileReader(userFile))) {
            // Map landlord ID to name
            java.util.Map<String, String> landlordMap = new java.util.HashMap<>();
            String line;
            boolean isFirst = true;
            while ((line = userReader.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[4].equalsIgnoreCase("landlord")) {
                    landlordMap.put(parts[0], parts[1]);
                }
            }

            isFirst = true;
            while ((line = propReader.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String landlord = landlordMap.getOrDefault(parts[5], "Unknown");
                    result.append("Name: ").append(parts[1])
                            .append(", Location: ").append(parts[2])
                            .append(", Price: ").append(parts[3])
                            .append(", Availability: ").append(parts[4])
                            .append(", Landlord: ").append(landlord)
                            .append(", Status: ").append(parts[6])
                            .append("\n");
                }
            }

            showTextArea("All Properties", result.toString());
        } catch (IOException e) {
            showError("Error reading files: " + e.getMessage());
        }
    }

    private void removeUser() {
        showAllUsers();
        String name = JOptionPane.showInputDialog(this, "Enter the user name to remove:");
        if (name == null || name.trim().isEmpty())
            return;

        File file = new File("src/file/user/users.csv");
        java.util.List<String> updatedLines = new java.util.ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) {
                    updatedLines.add(line);
                    isFirst = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equalsIgnoreCase(name.trim())) {
                    found = true;
                    continue;
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            showError("Error reading users file: " + e.getMessage());
            return;
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (FileWriter writer = new FileWriter(file, false)) {
            for (String line : updatedLines) {
                writer.write(line + System.lineSeparator());
            }
            JOptionPane.showMessageDialog(this, "User '" + name + "' removed.");
        } catch (IOException e) {
            showError("Error writing users file: " + e.getMessage());
        }
    }

    private void deactivateProperty() {
        showAllProperties();
        String name = JOptionPane.showInputDialog(this, "Enter the property name to deactivate:");
        if (name == null || name.trim().isEmpty())
            return;

        File file = new File("src/file/property/properties.csv");
        java.util.List<String> updatedLines = new java.util.ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) {
                    updatedLines.add(line);
                    isFirst = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[1].equalsIgnoreCase(name.trim())) {
                    parts[6] = "inactive";
                    line = String.join(",", parts);
                    found = true;
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            showError("Error reading properties file: " + e.getMessage());
            return;
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "Property not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (FileWriter writer = new FileWriter(file, false)) {
            for (String line : updatedLines) {
                writer.write(line + System.lineSeparator());
            }
            JOptionPane.showMessageDialog(this, "Property '" + name + "' deactivated.");
        } catch (IOException e) {
            showError("Error writing properties file: " + e.getMessage());
        }
    }

    private void showTextArea(String title, String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(380, 250));
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
