package theknife.gui.components;

import theknife.model.Ristorante;

import javax.swing.*;
import java.awt.*;

public class RestaurantDetailsFrame extends JFrame {

    public RestaurantDetailsFrame(Ristorante r) {

        // ----- FINESTRA PRINCIPALE -----
        setTitle("Dettagli – " + r.getNome());
        setSize(750, 720);
        setResizable(false);
        setLocationRelativeTo(null);
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

        // =======================
        //   INFO PRINCIPALI
        // =======================
        content.add(makeRow("Sito Web:", r.getWebsiteUrl()));
        content.add(makeRow("Latitudine:", String.valueOf(r.getLatitude())));
        content.add(makeRow("Longitudine:", String.valueOf(r.getLongitude())));

        content.add(Box.createVerticalStrut(25));

        // =======================
        //   SERVIZI DISPONIBILI
        // =======================
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

        // =======================
        //   FACILITIES
        // =======================
        content.add(sectionTitle("Servizi del locale:"));
        content.add(bigTextBox(r.getFacilitiesAndServices(), 120));

        content.add(Box.createVerticalStrut(30));

        // =======================
        //   DESCRIZIONE
        // =======================
        content.add(sectionTitle("Descrizione:"));
        content.add(bigTextBox(r.getDescription(), 200));

        content.add(Box.createVerticalStrut(40));

        // SCROLLPANE
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        container.add(scrollPane, BorderLayout.CENTER);

        // ----- FOOTER -----
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.WHITE);

        JButton preferitiBtn = new JButton("Aggiungi ai preferiti");
        preferitiBtn.setBackground(new Color(230, 57, 70));
        preferitiBtn.setForeground(Color.WHITE);

        JButton closeBtn = new JButton("Chiudi");
        closeBtn.addActionListener(e -> dispose());

        footer.add(preferitiBtn);
        footer.add(closeBtn);

        container.add(footer, BorderLayout.SOUTH);

        setContentPane(container);
        setVisible(true);
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
