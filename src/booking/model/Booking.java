package booking.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Booking {
    private String requestId;
    private String tenantId;
    private String propertyId;
    private String status = "pending";

    private static final String CSV_PATH = "src/file/booking/bookings.csv";

    public Booking(String requestId, String tenantId, String propertyId, String status) {
        this.requestId = requestId;
        this.tenantId = tenantId;
        this.propertyId = propertyId;
        this.status = status;
    }

    // Operations
    public void saveToCsv() {
        try {
            File file = new File(CSV_PATH);
            FileWriter fw = new FileWriter(file, true); // Append mode
            BufferedWriter writer = new BufferedWriter(fw);

            // Write booking data
            writer.write(String.join(",", UUID.randomUUID().toString(), tenantId, propertyId, "pending"));
            writer.newLine();
            writer.close();

            System.out.println("Booking has been made.");
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

    public String viewStatus() {
        return status;
    }

    public void updateStatus(String status) {
        this.status = status;
        System.out.println("booking.model.Booking status updated to: " + status);
    }

    public void printDetailedBooking() {
        System.out.println("Booking ID: " + requestId);
        System.out.println("Tenant ID: " + tenantId);
        System.out.println("Property ID: " + propertyId);
        System.out.println("Status: " + status);
    }

    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
