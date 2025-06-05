package user.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Admin extends User {
    public Admin() {
        super();
    }

    public Admin(User user) {
        super(user.getName(), user.getPhone(), user.getPassword(), user.getRole());
    }

    // empty implementation for abstract method
    public void register(String name, String phone, String password) {}

    public void login (String inputPhone, String inputPassword) {
        File file = new File(super.getCSVPath());
        if (!file.exists()) {
            System.out.println("No users registered. Please register first.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // skip header
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String phone = parts[2];
                    String password = parts[3];
                    String name = parts[1];
                    String role = parts[4];

                    if (phone.equals(inputPhone) && password.equals(inputPassword)) {
                        System.out.println("Welcome " + name + " (" + role + ")");
                        return; // successful login
                    }
                }
            }
            System.out.println("Invalid phone or password.");
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
    }

    public void readAllUsers() {
        File file = new File(super.getCSVPath());
        if (!file.exists()) {
            System.out.println("No users registered yet.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true; // variable to skip header

            System.out.println("Registered Users:");
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // skip header
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    System.out.println("ID: " + parts[0] + ", Name: " + parts[1] +
                            ", Phone: " + parts[2] + ", Role: " + parts[4]+ ", Status: " + parts[5]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
    }

    public void removeUser(String userId) {
        System.out.println("Successfully removed user with ID: " + userId);
    }

    public void deactivateProperty(String propertyId) {
        System.out.println("Successfully deactivated property with ID: " + propertyId);
    }

    public void changeUserStatus(String userId, String status) {
        System.out.println("Successfully changed status of user with ID: " + userId + " to: " + status);
    }
}
