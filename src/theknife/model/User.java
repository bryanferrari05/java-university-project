package theknife.model;

import java.util.ArrayList;
import java.util.List;

/**
 * User profile with credentials, role, and favorites.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class User {
    /** Username used for login and display. */
    private String username;
    /** Password hash or plain password depending on data source. */
    private String password;
    /** Given name. */
    private String nome;
    /** Family name. */
    private String cognome;
    /** User role (cliente, ristoratore, ospite). */
    private String ruolo;
    /** City of residence. */
    private String residenza;

    // ⭐ NUOVO: lista dei ristoranti preferiti (ID = numero riga)
    /** Favorite restaurant ids (CSV row index). */
    private List<Integer> preferiti;

    /**
     * Creates a user profile instance.
     *
     * @param username username
     * @param password password or hash
     * @param nome given name
     * @param cognome family name
     * @param ruolo user role
     * @param residenza residence city
     */
    public User(String username, String password, String nome, String cognome, String ruolo, String residenza) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;
        this.residenza = residenza;

        this.preferiti = new ArrayList<>(); // inizializza la lista dei preferiti
    }

    // Getters
    /**
     * Returns username.
     *
     * @return username
     */
    public String getUsername() { return username; }
    /**
     * Returns password or hash.
     *
     * @return password or hash
     */
    public String getPassword() { return password; }
    /**
     * Returns given name.
     *
     * @return given name
     */
    public String getNome() { return nome; }
    /**
     * Returns family name.
     *
     * @return family name
     */
    public String getCognome() { return cognome; }
    /**
     * Returns user role.
     *
     * @return user role
     */
    public String getRuolo() { return ruolo; }
    /**
     * Returns residence city.
     *
     * @return residence city
     */
    public String getResidenza() { return residenza; }

    // ⭐ Metodi per i preferiti (basati su ID del ristorante)
    /**
     * Returns the list of favorite restaurant ids.
     *
     * @return list of ids
     */
    public List<Integer> getPreferiti() {
        return preferiti;
    }

    /**
     * Adds a restaurant id to favorites if missing.
     *
     * @param idRistorante restaurant id
     */
    public void aggiungiPreferito(int idRistorante) {
        if (!preferiti.contains(idRistorante)) {
            preferiti.add(idRistorante);
        }
    }

    /**
     * Removes a restaurant id from favorites.
     *
     * @param idRistorante restaurant id
     */
    public void rimuoviPreferito(int idRistorante) {
        preferiti.remove(Integer.valueOf(idRistorante));
    }

    /**
     * Checks if a restaurant id is in favorites.
     *
     * @param idRistorante restaurant id
     * @return true when present
     */
    public boolean isPreferito(int idRistorante) {
        return preferiti.contains(idRistorante);
    }
}
