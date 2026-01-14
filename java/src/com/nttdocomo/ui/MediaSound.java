package com.nttdocomo.ui;

import javax.sound.sampled.*;

public class MediaSound {

    private Clip clip;

    MediaSound(byte[] data) {
        // TODO: decode audio...
    }

    public void play() {
        if (clip == null) return;
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void stop() {
        if (clip == null) return;
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    public void use() {}

    public Clip unwrap() {
        return clip;
    }
}
