package utils.seeder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class PaymentSeeder {
    private static final String PROPERTY_CSV_PATH = "src/file/property/properties.csv";
    private static final String BOOKING_CSV_PATH = "src/file/booking/bookings.csv";
    private static final String PAYMENT_CSV_PATH = "src/file/payment/payments.csv";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static void seedPayments() {
        List<String[]> acceptedBookings = getBookingsByStatus("accepted");
        if (acceptedBookings.isEmpty()) {
            System.err.println("Cannot seed payments without accepted bookings. Please run seedBookings() first.");
            return;
        }

        // Get property information to determine payment amounts
        Map<String, Double> propertyPrices = getPropertyPrices();
        if (propertyPrices.isEmpty()) {
            System.err.println("Cannot seed payments without property price information.");
            return;
        }

        try {
            File file = new File(PAYMENT_CSV_PATH);
            file.getParentFile().mkdirs(); // Ensure directories exist

            boolean isNewFile = !file.exists();
            FileWriter fw = new FileWriter(file, true); // Append mode
            BufferedWriter writer = new BufferedWriter(fw);

            if (isNewFile) {
                // Write headers if file is newly created
                writer.write("paymentId,tenantId,propertyId,amount,date");
                writer.newLine();
            }

            Random random = new Random();
            Calendar calendar = Calendar.getInstance();

            // Create payments based on accepted bookings
            for (String[] booking : acceptedBookings) {
                String tenantId = booking[1];
                String propertyId = booking[2];

                // Generate payment details
                String paymentId = UUID.randomUUID().toString();

                // Get property price if available, otherwise use a random amount
                double amount = propertyPrices.getOrDefault(propertyId, 1000.0 + random.nextDouble() * 4000.0);

                // Generate a random date within the last 6 months
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -random.nextInt(6));
                calendar.add(Calendar.DAY_OF_MONTH, -random.nextInt(30));
                String paymentDate = DATE_FORMAT.format(calendar.getTime());

                writer.write(String.join(",",
                        paymentId,
                        tenantId,
                        propertyId,
                        String.format("%.2f", amount),
                        paymentDate
                ));
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to payments CSV: " + e.getMessage());
        }
    }

    private static List<String[]> getBookingsByStatus(String status) {
        List<String[]> bookings = new ArrayList<>();
        java.nio.file.Path path = java.nio.file.Paths.get(BOOKING_CSV_PATH);

        try {
            if (!java.nio.file.Files.exists(path)) {
                System.err.println("Bookings CSV file does not exist.");
                return bookings;
            }

            java.util.List<String> lines = java.nio.file.Files.readAllLines(path);
            if (lines.size() <= 1) {
                System.err.println("Bookings CSV file is empty or contains only headers.");
                return bookings;
            }

            // Skip the header line
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                if (parts.length >= 4 && status.equals(parts[3])) {
                    bookings.add(parts);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return bookings;
    }

    private static Map<String, Double> getPropertyPrices() {
        Map<String, Double> propertyPrices = new HashMap<>();
        Path path = Paths.get(PROPERTY_CSV_PATH);

        try {
            if (!Files.exists(path)) {
                System.err.println("Properties CSV file does not exist.");
                return propertyPrices;
            }

            java.util.List<String> lines = Files.readAllLines(path);
            if (lines.size() <= 1) {
                System.err.println("Properties CSV file is empty or contains only headers.");
                return propertyPrices;
            }

            // Skip the header line and look for the price column index
            String[] headers = lines.get(0).split(",");
            int priceColumnIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if ("price".equals(headers[i])) {
                    priceColumnIndex = i;
                    break;
                }
            }

            if (priceColumnIndex == -1) {
                System.err.println("Price column not found in properties CSV.");
                return propertyPrices;
            }

            // Process each property line
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                if (parts.length > priceColumnIndex) {
                    String propertyId = parts[0];
                    try {
                        System.out.println(parts[priceColumnIndex]);
                        propertyPrices.put(propertyId, price);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid price format for property ID: " + propertyId);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return propertyPrices;
    }
}
