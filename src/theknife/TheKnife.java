package theknife;

import theknife.gui.LoginFrame;
import javax.swing.SwingUtilities;

public class TheKnife {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
