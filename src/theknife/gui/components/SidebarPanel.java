package theknife.gui.components;

import javax.swing.*;
import java.awt.*;
import theknife.utils.Theme;
import theknife.utils.IconUtils;
import theknife.gui.HomeFrame;
import theknife.gui.LoginFrame;

public class SidebarPanel extends JPanel {

    public SidebarPanel(String ruolo){
        setLayout(new GridLayout(5,1,0,10));
        setBackground(Theme.PRIMARY);
        setPreferredSize(new Dimension(220,0));
        setBorder(BorderFactory.createEmptyBorder(20,15,20,15));

        add(makeButton("Cerca Ristoranti","/theknife/resources/images/icon-search.png",28, ruolo));
        add(makeButton("I miei Preferiti","/theknife/resources/images/icon-favorites.png",28, ruolo));

        if("ristoratore".equalsIgnoreCase(ruolo)){
            add(makeButton("I miei Ristoranti","/theknife/resources/images/icon-restaurants.png",34, ruolo));
        }

        add(makeButton("Recensioni","/theknife/resources/images/icon-star.png",28, ruolo));

        add(makeLogoutButton("Logout","/theknife/resources/images/icon-logout.png",28));
    }

    private JButton makeButton(String text, String iconPath, int sizePx, String ruolo){
        JButton b = new JButton(text);
        b.setBackground(Theme.PRIMARY);
        b.setForeground(Color.WHITE);
        b.setFont(Theme.NORMAL_FONT);
        b.setBorder(BorderFactory.createEmptyBorder(10,12,10,12));
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setIconTextGap(14);

        ImageIcon ic = IconUtils.load(iconPath, sizePx);
        if(ic!=null) b.setIcon(IconUtils.tint(ic, Color.WHITE));

        b.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(b);
            HomeFrame home = (HomeFrame) topFrame;

            if(text.equals("Recensioni")){
                if(ruolo.equalsIgnoreCase("cliente")){
                    home.showPanel(SectionPanels.creaRecensioniCliente());
                } else if(ruolo.equalsIgnoreCase("ristoratore")){
                    home.showPanel(SectionPanels.creaRecensioniRistoratore());
                } else {
                    home.showPanel(SectionPanels.creaRecensioni());
                }
                return;
            }

            switch(text){
                case "Cerca Ristoranti":
                    home.showPanel(SectionPanels.creaCercaRistoranti());
                    break;
                case "I miei Preferiti":
                    home.showPanel(SectionPanels.creaMieiPreferiti());
                    break;
                case "I miei Ristoranti":
                    home.showPanel(SectionPanels.creaMieiRistoranti());
                    break;
            }
        });

        b.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt){ b.setBackground(Theme.ACCENT); }
            public void mouseExited(java.awt.event.MouseEvent evt){ b.setBackground(Theme.PRIMARY); }
        });

        return b;
    }

    private JButton makeLogoutButton(String text, String iconPath, int sizePx){
        JButton b = new JButton(text);
        b.setBackground(Theme.PRIMARY);
        b.setForeground(Color.WHITE);
        b.setFont(Theme.NORMAL_FONT);
        b.setBorder(BorderFactory.createEmptyBorder(10,12,10,12));
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setIconTextGap(14);

        ImageIcon ic = IconUtils.load(iconPath, sizePx);
        if(ic!=null) b.setIcon(IconUtils.tint(ic, Color.WHITE));

        b.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(b);
            topFrame.dispose();
            new LoginFrame();
        });

        b.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt){ b.setBackground(Theme.ACCENT); }
            public void mouseExited(java.awt.event.MouseEvent evt){ b.setBackground(Theme.PRIMARY); }
        });

        return b;
    }
}
