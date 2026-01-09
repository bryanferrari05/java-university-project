package theknife.controller;

import theknife.model.Recensione;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Logica in-memoria per le recensioni.
 * CSV: sono predisposti i metodi stub load/save per l'integrazione del backend.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class GestioneRecensioni {

    /**
     * Creates a new GestioneRecensioni instance.
     */
    public GestioneRecensioni() {
    }

    /** In-memory list of reviews. */
    private static final List<Recensione> RECENSIONI = new ArrayList<>();
    /** CSV path for reviews. */
    private static final String CSV_PATH = "data/recensioni.csv";

    static {
        loadFromCsv();
    }

    /**
     * Checks whether a restaurant reached its review limit.
     *
     * @param ristoranteId restaurant id
     * @return true if limit is reached
     */
    public static synchronized boolean ristoranteHaRaggiuntoLimite(int ristoranteId) {
        return false;
    }

    /**
     * Adds a new review and saves to CSV.
     *
     * @param ristoranteId restaurant id
     * @param ristoranteNome restaurant name
     * @param ristoranteCitta restaurant city
     * @param username author username
     * @param stelle star rating
     * @param testo review text
     */
    public static synchronized void aggiungiRecensione(int ristoranteId, String ristoranteNome, String ristoranteCitta,
                                                       String username, int stelle, String testo) {
        RECENSIONI.add(new Recensione(ristoranteId, ristoranteNome, ristoranteCitta, username, stelle, testo));
        saveToCsv();
    }

    /**
     * Returns reviews for a restaurant.
     *
     * @param ristoranteId restaurant id
     * @return list of reviews
     */
    public static synchronized List<Recensione> getRecensioniRistorante(int ristoranteId) {
        return RECENSIONI.stream()
                .filter(r -> r.getRistoranteId() == ristoranteId)
                .collect(Collectors.toList());
    }

    /**
     * Returns reviews written by a user.
     *
     * @param username username
     * @return list of reviews
     */
    public static synchronized List<Recensione> getRecensioniUtente(String username) {
        return RECENSIONI.stream()
                .filter(r -> r.getAutoreUsername().equalsIgnoreCase(username))
                .collect(Collectors.toList());
    }

    /**
     * Returns reviews for a set of restaurant ids.
     *
     * @param ristorantiIds list of ids
     * @return list of reviews
     */
    public static synchronized List<Recensione> getRecensioniRistoranti(List<Integer> ristorantiIds) {
        if (ristorantiIds == null || ristorantiIds.isEmpty()) return new ArrayList<>();
        Set<Integer> set = new HashSet<>(ristorantiIds);
        return RECENSIONI.stream()
                .filter(r -> set.contains(r.getRistoranteId()))
                .collect(Collectors.toList());
    }

    /**
     * Stores an owner reply for a specific review.
     *
     * @param ristoranteId restaurant id
     * @param autoreRecensione review author username
     * @param risposta reply text
     */
    public static synchronized void rispondiRistoratore(int ristoranteId, String autoreRecensione, String risposta) {
        for (Recensione r : RECENSIONI) {
            if (r.getRistoranteId() == ristoranteId
                    && r.getAutoreUsername().equalsIgnoreCase(autoreRecensione)) {
                if (r.getRispostaRistoratore() == null || r.getRispostaRistoratore().isBlank()) {
                    r.setRispostaRistoratore(risposta);
                    saveToCsv();
                }
                return;
            }
        }
    }

    /**
     * Removes a review authored by the given user.
     *
     * @param recensione review instance
     * @param username username
     * @return true if removed
     */
    public static synchronized boolean rimuoviRecensione(Recensione recensione, String username) {
        if (recensione == null || username == null) return false;
        if (!recensione.getAutoreUsername().equalsIgnoreCase(username)) return false;
        boolean removed = RECENSIONI.remove(recensione);
        if (removed) {
            saveToCsv();
        }
        return removed;
    }

    /**
     * Updates a review written by the given user.
     *
     * @param recensione review instance
     * @param username username
     * @param stelle new rating
     * @param testo new text
     * @return true if updated
     */
    public static synchronized boolean aggiornaRecensione(Recensione recensione, String username,
                                                          int stelle, String testo) {
        if (recensione == null || username == null) return false;
        if (!recensione.getAutoreUsername().equalsIgnoreCase(username)) return false;
        if (!RECENSIONI.contains(recensione)) return false;
        if (stelle < 1 || stelle > 5) return false;
        if (testo == null || testo.isBlank()) return false;

        recensione.setStelle(stelle);
        recensione.setTesto(testo);
        saveToCsv();
        return true;
    }

    /**
     * Calculates the average rating for a restaurant.
     *
     * @param ristoranteId restaurant id
     * @return average rating
     */
    public static synchronized double getMediaRistorante(int ristoranteId) {
        IntSummaryStatistics stats = RECENSIONI.stream()
                .filter(r -> r.getRistoranteId() == ristoranteId)
                .mapToInt(Recensione::getStelle)
                .summaryStatistics();
        if (stats.getCount() == 0) return 0.0;
        double m = (double) stats.getSum() / stats.getCount();
        return Math.round(m * 10.0) / 10.0;
    }

    /**
     * Counts reviews for a restaurant.
     *
     * @param ristoranteId restaurant id
     * @return number of reviews
     */
    public static synchronized int getConteggioRistorante(int ristoranteId) {
        return (int) RECENSIONI.stream()
                .filter(r -> r.getRistoranteId() == ristoranteId)
                .count();
    }

    /**
     * Loads reviews from the CSV file.
     */
    private static void loadFromCsv() {
        File file = new File(CSV_PATH);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    if (line.toLowerCase().startsWith("ristoranteid")) continue;
                }
                String[] c = splitCsvLine(line);
                if (c.length < 6) continue;

                int ristoranteId = parseIntSafe(cleanCsvField(c[0]), -1);
                String ristoranteNome = cleanCsvField(c[1]);
                String ristoranteCitta = cleanCsvField(c[2]);
                String username = cleanCsvField(c[3]);
                int stelle = parseIntSafe(cleanCsvField(c[4]), 0);
                String testo = cleanCsvField(c[5]);
                String risposta = c.length > 6 ? cleanCsvField(c[6]) : "";

                Recensione r = new Recensione(
                        ristoranteId, ristoranteNome, ristoranteCitta, username, stelle, testo
                );
                if (!risposta.isBlank()) {
                    r.setRispostaRistoratore(risposta);
                }
                RECENSIONI.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves reviews to the CSV file.
     */
    private static void saveToCsv() {
        try (FileWriter fw = new FileWriter(CSV_PATH)) {
            fw.write("ristoranteId,ristoranteNome,ristoranteCitta,username,stelle,testo,rispostaRistoratore\n");
            for (Recensione r : RECENSIONI) {
                fw.write(r.getRistoranteId() + ",");
                fw.write(escapeCsv(r.getRistoranteNome()));
                fw.write(",");
                fw.write(escapeCsv(r.getRistoranteCitta()));
                fw.write(",");
                fw.write(escapeCsv(r.getAutoreUsername()));
                fw.write(",");
                fw.write(r.getStelle() + ",");
                fw.write(escapeCsv(r.getTesto()));
                fw.write(",");
                String risposta = r.getRispostaRistoratore() == null ? "" : r.getRispostaRistoratore();
                fw.write(escapeCsv(risposta));
                fw.write("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Splits a CSV line preserving quoted commas.
     *
     * @param line raw line
     * @return fields array
     */
    private static String[] splitCsvLine(String line) {
        if (line == null) return new String[0];
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    /**
     * Escapes a value for CSV output.
     *
     * @param value raw value
     * @return escaped value
     */
    private static String escapeCsv(String value) {
        if (value == null) return "";
        String v = value.replace("\"", "\"\"");
        boolean needQuotes = v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r");
        return needQuotes ? "\"" + v + "\"" : v;
    }

    /**
     * Unquotes and trims a CSV field.
     *
     * @param value raw field
     * @return cleaned value
     */
    private static String cleanCsvField(String value) {
        if (value == null) return "";
        String v = value.trim();
        if (v.startsWith("\"") && v.endsWith("\"") && v.length() >= 2) {
            v = v.substring(1, v.length() - 1).replace("\"\"", "\"");
        }
        return v;
    }

    /**
     * Parses an integer with fallback.
     *
     * @param value raw value
     * @param fallback fallback value
     * @return parsed integer or fallback
     */
    private static int parseIntSafe(String value, int fallback) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return fallback;
        }
    }
}
