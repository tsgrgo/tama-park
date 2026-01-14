package com.nttdocomo.ui;

import java.awt.*;

public class Graphics {
    private final java.awt.Graphics g;

    public Graphics(java.awt.Graphics g) {
        this.g = g;
    }

    public static Object getColorOfName(int i) {
        // Random colors for now
        switch (i) {
            case 0:  return Color.black;
            case 1:  return Color.white;
            case 2:  return Color.red;
            case 3:  return Color.green;
            case 4:  return Color.blue;
            case 5:  return Color.yellow;
            case 6:  return Color.cyan;
            case 7:  return Color.magenta;
            case 8:  return Color.gray;
            case 9:  return Color.lightGray;
            case 10: return Color.darkGray;
            default: return Color.black;
        }
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

    public void lock() {/* no-op */}
    public void unlock(boolean present) {/* no-op */}

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

    public void fillRect(int x, int y, int w, int h) { g.fillRect(x, y, w, h); }
    public void drawRect(int x, int y, int w, int h) { g.drawRect(x, y, w, h); }
    public void fillArc(int x, int y, int w, int h, int start, int arc) { g.fillArc(x, y, w, h, start, arc); }
    public void drawString(String str, int x, int y) { g.drawString(str, x, y); }
    public void setClip(int x, int y, int w, int h) { g.setClip(x, y, w, h); }

    public void drawImage(Image img, int x, int y) {
        if (img == null) return;
        java.awt.Image awtImg = img.unwrap();
        if (awtImg != null) {
            g.drawImage(awtImg, x, y, null);
        }
    }

    public java.awt.Graphics unwrap() { return g; }
}
