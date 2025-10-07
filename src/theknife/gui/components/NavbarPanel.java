package theknife.gui.components;

import javax.swing.*;
import java.awt.*;
import theknife.utils.Theme;

public class NavbarPanel extends JPanel {
    public NavbarPanel(String username, Runnable onMenuClick) {
        setLayout(new BorderLayout());
        setBackground(Theme.ACCENT);
        setPreferredSize(new Dimension(0, 60));

        // Carica il logo
        ImageIcon logoIcon = null;
        try {
            logoIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
        } catch (Exception e) {
            System.out.println(" Logo non trovato, controlla la cartella resources/images/");
        }

        // Pulsante menu
        JButton menuButton = new JButton("☰");
        menuButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        menuButton.setBackground(Theme.ACCENT);
        menuButton.setForeground(Color.WHITE);
        menuButton.setFocusPainted(false);
        menuButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        menuButton.addActionListener(e -> onMenuClick.run());

        // Logo + titolo
        JLabel title;
        if (logoIcon != null) {
            logoIcon.setImage(logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            title = new JLabel("  TheKnife", logoIcon, SwingConstants.CENTER);
        } else {
            title = new JLabel("TheKnife", SwingConstants.CENTER);
        }
        title.setForeground(Color.WHITE);
        title.setFont(Theme.TITLE_FONT);

        // Username a destra
        JLabel userLabel = new JLabel(username + "  ", SwingConstants.RIGHT);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(Theme.NORMAL_FONT);

        add(menuButton, BorderLayout.WEST);
        add(title, BorderLayout.CENTER);
        add(userLabel, BorderLayout.EAST);
    }
}