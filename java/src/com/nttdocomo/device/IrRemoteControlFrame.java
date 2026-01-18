package com.nttdocomo.device;

import java.util.Arrays;

public class IrRemoteControlFrame {
    public static final int COUNT_INFINITE = 0;

    private byte[] dataBytes;
    private int dataBitLength = -1;

    private int frameDuration = -1; // 0.1 ms units
    private int repeatCount = -1;
    private int startHighUs = -1;
    private int startLowUs = -1;
    private int stopHighUs = -1;

    /**
     * Data is transmitted from the first byte, MSB -> LSB order.
     */
    public void setFrameData(byte[] data, int bitLength) {
        if (data == null) throw new NullPointerException("data");
        int maxBits = data.length * 8;

        if (bitLength < 0 || bitLength > maxBits) {
            throw new IllegalArgumentException("bitLength out of range: " + bitLength);
        }

        // Store only the needed bytes (ceil(bitLength/8));
        int neededBytes = (bitLength + 7) / 8;

        dataBytes = (neededBytes == 0) ? new byte[0] : Arrays.copyOf(data, neededBytes);
        dataBitLength = bitLength;
    }

    /**
     * Sets the frame data portion using up to 128 bits.
     * Data is transmitted data1 MSB->LSB then data2 MSB->LSB.
     */
    public void setFrameData(long data1, int bitLength1, long data2, int bitLength2) {
        if (bitLength1 < 0 || bitLength1 > 64) {
            throw new IllegalArgumentException("bitLength1 out of range: " + bitLength1);
        }
        if (bitLength2 < 0 || bitLength2 > 64) {
            throw new IllegalArgumentException("bitLength2 out of range: " + bitLength2);
        }

        int totalBits = bitLength1 + bitLength2;
        if (totalBits == 0) {
            dataBytes = new byte[0];
            dataBitLength = 0;
            return;
        }

        byte[] packed = new byte[(totalBits + 7) / 8];
        int bitPos = writeBitsToPacked(packed, 0, data1, bitLength1);
        writeBitsToPacked(packed, bitPos, data2, bitLength2);

        dataBytes = packed;
        dataBitLength = totalBits;
    }

    /**
     * Sets the frame repeat interval in 0.1 ms units.
     */
    public void setFrameDuration(int duration) {
        if (duration <= 0) throw new IllegalArgumentException("duration must be > 0");
        frameDuration = duration;
    }

    /**
     * Sets how many times this frame is transmitted repeatedly.
     * 0 = infinite
     */
    public void setRepeatCount(int count) {
        if (count < 0) throw new IllegalArgumentException("count must be >= 0");
        repeatCount = count;
    }

    /**
     * Sets start section High time in microseconds.
     * If both startHigh and startLow are 0, start section is not sent.
     */
    public void setStartHighDuration(int duration) {
        if (duration < 0) throw new IllegalArgumentException("duration must be >= 0");
        startHighUs = duration;
    }

    /**
     * Sets start section Low time in microseconds.
     * If both startHigh and startLow are 0, start section is not sent.
     */
    public void setStartLowDuration(int duration) {
        if (duration < 0) throw new IllegalArgumentException("duration must be >= 0");
        startLowUs = duration;
    }

    /**
     * Sets stop section High time in microseconds.
     * If 0, stop section is not sent.
     */
    public void setStopHighDuration(int duration) {
        if (duration < 0) throw new IllegalArgumentException("duration must be >= 0");
        stopHighUs = duration;
    }

    void validate() {
        // DoJa-4.0+ rule: everything must be explicitly set
        if (dataBytes == null || dataBitLength < 0) throw new IllegalArgumentException("Frame data not set");
        if (frameDuration <= 0) throw new IllegalArgumentException("Frame duration not set");
        if (repeatCount < 0) throw new IllegalArgumentException("Repeat count not set");
        if (startHighUs < 0) throw new IllegalArgumentException("StartHigh not set");
        if (startLowUs < 0) throw new IllegalArgumentException("StartLow not set");
        if (stopHighUs < 0) throw new IllegalArgumentException("StopHigh not set");
    }

    byte[] getDataBytes() {
        return dataBytes;
    }

    int getDataBitLength() {
        return dataBitLength;
    }

    int getFrameDuration() {
        return frameDuration;
    }

    int getRepeatCount() {
        return repeatCount;
    }

    int getStartHighUs() {
        return startHighUs;
    }

    int getStartLowUs() {
        return startLowUs;
    }

    int getStopHighUs() {
        return stopHighUs;
    }

    private static int writeBitsToPacked(byte[] packed, int startingPos, long value, int bitLength) {
        if (bitLength <= 0) return startingPos;
        int startShift = 64 - bitLength;

        for (int i = 0; i < bitLength; i++) {
            int bit = (int) ((value >>> (63 - (startShift + i))) & 1L);
            setPackedBit(packed, startingPos++, bit);
        }

        return startingPos;
    }

    private static void setPackedBit(byte[] packed, int bitPos, int bit) {
        int byteIndex = bitPos / 8;
        int bitInByte = bitPos % 8;
        int mask = 0x80 >>> bitInByte; // MSB first
        if (bit != 0) packed[byteIndex] = (byte) (packed[byteIndex] | mask);
        else packed[byteIndex] = (byte) (packed[byteIndex] & ~mask);
    }
}