package theknife.gui.components.home;

import java.awt.*;
import javax.swing.*;

import theknife.gui.components.SectionPanels;
import theknife.model.User;

public class HomePanelFactory {

    public static JPanel creaHomeBenvenuto() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        User user = SectionPanels.CURRENT_USER;

        String titolo;
        String sottotitolo;

        if (user == null || user.getRuolo().equalsIgnoreCase("ospite")) {
            titolo = "Benvenuto in TheKnife!";
            sottotitolo = "Registrati o accedi per utilizzare le funzionalità della piattaforma.";
        } else {
            titolo = "Benvenuto in TheKnife " + user.getUsername() + "!";
            sottotitolo = "Spostati nei menu per utilizzare le funzionalità della piattaforma.";
        }

        JLabel titleLabel = new JLabel(titolo, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(230, 57, 70));

        JLabel subtitleLabel = new JLabel(sottotitolo, SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(80, 80, 80));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(80, 40, 80, 40));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(titleLabel);
        center.add(Box.createVerticalStrut(20));
        center.add(subtitleLabel);

        panel.add(center, BorderLayout.CENTER);

        return panel;
    }
}
