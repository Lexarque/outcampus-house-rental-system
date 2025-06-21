package booking.view;

import booking.model.Booking;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class BookingDetailsGUI extends JDialog {
    private final Booking booking;

    public BookingDetailsGUI(Dialog parent, Booking booking) {
        super(parent, "Booking Details", true);
        this.booking = booking;
        setupUI();
    }

    private void setupUI() {
        setSize(500, 300);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel("Booking Details", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Details panel
        JPanel detailsPanel = createDetailsPanel();

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        // Add all to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Booking Information"));

        // Booking details
        addDetailRow(panel, "Request ID:", booking.getRequestId());
        addDetailRow(panel, "Tenant ID:", booking.getTenantId());
        addDetailRow(panel, "Property ID:", booking.getPropertyId());

        // Status with color
        String status = booking.getStatus();
        Color statusColor = getStatusColor(status);
        addDetailRow(panel, "Status:", status.toUpperCase(), statusColor);

        // Add some spacing
        panel.add(Box.createVerticalStrut(20));

        // Add action buttons if status is pending
        if ("pending".equalsIgnoreCase(status)) {
            JPanel actionPanel = new JPanel(new FlowLayout());
            actionPanel.setBorder(new TitledBorder("Actions"));

            JButton acceptButton = new JButton("Accept");
            acceptButton.setBackground(new Color(0, 150, 0));
            acceptButton.setForeground(Color.WHITE);
            acceptButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to accept this booking?",
                        "Confirm Accept",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    // Here you would call the landlord's updateBookingStatus method
                    JOptionPane.showMessageDialog(this, "Booking accepted successfully!");
                    dispose();
                }
            });

            JButton rejectButton = new JButton("Reject");
            rejectButton.setBackground(Color.RED);
            rejectButton.setForeground(Color.WHITE);
            rejectButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to reject this booking?",
                        "Confirm Reject",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    // Here you would call the landlord's updateBookingStatus method
                    JOptionPane.showMessageDialog(this, "Booking rejected successfully!");
                    dispose();
                }
            });

            actionPanel.add(acceptButton);
            actionPanel.add(rejectButton);
            panel.add(actionPanel);
        }

        return panel;
    }

    private Color getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                return new Color(255, 165, 0); // Orange
            case "accepted":
                return new Color(0, 150, 0); // Green
            case "rejected":
                return Color.RED;
            default:
                return Color.GRAY;
        }
    }

    private void addDetailRow(JPanel parent, String label, String value) {
        addDetailRow(parent, label, value, Color.BLACK);
    }

    private void addDetailRow(JPanel parent, String label, String value, Color valueColor) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 12));
        labelComponent.setPreferredSize(new Dimension(100, 20));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 12));
        valueComponent.setForeground(valueColor);

        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(valueComponent, BorderLayout.CENTER);

        parent.add(rowPanel);
    }
}
