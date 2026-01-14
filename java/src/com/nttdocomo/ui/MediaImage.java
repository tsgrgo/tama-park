package com.nttdocomo.ui;

import javax.imageio.ImageIO;
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
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
            if (img != null) return img;
        } catch (Exception ignored) {}
        return null;
    }
}
