package theknife.gui.components;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import theknife.controller.GestioneRecensioni;
import theknife.model.Recensione;
import theknife.model.Ristorante;


@SuppressWarnings("unused")
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

    JLabel title = new JLabel("🔍 Cerca Ristoranti", SwingConstants.CENTER);
    title.setFont(new Font("Segoe UI", Font.BOLD, 26));
    title.setForeground(new Color(230, 57, 70));
    title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
    panel.add(title, BorderLayout.NORTH);

    JPanel filters = new JPanel(new GridBagLayout());
    filters.setBackground(Color.WHITE);
    filters.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(6, 8, 6, 8);
    c.fill = GridBagConstraints.HORIZONTAL;

    JTextField nomeField = new JTextField(12);
    JTextField cittaField = new JTextField(10);
    JTextField tipoField = new JTextField(10);
    JComboBox<String> fasciaBox = new JComboBox<>(new String[]{"Qualsiasi", "<30€", "30€-50€", "50€-100€", ">100€"});
    JComboBox<String> stelleBox = new JComboBox<>(new String[]{"Qualsiasi", "≥1⭐", "≥2⭐", "≥3⭐", "≥4⭐", "5⭐"});
    JCheckBox deliveryBox = new JCheckBox("Delivery disponibile");
    JCheckBox prenotaBox = new JCheckBox("Prenotazione online");
    JTextField distanzaField = new JTextField("10", 5);

    c.gridx = 0; c.gridy = 0; filters.add(new JLabel("Nome:"), c);
    c.gridx = 1; filters.add(nomeField, c);
    c.gridx = 2; filters.add(new JLabel("Tipo cucina:"), c);
    c.gridx = 3; filters.add(tipoField, c);

    c.gridx = 0; c.gridy = 1; filters.add(new JLabel("Città*:"), c);
    c.gridx = 1; filters.add(cittaField, c);
    c.gridx = 2; filters.add(new JLabel("Fascia di prezzo:"), c);
    c.gridx = 3; filters.add(fasciaBox, c);

    c.gridx = 0; c.gridy = 2; filters.add(new JLabel("Stelle minime:"), c);
    c.gridx = 1; filters.add(stelleBox, c);
    c.gridx = 2; filters.add(new JLabel("Raggio (km):"), c);
    c.gridx = 3; filters.add(distanzaField, c);

    c.gridx = 0; c.gridy = 3; filters.add(deliveryBox, c);
    c.gridx = 1; filters.add(prenotaBox, c);

    JButton cercaBtn = new JButton("Cerca");
    cercaBtn.setBackground(new Color(230, 57, 70));
    cercaBtn.setForeground(Color.WHITE);
    cercaBtn.setFocusPainted(false);
    c.gridx = 3; c.gridy = 3; filters.add(cercaBtn, c);

    panel.add(filters, BorderLayout.NORTH);

    JPanel resultsPanel = new JPanel();
    resultsPanel.setBackground(Color.WHITE);
    resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

    JScrollPane scroll = new JScrollPane(resultsPanel);
    scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
    scroll.getVerticalScrollBar().setUnitIncrement(16);
    panel.add(scroll, BorderLayout.CENTER);

    cercaBtn.addActionListener(e -> {
        resultsPanel.removeAll();

        String nome = nomeField.getText().trim().toLowerCase();
        String citta = cittaField.getText().trim().toLowerCase();
        String tipo = tipoField.getText().trim().toLowerCase();
        String fascia = (String) fasciaBox.getSelectedItem();
        String stelleSel = (String) stelleBox.getSelectedItem();
        boolean delivery = deliveryBox.isSelected();
        boolean prenota = prenotaBox.isSelected();
        int distanzaMax;
        try { distanzaMax = Integer.parseInt(distanzaField.getText().trim()); } catch (Exception ex) { distanzaMax = 10; }

        if (citta.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Inserisci almeno la città per effettuare la ricerca.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Ristorante> tutti = getRistoranti();
        List<Ristorante> risultati = new ArrayList<>();


        for (Ristorante r : tutti) {
    boolean ok = true;
    if (!r.getCitta().toLowerCase().contains(citta)) ok = false;
    if (!nome.isEmpty() && !r.getNome().toLowerCase().contains(nome)) ok = false;
    if (!tipo.isEmpty() && !r.getTipoCucina().toLowerCase().contains(tipo)) ok = false;
    if (delivery && !r.isDelivery()) ok = false;
    if (prenota && !r.isPrenotazione()) ok = false;
    if (!fascia.equals("Qualsiasi") && !r.getFasciaPrezzo().equalsIgnoreCase(fascia)) ok = false;

    if (ok) risultati.add(r);
}


        if (risultati.isEmpty()) {
            JLabel no = new JLabel("Nessun ristorante trovato.", SwingConstants.CENTER);
            no.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsPanel.add(no);
        } else {
            for (Ristorante r : risultati) {
    JPanel card = new JPanel(new BorderLayout());
    card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 57, 70), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
    ));
    card.setBackground(Color.WHITE);

    JLabel nomeL = new JLabel(r.getNome());
    nomeL.setFont(new Font("Segoe UI", Font.BOLD, 15));
    JLabel infoL = new JLabel(r.getCitta() + " • " + r.getTipoCucina() + " • " + r.getFasciaPrezzo());
    infoL.setForeground(new Color(90, 90, 90));

    JPanel text = new JPanel();
    text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
    text.setBackground(Color.WHITE);
    text.add(nomeL);
    text.add(infoL);

    card.add(text, BorderLayout.CENTER);

    JButton vediBtn = new JButton("Visualizza dettagli");
    vediBtn.addActionListener(ev -> {
        JOptionPane.showMessageDialog(panel, "Scheda: " + r.getNome(), "Dettagli", JOptionPane.INFORMATION_MESSAGE);
    });
    card.add(vediBtn, BorderLayout.EAST);

    resultsPanel.add(card);
    resultsPanel.add(Box.createVerticalStrut(10));
}

        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    });

    return panel;
}

