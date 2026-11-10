
package theknife.controller;

import java.io.*;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

import theknife.model.User;

/**
 * Gestisce le operazioni relative agli utenti.
 * Attualmente utilizza il CSV; nel Lab B userà il database.
 */
public class GestioneUtenti {

    private static final String PATH_UTENTI = "data/utenti.csv";

    /**
     * Verifica le credenziali e restituisce l'utente autenticato.
     *
     * @param username nome utente
     * @param password password inserita
     * @return utente autenticato oppure null
     * @throws IOException se il CSV non può essere letto
     */
    public static User verificaCredenziali(
            String username, String password) throws IOException {

        String hash = hashPassword(password);

        try (BufferedReader br =
                new BufferedReader(new FileReader(PATH_UTENTI))) {

            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("nome,")) continue;

                String[] campi = line.split(
                    ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1
                );

                if (campi.length < 5) continue;

                String csvNome = campi[0].trim();
                String csvCognome = campi[1].trim();
                String csvUsername = campi[2].trim();
                String csvHash = campi[3].trim();
                String csvRuolo = campi[4].trim();
                String csvResidenza =
                    campi.length >= 6 ? campi[5].trim() : "";
                String preferitiRaw =
                    campi.length >= 7 ? campi[6].trim() : "";

                if (csvUsername.equals(username)
                        && csvHash.equals(hash)) {

                    User u = new User(
                        csvUsername, csvHash, csvNome,
                        csvCognome, csvRuolo, csvResidenza
                    );

                    if (!preferitiRaw.isEmpty()) {
                        for (String p : preferitiRaw.split("-")) {
                            try {
                                u.aggiungiPreferito(
                                    Integer.parseInt(p)
                                );
                            } catch (NumberFormatException ignored) {
                                // Ignora gli ID non validi
                            }
                        }
                    }

                    return u;
                }
            }
        }

        return null;
    }

    /**
     * Calcola l'hash SHA-256 per compatibilità
     * con le password già presenti nel CSV.
     */
    private static String hashPassword(String password) {
        try {
            MessageDigest md =
                MessageDigest.getInstance("SHA-256");

            byte[] hash = md.digest(
                password.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e) {
            throw new IllegalStateException(
                "Impossibile calcolare l'hash", e
            );
        }
    }

/**
 * Verifica se uno username è già presente nel CSV.
 *
 * @param username username da controllare
 * @return true se esiste, false altrimenti
 * @throws IOException in caso di errore nella lettura
 */
    public static boolean usernameEsistente(String username)
            throws IOException {

        try (BufferedReader br =
                new BufferedReader(new FileReader(PATH_UTENTI))) {

            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("nome,")) continue;

                String[] campi = line.split(",");

                if (campi.length >= 3 &&
                        campi[2].trim().equalsIgnoreCase(username)) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Salva un nuovo utente nel file CSV.
     *
     * @param nome nome dell'utente
     * @param cognome cognome dell'utente
     * @param residenza città di residenza
     * @param username nome utente
     * @param password password originale
     * @param ruolo cliente o ristoratore
     * @return true se il salvataggio riesce, false altrimenti
     */
    public static boolean salvaUtente(String nome, String cognome, String residenza, String username, String password, String ruolo) 
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_UTENTI, true))) 
        {

            String hashedPassword = hashPassword(password);

            bw.write(String.format("%s,%s,%s,%s,%s,%s,%s%n", nome, cognome, username, hashedPassword, ruolo, residenza, ""));
            return true;

        } catch (IOException e) 
        {
            System.err.println("Errore nella scrittura del file utenti.csv: " + e.getMessage());
            return false;
        }
    }


}
