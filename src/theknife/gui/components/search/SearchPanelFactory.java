package theknife.gui.components.search;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import theknife.gui.components.RestaurantDetailsFrame;
import theknife.gui.components.SectionPanels;
import theknife.model.Ristorante;

public class SearchPanelFactory {

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
        JComboBox<String> fasciaBox = new JComboBox<>(new String[]{
                "Qualsiasi", "<30€", "30€-50€", "50€-100€", ">100€"
        });
        JComboBox<String> stelleBox = new JComboBox<>(new String[]{
                "Qualsiasi", "≥1⭐", "≥2⭐", "≥3⭐", "≥4⭐", "5⭐"
        });
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
            boolean delivery = deliveryBox.isSelected();
            boolean prenota = prenotaBox.isSelected();

            if (citta.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Inserisci almeno la città per effettuare la ricerca.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Ristorante> tutti = RistoranteRepository.getRistoranti();
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
                    JLabel infoL = new JLabel(
                            r.getCitta() + " • " + r.getTipoCucina() + " • " + r.getFasciaPrezzo()
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
}