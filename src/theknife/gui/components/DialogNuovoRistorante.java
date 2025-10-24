package theknife.gui.components;

import theknife.controller.GestioneRistoranti;

import javax.swing.*;
import java.awt.*;

public class DialogNuovoRistorante extends JDialog {
    private final JTextField nomeField;
    private final JTextField cittaField;
    private final JTextField indirizzoField;
    private final JTextField tipoField;
    private final JComboBox<String> fasciaBox;
    private final JCheckBox deliveryBox;
    private final JCheckBox prenotazioneBox;

    public DialogNuovoRistorante(Frame parent, String username) {
        super(parent, "Aggiungi Ristorante", true);
        setSize(420, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        nomeField = new JTextField();
        cittaField = new JTextField();
        indirizzoField = new JTextField();
        tipoField = new JTextField();
        fasciaBox = new JComboBox<>(new String[]{"<20€", "20€-50€", "50€-100€", ">100€"});
        deliveryBox = new JCheckBox("Delivery disponibile");
        prenotazioneBox = new JCheckBox("Prenotazione online");

        c.gridx = 0; c.gridy = 0; form.add(new JLabel("Nome:"), c);
        c.gridx = 1; form.add(nomeField, c);
        c.gridx = 0; c.gridy = 1; form.add(new JLabel("Città:"), c);
        c.gridx = 1; form.add(cittaField, c);
        c.gridx = 0; c.gridy = 2; form.add(new JLabel("Indirizzo:"), c);
        c.gridx = 1; form.add(indirizzoField, c);
        c.gridx = 0; c.gridy = 3; form.add(new JLabel("Tipo di cucina:"), c);
        c.gridx = 1; form.add(tipoField, c);
        c.gridx = 0; c.gridy = 4; form.add(new JLabel("Fascia di prezzo:"), c);
        c.gridx = 1; form.add(fasciaBox, c);
        c.gridx = 0; c.gridy = 5; form.add(deliveryBox, c);
        c.gridx = 1; form.add(prenotazioneBox, c);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton annulla = new JButton("Annulla");
        JButton conferma = new JButton("Conferma");
        footer.add(annulla);
        footer.add(conferma);

        add(form, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        annulla.addActionListener(e -> dispose());
        conferma.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String citta = cittaField.getText().trim();
            String indirizzo = indirizzoField.getText().trim();
            String tipo = tipoField.getText().trim();
            String fascia = (String) fasciaBox.getSelectedItem();
            boolean delivery = deliveryBox.isSelected();
            boolean prenotazione = prenotazioneBox.isSelected();

            if (nome.isEmpty() || citta.isEmpty() || indirizzo.isEmpty() || tipo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Compila tutti i campi obbligatori", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            GestioneRistoranti.aggiungiRistorante(new GestioneRistoranti.Ristorante(nome, citta, indirizzo, tipo, fascia, delivery, prenotazione, username));
            JOptionPane.showMessageDialog(this, "Ristorante aggiunto con successo", "OK", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
    }
}
