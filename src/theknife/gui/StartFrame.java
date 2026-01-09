package theknife.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import theknife.model.User;
import theknife.utils.Theme;

/**
 * Initial access screen with login, register, or guest flow.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class StartFrame extends JFrame {

    /**
     * Builds the start window and hooks the actions.
     */
    public StartFrame() {
        setTitle("TheKnife");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background
        getContentPane().setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout());

        // Titolo
        JLabel title = new JLabel("TheKnife", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.ACCENT);
        title.setBorder(BorderFactory.createEmptyBorder(24, 0, 8, 0));
        add(title, BorderLayout.NORTH);

        // Sottotitolo
        JLabel subtitle = new JLabel("Scegli come vuoi accedere", SwingConstants.CENTER);
        subtitle.setFont(Theme.NORMAL_FONT);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        add(subtitle, BorderLayout.CENTER);

        // Pannello pulsanti
        JPanel buttons = new JPanel(new GridBagLayout());
        buttons.setBackground(Theme.BACKGROUND);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;

        JButton loginBtn = Theme.styledButton("Accedi");
        JButton registerBtn = Theme.styledButton("Registrati");
        JButton guestBtn = Theme.styledButton("Continua come Ospite");

        loginBtn.addActionListener(e -> {
            // apre la finestra di login (che al successo apre HomeFrame)
            setVisible(false);
            new LoginFrame(this);
        });

        registerBtn.addActionListener(e -> {
            // apre la finestra di registrazione (che, una volta completata, rimanda al login)
            setVisible(false);
            new RegisterFrame(this);
        });

        guestBtn.addActionListener(e -> {
            String city = JOptionPane.showInputDialog(
                    this,
                    "Inserisci la tua citta (es. Milan):",
                    "Accesso come Ospite",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (city == null) {
                return;
            }

            city = city.trim();
            if (city.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Inserisci una citta valida per continuare.",
                        "Accesso come Ospite",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            dispose();

            // Creiamo un utente fittizio "Ospite"
            User guest = new User(
                    "Ospite",         // username
                    "",               // password fittizia
                    "Ospite",         // nome
                    "",               // cognome
                    "ospite",         // ruolo
                    city              // residenza
            );

            new HomeFrame(guest);
        });

        c.gridy = 0;
        buttons.add(loginBtn, c);
        c.gridy = 1;
        buttons.add(registerBtn, c);
        c.gridy = 2;
        buttons.add(guestBtn, c);

        add(buttons, BorderLayout.SOUTH);

        setVisible(true);

        pack();
        setMinimumSize(new Dimension(520, 360));
        setLocationRelativeTo(null);
    }
}
