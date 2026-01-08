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
 */
public class GestioneRecensioni {

    private static final List<Recensione> RECENSIONI = new ArrayList<>();
    private static final String CSV_PATH = "data/recensioni.csv";

    static {
        loadFromCsv();
    }

    public static synchronized boolean ristoranteHaRaggiuntoLimite(int ristoranteId) {
        return false;
    }

    public static synchronized void aggiungiRecensione(int ristoranteId, String ristoranteNome, String ristoranteCitta,
                                                       String username, int stelle, String testo) {
        RECENSIONI.add(new Recensione(ristoranteId, ristoranteNome, ristoranteCitta, username, stelle, testo));
        saveToCsv();
    }

    public static synchronized List<Recensione> getRecensioniRistorante(int ristoranteId) {
        return RECENSIONI.stream()
                .filter(r -> r.getRistoranteId() == ristoranteId)
                .collect(Collectors.toList());
    }

    public static synchronized List<Recensione> getRecensioniUtente(String username) {
        return RECENSIONI.stream()
                .filter(r -> r.getAutoreUsername().equalsIgnoreCase(username))
                .collect(Collectors.toList());
    }

    public static synchronized List<Recensione> getRecensioniRistoranti(List<Integer> ristorantiIds) {
        if (ristorantiIds == null || ristorantiIds.isEmpty()) return new ArrayList<>();
        Set<Integer> set = new HashSet<>(ristorantiIds);
        return RECENSIONI.stream()
                .filter(r -> set.contains(r.getRistoranteId()))
                .collect(Collectors.toList());
    }

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

    public static synchronized boolean rimuoviRecensione(Recensione recensione, String username) {
        if (recensione == null || username == null) return false;
        if (!recensione.getAutoreUsername().equalsIgnoreCase(username)) return false;
        boolean removed = RECENSIONI.remove(recensione);
        if (removed) {
            saveToCsv();
        }
        return removed;
    }

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

    public static synchronized double getMediaRistorante(int ristoranteId) {
        IntSummaryStatistics stats = RECENSIONI.stream()
                .filter(r -> r.getRistoranteId() == ristoranteId)
                .mapToInt(Recensione::getStelle)
                .summaryStatistics();
        if (stats.getCount() == 0) return 0.0;
        double m = (double) stats.getSum() / stats.getCount();
        return Math.round(m * 10.0) / 10.0;
    }

    public static synchronized int getConteggioRistorante(int ristoranteId) {
        return (int) RECENSIONI.stream()
                .filter(r -> r.getRistoranteId() == ristoranteId)
                .count();
    }

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

    private static String[] splitCsvLine(String line) {
        if (line == null) return new String[0];
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        String v = value.replace("\"", "\"\"");
        boolean needQuotes = v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r");
        return needQuotes ? "\"" + v + "\"" : v;
    }

    private static String cleanCsvField(String value) {
        if (value == null) return "";
        String v = value.trim();
        if (v.startsWith("\"") && v.endsWith("\"") && v.length() >= 2) {
            v = v.substring(1, v.length() - 1).replace("\"\"", "\"");
        }
        return v;
    }

    private static int parseIntSafe(String value, int fallback) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return fallback;
        }
    }
}
