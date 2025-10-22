package theknife.gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class SectionPanels {

    public static JPanel creaHomeTopRistoranti() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("🍷 Ristoranti più votati", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(230, 57, 70));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(0, 2, 20, 20));
        content.setBackground(Color.WHITE);

        String[][] fake = {
                {"Osteria del Borgo", "Roma", "Italiana", "50€", "4.9⭐"},
                {"Sakura Sushi", "Milano", "Giapponese", "40€", "4.8⭐"},
                {"El Gaucho", "Torino", "Argentina", "60€", "4.7⭐"},
                {"Taverna Blu", "Napoli", "Pesce", "45€", "4.6⭐"}
        };

        for (String[] r : fake)
            content.add(creaCardRistorante(r[0], r[1], r[2], r[3], r[4]));

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static JPanel creaCercaRistoranti() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Cerca Ristoranti 🍽️", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(230, 57, 70));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel filters = new JPanel(new GridBagLayout());
        filters.setBackground(Color.WHITE);
        filters.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 10, 8, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;

        JTextField searchField = new JTextField(15);
        JTextField cittaField = new JTextField(10);
        JTextField tipoField = new JTextField(10);
        JComboBox<String> fasciaPrezzo = new JComboBox<>(new String[]{"Qualsiasi", "<20€", "20€–50€", "50€–100€", ">100€"});
        JComboBox<String> stelleMin = new JComboBox<>(new String[]{"Qualsiasi", "≥1⭐", "≥2⭐", "≥3⭐", "≥4⭐", "5⭐"});
        JButton cercaBtn = new JButton("Cerca 🔍");
        cercaBtn.setBackground(new Color(230, 57, 70));
        cercaBtn.setForeground(Color.WHITE);
        cercaBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cercaBtn.setFocusPainted(false);

        c.gridx = 0; c.gridy = 0; filters.add(new JLabel("Nome o parola chiave:"), c);
        c.gridx = 1; c.weightx = 1; filters.add(searchField, c);
        c.gridx = 2; c.weightx = 0; filters.add(new JLabel("Città:"), c);
        c.gridx = 3; c.weightx = 1; filters.add(cittaField, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0; filters.add(new JLabel("Cucina:"), c);
        c.gridx = 1; c.weightx = 1; filters.add(tipoField, c);
        c.gridx = 2; c.weightx = 0; filters.add(new JLabel("Prezzo:"), c);
        c.gridx = 3; c.weightx = 1; filters.add(fasciaPrezzo, c);

        c.gridx = 0; c.gridy = 2; c.weightx = 0; filters.add(new JLabel("Stelle minime:"), c);
        c.gridx = 1; c.weightx = 1; filters.add(stelleMin, c);
        c.gridx = 2; c.weightx = 0; filters.add(new JLabel(""), c);
        c.gridx = 3; c.weightx = 0; filters.add(cercaBtn, c);

        panel.add(filters, BorderLayout.NORTH);

        JPanel resultsPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        resultsPanel.setBackground(new Color(250, 250, 250));

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        List<String[]> ristorantiFake = new ArrayList<>();
        ristorantiFake.add(new String[]{"Trattoria BellaVita", "Milano", "Italiana", "35€", "4.7⭐"});
        ristorantiFake.add(new String[]{"Sushi Garden", "Torino", "Giapponese", "45€", "4.5⭐"});
        ristorantiFake.add(new String[]{"Burger Lab", "Roma", "Americana", "25€", "4.2⭐"});
        ristorantiFake.add(new String[]{"La Mer", "Genova", "Pesce", "60€", "4.8⭐"});

        for (String[] r : ristorantiFake)
            resultsPanel.add(creaCardRistorante(r[0], r[1], r[2], r[3], r[4]));

        return panel;
    }

    public static JPanel creaMieiPreferiti() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel title = new JLabel("❤️ I miei preferiti", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(230, 57, 70));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        JPanel placeholder = new JPanel();
        placeholder.setBackground(Color.WHITE);
        placeholder.add(new JLabel("Nessun ristorante preferito al momento."));
        panel.add(placeholder, BorderLayout.CENTER);
        return panel;
    }

    public static JPanel creaMieiRistoranti() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel title = new JLabel("🏨 I miei ristoranti", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(230, 57, 70));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        JPanel placeholder = new JPanel();
        placeholder.setBackground(Color.WHITE);
        placeholder.add(new JLabel("Nessun ristorante inserito."));
        panel.add(placeholder, BorderLayout.CENTER);
        return panel;
    }

    public static JPanel creaRecensioniCliente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel title = new JLabel("⭐ Le mie recensioni", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(230, 57, 70));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        JPanel placeholder = new JPanel();
        placeholder.setBackground(Color.WHITE);
        placeholder.add(new JLabel("Non hai ancora lasciato recensioni."));
        panel.add(placeholder, BorderLayout.CENTER);
        return panel;
    }

    public static JPanel creaRecensioniRistoratore() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel title = new JLabel("💬 Recensioni ricevute", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(230, 57, 70));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        JPanel placeholder = new JPanel();
        placeholder.setBackground(Color.WHITE);
        placeholder.add(new JLabel("Nessuna recensione disponibile."));
        panel.add(placeholder, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel creaCardRistorante(String nome, String citta, String tipo, String prezzo, String stelle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(360, 150));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 57, 70), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(Color.WHITE);

        JLabel titolo = new JLabel(nome);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titolo.setForeground(new Color(51, 51, 51));

        JLabel desc = new JLabel(tipo + " · " + citta);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        desc.setForeground(new Color(120, 120, 120));

        JPanel topText = new JPanel(new GridLayout(2, 1));
        topText.setBackground(Color.WHITE);
        topText.add(titolo);
        topText.add(desc);
        info.add(topText, BorderLayout.WEST);

        JPanel rating = new JPanel(new BorderLayout());
        rating.setBackground(Color.WHITE);
        JLabel stelleLabel = new JLabel(stelle);
        stelleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        stelleLabel.setForeground(new Color(230, 57, 70));
        stelleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        rating.add(stelleLabel, BorderLayout.NORTH);

        JLabel prezzoLabel = new JLabel("💶 " + prezzo, SwingConstants.RIGHT);
        prezzoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        prezzoLabel.setForeground(new Color(100, 100, 100));
        rating.add(prezzoLabel, BorderLayout.SOUTH);

        info.add(rating, BorderLayout.EAST);
        card.add(info, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(255, 240, 240));
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(card,
                        "Hai selezionato: " + nome + "\n(" + tipo + " - " + prezzo + ")",
                        "Dettagli Ristorante", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return card;
    }
}
