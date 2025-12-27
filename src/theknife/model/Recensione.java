package theknife.model;

public class Recensione {
    private final int ristoranteId;
    private final String ristoranteNome;
    private final String ristoranteCitta;
    private final String autoreUsername;
    private final int stelle; // 1..5
    private final String testo;
    private String rispostaRistoratore; // opzionale, una sola risposta

    public Recensione(int ristoranteId, String ristoranteNome, String ristoranteCitta,
                      String autoreUsername, int stelle, String testo) {
        this.ristoranteId = ristoranteId;
        this.ristoranteNome = ristoranteNome;
        this.ristoranteCitta = ristoranteCitta;
        this.autoreUsername = autoreUsername;
        this.stelle = stelle;
        this.testo = testo;
        this.rispostaRistoratore = null;
    }

    public int getRistoranteId() {
        return ristoranteId;
    }

    public String getRistoranteNome() {
        return ristoranteNome;
    }

    public String getRistoranteCitta() {
        return ristoranteCitta;
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
