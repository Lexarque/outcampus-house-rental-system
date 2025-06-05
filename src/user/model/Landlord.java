package user.model;

import property.model.Property;
import ui.helper.ImageUploader;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class Landlord extends User {
    private List<Property> properties = new ArrayList<>();

    private static final String PROPERTY_CSV_PATH = "src/file/property/properties.csv";
    private static final String PROPERTY_IMAGE_PATH = "src/file/property/images/";

    public Landlord() {
        super();
    }

    public Landlord(User user) {
        super(user.getName(), user.getPhone(), user.getPassword(), user.getRole());
        setProperties();
    }

    public void postListing(String propertyName, String location, double price, boolean availability) {
        Property property = new Property();
        property.setPropertyId(UUID.randomUUID().toString());
        property.setLandlordId(getUserId());
        property.setStatus("Active");

        property.setName(propertyName);
        property.setLocation(location);
        property.setPrice(price);
        property.setAvailability(availability);

        // Call the image uploader and wait for it to close
        final ImageUploader[] uploaderHolder = new ImageUploader[1];
        final CountDownLatch latch = new CountDownLatch(1);

        try {
            SwingUtilities.invokeAndWait(() -> {
                uploaderHolder[0] = new ImageUploader(PROPERTY_IMAGE_PATH);

                // Add window listener to detect when the uploader is closed
                uploaderHolder[0].addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        latch.countDown(); // Signal that the window has been closed
                    }
                });

                uploaderHolder[0].setVisible(true);
            });

            // Wait for the uploader window to be closed
            latch.await();

        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageUploader uploader = uploaderHolder[0];
        property.setImagePath(uploader.getImagePath());

        // Save the property to CSV
        property.saveToCsv();

        // Add to the landlord's properties list
        properties.add(property);

        System.out.println("Property '" + propertyName + "' has been posted successfully.");
    }

    public void editListing(String propertyId, String propertyName, String location, double price, boolean availability) {
        deleteListing(propertyId);
        postListing(propertyName, location, price, availability);
    }

    public void deleteListing(String propertyId) {
        boolean found = false;
        Property propertyToDelete = null;

        // Find the property to delete
        for (Property property : properties) {
            if (property.getPropertyId().equals(propertyId) && property.getLandlordId().equals(getUserId())) {
                propertyToDelete = property;
                found = true;
                break;
            }
        }

        if (found) {
            // Remove from ArrayList
            properties.remove(propertyToDelete);

            // Remove from CSV file
            try {
                removePropertyFromCsv(propertyId);
                System.out.println("Property with ID " + propertyId + " has been deleted from both memory and CSV file.");
            } catch (Exception e) {
                System.err.println("Error deleting property from CSV: " + e.getMessage());
                e.printStackTrace();
                // Add property back to the list if deletion fails
                 properties.add(propertyToDelete);
            }
        } else {
            System.out.println("Property with ID " + propertyId + " not found.");
        }
    }

    private void removePropertyFromCsv(String propertyId) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean headerProcessed = false;

        // Read all lines from the CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(PROPERTY_CSV_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!headerProcessed) {
                    // Keep the header line
                    lines.add(line);
                    headerProcessed = true;
                } else {
                    // Check if this line contains the property to delete
                    String[] columns = line.split(",");
                    if (columns.length > 0 && !columns[0].trim().equals(propertyId)) {
                        lines.add(line);
                    }
                    // If it matches the propertyId, we skip adding it (basically deleting it :D)
                }
            }
        }

        // Write the filtered lines back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROPERTY_CSV_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public void viewOwnListings() {
        System.out.println("-----------------------------------");
        for (Property property : properties) {
            property.printLandlordProperties();
            System.out.println("-----------------------------------");
        }
    }

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

                        // Set authenticated user's details'
                        setUserId(parts[0]);
                        setName(name);
                        setPhone(phone);
                        setPassword(password);
                        setRole(parts[4]);
                        setStatus(parts[5]);

                        //Set properties when login is successful
                        setProperties();
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

    private void setProperties() {
        File file = new File(PROPERTY_CSV_PATH);
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
                if (parts.length >= 7) {
                    String propertyLandlordId = parts[5];
                    String currentUserId = getUserId();

                    if (propertyLandlordId.equals(currentUserId)) {
                        properties.add(new Property(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), Boolean.parseBoolean(parts[4]), parts[5], parts[6], parts[7]));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from properties CSV: " + e.getMessage());
        }
    }
}
