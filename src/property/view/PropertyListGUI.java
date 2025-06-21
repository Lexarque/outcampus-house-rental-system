package property.view;

import property.model.Property;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class PropertyListGUI extends JDialog {
    private final List<Property> properties;
    private JList<Property> propertyList;
    private DefaultListModel<Property> listModel;

    public PropertyListGUI(Dialog parent, List<Property> properties, String title) {
        super(parent, title, true);
        this.properties = properties;
        setupUI();
    }

    private void setupUI() {
        setSize(600, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Your Properties", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Create list model and populate it
        listModel = new DefaultListModel<>();
        for (Property property : properties) {
            listModel.addElement(property);
        }

        // Create JList with custom renderer
        propertyList = new JList<>(listModel);
        propertyList.setCellRenderer(new PropertyListCellRenderer());
        propertyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        propertyList.setFixedCellHeight(80);

        // Add mouse listener for double-click
        propertyList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = propertyList.locationToIndex(e.getPoint());
                    if (index != -1) {
                        Property selectedProperty = listModel.getElementAt(index);
                        showPropertyDetails(selectedProperty);
                    }
                }
            }
        });

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(propertyList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Instructions label
        JLabel instructionLabel = new JLabel("Double-click on a property to view details", JLabel.CENTER);
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

        // Show message if no properties
        if (properties.isEmpty()) {
            JLabel noPropertiesLabel = new JLabel("No properties found", JLabel.CENTER);
            noPropertiesLabel.setForeground(Color.GRAY);
            noPropertiesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            scrollPane.setViewportView(noPropertiesLabel);
        }
    }

    private void showPropertyDetails(Property property) {
        PropertyDetailsGUI detailsGUI = new PropertyDetailsGUI(this, property);
        detailsGUI.setVisible(true);
    }

    // Custom cell renderer for the property list
    private static class PropertyListCellRenderer extends JPanel implements ListCellRenderer<Property> {
        private JLabel nameLabel;
        private JLabel locationLabel;
        private JLabel priceLabel;
        private JLabel statusLabel;

        public PropertyListCellRenderer() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(8, 12, 8, 12));

            // Create labels
            nameLabel = new JLabel();
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

            locationLabel = new JLabel();
            locationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            locationLabel.setForeground(Color.GRAY);

            priceLabel = new JLabel();
            priceLabel.setFont(new Font("Arial", Font.BOLD, 12));
            priceLabel.setForeground(new Color(0, 150, 0));

            statusLabel = new JLabel();
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));

            // Layout
            JPanel leftPanel = new JPanel(new GridLayout(3, 1, 0, 2));
            leftPanel.add(nameLabel);
            leftPanel.add(locationLabel);
            leftPanel.add(priceLabel);

            add(leftPanel, BorderLayout.CENTER);
            add(statusLabel, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Property> list, Property property,
                                                      int index, boolean isSelected, boolean cellHasFocus) {

            nameLabel.setText(property.getName());
            locationLabel.setText("📍 " + property.getLocation());
            priceLabel.setText("$" + String.format("%.2f", property.getPrice()));

            // Status indicator
            if (property.getAvailability()) {
                statusLabel.setText("✅ Available");
                statusLabel.setForeground(new Color(0, 150, 0));
            } else {
                statusLabel.setText("❌ Unavailable");
                statusLabel.setForeground(Color.RED);
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
