package com.nttdocomo.ui;

import java.util.Arrays;

public final class MediaSound {
    private final byte[] data;

    public MediaSound(byte[] data) {
        if (data == null) throw new NullPointerException("data");
        this.data = Arrays.copyOf(data, data.length);
    }

    byte[] getDataUnsafe() {
        return data;
    }

    public void use() {
    }
}
