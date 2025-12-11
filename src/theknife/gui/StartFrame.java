package theknife.gui;

import java.awt.*;
import javax.swing.*;
import theknife.utils.Theme;

public class StartFrame extends JFrame {

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
            new LoginFrame();
        });

        registerBtn.addActionListener(e -> {
            // apre la finestra di registrazione (che, una volta completata, rimanda al login)
            new RegisterFrame();
        });

        guestBtn.addActionListener(e -> {
            // entra in modalità ospite direttamente nell'Home
            dispose();
            new HomeFrame("ospite", "Ospite");
        });

        c.gridy = 0; buttons.add(loginBtn, c);
        c.gridy = 1; buttons.add(registerBtn, c);
        c.gridy = 2; buttons.add(guestBtn, c);

        add(buttons, BorderLayout.SOUTH);

        setVisible(true);

        pack();                       
        setMinimumSize(new Dimension(520, 360));
        setLocationRelativeTo(null);
    }
}
