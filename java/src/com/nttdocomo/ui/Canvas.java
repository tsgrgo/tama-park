package com.nttdocomo.ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class Canvas {
    // Event type constants
    public static final int KEY_PRESSED_EVENT = 0;
    public static final int KEY_RELEASED_EVENT = 1;

    // Key constants
    public static final int KEY_0 = 0x00;
    // to KEY9
    public static final int KEY_ASTERISK = 0x0A; // '*'
    public static final int KEY_POUND = 0x0B; // '#'


    // Directions / select
    public static final int KEY_LEFT = 0x10;
    public static final int KEY_UP = 0x11;
    public static final int KEY_RIGHT = 0x12;
    public static final int KEY_DOWN = 0x13;
    public static final int KEY_SELECT = 0x14;

    // Soft keys
    public static final int KEY_SOFT1 = 0x15;
    public static final int KEY_SOFT2 = 0x16;


    private final java.awt.Canvas awtCanvas;

    private volatile int keypadStateBits = 0;

    protected Canvas() {
        this.awtCanvas = new java.awt.Canvas() {
            @Override
            public void paint(java.awt.Graphics g) {
                com.nttdocomo.ui.Graphics wrappedG = new com.nttdocomo.ui.Graphics(g, awtCanvas);
                Canvas.this.paint(wrappedG);
            }

            @Override
            public void update(java.awt.Graphics g) {
                paint(g);
            }
        };

        awtCanvas.setBackground(Color.BLACK);

        awtCanvas.setFocusable(true);
        awtCanvas.setPreferredSize(new Dimension(240, 240));

        awtCanvas.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int dojaKeyCode = mapAwtToDojaKey(e);
                updateKeypadState(dojaKeyCode, true);
                processEvent(KEY_PRESSED_EVENT, dojaKeyCode);
                // repaint(); ??
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int dojaKeyCode = mapAwtToDojaKey(e);
                updateKeypadState(dojaKeyCode, false);
                processEvent(KEY_RELEASED_EVENT, e.getKeyCode());
            }
        });

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

    private static int mapAwtToDojaKey(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Directions / select
        switch (keyCode) {
            case KeyEvent.VK_LEFT: return KEY_LEFT;
            case KeyEvent.VK_RIGHT: return KEY_RIGHT;
            case KeyEvent.VK_UP: return KEY_UP;
            case KeyEvent.VK_DOWN: return KEY_DOWN;
            case KeyEvent.VK_ENTER: return KEY_SELECT;
            // Soft keys: map to function keys as a reasonable desktop substitute
            case KeyEvent.VK_F1: return KEY_SOFT1;
            case KeyEvent.VK_F2: return KEY_SOFT2;
        }

        // Digits (top row or numpad)
        if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) {
            return KEY_0 + (keyCode - KeyEvent.VK_0);
        }
        if (keyCode >= KeyEvent.VK_NUMPAD0 && keyCode <= KeyEvent.VK_NUMPAD9) {
            return KEY_0 + (keyCode - KeyEvent.VK_NUMPAD0);
        }

        // Symbols
        char ch = e.getKeyChar();
        if (ch == '*') return KEY_ASTERISK;
        if (ch == '#') return KEY_POUND;

        // Not mapped
        return -1;
    }

    private synchronized void updateKeypadState(int dojaKeyCode, boolean pressed) {
        if (dojaKeyCode < 0 || dojaKeyCode >= 32) {
            return;
        }

        int bit = 1 << dojaKeyCode;

        if (pressed) keypadStateBits |= bit;
        else keypadStateBits &= ~bit;
    }
}
