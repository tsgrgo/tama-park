package javax.microedition.io;

import com.nttdocomo.io.HttpConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public final class Connector {

    public static InputStream openInputStream(String name) {
        throw new UnsupportedOperationException();
    }

    public static OutputStream openOutputStream(String name) {
        throw new UnsupportedOperationException();
    }

    public static DataInputStream openDataInputStream(String name) {
        throw new UnsupportedOperationException();
    }

    public static DataOutputStream openDataOutputStream(String name) {
        throw new UnsupportedOperationException();
    }

    public static HttpConnection open(String var3, int i, boolean b) {
        return null;
    }
}
