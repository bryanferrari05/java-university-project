package theknife.model;

public class Ristorante {

    // ---- CAMPi ORIGINALI DAL CSV ----
    private String nome;
    private String address;
    private String location;      // Nel vecchio codice = città
    private String price;         // Nel vecchio codice = fasciaPrezzo
    private String cuisine;       // Nel vecchio codice = tipo cucina
    private double longitude;
    private double latitude;
    private String phoneNumber;
    private String url;
    private String websiteUrl;
    private String award;
    private String greenStar;
    private String facilitiesAndServices;
    private String description;

    // ---- DERIVATI AUTOMATICAMENTE ----
    private boolean delivery;
    private boolean prenotazione;

    // ---- COSTRUTTORE COMPLETO ----
    public Ristorante(String nome, String address, String location, String price, String cuisine,
                      double longitude, double latitude, String phoneNumber, String url,
                      String websiteUrl, String award, String greenStar,
                      String facilitiesAndServices, String description) {

        this.nome = nome;
        this.address = address;
        this.location = location;
        this.price = price;
        this.cuisine = cuisine;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNumber = phoneNumber;
        this.url = url;
        this.websiteUrl = websiteUrl;
        this.award = award;
        this.greenStar = greenStar;
        this.facilitiesAndServices = facilitiesAndServices;
        this.description = description;

        // Analisi automatica dei servizi
        String text = (facilitiesAndServices + " " + description).toLowerCase();

        this.delivery =
                text.contains("delivery")
                || text.contains("takeaway")
                || text.contains("take away");

        this.prenotazione =
                text.contains("reservation")
                || text.contains("book")
                || text.contains("prenot");
    }

    // ---- GETTER NUOVI (per dettagli) ----

    public String getNome() { return nome; }
    public String getAddress() { return address; }
    public String getLocation() { return location; }
    public String getPrice() { return price; }
    public String getCuisine() { return cuisine; }
    public double getLongitude() { return longitude; }
    public double getLatitude() { return latitude; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getUrl() { return url; }
    public String getWebsiteUrl() { return websiteUrl; }
    public String getAward() { return award; }
    public String getGreenStar() { return greenStar; }
    public String getFacilitiesAndServices() { return facilitiesAndServices; }
    public String getDescription() { return description; }

    public boolean isDelivery() { return delivery; }
    public boolean isPrenotazione() { return prenotazione; }

    // ---- GETTER DI COMPATIBILITÀ (vecchio codice SectionPanels) ----

    // Il vecchio codice usava getCitta() → ora ritorna "location"
    public String getCitta() {
        return location;
    }

    // Il vecchio codice usava getTipoCucina() → ora ritorna "cuisine"
    public String getTipoCucina() {
        return cuisine;
    }

    // Il vecchio codice usava getFasciaPrezzo() → ora ritorna "price"
    public String getFasciaPrezzo() {
        return price;
    }
}
