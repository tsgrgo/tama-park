package com.nttdocomo.ui;

import com.sun.jmx.remote.internal.ClientCommunicatorAdmin;

public abstract class IApplication {

    protected IApplication() {}

    protected static ClientCommunicatorAdmin getCurrentApp() {
    }

    public void start() {}
    public void stop() {}
    public void resume() {}
    public void suspend() {}

    protected String getSourceURL() {
        return "";
    }

    protected String[] getArgs() {
        return new String[0];
    }
}