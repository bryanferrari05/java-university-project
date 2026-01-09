package theknife.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.*;

/**
 * Registration window that writes new users to CSV.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class RegisterFrame extends JFrame {

    /** CSV path for users. */
    private static final String PATH_UTENTI = "data/utenti.csv";
    /** Parent start frame, if provided. */
    private final StartFrame parent;
    /** Reopen parent when this window closes. */
    private boolean reopenParentOnClose = true;

    /**
     * Builds a registration window without a parent.
     */
    public RegisterFrame() {
        this(null);
    }

    /**
     * Builds a registration window with an optional parent.
     *
     * @param parent start frame to reopen on close
     */
    public RegisterFrame(StartFrame parent) {
        this.parent = parent;

        setTitle("TheKnife - Registrazione");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 5, 5));

        add(new JLabel("Nome:"));
        JTextField nomeField = new JTextField();
        add(nomeField);

        add(new JLabel("Cognome:"));
        JTextField cognomeField = new JTextField();
        add(cognomeField);

        add(new JLabel("Citta di residenza:"));
        JTextField residenzaField = new JTextField();
        add(residenzaField);

        add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Ruolo:"));
        String[] ruoli = {"cliente", "ristoratore"};
        JComboBox<String> ruoloBox = new JComboBox<>(ruoli);
        add(ruoloBox);

        JButton confermaButton = new JButton("Conferma registrazione");
        add(confermaButton);
        add(new JLabel());

        confermaButton.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String residenza = residenzaField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String ruolo = (String) ruoloBox.getSelectedItem();

            if (nome.isEmpty() || cognome.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Compila tutti i campi!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("cliente".equalsIgnoreCase(ruolo) && residenza.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserisci la citta di residenza.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (usernameEsistente(username)) {
                JOptionPane.showMessageDialog(this, "Username già registrato!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (salvaUtente(nome, cognome, residenza, username, password, ruolo)) {
                JOptionPane.showMessageDialog(this, "Registrazione completata!\nOra puoi effettuare il login.",
                        "Successo", JOptionPane.INFORMATION_MESSAGE);
                reopenParentOnClose = false;
                dispose();
                new LoginFrame(parent);
            } else {
                JOptionPane.showMessageDialog(this, "Errore nel salvataggio dei dati.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);

        pack();
        setMinimumSize(new Dimension(400, 350));
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
     * Checks if a username already exists in CSV.
     *
     * @param username username to check
     * @return true if found
     */
    private boolean usernameEsistente(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_UTENTI))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("nome,")) continue;
                String[] campi = line.split(",");
                if (campi.length >= 3 && campi[2].trim().equalsIgnoreCase(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nel controllo username: " + e.getMessage());
        }
        return false;
    }

    /**
     * Appends a new user record to CSV.
     *
     * @param nome name
     * @param cognome surname
     * @param residenza city
     * @param username username
     * @param password raw password
     * @param ruolo role
     * @return true when saved
     */
    private boolean salvaUtente(String nome, String cognome, String residenza, String username, String password, String ruolo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_UTENTI, true))) {
            String hashedPassword = hashPassword(password);

            // ⭐ AGGIUNTA COLONNA PREFERITI VUOTA ("")
            bw.write(String.format("%s,%s,%s,%s,%s,%s,%s%n",
                    nome, cognome, username, hashedPassword, ruolo, residenza, ""));
            return true;

        } catch (IOException e) {
            System.err.println("Errore nella scrittura del file utenti.csv: " + e.getMessage());
            return false;
        }
    }

    /**
     * Hashes a password using SHA-256.
     *
     * @param password raw password
     * @return hash string
     */
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
