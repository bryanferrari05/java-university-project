package theknife.gui.components.search;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import theknife.gui.components.RestaurantDetailsFrame;
import theknife.gui.components.SectionPanels;
import theknife.controller.GestioneRecensioni;
import theknife.model.Ristorante;

public class SearchPanelFactory {

    public static JPanel creaCercaRistoranti() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Cerca Ristoranti", SwingConstants.CENTER);
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
        JComboBox<String> fasciaBox = new JComboBox<>(new String[]{
                "Qualsiasi", "<30 EUR", "30-50 EUR", "50-100 EUR", ">100 EUR"
        });
        JComboBox<String> stelleBox = new JComboBox<>(new String[]{
                "Qualsiasi", "1+", "2+", "3+", "4+", "5"
        });
        JCheckBox deliveryBox = new JCheckBox("Delivery disponibile");
        JCheckBox prenotaBox = new JCheckBox("Prenotazione online");
        JTextField distanzaField = new JTextField("10", 5);

        c.gridx = 0; c.gridy = 0; filters.add(new JLabel("Nome:"), c);
        c.gridx = 1; filters.add(nomeField, c);
        c.gridx = 2; filters.add(new JLabel("Tipo cucina:"), c);
        c.gridx = 3; filters.add(tipoField, c);

        c.gridx = 0; c.gridy = 1; filters.add(new JLabel("Citta*:"), c);
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
            boolean delivery = deliveryBox.isSelected();
            boolean prenota = prenotaBox.isSelected();

