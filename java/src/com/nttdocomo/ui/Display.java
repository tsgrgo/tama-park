package com.nttdocomo.ui;

public class Display {

    private static final Display instance = new Display();

    public static Display getDisplay() {
        return instance;
    }

    public static void setCurrent(Canvas canvas) {
        // stub
    }
}
