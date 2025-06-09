package utils.seeder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class PropertySeeder {
    private static final String PROPERTY_CSV_PATH = "src/file/property/properties.csv";
    private static final String USER_CSV_PATH = "src/file/user/users.csv";

    private static final String[][] properties = {
            {"Sunset Apartments", "123 Main St New York NY", "2500.00", "true", "active"},
            {"Mountain View Condos", "456 Park Ave Boston MA", "3200.00", "true", "active"},
            {"Ocean Breeze Villas", "789 Beach Rd Miami FL", "4100.00", "true", "active"},
    };

    public static void seedProperties() {
        String landlordUuid = getLandlordUuid();
        if (landlordUuid == null) {
            System.err.println("Cannot seed properties without a landlord UUID.");
            return;
        }

        try {
            File file = new File(PROPERTY_CSV_PATH);
            file.getParentFile().mkdirs(); // Ensure directories exist

            boolean isNewFile = !file.exists();
            FileWriter fw = new FileWriter(file, true); // Append mode
            BufferedWriter writer = new BufferedWriter(fw);

            if (isNewFile) {
                // Write headers if file is newly created
                writer.write("propertyId,name,location,price,availability,landlordId,status,imagePath");
                writer.newLine();
            }

            // Write all properties from the properties array
            for (String[] property : properties) {
                String name = property[0];
                String location = property[1];
                String price = property[2];
                String availability = property[3];
                String status = property[4];

                String propertyId = UUID.randomUUID().toString();

                writer.write(String.join(",",
                        propertyId,
                        name,
                        location,
                        price,
                        availability,
                        landlordUuid,
                        status,
                        "Null"
                ));
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to properties CSV: " + e.getMessage());
        }
    }

    private static String getLandlordUuid() {
        String landlordUuid = null;
        Path path = Paths.get(USER_CSV_PATH);

        try {
            if (!Files.exists(path)) {
                System.err.println("Users CSV file does not exist. Please run createUsers() first.");
                return null;
            }

            List<String> lines = Files.readAllLines(path);
            if (lines.size() <= 1) {
                System.err.println("Users CSV file is empty or contains only headers.");
                return null;
            }

            // Skip the header line
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                if (parts.length >= 5 && "landlord".equals(parts[4])) {
                    landlordUuid = parts[0];
                    break;
                }
            }

            if (landlordUuid == null) {
                System.err.println("No user with landlord role found in the CSV file.");
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return landlordUuid;
    }
}
