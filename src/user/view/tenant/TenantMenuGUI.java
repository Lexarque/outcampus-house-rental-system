package user.view.tenant;

import property.model.Property;
import user.model.Tenant;
import utils.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TenantMenuGUI extends JDialog {
    private final Tenant tenant = SessionManager.setTenantData();

    public TenantMenuGUI(Frame parent) {
        super(parent, "Tenant Menu", true);
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setupUI();
    }

    private void setupUI() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));

        JButton bookingBtn = new JButton("Create Booking Request");
        JButton viewBookingsBtn = new JButton("View Own Booking Requests");
        JButton viewPaymentsBtn = new JButton("View Own Payment History");
        JButton statusBtn = new JButton("Get Payment Status for Selected Month");
        JButton payBtn = new JButton("Pay for Selected Month");
        JButton exitBtn = new JButton("Exit");

        bookingBtn.addActionListener(e -> createBookingDialog());
        viewBookingsBtn.addActionListener(e -> {
            List<String> bookings = tenant.getOwnBookingSummaries(); // You need to implement this method in Tenant
            if (bookings == null || bookings.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You have no bookings.");
                return;
            }
            JTextArea textArea = new JTextArea(String.join("\n", bookings));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(350, 200));
            JOptionPane.showMessageDialog(this, scrollPane, "Your Bookings", JOptionPane.INFORMATION_MESSAGE);
        });

        viewPaymentsBtn.addActionListener(e -> {
            List<String> payments = tenant.getOwnPaymentSummaries(); // You need to implement this method in Tenant
            if (payments == null || payments.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You have no payment history.");
                return;
            }
            JTextArea textArea = new JTextArea(String.join("\n", payments));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(350, 200));
            JOptionPane.showMessageDialog(this, scrollPane, "Payment History", JOptionPane.INFORMATION_MESSAGE);
        });

        statusBtn.addActionListener(e -> getPaymentStatusDialog());
        payBtn.addActionListener(e -> payForMonthDialog());
        exitBtn.addActionListener(e -> dispose());

        panel.add(bookingBtn);
        panel.add(viewBookingsBtn);
        panel.add(viewPaymentsBtn);
        panel.add(statusBtn);
        panel.add(payBtn);
        panel.add(exitBtn);

        setContentPane(panel);
    }

    private void createBookingDialog() {
        List<Property> properties = new Property().getPropertiesFromCsv(true);

        int filter = JOptionPane.showConfirmDialog(this, "Filter properties by price?", "Filter",
                JOptionPane.YES_NO_OPTION);
        if (filter == JOptionPane.YES_OPTION) {
            try {
                String minStr = JOptionPane.showInputDialog(this, "Enter minimum price:");
                String maxStr = JOptionPane.showInputDialog(this, "Enter maximum price:");
                double min = Double.parseDouble(minStr);
                double max = Double.parseDouble(maxStr);
                properties = tenant.filterActivePropertyByPrice(properties, min, max);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid price input.");
                return;
            }
        }

        if (properties == null || properties.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No properties found.");
            return;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < properties.size(); i++) {
            Property p = properties.get(i);
            message.append(i + 1).append(". ")
                    .append(p.getName()).append(" - ")
                    .append(p.getLocation()).append(" - RM").append(p.getPrice())
                    .append("\n");
        }

        String choice = JOptionPane.showInputDialog(this, "Choose a property to book:\n" + message);
        try {
            int selected = Integer.parseInt(choice);
            String propertyId = properties.get(selected - 1).getPropertyId();
            tenant.sendBookingRequest(propertyId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid selection.");
        }
    }

    private void getPaymentStatusDialog() {
        List<String> approved = tenant.getAcceptedBookingPropertyIds();
        if (approved == null || approved.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No approved bookings found.");
            return;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < approved.size(); i++) {
            message.append(i + 1).append(". ").append(approved.get(i)).append("\n");
        }

        try {
            String indexStr = JOptionPane.showInputDialog(this, "Choose property:\n" + message);
            int index = Integer.parseInt(indexStr);
            String propertyId = approved.get(index - 1);

            String monthStr = JOptionPane.showInputDialog(this, "Enter month (1-12):");
            int month = Integer.parseInt(monthStr);

            tenant.getPaymentStatusForSelectedMonth(month, propertyId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void payForMonthDialog() {
        List<String> approved = tenant.getAcceptedBookingPropertyIds();
        if (approved == null || approved.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No approved bookings found.");
            return;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < approved.size(); i++) {
            message.append(i + 1).append(". ").append(approved.get(i)).append("\n");
        }

        try {
            String indexStr = JOptionPane.showInputDialog(this, "Choose property:\n" + message);
            int index = Integer.parseInt(indexStr);
            String propertyId = approved.get(index - 1);

            String monthStr = JOptionPane.showInputDialog(this, "Enter month (1-12):");
            int month = Integer.parseInt(monthStr);

            String amountStr = JOptionPane.showInputDialog(this, "Enter amount to pay:");
            double amount = Double.parseDouble(amountStr);

            tenant.payForTheSelectedMonth(month, propertyId, amount);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }
}
