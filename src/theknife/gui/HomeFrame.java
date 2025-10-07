package theknife.gui;

import javax.swing.*;
import java.awt.*;
import theknife.gui.components.NavbarPanel;
import theknife.gui.components.SidebarPanel;
import theknife.utils.Theme;

public class HomeFrame extends JFrame {
    private JPanel contentPanel;
    private SidebarPanel sidebar;
    private boolean sidebarVisible = false;

    public HomeFrame(String username) {
        setTitle("TheKnife - Home");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout());

        // Navbar superiore
        NavbarPanel navbar = new NavbarPanel(username, this::toggleSidebar);
        add(navbar, BorderLayout.NORTH);

        // Sidebar nascosta inizialmente
        sidebar = new SidebarPanel();
        sidebar.setVisible(false);
        add(sidebar, BorderLayout.WEST);

        // Pannello contenuti centrale
        contentPanel = new JPanel();
        contentPanel.setBackground(Theme.BACKGROUND);
        contentPanel.add(new JLabel("🏠 Benvenuto nella tua dashboard, " + username + "!"));
        add(contentPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void toggleSidebar() {
        sidebarVisible = !sidebarVisible;
        sidebar.setVisible(sidebarVisible);
        revalidate();
        repaint();
    }
}
