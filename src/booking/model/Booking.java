package booking.model;

public class Booking {
    private String requestId;
    private String tenantId;
    private String propertyId;
    private String status = "pending";

    public Booking(String requestId, String tenantId, String propertyId) {
        this.requestId = requestId;
        this.tenantId = tenantId;
        this.propertyId = propertyId;
    }

    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Operations
    public void sendRequest() {
        this.status = "pending";
        System.out.println("booking.model.Booking request sent for property.model.Property ID: " + propertyId);
    }

    public String viewStatus() {
        return status;
    }

    public void updateStatus(String status) {
        this.status = status;
        System.out.println("booking.model.Booking status updated to: " + status);
    }
}
