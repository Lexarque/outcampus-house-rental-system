package user.model;

import booking.model.Booking;
import property.model.Property;
import ui.helper.ImageUploader;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Landlord extends User {
    private List<Property> properties = new ArrayList<>();
    private List<String> propertyIds = new ArrayList<>();
    private List<Booking> pendingPropertyBookings = new ArrayList<>();

    private static final String PROPERTY_CSV_PATH = "src/file/property/properties.csv";
    private static final String BOOKING_CSV_PATH = "src/file/booking/bookings.csv";
    private static final String PROPERTY_IMAGE_PATH = "src/file/property/images/";

    public Landlord() {
        super();
    }

    public Landlord(User user) {
        super(user.getUserId(), user.getName(), user.getPhone(), user.getPassword(), user.getRole());
        setProperties();
        setPendingPropertyBookings();
    }

    // Authentication and Registration

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
                    this.getClass().getSimpleName().toLowerCase()
            ));
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
                    String role = parts[4];

                    if ((phone.equals(inputPhone) && password.equals(inputPassword)) && role.equals(this.getClass().getSimpleName().toLowerCase())) {

                        // Set authenticated user's details'
                        setUserId(parts[0]);
                        setName(name);
                        setPhone(phone);
                        setPassword(password);
                        setRole(parts[4]);

                        setProperties();
                        setPendingPropertyBookings();

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

    // Property Management

    public void viewOwnListings(boolean detailed) {
        if (properties == null || properties.isEmpty()) {
            System.out.println("No properties found for this landlord.");
        } else {
            for (int i = 0; i < properties.size(); i++) {
                System.out.println("Property " + (i + 1) + ":");
                System.out.println("-----------------------------------");
                if (detailed) {
                    properties.get(i).printDetailedProperties();
                }
                else {
                    properties.get(i).printBasicProperties();
                }
                System.out.println("-----------------------------------");
            }
        }
    }

    // Updated postListing method that accepts a parent dialog
    public void postListing(String propertyName, String location, double price, boolean availability, Dialog parent) {
        Property property = new Property();
        property.setPropertyId(UUID.randomUUID().toString());
        property.setLandlordId(getUserId());
        property.setStatus("active");

        property.setName(propertyName);
        property.setLocation(location);
        property.setPrice(price);
        property.setAvailability(availability);

        // Use callback approach with parent dialog
        final ImageUploader[] uploaderRef = new ImageUploader[1];

        uploaderRef[0] = new ImageUploader(parent, PROPERTY_IMAGE_PATH, () -> {
            property.setImagePath(uploaderRef[0].getImagePath());
            property.saveToCsv();
            properties.add(property);
            System.out.println("Property '" + propertyName + "' has been posted successfully.");
        });

        uploaderRef[0].setVisible(true);
    }

    // Keep the old method for backward compatibility
    public void postListing(String propertyName, String location, double price, boolean availability) {
        postListing(propertyName, location, price, availability, null);
    }

    public void editListing(String propertyId, String propertyName, String location, double price, boolean availability, Dialog parent) {
        File file = new File(PROPERTY_CSV_PATH);
        if (!file.exists()) {
            System.out.println("Property file does not exist.");
            return;
        }

        List<String> updatedLines = new ArrayList<>();
        boolean propertyFound = false;
        String oldImagePath = null;
        Property foundProperty = null;

        for (Property prop : properties) {
            if (prop.getPropertyId().equals(propertyId)) {
                foundProperty = prop;
                oldImagePath = prop.getImagePath();
                break;
            }
        }

        if (foundProperty == null) {
            System.out.println("Property with id \"" + propertyId + "\" not found in memory.");
            return;
        }

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
                if (parts.length >= 8 && parts[0].equals(propertyId)) {
                    parts[1] = propertyName;
                    parts[2] = location;
                    parts[3] = String.valueOf(price);
                    parts[4] = String.valueOf(availability);

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
            System.out.println("Property with id \"" + propertyId + "\" not found in CSV file.");
            return;
        }

        // Create a copy of the property with updated values for the callback
        final Property propertyToUpdate = foundProperty;
        final String finalOldImagePath = oldImagePath;
        final List<String> finalUpdatedLines = new ArrayList<>(updatedLines);

        final ImageUploader[] uploaderRef = new ImageUploader[1];

        uploaderRef[0] = new ImageUploader(parent, PROPERTY_IMAGE_PATH, () -> {
            String newImagePath = uploaderRef[0].getImagePath();

            if (newImagePath != null && !newImagePath.isEmpty()) {
                // Update the property object in memory
                propertyToUpdate.setName(propertyName);
                propertyToUpdate.setLocation(location);
                propertyToUpdate.setPrice(price);
                propertyToUpdate.setAvailability(availability);
                propertyToUpdate.setImagePath(newImagePath);

                // Update the CSV lines with the new image path
                for (int i = 0; i < finalUpdatedLines.size(); i++) {
                    String line = finalUpdatedLines.get(i);
                    String[] parts = line.split(",");
                    if (parts.length >= 8 && parts[0].equals(propertyId)) {
                        parts[7] = newImagePath; // Update image path
                        finalUpdatedLines.set(i, String.join(",", parts));
                        break;
                    }
                }

                // Write the updated lines back to the CSV file
                try (FileWriter writer = new FileWriter(file, false)) {
                    for (String updatedLine : finalUpdatedLines) {
                        writer.write(updatedLine + System.lineSeparator());
                    }

                    // Delete the old image file if it exists and is different from the new one
                    if (finalOldImagePath != null && !finalOldImagePath.isEmpty() &&
                            !finalOldImagePath.equals(newImagePath)) {
                        File oldImageFile = new File(finalOldImagePath);
                        if (oldImageFile.exists()) {
                            boolean deleted = oldImageFile.delete();
                            if (!deleted) {
                                System.err.println("Warning: Failed to delete old image file: " + finalOldImagePath);
                            }
                        }
                    }

                    System.out.println("Successfully updated property '" + propertyName + "' with new image.");

                } catch (IOException e) {
                    System.err.println("Error writing to properties file: " + e.getMessage());
                }
            } else {
                System.out.println("No image was uploaded. Property details updated without changing image.");

                // Still update the basic property details even if no new image was uploaded
                propertyToUpdate.setName(propertyName);
                propertyToUpdate.setLocation(location);
                propertyToUpdate.setPrice(price);
                propertyToUpdate.setAvailability(availability);

                // Write the updated lines (without image path change) back to the CSV file
                try (FileWriter writer = new FileWriter(file, false)) {
                    for (String updatedLine : finalUpdatedLines) {
                        writer.write(updatedLine + System.lineSeparator());
                    }
                    System.out.println("Successfully updated property details for: " + propertyName);
                } catch (IOException e) {
                    System.err.println("Error writing to properties file: " + e.getMessage());
                }
            }
        });

        uploaderRef[0].setVisible(true);
    }

    // Keep the old method for backward compatibility
    public void editListing(String propertyId, String propertyName, String location, double price, boolean availability) {
        editListing(propertyId, propertyName, location, price, availability, null);
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

            boolean imageDeleted;

            try {
                // Remove from CSV file
                propertyToDelete.removeFromCsv();

                // Remove image file
                String imagePath = propertyToDelete.getImagePath();
                if (imagePath != null && !imagePath.isEmpty()) {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        imageDeleted = imageFile.delete(); // attempt to delete file
                        if (!imageDeleted) {
                            System.err.println("Warning: Failed to delete image file at: " + imagePath);
                        }
                    } else {
                        System.out.println("Image file not found at: " + imagePath);
                    }
                }

                System.out.println("Property with ID " + propertyId + " has been deleted from both memory and CSV.");
            } catch (Exception e) {
                System.err.println("Error deleting property from CSV: " + e.getMessage());
                properties.add(propertyToDelete);
            }
        } else {
            System.out.println("Property with ID " + propertyId + " not found.");
        }
    }

    private void setProperties() {
        File file = new File(PROPERTY_CSV_PATH);
        if (!file.exists()) {
            System.out.println("Properties file not found.");
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
                        propertyIds.add(parts[0]);
                        properties.add(new Property(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), Boolean.parseBoolean(parts[4]), parts[5], parts[6], parts[7]));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from properties CSV: " + e.getMessage());
        }
    }

    // Booking Management

    public void viewPendingBookings() {
        if (pendingPropertyBookings == null || pendingPropertyBookings.isEmpty()) {
            System.out.println("No bookings found for this landlord.");
        } else {
            System.out.println("-----------------------------------");
            for (int i = 0; i < pendingPropertyBookings.size(); i++) {
                System.out.println("Booking " + (i + 1) + ":");
                pendingPropertyBookings.get(i).printDetailedBooking();
                System.out.println("-----------------------------------");
            }
        }
    }

    private void setPendingPropertyBookings() {
        File file = new File(BOOKING_CSV_PATH);
        if (!file.exists()) {
            System.out.println("Bookings file not found.");
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
                    String propertyId = parts[2];
                    if (propertyIds.contains(propertyId) && parts[3].equals("pending")) {
                        pendingPropertyBookings.add(new Booking(parts[0], parts[1], propertyId, parts[3]));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from properties CSV: " + e.getMessage());
        }
    }

    public void updateBookingStatus(String id, String newValue) {
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKING_CSV_PATH))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.err.println("CSV file is empty");
                return;
            }

            lines.add(headerLine);
            String[] headers = headerLine.split(",");

            // Find the index of ID column and target column
            int idColumnIndex = findColumnIndex(headers, "requestId");
            int targetColumnIndex = findColumnIndex(headers, "status");

            if (idColumnIndex == -1) {
                System.err.println("ID column not found in CSV");
                return;
            }

            if (targetColumnIndex == -1) {
                System.err.println("Column 'status' not found in CSV");
                return;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",", -1); // -1 to preserve empty trailing fields

                if (columns.length > idColumnIndex && columns[idColumnIndex].trim().equals(id)) {
                    // Found the row to update
                    if (columns.length > targetColumnIndex) {
                        pendingPropertyBookings.removeIf(booking -> booking.getRequestId().equals(id));
                        columns[targetColumnIndex] = newValue;
                        lines.add(String.join(",", columns));
                        updated = true;
                    } else {
                        System.err.println("Row structure is invalid");
                        return;
                    }
                } else {
                    lines.add(line);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return;
        }

        if (!updated) {
            System.err.println("Booking ID " + id + " not found");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKING_CSV_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    private static int findColumnIndex(String[] headers, String columnName) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public List<Booking> getPendingPropertyBookings() {
        return pendingPropertyBookings;
    }
}
