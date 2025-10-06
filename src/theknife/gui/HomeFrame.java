package theknife.gui;

import javax.swing.*;
import java.awt.*;
import theknife.utils.Theme;

public class HomeFrame extends JFrame {
    public HomeFrame(String username) {
        setTitle("TheKnife - Home");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout());

        JLabel welcome = new JLabel("Benvenuto, " + username + " 👋", SwingConstants.CENTER);
        welcome.setFont(Theme.TITLE_FONT);
        welcome.setForeground(Theme.ACCENT);
        welcome.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(welcome, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonsPanel.setBackground(Theme.BACKGROUND);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        JButton searchButton = Theme.styledButton("🔎 Cerca Ristoranti");
        JButton favoritesButton = Theme.styledButton("❤️ I miei Preferiti");
        JButton myRestaurantsButton = Theme.styledButton("🍽️ I miei Ristoranti");
        JButton logoutButton = Theme.styledButton("🚪 Logout");

        buttonsPanel.add(searchButton);
        buttonsPanel.add(favoritesButton);
        buttonsPanel.add(myRestaurantsButton);
        buttonsPanel.add(logoutButton);

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        add(buttonsPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
