package com.nttdocomo.ui;

import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
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
    // @formatter:on

    public static final int DEFAULT_FONT = FACE_SYSTEM | SIZE_TINY | STYLE_PLAIN;

    // Metrics helper
    private static final BufferedImage METRICS_IMG =
            new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Graphics METRICS_G =
            METRICS_IMG.getGraphics();

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
        return metrics().getHeight();
    }

    public int getDescent() {
        return metrics().getDescent();
    }

    public int stringWidth(String str) {
        if (str == null || str.isEmpty()) return 0;
        return metrics().stringWidth(str);
    }

    public java.awt.Font unwrap() {
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
        int face = spec & 0x0F000000;
        int style = spec & 0x00FF0000;
        int size = spec & 0x0000FF00;

        String family = mapFace(face);
        int awtStyle = mapStyle(style);
        int fontSize = mapSize(size);

        return new Font(new java.awt.Font(family, awtStyle, fontSize));
    }

    private static Font createTypeFont(int type) {
        switch (type) {
            case TYPE_HEADING:
                return new Font(new java.awt.Font(
                        "Dialog",
                        java.awt.Font.BOLD,
                        18
                ));

            case TYPE_DEFAULT:
            default:
                return new Font(new java.awt.Font(
                        "Dialog",
                        java.awt.Font.PLAIN,
                        10
                ));
        }
    }

    private static String mapFace(int face) {
        switch (face) {
            case FACE_MONOSPACE:
                return "Monospaced";
            case FACE_PROPORTIONAL:
                return "SansSerif";
            case FACE_SYSTEM:
            default:
                return "Serif";
        }
    }

    private static int mapStyle(int style) {
        switch (style) {
            case STYLE_BOLD:
                return java.awt.Font.BOLD;
            case STYLE_ITALIC:
                return java.awt.Font.ITALIC;
            case STYLE_BOLDITALIC:
                return java.awt.Font.BOLD | java.awt.Font.ITALIC;
            case STYLE_PLAIN:
            default:
                return java.awt.Font.PLAIN;
        }
    }

    private static int mapSize(int size) {
        switch (size) {
            case SIZE_SMALL:
                return 18;
            case SIZE_MEDIUM:
                return 22;
            case SIZE_LARGE:
                return 25;
            case SIZE_TINY:
            default:
                return 15;
        }
    }

}
