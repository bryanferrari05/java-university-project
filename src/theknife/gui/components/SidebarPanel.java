package theknife.gui.components;

import javax.swing.*;
import java.awt.*;
import theknife.utils.Theme;

public class SidebarPanel extends JPanel {
    public SidebarPanel() {
        setLayout(new GridLayout(5, 1, 0, 10));
        setBackground(Theme.PRIMARY);
        setPreferredSize(new Dimension(200, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        add(makeButton("Cerca Ristoranti", "/images/icon-search.png"));
        add(makeButton("I miei Preferiti", "/images/icon-favorites.png"));
        add(makeButton("I miei Ristoranti", "/images/icon-restaurants.png"));
        add(makeButton("Recensioni", "/images/icon-star.png"));
        add(makeButton("Logout", "/images/icon-logout.png"));
    }

    private JButton makeButton(String text, String iconPath) {
        JButton b = new JButton(text);
        b.setBackground(Theme.PRIMARY);
        b.setForeground(Color.WHITE);
        b.setFont(Theme.NORMAL_FONT);
        b.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Carica icona se presente
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            icon.setImage(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            b.setIcon(icon);
        } catch (Exception e) {
            System.out.println("Icona non trovata: " + iconPath);
        }

        // Effetto hover
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(Theme.ACCENT);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(Theme.PRIMARY);
            }
        });

        return b;
    }
}