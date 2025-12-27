package theknife.gui.components.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import theknife.model.Ristorante;

public class RistoranteRepository {

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
}
