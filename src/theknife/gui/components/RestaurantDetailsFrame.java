package theknife.gui.components;

import theknife.model.Ristorante;
import theknife.model.User;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class RestaurantDetailsFrame extends JFrame {

    private final User user;
    private final int restaurantId;

    public RestaurantDetailsFrame(User user, int restaurantId, Ristorante r) {

        this.user = user;
        this.restaurantId = restaurantId;

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

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        container.add(scrollPane, BorderLayout.CENTER);

        // ----- FOOTER -----
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.WHITE);

        // ⭐ BOTTONE PREFERITI DINAMICO
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

        JButton closeBtn = new JButton("Chiudi");
        closeBtn.addActionListener(e -> dispose());

        footer.add(preferitiBtn);
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
    }

    // --------------------- SALVATAGGIO PREFERITI ---------------------

    private void salvaPreferitiNelCSV() {
        try {
            File f = new File("data/utenti.csv");
            BufferedReader br = new BufferedReader(new FileReader(f));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {

                String[] campi = line.split(",");

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

                    line = String.format("%s,%s,%s,%s,%s,%s",
                            campi[0], campi[1], campi[2], campi[3], campi[4], preferitiStr);
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

    private void aggiornaBottone(JButton btn) {
        if (user.isPreferito(restaurantId)) {
            btn.setText("Rimuovi dai preferiti");
        } else {
            btn.setText("Aggiungi ai preferiti");
        }
    }

    // --------------------- COMPONENTI DI STILE ---------------------

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

    private JLabel sectionTitle(String text) {
        JLabel title = new JLabel(text);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(230, 57, 70));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return title;
    }

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
}
