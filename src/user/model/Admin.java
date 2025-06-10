package user.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin extends User {
    public Admin() {
        super();
    }

    public Admin(User user) {
        super(user.getUserId(), user.getName(), user.getPhone(), user.getPassword(), user.getRole());
    }

    // empty implementation for abstract method
    public void register(String name, String phone, String password) {
    }

    public void login(String inputPhone, String inputPassword) {
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
            boolean isFirstLine = true;

            System.out.println("Registered Users:");
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String status = (parts.length >= 6) ? parts[5] : "N/A";
                    System.out.println("ID: " + parts[0] + ", Name: " + parts[1] +
                            ", Phone: " + parts[2] + ", Role: " + parts[4]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
    }

    public void readAllProperties() {
        File propertyFile = new File("src/file/property/properties.csv");
        File userFile = new File("src/file/user/users.csv");

        if (!propertyFile.exists()) {
            System.out.println("No property listings found.");
            return;
        }

        System.out.println("Property Listings:");
        try (
                BufferedReader propertyReader = new BufferedReader(new FileReader(propertyFile));
                BufferedReader userReader = new BufferedReader(new FileReader(userFile))) {
            // Step 1: Load landlord IDs and names from users.csv
            Map<String, String> landlordMap = new HashMap<>();
            String userLine;
            boolean isFirstUserLine = true;
            while ((userLine = userReader.readLine()) != null) {
                if (isFirstUserLine) {
                    isFirstUserLine = false;
                    continue;
                }
                String[] userParts = userLine.split(",");
                if (userParts.length >= 5 && userParts[4].equalsIgnoreCase("landlord")) {
                    String id = userParts[0];
                    String name = userParts[1];
                    landlordMap.put(id, name);
                }
            }

            // Step 2: Read property listings
            String propLine;
            boolean isFirstPropLine = true;
            while ((propLine = propertyReader.readLine()) != null) {
                if (isFirstPropLine) {
                    isFirstPropLine = false;
                    continue;
                }

                String[] parts = propLine.split(",");
                if (parts.length >= 8) {
                    String name = parts[1];
                    String location = parts[2];
                    String price = parts[3];
                    String availability = parts[4];
                    String landlordId = parts[5];
                    String status = parts[6];

                    // Lookup landlord name
                    String landlordName = landlordMap.getOrDefault(landlordId, "Unknown");

                    System.out.println("Name: " + name +
                            ", Location: " + location +
                            ", Price: " + price +
                            ", Availability: " + availability +
                            ", Landlord: " + landlordName +
                            ", Status: " + status);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
        }
    }

    public void removeUserByName(String name) {
        File file = new File(super.getCSVPath());
        if (!file.exists()) {
            System.out.println("User file does not exist.");
            return;
        }

        List<String> updatedLines = new ArrayList<>();
        boolean userFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    updatedLines.add(line);
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equalsIgnoreCase(name)) {
                    userFound = true;
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
            return;
        }

        if (!userFound) {
            System.out.println("User with name \"" + name + "\" not found.");
            return;
        }

        try (FileWriter writer = new FileWriter(file, false)) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + System.lineSeparator());
            }
            System.out.println("Successfully removed user with name: " + name);
        } catch (IOException e) {
            System.err.println("Error writing to users file: " + e.getMessage());
        }
    }

    public void deactivateProperty(String propertyName) {
        File file = new File("src/file/property/properties.csv");
        if (!file.exists()) {
            System.out.println("Property file does not exist.");
            return;
        }

        List<String> updatedLines = new ArrayList<>();
        boolean propertyFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    updatedLines.add(line); // keep header
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[1].equalsIgnoreCase(propertyName)) {
                    parts[6] = "inactive"; // set status to inactive
                    line = String.join(",", parts);
                    propertyFound = true;
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading properties file: " + e.getMessage());
            return;
        }

        if (!propertyFound) {
            System.out.println("Property with name \"" + propertyName + "\" not found.");
            return;
        }

        try (FileWriter writer = new FileWriter(file, false)) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + System.lineSeparator());
            }
            System.out.println("Successfully deactivated property with name: " + propertyName);
        } catch (IOException e) {
            System.err.println("Error writing to properties file: " + e.getMessage());
        }
    }

}
