import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HeaderPanel extends JPanel {
    // Constants for colors
    private static final Color HEADER_BG = new Color(44, 62, 80);
    private static final Color BTN_BG = new Color(52, 152, 219);
    private static final Color BTN_HOVER = new Color(41, 128, 185);

    public HeaderPanel(JPanel currentPanel) {
        setLayout(new BorderLayout());
        setBackground(HEADER_BG);
        setPreferredSize(new Dimension(100, 60));
        
        // Logo with error handling
        ImageIcon logoIcon;
        try {
            logoIcon = new ImageIcon("assets/logo.png");
        } catch (Exception e) {
            logoIcon = new ImageIcon();
            System.err.println("Could not load logo: " + e.getMessage());
        }
        JLabel logo = new JLabel(logoIcon);
        logo.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        
        // Navigation buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navPanel.setOpaque(false);
        
        String[] pages = {"LANDING", "DETECT", "ADD_CRIMINAL"};
        String[] labels = {"Home", "Detect", "Add Criminal"};
        
        for (int i = 0; i < pages.length; i++) {
            final int index = i;
            JButton btn = createNavButton(labels[i]);
            btn.addActionListener(e -> {
                if (currentPanel != null) {
                    Main.switchTo(currentPanel, pages[index]);
                }
            });
            navPanel.add(btn);
        }
        
        add(logo, BorderLayout.WEST);
        add(navPanel, BorderLayout.EAST);
    }
    
    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(BTN_BG);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(BTN_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(BTN_BG);
            }
        });
        
        return btn;
    }
}