package com.nttdocomo.ui;

public final class Display {
    public static void setCurrent(Canvas canvas) {
        IApplication.getCurrentApp().setCanvas(canvas);
    }
}
