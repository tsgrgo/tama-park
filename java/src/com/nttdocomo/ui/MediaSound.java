package com.nttdocomo.ui;

import com.keitaiwiki.music.FloatPcmPlayer;
import com.keitaiwiki.music.MA3Sampler;
import com.keitaiwiki.music.MLD;
import com.keitaiwiki.music.MLDPlayer;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.util.Arrays;

public class MediaSound {

    private Clip clip; // unused for MLD

    private final byte[] mldBytes;

    private Thread audioThread;
    private volatile boolean playing = false;
    private volatile boolean stoppedByUser = false;

    private MediaListener listener;

    public MediaSound(byte[] data) {
        this.mldBytes = (data == null) ? new byte[0] : data.clone();
    }

    public void setListener(MediaListener l) {
        this.listener = l;
    }

    public synchronized void play() {
        stop();

        playing = true;
        stoppedByUser = false;

        audioThread = new Thread(() -> {
            final int sampleRate = 44100;
            final int channels = 2;
            final int framesPerChunk = 512;

            float[] buf = new float[framesPerChunk * channels];

            boolean finishedNaturally = false;

            try {
                MLD mld = new MLD(mldBytes);
                MA3Sampler sampler = new MA3Sampler();
                MLDPlayer renderer = new MLDPlayer(mld, sampler, sampleRate);

                try (FloatPcmPlayer player = new FloatPcmPlayer(sampleRate, channels)) {
                    while (playing) {
                        int rendered = renderer.render(buf, 0, framesPerChunk);
                        player.write(buf, 0, framesPerChunk);

                        // IMPORTANT: detect end-of-track
                        if (rendered <= 0 /* or renderer.isFinished() */) {
                            finishedNaturally = true;
                            break;
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                playing = false;

                // Notify outside audio loop
                if (finishedNaturally && !stoppedByUser && listener != null) {
                    listener.mediaAction(null, 3, 0);
                }
            }
        }, "MediaSound-Audio");

        audioThread.setDaemon(true);
        audioThread.start();
    }

    public synchronized void stop() {
        stoppedByUser = true;
        playing = false;

        if (audioThread != null) {
            try {
                audioThread.join(200);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            audioThread = null;
        }
    }

    public void use() {}

    public Clip unwrap() {
        return clip;
    }
}