package theknife.gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import theknife.gui.components.NavbarPanel;
import theknife.gui.components.SidebarPanel;
import theknife.utils.Theme;

public class HomeFrame extends JFrame {

    private JPanel contentPanel;
    private SidebarPanel sidebar;
    private boolean sidebarVisible = false;
    private String ruolo;

    public HomeFrame(String username, String ruolo){
        this.ruolo = ruolo;
        setTitle("TheKnife - Home");
        setSize(1000,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        NavbarPanel navbar = new NavbarPanel(username, this::toggleSidebar);
        add(navbar, BorderLayout.NORTH);

        sidebar = new SidebarPanel(this.ruolo);
        sidebar.setPreferredSize(new Dimension(0,getHeight()));
        sidebar.setVisible(true);
        add(sidebar, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Theme.BACKGROUND);
        add(contentPanel, BorderLayout.CENTER);

        showPanel(new JLabel("Benvenuto nella tua dashboard, "+username+"!", SwingConstants.CENTER));

        setVisible(true);
    }

    private void toggleSidebar(){
        sidebarVisible = !sidebarVisible;
        int targetWidth = sidebarVisible ? 220 : 0;
        Timer timer = new Timer(10, e -> {
            int currentWidth = sidebar.getPreferredSize().width;
            if(currentWidth == targetWidth){
                ((Timer)e.getSource()).stop();
            } else {
                sidebar.setPreferredSize(new Dimension(currentWidth + (targetWidth>currentWidth?10:-10), getHeight()));
                sidebar.revalidate();
                sidebar.repaint();
            }
        });
        timer.start();
    }

    public void showPanel(JComponent component){
        contentPanel.removeAll();
        contentPanel.add(component, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showPanel(JLabel label){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        showPanel(panel);
    }
}
