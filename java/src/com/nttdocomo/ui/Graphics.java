package com.nttdocomo.ui;

import java.awt.*;

public class Graphics {
    private final java.awt.Graphics g;

    Graphics(java.awt.Graphics g) {
        this.g = g;
    }

    public static Object getColorOfName(int name) {
        return switch (name) {
            case 0 -> Color.BLACK;
            case 1 -> Color.BLUE;
            case 2 -> new Color(0, 255, 0);      // LIME
            case 3 -> Color.CYAN;                         // AQUA
            case 4 -> Color.RED;
            case 5 -> Color.MAGENTA;                      // FUCHSIA
            case 6 -> Color.YELLOW;
            case 7 -> Color.WHITE;
            case 8 -> Color.GRAY;
            case 9 -> new Color(0, 0, 128);       // NAVY
            case 10 -> new Color(0, 128, 0);      // GREEN (dark)
            case 11 -> new Color(0, 128, 128);    // TEAL
            case 12 -> new Color(128, 0, 0);      // MAROON
            case 13 -> new Color(128, 0, 128);    // PURPLE
            case 14 -> new Color(128, 128, 0);    // OLIVE
            case 15 -> new Color(192, 192, 192);  // SILVER
            default -> Color.BLACK;
        };
    }

    public static int getColorOfRGB(int r, int g, int b) {
        r = clamp8(r);
        g = clamp8(g);
        b = clamp8(b);
        return (r << 16) | (g << 8) | b;
    }

    private static int clamp8(int v) {
        if (v < 0) return 0;
        if (v > 255) return 255;
        return v;
    }

    public void lock() {
        // no-op: double buffering is already done in Canvas
    }

    public void unlock(boolean present) {
        // no-op: double buffering is already done in Canvas
    }

    public void setFont(Font font) {
        if (font == null) return;
        java.awt.Font awtFont = font.unwrap();
        if (awtFont != null) g.setFont(awtFont);
    }

    public void setColor(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int gg = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        g.setColor(new Color(r, gg, b));
    }

    public void fillRect(int x, int y, int w, int h) {
        g.fillRect(x, y, w, h);
    }

    public void drawRect(int x, int y, int w, int h) {
        g.drawRect(x, y, w, h);
    }

    public void fillArc(int x, int y, int w, int h, int start, int arc) {
        g.fillArc(x, y, w, h, start, arc);
    }

    public void drawString(String str, int x, int y) {
        g.drawString(str, x, y);
    }

    public void setClip(int x, int y, int w, int h) {
        g.setClip(x, y, w, h);
    }

    public void drawImage(Image img, int x, int y) {
        if (img == null) return;
        java.awt.Image awtImg = img.unwrap();
        if (awtImg != null) {
            g.drawImage(awtImg, x, y, null);
        }
    }

}
