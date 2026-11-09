package theknife.gui;

import javax.swing.*;
import java.awt.*;

import theknife.gui.components.NavbarPanel;
import theknife.gui.components.SidebarPanel;
import theknife.gui.components.SectionPanels;
import theknife.model.User;

/**
 * Main application window with navigation and content panels.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class HomeFrame extends JFrame {

    /** Current logged-in user. */
    private final User user;
    /** Main content container. */
    private final JPanel mainPanel;
    /** Sidebar navigation panel. */
    private final SidebarPanel sidebar;
    /** Sidebar visibility state. */
    private boolean sidebarVisible = false;

    /**
     * Returns the active user instance.
     *
     * @return current user
     */
    public User getCurrentUser() {
        return user;
    }


    /**
     * Builds the main window for a user session.
     *
     * @param user current user
     */
    public HomeFrame(User user) {
        this.user = user;

        setTitle("TheKnife");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ⭐ Passiamo la sessione con il vero oggetto User
        SectionPanels.setSession(user, this::showPanel);

        // ⭐ Navbar: usa username e ruolo reali
        NavbarPanel navbar = new NavbarPanel(
                user.getUsername(),   // username per display
                this,
                user.getRuolo(),      // ruolo dell’utente
                this::toggleSidebar
        );
        add(navbar, BorderLayout.NORTH);

        sidebar = new SidebarPanel(user.getRuolo(), this);
        sidebar.setPreferredSize(new Dimension(0, getHeight()));
        sidebar.setVisible(false);
        add(sidebar, BorderLayout.WEST);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ⭐ NUOVA HOME DI BENVENUTO
        showPanel(SectionPanels.creaHomeBenvenuto());

        add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Replaces the main content panel.
     *
     * @param panel new content
     */
    public void showPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Animates sidebar opening and closing.
     */
    private void toggleSidebar() {
        sidebarVisible = !sidebarVisible;
        sidebar.setVisible(true);
        int targetWidth = sidebarVisible ? 220 : 0;

        Timer timer = new Timer(5, e -> {
            int currentWidth = sidebar.getPreferredSize().width;
            if (currentWidth == targetWidth) {
                ((Timer) e.getSource()).stop();
                if (!sidebarVisible) sidebar.setVisible(false);
            } else {
                int step = (targetWidth > currentWidth) ? 10 : -10;
                sidebar.setPreferredSize(
                        new Dimension(currentWidth + step, getHeight())
                );
                sidebar.revalidate();
            }
        });
        timer.start();
    }
}
