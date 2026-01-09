package theknife.gui.components.favorites;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

import theknife.gui.components.RestaurantDetailsFrame;
import theknife.gui.components.SectionPanels;
import theknife.gui.components.search.RistoranteRepository;
import theknife.model.Ristorante;
import theknife.model.User;

/**
 * Factory for the favorites panel.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class FavoritesPanelFactory {

    /**
     * Creates a new FavoritesPanelFactory instance.
     */
    public FavoritesPanelFactory() {
    }

    /**
     * Builds the favorites panel for the current user.
     *
     * @return panel instance
     */
    public static JPanel creaMieiPreferiti() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("I miei preferiti", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(230, 57, 70));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        User user = SectionPanels.CURRENT_USER;

        if (user == null) {
            JPanel ph = new JPanel();
            ph.setBackground(Color.WHITE);
            ph.add(new JLabel("Non sei loggato."));
            panel.add(ph, BorderLayout.CENTER);
            return panel;
        }
        if ("ospite".equalsIgnoreCase(user.getRuolo())) {
            JPanel ph = new JPanel();
            ph.setBackground(Color.WHITE);
            ph.add(new JLabel("Accesso richiesto per visualizzare i preferiti."));
            panel.add(ph, BorderLayout.CENTER);
            return panel;
        }

        List<Integer> preferiti = user.getPreferiti();

        if (preferiti.isEmpty()) {
            JPanel placeholder = new JPanel();
            placeholder.setBackground(Color.WHITE);
            placeholder.add(new JLabel("Nessun ristorante preferito al momento."));
            panel.add(placeholder, BorderLayout.CENTER);
            return panel;
        }

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        panel.add(content, BorderLayout.CENTER);

        JPanel loading = new JPanel();
        loading.setBackground(Color.WHITE);
        loading.setLayout(new BoxLayout(loading, BoxLayout.Y_AXIS));
        JLabel loadingLabel = new JLabel("Caricamento preferiti...", SwingConstants.CENTER);
        loadingLabel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        Spinner spinner = new Spinner(new Color(230, 57, 70), 20);
        spinner.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        loading.add(Box.createVerticalStrut(24));
        loading.add(spinner);
        loading.add(Box.createVerticalStrut(12));
        loading.add(loadingLabel);
        content.add(loading, BorderLayout.CENTER);

        SwingWorker<List<Ristorante>, Void> worker = new SwingWorker<List<Ristorante>, Void>() {
            private final List<Integer> idsValid = new ArrayList<>();

            @Override
            protected List<Ristorante> doInBackground() {
                List<Ristorante> tutti = RistoranteRepository.getRistoranti();
                List<Ristorante> risultati = new ArrayList<>();
                List<Integer> ordinati = new ArrayList<>(preferiti);
                insertionSort(ordinati);

                int maxId = ultimoIdValido(ordinati);
                if (maxId <= 0) return risultati;

                int limit = Math.min(maxId, tutti.size());
                int targetIndex = 0;
                for (int i = 1; i <= limit && targetIndex < ordinati.size(); i++) {
                    Integer currentId = ordinati.get(targetIndex);
                    if (currentId == null || currentId <= 0) {
                        targetIndex++;
                        i--;
                        continue;
                    }
                    if (currentId == i) {
                        idsValid.add(i);
                        risultati.add(tutti.get(i - 1));
                        targetIndex++;
                    }
                }
                return risultati;
            }

            @Override
            protected void done() {
                content.removeAll();
                try {
                    List<Ristorante> risultati = get();
                    if (risultati.isEmpty()) {
                        JPanel placeholder = new JPanel();
                        placeholder.setBackground(Color.WHITE);
                        placeholder.add(new JLabel("Nessun ristorante preferito al momento."));
                        content.add(placeholder, BorderLayout.CENTER);
                    } else {
                        JPanel list = new JPanel();
                        list.setBackground(Color.WHITE);
                        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

                        for (int i = 0; i < risultati.size(); i++) {
                            Ristorante r = risultati.get(i);
                            Integer id = idsValid.get(i);

                            JPanel card = new JPanel(new BorderLayout());
                            card.setBorder(BorderFactory.createCompoundBorder(
                                    BorderFactory.createLineBorder(new Color(230, 57, 70), 1),
                                    BorderFactory.createEmptyBorder(10, 14, 10, 14)
                            ));
                            card.setBackground(Color.WHITE);

                            JLabel nome = new JLabel(r.getNome() + " - " + r.getTipoCucina());
                            nome.setFont(new Font("Segoe UI", Font.BOLD, 15));

                            JLabel info = new JLabel(r.getCitta() + " - " + r.getFasciaPrezzo());
                            info.setForeground(new Color(90, 90, 90));

                            JPanel text = new JPanel();
                            text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
                            text.setBackground(Color.WHITE);
                            text.add(nome);
                            text.add(info);

                            card.add(text, BorderLayout.CENTER);

                            JButton dettagli = new JButton("Dettagli");
                            dettagli.addActionListener(e -> {
                                Window parentWindow = SwingUtilities.getWindowAncestor(content);
                                new RestaurantDetailsFrame(parentWindow, user, id, r);
                            });

                            card.add(dettagli, BorderLayout.EAST);

                            list.add(card);
                            list.add(Box.createVerticalStrut(10));
                        }

                        JScrollPane scroll = new JScrollPane(list);
                        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
                        scroll.getVerticalScrollBar().setUnitIncrement(16);
                        content.add(scroll, BorderLayout.CENTER);
                    }
                } catch (Exception ex) {
                    JPanel error = new JPanel();
                    error.setBackground(Color.WHITE);
                    error.add(new JLabel("Errore nel caricamento dei preferiti."));
                    content.add(error, BorderLayout.CENTER);
                }

                content.revalidate();
                content.repaint();
            }
        };
        worker.execute();

        return panel;
    }

    /**
     * Sorts a list of integers using insertion sort.
     *
     * @param list list to sort
     */
    private static void insertionSort(List<Integer> list) {
        for (int i = 1; i < list.size(); i++) {
            Integer key = list.get(i);
            int j = i - 1;
            while (j >= 0 && compareNullable(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    /**
     * Compares nullable integers treating null as greater.
     *
     * @param a first value
     * @param b second value
     * @return comparison result
     */
    private static int compareNullable(Integer a, Integer b) {
        if (a == null && b == null) return 0;
        if (a == null) return 1;
        if (b == null) return -1;
        return Integer.compare(a, b);
    }

    /**
     * Finds the last valid id in a list.
     *
     * @param list list of ids
     * @return last valid id or -1
     */
    private static int ultimoIdValido(List<Integer> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            Integer v = list.get(i);
            if (v != null && v > 0) return v;
        }
        return -1;
    }

    /**
     * Simple spinner component used for loading.
     *
     * @author Bryan Ferrari
 * @author Jacopo Fabbri
     */
    private static class Spinner extends JPanel {
        /** Current rotation angle. */
        private int angle = 0;
        /** Spinner color. */
        private final Color color;
        /** Spinner size in pixels. */
        private final int size;
        /** Animation timer. */
        private final Timer timer;

        /**
         * Builds a spinner with a given color and size.
         *
         * @param color spinner color
         * @param size size in pixels
         */
        Spinner(Color color, int size) {
            this.color = color;
            this.size = size;
            setOpaque(false);
            Dimension dim = new Dimension(size, size);
            setPreferredSize(dim);
            setMinimumSize(dim);
            setMaximumSize(dim);
            timer = new Timer(40, e -> {
                angle = (angle + 12) % 360;
                repaint();
            });
            timer.start();
        }

        @Override
        /**
         * Stops the animation when removed.
         */
        public void removeNotify() {
            super.removeNotify();
            timer.stop();
        }

        @Override
        /**
         * Paints the spinner arc.
         *
         * @param g graphics context
         */
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int pad = 3;
            int d = Math.min(getWidth(), getHeight()) - pad * 2;
            int x = (getWidth() - d) / 2;
            int y = (getHeight() - d) / 2;
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(x, y, d, d, angle, 280);
            g2.dispose();
        }
    }
}
