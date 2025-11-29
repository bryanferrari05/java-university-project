package theknife.gui.components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import theknife.gui.HomeFrame;
import theknife.gui.StartFrame;

public class SidebarPanel extends JPanel {

    private final HomeFrame parent;
    private final String ruolo;
    private JLabel selectedLabel;

    public SidebarPanel(String ruolo, HomeFrame parent) {
        this.ruolo = ruolo;
        this.parent = parent;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(230, 57, 70));
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        addMenuItem("Home", "icon-home.png");

        if (ruolo.equalsIgnoreCase("ospite")) {
            addMenuItem("Cerca Ristoranti", "icon-search.png");
            addMenuItem("Torna al Menu", "icon-logout.png");
        } 
        else if (ruolo.equalsIgnoreCase("cliente")) {
            addMenuItem("Cerca Ristoranti", "icon-search.png");
            addMenuItem("I miei Preferiti", "icon-favorites.png");
            addMenuItem("Recensioni", "icon-star.png");
            addMenuItem("Logout", "icon-logout.png");
        } 
        else if (ruolo.equalsIgnoreCase("ristoratore")) {
            addMenuItem("Cerca Ristoranti", "icon-search.png");
            addMenuItem("I miei Ristoranti", "icon-favorites.png");
            addMenuItem("Recensioni", "icon-star.png");
            addMenuItem("Logout", "icon-logout.png");
        }
    }

    private void addMenuItem(String text, String iconFile) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        itemPanel.setBackground(new Color(230, 57, 70));
        itemPanel.setMaximumSize(new Dimension(250, 50));

        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setIcon(loadIcon(iconFile));
        label.setIconTextGap(10);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                itemPanel.setBackground(new Color(200, 47, 60));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (label != selectedLabel)
                    itemPanel.setBackground(new Color(230, 57, 70));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                selectLabel(label, itemPanel);
                gestisciClick(text);
            }
        });

        itemPanel.add(label);
        add(itemPanel);
        add(Box.createVerticalStrut(10));
    }

    private void selectLabel(JLabel label, JPanel panel) {
        if (selectedLabel != null) {
            selectedLabel.getParent().setBackground(new Color(230, 57, 70));
        }
        selectedLabel = label;
        panel.setBackground(new Color(180, 40, 50));
    }

    private void gestisciClick(String text) {
        if (parent == null) return;

        switch (text) {
            case "Home" -> parent.showPanel(SectionPanels.creaHomeTopRistoranti());
            case "Cerca Ristoranti" -> parent.showPanel(SectionPanels.creaCercaRistoranti());
            case "I miei Preferiti" -> parent.showPanel(SectionPanels.creaMieiPreferiti());
            case "I miei Ristoranti" -> parent.showPanel(SectionPanels.creaMieiRistoranti());
            case "Recensioni" -> {
                if (ruolo.equalsIgnoreCase("ristoratore"))
                    parent.showPanel(SectionPanels.creaRecensioniRistoratore());
                else
                    parent.showPanel(SectionPanels.creaRecensioniCliente());
            }
            case "Torna al Menu" -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Vuoi tornare al menu iniziale?",
                        "Conferma",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    SwingUtilities.getWindowAncestor(this).dispose();
                    new StartFrame();
                }
            }
            case "Logout" -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Vuoi davvero uscire?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    SwingUtilities.getWindowAncestor(this).dispose();
                    new StartFrame();
                }
            }
        }
    }

    private ImageIcon loadIcon(String fileName) {
        try {
            java.net.URL url = getClass().getResource("/theknife/gui/images/" + fileName);
            if (url == null) {
                System.out.println("Icona non trovata: " + fileName);
                return new ImageIcon();
            }
            ImageIcon icon = new ImageIcon(url);
            Image scaled = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.out.println("Errore nel caricamento icona: " + fileName);
            return new ImageIcon();
        }
    }
}