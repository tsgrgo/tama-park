package com.nttdocomo.ui;

public class MediaManager {

    public static MediaImage getImage(String s) {
        byte[] data = new byte[0];
        // TODO: read from sp
        return new MediaImage(data);
    }

    public static MediaSound getSound(byte[] data) {
        return new MediaSound(data);
    }

    public static MediaImage getImage(byte[] data) {
        return new MediaImage(data);
    }
}
