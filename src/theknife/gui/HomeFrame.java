package theknife.gui;

import javax.swing.*;
import java.awt.*;

import theknife.gui.components.NavbarPanel;
import theknife.gui.components.SidebarPanel;
import theknife.gui.components.SectionPanels;
import theknife.model.User;

@SuppressWarnings("unused")
public class HomeFrame extends JFrame {

    private final String ruolo;
    private final String username;
    private final User user;

    private final JPanel mainPanel;
    private final SidebarPanel sidebar;
    private boolean sidebarVisible = false;

    // ⭐ NUOVO COSTRUTTORE: riceve l'oggetto User completo
    public HomeFrame(User user) {

        this.user = user;                // ⭐ Manteniamo l’utente completo
        this.ruolo = user.getRuolo();    // estraiamo ruolo
        this.username = user.getUsername(); // estraiamo username

        setTitle("TheKnife");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ⭐ PASSIAMO ruolo e username come prima → nessun altro codice da modificare
        SectionPanels.setSession(this.user, this::showPanel);


        NavbarPanel navbar = new NavbarPanel(username, this, ruolo, this::toggleSidebar);
        add(navbar, BorderLayout.NORTH);

        sidebar = new SidebarPanel(ruolo, this);
        sidebar.setPreferredSize(new Dimension(0, getHeight()));
        sidebar.setVisible(false);
        add(sidebar, BorderLayout.WEST);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        showPanel(SectionPanels.creaHomeTopRistoranti());
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int maxWidth = (int) (screen.width * 0.9);
        int maxHeight = (int) (screen.height * 0.9);

        setMinimumSize(new Dimension(900, 550));
        setPreferredSize(new Dimension(maxWidth, maxHeight));

        pack();
        setLocationRelativeTo(null);
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
                sidebar.setPreferredSize(new Dimension(currentWidth + step, getHeight()));
                sidebar.revalidate();
            }
        });
        timer.start();
    }

    // ⭐ Getter per l’utente (ti servirà dopo nella GUI Preferiti)
    public User getUser() {
        return user;
    }
}
