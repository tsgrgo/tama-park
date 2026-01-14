package com.nttdocomo.ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class MediaImage extends Image {
    public MediaImage(byte[] data) {
        super(decodeImage(data));
    }

    public void use() { /* no-op */}

    public Image getImage() {
        return this;
    }

    private static java.awt.Image decodeImage(byte[] data) {
        if (data == null) data = new byte[0];

        // 1) Try ImageIO first (best for PNG/JPEG/GIF, returns BufferedImage)
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
            if (img != null) {
                return img;
            }
        } catch (Exception ignored) {
        }

        // 2) Fallback: Toolkit can sometimes decode formats ImageIO doesn't
        try {
            java.awt.Image img = Toolkit.getDefaultToolkit().createImage(data);
            // Force load to get width/height available
            if (img != null) {
                // Simple synchronous "wait" without MediaTracker:
                // poke width/height a few times; AWT will load lazily.
                for (int i = 0; i < 20; i++) {
                    int w = img.getWidth(null);
                    int h = img.getHeight(null);
                    if (w > 0 && h > 0) return img;
                    try { Thread.sleep(10); } catch (Exception ignored) {}
                }
                return img;
            }
        } catch (Exception ignored) {
        }

        // 3) Last resort: 1x1 transparent image so the game doesn't crash
        return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    }
}
