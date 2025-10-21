package theknife.gui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SectionPanels {

    /* ---------- Utility di stile ---------- */
    private static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(60, 60, 60));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(60, 149, 237));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));
    }

    private static void styleActionButton(JButton b) {
        b.setBackground(new Color(60, 149, 237));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /* ---------- HOME (ristoranti più votati) ---------- */
    public static JPanel creaHomeTopRistoranti() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Ristoranti più votati", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        listPanel.setBackground(Color.WHITE);

        String[][] ristoranti = {
            {"La Pergola", "Roma", "Italiana", "€120", "5★"},
            {"Uliassi", "Senigallia", "Pesce", "€90", "5★"},
            {"Sushi King", "Milano", "Giapponese", "€45", "4★"},
            {"Osteria Francescana", "Modena", "Italiana", "€110", "5★"},
            {"Da Vittorio", "Bergamo", "Italiana", "€100", "5★"}
        };

        for (String[] r : ristoranti) {
            JPanel card = new JPanel(new GridLayout(2, 2));
            card.setBackground(new Color(245, 245, 245));
            card.setBorder(new EmptyBorder(10, 10, 10, 10));
            card.add(new JLabel("Nome: " + r[0]));
            card.add(new JLabel("Città: " + r[1]));
            card.add(new JLabel("Cucina: " + r[2]));
            card.add(new JLabel("Valutazione: " + r[4]));
            listPanel.add(card);
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    /* ---------- CERCA RISTORANTI ---------- */
    public static JPanel creaCercaRistoranti() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Cerca Ristoranti", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        // --- Filtri di ricerca ---
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField fieldCitta = new JTextField(12);
        JComboBox<String> comboCucina = new JComboBox<>(new String[]{
            "Tutte", "Italiana", "Giapponese", "Cinese", "Messicana", "Americana", "Indiana"
        });
        JComboBox<String> comboPrezzo = new JComboBox<>(new String[]{
            "Qualsiasi", "≤ 20€", "20–50€", "50–100€", "≥ 100€"
        });
        JCheckBox checkDelivery = new JCheckBox("Delivery disponibile");
        JCheckBox checkPrenotazione = new JCheckBox("Prenotazione online");
        JComboBox<String> comboRating = new JComboBox<>(new String[]{
            "Qualsiasi", "≥ 1 ★", "≥ 2 ★", "≥ 3 ★", "≥ 4 ★", "5 ★"
        });
        JButton buttonCerca = new JButton("Cerca");
        styleActionButton(buttonCerca);

        // Layout criteri
        c.gridx = 0; c.gridy = 0; searchPanel.add(new JLabel("Città o luogo:"), c);
        c.gridx = 1; searchPanel.add(fieldCitta, c);

        c.gridx = 0; c.gridy = 1; searchPanel.add(new JLabel("Tipo cucina:"), c);
        c.gridx = 1; searchPanel.add(comboCucina, c);

        c.gridx = 0; c.gridy = 2; searchPanel.add(new JLabel("Fascia di prezzo:"), c);
        c.gridx = 1; searchPanel.add(comboPrezzo, c);

        c.gridx = 0; c.gridy = 3; searchPanel.add(new JLabel("Servizi:"), c);
        c.gridx = 1;
        JPanel serviziPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        serviziPanel.setBackground(Color.WHITE);
        serviziPanel.add(checkDelivery);
        serviziPanel.add(checkPrenotazione);
        searchPanel.add(serviziPanel, c);

        c.gridx = 0; c.gridy = 4; searchPanel.add(new JLabel("Valutazione minima:"), c);
        c.gridx = 1; searchPanel.add(comboRating, c);

        c.gridx = 1; c.gridy = 5; c.anchor = GridBagConstraints.EAST;
        searchPanel.add(buttonCerca, c);
        panel.add(searchPanel, BorderLayout.NORTH);

        // --- Tabella risultati ---
        String[] columns = {"Nome", "Città", "Prezzo medio (€)", "Cucina", "Delivery", "Prenotazione", "Stelle"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // --- Pulsante Dettagli ---
        JButton buttonDettagli = new JButton("Mostra Dettagli");
        styleActionButton(buttonDettagli);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(buttonDettagli);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Listener ricerca
        buttonCerca.addActionListener(e -> {
            String luogo = fieldCitta.getText().trim();
            if (luogo.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Inserisci almeno la città o il luogo!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cucina = comboCucina.getSelectedItem().toString();
            String fascia = comboPrezzo.getSelectedItem().toString();
            boolean delivery = checkDelivery.isSelected();
            boolean prenotazione = checkPrenotazione.isSelected();
            String rating = comboRating.getSelectedItem().toString();

            System.out.printf("RICERCA → luogo=%s, cucina=%s, prezzo=%s, delivery=%s, prenotazione=%s, rating=%s%n",
                    luogo, cucina, fascia, delivery, prenotazione, rating);

            model.setRowCount(0); // mock
            model.addRow(new Object[]{"La Pergola", "Roma", "120", "Italiana", "si", "si", "5★"});
            model.addRow(new Object[]{"Sushi King", "Milano", "45", "Giapponese", "si", "no", "4★"});
        });

        buttonDettagli.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(panel, "Seleziona un ristorante.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String nome = table.getValueAt(row, 0).toString();
            String citta = table.getValueAt(row, 1).toString();
            String prezzo = table.getValueAt(row, 2).toString();
            String cucina = table.getValueAt(row, 3).toString();
            String del = table.getValueAt(row, 4).toString();
            String pre = table.getValueAt(row, 5).toString();
            String stelle = table.getValueAt(row, 6).toString();

            JOptionPane.showMessageDialog(panel,
                    "Nome: " + nome + "\nCittà: " + citta + "\nPrezzo medio: " + prezzo + "€" +
                    "\nCucina: " + cucina + "\nDelivery: " + del +
                    "\nPrenotazione: " + pre + "\nStelle: " + stelle,
                    "Dettagli Ristorante", JOptionPane.INFORMATION_MESSAGE);
        });

        return panel;
    }

    /* ---------- I miei Preferiti ---------- */
    public static JPanel creaMieiPreferiti() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel label = new JLabel("I tuoi ristoranti preferiti", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    /* ---------- I miei Ristoranti ---------- */
    public static JPanel creaMieiRistoranti() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel label = new JLabel("Gestione dei tuoi ristoranti", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    /* ---------- Recensioni Cliente ---------- */
    public static JPanel creaRecensioniCliente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel label = new JLabel("Le tue recensioni", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    /* ---------- Recensioni Ristoratore ---------- */
    public static JPanel creaRecensioniRistoratore() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel label = new JLabel("Recensioni dei tuoi ristoranti", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
