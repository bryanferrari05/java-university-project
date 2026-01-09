package theknife.gui.components;

import theknife.controller.GestioneRecensioni;
import theknife.model.Recensione;
import theknife.model.Ristorante;
import theknife.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.List;

/**
 * Detail window that shows restaurant info and reviews.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class RestaurantDetailsFrame extends JFrame {

    /** Current user viewing the details. */
    private final User user;
    /** CSV row id for the restaurant. */
    private final int restaurantId;
    /** Parent window to restore when closed. */
    private final Window parentWindow;

    /**
     * Builds a details window without a parent window.
     *
     * @param user current user
     * @param restaurantId restaurant id
     * @param r restaurant model
     */
    public RestaurantDetailsFrame(User user, int restaurantId, Ristorante r) {
        this(null, user, restaurantId, r);
    }

    /**
     * Builds a details window and hides the parent window.
     *
     * @param parentWindow parent window to hide and restore
     * @param user current user
     * @param restaurantId restaurant id
     * @param r restaurant model
     */
    public RestaurantDetailsFrame(Window parentWindow, User user, int restaurantId, Ristorante r) {

        this.parentWindow = parentWindow;
        this.user = user;
        this.restaurantId = restaurantId;

        if (this.parentWindow != null) {
            this.parentWindow.setVisible(false);
        }

        // ----- FINESTRA PRINCIPALE -----
        setTitle("Dettagli – " + r.getNome());
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);

        // ----- TITOLO -----
        JLabel title = new JLabel(r.getNome(), SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(230, 57, 70));
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));
        container.add(title, BorderLayout.NORTH);

        // ----- CONTENUTO SCORREVOLE -----
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(10, 50, 20, 50));

        content.add(makeRow("Indirizzo: ", String.valueOf(r.getAddress())));
        content.add(makeRow("Numero Telefonico: ", String.valueOf(r.getPhoneNumber())));
        content.add(makeRow("Sito Web: ", r.getWebsiteUrl()));
        content.add(makeRow("Tipo di CUcina: ", String.valueOf(r.getCuisine())));
        content.add(makeRow("Valuta: ", String.valueOf(r.getPrice())));
        content.add(makeRow("Stelle: ", String.valueOf(r.getAward())));

        content.add(Box.createVerticalStrut(25));

        JLabel serviziTitle = sectionTitle("Servizi disponibili:");
        content.add(serviziTitle);

        JPanel serviziRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        serviziRow.setBackground(Color.WHITE);

        JLabel disponibilita = new JLabel(
                (r.isPrenotazione() ? "Prenotazione disponibile" : "Nessuna prenotazione")
                        + "    |    " +
                        (r.isDelivery() ? "Delivery / Take Away" : "Niente delivery")
        );
        disponibilita.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        disponibilita.setForeground(new Color(60, 60, 60));

        serviziRow.add(disponibilita);
        content.add(serviziRow);
        content.add(Box.createVerticalStrut(30));

        content.add(sectionTitle("Servizi del locale:"));
        content.add(bigTextBox(r.getFacilitiesAndServices(), 120));
        content.add(Box.createVerticalStrut(30));

        content.add(sectionTitle("Descrizione:"));
        content.add(bigTextBox(r.getDescription(), 200));
        content.add(Box.createVerticalStrut(40));

        content.add(sectionTitle("Recensioni:"));
        content.add(makeRecensioniPanel());
        content.add(Box.createVerticalStrut(20));

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        container.add(scrollPane, BorderLayout.CENTER);

        // ----- FOOTER -----
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.WHITE);

        // ⭐ BOTTONE PREFERITI DINAMICO
        boolean isGuest = user == null || user.getRuolo().equalsIgnoreCase("ospite");

        if (!isGuest) {
            JButton preferitiBtn = new JButton();
            preferitiBtn.setBackground(new Color(230, 57, 70));
            preferitiBtn.setForeground(Color.WHITE);
            aggiornaBottone(preferitiBtn);
            preferitiBtn.addActionListener(e -> {
                if (user.isPreferito(restaurantId)) {
                    user.rimuoviPreferito(restaurantId);
                } else {
                    user.aggiungiPreferito(restaurantId);
                }
                salvaPreferitiNelCSV();
                aggiornaBottone(preferitiBtn);
            });
            footer.add(preferitiBtn);
        }

        JButton closeBtn = new JButton("Chiudi");
        closeBtn.addActionListener(e -> dispose());

        footer.add(closeBtn);

        container.add(footer, BorderLayout.SOUTH);

        setContentPane(container);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int maxWidth = (int) (screen.width * 0.85);
        int maxHeight = (int) (screen.height * 0.85);

        setMinimumSize(new Dimension(700, 500));
        setPreferredSize(new Dimension(maxWidth, maxHeight));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        if (this.parentWindow != null) {
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    if (RestaurantDetailsFrame.this.parentWindow.isDisplayable()) {
                        RestaurantDetailsFrame.this.parentWindow.setVisible(true);
                    }
                }
            });
        }
    }

    // --------------------- SALVATAGGIO PREFERITI ---------------------

    /**
     * Persists current favorites into the users CSV file.
     */
    private void salvaPreferitiNelCSV() {
        try {
            File f = new File("data/utenti.csv");
            BufferedReader br = new BufferedReader(new FileReader(f));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {

                String[] campi = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                // Salta l'intestazione
                if (line.startsWith("nome,")) {
                    sb.append(line).append("\n");
                    continue;
                }

                if (campi.length >= 5 && campi[2].equals(user.getUsername())) {

                    String preferitiStr = user.getPreferiti().isEmpty() ?
                            "" :
                            user.getPreferiti().stream()
                                    .map(String::valueOf)
                                    .reduce((a, b) -> a + "-" + b)
                                    .orElse("");

                    String residenza = "";
                    if (campi.length >= 6) {
                        residenza = campi[5];
                    }

                    line = String.format("%s,%s,%s,%s,%s,%s,%s",
                            campi[0], campi[1], campi[2], campi[3], campi[4], residenza, preferitiStr);
                }

                sb.append(line).append("\n");
            }

            br.close();

            FileWriter fw = new FileWriter("data/utenti.csv");
            fw.write(sb.toString());
            fw.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore nel salvataggio preferiti:\n" + e.getMessage());
        }
    }

    // --------------------- BOTTONE DINAMICO ---------------------

    /**
     * Updates the favorites button label based on state.
     *
     * @param btn button to update
     */
    private void aggiornaBottone(JButton btn) {
        if (user.isPreferito(restaurantId)) {
            btn.setText("Rimuovi dai preferiti");
        } else {
            btn.setText("Aggiungi ai preferiti");
        }
    }

    // --------------------- COMPONENTI DI STILE ---------------------

    /**
     * Builds a label/value row for details.
     *
     * @param label left label
     * @param value right value
     * @return row panel
     */
    private JPanel makeRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 17));

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.PLAIN, 17));

        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.CENTER);

        return row;
    }

    /**
     * Builds a section title label.
     *
     * @param text title text
     * @return label instance
     */
    private JLabel sectionTitle(String text) {
        JLabel title = new JLabel(text);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(230, 57, 70));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return title;
    }

    /**
     * Builds a scrollable text box for long content.
     *
     * @param text content text
     * @param height preferred height
     * @return scroll pane
     */
    private JScrollPane bigTextBox(String text, int height) {
        JTextArea area = new JTextArea(text);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        area.setForeground(new Color(40, 40, 40));
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 57, 70), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JScrollPane pane = new JScrollPane(area);
        pane.setBorder(null);
        pane.setPreferredSize(new Dimension(0, height));
        pane.getVerticalScrollBar().setUnitIncrement(15);

        return pane;
    }

    /**
     * Builds the reviews section for the restaurant.
     *
     * @return reviews panel
     */
    private JPanel makeRecensioniPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        double media = GestioneRecensioni.getMediaRistorante(restaurantId);
        int totale = GestioneRecensioni.getConteggioRistorante(restaurantId);
        JLabel stats = new JLabel("Media stelle: " + media + "/5  |  Totale recensioni: " + totale);
        stats.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        stats.setForeground(new Color(60, 60, 60));
        stats.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        panel.add(stats);

        List<Recensione> recensioni = GestioneRecensioni.getRecensioniRistorante(restaurantId);
        if (recensioni.isEmpty()) {
            JLabel empty = new JLabel("Nessuna recensione.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            empty.setForeground(new Color(60, 60, 60));
            panel.add(empty);
            return panel;
        }

        boolean isGuest = user == null || user.getRuolo().equalsIgnoreCase("ospite");
        for (Recensione r : recensioni) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 57, 70), 1),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
            ));

            String autore = isGuest ? "anonimo" : "@" + r.getAutoreUsername();
            JLabel head = new JLabel("Utente: " + autore + " | Stelle: " + r.getStelle() + "/5");
            head.setFont(new Font("Segoe UI", Font.BOLD, 15));
            head.setForeground(new Color(40, 40, 40));

            JTextArea testo = new JTextArea(r.getTesto());
            testo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            testo.setForeground(new Color(50, 50, 50));
            testo.setBackground(Color.WHITE);
            testo.setLineWrap(true);
            testo.setWrapStyleWord(true);
            testo.setEditable(false);
            testo.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

            card.add(head);
            card.add(testo);

            String risposta = r.getRispostaRistoratore();
            if (risposta != null && !risposta.isBlank()) {
                JLabel respLabel = new JLabel("Risposta ristoratore:");
                respLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
                respLabel.setForeground(new Color(40, 40, 40));

                JTextArea resp = new JTextArea(risposta);
                resp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                resp.setForeground(new Color(70, 70, 70));
                resp.setBackground(new Color(245, 245, 245));
                resp.setLineWrap(true);
                resp.setWrapStyleWord(true);
                resp.setEditable(false);
                resp.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

                card.add(respLabel);
                card.add(resp);
            }

            panel.add(card);
            panel.add(Box.createVerticalStrut(10));
        }

        return panel;
    }
}
