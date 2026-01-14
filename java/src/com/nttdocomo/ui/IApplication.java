package com.nttdocomo.ui;

public abstract class IApplication {

    protected IApplication() {}

    protected static IApplication getCurrentApp() {
        return null;
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

    public void launch(int i, String[] var2) {
    }

    public void terminate() {
    }
}