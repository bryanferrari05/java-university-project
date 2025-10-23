package theknife.gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

import theknife.controller.GestioneRecensioni;
import theknife.model.Recensione;

public class SectionPanels {

    private static String CURRENT_ROLE = "ospite";
    private static String CURRENT_USERNAME = "Ospite";
    private static Consumer<JPanel> NAVIGATE; // per cambiare il pannello centrale (fornito da HomeFrame)

    public static void setSession(String ruolo, String username, Consumer<JPanel> navigate) {
        CURRENT_ROLE = ruolo == null ? "ospite" : ruolo;
        CURRENT_USERNAME = username == null ? "Ospite" : username;
        NAVIGATE = navigate;
    }

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
                {"Taverna Blu", "Napoli", "Pesce", "45€", "4.6⭐"},
                {"prova1", "Genova", "Pesce", "60€", "4.8⭐"},
                {"prova2", "Genova", "Pesce", "60€", "4.8⭐"}
        };

        for (String[] r : fake) {
            content.add(creaCardRistorante(r[0], r[1], r[2], r[3], r[4]));
        }

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

        JTextField searchField = new JTextField(15);
        JTextField cittaField = new JTextField(10);
        JTextField tipoField = new JTextField(10);
        JComboBox<String> fasciaPrezzo = new JComboBox<>(new String[]{"Qualsiasi", "<20€", "20€–50€", "50€–100€", ">100€"});
        JComboBox<String> stelleMin = new JComboBox<>(new String[]{"Qualsiasi", "≥1⭐", "≥2⭐", "≥3⭐", "≥4⭐", "5⭐"});

        c.gridx = 0; c.gridy = 0; filters.add(new JLabel("Cerca:"), c);
        c.gridx = 1; c.gridy = 0; filters.add(searchField, c);
        c.gridx = 0; c.gridy = 1; filters.add(new JLabel("Città:"), c);
        c.gridx = 1; c.gridy = 1; filters.add(cittaField, c);
        c.gridx = 0; c.gridy = 2; filters.add(new JLabel("Tipo:"), c);
        c.gridx = 1; c.gridy = 2; filters.add(tipoField, c);
        c.gridx = 2; c.gridy = 0; filters.add(new JLabel("Prezzo:"), c);
        c.gridx = 3; c.gridy = 0; filters.add(fasciaPrezzo, c);
        c.gridx = 2; c.gridy = 1; filters.add(new JLabel("Stelle:"), c);
        c.gridx = 3; c.gridy = 1; filters.add(stelleMin, c);

        JButton searchBtn = new JButton("Cerca");
        c.gridx = 3; c.gridy = 2; filters.add(searchBtn, c);

        panel.add(filters, BorderLayout.NORTH);

        JPanel results = new JPanel(new GridLayout(0, 2, 16, 16));
        results.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(results);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

        List<String[]> fakeRis = new ArrayList<>();
        fakeRis.add(new String[]{"La Pergola", "Roma", "Gourmet", "€€€", "4.9⭐"});
        fakeRis.add(new String[]{"Trattoria da Nonna", "Bologna", "Tradizionale", "€€", "4.6⭐"});
        fakeRis.add(new String[]{"Fish&Sea", "Napoli", "Pesce", "€€", "4.5⭐"});
        fakeRis.add(new String[]{"Veg&Go", "Milano", "Vegetariano", "€", "4.3⭐"});

        for (String[] r : fakeRis) {
            results.add(creaCardRistorante(r[0], r[1], r[2], r[3], r[4]));
        }

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

        List<Recensione> mie = GestioneRecensioni.getRecensioniUtente(CURRENT_USERNAME);

        if (mie.isEmpty()) {
            JPanel placeholder = new JPanel();
            placeholder.setBackground(Color.WHITE);
            placeholder.add(new JLabel("Non hai ancora lasciato recensioni."));
            panel.add(placeholder, BorderLayout.CENTER);
            return panel;
        }

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(Color.WHITE);

        for (Recensione r : mie) {
            list.add(cardRecensione(r, false));
            list.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

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

        // TODO: quando il backend collegherà i ristoranti del proprietario, popolare qui.
        // Per ora mostriamo l'elenco vuoto con un placeholder pulito.
        JPanel placeholder = new JPanel();
        placeholder.setBackground(Color.WHITE);
        placeholder.setLayout(new BoxLayout(placeholder, BoxLayout.Y_AXIS));
        JLabel l1 = new JLabel("Qui vedrai i tuoi ristoranti con il tasto \"Visualizza recensioni\".");
        l1.setAlignmentX(Component.CENTER_ALIGNMENT);
        placeholder.add(Box.createVerticalStrut(10));
        placeholder.add(l1);
        panel.add(placeholder, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel creaCardRistorante(String nome, String citta, String tipo, String prezzo, String stelle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(300, 120));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 57, 70), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(Color.WHITE);

        JLabel titolo = new JLabel(nome);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titolo.setForeground(new Color(51, 51, 51));

        JLabel sub = new JLabel(citta + " • " + tipo);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(100, 100, 100));

        info.add(titolo, BorderLayout.NORTH);
        info.add(sub, BorderLayout.CENTER);

        JPanel rating = new JPanel(new BorderLayout());
        rating.setBackground(Color.WHITE);

        JLabel stelleLabel = new JLabel(stelle, SwingConstants.RIGHT);
        stelleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        stelleLabel.setForeground(new Color(230, 57, 70));
        stelleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        rating.add(stelleLabel, BorderLayout.NORTH);

        JLabel prezzoLabel = new JLabel("💶 " + prezzo, SwingConstants.RIGHT);
        prezzoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        prezzoLabel.setForeground(new Color(100, 100, 100));
        rating.add(prezzoLabel, BorderLayout.SOUTH);

        info.add(rating, BorderLayout.EAST);
        card.add(info, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        actions.setBackground(Color.WHITE);

        JButton btnVisualizza = new JButton("Visualizza recensioni");
        btnVisualizza.addActionListener(e -> apriDialogRecensioni(nome));
        actions.add(btnVisualizza);

        boolean isCliente = CURRENT_ROLE.equalsIgnoreCase("cliente");
        boolean giaRecensito = GestioneRecensioni.utenteHaRecensito(CURRENT_USERNAME, nome);
        if (isCliente && !giaRecensito) {
            JButton btnLascia = new JButton("Lascia una recensione");
            btnLascia.addActionListener(e -> {
                Integer stelleSel = chiediStelle();
                if (stelleSel == null) return;
                String testo = chiediTesto("Scrivi la tua recensione:");
                if (testo == null || testo.isBlank()) return;
                GestioneRecensioni.aggiungiRecensione(CURRENT_USERNAME, nome, stelleSel, testo.trim());
                JOptionPane.showMessageDialog(card, "Recensione pubblicata", "OK", JOptionPane.INFORMATION_MESSAGE);
            });
            actions.add(btnLascia);
        }

        card.add(actions, BorderLayout.SOUTH);

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
        });

        return card;
    }

    private static Integer chiediStelle() {
        Object[] options = {1, 2, 3, 4, 5};
        Object sel = JOptionPane.showInputDialog(null, "Valutazione (1-5):", "Stelle",
                JOptionPane.PLAIN_MESSAGE, null, options, 5);
        if (sel instanceof Integer) return (Integer) sel;
        return null;
        }

    private static String chiediTesto(String prompt) {
        return JOptionPane.showInputDialog(null, prompt, "");
    }

    private static JPanel cardRecensione(Recensione r, boolean consentiRisposta) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 57, 70), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JLabel top = new JLabel("@" + r.getAutoreUsername() + " • " + r.getStelle() + "⭐");
        top.setFont(new Font("Segoe UI", Font.BOLD, 13));
        top.setForeground(new Color(51, 51, 51));
        p.add(top, BorderLayout.NORTH);

        JTextArea body = new JTextArea(r.getTesto());
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setEditable(false);
        body.setBackground(Color.WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0));
        p.add(body, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);

        if (r.getRispostaRistoratore() != null && !r.getRispostaRistoratore().isBlank()) {
            JTextArea rep = new JTextArea("Risposta del ristoratore: " + r.getRispostaRistoratore());
            rep.setLineWrap(true);
            rep.setWrapStyleWord(true);
            rep.setEditable(false);
            rep.setBackground(new Color(246, 255, 247));
            rep.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
            bottom.add(rep, BorderLayout.CENTER);
        } else if (consentiRisposta) {
            JButton rispondi = new JButton("Rispondi");
            rispondi.addActionListener(e -> {
                String risposta = chiediTesto("Scrivi la risposta (una sola, definitiva):");
                if (risposta != null && !risposta.isBlank()) {
                    GestioneRecensioni.rispondiRistoratore(r.getRistoranteNome(), r.getAutoreUsername(), risposta.trim());
                    JOptionPane.showMessageDialog(p, "Risposta inviata", "OK", JOptionPane.INFORMATION_MESSAGE);
                    Window w = SwingUtilities.getWindowAncestor(p);
                    if (w != null) w.dispose();
                    apriDialogRecensioni(r.getRistoranteNome());
                }
            });
            bottom.add(rispondi, BorderLayout.EAST);
        }

        p.add(bottom, BorderLayout.SOUTH);
        return p;
    }

    private static void apriDialogRecensioni(String ristoranteNome) {
        JDialog d = new JDialog((Frame) null, "Recensioni • " + ristoranteNome, true);
        d.setSize(560, 520);
        d.setLocationRelativeTo(null);
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        JLabel title = new JLabel("Recensioni di " + ristoranteNome, SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        header.add(title, BorderLayout.CENTER);
        container.add(header, BorderLayout.NORTH);

        List<Recensione> elenco = GestioneRecensioni.getRecensioniRistorante(ristoranteNome);
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(Color.WHITE);

        boolean isRistoratore = CURRENT_ROLE.equalsIgnoreCase("ristoratore");
        boolean permetteRisposta = isRistoratore; // la logica di ownership del ristorante sarà gestita a backend

        if (elenco.isEmpty()) {
            JPanel ph = new JPanel();
            ph.setBackground(Color.WHITE);
            ph.add(new JLabel("Nessuna recensione presente."));
            container.add(ph, BorderLayout.CENTER);
        } else {
            for (Recensione r : elenco) {
                list.add(cardRecensione(r, permetteRisposta));
                list.add(Box.createVerticalStrut(10));
            }
            JScrollPane scroll = new JScrollPane(list);
            scroll.setBorder(BorderFactory.createEmptyBorder(10, 16, 16, 16));
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            container.add(scroll, BorderLayout.CENTER);
        }

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.WHITE);
        JButton chiudi = new JButton("Chiudi");
        chiudi.addActionListener(e -> d.dispose());
        footer.add(chiudi);
        container.add(footer, BorderLayout.SOUTH);

        d.setContentPane(container);
        d.setVisible(true);
    }
}
