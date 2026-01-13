package com.nttdocomo.ui;

public abstract class Canvas {

    protected abstract void paint(Graphics g);

    protected void processEvent(int type, int param) {
        // stub
    }

    public int getWidth() { return 0; }
    public int getHeight() { return 0; }

    public void repaint() {
        // stub
    }

    public void setBackground(Object colorOfName) {
    }

    public void setSoftLabel(int i, String var1) {
    }

    public int getKeypadState() {
        return 0;
    }
}
