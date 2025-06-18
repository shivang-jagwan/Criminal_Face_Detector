import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main landing page of the application with login/demo options
 * TODO: Add proper error handling for database connection
 */
public class LandingPage extends JPanel {
    private static final Color CARD_BORDER_COLOR = new Color(200, 200, 200);
    private static final int CARD_PADDING = 30;
    private static final int CARD_HORIZONTAL_PADDING = 40;
    
    public LandingPage() {
        // Use BorderLayout for header/footer and main content
        setLayout(new BorderLayout());
        
        // Add header and footer panels
        add(new HeaderPanel(this), BorderLayout.NORTH);
        add(new FooterPanel(), BorderLayout.SOUTH);
        
        // Create main content area with white background
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        
        // Create sign-in card with proper styling
        JPanel signInCard = new JPanel();
        signInCard.setLayout(new BoxLayout(signInCard, BoxLayout.Y_AXIS));
        
        // Add border and padding to the card
        signInCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER_COLOR),
            BorderFactory.createEmptyBorder(CARD_PADDING, 
                                          CARD_HORIZONTAL_PADDING, 
                                          CARD_PADDING, 
                                          CARD_HORIZONTAL_PADDING)
        ));
        
        // Add title with bold font
        JLabel titleLabel = new JLabel("CRIMINAL FACE DETECTION");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        // Create demo button with action listener
        JButton demoButton = new JButton("Try Demo");
        demoButton.addActionListener(e -> Main.switchTo(this, "DETECT"));
        
        // Add components to the sign-in card
        signInCard.add(titleLabel);
        signInCard.add(Box.createRigidArea(new Dimension(0, 20)));
        signInCard.add(demoButton);
        
        // Add card to content panel
        contentPanel.add(signInCard);
        
        // Add content panel to main layout
        add(contentPanel, BorderLayout.CENTER);
    }
}