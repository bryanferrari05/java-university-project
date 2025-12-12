package theknife.gui;

import javax.swing.*;
import java.awt.*;

import theknife.gui.components.NavbarPanel;
import theknife.gui.components.SidebarPanel;
import theknife.gui.components.SectionPanels;
import theknife.model.User;

public class HomeFrame extends JFrame {

    private final User user;
    private final JPanel mainPanel;
    private final SidebarPanel sidebar;
    private boolean sidebarVisible = false;

    public User getCurrentUser() {
        return user;
    }


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

    public void showPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

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
