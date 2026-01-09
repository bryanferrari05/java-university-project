package theknife.gui.components;

import java.awt.*;
import javax.swing.*;
import theknife.gui.HomeFrame;
import theknife.gui.LoginFrame;
import theknife.gui.RegisterFrame;
import theknife.utils.Theme;

/**
 * Top navigation bar with menu toggle and user actions.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
@SuppressWarnings("unused")
public class NavbarPanel extends JPanel {

    /** Username displayed in the navbar. */
    private final String username;
    /** Current user role. */
    private final String ruolo;
    /** Sidebar toggle callback. */
    private final Runnable toggleSidebar;

    /**
     * Builds the navbar with user context.
     *
     * @param username username to display
     * @param parent parent frame for navigation
     * @param ruolo current role
     * @param toggleSidebar callback to toggle sidebar
     */
    public NavbarPanel(String username, HomeFrame parent, String ruolo, Runnable toggleSidebar) {
        this.username = username;
        this.ruolo = ruolo;
        this.toggleSidebar = toggleSidebar;

        setLayout(new BorderLayout());
        setBackground(Theme.PRIMARY);
        setPreferredSize(new Dimension(1000, 60));

        // ---- Bottone menu ----
        JButton menuButton = new JButton();
        menuButton.setIcon(loadIcon("/theknife/gui/images/icon-menu.png"));
        menuButton.setBackground(Theme.PRIMARY);
        menuButton.setBorderPainted(false);
        menuButton.setFocusPainted(false);
        menuButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        menuButton.setPreferredSize(new Dimension(60, 60));

        // Hover leggero
        menuButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuButton.setBackground(new Color(25, 45, 80));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuButton.setBackground(Theme.PRIMARY);
            }
        });

        menuButton.addActionListener(e -> toggleSidebar.run());
        add(menuButton, BorderLayout.WEST);

        // ---- Titolo centrale ----
        JLabel title = new JLabel("TheKnife", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.CENTER);

        // ---- Area destra (login/registrati o utente) ----
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightPanel.setBackground(Theme.PRIMARY);

        if (ruolo.equalsIgnoreCase("ospite")) {
            JButton loginButton = creaTopButton("Login");
            JButton registerButton = creaTopButton("Registrati");

            loginButton.addActionListener(e -> {
                parent.dispose();
                new LoginFrame();
            });

            registerButton.addActionListener(e -> {
                parent.dispose();
                new RegisterFrame();
            });

            rightPanel.add(loginButton);
            rightPanel.add(registerButton);
        } else {
            JLabel userLabel = new JLabel(username);
            userLabel.setForeground(Color.WHITE);
            userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            rightPanel.add(userLabel);
        }

        add(rightPanel, BorderLayout.EAST);
    }

    /**
     * Creates a styled top-right action button.
     *
     * @param text label
     * @return configured button
     */
    private JButton creaTopButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(Theme.ACCENT);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    /**
     * Loads a small icon from resources.
     *
     * @param path resource path
     * @return scaled icon
     */
    private ImageIcon loadIcon(String path) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image scaled = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.out.println("Icona non trovata: " + path);
            return new ImageIcon();
        }
    }
}
