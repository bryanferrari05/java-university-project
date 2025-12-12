package theknife.gui.components.favorites;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import theknife.gui.components.SectionPanels;
import theknife.gui.components.RestaurantDetailsFrame;
import theknife.gui.components.search.RistoranteRepository;
import theknife.model.Ristorante;
import theknife.model.User;

public class FavoritesPanelFactory {

    public static JPanel creaMieiPreferiti() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("❤️ I miei preferiti", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(230, 57, 70));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        User user = SectionPanels.CURRENT_USER;

        if (user == null) {
            JPanel ph = new JPanel();
            ph.setBackground(Color.WHITE);
            ph.add(new JLabel("Non sei loggato."));
            panel.add(ph, BorderLayout.CENTER);
            return panel;
        }

        List<Integer> preferiti = user.getPreferiti();

        if (preferiti.isEmpty()) {
            JPanel placeholder = new JPanel();
            placeholder.setBackground(Color.WHITE);
            placeholder.add(new JLabel("Nessun ristorante preferito al momento."));
            panel.add(placeholder, BorderLayout.CENTER);
            return panel;
        }

        List<Ristorante> tutti = RistoranteRepository.getRistoranti();

        JPanel list = new JPanel();
        list.setBackground(Color.WHITE);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        for (Integer id : preferiti) {

            if (id < 0 || id >= tutti.size()) continue;

            Ristorante r = tutti.get(id);

            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 57, 70), 1),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)
            ));
            card.setBackground(Color.WHITE);

            JLabel nome = new JLabel(r.getNome() + " • " + r.getTipoCucina());
            nome.setFont(new Font("Segoe UI", Font.BOLD, 15));

            JLabel info = new JLabel(r.getCitta() + " • " + r.getFasciaPrezzo());
            info.setForeground(new Color(90, 90, 90));

            JPanel text = new JPanel();
            text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
            text.setBackground(Color.WHITE);
            text.add(nome);
            text.add(info);

            card.add(text, BorderLayout.CENTER);

            JButton dettagli = new JButton("Dettagli");
            dettagli.addActionListener(e -> {
                new RestaurantDetailsFrame(user, id, r);
            });

            card.add(dettagli, BorderLayout.EAST);

            list.add(card);
            list.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }
}
