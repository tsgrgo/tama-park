package com.nttdocomo.ui;

public class Image {
    protected java.awt.Image awtImage;

    protected Image(java.awt.Image awtImage) {
        this.awtImage = awtImage;
    }

    public int getWidth() {
        if (awtImage == null) return 0;
        int w = awtImage.getWidth(null);
        return (w < 0) ? 0 : w;
    }

    public int getHeight() {
        if (awtImage == null) return 0;
        int h = awtImage.getHeight(null);
        return (h < 0) ? 0 : h;
    }
    public void dispose() {
        if (awtImage != null) {
            awtImage.flush();
            awtImage = null;
        }
    }

    public java.awt.Image unwrap() {
        return awtImage;
    }
}
