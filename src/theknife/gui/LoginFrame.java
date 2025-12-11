package theknife.gui;

import java.awt.*;
import java.io.*;
import java.security.MessageDigest;
import javax.swing.*;

import theknife.model.User;
import theknife.utils.Theme;

public class LoginFrame extends JFrame {

    private static final String PATH_UTENTI = "data/utenti.csv";

    public LoginFrame() {
        setTitle("TheKnife - Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BACKGROUND);

        JLabel title = new JLabel("TheKnife", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.ACCENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.BACKGROUND);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton cancelButton = Theme.styledButton("Annulla");
        JButton loginButton = Theme.styledButton("Accedi");

        c.gridx = 0; c.gridy = 0; panel.add(new JLabel("Username:"), c);
        c.gridx = 1; panel.add(usernameField, c);
        c.gridx = 0; c.gridy = 1; panel.add(new JLabel("Password:"), c);
        c.gridx = 1; panel.add(passwordField, c);
        c.gridx = 0; c.gridy = 2; panel.add(cancelButton, c);
        c.gridx = 1; panel.add(loginButton, c);

        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        cancelButton.addActionListener(e -> dispose());

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserisci username e password.");
                return;
            }

            User user = verificaCredenziali(username, password);

            if (user != null) {
                dispose();
                new HomeFrame(user); // ⭐ PASSIAMO L'UTENTE COMPLETO
            } else {
                JOptionPane.showMessageDialog(this, "Credenziali errate.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);

        pack();
        setMinimumSize(new Dimension(420, 350));
        setLocationRelativeTo(null);
    }

    /**
     * Verifica username + password e, se sono giusti,
     * CARICA l'oggetto User COMPLETO (con preferiti inclusi)
     */
    private User verificaCredenziali(String username, String password) {
        String hash = hashPassword(password);

        try (BufferedReader br = new BufferedReader(new FileReader(PATH_UTENTI))) {
            String line;

            while ((line = br.readLine()) != null) {

                // salta intestazione CSV
                if (line.startsWith("nome,")) continue;

                String[] campi = line.split(",");

                if (campi.length >= 5) {
                    String csvNome     = campi[0].trim();
                    String csvCognome  = campi[1].trim();
                    String csvUsername = campi[2].trim();
                    String csvHash     = campi[3].trim();
                    String csvRuolo    = campi[4].trim();

                    if (csvUsername.equals(username) && csvHash.equals(hash)) {

                        // ⭐ CREA L'UTENTE COMPLETO
                        User u = new User(csvUsername, csvHash, csvNome, csvCognome, csvRuolo);

                        // ⭐ CARICA I PREFERITI (colonna 6)
                        if (campi.length >= 6 && !campi[5].trim().isEmpty()) {
                            String[] pref = campi[5].trim().split("-");
                            for (String p : pref) {
                                try {
                                    u.aggiungiPreferito(Integer.parseInt(p));
                                } catch (NumberFormatException ignored) {}
                            }
                        }

                        return u;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Errore lettura utenti.csv");
        }

        return null;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }
}
