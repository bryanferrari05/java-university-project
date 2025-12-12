package theknife.gui.components.restaurants;

import java.awt.*;
import javax.swing.*;
import java.util.List;

import theknife.gui.components.SectionPanels;
import theknife.controller.GestioneRistoranti;

public class MyRestaurantsPanelFactory {

    public static JPanel creaMieiRistoranti() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("🏨 I miei ristoranti", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(230, 57, 70));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        List<GestioneRistoranti.Ristorante> miei =
                GestioneRistoranti.getRistorantiProprietario(
                        SectionPanels.CURRENT_USERNAME
                );

        if (miei.isEmpty()) {
            JLabel msg = new JLabel("Non hai ancora inserito ristoranti.");
            msg.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(msg);
        } else {
            for (GestioneRistoranti.Ristorante r : miei) {

                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 57, 70), 1),
                        BorderFactory.createEmptyBorder(10, 14, 10, 14)
                ));
                card.setBackground(Color.WHITE);

                JLabel nome = new JLabel(
                        r.getNome() + " • " + r.getTipoCucina()
                );
                nome.setFont(new Font("Segoe UI", Font.BOLD, 15));

                JLabel info = new JLabel(
                        r.getCitta() + " • " + r.getFasciaPrezzo()
                );
                info.setForeground(new Color(90, 90, 90));

                JPanel text = new JPanel();
                text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
                text.setBackground(Color.WHITE);
                text.add(nome);
                text.add(info);

                card.add(text, BorderLayout.CENTER);

                JButton recensioniBtn = new JButton("Visualizza recensioni");
                recensioniBtn.addActionListener(e -> {
                    JOptionPane.showMessageDialog(
                            panel,
                            "Visualizzazione recensioni di " + r.getNome(),
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                });

                card.add(recensioniBtn, BorderLayout.EAST);

                content.add(card);
                content.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Color.WHITE);

        JButton addBtn = new JButton("➕ Aggiungi ristorante");
        addBtn.addActionListener(e -> {
            new theknife.gui.components.DialogNuovoRistorante(
                    null,
                    SectionPanels.CURRENT_USERNAME
            ).setVisible(true);

            SectionPanels.NAVIGATE.accept(creaMieiRistoranti());
        });

        bottom.add(addBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }
}
