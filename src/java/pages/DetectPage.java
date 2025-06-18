
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DetectPage extends JPanel {
    private JLabel capturedImageLabel;
    private JLabel matchedImageLabel;
    private JTextArea resultTextArea;
    private JLabel statusLabel;
    
    public DetectPage() {
        setLayout(new BorderLayout());
        
        // Header and Footer
        add(new HeaderPanel(this), BorderLayout.NORTH);
        add(new FooterPanel(), BorderLayout.SOUTH);
        
        // Main Content Panel with 3 sections
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Left Panel - Camera Control and Captured Image
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Camera Capture"));
        
        JButton captureBtn = new JButton("Capture and Detect");
        captureBtn.setPreferredSize(new Dimension(200, 40));
        captureBtn.setBackground(new Color(52, 152, 219));
        captureBtn.setForeground(Color.WHITE);
        captureBtn.setFocusPainted(false);
        captureBtn.addActionListener(this::captureAndDetect);
        
        capturedImageLabel = new JLabel("Captured image will appear here", SwingConstants.CENTER);
        capturedImageLabel.setPreferredSize(new Dimension(320, 240));
        capturedImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JPanel captureControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        captureControlPanel.add(captureBtn);
        
        leftPanel.add(captureControlPanel, BorderLayout.NORTH);
        leftPanel.add(capturedImageLabel, BorderLayout.CENTER);
        
        // Middle Panel - Matched Criminal Image
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBorder(BorderFactory.createTitledBorder("Matched Criminal"));
        
        matchedImageLabel = new JLabel("Match result will appear here", SwingConstants.CENTER);
        matchedImageLabel.setPreferredSize(new Dimension(320, 240));
        matchedImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        statusLabel = new JLabel("Status: Ready", SwingConstants.CENTER);
        
        middlePanel.add(matchedImageLabel, BorderLayout.CENTER);
        middlePanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Right Panel - Criminal Details
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Criminal Information"));
        
        resultTextArea = new JTextArea(10, 25);
        resultTextArea.setEditable(false);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        rightPanel.add(new JScrollPane(resultTextArea), BorderLayout.CENTER);
        
        // Add all panels to main panel
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(leftPanel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(middlePanel, gbc);
        
        gbc.gridx = 2;
        mainPanel.add(rightPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void captureAndDetect(ActionEvent e) {
        try {
            statusLabel.setText("Status: Capturing image...");
              // Execute Python scripts for camera capture
            ProcessBuilder captureProcessBuilder = new ProcessBuilder(
                "python", "src/python/camera.py"
            );
            captureProcessBuilder.redirectErrorStream(true);
            Process capture = captureProcessBuilder.start();
            
            // Read process output 
            BufferedReader reader = new BufferedReader(new InputStreamReader(capture.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                System.out.println("Camera script: " + line);
            }
            
            int captureExitCode = capture.waitFor();
            
            if (captureExitCode != 0) {
                throw new Exception("Failed to capture image from camera: " + output.toString());
            }
            
            // Display the captured image
            File capturedFile = new File("capture/temp.jpg");
            if (capturedFile.exists()) {
                ImageIcon capturedIcon = new ImageIcon(capturedFile.getPath());
                Image scaledImage = capturedIcon.getImage().getScaledInstance(320, 240, Image.SCALE_SMOOTH);
                capturedImageLabel.setIcon(new ImageIcon(scaledImage));
                capturedImageLabel.setText("");
            }
            
            statusLabel.setText("Status: Analyzing face...");
              // Execute Python script for face matching
            ProcessBuilder matchProcessBuilder = new ProcessBuilder(
                "python", "src/python/match.py"
            );
            matchProcessBuilder.redirectErrorStream(true);
            Process match = matchProcessBuilder.start();
            
            // Read process output
            BufferedReader matchReader = new BufferedReader(new InputStreamReader(match.getInputStream()));
            String matchLine;
            StringBuilder matchOutput = new StringBuilder();
            
            while ((matchLine = matchReader.readLine()) != null) {
                matchOutput.append(matchLine).append("\n");
                System.out.println("Match script: " + matchLine);
            }
            
            int matchExitCode = match.waitFor();
            
            if (matchExitCode != 0) {
                throw new Exception("Failed to analyze the captured image: " + matchOutput.toString());
            }
              // Read results and update UI
            String matchedId = null;
            float confidence = 0;
            
            File resultsFile = new File("capture/results.txt");
            if (resultsFile.exists()) {
                BufferedReader resultsReader = null;
                try {
                    resultsReader = new BufferedReader(new FileReader(resultsFile));
                    String resultsLine;
                    while ((resultsLine = resultsReader.readLine()) != null) {
                        if (resultsLine.startsWith("Match Found:")) {
                            matchedId = resultsLine.substring("Match Found:".length()).trim();
                        } else if (resultsLine.startsWith("Confidence:")) {
                            try {
                                String confStr = resultsLine.substring("Confidence:".length()).trim();
                                confStr = confStr.replace("%", "");
                                confidence = Float.parseFloat(confStr);
                            } catch (Exception ex) {
                                System.err.println("Error parsing confidence: " + ex.getMessage());
                            }
                        }
                    }
                } catch (IOException ex) {
                    System.err.println("Error reading results file: " + ex.getMessage());
                } finally {
                    if (resultsReader != null) {
                        try {
                            resultsReader.close();
                        } catch (IOException ex) {
                            System.err.println("Error closing reader: " + ex.getMessage());
                        }
                    }
                }
            }
            
            if (matchedId != null) {
                // Found a match - display information and criminal image
                statusLabel.setText("Status: Match found! Confidence: " + String.format("%.1f%%", confidence));
                
                // Try to load the criminal image
                File criminalImageFile = new File("database/" + matchedId + ".jpg");
                if (!criminalImageFile.exists()) {
                    // Try different extensions
                    for (String ext : new String[] {".jpeg", ".png", ".webp"}) {
                        criminalImageFile = new File("database/" + matchedId + ext);
                        if (criminalImageFile.exists()) break;
                    }
                }
                
                if (criminalImageFile.exists()) {
                    ImageIcon matchedIcon = new ImageIcon(criminalImageFile.getPath());
                    Image scaledMatchedImage = matchedIcon.getImage().getScaledInstance(320, 240, Image.SCALE_SMOOTH);
                    matchedImageLabel.setIcon(new ImageIcon(scaledMatchedImage));
                    matchedImageLabel.setText("");
                }
                
                // Get criminal details from database
                Criminal criminal = DatabaseConnection.getCriminalById(matchedId);
                if (criminal != null) {
                    resultTextArea.setText(criminal.toString() + "\n\nMatch Confidence: " + String.format("%.1f%%", confidence));
                } else {
                    resultTextArea.setText("ID: " + matchedId + "\n\n" +
                                         "Warning: Found a face match but no database entry exists for this ID.\n\n" +
                                         "Match Confidence: " + String.format("%.1f%%", confidence));
                }
            } else {
                // No match found
                statusLabel.setText("Status: No match found in criminal database");
                matchedImageLabel.setIcon(null);
                matchedImageLabel.setText("No match found");
                resultTextArea.setText("No criminal match found in the database.");
            }
            
        } catch (Exception ex) {
            statusLabel.setText("Status: Error");
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}