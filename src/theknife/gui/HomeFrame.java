package theknife.gui;

import javax.swing.*;
import java.awt.*;
import theknife.gui.components.NavbarPanel;
import theknife.gui.components.SidebarPanel;
import theknife.utils.Theme;

public class HomeFrame extends JFrame {

    private JPanel contentPanel;
    private SidebarPanel sidebar;
    private boolean sidebarVisible = false;  // Stato della sidebar

    public HomeFrame(String username) {
        setTitle("TheKnife - Home");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Navbar
        NavbarPanel navbar = new NavbarPanel(username, this::toggleSidebar);
        add(navbar, BorderLayout.NORTH);

        // Sidebar
        sidebar = new SidebarPanel();
        sidebar.setPreferredSize(new Dimension(0, getHeight()));  // Larghezza iniziale 0
        sidebar.setVisible(true); // Assicurati che la sidebar sia visibile quando viene creata
        add(sidebar, BorderLayout.WEST);

        // Content centrale
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Theme.BACKGROUND);
        add(contentPanel, BorderLayout.CENTER);

        // Pannello di default
        showPanel(new JLabel("Benvenuto nella tua dashboard, " + username + "!", SwingConstants.CENTER));

        setVisible(true);
    }

    // Metodo che toggla la visibilità della sidebar con animazione
    private void toggleSidebar() {
        sidebarVisible = !sidebarVisible;  // Inverte lo stato della sidebar
        int targetWidth = sidebarVisible ? 220 : 0;  // Imposta la larghezza della sidebar

        // Creiamo un Timer per animare la larghezza della sidebar
        Timer timer = new Timer(10, e -> {
            int currentWidth = sidebar.getPreferredSize().width;
            if (currentWidth == targetWidth) {
                ((Timer) e.getSource()).stop();  // Ferma l'animazione quando raggiunge la larghezza target
            } else {
                // Incrementiamo o decrementiamo la larghezza
                sidebar.setPreferredSize(new Dimension(currentWidth + (targetWidth > currentWidth ? 10 : -10), getHeight()));
                sidebar.revalidate();
                sidebar.repaint();
            }
        });
        timer.start();  // Avvia l'animazione
    }

    // Metodo che aggiunge un pannello (JComponent) alla finestra centrale
    public void showPanel(JComponent component) {
        contentPanel.removeAll();
        contentPanel.add(component, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Metodo che accetta solo JLabel (convenzione alternativa)
    public void showPanel(JLabel label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        showPanel(panel);  // Ora passa il JPanel a showPanel
    }
}
