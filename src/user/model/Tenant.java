package user.model;

import booking.model.Booking;
import property.model.Property;

import java.awt.print.Book;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tenant extends  User {
    private List<Booking> bookings = new ArrayList<>();

    private static final String BOOKING_CSV_PATH = "src/file/booking/bookings.csv";

    public Tenant() {
        super();
    }

    public Tenant(User user) {
        super(user.getName(), user.getPhone(), user.getPassword(), user.getRole());
    }

    // Operations

//    public void searchPropertiesByName(String query) {
//        Property dummyProperty = new Property("1", "1", "1", 1.0, true, "1");
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(dummyProperty);
//
//        for (Property property : properties) {
//            System.out.println(property);
//        }
//    }

    public void register(String name, String phone, String password) {
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
            writer.write(String.join(",",
                    UUID.randomUUID().toString(),
                    name,
                    phone,
                    password,
                    this.getClass().getSimpleName().toLowerCase(),
                    "Active" // default status
            ));
            writer.newLine();
            writer.close();

            System.out.println(getName() + " has registered.");
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
                    String role = parts[4];

                    if ((phone.equals(inputPhone) && password.equals(inputPassword)) && role.equals(this.getClass().getSimpleName().toLowerCase())) {

                        // Set authenticated user's details
                        setUserId(parts[0]);
                        setName(name);
                        setPhone(phone);
                        setPassword(password);
                        setRole(parts[4]);

                        System.out.println("Login successful. Welcome, " + name + "!");

                        //Set bookings when login is successful
                        setBookings();
                        return;
                    }
                }
            }

            System.out.println("Login failed. Incorrect phone number or password.");
        } catch (IOException e) {
            System.err.println("Error reading from CSV during login: " + e.getMessage());
        }
    }

    public void sendBookingRequest(String propertyId) {
        Booking booking = new Booking(this.getUserId(), propertyId);
        bookings.add(booking);

        //Write to booking CSV
        booking.saveToCsv();

        System.out.println("Booking request sent for property ID: " + propertyId);
    }

    public void viewOwnBookings() {
        if (bookings == null || bookings.isEmpty()) {
            System.out.println("No bookings found for this tenant.");
        } else {
            System.out.println("-----------------------------------");
            for (Booking booking : bookings) {
                booking.printDetailedBooking();
                System.out.println("-----------------------------------");
            }
        }
    }

    private void setBookings() {
        File file = new File(BOOKING_CSV_PATH);
        if (!file.exists()) {
            System.out.println("Properties not found.");
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
                if (parts.length >= 4) {
                    String tenantId = parts[1];
                    String currentUserId = getUserId();

                    if (tenantId.equals(currentUserId)) {
                        bookings.add(new Booking(parts[0], parts[1], parts[2], parts[3]));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from booking CSV: " + e.getMessage());
        }
    }
}
