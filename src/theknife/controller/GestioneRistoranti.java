package theknife.controller;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;

/**
 * Manages restaurant data loaded from CSV and append operations.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class GestioneRistoranti {

    /**
     * Creates a new GestioneRistoranti instance.
     */
    public GestioneRistoranti() {
    }

    /**
     * Lightweight restaurant record owned by a user.
     *
     * @author Bryan Ferrari
 * @author Jacopo Fabbri
     */
    public static class Ristorante {
        /** Restaurant name. */
        private final String nome;
        /** City name. */
        private final String citta;
        /** Street address. */
        private final String indirizzo;
        /** Cuisine type. */
        private final String tipoCucina;
        /** Price band. */
        private final String fasciaPrezzo;
        /** Delivery availability. */
        private final boolean delivery;
        /** Reservation availability. */
        private final boolean prenotazione;
        /** Owner username. */
        private final String proprietario;

        /**
         * Builds a restaurant record for owners.
         *
         * @param nome name
         * @param citta city
         * @param indirizzo address
         * @param tipoCucina cuisine type
         * @param fasciaPrezzo price band
         * @param delivery delivery flag
         * @param prenotazione reservation flag
         * @param proprietario owner username
         */
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

        /**
         * Returns restaurant name.
         *
         * @return restaurant name
         */
        public String getNome() { return nome; }
        /**
         * Returns city name.
         *
         * @return city name
         */
        public String getCitta() { return citta; }
        /**
         * Returns address.
         *
         * @return address
         */
        public String getIndirizzo() { return indirizzo; }
        /**
         * Returns cuisine type.
         *
         * @return cuisine type
         */
        public String getTipoCucina() { return tipoCucina; }
        /**
         * Returns price band.
         *
         * @return price band
         */
        public String getFasciaPrezzo() { return fasciaPrezzo; }
        /**
         * Returns true if delivery is available.
         *
         * @return true if delivery is available
         */
        public boolean isDelivery() { return delivery; }
        /**
         * Returns true if reservation is available.
         *
         * @return true if reservation is available
         */
        public boolean isPrenotazione() { return prenotazione; }
        /**
         * Returns owner username.
         *
         * @return owner username
         */
        public String getProprietario() { return proprietario; }
    }

    /** In-memory list of restaurants. */
    private static final List<Ristorante> RISTORANTI = new ArrayList<>();
    /** CSV path for restaurants. */
    private static final String CSV_PATH = "data/ristoranti.csv";
    /** CSV path for users. */
    private static final String UTENTI_PATH = "data/utenti.csv";

    static {
        loadFromCSV(CSV_PATH);
    }

    /**
     * Adds a restaurant and appends it to CSV.
     *
     * @param r restaurant record
     */
    public static synchronized void aggiungiRistorante(Ristorante r) {
        RISTORANTI.add(r);
        appendToCsv(r);
    }

    /**
     * Returns restaurants owned by the given user.
     *
     * @param username owner username
     * @return list of restaurants
     */
    public static synchronized List<Ristorante> getRistorantiProprietario(String username) {
        List<Ristorante> miei = new ArrayList<>();
        for (Ristorante r : RISTORANTI) {
            if (r.getProprietario().equalsIgnoreCase(username)) miei.add(r);
        }
        return miei;
    }

    /**
     * Returns CSV row ids for restaurants owned by the user.
     *
     * @param username owner username
     * @return list of ids
     */
    public static synchronized List<Integer> getRistorantiIdsProprietario(String username) {
        List<Integer> ids = new ArrayList<>();
        int row = trovaRigaUtente(username);
        if (row <= 0) return ids;

        File file = new File(CSV_PATH);
        if (!file.exists()) return ids;

        int index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    if (line.toLowerCase().startsWith("name,")) continue;
                }
                index++;
                String[] c = splitCsvLine(line);
                if (c.length >= 15) {
                    int ownerRow = parseIntSafe(cleanCsvField(c[14]), 0);
                    if (ownerRow == row) {
                        ids.add(index);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ids;
    }

    /**
     * Loads restaurant data from a CSV file.
     *
     * @param path CSV path
     */
    public static synchronized void loadFromCSV(String path) {
        RISTORANTI.clear();
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    if (line.toLowerCase().startsWith("name,")) continue;
                }

                String[] c = splitCsvLine(line);
                if (c.length < 14) continue;

                String nome = cleanCsvField(c[0]);
                String address = cleanCsvField(c[1]);
                String location = cleanCsvField(c[2]);
                String price = cleanCsvField(c[3]);
                String cuisine = cleanCsvField(c[4]);

                boolean delivery = false;
                boolean prenotazione = false;
                if (c.length > 12) {
                    String facilities = cleanCsvField(c[12]).toLowerCase();
                    delivery = facilities.contains("delivery") || facilities.contains("takeaway") || facilities.contains("take away");
                    prenotazione = facilities.contains("reservation") || facilities.contains("book") || facilities.contains("prenot");
                }

                String proprietario = "";
                if (c.length >= 15) {
                    int row = parseIntSafe(cleanCsvField(c[14]), 0);
                    proprietario = getUsernameByRow(row);
                }

                RISTORANTI.add(new Ristorante(
                        nome, location, address, cuisine, price, delivery, prenotazione, proprietario
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Placeholder for full CSV persistence.
     *
     * @param path CSV path
     */
    public static synchronized void saveToCSV(String path) {
    }

    /**
     * Appends a restaurant to the CSV file.
     *
     * @param r restaurant record
     */
    private static void appendToCsv(Ristorante r) {
        int rigaRistoratore = trovaRigaUtente(r.getProprietario());
        File file = new File(CSV_PATH);
        boolean fileVuoto = !file.exists() || file.length() == 0;
        boolean serveNewline = file.exists() && file.length() > 0 && !fileEndsWithNewline(file);

        try (FileWriter fw = new FileWriter(file, true)) {
            if (fileVuoto) {
                fw.write("Name,Address,Location,Price,Cuisine,Longitude,Latitude,PhoneNumber,Url,WebsiteUrl,Award,GreenStar,FacilitiesAndServices,Description,RistoratoreRow\n");
            } else if (serveNewline) {
                fw.write("\n");
            }

            String facilities = buildFacilities(r.isDelivery(), r.isPrenotazione());

            StringBuilder line = new StringBuilder();
            line.append(escapeCsv(r.getNome())).append(",");
            line.append(escapeCsv(r.getIndirizzo())).append(",");
            line.append(escapeCsv(r.getCitta())).append(",");
            line.append(escapeCsv(r.getFasciaPrezzo())).append(",");
            line.append(escapeCsv(r.getTipoCucina())).append(",");
            line.append("0").append(",");
            line.append("0").append(",");
            line.append(escapeCsv("")).append(",");
            line.append(escapeCsv("")).append(",");
            line.append(escapeCsv("")).append(",");
            line.append(escapeCsv("")).append(",");
            line.append("0").append(",");
            line.append(escapeCsv(facilities)).append(",");
            line.append(escapeCsv("")).append(",");
            line.append(rigaRistoratore);

            fw.write(line.toString());
            fw.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds the CSV row index for a username.
     *
     * @param username username to search
     * @return row index or 0 if not found
     */
    private static int trovaRigaUtente(String username) {
        int row = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(UTENTI_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("nome,")) continue;
                String[] campi = splitCsvLine(line);
                if (campi.length >= 3) {
                    row++;
                    if (campi[2].trim().equalsIgnoreCase(username)) {
                        return row;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Builds the facilities string for CSV output.
     *
     * @param delivery delivery flag
     * @param prenotazione reservation flag
     * @return facilities string
     */
    private static String buildFacilities(boolean delivery, boolean prenotazione) {
        StringBuilder sb = new StringBuilder();
        if (delivery) sb.append("Delivery");
        if (prenotazione) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("Reservation");
        }
        return sb.toString();
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

    /**
     * Resolves a username by its CSV row index.
     *
     * @param row row index
     * @return username or empty string
     */
    private static String getUsernameByRow(int row) {
        if (row <= 0) return "";
        int current = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(UTENTI_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("nome,")) continue;
                current++;
                if (current == row) {
                    String[] campi = splitCsvLine(line);
                    if (campi.length >= 3) {
                        return cleanCsvField(campi[2]);
                    }
                    return "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
     * Checks whether a file ends with a newline.
     *
     * @param file file to inspect
     * @return true when newline is present
     */
    private static boolean fileEndsWithNewline(File file) {
        if (!file.exists() || file.length() == 0) return true;
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(file.length() - 1);
            int last = raf.read();
            return last == '\n' || last == '\r';
        } catch (Exception e) {
            return true;
        }
    }
}
