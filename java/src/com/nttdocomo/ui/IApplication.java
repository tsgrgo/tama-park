package com.nttdocomo.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public abstract class IApplication {
    private static IApplication currentApp;

    private String[] args = new String[0];
    private Canvas canvas;
    private Frame frame;
    
    protected IApplication() {
        currentApp = this;
        createFrame();
    }

    protected static IApplication getCurrentApp() {
        return currentApp;
    }

    public abstract void start();

    public abstract void resume();

    protected String getSourceURL() {
        return "";
    }

    protected String[] getArgs() {
        return (args == null) ? new String[0] : Arrays.copyOf(args, args.length);
    }

    public void launch(int type, String[] args) {
        if (args != null) this.args = Arrays.copyOf(args, args.length);
        System.out.println("IApplication.launch type=" + type + " args=" + Arrays.toString(this.args));
    }

    public void terminate() {
        System.out.println("terminated");
        if (frame != null) frame.dispose();
        System.exit(0);
    }

    void setCanvas(Canvas canvas) {
        if (this.canvas == canvas) return;

        if (this.canvas != null) {
            frame.remove(this.canvas.unwrap());
        }

        this.canvas = canvas;

        if (canvas != null) {
            frame.add(canvas.unwrap(), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            canvas.unwrap().requestFocus();
        }
    }

    private void createFrame() {
        if (frame != null) return;

        frame = new Frame("DoJa Emulator");
        frame.setSize(240, 240);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                terminate();
            }
        });
    }

}