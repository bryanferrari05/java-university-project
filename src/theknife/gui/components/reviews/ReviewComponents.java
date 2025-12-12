package theknife.gui.components.reviews;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import theknife.controller.GestioneRecensioni;
import theknife.gui.components.SectionPanels;
import theknife.model.Recensione;

public class ReviewComponents {

    public static JPanel cardRecensione(Recensione r, boolean consentiRisposta) {

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 57, 70), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JLabel top = new JLabel("@" + r.getAutoreUsername() + " • " + r.getStelle() + "⭐");
        top.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
                    GestioneRecensioni.rispondiRistoratore(
                            r.getRistoranteNome(),
                            r.getAutoreUsername(),
                            risposta.trim()
                    );
                    JOptionPane.showMessageDialog(p, "Risposta inviata", "OK",
                            JOptionPane.INFORMATION_MESSAGE);

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

    public static void apriDialogRecensioni(String ristoranteNome) {

        JDialog d = new JDialog((Frame) null, "Recensioni • " + ristoranteNome, true);
        d.setSize(560, 520);
        d.setLocationRelativeTo(null);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);

        JLabel title = new JLabel("Recensioni di " + ristoranteNome);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        container.add(title, BorderLayout.NORTH);

        List<Recensione> elenco =
                GestioneRecensioni.getRecensioniRistorante(ristoranteNome);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(Color.WHITE);

        boolean consentiRisposta =
                SectionPanels.CURRENT_ROLE.equalsIgnoreCase("ristoratore");

        if (elenco.isEmpty()) {
            JPanel ph = new JPanel();
            ph.setBackground(Color.WHITE);
            ph.add(new JLabel("Nessuna recensione presente."));
            container.add(ph, BorderLayout.CENTER);
        } else {
            for (Recensione r : elenco) {
                list.add(cardRecensione(r, consentiRisposta));
                list.add(Box.createVerticalStrut(10));
            }
            JScrollPane scroll = new JScrollPane(list);
            scroll.setBorder(BorderFactory.createEmptyBorder(10, 16, 16, 16));
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            container.add(scroll, BorderLayout.CENTER);
        }

        JButton chiudi = new JButton("Chiudi");
        chiudi.addActionListener(e -> d.dispose());
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(chiudi);
        container.add(footer, BorderLayout.SOUTH);

        d.setContentPane(container);
        d.setVisible(true);
    }

    private static String chiediTesto(String prompt) {
        return JOptionPane.showInputDialog(null, prompt, "");
    }
}
