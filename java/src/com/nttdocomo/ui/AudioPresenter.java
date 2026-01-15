package com.nttdocomo.ui;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AudioPresenter extends MediaPresenter {
    private static final Map<Integer, AudioPresenter> INSTANCES = new ConcurrentHashMap<>();

    public static AudioPresenter getAudioPresenter(int i) {
        return INSTANCES.computeIfAbsent(i, k -> new AudioPresenter());
    }

    private MediaSound sound;
    private Clip clip;

    private final LineListener lineListener = e -> {
        if (clip == null) return;

        if (e.getType() == LineEvent.Type.START) {
            notifyListener(MEDIA_STARTED);
        } else if (e.getType() == LineEvent.Type.STOP) {
            boolean finished =
                    clip.getFrameLength() > 0 &&
                            clip.getFramePosition() >= clip.getFrameLength();

            notifyListener(finished ? MEDIA_FINISHED : MEDIA_STOPPED);
        }
    };

    public void setSound(MediaSound mediaSound) {
        mediaSound.setListener(listener);
        if (clip != null) {
            try {
                clip.removeLineListener(lineListener);
            } catch (Exception ignored) {}
        }

        this.sound = mediaSound;
        this.clip = (mediaSound != null) ? mediaSound.unwrap() : null;

        if (clip != null) {
            try {
                clip.addLineListener(lineListener);
            } catch (Exception ignored) {}
        }
    }

    public void setAttribute(int i, int i1) {
        // Not implemented
    }

    @Override
    public void play() {
        if (sound != null) {
            sound.play();
        }
    }

    @Override
    public void stop() {
        if (sound != null) {
            sound.stop();
        }
    }

}
