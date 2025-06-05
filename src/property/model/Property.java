package property.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    public void saveToCsv() {
        try {
            File file = new File(CSV_PATH);
            FileWriter fw = new FileWriter(file, true); // Append mode
            BufferedWriter writer = new BufferedWriter(fw);

            // Write user data
            writer.write(String.join(",", propertyId, name, location, String.valueOf(price), String.valueOf(availability), landlordId, imagePath, status));
            writer.newLine();
            writer.close();

            System.out.println("Property has been saved successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    public void printLandlordProperties() {
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

    // Operations
    public void createNewProperty(Property property) {
        System.out.println("Successfully created new property: ");
        System.out.println(property);
    }

    public void viewDetails() {
        System.out.println("property.model.Property ID: " + propertyId);
        System.out.println("Title: " + name);
        System.out.println("Location: " + location);
        System.out.println("Price: " + price);
        System.out.println("Available: " + (availability ? "Yes" : "No"));
        System.out.println("Landlord ID: " + landlordId);
    }

    @Override
    public String toString() {
        return "Property Id: " + propertyId + ", Title: " + name + ", Location: " + location + ", Price: " + price
                + ", Available: " + (availability ? "Yes" : "No") + ", Landlord Id: " + landlordId;
    }
}
