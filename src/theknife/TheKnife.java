package theknife;

import theknife.gui.LoginFrame;
import javax.swing.SwingUtilities;  /*questo codice fa partire la finestra LoginFrame, 
                                    invece di println*/
public class TheKnife {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}