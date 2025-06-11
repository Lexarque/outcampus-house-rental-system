package user.model;

import booking.model.Booking;
import payment.model.Payment;
import property.model.Property;

import java.awt.print.Book;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tenant extends  User {
    private List<Booking> bookings = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();

    private static final String BOOKING_CSV_PATH = "src/file/booking/bookings.csv";
    private static final String PAYMENT_CSV_PATH = "src/file/payment/payments.csv";

    public Tenant() {
        super();
    }

    public Tenant(User user) {
        super(user.getUserId(), user.getName(), user.getPhone(), user.getPassword(), user.getRole());
        setBookings();
        Payment.setPaymentHistory(getUserId(), payments);
    }

    // Operations

    public List<Property> filterActivePropertyByPrice(List<Property> activeProperties, double minPrice, double maxPrice) {
        List<Property> filteredProperties = new ArrayList<>();

        for (Property property : activeProperties) {
            if (property.getPrice() >= minPrice && property.getPrice() <= maxPrice) {
                filteredProperties.add(property);
            }
        }

        if (filteredProperties.isEmpty()) {
            System.out.println("No properties found in the specified price range.");
        } else {
            return filteredProperties;
        }

        return null;
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
                writer.write("userId,name,phone,password,role");
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

                        setBookings();

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

    public List<String> getAcceptedBookingPropertyIds() {
        List<String> acceptedPropertyIds = new ArrayList<>();
        for (Booking booking : bookings) {
            if ("accepted".equalsIgnoreCase(booking.getStatus()) && !acceptedPropertyIds.contains(booking.getPropertyId())) {
                acceptedPropertyIds.add(booking.getPropertyId());
            }
        }

        if (acceptedPropertyIds.isEmpty()) {
            System.out.println("No accepted bookings found.");
        } else {
            return acceptedPropertyIds;
        }

        return null;
    }

    // Payment

    public void viewOwnPayments() {
        if (payments == null || payments.isEmpty()) {
            System.out.println("No payments found for this tenant.");
        } else {
            System.out.println("-----------------------------------");
            for (Payment payment : payments) {
                payment.printBasicPayments();
                System.out.println("-----------------------------------");
            }
        }
    }

    public boolean getPaymentStatusForSelectedMonth(int month, String propertyId) {
        for (Payment payment : payments) {
            if (payment.getDate().getMonthValue() == month && payment.getPropertyId().equals(propertyId)) {
                System.out.println("Payment found");
                payment.printBasicPayments();
                return true;
            }
        }
        System.out.println("No payments found for the selected month and property ID.");
        return false;
    }

    public void payForTheSelectedMonth(int month, String propertyId, double amount) {
        boolean isPaymentFound = getPaymentStatusForSelectedMonth(month, propertyId);

        if (isPaymentFound) {
            System.out.println("Payment has already been made for this month!");
            return;
        }

        File file = new File(PAYMENT_CSV_PATH);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String paymentId = UUID.randomUUID().toString();
            Payment payment = new Payment(paymentId, getUserId(), propertyId, amount, LocalDate.now());

            writer.write(String.join(",",
                    payment.getPaymentId(),
                    payment.getTenantId(),
                    payment.getPropertyId(),
                    String.valueOf(payment.getAmount()),
                    payment.getDate().toString()
            ));

            writer.newLine();

            // Add new payment
            payments.add(payment);

            System.out.println("Payment recorded successfully for month: " + month + ", property id: " + propertyId);
        } catch (IOException e) {
            System.err.println("Error writing to payment CSV: " + e.getMessage());
        }
    }
}
