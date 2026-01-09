package theknife.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import theknife.model.User;

/**
 * JSON persistence helpers for users.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class FileManager {

    /**
     * Creates a new FileManager instance.
     */
    public FileManager() {
    }

    /** JSON path for users. */
    private static final String USERS_FILE = "data/utenti.json";
    /** Gson instance with pretty printing. */
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Legge gli utenti dal file JSON
    /**
     * Loads users from JSON storage.
     *
     * @return list of users
     */
    public static List<User> leggiUtenti() {
        try (Reader reader = new FileReader(USERS_FILE)) {
            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> utenti = gson.fromJson(reader, listType);
            return utenti != null ? utenti : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file utenti: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Verifica login
    /**
     * Validates login credentials against stored users.
     *
     * @param username username
     * @param password password
     * @return true if a match is found
     */
    public static boolean verificaCredenziali(String username, String password) {
        List<User> utenti = leggiUtenti();
        for (User u : utenti) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Aggiunge un nuovo utente
    /**
     * Persists a new user in JSON storage.
     *
     * @param nuovoUtente user to add
     */
    public static void aggiungiUtente(User nuovoUtente) {
        List<User> utenti = leggiUtenti();
        utenti.add(nuovoUtente);

        try (Writer writer = new FileWriter(USERS_FILE)) {
            gson.toJson(utenti, writer);
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio dell'utente: " + e.getMessage());
        }
    }
}
