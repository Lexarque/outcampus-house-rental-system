package user.view.landlord;

import booking.view.BookingListGUI;
import property.model.Property;
import property.view.PropertyListGUI;
import ui.helper.ImageUploader;
import user.model.Landlord;
import utils.SessionManager;

import javax.swing.*;
import java.awt.*;

public class LandlordMenuGUI extends JDialog {
    private final Landlord landlord = SessionManager.setLandlordData();

    public LandlordMenuGUI(Frame parent) {
        super(parent, "Landlord Menu", true);
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setupUI();
    }

    private void setupUI() {
        JPanel panel = new JPanel(new GridLayout(8, 1, 5, 5));

        JButton postBtn = new JButton("Post Listing");
        JButton editBtn = new JButton("Edit Listing");
        JButton showBtn = new JButton("Show Listings");
        JButton deleteBtn = new JButton("Delete Listing");
        JButton pendingBtn = new JButton("View Pending Bookings");
        JButton acceptRejectBtn = new JButton("Accept/Reject Booking");
        JButton exitBtn = new JButton("Exit");

        postBtn.addActionListener(e -> postListingDialog());
        editBtn.addActionListener(e -> editListingDialog());
        showBtn.addActionListener(e -> showListings());
        deleteBtn.addActionListener(e -> deleteListingDialog());
        pendingBtn.addActionListener(e -> showPendingBookings());
        acceptRejectBtn.addActionListener(e -> updateBookingStatusDialog());
        exitBtn.addActionListener(e -> dispose());

        panel.add(postBtn);
        panel.add(editBtn);
        panel.add(showBtn);
        panel.add(deleteBtn);
        panel.add(pendingBtn);
        panel.add(acceptRejectBtn);
        panel.add(exitBtn);

        setContentPane(panel);
    }

    private void postListingDialog() {
        JDialog dialog = new JDialog(this, "Post Listing", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        JPanel panel = new JPanel(new GridLayout(5, 2));

        JTextField nameField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField priceField = new JTextField();
        JCheckBox availableBox = new JCheckBox("Available", true);

        panel.add(new JLabel("Property Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Location:"));
        panel.add(locationField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Available:"));
        panel.add(availableBox);

        JButton uploadBtn = new JButton("Upload Image and Post");
        uploadBtn.addActionListener(e -> {
            try {
                double price = Double.parseDouble(priceField.getText());
                // Pass the dialog as parent to the postListing method
                landlord.postListing(
                        nameField.getText(),
                        locationField.getText(),
                        price,
                        availableBox.isSelected(),
                        dialog  // Pass the dialog as parent
                );
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid price format.");
            }
        });

        panel.add(new JLabel());
        panel.add(uploadBtn);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private void editListingDialog() {
        landlord.viewOwnListings(false);
        String indexStr = JOptionPane.showInputDialog(this, "Enter the listing number to edit:");
        try {
            int index = Integer.parseInt(indexStr);
            Property prop = landlord.getProperties().get(index - 1);

            JDialog dialog = new JDialog(this, "Edit Listing", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            JPanel panel = new JPanel(new GridLayout(5, 2));

            JTextField nameField = new JTextField(prop.getName());
            JTextField locationField = new JTextField(prop.getLocation());
            JTextField priceField = new JTextField(String.valueOf(prop.getPrice()));
            JCheckBox availableBox = new JCheckBox("Available", prop.getAvailability());

            panel.add(new JLabel("Property Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Location:"));
            panel.add(locationField);
            panel.add(new JLabel("Price:"));
            panel.add(priceField);
            panel.add(new JLabel("Available:"));
            panel.add(availableBox);

            JButton updateBtn = new JButton("Upload New Image and Save");
            updateBtn.addActionListener(e -> {
                try {
                    double price = Double.parseDouble(priceField.getText());
                    // Pass the dialog as parent to the editListing method
                    landlord.editListing(
                            prop.getPropertyId(),
                            nameField.getText(),
                            locationField.getText(),
                            price,
                            availableBox.isSelected(),
                            dialog  // Pass the dialog as parent
                    );
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid price format.");
                }
            });

            panel.add(new JLabel());
            panel.add(updateBtn);
            dialog.setContentPane(panel);
            dialog.setVisible(true);

        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Invalid property selection.");
        }
    }

    private void showListings() {
        if (landlord.getProperties() == null || landlord.getProperties().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No properties found.", "No Properties", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        PropertyListGUI propertyListGUI = new PropertyListGUI(this, landlord.getProperties(), "Your Properties");
        propertyListGUI.setVisible(true);
    }

    private void deleteListingDialog() {
        landlord.viewOwnListings(false);
        String indexStr = JOptionPane.showInputDialog(this, "Enter the listing number to delete:");
        try {
            int index = Integer.parseInt(indexStr);
            Property prop = landlord.getProperties().get(index - 1);
            landlord.deleteListing(prop.getPropertyId());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Invalid property selection.");
        }
    }

    private void showPendingBookings() {
        if (landlord.getPendingPropertyBookings() == null || landlord.getPendingPropertyBookings().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No pending bookings found.", "No Pending Bookings", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        BookingListGUI bookingListGUI = new BookingListGUI(this, landlord.getPendingPropertyBookings(), "Pending Bookings");
        bookingListGUI.setVisible(true);
    }

    private void updateBookingStatusDialog() {
        landlord.viewPendingBookings();
        if (landlord.getPendingPropertyBookings() == null || landlord.getPendingPropertyBookings().isEmpty()) return;

        String bookingIndexStr = JOptionPane.showInputDialog(this, "Enter booking number:");
        String status = JOptionPane.showInputDialog(this, "Enter new status (accepted/rejected):");

        try {
            int bookingIndex = Integer.parseInt(bookingIndexStr);
            String bookingId = landlord.getPendingPropertyBookings().get(bookingIndex - 1).getRequestId();
            landlord.updateBookingStatus(bookingId, status);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }
}