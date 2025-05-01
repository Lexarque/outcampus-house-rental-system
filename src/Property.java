public class Property {
    private String propertyId;
    private String title;
    private String location;
    private double price;
    private boolean availability;
    private String landlordId;

    public Property(String propertyId, String title, String location, double price, boolean availability,
            String landlordId) {
        this.propertyId = propertyId;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    public void viewDetails() {
        System.out.println("Property ID: " + propertyId);
        System.out.println("Title: " + title);
        System.out.println("Location: " + location);
        System.out.println("Price: " + price);
        System.out.println("Available: " + (availability ? "Yes" : "No"));
        System.out.println("Landlord ID: " + landlordId);
    }
}
