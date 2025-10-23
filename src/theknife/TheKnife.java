package theknife;

import javax.swing.SwingUtilities;
import theknife.gui.StartFrame;

public class TheKnife {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartFrame::new);
    }
}