            if (citta.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Inserisci almeno la citta per effettuare la ricerca.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Ristorante> tutti = RistoranteRepository.getRistoranti();
            List<Ristorante> risultati = new ArrayList<>();
            Double[] centro = getCityCenter(tutti, citta);
            double raggioKm = parseRadiusKm(distanzaField.getText());
            Integer stelleMinime = parseMinStars((String) stelleBox.getSelectedItem());

            for (Ristorante r : tutti) {
                boolean ok = true;

                if (!r.getCitta().toLowerCase().contains(citta)) ok = false;
                if (!nome.isEmpty() && !r.getNome().toLowerCase().contains(nome)) ok = false;
                if (!tipo.isEmpty() && !r.getTipoCucina().toLowerCase().contains(tipo)) ok = false;
                if (delivery && !r.isDelivery()) ok = false;
                if (prenota && !r.isPrenotazione()) ok = false;
                if (!"Qualsiasi".equals(fascia) && !priceMatchesRange(r.getFasciaPrezzo(), fascia)) ok = false;
                if (stelleMinime != null && r.getStarCount() < stelleMinime) ok = false;
                if (raggioKm > 0 && centro[0] != null && centro[1] != null) {
                    double dist = haversineKm(centro[0], centro[1], r.getLatitude(), r.getLongitude());
                    if (dist > raggioKm) ok = false;
                }

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
                    JLabel infoL = new JLabel(
                            r.getCitta() + " | " + r.getTipoCucina() + " | " + r.getFasciaPrezzo()
                    );
                    infoL.setForeground(new Color(90, 90, 90));

                    JPanel text = new JPanel();
                    text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
                    text.setBackground(Color.WHITE);
                    text.add(nomeL);
                    text.add(infoL);

                    card.add(text, BorderLayout.CENTER);

                    JButton vediBtn = new JButton("Visualizza dettagli");
                    vediBtn.addActionListener(ev -> {
                        int id = RistoranteRepository.getIdByRistorante(r);
                        new RestaurantDetailsFrame(SectionPanels.CURRENT_USER, id, r);
                    });

                    JButton recensioneBtn = new JButton("Lascia recensione");
                    recensioneBtn.addActionListener(ev -> {
                        if (SectionPanels.CURRENT_USER == null
                                || "ospite".equalsIgnoreCase(SectionPanels.CURRENT_ROLE)) {
                            JOptionPane.showMessageDialog(panel,
                                    "Per lasciare una recensione devi accedere.",
                                    "Accesso richiesto", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        int ristoranteId = RistoranteRepository.getIdByRistorante(r);
                        if (ristoranteId < 0) {
                            JOptionPane.showMessageDialog(panel,
                                    "Impossibile trovare il ristorante selezionato.",
                                    "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (GestioneRecensioni.ristoranteHaRaggiuntoLimite(ristoranteId)) {
                            JOptionPane.showMessageDialog(panel,
                                    "Questo ristorante ha gia raggiunto il limite di 2 recensioni.",
                                    "Info", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        apriDialogRecensione(panel, r, ristoranteId);
                    });

                    JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
                    actions.setBackground(Color.WHITE);
                    actions.add(recensioneBtn);
                    actions.add(vediBtn);

                    card.add(actions, BorderLayout.EAST);

                    resultsPanel.add(card);
                    resultsPanel.add(Box.createVerticalStrut(10));
                }
            }

            resultsPanel.revalidate();
            resultsPanel.repaint();
        });

        return panel;
    }

    private static void apriDialogRecensione(Component parent, Ristorante ristorante, int ristoranteId) {
        JComboBox<String> stelleBox = new JComboBox<>(
                new String[]{"1", "2", "3", "4", "5"}
        );

        JTextArea testoArea = new JTextArea(5, 24);
        testoArea.setLineWrap(true);
        testoArea.setWrapStyleWord(true);
        JScrollPane testoScroll = new JScrollPane(testoArea);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0; gc.gridy = 0;
        form.add(new JLabel("Stelle (1-5):"), gc);
        gc.gridx = 1;
        form.add(stelleBox, gc);

        gc.gridx = 0; gc.gridy = 1; gc.gridwidth = 2;
        form.add(new JLabel("Recensione:"), gc);
        gc.gridy = 2; gc.weightx = 1.0; gc.weighty = 1.0;
        gc.fill = GridBagConstraints.BOTH;
        form.add(testoScroll, gc);

        int result = JOptionPane.showConfirmDialog(
                parent, form, "Lascia una recensione - " + ristorante.getNome(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        String testo = testoArea.getText().trim();
        int stelle = Integer.parseInt((String) stelleBox.getSelectedItem());

        if (testo.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                    "Inserisci il testo della recensione.",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (GestioneRecensioni.ristoranteHaRaggiuntoLimite(ristoranteId)) {
            JOptionPane.showMessageDialog(parent,
                    "Questo ristorante ha gia raggiunto il limite di 2 recensioni.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        GestioneRecensioni.aggiungiRecensione(
                ristoranteId,
                ristorante.getNome(),
                ristorante.getCitta(),
                SectionPanels.CURRENT_USERNAME,
                stelle,
                testo
        );

        JOptionPane.showMessageDialog(parent,
                "Recensione salvata.",
                "OK", JOptionPane.INFORMATION_MESSAGE);
    }

    private static Double[] getCityCenter(List<Ristorante> tutti, String citta) {
        double sumLat = 0.0;
        double sumLon = 0.0;
        int count = 0;
        for (Ristorante r : tutti) {
            if (r.getCitta().toLowerCase().contains(citta)) {
                sumLat += r.getLatitude();
                sumLon += r.getLongitude();
                count++;
            }
        }
        if (count == 0) return new Double[]{null, null};
        return new Double[]{sumLat / count, sumLon / count};
    }

    private static double parseRadiusKm(String text) {
        if (text == null) return 0.0;
        String trimmed = text.trim();
        if (trimmed.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(trimmed.replace(",", "."));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static Integer parseMinStars(String selection) {
        if (selection == null || selection.equals("Qualsiasi")) return null;
        String digits = selection.replace("+", "").trim();
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean priceMatchesRange(String prezzo, String fascia) {
        int level = priceLevel(prezzo);
        if (level == 0) return false;
        switch (fascia) {
            case "<30 EUR":
                return level == 1;
            case "30-50 EUR":
                return level == 2;
            case "50-100 EUR":
                return level == 3;
            case ">100 EUR":
                return level >= 4;
            default:
                return true;
        }
    }

    private static int priceLevel(String prezzo) {
        if (prezzo == null) return 0;
        int euro = countChar(prezzo, '\u20AC');
        int dollar = countChar(prezzo, '$');
        int level = Math.max(euro, dollar);
        if (level == 0) {
            int ticks = countChar(prezzo, '\'');
            if (ticks >= 1 && ticks <= 4) level = ticks;
        }
        return level;
    }

    private static int countChar(String text, char target) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == target) count++;
        }
        return count;
    }

    private static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371.0 * c;
    }
}
