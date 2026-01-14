package com.nttdocomo.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Font {
    // --- Font IDs ---
    public static final int FONT_1 = 1895826432;
    public static final int FONT_2 = 1895825664;
    public static final int FONT_3 = 1895825920;
    public static final int FONT_4 = 1896940032;

    // Shared graphics context for metrics
    private static final BufferedImage METRICS_IMG = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Graphics METRICS_G = METRICS_IMG.getGraphics();
    private static final Map<Integer, Font> FONT_CACHE = new HashMap<>();

    public static Font getFont(int i) {
        synchronized (FONT_CACHE) {
            Font f = FONT_CACHE.get(i);
            if (f != null) return f;

            java.awt.Font awtFont;
            switch (i) {
                case FONT_1:
                    awtFont = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10);
                    break;

                case FONT_2:
                    awtFont = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 16);
                    break;

                case FONT_3:
                    awtFont = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12);
                    break;

                case FONT_4:
                default:
                    awtFont = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 11);
                    break;
            }

            f = new Font(awtFont);
            FONT_CACHE.put(i, f);
            return f;
        }
    }

    // ---------------------------------------------------------------------

    private final java.awt.Font font;
    private FontMetrics metrics;

    public Font(java.awt.Font font) {
        this.font = font;
    }

    private FontMetrics metrics() {
        if (metrics == null) {
            synchronized (this) {
                if (metrics == null) {
                    metrics = METRICS_G.getFontMetrics(font);
                }
            }
        }
        return metrics;
    }


    /**
     * Total font height (ascent + descent + leading).
     */
    public int getHeight() {
        return metrics().getHeight();
    }

    /**
     * Font descent (pixels below baseline).
     */
    public int getDescent() {
        return metrics().getDescent();
    }

    /**
     * Pixel width of the string in this font.
     */
    public int stringWidth(String str) {
        if (str == null || str.length() == 0) return 0;
        return metrics().stringWidth(str);
    }

    public java.awt.Font unwrap() {
        return font;
    }
}
