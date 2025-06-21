package booking.view;

import booking.model.Booking;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BookingListGUI extends JDialog {
    private final List<Booking> bookings;
    private JList<Booking> bookingList;
    private DefaultListModel<Booking> listModel;

    public BookingListGUI(Dialog parent, List<Booking> bookings, String title) {
        super(parent, title, true);
        this.bookings = bookings;
        setupUI();
    }

    private void setupUI() {
        setSize(650, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Pending Bookings", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Create list model and populate it
        listModel = new DefaultListModel<>();
        for (Booking booking : bookings) {
            listModel.addElement(booking);
        }

        // Create JList with custom renderer
        bookingList = new JList<>(listModel);
        bookingList.setCellRenderer(new BookingListCellRenderer());
        bookingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingList.setFixedCellHeight(70);

        // Add mouse listener for double-click
        bookingList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = bookingList.locationToIndex(e.getPoint());
                    if (index != -1) {
                        Booking selectedBooking = listModel.getElementAt(index);
                        showBookingDetails(selectedBooking);
                    }
                }
            }
        });

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(bookingList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Instructions label
        JLabel instructionLabel = new JLabel("Double-click on a booking to view details", JLabel.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        instructionLabel.setForeground(Color.GRAY);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeButton);

        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(instructionLabel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Show message if no bookings
        if (bookings.isEmpty()) {
            JLabel noBookingsLabel = new JLabel("No pending bookings found", JLabel.CENTER);
            noBookingsLabel.setForeground(Color.GRAY);
            noBookingsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            scrollPane.setViewportView(noBookingsLabel);
        }
    }

    private void showBookingDetails(Booking booking) {
        BookingDetailsGUI detailsGUI = new BookingDetailsGUI(this, booking);
        detailsGUI.setVisible(true);
    }

    // Custom cell renderer for the booking list
    private static class BookingListCellRenderer extends JPanel implements ListCellRenderer<Booking> {
        private JLabel requestIdLabel;
        private JLabel tenantIdLabel;
        private JLabel propertyIdLabel;
        private JLabel statusLabel;

        public BookingListCellRenderer() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(8, 12, 8, 12));

            // Create labels
            requestIdLabel = new JLabel();
            requestIdLabel.setFont(new Font("Arial", Font.BOLD, 12));

            tenantIdLabel = new JLabel();
            tenantIdLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            tenantIdLabel.setForeground(Color.GRAY);

            propertyIdLabel = new JLabel();
            propertyIdLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            propertyIdLabel.setForeground(Color.GRAY);

            statusLabel = new JLabel();
            statusLabel.setFont(new Font("Arial", Font.BOLD, 12));

            // Layout
            JPanel leftPanel = new JPanel(new GridLayout(3, 1, 0, 2));
            leftPanel.add(requestIdLabel);
            leftPanel.add(tenantIdLabel);
            leftPanel.add(propertyIdLabel);

            add(leftPanel, BorderLayout.CENTER);
            add(statusLabel, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Booking> list, Booking booking,
                                                      int index, boolean isSelected, boolean cellHasFocus) {

            requestIdLabel.setText("Booking ID: " + booking.getRequestId());
            tenantIdLabel.setText("👤 Tenant: " + booking.getTenantId());
            propertyIdLabel.setText("🏠 Property: " + booking.getPropertyId());

            // Status styling
            String status = booking.getStatus().toUpperCase();
            statusLabel.setText("⏳ " + status);

            switch (status) {
                case "PENDING":
                    statusLabel.setForeground(new Color(255, 165, 0)); // Orange
                    break;
                case "ACCEPTED":
                    statusLabel.setForeground(new Color(0, 150, 0)); // Green
                    break;
                case "REJECTED":
                    statusLabel.setForeground(Color.RED);
                    break;
                default:
                    statusLabel.setForeground(Color.GRAY);
            }

            // Selection styling
            if (isSelected) {
                setBackground(new Color(184, 207, 229));
                setOpaque(true);
            } else {
                setBackground(Color.WHITE);
                setOpaque(true);
            }

            return this;
        }
    }
}
