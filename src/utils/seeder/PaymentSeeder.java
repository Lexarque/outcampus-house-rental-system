package utils.seeder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PaymentSeeder {
    private static final String PROPERTY_CSV_PATH = "src/file/property/properties.csv";
    private static final String BOOKING_CSV_PATH = "src/file/booking/bookings.csv";
    private static final String PAYMENT_CSV_PATH = "src/file/payment/payments.csv";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void seedPayments() {
        List<String[]> acceptedBookings = getBookingsByStatus("accepted");
        if (acceptedBookings.isEmpty()) {
            System.err.println("Cannot seed payments without accepted bookings. Please run seedBookings() first.");
            return;
        }

        Map<String, Double> propertyPrices = getPropertyPrices();
        if (propertyPrices.isEmpty()) {
            System.err.println("Cannot seed payments without property price information.");
            return;
        }

        try {
            File file = new File(PAYMENT_CSV_PATH);
            file.getParentFile().mkdirs();

            boolean isNewFile = !file.exists();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fw);

            if (isNewFile) {
                writer.write("paymentId,tenantId,propertyId,amount,date");
                writer.newLine();
            }

            Random random = new Random();

            for (String[] booking : acceptedBookings) {
                String tenantId = booking[1];
                String propertyId = booking[2];
                String paymentId = UUID.randomUUID().toString();
                double amount = propertyPrices.getOrDefault(propertyId, 1000.0 + random.nextDouble() * 4000.0);

                // Generate a random LocalDate within the last 6 months
                int randomMonths = random.nextInt(6);
                int randomDays = random.nextInt(30);
                LocalDate paymentDate = LocalDate.now().minusMonths(randomMonths).minusDays(randomDays);

                writer.write(String.join(",",
                        paymentId,
                        tenantId,
                        propertyId,
                        String.valueOf(amount),
                        paymentDate.format(DATE_FORMAT)
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
        Path path = Paths.get(BOOKING_CSV_PATH);

        try {
            if (!Files.exists(path)) {
                System.err.println("Bookings CSV file does not exist.");
                return bookings;
            }

            List<String> lines = Files.readAllLines(path);
            if (lines.size() <= 1) {
                System.err.println("Bookings CSV file is empty or contains only headers.");
                return bookings;
            }

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

            List<String> lines = Files.readAllLines(path);
            if (lines.size() <= 1) {
                System.err.println("Properties CSV file is empty or contains only headers.");
                return propertyPrices;
            }

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

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");
                if (parts.length > priceColumnIndex) {
                    String propertyId = parts[0];
                    try {
                        double price = Double.parseDouble(parts[priceColumnIndex]);
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