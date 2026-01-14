package com.nttdocomo.ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class Canvas {
    // Event type constants
    public static final int EVENT_KEY_PRESSED  = 0;
    public static final int EVENT_KEY_RELEASED = 1;
    public static final int EVENT_KEY_TYPED    = 2;

    // Keypad state bitmask flags
    public static final int KEY_LEFT  = 1 << 0;
    public static final int KEY_RIGHT = 1 << 1;
    public static final int KEY_UP    = 1 << 2;
    public static final int KEY_DOWN  = 1 << 3;
    public static final int KEY_ENTER = 1 << 4;

    private final java.awt.Canvas awtCanvas;

    private volatile int keypadStateBits = 0;

    protected Canvas() {
        this.awtCanvas = new java.awt.Canvas() {
            @Override
            public void paint(java.awt.Graphics g) {
                com.nttdocomo.ui.Graphics wrappedG = new com.nttdocomo.ui.Graphics(g);
                Canvas.this.paint(wrappedG);
            }

            @Override
            public void update(java.awt.Graphics g) {
                // Avoid flicker: don't clear background here, just paint
                paint(g);
            }
        };

        awtCanvas.setBackground(Color.BLACK);

        awtCanvas.setFocusable(true);
        awtCanvas.setPreferredSize(new Dimension(240, 240));

        awtCanvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                processEvent(EVENT_KEY_TYPED, e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                updateKeypadState(e.getKeyCode(), true);
                processEvent(EVENT_KEY_PRESSED, e.getKeyCode());
                // repaint(); ??
            }

            @Override
            public void keyReleased(KeyEvent e) {
                updateKeypadState(e.getKeyCode(), false);
                processEvent(EVENT_KEY_RELEASED, e.getKeyCode());
            }
        });

        // IDK why the game doesnt call repaint or something like that...
        // Dirty fix for now
        new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask() {
            @Override public void run() {
                awtCanvas.repaint();
            }
        }, 0, 16);
    }


    protected abstract void paint(Graphics g);
    protected abstract void processEvent(int type, int param);

    public int getWidth() { return 240; }
    public int getHeight() { return 240; }
    public void repaint() { awtCanvas.repaint(); }

    public void setBackground(Object color) {
        if (color instanceof Color) { // Might wrap color properly in the future
            awtCanvas.setBackground((Color) color);
        }
    }

    public void setSoftLabel(int which, String text) {
        if (text == null) text = "";
        if (which == 0) System.out.println("left soft label:" + text);
        else if (which == 1) System.out.println("right soft label:" + text);
    }

    public int getKeypadState() {
        return keypadStateBits;
    }

    public java.awt.Canvas unwrap() { return awtCanvas; }

    private synchronized void updateKeypadState(int keyCode, boolean pressed) {
        int bit = 0;

        switch (keyCode) {
            case KeyEvent.VK_LEFT:  bit = KEY_LEFT;  break;
            case KeyEvent.VK_RIGHT: bit = KEY_RIGHT; break;
            case KeyEvent.VK_UP:    bit = KEY_UP;    break;
            case KeyEvent.VK_DOWN:  bit = KEY_DOWN;  break;
            case KeyEvent.VK_ENTER: bit = KEY_ENTER; break;
            default: return;
        }

        if (pressed) keypadStateBits |= bit;
        else keypadStateBits &= ~bit;
    }
}
