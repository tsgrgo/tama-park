package com.nttdocomo.ui;

public interface MediaPresenter {
    // MediaResource getMediaResource();

    void play();

    void stop();

    void setAttribute(int attrib, int value);

    // void setData(MediaData data);

    void setMediaListener(MediaListener listener);
}