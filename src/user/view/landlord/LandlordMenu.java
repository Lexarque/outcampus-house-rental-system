package user.view.landlord;

import user.model.Landlord;
import utils.SessionManager;

import java.util.Scanner;

public class LandlordMenu {
    private static Landlord landlord = SessionManager.setLandlordData();
    private final static Scanner scanner = new Scanner(System.in);

    public static void printLandlordMenu() {
        while (true) {
            System.out.print("""
                1. Post Listing\

                2. Edit Listing\

                3. Show Listings\
                
                4. Delete Listing\
                
                5. View Pending Bookings\
                
                6. Accept or Reject Booking\
                
                7. Exit\
                
                : \s""");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    postListing();
                    break;
                case 2:
                    editListing();
                    break;
                case 3:
                    landlord.viewOwnListings(false);
                    break;
                case 4:
                    deleteListing();
                    break;
                case 5:
                    landlord.viewPendingBookings();
                    break;
                case 6:
                    updateBookingStatus();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private static void postListing() {
        System.out.print("Enter property name: ");
        String name = scanner.nextLine();
        System.out.print("Enter property location: ");
        String location = scanner.nextLine();
        System.out.print("Enter property price: ");
        double price = Double.parseDouble(scanner.nextLine());
        landlord.postListing(name, location, price, true);
    }

    private static void editListing() {
        landlord.viewOwnListings(false);
        System.out.print("Choose the property number to edit: ");
        int propertyNumber = Integer.parseInt(scanner.nextLine());
        String propertyId = landlord.getProperties().get(propertyNumber - 1).getPropertyId();
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new location: ");
        String newLocation = scanner.nextLine();
        System.out.print("Enter new price: ");
        double newPrice = Double.parseDouble(scanner.nextLine());
        System.out.print("Is it available? (true/false): ");
        boolean newAvailability = Boolean.parseBoolean(scanner.nextLine());
        landlord.editListing(propertyId, newName, newLocation, newPrice, newAvailability);
    }

    private static void deleteListing() {
        landlord.viewOwnListings(false);
        System.out.print("Choose the property number to delete: ");
        int propertyNumber = Integer.parseInt(scanner.nextLine());
        String propertyId = landlord.getProperties().get(propertyNumber - 1).getPropertyId();
        landlord.deleteListing(propertyId);
    }

    private static void updateBookingStatus() {
        landlord.viewPendingBookings();
        if (landlord.getPendingPropertyBookings() == null || landlord.getPendingPropertyBookings().isEmpty()) {
            return;
        }
        System.out.print("Choose the booking number to approve/reject: ");
        int bookingNumber = Integer.parseInt(scanner.nextLine());
        String bookingId = landlord.getPendingPropertyBookings().get(bookingNumber - 1).getRequestId();
        System.out.print("Enter new status (accepted/rejected): ");
        String newStatus = scanner.nextLine();
        landlord.updateBookingStatus(bookingId, newStatus);
    }
}
