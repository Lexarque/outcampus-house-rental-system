package test;

import property.model.Property;
import user.model.Landlord;
import user.model.Tenant;

import java.util.List;
import java.util.Scanner;

public class BookingTest {
    private static final Landlord landlord = new Landlord();
    private static final Tenant tenant = new Tenant();
    private static final Property property = new Property();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        landlord.login("5559876543", "securePass456");
        tenant.login("5554567890", "bobsPassword");

        System.out.print("""
                1. Request Booking\

                2. Approve / Reject Booking\

                3. Show Tenant Bookings\
                
                : \s""");

        int option = Integer.parseInt(scanner.nextLine());

        switch (option) {
            case 1:
                requestBookingTest();
                break;
            case 2:
                changeBookingStatusTest();
                break;
            case 3:
                tenant.viewOwnBookings();
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    }

    private static void requestBookingTest() {
        List<Property> activeProperties = property.getPropertiesFromCsv(true, true);
        System.out.print("Choose the property number make a booking request: ");
        int propertyNumber = Integer.parseInt(scanner.nextLine());
        String propertyId = activeProperties.get(propertyNumber - 1).getPropertyId();
        tenant.sendBookingRequest(propertyId);
    }

    private static void changeBookingStatusTest() {
        landlord.viewPendingBookings();
        System.out.print("Choose the booking number to approve/reject: ");
        int bookingNumber = Integer.parseInt(scanner.nextLine());
        String bookingId = landlord.getPendingPropertyBookings().get(bookingNumber - 1).getRequestId();
        System.out.print("Enter new status (accepted/rejected): ");
        String newStatus = scanner.nextLine();
        landlord.updateBookingStatus(bookingId, newStatus);
    }
}
