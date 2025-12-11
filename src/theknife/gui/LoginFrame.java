package theknife.gui;

import java.awt.*;
import java.io.*;
import java.security.MessageDigest;
import javax.swing.*;
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
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserisci username e password.");
                return;
            }

            String ruolo = verificaCredenziali(user, pass);
            if (ruolo != null) {
                dispose();
                new HomeFrame(ruolo, user);
            } else {
                JOptionPane.showMessageDialog(this, "Credenziali errate.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);

        pack();
        setMinimumSize(new Dimension(420, 350)); // se vuoi mantenerla più o meno così
        setLocationRelativeTo(null);

    }

    private String verificaCredenziali(String username, String password) {
        String hash = hashPassword(password);
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_UTENTI))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("nome,")) continue;
                String[] campi = line.split(",");
                if (campi.length >= 5) {
                    String user = campi[2].trim();
                    String storedHash = campi[3].trim();
                    String ruolo = campi[4].trim();
                    if (user.equals(username) && storedHash.equals(hash))
                        return ruolo;
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
