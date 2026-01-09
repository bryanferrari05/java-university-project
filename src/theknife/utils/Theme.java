package theknife.utils;

import java.awt.*;
import javax.swing.*; // 🔥 serve per JButton e BorderFactory

/**
 * Shared colors and fonts for the UI.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class Theme {

    /**
     * Creates a new Theme instance.
     */
    public Theme() {
    }
    // Colori principali
    /** Background color. */
    public static final Color BACKGROUND = new Color(245, 245, 245);
    /** Primary accent color. */
    public static final Color PRIMARY = new Color(230, 57, 70);      // rosso elegante
    /** Secondary accent color. */
    public static final Color ACCENT = new Color(29, 53, 87);        // blu profondo
    /** Primary text color. */
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    /** Secondary text color. */
    public static final Color TEXT_SECONDARY = new Color(80, 80, 80);

    // Font base
    /** Title font. */
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    /** Standard body font. */
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 15);

    // Metodo helper per stilizzare un JButton
    /**
     * Applies the base theme to a button.
     *
     * @param button button to style
     */
    public static void styleButton(Button button) {
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(NORMAL_FONT);
    }

    /**
     * Creates a themed button instance.
     *
     * @param text button label
     * @return themed button
     */
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
