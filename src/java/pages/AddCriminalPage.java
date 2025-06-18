// Add these imports at the top
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;  // Missing import for File
import javax.swing.filechooser.FileNameExtensionFilter;  // Missing import

public class AddCriminalPage extends JPanel {
    private JTextField idField, nameField, crimeField, cityField, yearField, mobileField;
    private JLabel imageLabel;
    private File selectedImage;
    
    public AddCriminalPage() {
        setLayout(new BorderLayout());
        
        // Header and Footer
        add(new HeaderPanel(this), BorderLayout.NORTH);
        add(new FooterPanel(), BorderLayout.SOUTH);
        
        // Main Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Image Upload
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JButton uploadBtn = new JButton("Upload Image");
        uploadBtn.addActionListener(this::uploadImage);
        formPanel.add(uploadBtn, gbc);
        
        imageLabel = new JLabel("No image selected");
        imageLabel.setPreferredSize(new Dimension(200, 200));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridy = 1;
        formPanel.add(imageLabel, gbc);
        
        // Form Fields
        String[] labels = {"ID:", "Name:", "Crime:", "City:", "Year:", "Mobile:"};
        JTextField[] fields = {
            idField = new JTextField(20),
            nameField = new JTextField(20),
            crimeField = new JTextField(20),
            cityField = new JTextField(20),
            yearField = new JTextField(20),
            mobileField = new JTextField(20)
        };
        
        gbc.gridwidth = 1;
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 2;
            formPanel.add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }
        
        // Submit Button
        JButton submitBtn = new JButton("Add Criminal");
        submitBtn.addActionListener(this::submitForm);
        gbc.gridx = 0; gbc.gridy = labels.length + 2; gbc.gridwidth = 2;
        formPanel.add(submitBtn, gbc);
        
        add(formPanel, BorderLayout.CENTER);
    }
      private void uploadImage(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Criminal Image");
        chooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "webp"));
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedImage = chooser.getSelectedFile();
            
            try {
                // Load the image
                ImageIcon originalIcon = new ImageIcon(selectedImage.getPath());
                
                // Scale the image to fit the label if it's too large
                if (originalIcon.getIconWidth() > 200 || originalIcon.getIconHeight() > 200) {
                    Image scaledImage = originalIcon.getImage().getScaledInstance(
                        200, 200, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    imageLabel.setIcon(originalIcon);
                }
                
                imageLabel.setText("");
                
                // Update ID field with filename without extension if it's empty
                if (idField.getText().trim().isEmpty()) {
                    String filename = selectedImage.getName();
                    int dotIndex = filename.lastIndexOf('.');
                    if (dotIndex > 0) {
                        filename = filename.substring(0, dotIndex);
                    }
                    idField.setText(filename);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error loading image: " + ex.getMessage(),
                    "Image Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
      private void submitForm(ActionEvent e) {
        // Validate form fields
        if (idField.getText().trim().isEmpty() || 
            nameField.getText().trim().isEmpty() || 
            crimeField.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields (ID, Name, Crime)", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate image
        if (selectedImage == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select an image for the criminal", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Create Criminal object
            Criminal criminal = new Criminal();
            criminal.setId(idField.getText().trim());
            criminal.setName(nameField.getText().trim());
            criminal.setCrime(crimeField.getText().trim());
            
            // Build address from city and other fields
            StringBuilder address = new StringBuilder();
            if (!cityField.getText().trim().isEmpty()) {
                criminal.setCity(cityField.getText().trim());
                address.append(cityField.getText().trim());
            }
            
            if (!yearField.getText().trim().isEmpty()) {
                criminal.setYear(yearField.getText().trim());
                if (address.length() > 0) address.append(", ");
                address.append("Year: ").append(yearField.getText().trim());
            }
            
            if (!mobileField.getText().trim().isEmpty()) {
                criminal.setMobile(mobileField.getText().trim());
                if (address.length() > 0) address.append(", ");
                address.append("Contact: ").append(mobileField.getText().trim());
            }
            
            criminal.setAddress(address.toString());
            
            // Save to database
            boolean success = DatabaseConnection.addCriminal(criminal);
            
            if (success) {
                // Copy image to database folder
                File databaseDir = new File("database");
                if (!databaseDir.exists()) {
                    databaseDir.mkdir();
                }
                
                // Determine file extension
                String extension = getFileExtension(selectedImage.getName());
                File destFile = new File("database/" + criminal.getId() + "." + extension);
                
                // Copy the file
                java.nio.file.Files.copy(
                    selectedImage.toPath(), 
                    destFile.toPath(), 
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
                
                JOptionPane.showMessageDialog(this, 
                    "Criminal successfully added to database!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear form
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to add criminal to database. Please try again.", 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return "jpg"; // Default extension
    }
    
    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        crimeField.setText("");
        cityField.setText("");
        yearField.setText("");
        mobileField.setText("");
        imageLabel.setIcon(null);
        imageLabel.setText("No image selected");
        selectedImage = null;
    }
}