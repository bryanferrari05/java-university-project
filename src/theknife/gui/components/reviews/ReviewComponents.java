package theknife.gui.components.reviews;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import theknife.controller.GestioneRecensioni;
import theknife.gui.components.SectionPanels;
import theknife.model.Recensione;

/**
 * Review UI builders and dialogs.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class ReviewComponents {

    /**
     * Creates a new ReviewComponents instance.
     */
    public ReviewComponents() {
    }

    /**
     * Builds a review card with optional actions.
     *
     * @param r review instance
     * @param consentiRisposta allow owner reply
     * @param consentiEliminazione allow deletion
     * @param consentiModifica allow editing
     * @return card panel
     */
    public static JPanel cardRecensione(Recensione r, boolean consentiRisposta,
                                        boolean consentiEliminazione, boolean consentiModifica) {

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
        } else if (consentiRisposta || consentiEliminazione || consentiModifica) {
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
            actions.setBackground(Color.WHITE);

            if (consentiModifica) {
                JButton modifica = new JButton("Modifica");
                modifica.addActionListener(e -> {
                    Integer nuovoVoto = chiediStelle(p, r.getStelle());
                    String nuovoTesto = chiediTestoArea(p, "Modifica recensione:", r.getTesto());

                    if (nuovoVoto == null || nuovoTesto == null) return;
                    nuovoTesto = nuovoTesto.trim();
                    if (nuovoTesto.isBlank()) {
                        JOptionPane.showMessageDialog(
                                p,
                                "Inserisci il testo della recensione.",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    boolean updated = GestioneRecensioni.aggiornaRecensione(
                            r,
                            SectionPanels.CURRENT_USERNAME,
                            nuovoVoto,
                            nuovoTesto
                    );

                    if (!updated) {
                        JOptionPane.showMessageDialog(
                                p,
                                "Impossibile modificare la recensione.",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    rating.setText("Stelle: " + r.getStelle() + "/5");
                    body.setText(r.getTesto());
                    JOptionPane.showMessageDialog(p, "Recensione modificata", "OK",
                            JOptionPane.INFORMATION_MESSAGE);

                    if (SectionPanels.NAVIGATE != null) {
                        SectionPanels.NAVIGATE.accept(ReviewsPanelFactory.creaRecensioniCliente());
                    }
                });
                actions.add(modifica);
            }

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

    /**
     * Opens a modal dialog listing reviews for a restaurant.
     *
     * @param ristoranteId restaurant id
     * @param ristoranteNome restaurant name
     */
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
                list.add(cardRecensione(r, consentiRisposta, false, false));
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

    /**
     * Prompts the user for a short text input.
     *
     * @param prompt message to show
     * @return input text or null
     */
    private static String chiediTesto(String prompt) {
        return JOptionPane.showInputDialog(null, prompt, "");
    }

    /**
     * Prompts the user for a star rating.
     *
     * @param parent parent component
     * @param corrente current rating
     * @return selected rating or null
     */
    private static Integer chiediStelle(Component parent, int corrente) {
        JComboBox<String> stelleBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        stelleBox.setSelectedItem(String.valueOf(corrente));
        int result = JOptionPane.showConfirmDialog(
                parent,
                stelleBox,
                "Seleziona stelle (1-5)",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) return null;
        return Integer.parseInt((String) stelleBox.getSelectedItem());
    }

    /**
     * Prompts the user for a multi-line text.
     *
     * @param parent parent component
     * @param titolo dialog title
     * @param testoIniziale initial text
     * @return entered text or null
     */
    private static String chiediTestoArea(Component parent, String titolo, String testoIniziale) {
        JTextArea area = new JTextArea(6, 24);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setText(testoIniziale);
        JScrollPane scroll = new JScrollPane(area);
        int result = JOptionPane.showConfirmDialog(
                parent,
                scroll,
                titolo,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) return null;
        return area.getText();
    }
}
