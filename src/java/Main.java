import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    // This main method MUST exist
    public static void main(String[] args) {
        FlatLightLaf.setup();  // Initialize FlatLaf
        
        JFrame frame = new JFrame("Criminal Face Detection System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        
        // Setup CardLayout
        JPanel mainPanel = new JPanel(new CardLayout());
        
        mainPanel.add(new LandingPage(), "LANDING");
        mainPanel.add(new DetectPage(), "DETECT");
        mainPanel.add(new AddCriminalPage(), "ADD_CRIMINAL");
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    public static void switchTo(JPanel currentPanel, String cardName) {
        CardLayout cl = (CardLayout)(currentPanel.getParent().getLayout());
        cl.show(currentPanel.getParent(), cardName);
    }
}