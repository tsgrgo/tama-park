package com.nttdocomo.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DesktopHttpConnection implements HttpConnection {

    private final String urlString;

    private HttpURLConnection conn;
    private String method = "GET";
    private boolean connected = false;

    private InputStream inputStream;
    private OutputStream outputStream;

    public DesktopHttpConnection(String urlString) {
        this.urlString = urlString;
    }

    @Override
    public void setRequestMethod(String get) {
        if (get != null && !get.isEmpty()) {
            this.method = get;
        }
    }

    @Override
    public void connect() {
        if (connected) return;

        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);

            // Reasonable defaults; adjust if needed
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(30000);

            // allow output for non-GET methods
            if (!"GET".equalsIgnoreCase(method) && !"HEAD".equalsIgnoreCase(method)) {
                conn.setDoOutput(true);
            }

            conn.connect();
            connected = true;
        } catch (IOException e) {
            throw new RuntimeException("HttpConnection.connect failed for: " + urlString, e);
        }
    }

    @Override
    public InputStream openInputStream() {
        connect();
        try {
            if (inputStream != null) return inputStream;

            // For HTTP errors, getInputStream() throws; fall back to error stream if present.
            try {
                inputStream = conn.getInputStream();
            } catch (IOException ex) {
                InputStream err = conn.getErrorStream();
                if (err != null) {
                    inputStream = err;
                } else {
                    throw ex;
                }
            }
            return inputStream;
        } catch (IOException e) {
            throw new RuntimeException("HttpConnection.openInputStream failed for: " + urlString, e);
        }
    }

    @Override
    public DataInputStream openDataInputStream() {
        return new DataInputStream(openInputStream());
    }

    @Override
    public OutputStream openOutputStream() {
        connect();
        try {
            if (outputStream != null) return outputStream;
            conn.setDoOutput(true);
            outputStream = conn.getOutputStream();
            return outputStream;
        } catch (IOException e) {
            throw new RuntimeException("HttpConnection.openOutputStream failed for: " + urlString, e);
        }
    }

    @Override
    public int getLength() {
        connect();
        int len = conn.getContentLength();
        return len; // -1 if unknown
    }

    @Override
    public void close() {
        // Close streams first
        try { if (inputStream != null) inputStream.close(); } catch (Exception ignored) {}
        try { if (outputStream != null) outputStream.close(); } catch (Exception ignored) {}

        inputStream = null;
        outputStream = null;

        if (conn != null) {
            conn.disconnect();
            conn = null;
        }

        connected = false;
    }
}
