package theknife.gui.components.home;

import java.awt.*;
import javax.swing.*;

import theknife.gui.components.SectionPanels;
import theknife.gui.components.search.RistoranteRepository;
import theknife.model.Ristorante;
import theknife.model.User;

public class HomePanelFactory {

    public static JPanel creaHomeBenvenuto() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        User user = SectionPanels.CURRENT_USER;

        String titolo;
        String sottotitolo;

        boolean isGuest = user == null || user.getRuolo().equalsIgnoreCase("ospite");
        boolean isCliente = !isGuest && user.getRuolo().equalsIgnoreCase("cliente");

        if (isGuest) {
            titolo = "Benvenuto in TheKnife!";
            sottotitolo = "Registrati o accedi per utilizzare le funzionalità della piattaforma.";
        } else {
            titolo = "Benvenuto in TheKnife " + user.getUsername() + "!";
            sottotitolo = "Spostati nei menu per utilizzare le funzionalità della piattaforma.";
        }

        JLabel titleLabel = new JLabel(titolo, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(230, 57, 70));

        JLabel subtitleLabel = new JLabel(sottotitolo, SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(80, 80, 80));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(80, 40, 80, 40));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(titleLabel);
        center.add(Box.createVerticalStrut(20));
        center.add(subtitleLabel);

        if (isCliente) {
            JPanel nearby = creaRistorantiCitta(user.getResidenza());
            if (nearby != null) {
                center.add(Box.createVerticalStrut(30));
                center.add(nearby);
            }
        }

        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel creaRistorantiCitta(String residenza) {
        if (residenza == null || residenza.trim().isEmpty()) return null;

        String city = residenza.trim().toLowerCase();
        java.util.List<Ristorante> tutti = RistoranteRepository.getRistoranti();
        java.util.List<Ristorante> risultati = new java.util.ArrayList<>();

        for (Ristorante r : tutti) {
            if (r.getCitta() != null && r.getCitta().toLowerCase().contains(city)) {
                risultati.add(r);
                if (risultati.size() == 6) break;
            }
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);

        JLabel sectionTitle = new JLabel("Ristoranti nella tua citta: " + residenza, SwingConstants.CENTER);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(230, 57, 70));
        wrapper.add(sectionTitle, BorderLayout.NORTH);
        wrapper.add(Box.createVerticalStrut(12), BorderLayout.CENTER);

        if (risultati.isEmpty()) {
            JLabel empty = new JLabel("Nessun ristorante trovato per la tua citta.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            empty.setForeground(new Color(90, 90, 90));
            wrapper.add(empty, BorderLayout.CENTER);
            return wrapper;
        }

        JPanel grid = new JPanel(new GridLayout(3, 2, 12, 12));
        grid.setBackground(Color.WHITE);

        for (Ristorante r : risultati) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 57, 70), 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));

            JLabel name = new JLabel(r.getNome());
            name.setFont(new Font("Segoe UI", Font.BOLD, 14));

            String infoText = r.getTipoCucina() + " | " + r.getFasciaPrezzo();
            JLabel info = new JLabel(infoText);
            info.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            info.setForeground(new Color(90, 90, 90));

            JPanel text = new JPanel();
            text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
            text.setBackground(Color.WHITE);
            text.add(name);
            text.add(info);

            card.add(text, BorderLayout.CENTER);
            grid.add(card);
        }

        while (grid.getComponentCount() < 6) {
            JPanel filler = new JPanel();
            filler.setBackground(Color.WHITE);
            grid.add(filler);
        }

        wrapper.add(grid, BorderLayout.SOUTH);
        return wrapper;
    }
}
