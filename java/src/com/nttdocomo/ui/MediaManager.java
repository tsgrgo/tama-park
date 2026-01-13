package com.nttdocomo.ui;

public class MediaManager {

    public static MediaImage getImage(String s) {
        byte[] data = new byte[0];
        return new MediaImage(data);
    }

    public static MediaSound getSound(byte[] data) {
        return new MediaSound();
    }

    public static MediaImage getImage(byte[] data) {
        return new MediaImage(data);
    }
}
