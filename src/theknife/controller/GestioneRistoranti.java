package theknife.controller;

import java.util.ArrayList;
import java.util.List;

public class GestioneRistoranti {

    public static class Ristorante {
        private final String nome;
        private final String citta;
        private final String indirizzo;
        private final String tipoCucina;
        private final String fasciaPrezzo;
        private final boolean delivery;
        private final boolean prenotazione;
        private final String proprietario;

        public Ristorante(String nome, String citta, String indirizzo, String tipoCucina, String fasciaPrezzo, boolean delivery, boolean prenotazione, String proprietario) {
            this.nome = nome;
            this.citta = citta;
            this.indirizzo = indirizzo;
            this.tipoCucina = tipoCucina;
            this.fasciaPrezzo = fasciaPrezzo;
            this.delivery = delivery;
            this.prenotazione = prenotazione;
            this.proprietario = proprietario;
        }

        public String getNome() { return nome; }
        public String getCitta() { return citta; }
        public String getIndirizzo() { return indirizzo; }
        public String getTipoCucina() { return tipoCucina; }
        public String getFasciaPrezzo() { return fasciaPrezzo; }
        public boolean isDelivery() { return delivery; }
        public boolean isPrenotazione() { return prenotazione; }
        public String getProprietario() { return proprietario; }
    }

    private static final List<Ristorante> RISTORANTI = new ArrayList<>();

    public static synchronized void aggiungiRistorante(Ristorante r) {
        RISTORANTI.add(r);
    }

    public static synchronized List<Ristorante> getRistorantiProprietario(String username) {
        List<Ristorante> miei = new ArrayList<>();
        for (Ristorante r : RISTORANTI) {
            if (r.getProprietario().equalsIgnoreCase(username)) miei.add(r);
        }
        return miei;
    }

    public static synchronized void loadFromCSV(String path) {
    }

    public static synchronized void saveToCSV(String path) {
    }
}
