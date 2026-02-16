package com.nttdocomo.ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public final class MediaSound {
    private final byte[] data;
    private static int c = 0;

    public MediaSound(byte[] data) {
        if (data == null) throw new NullPointerException("data");
        this.data = Arrays.copyOf(data, data.length);
//        try {
//            saveToDesktop(String.valueOf(c++));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    byte[] getDataUnsafe() {
        return data;
    }

    public void saveToDesktop(String fileName) throws IOException {
        if (!fileName.endsWith(".mld")) {
            fileName += ".mld";
        }

        Path folderPath = Paths.get(
                System.getProperty("user.home"),
                "Desktop",
                "mld_sounds"   // ← folder name
        );

        Files.createDirectories(folderPath); // creates folder if missing

        Path filePath = folderPath.resolve(fileName);
        Files.write(filePath, data);
    }

    public void use() {
        // whatever this does
    }
}
