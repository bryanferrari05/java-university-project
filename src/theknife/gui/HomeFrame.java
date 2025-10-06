package theknife.gui;

import javax.swing.*;
import java.awt.*;

public class HomeFrame extends JFrame {
    public HomeFrame(String username) {
        setTitle("TheKnife - Home");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel welcome = new JLabel("Benvenuto, " + username + "!", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 20));
        add(welcome, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton searchButton = new JButton("Cerca Ristoranti");
        JButton favoritesButton = new JButton("I miei Preferiti");
        JButton myRestaurantsButton = new JButton("I miei Ristoranti");
        JButton logoutButton = new JButton("Logout");

        buttonsPanel.add(searchButton);
        buttonsPanel.add(favoritesButton);
        buttonsPanel.add(myRestaurantsButton);
        buttonsPanel.add(logoutButton);

        add(buttonsPanel, BorderLayout.CENTER);

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
