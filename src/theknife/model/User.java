package theknife.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String nome;
    private String cognome;
    private String ruolo;

    // ⭐ NUOVO: lista dei ristoranti preferiti (ID = numero riga)
    private List<Integer> preferiti;

    public User(String username, String password, String nome, String cognome, String ruolo) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;

        this.preferiti = new ArrayList<>(); // inizializza la lista dei preferiti
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getRuolo() { return ruolo; }

    // ⭐ Metodi per i preferiti (basati su ID del ristorante)
    public List<Integer> getPreferiti() {
        return preferiti;
    }

    public void aggiungiPreferito(int idRistorante) {
        if (!preferiti.contains(idRistorante)) {
            preferiti.add(idRistorante);
        }
    }

    public void rimuoviPreferito(int idRistorante) {
        preferiti.remove(Integer.valueOf(idRistorante));
    }

    public boolean isPreferito(int idRistorante) {
        return preferiti.contains(idRistorante);
    }
}
