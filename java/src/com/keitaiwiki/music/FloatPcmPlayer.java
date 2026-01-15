package com.keitaiwiki.music;

import javax.sound.sampled.*;

public final class FloatPcmPlayer implements AutoCloseable {
    private final SourceDataLine line;
    private final int sampleRate;
    private final int channels;

    public FloatPcmPlayer(int sampleRate, int channels) throws LineUnavailableException {
        this.sampleRate = sampleRate;
        this.channels = channels;

        AudioFormat fmt = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sampleRate,
                16,                 // bits per sample
                channels,
                channels * 2,       // frame size bytes (16-bit * channels)
                sampleRate,
                false               // little endian
        );

        line = AudioSystem.getSourceDataLine(fmt);
        // Buffer ~50ms
        int bufferBytes = (sampleRate / 20) * fmt.getFrameSize();
        line.open(fmt, bufferBytes);
        line.start();
    }

    /** Write interleaved float samples in range [-1, +1]. */
    public void write(float[] samples, int offsetFrames, int frames) {
        int sampleCount = frames * channels;
        int offsetSamples = offsetFrames * channels;

        byte[] pcm = new byte[sampleCount * 2]; // 16-bit
        int bi = 0;

        for (int i = 0; i < sampleCount; i++) {
            float f = samples[offsetSamples + i];

            // clamp
            if (f > 1f) f = 1f;
            else if (f < -1f) f = -1f;

            short s = (short) Math.round(f * 32767f);

            // little-endian
            pcm[bi++] = (byte) (s & 0xFF);
            pcm[bi++] = (byte) ((s >>> 8) & 0xFF);
        }

        line.write(pcm, 0, pcm.length);
    }

    @Override
    public void close() {
        try { line.drain(); } catch (Exception ignored) {}
        try { line.stop(); } catch (Exception ignored) {}
        try { line.close(); } catch (Exception ignored) {}
    }
}
