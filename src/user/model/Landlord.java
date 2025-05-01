package user.model;

import booking.model.Booking;
import property.model.Property;

import java.util.List;

public class Landlord extends User {
    private List<Property> property;

    public Landlord(User user) {
        super(user.getUserId(), user.getName(), user.getPhone(), user.getPassword(), user.getRole());
        setProperties();
    }

    public void postListing() {
        Property dummyProperty = new Property("1", "1", "1", 1.0, true, "1");
        dummyProperty.createNewProperty(dummyProperty);
    }

    public void editListing() {
        System.out.println("Successfully edited listing");
    }

    public void deleteListing() {
        System.out.println("Successfully deleted listing");
    }

    public void viewOwnListings() {
        for (Property property : property) {
            System.out.println(property);
        }
    }

    private void setProperties() {
        // Dummy property insert
        Property property = new Property("1", "1", "1", 1.0, true, "1");
        this.property.add(property);
    }
}
