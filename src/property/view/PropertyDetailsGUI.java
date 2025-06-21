package property.view;

import property.model.Property;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;

public class PropertyDetailsGUI extends JDialog {
    private final Property property;
    private JLabel imageLabel;
    private JPanel imagePanel;

    public PropertyDetailsGUI(Dialog parent, Property property) {
        super(parent, "Property Details", true);
        this.property = property;
        setupUI();
    }

    private void setupUI() {
        setSize(650, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel("Property Details", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Create content panel with image and details side by side
        JPanel contentPanel = new JPanel(new BorderLayout());

        // Image panel (left side)
        imagePanel = createImagePanel();

        // Details panel (right side)
        JPanel detailsPanel = createDetailsPanel();

        // Add to content panel
        contentPanel.add(imagePanel, BorderLayout.WEST);
        contentPanel.add(detailsPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        // Add all to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Property Image"));
        panel.setPreferredSize(new Dimension(300, 250));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.LIGHT_GRAY);

        // Load and display image
        loadPropertyImage();

        JScrollPane imageScrollPane = new JScrollPane(imageLabel);
        imageScrollPane.setPreferredSize(new Dimension(280, 220));

        panel.add(imageScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadPropertyImage() {
        String imagePath = property.getImagePath();

        if (imagePath == null || imagePath.trim().isEmpty() || imagePath.equalsIgnoreCase("null")) {
            // No image available
            imageLabel.setText("<html><center>🏠<br><br>No Image Available</center></html>");
            imageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            imageLabel.setForeground(Color.GRAY);
            imageLabel.setPreferredSize(new Dimension(280, 220));
        } else {
            // Try to load the image
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                try {
                    ImageIcon imageIcon = new ImageIcon(imagePath);
                    Image image = imageIcon.getImage();

                    // Scale image to fit while maintaining aspect ratio
                    int maxWidth = 280;
                    int maxHeight = 220;

                    int originalWidth = image.getWidth(null);
                    int originalHeight = image.getHeight(null);

                    if (originalWidth > 0 && originalHeight > 0) {
                        double scaleX = (double) maxWidth / originalWidth;
                        double scaleY = (double) maxHeight / originalHeight;
                        double scale = Math.min(scaleX, scaleY);

                        int scaledWidth = (int) (originalWidth * scale);
                        int scaledHeight = (int) (originalHeight * scale);

                        Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaledImage));
                        imageLabel.setText("");
                        imageLabel.setPreferredSize(new Dimension(scaledWidth, scaledHeight));
                    } else {
                        showImageError();
                    }
                } catch (Exception e) {
                    showImageError();
                }
            } else {
                showImageNotFound();
            }
        }
    }

    private void showImageError() {
        imageLabel.setText("<html><center>❌<br><br>Error Loading Image</center></html>");
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        imageLabel.setForeground(Color.RED);
        imageLabel.setIcon(null);
        imageLabel.setPreferredSize(new Dimension(280, 220));
    }

    private void showImageNotFound() {
        imageLabel.setText("<html><center>📁<br><br>Image File Not Found<br><small>" + property.getImagePath() + "</small></center></html>");
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        imageLabel.setForeground(Color.ORANGE);
        imageLabel.setIcon(null);
        imageLabel.setPreferredSize(new Dimension(280, 220));
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Property Information"));

        // Property details
        addDetailRow(panel, "Property Name:", property.getName());
        addDetailRow(panel, "Location:", property.getLocation());
        addDetailRow(panel, "Price:", "$" + String.format("%.2f", property.getPrice()));

        // Availability status
        String availabilityText = property.getAvailability() ? "Available" : "Not Available";
        Color availabilityColor = property.getAvailability() ? new Color(0, 150, 0) : Color.RED;
        addDetailRow(panel, "Status:", availabilityText, availabilityColor);

        addDetailRow(panel, "Property ID:", property.getPropertyId());
        addDetailRow(panel, "Landlord ID:", property.getLandlordId());

        // Image path (only show if not null)
        if (property.getImagePath() != null && !property.getImagePath().equalsIgnoreCase("null")) {
            addDetailRow(panel, "Image Path:", property.getImagePath());
        }

        return panel;
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
