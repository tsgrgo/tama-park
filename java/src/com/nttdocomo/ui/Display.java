package com.nttdocomo.ui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class Display {

    private static Canvas current;
    private static Frame frame;

    private static void init() {
        if (frame != null) return;

        frame = new Frame("DoJa Emulator");
        frame.setSize(240, 240);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
    }

    public static void setCurrent(Canvas canvas) {
        init();

        if (current == canvas) return;

        if (current != null) {
            frame.remove(current.unwrap());
        }

        current = canvas;

        if (canvas != null) {
            frame.add(canvas.unwrap(), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            canvas.unwrap().requestFocus();
        }
    }

    public static Canvas getCurrent() {
        return current;
    }
}
