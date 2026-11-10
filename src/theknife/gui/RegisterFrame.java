package theknife.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import javax.swing.*;
import theknife.controller.GestioneUtenti;

/**
 * Registration window that writes new users to CSV.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class RegisterFrame extends JFrame {

    /** CSV path for users. */
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

            try {
                    if (GestioneUtenti.usernameEsistente(username)) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Username già registrato!",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                } catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(this, "Impossibile verificare lo username.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            if (GestioneUtenti.salvaUtente(nome, cognome, residenza, username, password, ruolo)) {
                JOptionPane.showMessageDialog(this, "Registrazione completata!\nOra puoi effettuare il login.",
                        "Successo", JOptionPane.INFORMATION_MESSAGE);
                reopenParentOnClose = false;
                dispose();
                new LoginFrame(parent);
            } else {
                JOptionPane.showMessageDialog(this, "Errore nel salvataggio dei dati.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        pack();
        setMinimumSize(new Dimension(400, 350));
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (reopenParentOnClose && parent != null && parent.isDisplayable()) {
                    parent.setVisible(true);
                }
            }
        });
    }
}
