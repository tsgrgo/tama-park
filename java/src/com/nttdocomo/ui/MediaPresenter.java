package com.nttdocomo.ui;

public class MediaPresenter {
    public static final int MEDIA_STARTED  = 1;
    public static final int MEDIA_STOPPED  = 2;
    public static final int MEDIA_FINISHED = 3;
    public static final int MEDIA_ERROR    = 4;

    private MediaListener listener;

    public void setMediaListener(MediaListener listener) {
        this.listener = listener;
    }

    public void play() {}
    public void stop() {}

    protected void notifyListener(int event) {
        if (listener == null) return;
        try {
            listener.mediaAction(this, event, 0);
        } catch (Exception ignored) {}
    }

    protected void notifyListener(int event, int arg) {
        if (listener == null) return;
        try {
            listener.mediaAction(this, event, arg);
        } catch (Exception ignored) {}
    }
}
