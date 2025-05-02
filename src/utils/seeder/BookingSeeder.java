package utils.seeder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class BookingSeeder {
    private static final String PROPERTY_CSV_PATH = "src/file/property/properties.csv";
    private static final String USER_CSV_PATH = "src/file/user/users.csv";
    private static final String BOOKING_CSV_PATH = "src/file/booking/bookings.csv";

    public static void seedBookings() {
        List<String> tenantIds = getTenantIds();
        if (tenantIds.isEmpty()) {
            System.err.println("Cannot seed bookings without tenant users. Please run createUsers() first.");
            return;
        }

        List<String> propertyIds = getPropertyIds();
        if (propertyIds.isEmpty()) {
            System.err.println("Cannot seed bookings without properties. Please run seedProperties() first.");
            return;
        }

        try {
            File file = new File(BOOKING_CSV_PATH);
            file.getParentFile().mkdirs(); // Ensure directories exist

            boolean isNewFile = !file.exists();
            FileWriter fw = new FileWriter(file, true); // Append mode
            BufferedWriter writer = new BufferedWriter(fw);

            if (isNewFile) {
                // Write headers if file is newly created
                writer.write("requestId,tenantId,propertyId,status");
                writer.newLine();
            }

            Random random = new Random();
            for (int i = 0; i < 5; i++) {
                String requestId = UUID.randomUUID().toString();

                String tenantId = tenantIds.get(random.nextInt(tenantIds.size()));
                String propertyId = propertyIds.get(random.nextInt(propertyIds.size()));

                String status = "pending";
                int statusRoll = random.nextInt(10);
                if (statusRoll < 5) {
                    status = "pending";
                } else if (statusRoll < 8) {
                    status = "accepted";
                } else if (statusRoll < 9) {
                    status = "rejected";
                }

                writer.write(String.join(",", requestId, tenantId, propertyId, status));
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to bookings CSV: " + e.getMessage());
        }
    }

    private static List<String> getTenantIds() {
        List<String> userIds = new ArrayList<>();
        Path path = Paths.get(USER_CSV_PATH);

        try {
            if (!Files.exists(path)) {
                System.err.println("Users CSV file does not exist.");
                return userIds;
            }

            java.util.List<String> lines = Files.readAllLines(path);
            if (lines.size() <= 1) {
                System.err.println("Users CSV file is empty or contains only headers.");
                return userIds;
            }

            // Skip the header line
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                if (parts.length >= 5 && "TENANT".equals(parts[4])) {
                    userIds.add(parts[0]);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return userIds;
    }

    private static List<String> getPropertyIds() {
        List<String> propertyIds = new ArrayList<>();
        Path path = Paths.get(PROPERTY_CSV_PATH);

        try {
            if (!Files.exists(path)) {
                System.err.println("Properties CSV file does not exist.");
                return propertyIds;
            }

            java.util.List<String> lines = Files.readAllLines(path);
            if (lines.size() <= 1) {
                System.err.println("Properties CSV file is empty or contains only headers.");
                return propertyIds;
            }

            // Skip the header line
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                if (parts.length >= 5) {
                    propertyIds.add(parts[0]);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return propertyIds;
    }
}
