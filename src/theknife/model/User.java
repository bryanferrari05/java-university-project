package theknife.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String nome;
    private String cognome;
    private String ruolo;

    // ⭐ NUOVO: lista dei ristoranti preferiti
    private List<String> preferiti;

    public User(String username, String password, String nome, String cognome, String ruolo) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;

        this.preferiti = new ArrayList<>(); // inizializza la lista vuota
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getRuolo() { return ruolo; }

    // ⭐ NUOVO: metodi per i preferiti
    public List<String> getPreferiti() {
        return preferiti;
    }

    public void aggiungiPreferito(String ristoranteNome) {
        if (!preferiti.contains(ristoranteNome)) {
            preferiti.add(ristoranteNome);
        }
    }

    public void rimuoviPreferito(String ristoranteNome) {
        preferiti.remove(ristoranteNome);
    }

    public boolean isPreferito(String ristoranteNome) {
        return preferiti.contains(ristoranteNome);
    }
}
