package property.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Property {
    private String propertyId;
    private String name;
    private String location;
    private double price;
    private boolean availability;
    private String landlordId;
    private String imagePath;
    private String status;

    private final String CSV_PATH = "src/file/property/properties.csv";

    public Property() {}

    public Property(String propertyId, String name, String location, double price, boolean availability,
                    String landlordId, String imagePath, String status) {
        this.propertyId = propertyId;
        this.name = name;
        this.location = location;
        this.price = price;
        this.availability = availability;
        this.landlordId = landlordId;
        this.imagePath = imagePath;
        this.status = status;
    }

    public List<Property> getPropertiesFromCsv(boolean isOnlyActive) {
        List<Property> properties = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_PATH))) {
            String line;
            boolean headerProcessed = false;

            int counter = 1;
            while ((line = reader.readLine()) != null) {
                if (!headerProcessed) {
                    headerProcessed = true;
                    continue;
                }

                String[] columns = line.split(",");

                if (columns.length > 0 && (!isOnlyActive || columns[6].trim().equalsIgnoreCase("active"))) {

                    // Add property to the list
                    properties.add(new Property(
                            columns[0].trim(),
                            columns[1].trim(),
                            columns[2].trim(),
                            Double.parseDouble(columns[3].trim()),
                            Boolean.parseBoolean(columns[4].trim()),
                            columns[5].trim(),
                            columns[6].trim(),
                            columns[7].trim()
                    ));
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading from CSV: " + e.getMessage());
        }

        return properties;
    }

    public void saveToCsv() {
        try {
            File file = new File(CSV_PATH);
            FileWriter fw = new FileWriter(file, true); // Append mode
            BufferedWriter writer = new BufferedWriter(fw);

            // Write user data
            writer.write(String.join(",", propertyId, name, location, String.valueOf(price), String.valueOf(availability), landlordId, status, imagePath));
            writer.newLine();
            writer.close();

            System.out.println("Property has been saved successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    public void removeFromCsv() throws IOException {
        List<String> lines = new ArrayList<>();
        boolean headerProcessed = false;

        // Read all lines from the CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_PATH))) {
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public void printBasicProperties() {
        System.out.println("Property ID: " + propertyId);
        System.out.println("Name: " + name);
        System.out.println("Location: " + location);
        System.out.println("Price: " + price);
        System.out.println("Available: " + (availability ? "Yes" : "No"));
    }

    public void printDetailedProperties() {
        System.out.println("Landlord ID: " + landlordId);
        System.out.println("Property ID: " + propertyId);
        System.out.println("Name: " + name);
        System.out.println("Location: " + location);
        System.out.println("Price: " + price);
        System.out.println("Availability: " + (availability ? "Yes" : "No"));
        System.out.println("Image Path: " + imagePath);
        System.out.println("Status: " + status);
    }

    // Getters and Setters
    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(String landlordId) {
        this.landlordId = landlordId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean getAvailability() {
        return availability;
    }

    // Operations
    @Override
    public String toString() {
        return "Property Id: " + propertyId + ", Title: " + name + ", Location: " + location + ", Price: " + price
                + ", Available: " + (availability ? "Yes" : "No") + ", Landlord Id: " + landlordId;
    }
}
