package theknife.controller;

import theknife.model.Recensione;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Logica in-memoria per le recensioni.
 * CSV: sono predisposti i metodi stub load/save per l'integrazione del backend.
 */
public class GestioneRecensioni {

    private static final List<Recensione> RECENSIONI = new ArrayList<>();

    public static synchronized boolean utenteHaRecensito(String username, String ristoranteNome) {
        return RECENSIONI.stream()
                .anyMatch(r -> r.getRistoranteNome().equalsIgnoreCase(ristoranteNome)
                        && r.getAutoreUsername().equalsIgnoreCase(username));
    }

    public static synchronized void aggiungiRecensione(String username, String ristoranteNome, int stelle, String testo) {
        if (utenteHaRecensito(username, ristoranteNome)) return;
        RECENSIONI.add(new Recensione(ristoranteNome, username, stelle, testo));
    }

    public static synchronized List<Recensione> getRecensioniRistorante(String ristoranteNome) {
        return RECENSIONI.stream()
                .filter(r -> r.getRistoranteNome().equalsIgnoreCase(ristoranteNome))
                .collect(Collectors.toList());
    }

    public static synchronized List<Recensione> getRecensioniUtente(String username) {
        return RECENSIONI.stream()
                .filter(r -> r.getAutoreUsername().equalsIgnoreCase(username))
                .collect(Collectors.toList());
    }

    public static synchronized void rispondiRistoratore(String ristoranteNome, String autoreRecensione, String risposta) {
        for (Recensione r : RECENSIONI) {
            if (r.getRistoranteNome().equalsIgnoreCase(ristoranteNome)
                    && r.getAutoreUsername().equalsIgnoreCase(autoreRecensione)) {
                if (r.getRispostaRistoratore() == null || r.getRispostaRistoratore().isBlank()) {
                    r.setRispostaRistoratore(risposta);
                }
                return;
            }
        }
    }

    public static synchronized double getMediaRistorante(String ristoranteNome) {
        IntSummaryStatistics stats = RECENSIONI.stream()
                .filter(r -> r.getRistoranteNome().equalsIgnoreCase(ristoranteNome))
                .mapToInt(Recensione::getStelle)
                .summaryStatistics();
        if (stats.getCount() == 0) return 0.0;
        double m = (double) stats.getSum() / stats.getCount();
        return Math.round(m * 10.0) / 10.0;
    }

    public static synchronized int getConteggioRistorante(String ristoranteNome) {
        return (int) RECENSIONI.stream()
                .filter(r -> r.getRistoranteNome().equalsIgnoreCase(ristoranteNome))
                .count();
    }

    // ================= CSV STUB (da collegare al backend) =================

    public static synchronized void loadRecensioniDaCSV(String pathCsv) {
        // TODO: il tuo compagno caricherà qui le recensioni da CSV in RECENSIONI
        // formato proposto (es.): ristorante;autore;stelle;testo;rispostaRistoratore
    }

    public static synchronized void saveRecensioniSuCSV(String pathCsv) {
        // TODO: il tuo compagno salverà qui le recensioni RECENSIONI su CSV
    }
}
