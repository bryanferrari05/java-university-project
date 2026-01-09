package theknife.model;

/**
 * Restaurant data parsed from CSV and derived flags.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class Ristorante {

    // ---- CAMPi ORIGINALI DAL CSV ----
    /** Restaurant name. */
    private String nome;
    /** Street address. */
    private String address;
    /** City/location field from CSV. */
    private String location;      // Nel vecchio codice = città
    /** Price range or symbols. */
    private String price;         // Nel vecchio codice = fasciaPrezzo
    /** Cuisine type. */
    private String cuisine;       // Nel vecchio codice = tipo cucina
    /** Longitude coordinate. */
    private double longitude;
    /** Latitude coordinate. */
    private double latitude;
    /** Contact phone number. */
    private String phoneNumber;
    /** CSV url field. */
    private String url;
    /** Website url. */
    private String websiteUrl;
    /** Award text field. */
    private String award;
    /** Green star information. */
    private String greenStar;
    /** Facilities/services text. */
    private String facilitiesAndServices;
    /** Description text. */
    private String description;

    // ---- DERIVATI AUTOMATICAMENTE ----
    /** True when delivery or takeaway is detected. */
    private boolean delivery;
    /** True when reservation is detected. */
    private boolean prenotazione;

    // ---- COSTRUTTORE COMPLETO ----
    /**
     * Builds a restaurant record from CSV fields.
     *
     * @param nome name
     * @param address address
     * @param location city/location
     * @param price price range
     * @param cuisine cuisine type
     * @param longitude longitude
     * @param latitude latitude
     * @param phoneNumber phone number
     * @param url raw url
     * @param websiteUrl website url
     * @param award award info
     * @param greenStar green star info
     * @param facilitiesAndServices facilities/services text
     * @param description description
     */
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

    /**
     * Returns restaurant name.
     *
     * @return restaurant name
     */
    public String getNome() { return nome; }
    /**
     * Returns street address.
     *
     * @return street address
     */
    public String getAddress() { return address; }
    /**
     * Returns city/location.
     *
     * @return city/location
     */
    public String getLocation() { return location; }
    /**
     * Returns price range.
     *
     * @return price range
     */
    public String getPrice() { return price; }
    /**
     * Returns cuisine type.
     *
     * @return cuisine type
     */
    public String getCuisine() { return cuisine; }
    /**
     * Returns longitude.
     *
     * @return longitude
     */
    public double getLongitude() { return longitude; }
    /**
     * Returns latitude.
     *
     * @return latitude
     */
    public double getLatitude() { return latitude; }
    /**
     * Returns phone number.
     *
     * @return phone number
     */
    public String getPhoneNumber() { return phoneNumber; }
    /**
     * Returns raw url.
     *
     * @return raw url
     */
    public String getUrl() { return url; }
    /**
     * Returns website url.
     *
     * @return website url
     */
    public String getWebsiteUrl() { return websiteUrl; }
    /**
     * Returns award info.
     *
     * @return award info
     */
    public String getAward() { return award; }
    /**
     * Returns green star info.
     *
     * @return green star info
     */
    public String getGreenStar() { return greenStar; }
    /**
     * Returns facilities and services.
     *
     * @return facilities and services
     */
    public String getFacilitiesAndServices() { return facilitiesAndServices; }
    /**
     * Returns description.
     *
     * @return description
     */
    public String getDescription() { return description; }

    /**
     * Returns true if delivery or takeaway is available.
     *
     * @return true if delivery or takeaway is available
     */
    public boolean isDelivery() { return delivery; }
    /**
     * Returns true if reservation is available.
     *
     * @return true if reservation is available
     */
    public boolean isPrenotazione() { return prenotazione; }

    /**
     * Extracts the star count from the award text.
     *
     * @return number of stars, 0 if missing
     */
    public int getStarCount() {
        if (award == null) return 0;
        String lower = award.toLowerCase();
        if (!lower.contains("star")) return 0;
        for (int i = 0; i < lower.length(); i++) {
            char ch = lower.charAt(i);
            if (ch >= '1' && ch <= '5') return ch - '0';
        }
        return 0;
    }

    // ---- GETTER DI COMPATIBILITÀ (vecchio codice SectionPanels) ----

    // Il vecchio codice usava getCitta() → ora ritorna "location"
    /**
     * Returns location field for legacy code.
     *
     * @return location field for legacy code
     */
    public String getCitta() {
        return location;
    }

    // Il vecchio codice usava getTipoCucina() → ora ritorna "cuisine"
    /**
     * Returns cuisine field for legacy code.
     *
     * @return cuisine field for legacy code
     */
    public String getTipoCucina() {
        return cuisine;
    }

    // Il vecchio codice usava getFasciaPrezzo() → ora ritorna "price"
    /**
     * Returns price field for legacy code.
     *
     * @return price field for legacy code
     */
    public String getFasciaPrezzo() {
        return price;
    }
}
