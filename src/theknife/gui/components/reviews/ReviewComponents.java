package theknife.gui.components.reviews;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import theknife.controller.GestioneRecensioni;
import theknife.gui.components.SectionPanels;
import theknife.model.Recensione;

public class ReviewComponents {

    public static JPanel cardRecensione(Recensione r, boolean consentiRisposta, boolean consentiEliminazione) {

        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 57, 70), 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel author = new JLabel("@" + r.getAutoreUsername());
        author.setFont(new Font("Segoe UI", Font.BOLD, 14));
        author.setForeground(new Color(40, 40, 40));

        String info = r.getRistoranteNome();
        if (r.getRistoranteCitta() != null && !r.getRistoranteCitta().isBlank()) {
            info += " (" + r.getRistoranteCitta() + ")";
        }
        JLabel restaurant = new JLabel(info);
        restaurant.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        restaurant.setForeground(new Color(120, 120, 120));

        JLabel rating = new JLabel("Stelle: " + r.getStelle() + "/5");
        rating.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rating.setForeground(new Color(90, 90, 90));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(Color.WHITE);
        left.add(author);
        left.add(restaurant);

        header.add(left, BorderLayout.WEST);
        header.add(rating, BorderLayout.EAST);
        p.add(header, BorderLayout.NORTH);

        JTextArea body = new JTextArea(r.getTesto());
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setEditable(false);
        body.setBackground(new Color(250, 250, 250));
        body.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 235, 235), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        p.add(body, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        if (r.getRispostaRistoratore() != null && !r.getRispostaRistoratore().isBlank()) {
            JTextArea rep = new JTextArea("Risposta del ristoratore:\n" + r.getRispostaRistoratore());
            rep.setLineWrap(true);
            rep.setWrapStyleWord(true);
            rep.setEditable(false);
            rep.setBackground(new Color(246, 255, 247));
            rep.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 245, 225), 1),
                    BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
            bottom.add(rep, BorderLayout.CENTER);
        } else if (consentiRisposta || consentiEliminazione) {
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
            actions.setBackground(Color.WHITE);

            if (consentiEliminazione) {
                JButton elimina = new JButton("Elimina");
                elimina.addActionListener(e -> {
                    int scelta = JOptionPane.showConfirmDialog(
                            p,
                            "Vuoi eliminare questa recensione?",
                            "Conferma eliminazione",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (scelta == JOptionPane.YES_OPTION) {
                        boolean removed = GestioneRecensioni.rimuoviRecensione(
                                r,
                                SectionPanels.CURRENT_USERNAME
                        );
                        if (!removed) {
                            JOptionPane.showMessageDialog(
                                    p,
                                    "Impossibile eliminare la recensione.",
                                    "Errore",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }

                        JOptionPane.showMessageDialog(p, "Recensione eliminata", "OK",
                                JOptionPane.INFORMATION_MESSAGE);
                        if (SectionPanels.NAVIGATE != null) {
                            SectionPanels.NAVIGATE.accept(ReviewsPanelFactory.creaRecensioniCliente());
                        } else {
                            Container parent = p.getParent();
                            if (parent != null) {
                                parent.remove(p);
                                parent.revalidate();
                                parent.repaint();
                            }
                        }
                    }
                });
                actions.add(elimina);
            }

            if (consentiRisposta) {
                JButton rispondi = new JButton("Rispondi");
                rispondi.addActionListener(e -> {
                    String risposta = chiediTesto("Scrivi la risposta (una sola, definitiva):");
                    if (risposta != null && !risposta.isBlank()) {
                        GestioneRecensioni.rispondiRistoratore(
                                r.getRistoranteId(),
                                r.getAutoreUsername(),
                                risposta.trim()
                        );
                        JOptionPane.showMessageDialog(p, "Risposta inviata", "OK",
                                JOptionPane.INFORMATION_MESSAGE);

                        Window w = SwingUtilities.getWindowAncestor(p);
                        if (w != null) w.dispose();
                        apriDialogRecensioni(r.getRistoranteId(), r.getRistoranteNome());
                    }
                });
                actions.add(rispondi);
            }

            bottom.add(actions, BorderLayout.EAST);
        }

        p.add(bottom, BorderLayout.SOUTH);
        return p;
    }

    public static void apriDialogRecensioni(int ristoranteId, String ristoranteNome) {

        JDialog d = new JDialog((Frame) null, "Recensioni - " + ristoranteNome, true);
        d.setSize(560, 520);
        d.setLocationRelativeTo(null);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);

        JLabel title = new JLabel("Recensioni di " + ristoranteNome);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        container.add(title, BorderLayout.NORTH);

        List<Recensione> elenco =
                GestioneRecensioni.getRecensioniRistorante(ristoranteId);

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
                list.add(cardRecensione(r, consentiRisposta, false));
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
