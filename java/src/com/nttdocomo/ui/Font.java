package com.nttdocomo.ui;

public class Font {

    public static Font getFont(int face, int style, int size) {
        return new Font();
    }

    public int getHeight() {
        return 0;
    }

    public int stringWidth(String str) {
        return str != null ? str.length() * 8 : 0;
    }

    public int getDescent() {
        return 0;
    }
}
