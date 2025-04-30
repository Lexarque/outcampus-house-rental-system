import java.util.Date;

public class Payment {
    private String paymentId;
    private String tenantId;
    private String propertyId;
    private double amount;
    private Date date;

    public Payment(String paymentId, String tenantId, String propertyId, double amount, Date date) {
        this.paymentId = paymentId;
        this.tenantId = tenantId;
        this.propertyId = propertyId;
        this.amount = amount;
        this.date = date;
    }

    public void trackPayment() {
        System.out.println("Tracking payment of " + amount + " for property " + propertyId + " made by tenant " + tenantId + " on " + date);
    }

    public void sendReminder() {
        System.out.println("Sending payment reminder to tenant " + tenantId);
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
