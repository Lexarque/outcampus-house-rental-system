package user.model;

import booking.model.Booking;
import property.model.Property;

import java.util.List;
import java.util.ArrayList;

public class Tenant extends  User {
    private List<Booking> bookings;

    public Tenant(User user) {
        super(user.getName(), user.getPhone(), user.getPassword(), user.getRole());
        setBookings();
    }

    // Operations

    public void searchPropertiesByName(String query) {
        Property dummyProperty = new Property("1", "1", "1", 1.0, true, "1");

        List<Property> properties = new ArrayList<>();
        properties.add(dummyProperty);

        for (Property property : properties) {
            System.out.println(property);
        }
    }

    public void sendBookingRequest(Booking booking) {
        booking.sendRequest();
    }

    public void viewBookings() {
        for (Booking booking : bookings) {
            System.out.println(booking);
        }
    }

    private void setBookings() {
        // Dummy booking insert
        Booking dummyBooking = new Booking("1", "1", "1");
        this.bookings.add(dummyBooking);
    }
}
