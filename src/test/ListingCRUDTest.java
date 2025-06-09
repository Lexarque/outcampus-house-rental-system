package test;

import property.model.Property;
import user.model.Landlord;

import java.util.Scanner;

public class ListingCRUDTest {
    private static final Landlord landlord = new Landlord();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        landlord.login("5559876543", "securePass456");

        System.out.print("""
                1. Post Listing\

                2. Edit Listing\

                3. Show Listings\
                
                4. Delete Listing\
                
                : \s""");
        int option = Integer.parseInt(scanner.nextLine());

        switch (option) {
            case 1:
                postListingTest();
                break;
            case 2:
                editListingTest();
                break;
            case 3:
                landlord.viewOwnListings(true);
                break;
            case 4:
                deleteListingTest();
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    }

    private static void postListingTest() {
        landlord.postListing("Cozy Apartment", "123 Main St", 1200.00, true);
    }

    private static void editListingTest() {
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

    private static void deleteListingTest() {
        landlord.viewOwnListings(false);
        System.out.print("Choose the property number to delete: ");
        int propertyNumber = Integer.parseInt(scanner.nextLine());
        String propertyId = landlord.getProperties().get(propertyNumber - 1).getPropertyId();
        landlord.deleteListing(propertyId);
    }
}
