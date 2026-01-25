package com.nttdocomo.io;


import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public interface HttpConnection {
    InputStream openInputStream();

    OutputStream openOutputStream();

    void close();

    DataInputStream openDataInputStream();

    void setRequestMethod(String get);

    void connect();

    long getLength();
}