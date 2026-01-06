package theknife.gui.components.reviews;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import theknife.controller.GestioneRecensioni;
import theknife.controller.GestioneRistoranti;
import theknife.gui.components.SectionPanels;
import theknife.model.Recensione;

public class ReviewsPanelFactory {

    public static JPanel creaRecensioniCliente() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("⭐ Le mie recensioni", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(230, 57, 70));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        List<Recensione> mie =
                GestioneRecensioni.getRecensioniUtente(SectionPanels.CURRENT_USERNAME);

        if (mie.isEmpty()) {
            JPanel ph = new JPanel();
            ph.setBackground(Color.WHITE);
            ph.add(new JLabel("Non hai ancora lasciato recensioni."));
            panel.add(ph, BorderLayout.CENTER);
            return panel;
        }

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(Color.WHITE);

        for (Recensione r : mie) {
            list.add(ReviewComponents.cardRecensione(r, false, true));
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

        List<Integer> ids =
                GestioneRistoranti.getRistorantiIdsProprietario(SectionPanels.CURRENT_USERNAME);
        List<Recensione> ricevute = GestioneRecensioni.getRecensioniRistoranti(ids);

        if (ricevute.isEmpty()) {
            JPanel ph = new JPanel();
            ph.setBackground(Color.WHITE);
            ph.add(new JLabel("Nessuna recensione per i tuoi ristoranti."));
            panel.add(ph, BorderLayout.CENTER);
            return panel;
        }

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(Color.WHITE);

        for (Recensione r : ricevute) {
            list.add(ReviewComponents.cardRecensione(r, true, false));
            list.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }
}
