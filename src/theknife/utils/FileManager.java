package theknife.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import theknife.model.User;

public class FileManager {

    private static final String USERS_FILE = "data/utenti.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Legge gli utenti dal file JSON
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
