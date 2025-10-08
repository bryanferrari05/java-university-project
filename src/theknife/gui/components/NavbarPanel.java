package theknife.gui.components;

import javax.swing.*;
import java.awt.*;
import theknife.utils.Theme;
import theknife.utils.IconUtils;

public class NavbarPanel extends JPanel {
    public NavbarPanel(String username, Runnable onMenuClick) {
        setLayout(new BorderLayout());
        setBackground(Theme.ACCENT);
        setPreferredSize(new Dimension(0, 60));

        // Pulsante menu con icona
        JButton menuButton = new JButton();
        ImageIcon menuIc = IconUtils.load("/theknife/resources/images/icon-menu.png", 22);
        if (menuIc != null) {
            menuButton.setIcon(IconUtils.tint(menuIc, Color.WHITE));
        } else {
            menuButton.setText("≡"); // fallback
        }
        menuButton.setBackground(Theme.ACCENT);
        menuButton.setForeground(Color.WHITE);
        menuButton.setFocusPainted(false);
        menuButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        menuButton.addActionListener(e -> onMenuClick.run()); // Chiama il metodo toggle

        // Logo + titolo
        JLabel title;
        ImageIcon logoIc = IconUtils.load("/theknife/resources/images/logo.png", 28);
        if (logoIc != null) {
            title = new JLabel("  TheKnife", IconUtils.tint(logoIc, Color.WHITE), SwingConstants.CENTER);
        } else {
            title = new JLabel("TheKnife", SwingConstants.CENTER);
        }
        title.setForeground(Color.WHITE);
        title.setFont(Theme.TITLE_FONT);

        JLabel userLabel = new JLabel(username + "  ", SwingConstants.RIGHT);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(Theme.NORMAL_FONT);

        add(menuButton, BorderLayout.WEST);
        add(title, BorderLayout.CENTER);
        add(userLabel, BorderLayout.EAST);
    }
}
