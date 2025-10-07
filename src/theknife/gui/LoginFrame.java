package theknife.gui;

import javax.swing.*;
import java.awt.*;
import theknife.utils.FileManager;
import theknife.utils.Theme;

public class LoginFrame extends JFrame {
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

        // LOGIN listener
        loginButton.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            if (FileManager.verificaCredenziali(user, pass)) {
                JOptionPane.showMessageDialog(this, "Accesso effettuato!", "Login", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new HomeFrame(user);
            } else {
                JOptionPane.showMessageDialog(this, "Credenziali errate.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        // REGISTRAZIONE listener
        registerButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funzione di registrazione in arrivo 💡", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
