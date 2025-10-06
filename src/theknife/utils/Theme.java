package theknife.utils;

import java.awt.*;
import javax.swing.*; // 🔥 serve per JButton e BorderFactory

public class Theme {
    // Colori principali
    public static final Color BACKGROUND = new Color(245, 245, 245);
    public static final Color PRIMARY = new Color(230, 57, 70);      // rosso elegante
    public static final Color ACCENT = new Color(29, 53, 87);        // blu profondo
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    public static final Color TEXT_SECONDARY = new Color(80, 80, 80);

    // Font base
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 15);

    // Metodo helper per stilizzare un JButton
    public static void styleButton(Button button) {
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(NORMAL_FONT);
    }

    public static JButton styledButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(PRIMARY);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(NORMAL_FONT);
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return b;
    }
}
