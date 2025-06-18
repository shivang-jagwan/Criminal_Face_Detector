import java.awt.*;
import javax.swing.*;

public class FooterPanel extends JPanel {
    public FooterPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(new Color(44, 62, 80));
        setPreferredSize(new Dimension(100, 40));
        
        JLabel footerText = new JLabel("Criminal Detection System © 2024 | Contact: admin@college.edu");
        footerText.setForeground(Color.WHITE);
        add(footerText);
    }
}