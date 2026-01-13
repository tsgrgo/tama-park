package com.nttdocomo.ui;

public class Graphics {

    public static Object getColorOfName(int i) {
        return null;
    }

    public static int getColorOfRGB(int r, int g, int b) {
        return r;
    }

    public void lock() {}
    public void unlock(boolean present) {}

    public void setFont(Font font) {}
    public void setColor(int rgb) {}

    public void fillRect(int x, int y, int w, int h) {}
    public void drawRect(int x, int y, int w, int h) {}
    public void fillArc(int x, int y, int w, int h, int start, int arc) {}

    public void drawString(String str, int x, int y) {}
    public void drawImage(Image img, int x, int y) {}

    public void setClip(int x, int y, int w, int h) {}
}
