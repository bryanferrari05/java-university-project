package theknife.gui;

import javax.swing.*;
import java.awt.*;
import theknife.utils.FileManager;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("TheKnife - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Benvenuto su TheKnife!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // LOGIN BUTTON
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

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
