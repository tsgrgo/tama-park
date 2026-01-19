package javax.microedition.io;

import com.nttdocomo.io.DesktopHttpConnection;
import com.nttdocomo.io.HttpConnection;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public final class Connector {
    private static final int SCRATCHPAD_HEADER_SIZE = 64;
    private static volatile File scratchpadFile;

    public static void setScratchpadFile(File file) {
        scratchpadFile = file;
    }

    // public static HttpConnection open(String var3, int i, boolean b)
    public static HttpConnection open(String url, int mode, boolean timeouts) throws IOException {
        System.out.println("http open: " + url);
        return new DesktopHttpConnection(url);
    }

    public static InputStream openInputStream(String location) throws IOException {
        System.out.println("openInputStream: " + location);
        ParsedUrl u = parse(location);
        // if (!"scratchpad".equals(u.scheme)) return null;
        long pos = u.getLongParam("pos", 0);
        return new ScratchpadInputStream(scratchpadFile, pos);
    }

    public static OutputStream openOutputStream(String location) throws IOException {
        System.out.println("openOutputStream: " + location);
        ParsedUrl u = parse(location);
        // if (!"scratchpad".equals(u.scheme)) return null;
        long pos = u.getLongParam("pos", 0);
        return new ScratchpadOutputStream(scratchpadFile, pos);
    }

    public static DataInputStream openDataInputStream(String location) throws IOException {
        return new DataInputStream(openInputStream(location));
    }

    public static DataOutputStream openDataOutputStream(String location) throws IOException {
        return new DataOutputStream(openOutputStream(location));
    }

    // -------------------------------------

    private static ParsedUrl parse(String raw) {
        // Examples:
        //  scratchpad:///0;pos=128
        //  https://example.com/a.bin
        //  file path
        int schemeIdx = raw.indexOf(':');
        if (schemeIdx <= 0) return new ParsedUrl("file", raw, new HashMap<>());

        String scheme = raw.substring(0, schemeIdx).toLowerCase();
        if (!"scratchpad".equals(scheme)) {
            return new ParsedUrl(scheme, raw, new HashMap<>());
        }

        // scratchpad:///0;pos=123;foo=bar
        // Split parameters after the first ';'
        int semi = raw.indexOf(';');
        Map<String, String> params = new HashMap<>();
        if (semi >= 0 && semi + 1 < raw.length()) {
            String paramPart = raw.substring(semi + 1);
            String[] pairs = paramPart.split(";");
            for (String p : pairs) {
                int eq = p.indexOf('=');
                if (eq > 0 && eq + 1 < p.length()) {
                    params.put(p.substring(0, eq), p.substring(eq + 1));
                } else if (!p.isEmpty()) {
                    params.put(p, "");
                }
            }
        }

        return new ParsedUrl("scratchpad", raw, params);
    }

    private static final class ParsedUrl {
        final String scheme;
        final String raw;
        final Map<String, String> params;

        ParsedUrl(String scheme, String raw, Map<String, String> params) {
            this.scheme = scheme;
            this.raw = raw;
            this.params = params;
        }

        long getLongParam(String key, long def) {
            try {
                String v = params.get(key);
                if (v == null || v.isEmpty()) return def;
                return Long.parseLong(v);
            } catch (Exception e) {
                return def;
            }
        }
    }

    // ---------------- Scratchpad streams ----------------

    /**
     * InputStream that reads from a file starting at an offset.
     * Implemented using RandomAccessFile so index/seek is correct.
     */
    private static final class ScratchpadInputStream extends InputStream {
        private final RandomAccessFile raf;

        ScratchpadInputStream(File file, long pos) throws IOException {
            this.raf = new RandomAccessFile(file, "r");
            if (pos < 0) pos = 0;
            raf.seek(pos + SCRATCHPAD_HEADER_SIZE);
        }

        @Override
        public int read() throws IOException {
            return (raf.read());
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return (raf.read(b, off, len));
        }

        @Override
        public long skip(long n) throws IOException {
            if (n <= 0) return 0;
            long cur = raf.getFilePointer();
            long end = Math.min(cur + n, raf.length());
            raf.seek(end);
            return end - cur;
        }

        @Override
        public int available() throws IOException {
            long remaining = raf.length() - raf.getFilePointer();
            if (remaining <= 0) return 0;
            return (remaining > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) remaining;
        }

        @Override
        public void close() throws IOException {
            raf.close();
        }
    }

    /**
     * OutputStream that writes to a file starting at an offset.
     * Uses RandomAccessFile in "rw" mode.
     */
    private static final class ScratchpadOutputStream extends OutputStream {
        private final RandomAccessFile raf;

        ScratchpadOutputStream(File file, long pos) throws IOException {
            this.raf = new RandomAccessFile(file, "rw");
            if (pos < 0) pos = 0;
            raf.seek(pos + SCRATCHPAD_HEADER_SIZE);
        }

        @Override
        public void write(int b) throws IOException {
            raf.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            raf.write(b, off, len);
        }

        @Override
        public void close() throws IOException {
            raf.close();
        }
    }
}
