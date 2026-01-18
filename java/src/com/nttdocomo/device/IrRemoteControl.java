package com.nttdocomo.device;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class IrRemoteControl {

    /**
     * Indicates the code output pattern is High-first (= 0).
     */
    public static final int PATTERN_HL = 0;
    /**
     * Indicates the code output pattern is Low-first (= 1).
     */
    public static final int PATTERN_LH = 1;

    private static final IrRemoteControl INSTANCE = new IrRemoteControl();

    public static IrRemoteControl getIrRemoteControl() {
        return INSTANCE;
    }

    private int carrierHighUs = -1;
    private int carrierLowUs = -1;

    private Pulse code0;
    private Pulse code1;

    private Thread sendThread;
    private final AtomicBoolean sending = new AtomicBoolean(false);

    protected IrRemoteControl() {
    }

    /**
     * @param highDuration carrier High duration (0.1 microsecond units).
     * @param lowDuration  carrier Low duration (0.1 microsecond units).
     */
    public synchronized void setCarrier(int highDuration, int lowDuration) {
        if (highDuration <= 0 || lowDuration <= 0) {
            throw new IllegalArgumentException("Carrier durations must be > 0");
        }
        this.carrierHighUs = highDuration;
        this.carrierLowUs = lowDuration;

        log("Carrier set: HIGH=%dus LOW=%dus", highDuration, lowDuration);
    }

    /**
     * Sets the logical "0" pulse information.
     *
     * @param pattern      PATTERN_HL or PATTERN_LH
     * @param highDuration pulse High duration
     * @param lowDuration  pulse Low duration
     */
    public synchronized void setCode0(int pattern, int highDuration, int lowDuration) {
        this.code0 = new Pulse(pattern, highDuration, lowDuration);
        log("Code0 set: %s", code0);
    }

    /**
     * Sets the logical "1" pulse information.
     *
     * @param pattern      PATTERN_HL or PATTERN_LH
     * @param highDuration pulse High duration
     * @param lowDuration  pulse Low duration
     */
    public synchronized void setCode1(int pattern, int highDuration, int lowDuration) {
        this.code1 = new Pulse(pattern, highDuration, lowDuration);
        log("Code1 set: %s", code1);
    }

    /**
     * @param numFrames number of frames to send (must be >= 1).
     * @param frames    array of frames to send.
     */
    public void send(int numFrames, IrRemoteControlFrame[] frames) {
        send(numFrames, frames, 10, Integer.MAX_VALUE);
    }

    /**
     * @param numFrames number of frames to send (must be >= 1).
     * @param frames    array of frames to send.
     * @param timeout   timeout value (seconds).
     */
    public void send(int numFrames, IrRemoteControlFrame[] frames, int timeout) {
        send(numFrames, frames, timeout, Integer.MAX_VALUE);
    }

    /**
     * @param numFrames number of frames to send (must be >= 1).
     * @param frames    array of frames to send.
     * @param timeout   timeout value (seconds).
     * @param count     number of times to transmit the signal.
     */
    public synchronized void send(int numFrames,
                                  IrRemoteControlFrame[] frames,
                                  int timeout,
                                  int count) {

        validateSendArgs(numFrames, frames, timeout, count);
        stop();
        sending.set(true);

        sendThread = new Thread(() -> runSendLoop(numFrames, frames, timeout, count),
                "IrRemoteControl-Send");
        sendThread.setDaemon(true);
        sendThread.start();
    }

    private void runSendLoop(int numFrames,
                             IrRemoteControlFrame[] frames,
                             int timeoutSec,
                             int count) {

        log("=== IR SEND START ===");

        long endTimeMs = System.currentTimeMillis() + timeoutSec * 1000L;

        try {
            for (int cycle = 1; cycle <= count; cycle++) {
                if (shouldStop(endTimeMs)) break;
                log("Send cycle %d", cycle);
                boolean completed = sendOneCycle(numFrames, frames, endTimeMs);
                if (!completed) break;
            }
        } finally {
            sending.set(false);
            log("=== IR SEND END ===");
        }
    }

    private boolean sendOneCycle(int numFrames,
                                 IrRemoteControlFrame[] frames,
                                 long endTimeMs) {

        for (int i = 0; i < numFrames; i++) {
            if (shouldStop(endTimeMs)) return false;

            IrRemoteControlFrame f = frames[i];
            logFrame(i, f);

            boolean shouldContinue = sendFrameWithRepeats(i, f, endTimeMs);
            if (!shouldContinue) return false;
        }
        return true;
    }

    private boolean sendFrameWithRepeats(int index,
                                         IrRemoteControlFrame f,
                                         long endTimeMs) {

        int repeatCount = f.getRepeatCount();

        if (repeatCount == IrRemoteControlFrame.COUNT_INFINITE) {
            log("Frame %d repeats infinitely", index);

            while (!shouldStop(endTimeMs)) {
                sleepFrame(f);
            }

            return false;
        } else {
            for (int i = 0; i < repeatCount; i++) {
                if (shouldStop(endTimeMs)) return false;
                sleepFrame(f);
            }

            return true;
        }
    }

    private boolean shouldStop(long endTimeMs) {
        return !sending.get() || System.currentTimeMillis() >= endTimeMs || Thread.currentThread().isInterrupted();
    }

    private void validateSendArgs(int numFrames,
                                  IrRemoteControlFrame[] frames,
                                  int timeout,
                                  int count) {
        if (frames == null) throw new NullPointerException("frames");
        if (numFrames <= 0) throw new ArrayIndexOutOfBoundsException("numFrames <= 0");
        if (frames.length < numFrames) throw new ArrayIndexOutOfBoundsException("frames too short");
        if (timeout <= 0) throw new IllegalArgumentException("timeout <= 0");
        if (count <= 0) throw new IllegalArgumentException("count <= 0");

        if (carrierHighUs <= 0 || carrierLowUs <= 0) {
            throw new IllegalArgumentException("Carrier not configured");
        }
        if (code0 == null || code1 == null) {
            throw new IllegalArgumentException("Code pulses not configured");
        }

        for (int i = 0; i < numFrames; i++) {
            IrRemoteControlFrame f = frames[i];
            if (f == null) throw new NullPointerException("frame[" + i + "] is null");
            f.validate();
        }
    }


    public synchronized void stop() {
        if (!sending.get()) return;

        sending.set(false);

        if (sendThread != null) {
            try {
                sendThread.join(200);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            sendThread = null;
        }

        log("IR transmission stopped");
    }

    private void sleepFrame(IrRemoteControlFrame f) {
        try {
            long ms = f.getFrameDuration() / 10; // 0.1 ms units --> ms
            if (ms > 0) Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    private void logFrame(int index, IrRemoteControlFrame frame) {
        log(
                "Frame[%d]: bits=%d data=%s start(H=%dus L=%dus) stop(H=%dus) repeat=%d dur=%d(0.1ms)",
                index,
                frame.getDataBitLength(),
                Arrays.toString(frame.getDataBytes()),
                frame.getStartHighUs(),
                frame.getStartLowUs(),
                frame.getStopHighUs(),
                frame.getRepeatCount(),
                frame.getFrameDuration()
        );
    }

    private static void log(String fmt, Object... args) {
        System.out.println("[IR] " + String.format(fmt, args));
    }

    private static final class Pulse {
        final int pattern;
        final int highDuration;
        final int lowDuration;

        Pulse(int pattern, int highDuration, int lowDuration) {
            if (pattern != PATTERN_HL && pattern != PATTERN_LH) {
                throw new IllegalArgumentException("Invalid pattern: " + pattern);
            }
            if (highDuration <= 0 || lowDuration <= 0) {
                throw new IllegalArgumentException("Pulse durations must be > 0");
            }
            this.pattern = pattern;
            this.highDuration = highDuration;
            this.lowDuration = lowDuration;
        }

        @Override
        public String toString() {
            return (pattern == PATTERN_HL ? "HL" : "LH") +
                    " HIGH=" + highDuration + "us LOW=" + lowDuration + "us";
        }
    }
}
