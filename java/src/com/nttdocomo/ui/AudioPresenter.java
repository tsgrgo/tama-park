package com.nttdocomo.ui;

import com.keitaiwiki.music.FloatPcmPlayer;
import com.keitaiwiki.music.MA3Sampler;
import com.keitaiwiki.music.MLD;
import com.keitaiwiki.music.MLDPlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class AudioPresenter implements MediaPresenter {
    // Event types
    public static final int AUDIO_PLAYING = 1;
    public static final int AUDIO_STOPPED = 2;
    public static final int AUDIO_COMPLETE = 3;
    public static final int AUDIO_SYNC = 4;
    public static final int AUDIO_PAUSED = 5;
    public static final int AUDIO_RESTARTED = 6;
    public static final int AUDIO_LOOPED = 7;

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

    // Playback config
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNELS = 2;
    private static final int FRAMES_PER_CHUNK = 512;

    private static final Map<Integer, AudioPresenter> INSTANCES = new ConcurrentHashMap<>();

    public static AudioPresenter getAudioPresenter(int port) {
        return INSTANCES.computeIfAbsent(port, AudioPresenter::new);
    }

    private final Map<Integer, Integer> attributes = new ConcurrentHashMap<>();
    private final AtomicBoolean playing = new AtomicBoolean(false);

    private volatile boolean stopRequestedByUser;

    private volatile Thread audioThread;
    private volatile MediaSound sound;
    private volatile MediaListener listener;

    protected AudioPresenter(int port) {
    }

    public synchronized void setSound(MediaSound mediaSound) {
        stop();
        this.sound = mediaSound;
    }

    @Override
    public synchronized void play() {
        stop();
        if (sound == null) return;

        stopRequestedByUser = false;
        playing.set(true);

        startPlaybackThread(sound);
        fireEvent(AUDIO_PLAYING, 0);
    }

    @Override
    public synchronized void stop() {
        stopRequestedByUser = true;

        if (!playing.getAndSet(false)) {
            stopPlaybackThread();
            return;
        }

        stopPlaybackThread();
        fireEvent(AUDIO_STOPPED, 0);
    }

    @Override
    public void setAttribute(int attrib, int value) {
        attributes.put(attrib, value);
    }

    @Override
    public void setMediaListener(MediaListener listener) {
        this.listener = listener;
    }

    private void startPlaybackThread(MediaSound soundSnapshot) {
        Thread t = new Thread(() -> playbackLoop(soundSnapshot), "MediaSound-Audio");
        t.setDaemon(true);
        audioThread = t;
        t.start();
    }

    private void stopPlaybackThread() {
        Thread t = audioThread;
        audioThread = null;

        if (t == null) return;

        try {
            t.join(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void playbackLoop(MediaSound soundSnapshot) {
        boolean finishedNaturally = false;

        float[] buffer = new float[FRAMES_PER_CHUNK * CHANNELS];

        try {
            MLDPlayer renderer = createRenderer(soundSnapshot, SAMPLE_RATE);

            try (FloatPcmPlayer player = new FloatPcmPlayer(SAMPLE_RATE, CHANNELS)) {
                while (playing.get()) {
                    int renderedFrames = renderer.render(buffer, 0, FRAMES_PER_CHUNK);
                    player.write(buffer, 0, FRAMES_PER_CHUNK);
                    if (renderedFrames <= 0) {
                        finishedNaturally = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            playing.set(false);
            if (finishedNaturally && !stopRequestedByUser) {
                fireEvent(AUDIO_COMPLETE, 0);
            }
        }
    }

    private MLDPlayer createRenderer(MediaSound s, int sampleRate) {
        byte[] data = s.getDataUnsafe();
        MLD mld = new MLD(data);
        MA3Sampler sampler = new MA3Sampler();
        return new MLDPlayer(mld, sampler, sampleRate);
    }

    private void fireEvent(int type, int param) {
        MediaListener l = listener;
        if (l == null) return;

        try {
            l.mediaAction(this, type, param);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
