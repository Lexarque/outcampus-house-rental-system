package ui.helper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageUploader extends JDialog {  // Changed from JFrame to JDialog

    private String IMAGES_DIR = "";
    private String imagePath = "";

    // UI Components
    private JLabel imagePreview;
    private JLabel imagePathLabel;
    private JButton uploadBtn;
    private JButton clearBtn;
    private JTextField imagePathField;
    private Runnable onCloseCallback;

    // Updated constructor to accept parent Dialog
    public ImageUploader(Dialog parent, String imagePath, Runnable onCloseCallback) {
        super(parent, "Image Upload", true);  // Make it modal to the parent dialog
        this.onCloseCallback = onCloseCallback;

        IMAGES_DIR = imagePath;
        setupUI();
    }

    // Alternative constructor for Frame parent (for backward compatibility)
    public ImageUploader(Frame parent, String imagePath, Runnable onCloseCallback) {
        super(parent, "Image Upload", true);
        this.onCloseCallback = onCloseCallback;

        IMAGES_DIR = imagePath;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Upload Property Image", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Create image panel
        JPanel imagePanel = createImagePanel();

        // Create button panel
        JPanel buttonPanel = createButtonPanel();

        // Create path panel
        JPanel pathPanel = createPathPanel();

        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(imagePanel, BorderLayout.CENTER);
        mainPanel.add(pathPanel, BorderLayout.SOUTH);

        // Add main panel to dialog
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set dialog properties
        setSize(400, 450);
        setLocationRelativeTo(getParent()); // Center relative to parent

        // Ensure directories exist
        createDirectories();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (onCloseCallback != null) {
                    onCloseCallback.run();
                }
            }
        });
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Image Preview"));

        // Image preview
        imagePreview = new JLabel("Click 'Upload Image' to select", JLabel.CENTER);
        imagePreview.setPreferredSize(new Dimension(300, 200));
        imagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imagePreview.setOpaque(true);
        imagePreview.setBackground(Color.LIGHT_GRAY);

        // Image info label
        imagePathLabel = new JLabel("No image selected", JLabel.CENTER);
        imagePathLabel.setForeground(Color.GRAY);
        imagePathLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        panel.add(imagePreview, BorderLayout.CENTER);
        panel.add(imagePathLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPathPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel pathLabel = new JLabel("Image Path:");
        imagePathField = new JTextField();
        imagePathField.setEditable(false);
        imagePathField.setBackground(Color.WHITE);

        panel.add(pathLabel, BorderLayout.WEST);
        panel.add(imagePathField, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));

        uploadBtn = new JButton("Upload Image");
        uploadBtn.setBackground(new Color(33, 150, 243));
        uploadBtn.setForeground(Color.WHITE);
        uploadBtn.setPreferredSize(new Dimension(120, 35));
        uploadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadImage();
            }
        });

        clearBtn = new JButton("Clear");
        clearBtn.setBackground(new Color(158, 158, 158));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setPreferredSize(new Dimension(120, 35));
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearImage();
            }
        });

        panel.add(uploadBtn);
        panel.add(clearBtn);

        return panel;
    }

    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Image File");

        // Set file filter for images
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image Files (*.jpg, *.jpeg, *.png, *.gif, *.bmp)",
                "jpg", "jpeg", "png", "gif", "bmp");
        fileChooser.setFileFilter(filter);

        // Set current directory to user's pictures folder (if exists)
        String userHome = System.getProperty("user.home");
        File picturesDir = new File(userHome + "/Pictures");
        if (picturesDir.exists()) {
            fileChooser.setCurrentDirectory(picturesDir);
        }

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                // Create unique filename with timestamp
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                Path targetPath = Paths.get(IMAGES_DIR + fileName);

                // Copy file to images directory
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Update image path
                imagePath = targetPath.toString();

                // Update UI - Load and display image
                displayImage(targetPath.toString());

                // Update labels and text field
                imagePathLabel.setText("Image uploaded successfully!");
                imagePathLabel.setForeground(new Color(76, 175, 80));
                imagePathField.setText(imagePath);

                // Show success message
                JOptionPane.showMessageDialog(this,
                        "Image uploaded successfully!\nSaved as: " + fileName,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to upload image: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayImage(String imagePath) {
        try {
            ImageIcon imageIcon = new ImageIcon(imagePath);
            Image image = imageIcon.getImage();

            // Scale image to fit preview area while maintaining aspect ratio
            int maxWidth = 280;
            int maxHeight = 180;

            int originalWidth = image.getWidth(null);
            int originalHeight = image.getHeight(null);

            double scaleX = (double) maxWidth / originalWidth;
            double scaleY = (double) maxHeight / originalHeight;
            double scale = Math.min(scaleX, scaleY);

            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);

            Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            imagePreview.setIcon(new ImageIcon(scaledImage));
            imagePreview.setText("");

        } catch (Exception e) {
            imagePreview.setIcon(null);
            imagePreview.setText("Error loading image");
        }
    }

    private void clearImage() {
        imagePreview.setIcon(null);
        imagePreview.setText("Click 'Upload Image' to select");
        imagePathLabel.setText("No image selected");
        imagePathLabel.setForeground(Color.GRAY);
        imagePathField.setText("");
        imagePath = "";
    }

    private void createDirectories() {
        try {
            if (!Files.exists(Paths.get(IMAGES_DIR))) {
                Files.createDirectories(Paths.get(IMAGES_DIR));
                System.out.println("Images directory created: " + IMAGES_DIR);
            }
        } catch (IOException e) {
            System.err.println("Error creating images directory: " + e.getMessage());
        }
    }

    public String getImagePath() {
        return imagePath;
    }
}