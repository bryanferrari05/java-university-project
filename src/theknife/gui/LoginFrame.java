package theknife.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.security.MessageDigest;
import javax.swing.*;

import theknife.model.User;
import theknife.utils.Theme;
import theknife.controller.GestioneUtenti;

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

            User user;

            try {
                user = GestioneUtenti.verificaCredenziali(
                    username, password
                );
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Errore lettura utenti.csv",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

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

        pack();
        setMinimumSize(new Dimension(420, 350));
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
