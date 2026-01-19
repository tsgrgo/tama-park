package com.nttdocomo.ui;

import javax.microedition.io.Connector;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MediaManager {

    public static MediaImage getImage(String location) {
        if (location == null) {
            throw new NullPointerException("location");
        }

        if (location.startsWith("comm:") || location.startsWith("obex:")) {
            throw new IllegalArgumentException("Unsupported location: " + location);
        }

        try (DataInputStream in = new DataInputStream(Connector.openInputStream(location))) {
            // Disgusting solution but IDK how to do this better for now...
            byte[] data = readAll(in);
            return new MediaImage(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image: " + location, e);
        }
    }

    public static MediaImage getImage(byte[] data) {
        if (data == null) throw new NullPointerException("data");
        return new MediaImage(data);
    }

    public static MediaSound getSound(byte[] data) {
        if (data == null) throw new NullPointerException("data");
        return new MediaSound(data);
    }

    private static byte[] readAll(DataInputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
        byte[] buf = new byte[4096];
        int n;

        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }

        return out.toByteArray();
    }
}
