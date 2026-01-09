package theknife.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.security.MessageDigest;
import javax.swing.*;

import theknife.model.User;
import theknife.utils.Theme;

/**
 * Login window that validates credentials and opens HomeFrame.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class LoginFrame extends JFrame {

    /** CSV path for users. */
    private static final String PATH_UTENTI = "data/utenti.csv";
    /** Parent start frame, if provided. */
    private final StartFrame parent;
    /** Reopen parent when this window closes. */
    private boolean reopenParentOnClose = true;

    /**
     * Builds a login window without a parent.
     */
    public LoginFrame() {
        this(null);
    }

    /**
     * Builds a login window with an optional parent.
     *
     * @param parent start frame to reopen on close
     */
    public LoginFrame(StartFrame parent) {
        this.parent = parent;

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
                reopenParentOnClose = false;
                if (parent != null) {
                    parent.dispose();
                }
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (reopenParentOnClose && parent != null && parent.isDisplayable()) {
                    parent.setVisible(true);
                }
            }
        });
    }

    /**
     * Validates credentials and loads the full user object.
     *
     * @param username username
     * @param password raw password
     * @return user instance or null
     */
    private User verificaCredenziali(String username, String password) {
        String hash = hashPassword(password);

        try (BufferedReader br = new BufferedReader(new FileReader(PATH_UTENTI))) {
            String line;

            while ((line = br.readLine()) != null) {

                // salta intestazione CSV
                if (line.startsWith("nome,")) continue;

                String[] campi = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (campi.length >= 5) {
                    String csvNome     = campi[0].trim();
                    String csvCognome  = campi[1].trim();
                    String csvUsername = campi[2].trim();
                    String csvHash     = campi[3].trim();
                    String csvRuolo    = campi[4].trim();

                    String csvResidenza = "";
                    String preferitiRaw = "";
                    if (campi.length >= 7) {
                        csvResidenza = campi[5].trim();
                        preferitiRaw = campi[6].trim();
                    } else if (campi.length >= 6) {
                        csvResidenza = campi[5].trim();
                    }

                    if (csvUsername.equals(username) && csvHash.equals(hash)) {

                        // ⭐ CREA L'UTENTE COMPLETO
                        User u = new User(csvUsername, csvHash, csvNome, csvCognome, csvRuolo, csvResidenza);

                        // ⭐ CARICA I PREFERITI (colonna 6)
                        if (!preferitiRaw.isEmpty()) {
                            String[] pref = preferitiRaw.split("-");
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

    /**
     * Hashes a password with SHA-256 when available.
     *
     * @param password raw password
     * @return hash string or raw password on error
     */
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
