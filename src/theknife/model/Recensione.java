package theknife.model;

public class Recensione {
    private final String ristoranteNome;
    private final String autoreUsername;
    private final int stelle; // 1..5
    private final String testo;
    private String rispostaRistoratore; // opzionale, una sola risposta

    public Recensione(String ristoranteNome, String autoreUsername, int stelle, String testo) {
        this.ristoranteNome = ristoranteNome;
        this.autoreUsername = autoreUsername;
        this.stelle = stelle;
        this.testo = testo;
        this.rispostaRistoratore = null;
    }

    public String getRistoranteNome() {
        return ristoranteNome;
    }

    public String getAutoreUsername() {
        return autoreUsername;
    }

    public int getStelle() {
        return stelle;
    }

    public String getTesto() {
        return testo;
    }

    public String getRispostaRistoratore() {
        return rispostaRistoratore;
    }

    public void setRispostaRistoratore(String rispostaRistoratore) {
        this.rispostaRistoratore = rispostaRistoratore;
    }
}
