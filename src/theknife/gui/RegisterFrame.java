package theknife.gui;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    public RegisterFrame() {
        setTitle("TheKnife - Registrazione");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(7, 2, 5, 5));

        add(new JLabel("Nome:"));
        JTextField nomeField = new JTextField();
        add(nomeField);

        add(new JLabel("Cognome:"));
        JTextField cognomeField = new JTextField();
        add(cognomeField);

        add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Ruolo:"));
        String[] ruoli = {"Cliente", "Ristoratore"};
        JComboBox<String> ruoloBox = new JComboBox<>(ruoli);
        add(ruoloBox);

        JButton confermaButton = new JButton("Conferma registrazione");
        add(confermaButton);
        add(new JLabel()); // spazio

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
