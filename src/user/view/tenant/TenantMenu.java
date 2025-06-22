package user.view.tenant;

import booking.model.Booking;
import property.model.Property;
import user.model.Tenant;
import utils.SessionManager;

import java.util.List;
import java.util.Scanner;

public class TenantMenu {
    private static Tenant tenant = SessionManager.setTenantData();
    private final static Scanner scanner = new Scanner(System.in);

    public static void printTenantMenu() {
        // This is a comment
        while (true) {
            System.out.println();
            System.out.print("""
                1. Create Booking Request\
                
                2. View Own Booking Request\

                3. View Own Payment History\
                
                4. Get Payment Status For Selected Month\
                
                5. Pay For Selected Month\

                6. Exit\
                
                : \s""");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    requestBooking();
                    break;
                case 2:
                    tenant.viewOwnBookings();
                    break;
                case 3:
                    tenant.viewOwnPayments();
                    break;
                case 4:
                    getPaymentStatusForSelectedMonthPropertyId();
                    break;
                case 5:
                    payForSelectedMonthAndPropertyId();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private static void requestBooking() {
        Property property = new Property();
        List<Property> activeProperties = property.getPropertiesFromCsv(true);
        System.out.println("Do you want to filter properties by price? (yes/no)");
        String filterChoice = scanner.nextLine();

        if (filterChoice.equalsIgnoreCase("yes")) {
            System.out.print("Enter minimum price: ");
            double minPrice = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter maximum price: ");
            double maxPrice = Double.parseDouble(scanner.nextLine());
            activeProperties = tenant.filterActivePropertyByPrice(activeProperties, minPrice, maxPrice);
            if (activeProperties == null || activeProperties.isEmpty()) {
                return;
            }
        }

        for (int i = 0; i < activeProperties.size(); i++) {
            System.out.println("-----------------------------------");
            System.out.println("Property " + (i + 1));
            activeProperties.get(i).printBasicProperties();
            System.out.println("-----------------------------------");
        }

        System.out.print("Choose the property number make a booking request: ");
        int propertyNumber = Integer.parseInt(scanner.nextLine());
        String propertyId = activeProperties.get(propertyNumber - 1).getPropertyId();
        tenant.sendBookingRequest(propertyId);
    }

    private static void getPaymentStatusForSelectedMonthPropertyId() {
        List<String> approvedBookingsIds = tenant.getAcceptedBookingPropertyIds();

        if (approvedBookingsIds == null || approvedBookingsIds.isEmpty()) {
            System.out.println("No approved bookings found, please wait for an accepted booking");
            return;
        }

        for (int i = 0; i < approvedBookingsIds.size(); i++) {
            System.out.println("-----------------------------------");
            System.out.println((i + 1) + ". Property Id: " + approvedBookingsIds.get(i));
            System.out.println("-----------------------------------");
        }

        System.out.print("Choose the property number to get payment status: ");
        int choice = Integer.parseInt(scanner.nextLine());
        String propertyId = approvedBookingsIds.get(choice - 1);

        System.out.print("Enter the month (1-12) to get payment status: ");
        int month = Integer.parseInt(scanner.nextLine());
        tenant.getPaymentStatusForSelectedMonth(month, propertyId);
    }

    private static void payForSelectedMonthAndPropertyId() {
        List<String> approvedBookingsIds = tenant.getAcceptedBookingPropertyIds();

        if (approvedBookingsIds == null || approvedBookingsIds.isEmpty()) {
            System.out.println("No approved bookings found, please wait for an accepted booking");
            return;
        }

        for (int i = 0; i < approvedBookingsIds.size(); i++) {
            System.out.println("-----------------------------------");
            System.out.println((i + 1) + ". Property Id: " + approvedBookingsIds.get(i));
            System.out.println("-----------------------------------");
        }

        System.out.print("Choose the property number to get payment status: ");
        int choice = Integer.parseInt(scanner.nextLine());
        String propertyId = approvedBookingsIds.get(choice - 1);

        System.out.print("Enter the month (1-12) to get payment status: ");
        int month = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter the amount you want to pay: ");
        double amount = Double.parseDouble(scanner.nextLine());

        tenant.payForTheSelectedMonth(month, propertyId, amount);
    }
}
