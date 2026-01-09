package theknife;

import javax.swing.SwingUtilities;
import theknife.gui.StartFrame;

/**
 * Application entry point that launches the UI.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class TheKnife {

    /**
     * Creates a new TheKnife instance.
     */
    public TheKnife() {
    }
    /**
     * Starts the Swing UI on the EDT.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartFrame::new);
    }
}
