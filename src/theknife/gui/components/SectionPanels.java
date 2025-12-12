package theknife.gui.components;

import java.util.function.Consumer;
import javax.swing.JPanel;

import theknife.gui.components.home.HomePanelFactory;
import theknife.gui.components.search.SearchPanelFactory;
import theknife.gui.components.favorites.FavoritesPanelFactory;
import theknife.gui.components.reviews.ReviewsPanelFactory;
import theknife.gui.components.restaurants.MyRestaurantsPanelFactory;
import theknife.model.User;

/**
 * Facade centrale per la creazione dei pannelli dell'applicazione.
 * Gestisce lo stato di sessione e delega la costruzione della UI
 * a factory specializzate.
 */
public class SectionPanels {

    /* =========================
       Stato di sessione globale
       ========================= */

    public static String CURRENT_ROLE = "ospite";
    public static String CURRENT_USERNAME = "Ospite";
    public static User CURRENT_USER = null;
    public static Consumer<JPanel> NAVIGATE;

    /**
     * Imposta la sessione corrente dell'utente.
     */
    public static void setSession(User user, Consumer<JPanel> navigate) {
        CURRENT_USER = user;
        CURRENT_ROLE = user.getRuolo();
        CURRENT_USERNAME = user.getUsername();
        NAVIGATE = navigate;
    }

    /* =========================
       Deleghe ai pannelli
       ========================= */

    public static JPanel creaHomeBenvenuto() {
        return HomePanelFactory.creaHomeBenvenuto();
    }

    public static JPanel creaCercaRistoranti() {
        return SearchPanelFactory.creaCercaRistoranti();
    }

    public static JPanel creaMieiPreferiti() {
        return FavoritesPanelFactory.creaMieiPreferiti();
    }

    public static JPanel creaMieiRistoranti() {
        return MyRestaurantsPanelFactory.creaMieiRistoranti();
    }

    public static JPanel creaRecensioniCliente() {
        return ReviewsPanelFactory.creaRecensioniCliente();
    }

    public static JPanel creaRecensioniRistoratore() {
        return ReviewsPanelFactory.creaRecensioniRistoratore();
    }
}
