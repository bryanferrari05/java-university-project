package theknife.gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import theknife.gui.components.NavbarPanel;
import theknife.gui.components.SidebarPanel;
import theknife.gui.components.SectionPanels;

public class HomeFrame extends JFrame {

    private final String ruolo;
    private final String username;
    private final JPanel mainPanel;
    private final SidebarPanel sidebar;
    private boolean sidebarVisible = false; // CHIUSA all’avvio

    public HomeFrame(String ruolo, String username) {
        this.ruolo = ruolo;
        this.username = username;

        setTitle("TheKnife - Home");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Navbar con callback toggle
        NavbarPanel navbar = new NavbarPanel(username, this, ruolo, this::toggleSidebar);
        add(navbar, BorderLayout.NORTH);

        // Sidebar (chiusa all'avvio)
        sidebar = new SidebarPanel(ruolo, this);
        sidebar.setPreferredSize(new Dimension(0, getHeight()));
        sidebar.setVisible(false);
        add(sidebar, BorderLayout.WEST);

        // Area principale
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        showPanel(SectionPanels.creaHomeTopRistoranti());
        add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** Cambia il pannello principale */
    public void showPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /** Apre o chiude la sidebar con animazione */
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
                sidebar.setPreferredSize(new Dimension(currentWidth + step, getHeight()));
                sidebar.revalidate();
            }
        });
        timer.start();
    }
}
