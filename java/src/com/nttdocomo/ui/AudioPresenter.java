package com.nttdocomo.ui;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AudioPresenter implements MediaPresenter {
    // Event types
    public static final int AUDIO_PLAYING = 1;
    public static final int AUDIO_STOPPED = 2;
    public static final int AUDIO_COMPLETE = 3;
    public static final int AUDIO_SYNC = 4;
    public static final int AUDIO_PAUSED = 5;
    public static final int AUDIO_RESTARTED = 6;
    public static final int AUDIO_LOOPED = 7; // optional API in docs

    // Attributes
    public static final int PRIORITY = 1;
    public static final int SYNC_MODE = 2;
    public static final int TRANSPOSE_KEY = 3;
    public static final int SET_VOLUME = 4;
    public static final int CHANGE_TEMPO = 5;
    public static final int LOOP_COUNT = 6;

    public static final int ATTR_SYNC_OFF = 0;
    public static final int ATTR_SYNC_ON = 1;

    public static final int MIN_PRIORITY = 1;
    public static final int NORM_PRIORITY = 5;
    public static final int MAX_PRIORITY = 10;

    private static final Map<Integer, AudioPresenter> INSTANCES = new ConcurrentHashMap<>();

    public static AudioPresenter getAudioPresenter(int port) {
        return INSTANCES.computeIfAbsent(port, k -> new AudioPresenter(port));
    }

    private final int port;
    private final Map<Integer, Integer> attributes = new HashMap<>();

    private volatile MediaListener listener;

    protected AudioPresenter(int port) {
        this.port = port;
        // defaults
        attributes.put(PRIORITY, NORM_PRIORITY);
        attributes.put(SYNC_MODE, ATTR_SYNC_OFF);
        attributes.put(SET_VOLUME, 100); // emulator convention: 0..100
        attributes.put(CHANGE_TEMPO, 100); // 100 = normal
        attributes.put(TRANSPOSE_KEY, 0);
        attributes.put(LOOP_COUNT, 1); // play once
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
            } catch (Exception ignored) {
            }
        }

        this.sound = mediaSound;
        this.clip = (mediaSound != null) ? mediaSound.unwrap() : null;

        if (clip != null) {
            try {
                clip.addLineListener(lineListener);
            } catch (Exception ignored) {
            }
        }
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

    @Override
    public void setAttribute(int attrib, int value) {

    }

    @Override
    public void setMediaListener(MediaListener listener) {
        this.listener = listener;
    }

}
