package theknife.gui;

import java.awt.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.*;
import theknife.utils.Theme;

public class LoginFrame extends JFrame {

    private static final String PATH_UTENTI = "data/utenti.csv";

    public LoginFrame() {
        setTitle("TheKnife - Login");
        setSize(420, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BACKGROUND);

        // Titolo elegante
        JLabel title = new JLabel("TheKnife", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.ACCENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Pannello campi
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.BACKGROUND);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = Theme.styledButton("Accedi");
        JButton registerButton = Theme.styledButton("Registrati");

        c.gridx = 0; c.gridy = 0; panel.add(new JLabel("Username:"), c);
        c.gridx = 1; panel.add(usernameField, c);
        c.gridx = 0; c.gridy = 1; panel.add(new JLabel("Password:"), c);
        c.gridx = 1; panel.add(passwordField, c);
        c.gridx = 0; c.gridy = 2; panel.add(loginButton, c);
        c.gridx = 1; panel.add(registerButton, c);

        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // --- LOGIN listener ---
        loginButton.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserisci username e password.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (verificaCredenziali(user, pass)) {
                JOptionPane.showMessageDialog(this, "Accesso effettuato!", "Login", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new HomeFrame(user);
            } else {
                JOptionPane.showMessageDialog(this, "Credenziali errate.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- REGISTRAZIONE listener ---
        registerButton.addActionListener(e -> new RegisterFrame());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // 🔍 Verifica le credenziali confrontando l'hash della password
    private boolean verificaCredenziali(String username, String password) {
        String hashedInput = hashPassword(password);

        try (BufferedReader br = new BufferedReader(new FileReader(PATH_UTENTI))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("nome,")) continue; // salta intestazione
                String[] campi = line.split(",");
                if (campi.length >= 5) {
                    String user = campi[2].trim();
                    String storedHash = campi[3].trim();

                    if (user.equals(username) && storedHash.equals(hashedInput)) {
                        return true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File utenti.csv non trovato.\nAssicurati che esista nella cartella /data.", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Errore nella lettura del file utenti.csv:\n" + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    // 🔐 Crea l'hash SHA-256 della password
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore algoritmo SHA-256", e);
        }
    }
}
