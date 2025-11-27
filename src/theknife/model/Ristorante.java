package theknife.model;

public class Ristorante {
    private String nome;
    private String citta;
    private String tipoCucina;
    private String fasciaPrezzo;
    private boolean delivery;
    private boolean prenotazione;

    public Ristorante(String nome, String citta, String tipoCucina, String fasciaPrezzo,
                      boolean delivery, boolean prenotazione) {
        this.nome = nome;
        this.citta = citta;
        this.tipoCucina = tipoCucina;
        this.fasciaPrezzo = fasciaPrezzo;
        this.delivery = delivery;
        this.prenotazione = prenotazione;
    }

    public String getNome() { return nome; }
    public String getCitta() { return citta; }
    public String getTipoCucina() { return tipoCucina; }
    public String getFasciaPrezzo() { return fasciaPrezzo; }
    public boolean isDelivery() { return delivery; }
    public boolean isPrenotazione() { return prenotazione; }
}
