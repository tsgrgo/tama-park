package com.nttdocomo.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Font {
    //
    // |<- 4 ->|<- 4 ->|<----- 8 ----->|<----- 8 ----->|<----- 8 ----->|
    // +-------+-------+-------+-------+-------+-------+-------+-------+
    // | 0111  | face  |         style |         size  |      -        |
    // +-------+-------+---------------+---------------+-------+-------+
    // Or last is type and everything else is 0
    //
    // @formatter:off
    public static final int TYPE_DEFAULT =      0x00000000;
    public static final int TYPE_HEADING =      0x00000001;

    public static final int FACE_SYSTEM =       0x71000000; // 0111 0001 00000000 00000000 00000000
    public static final int FACE_MONOSPACE =    0x72000000; // 0111 0010 00000000 00000000 00000000
    public static final int FACE_PROPORTIONAL = 0x73000000; // 0111 0011 00000000 00000000 00000000

    public static final int STYLE_PLAIN =       0x70100000; // 0111 0000 00010000 00000000 00000000
    public static final int STYLE_BOLD =        0x70110000; // 0111 0000 00010001 00000000 00000000
    public static final int STYLE_ITALIC =      0x70120000; // 0111 0000 00010010 00000000 00000000
    public static final int STYLE_BOLDITALIC =  0x70130000; // 0111 0000 00010011 00000000 00000000

    public static final int SIZE_SMALL =        0x70000100; // 0111 0000 00000000 00000001 00000000
    public static final int SIZE_MEDIUM =       0x70000200; // 0111 0000 00000000 00000010 00000000
    public static final int SIZE_LARGE =        0x70000300; // 0111 0000 00000000 00000011 00000000
    public static final int SIZE_TINY =         0x70000400; // 0111 0000 00000000 00000100 00000000

    public static final int DEFAULT_FONT = FACE_SYSTEM | SIZE_TINY | STYLE_PLAIN;

    private static final int FACE_MASK =        0xFF000000;
    private static final int STYLE_MASK =       0xF0FF0000;
    private static final int SIZE_MASK =        0xF000FF00;
    // @formatter:on

    // Metrics helper
    private static final BufferedImage METRICS_IMG = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Graphics METRICS_G = METRICS_IMG.getGraphics();

    private static final java.awt.Font BASE_SYSTEM;
    private static final java.awt.Font BASE_MONO;
    private static final java.awt.Font BASE_PROP;

    static {
        BASE_SYSTEM = loadBundledFont("/fonts/msgothic.ttc");
        BASE_MONO = new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12);
        BASE_PROP = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12);
    }

    private static final Map<Integer, Font> CACHE = new ConcurrentHashMap<>();

    private final java.awt.Font font;
    private FontMetrics metrics;

    protected Font(java.awt.Font font) {
        this.font = font;
    }

    public static Font getFont(int spec) {
        return CACHE.computeIfAbsent(spec, Font::createFont);
    }

    public int getHeight() {
        return metrics().getAscent() + metrics().getDescent();
    }

    public int getDescent() {
        return metrics().getDescent();
    }

    public int stringWidth(String str) {
        if (str == null || str.isEmpty()) return 0;
        return metrics().stringWidth(str);
    }

    java.awt.Font unwrap() {
        return font;
    }

    private FontMetrics metrics() {
        if (metrics == null) {
            synchronized (this) {
                if (metrics == null) {
                    metrics = METRICS_G.getFontMetrics(font);
                }
            }
        }
        return metrics;
    }

    private static Font createFont(int spec) {
        // TYPE form: low 8 bits only
        if ((spec & 0xFFFFFF00) == 0) {
            int type = spec & 0xFF;
            return createTypeFont(type);
        }

        // FACE|STYLE|SIZE form
        int face = spec & FACE_MASK;
        int style = spec & STYLE_MASK;
        int size = spec & SIZE_MASK;

        java.awt.Font base = getBaseForFace(face);
        int awtStyle = mapStyle(style);
        int fontSize = mapSize(size);

        return new Font(base.deriveFont(awtStyle, fontSize));
    }

    private static Font createTypeFont(int type) {
        return switch (type) {
            case TYPE_HEADING -> new Font(new java.awt.Font(
                    "Dialog",
                    java.awt.Font.BOLD,
                    18
            ));
            default -> new Font(new java.awt.Font(
                    "Dialog",
                    java.awt.Font.PLAIN,
                    10
            ));
        };
    }

    private static java.awt.Font getBaseForFace(int face) {
        return switch (face) {
            case FACE_SYSTEM -> BASE_SYSTEM;
            case FACE_MONOSPACE -> BASE_MONO;
            case FACE_PROPORTIONAL -> BASE_PROP;
            default -> BASE_SYSTEM;
        };
    }

    private static int mapStyle(int style) {
        return switch (style) {
            case STYLE_PLAIN -> java.awt.Font.PLAIN;
            case STYLE_BOLD -> java.awt.Font.BOLD;
            case STYLE_ITALIC -> java.awt.Font.ITALIC;
            case STYLE_BOLDITALIC -> java.awt.Font.BOLD | java.awt.Font.ITALIC;
            default -> java.awt.Font.PLAIN;
        };
    }

    private static int mapSize(int size) {
        return switch (size) {
            case SIZE_TINY -> 12;
            case SIZE_SMALL -> 18;
            case SIZE_MEDIUM -> 24;
            case SIZE_LARGE -> 26;
            default -> 12;
        };
    }

    private static java.awt.Font loadBundledFont(String resourcePath) {
        try (InputStream in = Font.class.getResourceAsStream(resourcePath)) {
            if (in == null) throw new RuntimeException("Font resource not found: " + resourcePath);
            java.awt.Font base = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, in);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(base);
            return base;
        } catch (Exception e) {
            e.printStackTrace();
            return new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12);
        }
    }

}
