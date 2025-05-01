package property.model;

public class Property {
    private String propertyId;
    private String name;
    private String location;
    private double price;
    private boolean availability;
    private String landlordId;
    private String status = "active";

    public Property(String propertyId, String name, String location, double price, boolean availability,
                    String landlordId) {
        this.propertyId = propertyId;
        this.name = name;
        this.location = location;
        this.price = price;
        this.availability = availability;
        this.landlordId = landlordId;
    }

    // Getters and Setters
    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(String landlordId) {
        this.landlordId = landlordId;
    }

    // Operations
    public void createNewProperty(Property property) {
        System.out.println("Successfully created new property: ");
        System.out.println(property);
    }

    public void viewDetails() {
        System.out.println("property.model.Property ID: " + propertyId);
        System.out.println("Title: " + name);
        System.out.println("Location: " + location);
        System.out.println("Price: " + price);
        System.out.println("Available: " + (availability ? "Yes" : "No"));
        System.out.println("Landlord ID: " + landlordId);
    }

    @Override
    public String toString() {
        return "Property Id: " + propertyId + ", Title: " + name + ", Location: " + location + ", Price: " + price
                + ", Available: " + (availability ? "Yes" : "No") + ", Landlord Id: " + landlordId;
    }
}
