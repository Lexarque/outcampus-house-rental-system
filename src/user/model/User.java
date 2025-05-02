package user.model;

import java.io.*;
import java.util.UUID;

public class User {
    private String userId;
    private String name;
    private String phone;
    private String password;
    private String role;
    private String status = "pending";

    private static final String CSV_PATH = "src/file/user/users.csv";

    public User(String name, String phone, String password, String role) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    public void register() {
        try {
            File file = new File(CSV_PATH);
            file.getParentFile().mkdirs(); // Ensure directories exist

            boolean isNewFile = !file.exists();
            FileWriter fw = new FileWriter(file, true); // Append mode
            BufferedWriter writer = new BufferedWriter(fw);

            if (isNewFile) {
                // Write headers if file is newly created
                writer.write("userId,name,phone,password,role,status");
                writer.newLine();
            }

            // Write user data
            writer.write(String.join(",", userId, name, phone, password, role, status));
            writer.newLine();
            writer.close();

            System.out.println(name + " has registered.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    public void login(String inputPhone, String inputPassword) {
        File file = new File(CSV_PATH);
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

                    if (phone.equals(inputPhone) && password.equals(inputPassword)) {

                        // Set authenticated user's details'
                        setUserId(parts[0]);
                        setName(name);
                        setPhone(phone);
                        setPassword(password);
                        setRole(parts[4]);
                        setStatus(parts[5]);

                        System.out.println("Login successful. Welcome, " + name + "!");
                        return;
                    }
                }
            }

            System.out.println("Login failed. Incorrect phone number or password.");
        } catch (IOException e) {
            System.err.println("Error reading from CSV during login: " + e.getMessage());
        }
    }

    public void logout() {
        System.out.println(name + " has logged out.");
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCSVPath() {
        return CSV_PATH;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
