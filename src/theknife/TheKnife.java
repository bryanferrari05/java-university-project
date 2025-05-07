package theknife;

import javax.swing.*;

public class TheKnife {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("TheKnife - Progetto Swing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.add(new JLabel("Benvenuto su TheKnife!", SwingConstants.CENTER));
            frame.setVisible(true);
        });
    }
}
