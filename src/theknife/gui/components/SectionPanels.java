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
 * Facade for creating main panels and tracking session state.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class SectionPanels {

    /**
     * Creates a new SectionPanels instance.
     */
    public SectionPanels() {
    }

    /* =========================
       Stato di sessione globale
       ========================= */

    /** Current role for the session. */
    public static String CURRENT_ROLE = "ospite";
    /** Current username for the session. */
    public static String CURRENT_USERNAME = "Ospite";
    /** Current user instance for the session. */
    public static User CURRENT_USER = null;
    /** Navigation callback used by panels. */
    public static Consumer<JPanel> NAVIGATE;

    /**
     * Sets the active user session and navigation callback.
     *
     * @param user logged-in user
     * @param navigate callback to switch panels
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

    /**
     * Builds the welcome home panel.
     *
     * @return panel instance
     */
    public static JPanel creaHomeBenvenuto() {
        return HomePanelFactory.creaHomeBenvenuto();
    }

    /**
     * Builds the search panel.
     *
     * @return panel instance
     */
    public static JPanel creaCercaRistoranti() {
        return SearchPanelFactory.creaCercaRistoranti();
    }

    /**
     * Builds the favorites panel.
     *
     * @return panel instance
     */
    public static JPanel creaMieiPreferiti() {
        return FavoritesPanelFactory.creaMieiPreferiti();
    }

    /**
     * Builds the restaurant owner panel.
     *
     * @return panel instance
     */
    public static JPanel creaMieiRistoranti() {
        return MyRestaurantsPanelFactory.creaMieiRistoranti();
    }

    /**
     * Builds the customer reviews panel.
     *
     * @return panel instance
     */
    public static JPanel creaRecensioniCliente() {
        return ReviewsPanelFactory.creaRecensioniCliente();
    }

    /**
     * Builds the owner reviews panel.
     *
     * @return panel instance
     */
    public static JPanel creaRecensioniRistoratore() {
        return ReviewsPanelFactory.creaRecensioniRistoratore();
    }
}
