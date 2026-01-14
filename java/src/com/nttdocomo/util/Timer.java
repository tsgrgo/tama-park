package com.nttdocomo.util;

import java.util.TimerTask;

public class Timer {

    private TimerListener listener;
    private int delayMillis;
    private boolean repeat;

    private java.util.Timer internalTimer;
    private TimerTask task;
    private boolean running;

    public Timer() {
        this.running = false;
    }

    public void setTime(int millis) {
        this.delayMillis = millis;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setListener(TimerListener listener) {
        this.listener = listener;
    }

    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("Timer already running");
        }

        if (listener == null) {
            throw new IllegalStateException("TimerListener not set");
        }

        internalTimer = new java.util.Timer();
        running = true;

        task = new TimerTask() {
            @Override
            public void run() {
                listener.timerExpired(Timer.this);

                if (!repeat) {
                    stop();
                }
            }
        };

        if (repeat) {
            internalTimer.schedule(task, delayMillis, delayMillis);
        } else {
            internalTimer.schedule(task, delayMillis);
        }
    }

    public synchronized void stop() {
        running = false;

        if (task != null) {
            task.cancel();
            task = null;
        }

        if (internalTimer != null) {
            internalTimer.cancel();
            internalTimer = null;
        }
    }
}
