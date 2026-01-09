package theknife.gui.components.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import theknife.model.Ristorante;

/**
 * Loads restaurant data from CSV for search panels.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class RistoranteRepository {

    /**
     * Creates a new RistoranteRepository instance.
     */
    public RistoranteRepository() {
    }

    /**
     * Loads all restaurants from CSV.
     *
     * @return list of restaurants
     */
    public static List<Ristorante> getRistoranti() {

        List<Ristorante> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data/ristoranti.csv"))) {
            String line;
            br.readLine(); // salta header

            while ((line = br.readLine()) != null) {
                String[] c = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (c.length < 14) continue;

                String nome        = c[0].replace("\"", "").trim();
                String address     = c[1].replace("\"", "").trim();
                String location    = c[2].replace("\"", "").trim();
                String price       = c[3].replace("\"", "").trim();
                String cuisine     = c[4].replace("\"", "").trim();
                double longitude   = Double.parseDouble(c[5]);
                double latitude    = Double.parseDouble(c[6]);
                String phone       = c[7].replace("\"", "").trim();
                String url         = c[8].replace("\"", "").trim();
                String website     = c[9].replace("\"", "").trim();
                String award       = c[10].replace("\"", "").trim();
                String greenStar   = c[11].replace("\"", "").trim();
                String facilities  = c[12].replace("\"", "").trim();
                String description = c[13].replace("\"", "").trim();

                lista.add(new Ristorante(
                        nome, address, location, price, cuisine,
                        longitude, latitude, phone, url, website,
                        award, greenStar, facilities, description
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Loads restaurants filtered by city with a max limit.
     *
     * @param residenza city name
     * @param limit maximum results
     * @return list of restaurants
     */
    public static List<Ristorante> getRistorantiByCityLimit(String residenza, int limit) {
        List<Ristorante> risultati = new ArrayList<>();
        if (residenza == null || residenza.trim().isEmpty() || limit <= 0) return risultati;

        String city = normalizeCity(residenza);
        String cityPrefix = city.length() >= 4 ? city.substring(0, 4) : city;

        try (BufferedReader br = new BufferedReader(new FileReader("data/ristoranti.csv"))) {
            String line = br.readLine(); // salta header
            while ((line = br.readLine()) != null) {
                String[] c = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (c.length < 14) continue;

                String location = c[2].replace("\"", "").trim();
                String rCity = normalizeCity(location);
                if (!rCity.isEmpty() && (rCity.contains(city) || rCity.contains(cityPrefix))) {
                    String nome        = c[0].replace("\"", "").trim();
                    String address     = c[1].replace("\"", "").trim();
                    String price       = c[3].replace("\"", "").trim();
                    String cuisine     = c[4].replace("\"", "").trim();
                    double longitude   = Double.parseDouble(c[5]);
                    double latitude    = Double.parseDouble(c[6]);
                    String phone       = c[7].replace("\"", "").trim();
                    String url         = c[8].replace("\"", "").trim();
                    String website     = c[9].replace("\"", "").trim();
                    String award       = c[10].replace("\"", "").trim();
                    String greenStar   = c[11].replace("\"", "").trim();
                    String facilities  = c[12].replace("\"", "").trim();
                    String description = c[13].replace("\"", "").trim();

                    risultati.add(new Ristorante(
                            nome, address, location, price, cuisine,
                            longitude, latitude, phone, url, website,
                            award, greenStar, facilities, description
                    ));

                    if (risultati.size() >= limit) break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return risultati;
    }

    /**
     * Resolves the CSV row id for a restaurant.
     *
     * @param target restaurant model
     * @return row id or -1
     */
    public static int getIdByRistorante(Ristorante target) {
        List<Ristorante> lista = getRistoranti();

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getNome().equals(target.getNome())
                    && lista.get(i).getCitta().equals(target.getCitta())) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Normalizes a city string for matching.
     *
     * @param text raw city text
     * @return normalized city
     */
    private static String normalizeCity(String text) {
        if (text == null) return "";
        String lower = text.trim().toLowerCase();
        return lower.replaceAll("[^a-z]", " ").replaceAll("\\s+", " ").trim();
    }
}
