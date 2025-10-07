package theknife.gui.components;

import javax.swing.*;
import java.awt.*;
import theknife.utils.Theme;
import theknife.utils.IconUtils;

public class SidebarPanel extends JPanel {
    public SidebarPanel() {
        setLayout(new GridLayout(5, 1, 0, 10));
        setBackground(Theme.PRIMARY);
        setPreferredSize(new Dimension(220, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // icone uniformi 28px; per "Ristoranti" usiamo 34px per compensare PNG con padding
        add(makeButton("Cerca Ristoranti", "/theknife/resources/images/icon-search.png", 28));
        add(makeButton("I miei Preferiti", "/theknife/resources/images/icon-favorites.png", 28));
        add(makeButton("I miei Ristoranti", "/theknife/resources/images/icon-restaurants.png", 34));
        add(makeButton("Recensioni", "/theknife/resources/images/icon-star.png", 28));
        add(makeButton("Logout", "/theknife/resources/images/icon-logout.png", 28));
    }

    private JButton makeButton(String text, String iconPath, int sizePx) {
        JButton b = new JButton(text);
        b.setBackground(Theme.PRIMARY);
        b.setForeground(Color.WHITE);
        b.setFont(Theme.NORMAL_FONT);
        b.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setIconTextGap(14);

        ImageIcon ic = IconUtils.load(iconPath, sizePx);
        if (ic != null) b.setIcon(IconUtils.tint(ic, Color.WHITE));
        else System.out.println("⚠️ Icona mancante: " + iconPath);

        // hover
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { b.setBackground(Theme.ACCENT); }
            public void mouseExited (java.awt.event.MouseEvent evt) { b.setBackground(Theme.PRIMARY); }
        });
        return b;
    }
}