public static List<Ristorante> getRistoranti() {

    List<Ristorante> lista = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader("data/ristoranti.csv"))) {
        String line;
        br.readLine(); // SALTA HEADER

        while ((line = br.readLine()) != null) {
            String[] c = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            String nome   = c[0].replace("\"", "").trim();
            String citta  = c[2].replace("\"", "").trim();
            String tipo   = c[3].replace("\"", "").trim();
            String prezzo = c[4].replace("\"", "").trim();

            boolean delivery = c.length > 10 && c[10].toLowerCase().contains("delivery");
            boolean prenota  = c.length > 10 && c[10].toLowerCase().contains("reservation");

            lista.add(new Ristorante(nome, citta, tipo, prezzo, delivery, prenota));
        }

    } catch (Exception e) {
        System.out.println("❌ Errore lettura CSV: "+e.getMessage());
    }

    return lista;
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

    JPanel content = new JPanel();
    content.setBackground(Color.WHITE);
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

    java.util.List<theknife.controller.GestioneRistoranti.Ristorante> miei = theknife.controller.GestioneRistoranti.getRistorantiProprietario(CURRENT_USERNAME);

    if (miei.isEmpty()) {
        JLabel msg = new JLabel("Non hai ancora inserito ristoranti.");
        msg.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(msg);
    } else {
        for (theknife.controller.GestioneRistoranti.Ristorante r : miei) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 57, 70), 1),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)
            ));
            card.setBackground(Color.WHITE);
            JLabel nome = new JLabel(r.getNome() + " • " + r.getTipoCucina());
            nome.setFont(new Font("Segoe UI", Font.BOLD, 15));
            JLabel info = new JLabel(r.getCitta() + " • " + r.getFasciaPrezzo());
            info.setForeground(new Color(90, 90, 90));
            JPanel text = new JPanel();
            text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
            text.setBackground(Color.WHITE);
            text.add(nome);
            text.add(info);
            card.add(text, BorderLayout.CENTER);
            JButton recensioniBtn = new JButton("Visualizza recensioni");
            recensioniBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(panel, "Visualizzazione recensioni di " + r.getNome(), "Info", JOptionPane.INFORMATION_MESSAGE);
            });
            card.add(recensioniBtn, BorderLayout.EAST);
            content.add(card);
            content.add(Box.createVerticalStrut(10));
        }
    }

    JScrollPane scroll = new JScrollPane(content);
    scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
    scroll.getVerticalScrollBar().setUnitIncrement(16);
    panel.add(scroll, BorderLayout.CENTER);

    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    bottom.setBackground(Color.WHITE);
    JButton addBtn = new JButton("➕ Aggiungi ristorante");
    addBtn.addActionListener(e -> {
        new theknife.gui.components.DialogNuovoRistorante(null, CURRENT_USERNAME).setVisible(true);
        NAVIGATE.accept(creaMieiRistoranti());
    });
    bottom.add(addBtn);
    panel.add(bottom, BorderLayout.SOUTH);

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
