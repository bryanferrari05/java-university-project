package theknife.model;

/**
 * Review left by a user for a restaurant.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class Recensione {
    /** Restaurant id (row index). */
    private final int ristoranteId;
    /** Restaurant name. */
    private final String ristoranteNome;
    /** Restaurant city. */
    private final String ristoranteCitta;
    /** Author username. */
    private final String autoreUsername;
    /** Star rating (1..5). */
    private int stelle; // 1..5
    /** Review text. */
    private String testo;
    /** Optional owner reply. */
    private String rispostaRistoratore; // opzionale, una sola risposta

    /**
     * Creates a review instance.
     *
     * @param ristoranteId restaurant id
     * @param ristoranteNome restaurant name
     * @param ristoranteCitta restaurant city
     * @param autoreUsername author username
     * @param stelle star rating
     * @param testo review text
     */
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

    /**
     * Returns restaurant id.
     *
     * @return restaurant id
     */
    public int getRistoranteId() {
        return ristoranteId;
    }

    /**
     * Returns restaurant name.
     *
     * @return restaurant name
     */
    public String getRistoranteNome() {
        return ristoranteNome;
    }

    /**
     * Returns restaurant city.
     *
     * @return restaurant city
     */
    public String getRistoranteCitta() {
        return ristoranteCitta;
    }

    /**
     * Returns author username.
     *
     * @return author username
     */
    public String getAutoreUsername() {
        return autoreUsername;
    }

    /**
     * Returns star rating.
     *
     * @return star rating
     */
    public int getStelle() {
        return stelle;
    }

    /**
     * Returns review text.
     *
     * @return review text
     */
    public String getTesto() {
        return testo;
    }

    /**
     * Updates the star rating.
     *
     * @param stelle new rating (1..5)
     */
    public void setStelle(int stelle) {
        this.stelle = stelle;
    }

    /**
     * Updates the review text.
     *
     * @param testo new text
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }

    /**
     * Returns owner reply or null.
     *
     * @return owner reply or null
     */
    public String getRispostaRistoratore() {
        return rispostaRistoratore;
    }

    /**
     * Sets the owner reply.
     *
     * @param rispostaRistoratore reply text
     */
    public void setRispostaRistoratore(String rispostaRistoratore) {
        this.rispostaRistoratore = rispostaRistoratore;
    }
}
