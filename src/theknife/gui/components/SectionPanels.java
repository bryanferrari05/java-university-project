package theknife.gui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SectionPanels {

    private static final Color ROW_COLOR_1 = new Color(245, 245, 245);
    private static final Color ROW_COLOR_2 = new Color(230, 230, 230);
    private static final Color SELECT_COLOR = new Color(100, 149, 237); // cornflower blue

    // ------------------- Style tabella -------------------
    private static void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.setSelectionBackground(SELECT_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);

        table.setDefaultRenderer(Object.class, (tbl, value, isSelected, hasFocus, row, column) -> {
            Component c = new JLabel(value.toString());
            if (!isSelected) c.setBackground(row % 2 == 0 ? ROW_COLOR_1 : ROW_COLOR_2);
            else c.setBackground(SELECT_COLOR);
            ((JLabel) c).setOpaque(true);
            return c;
        });

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(60,60,60));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
    }

    // ------------------- Cerca Ristoranti -------------------
    public static JPanel creaCercaRistoranti() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        JTextField searchField = new JTextField(15);
        JComboBox<String> cucinaFilter = new JComboBox<>(new String[]{"Tutte","Italiana","Giapponese","Fast Food"});
        JTextField distanzaField = new JTextField(5);
        JButton searchButton = new JButton("Cerca");
        searchButton.setBackground(new Color(60,149,237));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        searchPanel.add(new JLabel("Nome ristorante:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Tipo cucina:"));
        searchPanel.add(cucinaFilter);
        searchPanel.add(new JLabel("Distanza max (km):"));
        searchPanel.add(distanzaField);
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"Nome","Tipo Cucina","Distanza","Rating"};
        Object[][] data = {
                {"Trattoria Bella","Italiana",1.2,4.5},
                {"Sushi King","Giapponese",3.5,4.7},
                {"Burger House","Fast Food",2.0,4.2},
                {"La Piadina","Italiana",0.8,4.0}
        };
        DefaultTableModel model = new DefaultTableModel(data, columns){
            @Override public boolean isCellEditable(int row,int col){return false;}
        };
        JTable table = new JTable(model);
        styleTable(table);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton detailsButton = new JButton("Mostra Dettagli");
        detailsButton.setBackground(new Color(60,149,237));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(detailsButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> {
            String nome = searchField.getText().trim().toLowerCase();
            String tipo = cucinaFilter.getSelectedItem().toString();
            double distanzaMax = 999;
            try{
                if(!distanzaField.getText().trim().isEmpty()){
                    distanzaMax = Double.parseDouble(distanzaField.getText().trim());
                }
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(panel,"Inserisci un numero valido per la distanza.","Errore",JOptionPane.ERROR_MESSAGE);
                return;
            }
            table.clearSelection();
            for(int i=0;i<model.getRowCount();i++){
                String rNome = model.getValueAt(i,0).toString().toLowerCase();
                String rTipo = model.getValueAt(i,1).toString();
                double rDistanza = Double.parseDouble(model.getValueAt(i,2).toString());
                if(rNome.contains(nome) && (tipo.equals("Tutte") || rTipo.equals(tipo)) && rDistanza<=distanzaMax){
                    table.addRowSelectionInterval(i,i);
                }
            }
        });

        detailsButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if(selectedRow == -1){
                JOptionPane.showMessageDialog(panel,"Seleziona un ristorante prima.","Info",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String nomeR = table.getValueAt(selectedRow,0).toString();
            String tipoR = table.getValueAt(selectedRow,1).toString();
            String distanzaR = table.getValueAt(selectedRow,2).toString();
            String ratingR = table.getValueAt(selectedRow,3).toString();
            JOptionPane.showMessageDialog(panel,
                    "Ristorante: "+nomeR+"\nTipo cucina: "+tipoR+"\nDistanza: "+distanzaR+" km\nRating: "+ratingR,
                    "Dettagli ristorante",JOptionPane.INFORMATION_MESSAGE);
        });

        return panel;
    }

    // ------------------- I miei Preferiti -------------------
    public static JPanel creaMieiPreferiti(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        String[] preferiti = {"Sushi King","Trattoria Bella","La Piadina"};
        JList<String> list = new JList<>(preferiti);
        list.setFont(new Font("Segoe UI", Font.PLAIN,14));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton detailsButton = new JButton("Mostra Dettagli");
        detailsButton.setBackground(new Color(60,149,237));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.addActionListener(e -> {
            String selected = list.getSelectedValue();
            if(selected==null){
                JOptionPane.showMessageDialog(panel,"Seleziona un ristorante prima.","Info",JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(panel,"Ristorante preferito: "+selected+"\nDettagli fittizi qui.","Dettagli ristorante",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panel.add(new JLabel("I tuoi ristoranti preferiti:", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(detailsButton);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    // ------------------- I miei Ristoranti -------------------
    public static JPanel creaMieiRistoranti(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        String[] mieiRistoranti = {"Trattoria Bella","La Piadina"};
        JList<String> list = new JList<>(mieiRistoranti);
        list.setFont(new Font("Segoe UI",Font.PLAIN,14));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton modifyButton = new JButton("Modifica Ristorante");
        modifyButton.setBackground(new Color(60,149,237));
        modifyButton.setForeground(Color.WHITE);
        modifyButton.setFocusPainted(false);
        modifyButton.addActionListener(e -> {
            String selected = list.getSelectedValue();
            if(selected==null){
                JOptionPane.showMessageDialog(panel,"Seleziona un ristorante prima.","Info",JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(panel,"Modifica ristorante: "+selected+"\nFunzionalità futura.","Modifica",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panel.add(new JLabel("I tuoi ristoranti gestiti:",SwingConstants.CENTER),BorderLayout.NORTH);
        panel.add(new JScrollPane(list),BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(modifyButton);
        panel.add(bottom,BorderLayout.SOUTH);

        return panel;
    }

    // ------------------- Recensioni Cliente -------------------
    public static JPanel creaRecensioniCliente(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        String[] columns = {"Ristorante","Voto","Commento"};
        Object[][] data = {
                {"Trattoria Bella",5,"Ottimo servizio!"},
                {"Sushi King",4,"Sushi fresco, ambiente gradevole."}
        };
        DefaultTableModel model = new DefaultTableModel(data,columns){
            @Override public boolean isCellEditable(int row,int col){return false;}
        };
        JTable table = new JTable(model);
        styleTable(table);

        JButton detailsButton = new JButton("Dettagli Recensione");
        detailsButton.setBackground(new Color(60,149,237));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row==-1){
                JOptionPane.showMessageDialog(panel,"Seleziona una recensione prima.","Info",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String ristorante = table.getValueAt(row,0).toString();
            String voto = table.getValueAt(row,1).toString();
            String commento = table.getValueAt(row,2).toString();
            JOptionPane.showMessageDialog(panel,"Ristorante: "+ristorante+"\nVoto: "+voto+"\nCommento: "+commento,"Dettagli recensione",JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(new JScrollPane(table),BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(detailsButton);
        panel.add(bottom,BorderLayout.SOUTH);

        return panel;
    }

    // ------------------- Recensioni Ristoratore -------------------
    public static JPanel creaRecensioniRistoratore(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        String[] columns = {"Cliente","Voto","Commento"};
        Object[][] data = {
                {"Mario Rossi",5,"Ottima esperienza!"},
                {"Lucia Bianchi",4,"Buono, servizio veloce."},
                {"Paolo Verdi",3,"Cibo discreto, locale rumoroso."}
        };
        DefaultTableModel model = new DefaultTableModel(data,columns){
            @Override public boolean isCellEditable(int row,int col){return false;}
        };
        JTable table = new JTable(model);
        styleTable(table);

        JButton detailsButton = new JButton("Dettagli Recensione");
        detailsButton.setBackground(new Color(60,149,237));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row==-1){
                JOptionPane.showMessageDialog(panel,"Seleziona una recensione prima.","Info",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String cliente = table.getValueAt(row,0).toString();
            String voto = table.getValueAt(row,1).toString();
            String commento = table.getValueAt(row,2).toString();
            JOptionPane.showMessageDialog(panel,"Cliente: "+cliente+"\nVoto: "+voto+"\nCommento: "+commento,"Dettagli recensione",JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(new JScrollPane(table),BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(detailsButton);
        panel.add(bottom,BorderLayout.SOUTH);

        return panel;
    }

    // ------------------- Recensioni Gestore Generico -------------------
    public static JPanel creaRecensioni(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        String[] columns = {"Ristorante","Voto","Commento"};
        Object[][] data = {
                {"Trattoria Bella",5,"Ottimo servizio!"},
                {"Sushi King",4,"Sushi fresco, ambiente gradevole."},
                {"Burger House",3,"Buono ma troppo affollato."}
        };
        DefaultTableModel model = new DefaultTableModel(data,columns){
            @Override public boolean isCellEditable(int row,int col){return false;}
        };
        JTable table = new JTable(model);
        styleTable(table);

        JButton detailsButton = new JButton("Dettagli Recensione");
        detailsButton.setBackground(new Color(60,149,237));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row==-1){
                JOptionPane.showMessageDialog(panel,"Seleziona una recensione prima.","Info",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String ristorante = table.getValueAt(row,0).toString();
            String voto = table.getValueAt(row,1).toString();
            String commento = table.getValueAt(row,2).toString();
            JOptionPane.showMessageDialog(panel,"Ristorante: "+ristorante+"\nVoto: "+voto+"\nCommento: "+commento,"Dettagli recensione",JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(new JScrollPane(table),BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(detailsButton);
        panel.add(bottom,BorderLayout.SOUTH);

        return panel;
    }
}
