package theknife.utils;

import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Image icon loading and color utilities.
 *
 * @author Bryan Ferrari
 * @author Jacopo Fabbri
 */
public class IconUtils {

    /**
     * Creates a new IconUtils instance.
     */
    public IconUtils() {
    }

    /**
     * Loads and resizes an icon with high-quality rendering.
     *
     * @param resourcePath classpath resource path
     * @param sizePx target size in pixels
     * @return resized icon or null when missing
     */
    public static ImageIcon load(String resourcePath, int sizePx) {
        java.net.URL url = IconUtils.class.getResource(resourcePath);
        if (url == null) {
            System.out.println("⚠️ Icona non trovata: " + resourcePath);
            return null;
        }

        Image img = new ImageIcon(url).getImage();

        // Ridimensionamento di alta qualità
        BufferedImage resized = new BufferedImage(sizePx, sizePx, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();

        // 🔍 Migliori impostazioni per nitidezza
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        // Disegna l'immagine scalata
        g2d.drawImage(img, 0, 0, sizePx, sizePx, null);
        g2d.dispose();

        return new ImageIcon(resized);
    }

    /**
     * Applies a flat tint while preserving transparency.
     *
     * @param src source icon
     * @param color tint color
     * @return tinted icon or null when source is null
     */
    public static ImageIcon tint(ImageIcon src, Color color) {
        if (src == null) return null;
        int w = src.getIconWidth(), h = src.getIconHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(src.getImage(), 0, 0, null);
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(color);
        g.fillRect(0, 0, w, h);
        g.dispose();
        return new ImageIcon(out);
    }
}
