package theknife;

import javax.swing.SwingUtilities;
import theknife.gui.HomeFrame;

public class TheKnife {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomeFrame("ospite", "Ospite"));
    }
}
