import com.nttdocomo.device.IrRemoteControl;
import com.nttdocomo.device.IrRemoteControlFrame;
import com.nttdocomo.io.HttpConnection;
import com.nttdocomo.ui.AudioPresenter;
import com.nttdocomo.ui.Canvas;
import com.nttdocomo.ui.Display;
import com.nttdocomo.ui.Font;
import com.nttdocomo.ui.Graphics;
import com.nttdocomo.ui.IApplication;
import com.nttdocomo.ui.Image;
import com.nttdocomo.ui.MediaImage;
import com.nttdocomo.ui.MediaListener;
import com.nttdocomo.ui.MediaManager;
import com.nttdocomo.ui.MediaPresenter;
import com.nttdocomo.ui.MediaSound;
import com.nttdocomo.ui.PhoneSystem;
import com.nttdocomo.util.Timer;
import com.nttdocomo.util.TimerListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;
import javax.microedition.io.Connector;

enum TextAlign {
    LEFT,
    RIGHT,
    CENTER
}

public class GameApp extends IApplication implements TimerListener, MediaListener {
    public static boolean aL;
    public static int Code; // 0: Idle, 2: Request pending, 3: Currently executing
    public static boolean running;
    public static boolean Z;
    public static int fps;
    public static boolean Exceptions;
    public static boolean I;
    public static boolean StackMap;
    public static int aJ;
    public static Timer timer;
    public static GameApp mediaListener;
    public static Canvas canvas;
    public static int canvasWidth;
    public static int canvasHeight;
    public static int rootX;
    public static int rootY;
    public static Font currentFont;
    public static int aM;
    public static int currentFontHeight;
    public static int d;
    public static int E;
    public static boolean k;
    public static long aN;
    public static int O;
    public static int aO;
    public static boolean T;
    public static boolean ai;
    public static long totalMemory;
    public static long[] aQ;
    public static int aR;
    public static AudioPresenter[] audioPresenters;
    public static MediaSound[] mediaSounds;
    public static int loopedSoundIdx;
    public static int aS;
    public static int aT;
    public static int p;
    public static boolean r;
    public static boolean q;
    public static long[] inputState;
    public static int t;
    public static int u;
    public static int[] v;
    public static int[] gameSave; // [?, resDownloaded, ?, isSoundEnabled, ? ..]
    public static int currentFontIdx;
    public static int rngState;
    public static short[] aU;
    public static int[] aV;
    public static boolean[] aW;
    public static int[] aX;
    public static int previousSoftLabelIdx;
    public static int currentSoftLabelIdx;
    public static String[] softLabels;
    public static int w;
    public static Image[] images;
    public static int[] imageSizes;
    public static int[] G;
    public static int[] H;
    public static int[] aY;
    public static int[] K;
    public static int[] L;
    public static int[] M;
    public static int[] N;
    public static int[] P;
    public static int[] R;
    public static int[] Q;
    public static int[] S;
    public static int[] U;
    public static int[] Y;
    public static Image[] downloadedImages2;
    public static String downloadedString;
    public static String currentDownloadedQuote;
    public static int[] ab;
    public static int[] ae;
    public static Image[] gotchiKingImages;
    public static int[] aa;
    public static int[] af;
    public static int[] aj;
    public static String[] ah;
    public static Image travelMemoryPhoto;
    public static int[] al;
    public static int[] ao;
    public static Image[] downloadedImages;
    public static String[] downloadedTexts;
    public static int[] ak;
    public static int[] explanationState; // [offset, size, current, isOpen]
    public static int[] aq;
    public static int[] ar;
    public static String as;
    public static long aB;
    public static long ad;
    public static boolean au;
    public static int at;
    public static String[] texts;
    public static int[] aw; // [selectedButtonIdx, ...]
    public static int[] digitEditorState;
    public static int[] digitShuffleTable;
    public static int[] az;
    public static int[] aZ;
    public static byte[] ba;
    public static byte[] bb;
    public static byte[] digitsSentToServer;
    public static String bc;
    public static String bd;
    public static byte[] aE;
    public static IrRemoteControlFrame[] irFrames;
    public static IrRemoteControl irRemoteControl;
    public static int[] aC; // [transmissionState, ...]
    public static long aG;
    public static int[] aI;
    public static int[] aH;
    public boolean c;

    static {
        new Object();
        running = true;
        Z = false;
        Exceptions = false;
        I = false;
        aJ = 8;
        d = 0;
        E = 0;
        k = false;
        O = 0;
        aO = 16;
        totalMemory = Runtime.getRuntime().totalMemory();
        aQ = new long[240];
        aR = 0;
        audioPresenters = new AudioPresenter[2];
        loopedSoundIdx = -1;
        aS = 0;
        aT = -1;
        p = -1;
        inputState = new long[7];
        t = 0;
        u = 0;
        v = new int[2];
        gameSave = new int[7];
        rngState = 0;
        aU = new short[]{0, 4, 8, 13, 17, 22, 26, 31, 35, 40, 44, 48, 53, 57, 61, 66, 70, 74, 79, 83, 87, 91, 95, 100, 104, 108, 112, 116, 120, 124, 127, 131, 135, 139, 143, 146, 150, 154, 157, 161, 164, 167, 171, 174, 177, 181, 184, 187, 190, 193, 196, 198, 201, 204, 207, 209, 212, 214, 217, 219, 221, 223, 226, 228, 230, 232, 233, 235, 237, 238, 240, 242, 243, 244, 246, 247, 248, 249, 250, 251, 252, 252, 253, 254, 254, 255, 255, 255, 255, 255, 256};
        aV = new int[]{0, 17, 34, 52, 69, 87, 105, 122, 140, 158, 176, 194, 212, 230, 249, 267, 286, 305, 324, 344, 363, 383, 404, 424, 445, 466, 487, 509, 531, 554, 577, 600, 624, 649, 674, 700, 726, 753, 781, 809, 839, 869, 900, 932, 965, 999, 1035, 1072, 1110, 1150, 1191, 1234, 1279, 1327, 1376, 1428, 1482, 1539, 1600, 1664, 1732, 1804, 1880, 1962, 2050, 2144, 2246, 2355, 2475, 2605, 2747, 2904, 3077, 3270, 3487, 3732, 4010, 4331, 4704, 5144, 5671, 6313, 7115, 8144, 9514, 11430, 14300, 19081, 28636, 57289};
        aW = new boolean[]{false, false};
        aX = new int[2];
        String[] var10000 = new String[2];
        previousSoftLabelIdx = 6;
        currentSoftLabelIdx = 6;
        softLabels = new String[]{"Start", "Menu", "Close", "Back", "Title", "Help", ""};
        images = new Image[93];
        G = new int[5];
        H = new int[]{120, 176, 186, 26, 26, 25, 2, 120, 208, 186, 26, 16, 15, 2};
        aY = new int[]{120, 86, 120, 34, 53, 2, 120, 140, 108, 34, 9, 2};
        K = new int[3];
        L = new int[]{120, 132, 230, 26, 20, 19, 2, 120, 168, 230, 26, 18, 17, 2, 120, 204, 230, 26, 11, 10, 2};
        M = new int[3];
        N = new int[]{120, 146, 220, 26, 22, 21, 2, 120, 186, 220, 26, 14, 13, 2};
        P = new int[4];
        R = new int[]{120, 202, 220, 24, 14, 2, 120, 168, 220, 24, 13, 2, 120, 134, 220, 24, 12, 2, 120, 100, 220, 24, 11, 2, 120, 66, 220, 24, 10, 2};
        Q = new int[]{11025351, 1648446, 11025351, 1648446, 7053048, 1648446, 7053048, 1648446, 10873427, 2323575, 10873427, 2323575, 16777041, 16734720, 16777041, 16734720, 16021161, 16777215, 16021161, 16777215};
        S = new int[]{120, 144, 190, 28, 93, 2, 120, 176, 190, 28, 16, 2, 120, 208, 190, 28, 15, 2};
        U = new int[7];
        Y = new int[]{120, 165, 170, 28, 93, 2, 120, 198, 170, 28, 15, 2};
        downloadedImages2 = new Image[2];
        ab = new int[6];
        ae = new int[]{120, 165, 170, 28, 93, 2, 120, 198, 170, 28, 15, 2};
        gotchiKingImages = new Image[2];
        aa = new int[]{58, 59, 60, 71, 89, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 72, 89};
        af = new int[4];
        aj = new int[]{120, 165, 170, 28, 15, 2, 120, 198, 170, 28, 35, 2};
        ah = new String[2];
        al = new int[5];
        ao = new int[]{120, 165, 170, 28, 93, 2, 120, 198, 170, 28, 15, 2};
        downloadedImages = new Image[2];
        downloadedTexts = new String[3];
        ak = new int[]{0, 0, 1, 6, 6, 1, 80, 41, 0, 28, 2, 0, 0, 2, 81, 43, 0, 64, 3, 1, 1, 3, 82, 45, -44, 44, 4, 2, 2, 4, 83, 47, -64, 60, 5, 3, 3, 5, 84, 49, -108, 60, 6, 4, 4, 6, 85, 51, -164, 60, 0, 5, 5, 0, 86, 53};
        explanationState = new int[4];
        aq = new int[9];
        ar = new int[4];
        texts = new String[183];
        aw = new int[14];
        digitEditorState = new int[6];
        digitShuffleTable = new int[]{3, 8, 4, 0, 1, 5, 6, 7, 2, 9, 3, 8, 4, 0, 1, 5, 6, 7, 2, 9, 4, 8, 1, 6, 2, 3, 9, 5, 0, 7, 4, 8, 1, 6, 2, 3, 9, 5, 0, 7, 5, 8, 0, 6, 2, 7, 3, 9, 1, 4, 5, 8, 0, 6, 2, 7, 3, 9, 1, 4, 5, 8, 7, 1, 6, 3, 0, 2, 9, 4, 5, 8, 7, 1, 6, 3, 0, 2, 9, 4, 6, 8, 5, 3, 0, 9, 7, 2, 4, 1, 6, 8, 5, 3, 0, 9, 7, 2, 4, 1};
        az = new int[]{15947864, 16777041, 10873427, 7053048, 16777215, 11025351, 16021161};
        aZ = new int[1];
        ba = new byte[1];
        bb = new byte[]{65, 80, 68, 65, 84, 65};
        digitsSentToServer = new byte[14];
        bc = "";
        bd = "";
        aE = new byte[16];
        irFrames = createIrRemoteControlFrames(1);
        irRemoteControl = IrRemoteControl.getIrRemoteControl();
        aC = new int[6];
        aI = new int[]{120, 144, 170, 28, 15, 2, 120, 176, 170, 28, 94, 2, 120, 208, 170, 28, 95, 2};
        aH = new int[]{120, 144, 170, 28, 16, 2, 120, 176, 170, 28, 94, 2, 120, 208, 170, 28, 95, 2};
    }

    public GameApp() {
        mediaListener = this;
        String[] args = this.getArgs();
        this.initCanvas();

        try {
            PhoneSystem.setAttribute(0, 1);
        } catch (Exception e) {
        }

        setCurrentFont(2);
        fps = 8;
        timer = new Timer();
        timer.setRepeat(true);
        timer.setTime(1000 / fps);
        timer.setListener(this);
        timer.start();
    }

    public static void refresh() {
        Code = 2;
        canvas.repaint();
    }

    public static void startTimerWithRetry() {
        for (int i = 0; i < 10; ++i) {
            try {
                timer.start();
                return;
            } catch (Exception e) {
                try {
                    timer.stop();
                } catch (Exception e2) {
                }

                try {
                    Thread.sleep(1000L);
                } catch (Exception e2) {
                }
            }
        }
    }

    public static void log(String str) {
        System.out.println(str);
    }

    public static String byteToHex(int b) {
        String res = "";
        if (b < 0) {
            b += 256;
        }

        for (int i = 0; i < 2; ++i) {
            res = hexDigitToString(b % 16) + res;
            b /= 16;
        }

        return res;
    }

    public static String hexDigitToString(int digit) {
        String res = "";
        if (digit < 10) {
            res = "" + digit;
        } else {
            switch (digit) {
                case 10:
                    res = "A";
                    break;
                case 11:
                    res = "B";
                    break;
                case 12:
                    res = "C";
                    break;
                case 13:
                    res = "D";
                    break;
                case 14:
                    res = "E";
                    break;
                case 15:
                    res = "F";
            }
        }

        return res;
    }

    public static void initAudioPresenters() {
        audioPresenters[0] = AudioPresenter.getAudioPresenter(0);
        audioPresenters[0].setAttribute(133, 0);
        audioPresenters[0].setMediaListener(mediaListener);
        audioPresenters[1] = AudioPresenter.getAudioPresenter(1);
        audioPresenters[1].setAttribute(133, 1);
    }

    public static void playSoundInternal(int soundIdx, int presenterIdx, boolean var2) {
        stopSound(presenterIdx);

        try {
            if (gameSave[3] == 0) {
                audioPresenters[presenterIdx].setSound(mediaSounds[soundIdx]);
                if (var2) {
                    // Not sure why this is empty...
                }

                audioPresenters[presenterIdx].play();
            }
        } catch (Exception ex) {
            log("playsound:" + soundIdx + " " + ex);
        }

    }

    public static void playSound(int soundIdx, boolean var1) {
        playSoundInternal(soundIdx, var1 ? 0 : 1, var1);
    }

    public static void stopSound(int presenterIdx) {
        try {
            Thread.sleep(100L);
            audioPresenters[presenterIdx].stop();
        } catch (Exception var2) {
        }

    }

    public static void stopAllSounds() {
        for (int i = 0; i < audioPresenters.length; ++i) {
            stopSound(i);
        }

    }

    public static void StackMap(int soundIdx, boolean var1) {
        if (gameSave[3] == 0 && soundIdx >= 0) {
            stopSound(0);
            playSoundInternal(soundIdx, 0, var1);
        }

        loopedSoundIdx = soundIdx;
        p = soundIdx;
        q = var1;
    }

    public static void o() {
        if (r) {
            n();
            r = false;
        }

    }

    public static void Z() {
        r = true;
    }

    public static void toggleSound() {
        gameSave[3] = 1 - gameSave[3];
        stopAllSounds();
        n();
    }

    public static void n() {
        if (q) {
            StackMap(p, q);
        }

    }

    public static boolean loadSounds() {
        DataInputStream stream = null;
        byte var2 = 0;

        boolean success;
        try {
            mediaSounds = new MediaSound[7];
            int[] var3 = loadShortArray(128);
            int var4 = 0;

            for (int i = 0; i < 93; ++i) {
                var4 += var3[i];
            }

            stream = Connector.openDataInputStream("scratchpad:///0;pos=" + (var4 + 128 + 568));

            for (int i = 0; i < 7; ++i) {
                byte[] data = new byte[var3[i + 93]];
                stream.read(data);

                for (int var6 = 0; var6 < data.length; ++var6) {
                }

                mediaSounds[i] = MediaManager.getSound(data);
                mediaSounds[i].use();
                log("loadsound:" + i);
                Object var19 = null;
                System.gc();
            }

            initAudioPresenters();
            success = true;
        } catch (Exception var16) {
            log("loadsounderr i:" + var2);
            success = false;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }

        }

        return success;
    }

    public static boolean isKeyPressed(long key) {
        return (inputState[1] & key) != 0L;
    }

    public static boolean isKeyDown(long key) {
        return (inputState[2] & key) != 0L;
    }

    public static void Code(int type, int param) {
        try {
            if (0 == type) {
                inputState[0] = (long) (canvas.getKeypadState() & Integer.MAX_VALUE);
                inputState[4]++;
            }
        } catch (Exception e) {
        }

    }

    public static void updateInputState() {
        long var0 = 0L;
        long var2 = 0L;
        long var4 = 0L;
        var2 = 0L;
        if (inputState[4] == 0L) {
            inputState[0] = 0L;
        }

        inputState[3] = inputState[6];
        inputState[6] = inputState[0] | var0 | var2 << 32;
        inputState[5] = inputState[6] & (inputState[6] ^ inputState[3]);
        long[] var10000;
        if (k) {
            if (inputState[4] != 0L) {
                var10000 = inputState;
                var10000[5] |= var2 << 32 & 844424930131968L;
            }
        } else if (inputState[4] != 0L) {
            var10000 = inputState;
            var10000[5] |= var0 & 655360L;
        }

        if ((inputState[3] ^ inputState[6]) == 0L && inputState[6] != 0L) {
            ++t;
        } else {
            t = 0;
        }

        inputState[4] = 0L;
        if ((inputState[6] & 9851624207876096L) == 0L) {
            ++u;
        } else {
            u = 0;
        }

        inputState[2] = inputState[6];
        inputState[1] = inputState[5];
    }

    public static void u() {
        try {
            v[0] = 0;
            v[1] = 0;
            PhoneSystem.setAttribute(1, 0);
        } catch (Exception e) {
        }

    }

    public static void a() {
        if (v[0] > 0) {
            if (gameSave[5] == 0 && v[1] == 0) {
                try {
                    v[1] = 1;
                    PhoneSystem.setAttribute(1, 1);
                } catch (Exception e) {
                }
            }

            if (--v[0] <= 0) {
                u();
            }
        }

    }

    public static void loadGameSave() {
        try {
            DataInputStream inputStream = Connector.openDataInputStream("scratchpad:///0;pos=0");

            for (int i = 0; i < gameSave.length; ++i) {
                gameSave[i] = inputStream.readInt();
            }

            inputStream.close();
            inputStream = null;
            System.gc();
        } catch (Exception e) {
        }

    }

    public static void saveGame() {
        try {
            DataOutputStream outputStream = Connector.openDataOutputStream("scratchpad:///0;pos=0");

            for (int i = 0; i < gameSave.length; ++i) {
                outputStream.writeInt(gameSave[i]);
            }

            outputStream.close();
            outputStream = null;
            System.gc();
        } catch (Exception e) {
        }

    }

    public static void downloadGameData(String path, int var1, int pos) throws Exception {
        byte[] buffer = new byte[10240];
        refresh();
        pos += gameSave[1] * 10240;

        for (int i = gameSave[1]; i < (var1 - 1) / 10240 + 1; ++i) {
            HttpConnection httpConnection = (HttpConnection) Connector.open(mediaListener.getSourceURL() + path + i + ".bin", 1, true);
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();
            System.gc();
            DataInputStream inputStream = new DataInputStream(httpConnection.openInputStream());
            int length = (int) httpConnection.getLength();
            int bytesRead = inputStream.read(buffer, 0, length);

            inputStream.close();
            httpConnection.close();

            if (bytesRead != length) {
                throw new Exception("http load error!");
            }

            DataOutputStream outputStream = Connector.openDataOutputStream("scratchpad:///0;pos=" + pos);
            outputStream.write(buffer, 0, length);
            outputStream.close();

            pos += length;
            gameSave[1]++;
            saveGame();
            ++w;
            refresh();
        }

        buffer = null;
        System.gc();
    }

    public static int[] loadShortArray(int pos) throws Exception {
        return loadArray(pos, 2);
    }

    public static int[] loadArray(int pos, int elementSizeInBytes) throws Exception {
        DataInputStream stream = new DataInputStream(Connector.openInputStream("scratchpad:///0;pos=" + pos));
        short arraySize = stream.readShort();
        int[] res = new int[arraySize];

        if (elementSizeInBytes == 2) {
            for (int i = 0; i < arraySize; ++i) {
                res[i] = stream.readShort();
            }
        } else if (elementSizeInBytes == 4) {
            for (int i = 0; i < arraySize; ++i) {
                res[i] = stream.readInt();
            }
        }

        stream.close();
        return res;
    }

    public static void drawSprite(Graphics g, int idx, int x, int y, int anchor) {
        drawImage(g, images[idx], x, y, anchor);
    }

    public static void drawImage(Graphics g, Image img, int x, int y, int anchor) {
        if (anchor == 2) {
            x -= img.getWidth() / 2;
        } else if (anchor == 1) {
            x -= img.getWidth();
        } else if (anchor == 3) {
            x -= img.getWidth() / 2;
            y -= img.getHeight() / 2;
        }

        g.drawImage(img, x, y);
    }

    public static int getSpriteWidth(int idx) {
        return images[idx].getWidth();
    }

    public static int getSpriteHeight(int idx) {
        return images[idx].getHeight();
    }

    public static void setColorOfRGB(Graphics graphics, int r, int g, int b) {
        graphics.setColor(Graphics.getColorOfRGB(r, g, b));
    }

    public static void setColorOfRGBInt(Graphics g, int rgb) {
        g.setColor(Graphics.getColorOfRGB(rgb >> 16 & 255, rgb >> 8 & 255, rgb & 255));
    }

    public static void drawString(Graphics g, String str, int x, int y, TextAlign align) {
        drawMultilineString(g, str, x, y, currentFont.getHeight() + 1, align);
    }

    public static void drawMultilineString(Graphics g, String str, int x, int y, int lineHeight, TextAlign align) {
        boolean var6 = false;
        int fromIndex = 0;
        boolean hasNewLine = true;

        for (y += currentFont.getHeight(); hasNewLine; y += lineHeight) {
            int toIndex = str.indexOf("\n", fromIndex);
            if (toIndex == -1) {
                toIndex = str.length();
                hasNewLine = false;
            }

            int newX = x;
            if (align == TextAlign.CENTER) {
                newX = x - currentFont.stringWidth(str.substring(fromIndex, toIndex)) / 2;
            } else if (align == TextAlign.RIGHT) {
                newX = x - currentFont.stringWidth(str.substring(fromIndex, toIndex));
            }

            g.drawString(str.substring(fromIndex, toIndex), newX, y - currentFont.getDescent());
            fromIndex = toIndex + 1;
        }

    }

    public static void drawBeveledRect(Graphics g, int x, int y, int width, int height, int borderColor, int innerColor) {
        if (width >= 2 && height >= 2) {
            setColorOfRGBInt(g, borderColor);
            g.fillRect(x + 1, y, width - 2, 2);
            g.fillRect(x + 1, y + height - 2, width - 2, 2);
            g.fillRect(x, y + 1, 2, height - 2);
            g.fillRect(x + width - 2, y + 1, 2, height - 2);
            if (width >= 4 && height >= 4) {
                setColorOfRGBInt(g, innerColor);
                g.fillRect(x + 1, y + 2, 1, height - 4);
                g.fillRect(x + width - 2, y + 2, 1, height - 4);
                g.fillRect(x + 2, y + 1, width - 4, height - 2);
            }
        }
    }

    public static void drawShadedRect(Graphics g, int x, int y, int width, int height, int color1, int color2, int color3) {
        int[] colors = new int[]{color1, color2, color1, color3};

        for (int i = 0; i < 4; ++i) {
            g.setColor(colors[i]);
            g.fillRect(x + i, y + i, width - i * 2, height - i * 2);
        }

        Object var10 = null;
    }

    public static void setCurrentFont(int i) {
        switch (i) {
            case 0:                                           //      face style    size     no type
                currentFont = Font.getFont(1895826432); // 0111 0001 00000000 00000100 00000000
                break;
            case 1:
                currentFont = Font.getFont(1895825664); // 0111 0001 00000000 00000001 00000000
                break;
            case 2:
                currentFont = Font.getFont(1895825920); // 0111 0001 00000000 00000010 00000000
                break;
            case 3:
                currentFont = Font.getFont(1896940032); // 0111 0001 00010001 00000010 00000000
        }

        currentFontHeight = currentFont.getHeight();
        currentFontIdx = i;
    }

    public static int rand(int max) {
        rngState = rngState * 1103515245 + 12345;
        rngState &= 32767;
        return rngState * max / '耀';
    }

    public static int abs(int n) {
        if (n < 0) {
            n *= -1;
        }

        return n;
    }

    public static int splitCount(String text, String delimiter) {
        int startIndex = 0;
        boolean hasMoreDelimiters = true;

        int count;
        int delimeterIndex;
        for (count = 0; hasMoreDelimiters; startIndex = delimeterIndex + delimiter.length()) {
            delimeterIndex = text.indexOf(delimiter, startIndex);
            if (delimeterIndex == -1) {
                delimeterIndex = text.length();
                hasMoreDelimiters = false;
            }

            ++count;
        }

        return count;
    }

    public static String substringBetweenDelimiters(String str, int startDelimiterCount, int endDelimiterCount, String delimiter) {
        int currentIndex = 0;

        for (int i = 0; i < startDelimiterCount; ++i) {
            currentIndex = str.indexOf(delimiter, currentIndex);
            if (currentIndex == -1) {
                log("subStringLine:Invalid line selection");
                return "";
            }

            currentIndex += delimiter.length();
        }

        int startIndex = currentIndex;

        for (int i = 0; i < endDelimiterCount; ++i) {
            currentIndex = str.indexOf(delimiter, currentIndex);
            if (currentIndex == -1) {
                return str.substring(startIndex);
            }

            currentIndex += delimiter.length();
        }

        if (0 < endDelimiterCount) {
            currentIndex -= delimiter.length();
        }

        return str.substring(startIndex, currentIndex);
    }

    public static boolean launchCurrentApp(String arg) {
        boolean success = true;

        try {
            String[] args = new String[]{arg};
            IApplication.getCurrentApp().launch(1, args);
        } catch (Exception e) {
            success = false;
        }

        return success;
    }

    public static void setSoftLabel(int which, String str) {
        if (which == 0) {
            canvas.setSoftLabel(0, str);
        } else if (which == 1) {
            canvas.setSoftLabel(1, str);
        }

    }

    public static void selectSoftLabel(int idx) {
        if (previousSoftLabelIdx != idx) {
            try {
                currentSoftLabelIdx = previousSoftLabelIdx;
                setSoftLabel(0, softLabels[idx]);
                previousSoftLabelIdx = idx;
            } catch (Exception e) {
            }

        }
    }

    public static boolean downloadGameDataIfNeeded() {
        try {
            if (E != gameSave[2]) {
                gameSave[1] = 0;
                gameSave[2] = E;
                saveGame();
            }

            if (gameSave[1] != 255) {
                w = gameSave[1];
                downloadGameData("", 72483, 128);
                gameSave[1] = 255;
                saveGame();
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void checkGameData() {
        if (downloadGameDataIfNeeded()) {
            T(3);
        } else {
            T(-2);
        }

    }

    public static void loadResources() {
        int result = loadImages(128, 0, 93);
        if (result == -1) {
            T(-1);
        } else if (loadSounds() && loadTexts()) {
            T(4);
        } else {
            T(-1);
        }
    }

    public static int loadImages(int pos, int startIndex, int count) {
//        try {
//            Thread.sleep(200L);
//        } catch (Exception e) {
//        }

        try {
            int[] sizes = loadShortArray(128);
            imageSizes = sizes;
            pos += (sizes.length + 1) * 2;

            for (int i = 0; i < startIndex; ++i) {
                pos += sizes[i];
            }

            DataInputStream stream = new DataInputStream(Connector.openInputStream("scratchpad:///0;pos=" + pos));

            for (int i = startIndex; i < count; ++i) {
                byte[] imageData = new byte[sizes[i]];
                stream.read(imageData);
                MediaImage mediaImage = MediaManager.getImage(imageData);
                mediaImage.use();
                images[i] = mediaImage.getImage();
                ++w;
                pos += sizes[i];
                refresh();

//                try {
//                    Thread.sleep(50L);
//                } catch (Exception e) {
//                }
            }

            stream.close();
            System.gc();
            return pos;
        } catch (Exception e) {
            return -1;
        }
    }

    public static void loadImage(int idx) throws Exception {
        if (images[idx] == null) {
            short baseOffset = 128;
            int pos = baseOffset + (imageSizes.length + 1) * 2;

            for (int i = 0; i < idx; ++i) {
                pos += imageSizes[i];
            }

            MediaImage mediaImage = MediaManager.getImage("scratchpad:///0;pos=" + pos);
            mediaImage.use();
            images[idx] = mediaImage.getImage();
        }

    }

    public static void disposeImage(int idx) {
        if (images[idx] != null) {
            images[idx].dispose();
            images[idx] = null;
        }

    }

    public static void downloadingPage(Graphics g, int x, int y) {
        int var3 = (GameApp.canvasWidth - 200) / 2;
        int var4 = y + 168;
        setColorOfRGBInt(g, 16777215);
        g.fillRect(x, y, 240, 240);
        setColorOfRGBInt(g, 16763955);
        g.drawRect(var3, var4, 200, 40);
        drawString(g, "Downloading", GameApp.canvasWidth / 2, var4 - currentFontHeight - 4, TextAlign.CENTER);
        int var5 = 200 * w / 8;
        g.fillRect(var3, var4, var5, 40);
    }

    public static void ad(Graphics g, int x, int y) {
        if (3 < w) {
            try {
                Thread.sleep(300L);
            } catch (Exception e) {
            }

            ac(g, x, y, w * 8 / 93, w);
        }

    }

    public static void exitGameOnSelect() {
        if (isKeyPressed(1048576L)) {
            running = false;
        }

    }

    public static void showError(Graphics g, String str, int x, int y) {
        setColorOfRGBInt(g, 0);
        g.fillRect(x, y, 240, 240);
        setColorOfRGBInt(g, 16777215);
        drawString(g, str, GameApp.canvasWidth / 2, y + 30, TextAlign.CENTER);
        drawString(g, "An error has occured", GameApp.canvasWidth / 2, y + 31 + currentFontHeight, TextAlign.CENTER);
        drawString(g, "Confirm:Exit", GameApp.canvasWidth / 2, y + 240 - 10 - currentFontHeight, TextAlign.CENTER);
    }

    public static void ak() {
        ag(2, true);
        setSelectedButtonIdx(0);
        ai(15947864, 15947864, 16353930, 16777215, 12138328, 16777215, 16777215, 16353930, 16777215, 13816530);
        StackMap(1, true);
        G[2] = 0;
        G[3] = 0;
        G[4] = 0;
        aj(0);
    }

    public static void aj(int var0) {
        switch (var0) {
            case 0:
            case 1:
            case 2:
            default:
                StackMap = true;
                G[2] = 0;
                G[1] = var0;
        }
    }

    public static void an() {
        int var10002 = G[2]++;
        switch (G[1]) {
            case 0:
                aj(1);
                return;
            case 1:
                al();
                break;
            case 2:
                am();
        }

    }

    public static void al() {
        int var0 = -48 + G[2] * 8;
        if (isKeyPressed(1048576L) || 0 <= var0) {
            aj(2);
        }

    }

    public static void am() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(100, 5);
        } else {
            int[] var10000;
            if (G[3] <= 32) {
                var10000 = G;
                var10000[4] += 2;
            } else {
                var10000 = G;
                var10000[4] -= 2;
            }

            var10000 = G;
            var10000[3] += G[4];
            switch (aq(isKeyPressed(1048576L), isKeyPressed(131072L), isKeyPressed(524288L))) {
                case 0:
                    T(5);
                    return;
                case 1:
                    T(6);
                    return;
                default:
                    G[0] = getSelectedButtonIdx();
            }
        }
    }

    public static void au(Graphics var0, int var1, int var2) {
        switch (G[1]) {
            case 1:
                as(var0, var1, var2);
                break;
            case 2:
                at(var0, var1, var2);
        }

    }

    public static void as(Graphics g, int x, int y) {
        int var3 = (canvasWidth - getSpriteWidth(72)) / 2;
        int var4 = y + 54;
        int var5 = -48 + G[2] * 8;
        setColorOfRGBInt(g, 6728679);
        g.fillRect(x, y, 240, 240);
        drawSprite(g, 23, x, y + var5, 0);
        drawSprite(g, 6, x, y + 116, 0);
        drawSprite(g, 6, x + 120, y + 116, 0);
        setColorOfRGBInt(g, 7456538);
        g.fillRect(x, y + 158, 240, 82);
        drawSprite(g, 72, var3, var4, 0);
        drawSprite(g, 73 + (G[2] >> 4 & 1), var3 + 23, var4 + 66, 0);
    }

    public static void at(Graphics var0, int var1, int var2) {
        int var3 = (canvasWidth - getSpriteWidth(72)) / 2;
        int var4 = var2 + 54;
        int var5;
        if (I) {
            setColorOfRGBInt(var0, 6728679);
            var0.fillRect(var1, var2, 240, 240);
            drawSprite(var0, 23, var1, var2, 0);
            av(var0, 6, G[3], var1, var2 + 116, 240);
            setColorOfRGBInt(var0, 7456538);
            var0.fillRect(var1, var2 + 158, 240, 82);
            drawSprite(var0, 9, canvasWidth / 2, var2 + 158, 2);
            drawSprite(var0, 72, var3, var4, 0);
            drawSprite(var0, 73 + (G[2] >> 4 & 1), var3 + 23, var4 + 66, 0);

            for (var5 = 0; var5 < 2; ++var5) {
                aw(var0, var5, H, 0);
            }
        } else {
            setColorOfRGBInt(var0, 7456538);
            var0.fillRect(var1, var2 + 158 + getSpriteHeight(9), 240, 240 - (158 + getSpriteHeight(9)));
            setColorOfRGBInt(var0, 6728679);
            var0.fillRect(var1, var2 + 116, 240, getSpriteHeight(6));
            av(var0, 6, G[3], var1, var2 + 116, 240);
            drawSprite(var0, 72, var3, var4, 0);
            drawSprite(var0, 73 + (G[2] >> 4 & 1), var3 + 23, var4 + 66, 0);

            for (var5 = 0; var5 < 2; ++var5) {
                aw(var0, var5, H, 0);
            }
        }

        var5 = ax(H, getSelectedButtonIdx(), 1);
        var5 += ax(H, getSelectedButtonIdx(), 3) / 2;
        var5 -= 10;
        drawSprite(var0, 75, var1 + 2, var2 + var5, 0);
        drawSprite(var0, 75, var1 + 240 - (getSpriteWidth(75) - 10), var2 + var5, 0);
    }

    public static void av(Graphics var0, int var1, int var2, int var3, int var4, int var5) {
        while (0 < var2) {
            var2 -= getSpriteWidth(var1);
        }

        while (var2 < var5) {
            drawSprite(var0, var1, var3 + var2, var4, 0);
            var2 += getSpriteWidth(var1);
        }

    }

    public static void az() {
        ag(3, true);
        setSelectedButtonIdx(0);
        ai(15947864, 15947864, 16353930, 16777215, 12138328, 16777215, 16777215, 16353930, 16777215, 13816530);
        StackMap(2, true);
        ay(0);
    }

    public static void ay(int var0) {
        K[2] = 0;
        K[1] = var0;
    }

    public static void aB() {
        int var10002 = K[2]++;
        switch (K[1]) {
            case 0:
                ay(1);
                break;
            case 1:
                aA();
        }

    }

    public static void aA() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(105, 3);
        } else {
            switch (aq(isKeyPressed(1048576L), isKeyPressed(131072L), isKeyPressed(524288L))) {
                case 0:
                    T(7);
                    return;
                case 1:
                    T(8);
                    return;
                case 2:
                    T(9);
                    return;
                default:
                    K[0] = getSelectedButtonIdx();
            }
        }
    }

    public static void aF(Graphics g, int x, int y) {
        int var3 = (currentFontHeight + 1) * 2 + currentFontHeight;
        short var4 = 184;
        int var5;
        if (I) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            setColorOfRGBInt(g, 6728679);
            g.fillRect(x, y + 78, 240, getSpriteHeight(6));
            drawSprite(g, 6, x, y + 78, 0);
            drawSprite(g, 6, x + 120, y + 78, 0);
            drawBeveledRect(g, x + 3, y + 3, var4, var3 + 4, 0, 16056665);
            setColorOfRGBInt(g, 16777215);
            // 29: Connect to Tama Planet by phone!
            drawString(g, getText(29), x + 3 + 2, y + 3 + 2, TextAlign.LEFT);

            for (var5 = 0; var5 < 3; ++var5) {
                aw(g, var5, L, 0);
            }
        } else {
            for (var5 = 0; var5 < 3; ++var5) {
                aw(g, var5, L, 0);
            }
        }

        var5 = ax(L, getSelectedButtonIdx(), 2) / 2;
        var5 -= 10;
        int var6 = ax(L, getSelectedButtonIdx(), 1) + y;
        var6 += ax(L, getSelectedButtonIdx(), 3) / 2;
        aD(g, 64, canvasWidth / 2, var6, (var5 - getSpriteWidth(64)) * 2, K[2]);
        if (I) {
            drawSprite(g, 90, x + 3 + var4 - 1, y + 3 + var3 / 2 - 5, 0);
            drawSprite(g, 89, x + 240 + 2 - getSpriteWidth(89), y + 54, 0);
            clearOutsideGameArea(g);
        }

    }

    public static void aH() {
        ag(2, true);
        setSelectedButtonIdx(0);
        ai(15947864, 15947864, 16353930, 16777215, 12138328, 16777215, 16777215, 16353930, 16777215, 13816530);
        StackMap(0, true);
        aG(0);
    }

    public static void aG(int var0) {
        M[1] = var0;
        M[2] = 0;
    }

    public static void aJ() {
        int var10002 = M[2]++;
        switch (M[1]) {
            case 0:
                aG(1);
                break;
            case 1:
                aI();
        }

    }

    public static void aI() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(108, 2);
        } else {
            switch (aq(isKeyPressed(1048576L), isKeyPressed(131072L), isKeyPressed(524288L))) {
                case 0:
                    T(10);
                    return;
                case 1:
                    T(11);
                    return;
                default:
                    M[0] = getSelectedButtonIdx();
            }
        }
    }

    public static void travelModePage(Graphics g, int x, int y) {
        int var3 = (currentFontHeight + 1) * 2 + currentFontHeight;
        short var4 = 192;
        int var5;
        if (I) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            setColorOfRGBInt(g, 6728679);
            g.fillRect(x, y + 78, 240, getSpriteHeight(6));
            drawSprite(g, 6, x, y + 78, 0);
            drawSprite(g, 6, x + 120, y + 78, 0);
            drawBeveledRect(g, x + 3, y + 3, var4, var3 + 8, 0, 16056665);
            setColorOfRGBInt(g, 16777215);
            // 40: Send your Tama on a trip with your phone!
            drawString(g, getText(40), x + 3 + 6, y + 3 + 4, TextAlign.LEFT);

            for (var5 = 0; var5 < 2; ++var5) {
                aw(g, var5, N, 0);
            }
        } else {
            for (var5 = 0; var5 < 2; ++var5) {
                aw(g, var5, N, 0);
            }
        }

        var5 = ax(N, getSelectedButtonIdx(), 2) / 2;
        var5 -= 10;
        int var6 = ax(N, getSelectedButtonIdx(), 1) + y;
        var6 += ax(N, getSelectedButtonIdx(), 3) / 2;
        aD(g, 64, canvasWidth / 2, var6, (var5 - getSpriteWidth(64)) * 2, M[2]);
        if (I) {
            drawSprite(g, 90, x + 3 + var4 - 1, y + 3 + var3 / 2 - 5, 0);
            drawSprite(g, 57, x + 240 - getSpriteWidth(57) - 2, y + 68, 0);
        }

    }

    public static void aM() {
        aL(0);
    }

    public static void aL(int var0) {
        switch (var0) {
            case 0:
                selectSoftLabel(6);
                break;
            case 1:
                ag(5, true);
                setSelectedButtonIdx(4);
                aN(16750848, 16750848, 16763955, 16777215, 16750848, 16777164, 16750848, 16750848);
                selectSoftLabel(1);
                break;
            case 2:
                selectSoftLabel(6);
                break;
            case 3:
                selectSoftLabel(1);
                ag(3, true);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 6594720, 16777215, 16777215, 6594720, 16763955, 13158600);
                break;
            case 4:
                aO(O, 175, 2, 61);
        }

        StackMap = true;
        P[3] = 0;
        P[2] = 0;
        P[1] = var0;
    }

    public static void aT() {
        int var10002 = P[2]++;
        switch (P[1]) {
            case 0:
                aL(1);
                break;
            case 1:
                aP();
                break;
            case 2:
                aQ();
                break;
            case 3:
                aR();
                break;
            case 4:
                aS();
        }

    }

    public static void aP() {
        int var10002 = P[3]++;
        if (isKeyPressed(2097152L)) {
            ao();
            ap(110, 5);
        } else {
            int var0 = getSelectedButtonIdx();
            int var1 = aq(isKeyPressed(1048576L), isKeyPressed(524288L), isKeyPressed(131072L));
            P[0] = getSelectedButtonIdx();
            if (var0 != P[0]) {
                P[3] = 0;
            }

            if (var1 != -1) {
                aL(2);
            }

        }
    }

    public static void aQ() {
        aU();
        DataInputStream inputStream = null;

        try {
            inputStream = sendDigitsToServer(4);
            unknownOperationOnServerResponse(inputStream);
            int var1 = inputStream.read();
            if (0 < var1) {
                String var2 = readString(inputStream, var1);
                aY(O, 2, 1, var2);
            } else {
                byte[] var14 = new byte[10];
                inputStream.read(var14);
                parseAndStoreDownloadedPassword(var14);
                aL(3);
            }
        } catch (Exception e) {
            log("e:" + e);
            // 92: Communication has failed. Would you like to try again?
            aY(O, 2, 1, getText(92));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }

            playSound(6, false);
        }

    }

    public static void aU() {
        setSomeFlagSentToServer();
        setDigitSentToServer(1, 0);
        setDigitSentToServer(2, 3);
        setDigitSentToServer(3, P[0] + 1);
    }

    public static void aR() {
        int var10002 = P[3]++;
        if (isKeyPressed(2097152L)) {
            ao();
            ap(115, 6);
        } else {
            if (6 < P[2]) {
                switch (aq(isKeyPressed(1048576L), isKeyPressed(131072L), isKeyPressed(524288L))) {
                    case 0:
                        aL(4);
                        break;
                    case 1:
                        aL(0);
                        break;
                    case 2:
                        T(4);
                }
            }

        }
    }

    public static void aS() {
        if (!bc()) {
            aL(3);
        } else {
            bd();
        }
    }

    public static void shoppingCenterPage(Graphics g, int x, int y) {
        switch (P[1]) {
            case 0:
            default:
                break;
            case 1:
                shoppingCenterItemTypeSelect(g, x, y);
                break;
            case 2:
                shoppingCenterIssuingItemTicket(g, x, y);
                break;
            case 3:
                shoppingCenterItemTicket(g, x, y);
                break;
            case 4:
                shoppingCenterSendViaIR(g, x, y);
        }

    }

    public static void shoppingCenterItemTypeSelect(Graphics g, int x, int y) {
        if (I) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // 2: Shopping Center
            drawTextWithBackground(g, getText(2), canvasWidth / 2, y + 2, currentFont.stringWidth(getText(2)) + 8, 2);
        }

        int var4;
        for (int i = 0; i < 5; ++i) {
            var4 = i * 4;
            ai(Q[var4 + 0], Q[var4 + 0], Q[var4 + 0], Q[var4 + 1], Q[var4 + 0], Q[var4 + 2], Q[var4 + 2], Q[var4 + 2], Q[var4 + 3], Q[var4 + 2]);
            bk(g, i, R, 0);
        }

        // 26: Use your Gotchi Points from your Keitama to buy Tamagotchi goods! Choose the rank of the item you want and press OK!
        bl(g, x, getText(26), y + 2 + 34 + 1, P[2], 12, 16056665, 16777215);

        for (var4 = 0; var4 < 5; ++var4) {
            int var5 = bm(R, var4, 1) + 3;
            int var6 = bm(R, var4, 2) - 66;
            byte var7;
            boolean var8;
            if (var4 == getSelectedButtonIdx()) {
                var7 = 61;
                var8 = false;
                var6 -= abs((P[3] & 31) - 16);
            } else {
                var7 = 62;
                var8 = false;
            }

            int var10 = var7 + (P[3] >> 3 & 1);
            int var9 = y + bm(R, var4, 1);
            var9 += bm(R, var4, 3) / 2 + 2;
            drawButtonTamas(g, var10, x + bm(R, var4, 0), var9, var6, P[2], 0);
        }

    }

    public static void shoppingCenterIssuingItemTicket(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        setColorOfRGBInt(g, 6728679);
        g.fillRect(x, y + 110, 240, getSpriteHeight(6));
        drawSprite(g, 6, x, y + 110, 0);
        drawSprite(g, 6, x + 120, y + 110, 0);
        // 76: Issuing Item Ticket
        drawTextWithBackground(g, getText(76), canvasWidth / 2, y + 2, 200, 2);
        drawSprite(g, 76, canvasWidth / 2, y + 100, 2);
    }

    public static void shoppingCenterItemTicket(Graphics g, int x, int y) {
        if (I) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // 79: Item Ticket
            drawTextWithBackground(g, getText(79), canvasWidth / 2, y + 3, currentFont.stringWidth(getText(79)) + 8, 2);
        } else {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y + bm(S, 0, 1), 240, 240 - (bm(S, 0, 1) + getSpriteHeight(0)));
        }

        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 64: Enter the Ticket No. in your Keitama
        bl(g, x, getText(64), y + 3 + currentFontHeight + 12, P[2], 12, 16056665, 16777215);
        int var3 = y + 3 + currentFontHeight + 12 + currentFontHeight + 4;
        if (I) {
            bo(g, canvasWidth / 2, var3, 2, 0, 16770972, 16750748, 16770972);
            bp(g, canvasWidth / 2, var3 + 10, 56, P[2] >> 1, false);
        } else {
            bq(g, canvasWidth / 2, var3 + 10, 56, P[2] >> 1, false, 16770972);
        }

        br(g, canvasWidth / 2, var3 + 4, 62, false, I);

        for (int var4 = 0; var4 < 3; ++var4) {
            bk(g, var4, S, 0);
        }

        aD(g, 61, x + bm(S, getSelectedButtonIdx(), 0), y + bm(S, getSelectedButtonIdx(), 1) + bm(S, getSelectedButtonIdx(), 3) / 2, bm(S, getSelectedButtonIdx(), 2) - 10, P[2]);
    }

    public static void shoppingCenterSendViaIR(Graphics g, int x, int y) {
        sendViaIR(g, x, y);
    }

    public static void bl(Graphics g, int var1, String var2, int var3, int var4, int var5, int var6, int var7) {
        bt(g, var2, var1, var3, 240, var4, var5, var6, var7);
    }

    public static void bt(Graphics g, String text, int var2, int var3, int var4, int var5, int var6, int textColor, int var8) {
        setColorOfRGBInt(g, var8);
        g.fillRect(var2, var3, var4, currentFontHeight);
        setColorOfRGBInt(g, textColor);
        int var9 = currentFont.stringWidth(text);
        drawString(g, text, var2 + var4 - var5 * var6 % (var4 + var9), var3, TextAlign.LEFT);
    }

    public static void bp(Graphics g, int var1, int var2, int var3, int var4, boolean var5) {
        drawSprite(g, 32, var1 - (var3 + 6), var2 + 18, 0);
        drawSprite(g, (!var5 ? 77 : 78) + var4 % 3 * 2, var1 - (var3 + 5), var2, 0);
        drawSprite(g, 33, var1 + (var3 - 3), var2 + 20, 0);
        drawSprite(g, (var5 ? 83 : 84) + var4 % 3 * 2, var1 + (var3 - 3), var2, 0);
    }

    public static void bq(Graphics g, int var1, int var2, int var3, int var4, boolean var5, int var6) {
        setColorOfRGBInt(g, var6);
        g.fillRect(var1 - (var3 + 6), var2, getSpriteWidth(32), 18 + getSpriteHeight(32));
        g.fillRect(var1 + (var3 - 3), var2, getSpriteWidth(33), 20 + getSpriteHeight(33));
        bp(g, var1, var2, var3, var4, var5);
    }

    public static void drawTextWithBackground(Graphics g, String text, int x, int y, int width, int align) {
        int height = calculateTextHeight(text);
        byte var7 = 0;
        int var8 = 16056665;
        int var9 = 16777215;
        if (align == 2) {
            x -= width / 2;
        } else if (align == 1) {
            x -= width;
        }

        drawBeveledRect(g, x, y, width, height, var7, var8);
        setColorOfRGBInt(g, var9);
        drawString(g, text, x + width / 2, y + 2, TextAlign.CENTER);
    }

    public static int calculateTextHeight(String text) {
        return (currentFontHeight + 1) * (splitCount(text, "\n") - 1) + currentFontHeight + 4;
    }

    public static void bo(Graphics g, int x, int y, int var3, int var4, int var5, int var6, int var7) {
        if (var3 == 2) {
            x -= 88;
        } else if (var3 == 1) {
            x -= 176;
        }

        drawBeveledRect(g, x, y, 176, 70, var4, var5);
        drawBeveledRect(g, x + 3, y + 3, 170, 64, var6, var7);
    }

    public static void bv(Graphics g, int x, int y, int var3, int var4, int var5, int var6, int var7) {
        byte width = 32;
        int newY = y + 24;
        setColorOfRGBInt(g, var7);
        g.fillRect(x, newY, width, 2);
        g.fillRect(x, newY + 2 + 1, width, 16);
        g.fillRect(x, newY + 2 + 1 + 16 + 1, width, 2);
        g.fillRect(x + width + 176, newY, width, 2);
        g.fillRect(x + width + 176, newY + 2 + 1, width, 16);
        g.fillRect(x + width + 176, newY + 2 + 1 + 16 + 1, width, 2);
        bo(g, canvasWidth / 2, y, 2, var3, var4, var5, var6);
    }

    public static void aD(Graphics var0, int var1, int var2, int var3, int var4, int var5) {
        drawButtonTamas(var0, var1, var2, var3, var4, var5, 1);
    }

    public static void drawButtonTamas(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6) {
        drawSprite(var0, var1 + (var6 & -(var5 >> 2 & 1)), var2 - var4 / 2 - getSpriteWidth(var1) - 2 - 2, var3 - getSpriteHeight(var1) / 2, 0);
        drawSprite(var0, var1 + (var6 & -((var5 >> 2) + 1 & 1)), var2 + var4 / 2 + 2 + 2, var3 - getSpriteHeight(var1) / 2, 0);
    }

    public static void br(Graphics var0, int var1, int var2, int var3, boolean var4, boolean var5) {
        int var6 = var1 - 35;
        int var7 = var2 + (var3 - 49) / 2;
        int var8 = getCursorIndex();
        int var9 = getPreviousCursorIndex();
        setColorOfRGBInt(var0, 16777215);
        if (var5) {
            var0.fillRect(var6 - 2, var2, 75, var3);
        } else {
            var0.fillRect(var6 + var9 % 5 * 15, var7 + var9 / 5 * 26, 11, 24);
            var0.fillRect(var6 + var8 % 5 * 15, var7 + var8 / 5 * 26, 11, 24);
        }

        if (var4) {
            setColorOfRGBInt(var0, 13619071);
            var0.fillRect(var6 + var8 % 5 * 15, var7 + var8 / 5 * 26, 11, 24);
        }

        setColorOfRGBInt(var0, 0);
        if (var5) {
            by(var0, var1, var7, 2);
        } else {
            bz(var0, var1, var7, 2, var9);
            bz(var0, var1, var7, 2, var8);
        }

    }

    public static void bD() {
        clearDigitEditor();
        setCursorIndex(0);
        bC(0);
    }

    public static void bC(int var0) {
        T = true;
        switch (var0) {
            case 0:
                selectSoftLabel(6);
                break;
            case 1:
                selectSoftLabel(1);
                T = false;
                ag(2, true);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                U[0] = 0;
                break;
            case 2:
                selectSoftLabel(6);
                break;
            case 3:
                selectSoftLabel(6);
                ag(1, false);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 4:
                selectSoftLabel(1);
                ag(2, true);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 5:
                aO(O, 177, 2, 55);
        }

        StackMap = true;
        U[6] = 0;
        U[1] = var0;
    }

    public static void clearDownloadedImagesAndTexts() {
        for (int i = 0; i < 2; ++i) {
            if (downloadedImages2[i] != null) {
                downloadedImages2[i].dispose();
                downloadedImages2[i] = null;
            }
        }

        downloadedString = null;
        currentDownloadedQuote = null;
        System.gc();
    }

    public static void bK() {
        int var10002 = U[6]++;
        switch (U[1]) {
            case 0:
                bC(1);
                break;
            case 1:
                bF();
                break;
            case 2:
                bG();
                break;
            case 3:
                bH();
                break;
            case 4:
                bI();
                break;
            case 5:
                bJ();
        }

    }

    public static void bF() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(121, 6);
        } else {
            if (U[0] != 0) {
                if (aq(isKeyPressed(1048576L), false, false) != -1) {
                    bC(2);
                } else if (!isKeyPressed(1048576L)) {
                    if (isKeyPressed(196608L)) {
                        setCursorIndex(9);
                        U[0] = 0;
                    } else if (isKeyPressed(786432L)) {
                        setCursorIndex(0);
                        U[0] = 0;
                    }
                }
            } else if (handleDigitEditorInput(isKeyPressed(65536L), isKeyPressed(262144L), isKeyPressed(131072L), isKeyPressed(524288L), isKeyPressed(1048576L), getPressedNumber())) {
                U[0] = 1;
            }

            setSelectedButtonIdx(U[0]);
        }
    }

    public static void bG() {
        clearDownloadedImagesAndTexts();
        bN();
        DataInputStream inputStream = null;

        try {
            inputStream = sendDigitsToServer(13);
            unknownOperationOnServerResponse(inputStream);
            int var1 = inputStream.read();
            if (0 < var1) {
                String var2 = readString(inputStream, var1);
                aY(O, 2, 1, var2);
            } else {
                byte[] buffer = new byte[10];
                inputStream.read(buffer);

                for (int i = 0; i < 2; ++i) {
                    int imageSize = inputStream.readUnsignedShort();
                    downloadedImages2[i] = readImage(inputStream, imageSize);
                }

                int stringSize = inputStream.readUnsignedShort();
                downloadedString = readString(inputStream, stringSize);

                U[2] = 0;
                U[4] = 0;
                U[5] = countQuotedSegments(downloadedString);
                currentDownloadedQuote = findNthQuote(downloadedString, U[4]);
                parseAndStoreDownloadedPassword(buffer);
                bC(3);
            }
        } catch (Exception e) {
            log("e:" + e);
            // 92: Communication has failed. Would you like to try again?
            aY(O, 2, 1, getText(92));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }

            playSound(6, false);
        }

    }

    public static void bN() {
        setSomeFlagSentToServer();
        setDigitSentToServer(1, 1);
        setDigitSentToServer(2, 3);

        for (int i = 0; i < 10; ++i) {
            setDigitSentToServer(3 + i, getDigitBankA(i));
        }

    }

    public static void bH() {
        int var10002 = U[3]++;
        if (6 < U[6]) {
            if (-1 != aq(isKeyPressed(1048576L), isKeyPressed(65536L), isKeyPressed(262144L))) {
                var10002 = U[4]++;
                U[3] = 0;
                if (U[5] <= U[4]) {
                    generateDerivedCodeInBankB();
                    if (3 < getDigitBankB(2)) {
                        T(4);
                    } else {
                        bC(4);
                    }
                } else {
                    currentDownloadedQuote = findNthQuote(downloadedString, U[4]);
                }
            }
        } else {
            int var0 = currentFont.stringWidth(currentDownloadedQuote);
            if (var0 + 232 <= U[3] * 12) {
                U[3] = 0;
            }
        }

    }

    public static void bI() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(127, 6);
        } else {
            if (6 < U[6]) {
                switch (aq(isKeyPressed(1048576L), isKeyPressed(131072L), isKeyPressed(524288L))) {
                    case 0:
                        bC(5);
                        break;
                    case 1:
                        T(4);
                }
            }

        }
    }

    public static void bJ() {
        if (!bc()) {
            bC(4);
        } else {
            bd();
        }
    }

    public static void parentCallPage(Graphics g, int x, int y) {
        switch (U[1]) {
            case 0:
            default:
                break;
            case 1:
                parentCallCodeInput(g, x, y);
                break;
            case 2:
                bV(g, x, y);
                break;
            case 3:
                parentCallExplanation(g, x, y);
                break;
            case 4:
                parentCallAllowanceTicket(g, x, y);
                break;
            case 5:
                parentCallSendViaIR(g, x, y);
        }

    }

    public static void parentCallCodeInput(Graphics g, int x, int y) {
        // 17: Connect with your parent on Tamagotchi Planet!
        int textHeight = calculateTextHeight(getText(17));
        int var4 = y + 240 - 42;
        if (I) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240 - getSpriteHeight(0));
            // 17: Connect with your parent on Tamagotchi Planet!
            drawTextWithBackground(g, getText(17), canvasWidth / 2, y + 3, 232, 2);
        }

        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 65: Enter the Address No. shown on your Keitama
        bl(g, x, getText(65), y + 3 + textHeight + 3, U[6], 12, 16056665, 16777215);
        int var5 = y + 3 + textHeight + 3 + currentFontHeight + 4 + 20;
        boolean var6 = 0 == getSelectedButtonIdx() & (U[6] & 4) != 0;
        ca(g, canvasWidth / 2, var5, var6);
        // 18: OK
        cb(g, 1, getText(18), canvasWidth / 2, var4, 100, 28, 2, 0);
        if (I) {
            bp(g, canvasWidth / 2, var5 + 5, 56, U[6] >> 1, true);
        } else {
            bq(g, canvasWidth / 2, var5 + 5, 56, U[6] >> 1, true, 16777076);
        }

        if (1 == getSelectedButtonIdx()) {
            aD(g, 55, canvasWidth / 2, y + 240 - 42 + 14, 120, U[6]);
        }

    }

    public static void bV(Graphics g, int x, int y) {
        ac(g, x, y, 8, 0);
        drawSprite(g, 24, x + 126, y + 100, 0);
        int spriteWidth = getSpriteWidth(24);

        for (int i = 0; i < 3; ++i) {
            drawSprite(g, 7, x + 126 - 6 * i, y + 118, 0);
            drawSprite(g, 8, x + 126 + spriteWidth + 6 * i, y + 118, 0);
        }

    }

    public static void parentCallExplanation(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        int var3 = y + 4;
        // 23: Parent Call
        drawTextWithBackground(g, getText(23), canvasWidth / 2, var3, currentFont.stringWidth(getText(23)) + 8, 2);
        int var4 = var3 + currentFontHeight + 8 + 6;
        drawSprite(g, 58, x, var4, 0);
        drawImage(g, downloadedImages2[U[6] >> 3 & 1], x + 144, var4, 0);
        int var5 = var4 + 130 + 1;
        int var6 = (canvasWidth - 232) / 2;
        bt(g, currentDownloadedQuote, var6, var5, 232, U[3], 12, 16777215, 16056665);
        setColorOfRGBInt(g, 0);
        g.drawRect(var6, var5 - 1, 231, currentFontHeight + 1);
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, var5, 4, currentFontHeight);
        g.fillRect(var6, var5 - 1, 1, 1);
        g.fillRect(var6, var5 + currentFontHeight, 1, 1);
        g.fillRect(var6 + 232, var5, 4, currentFontHeight);
        g.fillRect(var6 - 1, var5 - 1, 1, 1);
        g.fillRect(var6 + 232 - 1, var5 + currentFontHeight, 1, 1);
        byte var7;
        if (U[5] - 1 <= U[4]) {
            var7 = 22;
        } else {
            var7 = 87;
        }

        // 7: Explanation
        cb(g, 0, getText(var7), canvasWidth / 2, y + 240 - 36, 160, 28, 2, 0);
        drawSprite(g, 27, x + 1, y + 1, 0);
        drawSprite(g, 28, x + 240 - 1 - getSpriteWidth(28), y + 1, 0);
        drawSprite(g, 37 + (U[6] >> 1 & 1), x + 240 - 44, y + var4 + 1, 0);
        drawSprite(g, 12, x + 240 - 38, var4 + 9, 0);
        drawSprite(g, 92, x + 140, var5 - 16, 0);
        aD(g, 55, canvasWidth / 2, y + 240 - 36 + getSpriteHeight(55) / 2, 160, U[6]);
    }

    public static void parentCallAllowanceTicket(Graphics g, int x, int y) {
        // 67: Allowance Ticket
        int textHeight = calculateTextHeight(getText(67));
        if (I) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // 67: Allowance Ticket
            drawTextWithBackground(g, getText(67), canvasWidth / 2, y + 3, currentFont.stringWidth(getText(67)) + 8, 2);
        } else {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y + bm(Y, 0, 1) - 5, 240, 240 - (bm(Y, 0, 1) - 5 + getSpriteHeight(0)));
        }

        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 66: Enter the Allowance Ticket No. in your Keitama!
        bl(g, x, getText(66), y + 3 + textHeight + 3, U[6], 12, 16056665, 16777215);
        int var4 = y + 3 + textHeight + 3 + currentFontHeight + 12;
        ca(g, canvasWidth / 2, var4, false);

        for (int i = 0; i < 2; ++i) {
            bk(g, i, Y, 0);
        }

        bq(g, canvasWidth / 2, var4 + 5, 56, U[6] >> 1, false, 16777076);
        aD(g, 55, x + bm(Y, getSelectedButtonIdx(), 0), y + bm(Y, getSelectedButtonIdx(), 1) + bm(Y, getSelectedButtonIdx(), 3) / 2, bm(Y, getSelectedButtonIdx(), 2), U[6]);
    }

    public static void parentCallSendViaIR(Graphics var0, int var1, int var2) {
        sendViaIR(var0, var1, var2);
    }

    public static void ca(Graphics var0, int var1, int var2, boolean var3) {
        int var4 = var1 - 88;
        if (I) {
            setColorOfRGBInt(var0, 16777076);
            var0.drawRect(var4, var2, 176, 70);
            var0.fillRect(var4 + 2, var2 + 2, 173, 67);
        }

        br(var0, var1, var2 + 2, 67, var3, I);
    }

    public static void ac(Graphics var0, int var1, int var2, int var3, int var4) {
        setColorOfRGBInt(var0, 11367);
        var0.fillRect(var1, var2, 240, 240);
        drawSprite(var0, 1, var1, var2 + 240 - getSpriteHeight(1), 0);
        byte var5 = 0;
        drawSprite(var0, 3, var1 + 240 + var5, var2 + 10, 1);
        cc(var0, var1 + 50, var2 + 108, 12, -8, 5, var4, var3);
    }

    public static void ce() {
        for (int i = 0; i < aa.length; ++i) {
            disposeImage(aa[i]);
        }

        clearDigitEditor();
        setCursorIndex(0);
        cd(0);
    }

    public static void cd(int var0) {
        T = true;
        switch (var0) {
            case 0:
                selectSoftLabel(6);
                break;
            case 1:
                selectSoftLabel(1);
                T = false;
                ab[0] = 0;
                ag(2, true);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 2:
                selectSoftLabel(6);
                break;
            case 3:
                selectSoftLabel(6);
                ag(1, false);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 4:
                selectSoftLabel(6);
                break;
            case 5:
                selectSoftLabel(6);
                ag(1, false);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 6:
                selectSoftLabel(6);
                break;
            case 7:
                selectSoftLabel(1);
                ag(2, true);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 8:
                aO(O, 179, 2, 39);
        }

        StackMap = true;
        ab[3] = 0;
        ab[1] = var0;
    }

    public static void clearGotchiKingImages() {
        for (int i = 0; i < 2; ++i) {
            if (gotchiKingImages[i] != null) {
                gotchiKingImages[i].dispose();
                gotchiKingImages[i] = null;
            }
        }

        System.gc();

        for (int i = 0; i < aa.length; ++i) {
            try {
                loadImage(aa[i]);
            } catch (Exception e) {
            }
        }

        System.gc();
    }

    public static void gotchiKingFlow() {
        int var10002 = ab[3]++;
        switch (ab[1]) {
            case 0:
                cd(1);
                break;
            case 1:
                cg();
                break;
            case 2:
                downloadGotchiKingData();
                break;
            case 3:
                ci();
                break;
            case 4:
                cj();
                break;
            case 5:
                ck();
                break;
            case 6:
                cl();
                break;
            case 7:
                cm();
                break;
            case 8:
                cn();
        }

    }

    public static void cg() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(133, 6);
        } else {
            if (ab[0] != 0) {
                if (aq(isKeyPressed(1048576L), false, false) != -1) {
                    cd(2);
                } else if (!isKeyPressed(1048576L)) {
                    if (isKeyPressed(196608L)) {
                        setCursorIndex(9);
                        ab[0] = 0;
                    } else if (isKeyPressed(786432L)) {
                        setCursorIndex(0);
                        ab[0] = 0;
                    }
                }
            } else if (handleDigitEditorInput(isKeyPressed(65536L), isKeyPressed(262144L), isKeyPressed(131072L), isKeyPressed(524288L), isKeyPressed(1048576L), getPressedNumber())) {
                ab[0] = 1;
            }

            setSelectedButtonIdx(ab[0]);
        }
    }

    public static void downloadGotchiKingData() {
        clearGotchiKingImages();
        cp();
        DataInputStream inputStream = null;

        try {
            ad = 0L;
            inputStream = sendDigitsToServer(13);
            unknownOperationOnServerResponse(inputStream);

            int textLength = inputStream.read();
            if (0 < textLength) {
                String text = readString(inputStream, textLength);
                aY(O, 2, 1, text);
            } else {
                byte[] passwordData = new byte[10];
                inputStream.read(passwordData);

                for (int i = 0; i < 2; ++i) {
                    int imageSize = inputStream.readUnsignedShort();
                    gotchiKingImages[i] = readImage(inputStream, imageSize);
                }

                ab[4] = 0;
                ab[5] = 0;
                ab[2] = 0;
                parseAndStoreDownloadedPassword(passwordData);
                cd(3);
            }
        } catch (Exception e) {
            log("e:" + e);
            // 92: Communication has failed. Would you like to try again?
            aY(O, 2, 1, getText(92));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }

            playSound(6, false);
        }

    }

    public static void cp() {
        setSomeFlagSentToServer();
        setDigitSentToServer(1, 2);
        setDigitSentToServer(2, 3);

        for (int i = 0; i < 10; ++i) {
            setDigitSentToServer(3 + i, getDigitBankA(i));
        }

    }

    public static void ci() {
        if (aq(isKeyPressed(1048576L), isKeyPressed(65536L), isKeyPressed(262144L)) != -1) {
            cd(4);
        }

    }

    public static void cj() {
        if (45 <= ab[3] || isKeyPressed(1048576L)) {
            cd(5);
        }

    }

    public static void ck() {
        int var10002;
        if (6 <= ab[5]) {
            ab[5] = 0;
            var10002 = ab[4]++;
            int[] var10000 = ab;
            var10000[4] %= 2;
        } else {
            var10002 = ab[5]++;
        }

        if (6 < ab[3] && aq(isKeyPressed(1048576L), isKeyPressed(65536L), isKeyPressed(262144L)) != -1) {
            cd(6);
        }

    }

    public static void cl() {
        if (30 < ab[3]) {
            playSound(6, false);
            cd(7);
        }

    }

    public static void cm() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(139, 6);
        } else {
            if (6 < ab[3]) {
                switch (aq(isKeyPressed(1048576L), isKeyPressed(131072L), isKeyPressed(524288L))) {
                    case 0:
                        cd(8);
                        break;
                    case 1:
                        T(4);
                }
            }

        }
    }

    public static void cn() {
        if (!bc()) {
            cd(7);
        } else {
            bd();
        }
    }

    public static void gotchiKingPage(Graphics g, int x, int y) {
        switch (ab[1]) {
            case 0:
            default:
                break;
            case 1:
                gotchiKingCodeInput(g, x, y);
                break;
            case 2:
                cr(g, x, y);
                break;
            case 3:
            case 4:
                gotchiKingBroadcast(g, x, y);
                break;
            case 5:
                ct(g, x, y);
                break;
            case 6:
                gotchiKingIssuingInvitation(g, x, y);
                break;
            case 7:
                gotchiKingInviteTicket(g, x, y);
                break;
            case 8:
                gotchiKingSendViaIR(g, x, y);
        }

    }

    public static void gotchiKingCodeInput(Graphics g, int x, int y) {
        // 30: Connect with the Gotchi King on Tamagotchi Planet!
        int textHeight = calculateTextHeight(getText(30));
        if (I) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240 - getSpriteHeight(0));
            // 30: Connect with the Gotchi King on Tamagotchi Planet!
            drawTextWithBackground(g, getText(30), canvasWidth / 2, y + 2, 230, 2);
        }

        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 68: Enter the Gotchi King Address No. shown on your Keitama
        bl(g, x, getText(68), y + 2 + textHeight + 2, ab[3], 12, 16056665, 16777215);
        boolean var4 = 0 == getSelectedButtonIdx() & (ab[3] & 4) != 0;
        int var5 = y + 2 + textHeight + 2 + currentFontHeight + 3;
        ca(g, canvasWidth / 2, var5, var4);
        // 18: OK
        cb(g, 1, getText(18), canvasWidth / 2, y + 240 - 42, 100, 28, 2, 0);
        if (I) {
        }

        if (I) {
            bp(g, canvasWidth / 2, var5 + 5, 56, ab[3] >> 1, true);
        } else {
            bq(g, canvasWidth / 2, var5 + 5, 56, ab[3] >> 1, true, 16777076);
        }

        if (1 == getSelectedButtonIdx()) {
            aD(g, 39, canvasWidth / 2, y + 240 - 42 + getSpriteHeight(39) / 2, 100, ab[3]);
        }

    }

    public static void cr(Graphics g, int x, int y) {
        bV(g, x, y);
    }

    public static void gotchiKingBroadcast(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        int var3 = y + 68;
        setColorOfRGBInt(g, 6532583);
        g.fillRect(x, var3, 240, 118);
        int var4 = y + 4;
        // 31: Gotchi King Broadcast
        drawTextWithBackground(g, getText(31), canvasWidth / 2, var4, 200, 2);
        int var5 = var3 + 15;
        if (ab[1] == 4) {
            int var6;
            if (20 < ab[3]) {
                var6 = 20;
            } else {
                var6 = ab[3];
            }

            var5 -= var6 * 6 / 20;
        }

        drawSprite(g, 6, x, var3 + 76, 0);
        drawSprite(g, 6, x + 120, var3 + 76, 0);
        drawSprite(g, 66, canvasWidth / 2, var3, 2);
        drawBeveledRect(g, (canvasWidth - 36) / 2, var3 + 21, 36, 8, 3805255, 16315136);
        drawBeveledRect(g, (canvasWidth - 36) / 2, var5, 36, 8, 3805255, 16315136);
        if (ab[1] == 3) {
            // 75: Enter
            cb(g, 0, getText(75), canvasWidth / 2, y + 240 - 44, 100, 28, 2, 0);
            aD(g, 39, canvasWidth / 2, y + 240 - 44 + getSpriteHeight(39) / 2, 100, ab[3]);
        }

        drawSprite(g, 27, x + 1, y + 1, 0);
        drawSprite(g, 28, x + 240 - 1 - getSpriteWidth(28), y + 1, 0);
        drawSprite(g, 37 + (ab[3] >> 1 & 1), x + 240 - 44, var3 + 2, 0);
        drawSprite(g, 12, x + 240 - 38, var3 + 10, 0);
    }

    public static void ct(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        int var3 = y + 4;
        // 31: Gotchi King Broadcast
        drawTextWithBackground(g, getText(31), canvasWidth / 2, var3, 200, 2);
        int imageY = y + 68;
        int imageIndex = ab[4];
        drawImage(g, gotchiKingImages[imageIndex], x + 0, imageY, 0);
        // 32: Invite
        cb(g, 0, getText(32), canvasWidth / 2, y + 240 - 44, 160, 28, 2, 0);
        drawSprite(g, 27, x + 1, y + 1, 0);
        drawSprite(g, 28, x + 240 - 1 - getSpriteWidth(28), y + 1, 0);
        drawSprite(g, 37 + (ab[3] >> 1 & 1), x + 240 - 44, imageY + 2, 0);
        drawSprite(g, 12, x + 240 - 38, imageY + 10, 0);
        aD(g, 39, canvasWidth / 2, y + 240 - 44 + getSpriteHeight(39) / 2, 160, ab[3]);
    }

    public static void gotchiKingIssuingInvitation(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 77: Issuing Invitation
        drawTextWithBackground(g, getText(77), canvasWidth / 2, y + 2, 200, 2);
        byte var3 = 16;
        byte var4 = 6;
        byte var5 = 11;
        int var6 = var4 + var3 * (var5 - 1);
        cc(g, (canvasWidth - var6) / 2, y + 68, var3, 0, var4, ab[3], var5);
        drawSprite(g, 34, canvasWidth / 2, y + 90, 2);
    }

    public static void gotchiKingInviteTicket(Graphics g, int x, int y) {
        // 69: Invite Ticket
        int textHeight = calculateTextHeight(getText(69));
        int var4;
        if (I) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // 69: Invite Ticket
            drawTextWithBackground(g, getText(69), canvasWidth / 2, y + 3, currentFont.stringWidth(getText(69)) + 8, 2);
        } else {
            var4 = y + bm(ae, 0, 1);
            var4 += bm(ae, 0, 3) / 2;
            var4 -= getSpriteHeight(39) / 2;
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, var4, 240, 240 - (var4 + getSpriteHeight(39) / 2));
        }

        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 70: Enter the Invitation Ticket No. in your Keitama!
        bl(g, x, getText(70), y + 3 + textHeight + 3, ab[3], 12, 16056665, 16777215);

        for (var4 = 0; var4 < 2; ++var4) {
            bk(g, var4, ae, 0);
        }

        int var5 = y + 3 + textHeight + 3 + currentFontHeight + 12;
        if (I) {
            bo(g, canvasWidth / 2, var5, 2, 0, 16756418, 13722050, 16756418);
            bp(g, canvasWidth / 2, var5 + 10, 56, ab[3] >> 1, false);
        } else {
            bq(g, canvasWidth / 2, var5 + 10, 56, ab[3] >> 1, false, 16756418);
        }

        br(g, canvasWidth / 2, var5 + 4, 62, false, I);
        aD(g, 39, x + bm(ae, getSelectedButtonIdx(), 0), y + bm(ae, getSelectedButtonIdx(), 1) + bm(ae, getSelectedButtonIdx(), 3) / 2 + 1, bm(ae, getSelectedButtonIdx(), 2), ab[3]);
    }

    public static void gotchiKingSendViaIR(Graphics g, int x, int y) {
        sendViaIR(g, x, y);
    }

    public static void cz() {
        clearDigitEditor();
        setCursorIndex(0);
        cy(0);
    }

    public static void cy(int var0) {
        T = true;
        switch (var0) {
            case 0:
                selectSoftLabel(6);
                break;
            case 1:
                selectSoftLabel(1);
                T = false;
                af[0] = 0;
                ag(2, true);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 2:
                selectSoftLabel(6);
                ag(1, false);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 3:
                selectSoftLabel(1);
                ag(2, true);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 4:
                selectSoftLabel(1);
        }

        StackMap = true;
        af[2] = 0;
        af[1] = var0;
    }

    public static void cA() {
        if (travelMemoryPhoto != null) {
            travelMemoryPhoto.dispose();
            travelMemoryPhoto = null;
        }

        for (int i = 0; i < 2; ++i) {
            ah[i] = null;
        }

        System.gc();
    }

    public static void cF() {
        int var10002 = af[2]++;
        switch (af[1]) {
            case 0:
                cy(1);
                break;
            case 1:
                cB();
                break;
            case 2:
                cC();
                break;
            case 3:
                cD();
                break;
            case 4:
                cE();
        }

    }

    public static void cB() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(145, 6);
        } else {
            if (af[0] != 0) {
                if (aq(isKeyPressed(1048576L), false, false) != -1) {
                    cy(2);
                } else if (!isKeyPressed(1048576L)) {
                    if (isKeyPressed(196608L)) {
                        setCursorIndex(9);
                        af[0] = 0;
                    } else if (isKeyPressed(786432L)) {
                        setCursorIndex(0);
                        af[0] = 0;
                    }
                }
            } else if (handleDigitEditorInput(isKeyPressed(65536L), isKeyPressed(262144L), isKeyPressed(131072L), isKeyPressed(524288L), isKeyPressed(1048576L), getPressedNumber())) {
                af[0] = 1;
            }

            setSelectedButtonIdx(af[0]);
        }
    }

    public static void cC() {
        cA();
        cG();
        DataInputStream var0 = null;

        try {
            var0 = sendDigitsToServer(13);
            unknownOperationOnServerResponse(var0);
            int var1 = var0.read();
            if (0 < var1) {
                String var2 = readString(var0, var1);
                aY(O, 2, 1, var2);
            } else {
                int var14 = var0.read();
                ah[0] = readString(var0, var14);
                af[3] = var0.readUnsignedShort();
                var14 = var0.readUnsignedShort();
                travelMemoryPhoto = readImage(var0, var14);
                var14 = var0.readUnsignedShort();
                ah[1] = readString(var0, var14);
                cy(3);
            }
        } catch (Exception e) {
            log("e:" + e);
            // 92: Communication has failed. Would you like to try again?
            aY(O, 2, 1, getText(92));
        } finally {
            if (var0 != null) {
                try {
                    var0.close();
                } catch (Exception var11) {
                }
            }

            playSound(6, false);
        }

    }

    public static void cG() {
        setSomeFlagSentToServer();
        byte var0 = 4;
        if (ai) {
            var0 = 9;
        }

        setDigitSentToServer(1, var0);
        setDigitSentToServer(2, 4);

        for (int i = 0; i < 10; ++i) {
            setDigitSentToServer(3 + i, getDigitBankA(i));
        }

    }

    public static void cD() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(151, 7);
        } else {
            if (6 < af[2]) {
                switch (aq(isKeyPressed(1048576L), isKeyPressed(131072L), isKeyPressed(524288L))) {
                    case 0:
                        T(4);
                        break;
                    case 1:
                        launchCurrentApp("http://tamapark.gs.keitaiarchive.org/cgi-bin/album.cgi?uid=NULLGWDOCOMO&op=latest");
                }
            }

        }
    }

    public static void cE() {
    }

    public static void travelMemoryPage(Graphics g, int x, int y) {
        switch (af[1]) {
            case 0:
            default:
                break;
            case 1:
                travelMemoryCodeInput(g, x, y);
                break;
            case 2:
                printingPhoto(g, x, y);
                break;
            case 3:
                drawMemoryPhoto(g, x, y);
                break;
            case 4:
                cK(g, x, y);
        }

    }

    public static void travelMemoryCodeInput(Graphics g, int x, int y) {
        // 36: Let's look at travel memories from your trips!
        int textHeight = calculateTextHeight(getText(36));
        int var4 = y + 2 + textHeight + 2 + currentFontHeight + 3;
        boolean var5 = true;
        boolean var6 = true;
        if (I) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // 36: Let's look at travel memories from your trips!
            drawTextWithBackground(g, getText(36), canvasWidth / 2, y + 2, 230, 2);
            if (ai) {
                setColorOfRGBInt(g, 16777215);
                g.fillRect(x, y + 240 - 4, 4, 4);
            }
        } else {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y + 240 - 42, 240, 193);
        }

        if (I) {
            bv(g, x, var4, 0, 16777215, 2210832, 10873360, 7786961);
            bp(g, canvasWidth / 2, var4 + 4 + 2, 56, af[2] >> 1, true);
        } else {
            bq(g, canvasWidth / 2, var4 + 4 + 2, 56, af[2] >> 1, true, 10873360);
        }

        boolean var7 = 0 == getSelectedButtonIdx() & (af[2] & 4) != 0;
        br(g, canvasWidth / 2, var4 + 4, 62, var7, I);
        // 71: Enter the Travel No. shown on your Keitama
        bl(g, x, getText(71), y + 2 + textHeight + 2, af[2], 12, 16056665, 16777215);
        // 18: OK
        cb(g, 1, getText(18), canvasWidth / 2, y + 240 - 42, 100, 28, 2, 0);
        if (1 == getSelectedButtonIdx()) {
            aD(g, 35, canvasWidth / 2, y + 240 - 42 + 14 + 1, 100, af[2]);
        }

    }

    public static void printingPhoto(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        int var3 = y + 4;
        // 37: Printing Photo
        drawTextWithBackground(g, getText(37), canvasWidth / 2, y + 2, 200, 2);
        setColorOfRGBInt(g, 16770939);
        g.fillRect(x, y + 46, 240, 108);
        drawSprite(g, 60, canvasWidth / 2, y + 46, 2);
        byte var4 = 16;
        byte var5 = 6;
        byte var6 = 11;
        int var7 = var5 + var4 * (var6 - 1);
        cc(g, (canvasWidth - var7) / 2, y + 46 + 108 + 8, var4, 0, var5, 0, var6);
    }

    public static void drawMemoryPhoto(Graphics g, int x, int y) {
        int fontHeight = currentFontHeight;
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        int stringWidth = currentFont.stringWidth(ah[0]) + 8;
        if (stringWidth < travelMemoryPhoto.getWidth()) {
            stringWidth = travelMemoryPhoto.getWidth();
        }

        drawTextWithBackground(g, ah[0], canvasWidth / 2, y + 2, stringWidth, 2);
        stringWidth = currentFontHeight;
        drawImage(g, travelMemoryPhoto, canvasWidth / 2, y + 2 + fontHeight + 4 + 4, 2);
        bl(g, x, ah[1], y + 2 + fontHeight + 4 + 4 + travelMemoryPhoto.getHeight() + 7, af[2], 12, 16056665, 16777215);

        for (int i = 0; i < 2; ++i) {
            bk(g, i, aj, 0);
        }

        aD(g, 35, x + bm(aj, getSelectedButtonIdx(), 0), y + bm(aj, getSelectedButtonIdx(), 1) + bm(aj, getSelectedButtonIdx(), 3) / 2 + 1, bm(aj, getSelectedButtonIdx(), 2), af[2]);
    }

    public static void cK(Graphics var0, int var1, int var2) {
    }

    public static int cM(int var0, int var1) {
        return ak[var0 * 8 + var1];
    }

    public static void cO() {
        clearDigitEditor();
        ag(2, true);
        setSelectedButtonIdx(al[0]);
        cN(0);
    }

    public static void cN(int var0) {
        T = true;
        switch (var0) {
            case 0:
                selectSoftLabel(6);
                break;
            case 1:
                selectSoftLabel(1);
                T = false;
                al[0] = 0;
                ag(2, true);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 2:
                selectSoftLabel(6);
                ag(1, false);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 3:
                al[3] = 0;
                selectSoftLabel(1);
                break;
            case 4:
                selectSoftLabel(6);
                ag(1, false);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 5:
                selectSoftLabel(1);
                ag(1, false);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 6:
                selectSoftLabel(1);
                ag(1, false);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 7:
                selectSoftLabel(1);
                ag(2, true);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 8:
                aO(O, 181, 2, 30);
        }

        StackMap = true;
        al[2] = 0;
        al[1] = var0;
    }

    public static void clearDownloadedTextsAndImages() {

        for (int i = 0; i < 2; ++i) {
            if (downloadedImages[i] != null) {
                downloadedImages[i].dispose();
                downloadedImages[i] = null;
            }
        }

        for (int i = 0; i < 3; ++i) {
            downloadedTexts[i] = null;
        }

        System.gc();
    }

    public static void cY() {
        int var10002 = al[2]++;
        switch (al[1]) {
            case 0:
                cN(1);
                break;
            case 1:
                cQ();
                break;
            case 2:
                cR();
                break;
            case 3:
                cS();
                break;
            case 4:
                cT();
                break;
            case 5:
                cU();
                break;
            case 6:
                cV();
                break;
            case 7:
                cW();
                break;
            case 8:
                cX();
        }

    }

    public static void cQ() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(158, 6);
        } else {
            if (al[0] != 0) {
                if (aq(isKeyPressed(1048576L), false, false) != -1) {
                    cN(2);
                } else if (!isKeyPressed(1048576L)) {
                    if (isKeyPressed(196608L)) {
                        setCursorIndex(9);
                        al[0] = 0;
                    } else if (isKeyPressed(786432L)) {
                        setCursorIndex(0);
                        al[0] = 0;
                    }
                }
            } else if (handleDigitEditorInput(isKeyPressed(65536L), isKeyPressed(262144L), isKeyPressed(131072L), isKeyPressed(524288L), isKeyPressed(1048576L), getPressedNumber())) {
                al[0] = 1;
            }

            setSelectedButtonIdx(al[0]);
        }
    }

    public static void cR() {
        if (10 < al[2]) {
            playSound(6, false);
            cN(3);
        }

    }

    public static void cS() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(164, 2);
        } else {
            al[4] = al[3];
            if (isKeyPressed(65536L)) {
                playSound(4, false);
                al[3] = cM(al[3], 2);
            } else if (isKeyPressed(131072L)) {
                playSound(4, false);
                al[3] = cM(al[3], 3);
            } else if (isKeyPressed(262144L)) {
                playSound(4, false);
                al[3] = cM(al[3], 4);
            } else if (isKeyPressed(524288L)) {
                playSound(4, false);
                al[3] = cM(al[3], 5);
            } else if (isKeyPressed(1048576L)) {
                playSound(5, false);
                cN(4);
            }

        }
    }

    public static void cT() {
        clearDownloadedTextsAndImages();
        cZ();
        DataInputStream inputStream = null;

        try {
            // Get response from server
            inputStream = sendDigitsToServer(14);
            unknownOperationOnServerResponse(inputStream);

            int length = inputStream.read();

            if (length > 0) {
                String var2 = readString(inputStream, length);

                if (var2.compareTo("1") == 0) {
                    int imageSize = inputStream.readUnsignedShort();
                    downloadedImages[0] = readImage(inputStream, imageSize);
                    cN(5);
                } else {
                    aY(O, 4, 1, var2);
                }

            } else {
                byte[] buffer = new byte[10];
                inputStream.read(buffer);

                int size = inputStream.read();
                downloadedTexts[2] = readString(inputStream, size);

                size = inputStream.read();
                downloadedTexts[0] = readString(inputStream, size);

                size = inputStream.read();
                // 49: -san
                downloadedTexts[1] = readString(inputStream, size) + getText(49);

                size = inputStream.readUnsignedShort();
                downloadedImages[0] = readImage(inputStream, size);

                size = inputStream.readUnsignedShort();
                downloadedImages[1] = readImage(inputStream, size);

                parseAndStoreDownloadedPassword(buffer);
                cN(6);
            }
        } catch (Exception e) {
            log("e:" + e);
            // 92: Communication has failed. Would you like to try again?
            aY(O, 4, 1, getText(92));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }

            playSound(6, false);
        }

    }

    public static void cZ() {
        setSomeFlagSentToServer();
        setDigitSentToServer(1, 5);
        setDigitSentToServer(2, 3);

        for (int i = 0; i < 10; ++i) {
            setDigitSentToServer(3 + i, getDigitBankA(i));
        }

        setDigitSentToServer(13, al[3] + 1);
    }

    public static void cU() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(167, 1);
        } else {
            if (6 < al[2] && aq(isKeyPressed(1048576L), isKeyPressed(65536L), isKeyPressed(262144L)) != -1) {
                cN(3);
            }

        }
    }

    public static void cV() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(166, 1);
        } else {
            if (6 < al[2] && aq(isKeyPressed(1048576L), isKeyPressed(65536L), isKeyPressed(262144L)) != -1) {
                cN(7);
            }

        }
    }

    public static void cW() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(168, 7);
        } else {
            if (6 < al[2]) {
                switch (aq(isKeyPressed(1048576L), isKeyPressed(131072L), isKeyPressed(524288L))) {
                    case 0:
                        cN(8);
                        break;
                    case 1:
                        T(4);
                }
            }

        }
    }

    public static void cX() {
        if (!bc()) {
            cN(7);
        } else {
            bd();
        }
    }

    public static void exchangePlazaPage(Graphics g, int x, int y) {
        switch (al[1]) {
            case 0:
            default:
                break;
            case 1:
                exchangePlazaCodeInput(g, x, y);
                break;
            case 2:
                exchangePlazaLoadingScreen(g, x, y);
                break;
            case 3:
                exchangePlazaRegionSelect(g, x, y);
                break;
            case 4:
                exchangePlazaLoadingShip(g, x, y);
                break;
            case 5:
                exchangePlazaExchangeFailed(g, x, y);
                break;
            case 6:
                exchangePlazaExchangeSuccess(g, x, y);
                break;
            case 7:
                exchangePlazaExchangeTicket(g, x, y);
                break;
            case 8:
                exchangePlazaSendViaIR(g, x, y);
        }

    }

    public static void exchangePlazaCodeInput(Graphics g, int x, int y) {
        // 41: Trade the regional specialty items you've collected!
        int textHeight = calculateTextHeight(getText(41));
        int var4 = y + 2 + textHeight + 2 + currentFontHeight + 3;
        boolean var5 = true;
        boolean var6 = true;
        if (I) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // 41: Trade the regional specialty items you've collected!
            drawTextWithBackground(g, getText(41), canvasWidth / 2, y + 2, 230, 2);
        } else {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y + 240 - 52, 240, 188);
        }

        if (I) {
            bv(g, x, var4, 0, 16777215, 3429838, 6728678, 7786961);
            bp(g, canvasWidth / 2, var4 + 4 + 2, 56, al[2] >> 1, true);
        } else {
            bq(g, canvasWidth / 2, var4 + 4 + 2, 56, al[2] >> 1, true, 6728678);
        }

        // 72: Enter the Exchange No. shown on your Keitama
        bl(g, x, getText(72), y + 2 + textHeight + 2, al[2], 12, 16056665, 16777215);
        boolean var7 = 0 == getSelectedButtonIdx() & (al[2] & 4) != 0;
        br(g, canvasWidth / 2, var4 + 4, 62, var7, I);
        // 18: OK
        cb(g, 1, getText(18), canvasWidth / 2, y + 240 - 42, 100, 28, 2, 0);
        if (1 == getSelectedButtonIdx()) {
            aD(g, 30, canvasWidth / 2, y + 240 - 42 + 7 + 1, 100, al[2]);
        }

    }

    public static void exchangePlazaLoadingScreen(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 73: To Exchange Plaza!
        drawTextWithBackground(g, getText(73), canvasWidth / 2, y + 2, currentFont.stringWidth(getText(73)) + 8, 2);
        drawSprite(g, 59, canvasWidth / 2, y + 50, 2);
        short var3 = 176;
        cc(g, (canvasWidth - var3) / 2, y + 160, 16, 0, 6, al[2], 11);
    }

    public static void exchangePlazaRegionSelect(Graphics g, int x, int y) {
        int var3 = x + 168;
        int var4 = y + 76;
        int var5;
        if (I) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            drawSprite(g, 71, x, y + 240 - getSpriteHeight(71), 0);
            // 6: Exchange Plaza
            drawTextWithBackground(g, getText(6), canvasWidth / 2, y + 3, currentFont.stringWidth(getText(6)) + 8, 2);
            // 74: Choose a region to trade with and press OK!
            bl(g, x, getText(74), y + 3 + calculateTextHeight(getText(6)) + 3, al[2], 12, 16056665, 16777215);

            drawTextWithBackground(g, getText(cM(al[3], 6)), x + 3, var4, 110, 0);

            for (var5 = 0; var5 < 7; ++var5) {
                int var6 = cM(var5, 7);
                if (al[3] == var5 && (al[2] & 4) != 0) {
                    ++var6;
                }

                drawSprite(g, var6, var3 + cM(var5, 0), var4 + cM(var5, 1), 0);
            }
        } else {
            bl(g, x, getText(74), y + 3 + calculateTextHeight(getText(6)) + 3, al[2], 12, 16056665, 16777215);
            if (al[4] != al[3]) {
                drawTextWithBackground(g, getText(cM(al[3], 6)), x + 3, var4, 110, 0);
                drawSprite(g, cM(al[4], 7), var3 + cM(al[4], 0), var4 + cM(al[4], 1), 0);
                var5 = cM(al[3], 7);
                if ((al[2] & 4) != 0) {
                    ++var5;
                }

                drawSprite(g, var5, var3 + cM(al[3], 0), var4 + cM(al[3], 1), 0);
            } else if ((al[2] & 3) == 0) {
                var5 = cM(al[3], 7);
                if ((al[2] & 4) != 0) {
                    ++var5;
                }

                drawSprite(g, var5, var3 + cM(al[3], 0), var4 + cM(al[3], 1), 0);
            }
        }

    }

    public static void exchangePlazaLoadingShip(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        setColorOfRGBInt(g, 6728679);
        g.fillRect(x, y + 114, 240, getSpriteHeight(6));
        drawSprite(g, 6, x, y + 114, 0);
        drawSprite(g, 6, x + 120, y + 114, 0);
        // 43: Landing ship...
        drawTextWithBackground(g, getText(43), canvasWidth / 2, y + 2, currentFont.stringWidth(getText(43)) + 8, 2);
        drawSprite(g, 76, canvasWidth / 2, y + 102, 2);
    }

    public static void exchangePlazaExchangeFailed(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 89: Exchanged Failed
        drawTextWithBackground(g, getText(89), canvasWidth / 2, y + 2, currentFont.stringWidth(getText(89)) + 8, 2);
        int var3 = y + 62;
        drawSprite(g, 4, x + 0, var3, 0);
        drawSprite(g, 4, x + 120, var3, 0);
        drawImage(g, downloadedImages[0], canvasWidth / 2, var3 - 12, 2);
        int var4 = (currentFontHeight + 1) * 2 + currentFontHeight;
        int var5 = y + 240 - (var4 + 4) - 2;
        // 88: Retry
        cb(g, 0, getText(88), canvasWidth / 2, var5 - 44, 100, 28, 2, 0);
        drawBeveledRect(g, (canvasWidth - 232) / 2, var5, 232, var4 + 4, 16056665, 16056665);
        setColorOfRGBInt(g, 16777215);
        // 90: No trading partners were found in that region.
        drawString(g, getText(90), canvasWidth / 2, var5 + 2, TextAlign.CENTER);
        aD(g, 30, canvasWidth / 2, var5 - 44 + getSpriteHeight(30) / 2, 100, al[2]);
    }

    public static void exchangePlazaExchangeSuccess(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 48: Exchange Success!
        drawTextWithBackground(g, getText(48), canvasWidth / 2, y + 2, currentFont.stringWidth(getText(48)) + 8, 2);
        drawImage(g, downloadedImages[0], x + 0, y + 42, 0);
        drawImage(g, downloadedImages[1], x + 120, y + 42, 0);
        int var3 = (currentFontHeight + 1) * 2 + currentFontHeight;
        int var4 = y + 240 - (var3 + 4) - 2;
        // 18: OK
        cb(g, 0, getText(18), canvasWidth / 2, var4 - 38, 100, 28, 2, 0);
        drawBeveledRect(g, (canvasWidth - 232) / 2, var4, 232, var3 + 4, 16056665, 16056665);
        setColorOfRGBInt(g, 16777215);
        drawString(g, downloadedTexts[0], canvasWidth / 2, var4 + 2, TextAlign.CENTER);
        drawString(g, downloadedTexts[1], canvasWidth / 2, var4 + 2 + currentFontHeight + 1, TextAlign.CENTER);
        drawString(g, downloadedTexts[2], canvasWidth / 2, var4 + 2 + (currentFontHeight + 1) * 2, TextAlign.CENTER);
        aD(g, 30, canvasWidth / 2, var4 - 38 + getSpriteHeight(30) / 2, 100, al[2]);
    }

    public static void exchangePlazaExchangeTicket(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);

        // 51: Exchange Ticket
        int textHeight = calculateTextHeight(getText(51));
        drawTextWithBackground(g, getText(51), canvasWidth / 2, y + 2, currentFont.stringWidth(getText(51)) + 8, 2);

        // 91: Enter the Exchange Ticket No. in your Keitama!
        bl(g, x, getText(91), y + 2 + textHeight + 2, al[2], 12, 16056665, 16777215);

        for (int i = 0; i < 2; ++i) {
            bk(g, i, ao, 0);
        }

        int var5 = y + 3 + textHeight + 3 + currentFontHeight + 12;
        bv(g, x, var5, 0, 16777215, 16730112, 16751616, 7786961);
        br(g, canvasWidth / 2, var5 + 4, 62, false, true);
        bp(g, canvasWidth / 2, var5 + 10, 56, al[2] >> 1, false);
        aD(g, 30, x + bm(ao, getSelectedButtonIdx(), 0), y + bm(ao, getSelectedButtonIdx(), 1) + bm(ao, getSelectedButtonIdx(), 3) / 2 + 1, bm(ao, getSelectedButtonIdx(), 2), al[2]);
    }

    public static void exchangePlazaSendViaIR(Graphics var0, int var1, int var2) {
        sendViaIR(var0, var1, var2);
    }

    public static void openExplanation(int idx, int var1) {
        explanationState[0] = idx;
        explanationState[1] = var1;
        explanationState[2] = 0;
        explanationState[3] = 1;
        selectSoftLabel(3);
    }

    public static void exitExplanation() {
        explanationState[3] = 0;
    }

    public static void scrollExplanation() {
        if (isKeyPressed(2097152L)) {
            exitExplanation();
        } else {
            if (isKeyPressed(1835008L)) {
                explanationState[2]++;
                if (explanationState[1] <= explanationState[2]) {
                    exitExplanation();
                }
            } else if (isKeyPressed(196608L)) {
                explanationState[2]--;
                if (explanationState[2] < 0) {
                    explanationState[2] = 0;
                }
            }

        }
    }

    public static boolean isExplanationOpen() {
        return explanationState[3] != 0;
    }

    public static void explanationPage(Graphics g, int x, int y) {
        if (isExplanationOpen()) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
            int var3 = x + 4;
            int var4 = y + (240 - (currentFontHeight + 1) * 7) / 2;
            drawBeveledRect(g, x + 2, var4 - 1, 236, (currentFontHeight + 1) * 7 + 2, 16056665, 16056665);
            String text = getText(explanationState[0] + explanationState[2]);
            int lineCount = splitCount(text, "\n");
            setColorOfRGBInt(g, 16777215);

            for (int i = 0; i < lineCount; ++i) {
                String var8 = substringBetweenDelimiters(text, i, 1, "\n");
                int var9 = var8.indexOf("$");
                if (var9 == -1) {
                    drawString(g, var8, var3, var4, TextAlign.LEFT);
                } else {
                    int var10 = var3;

                    do {
                        String var11 = var8.substring(0, var9);
                        drawString(g, var11, var10, var4, TextAlign.LEFT);
                        var10 += currentFont.stringWidth(var11);
                        if (var8.length() - 1 <= var9) {
                            break;
                        }

                        byte var12 = 0;
                        int var13 = var12 | Integer.parseInt(var8.substring(var9 + 1, var9 + 1 + 3)) << 16;
                        var13 |= Integer.parseInt(var8.substring(var9 + 4, var9 + 4 + 3)) << 8;
                        var13 |= Integer.parseInt(var8.substring(var9 + 7, var9 + 7 + 3));
                        setColorOfRGBInt(g, var13);
                        var8 = var8.substring(var9 + 10);
                        var9 = var8.indexOf("$");
                    } while (var9 != -1);

                    drawString(g, var8, var10, var4, TextAlign.LEFT);
                }

                var4 += currentFontHeight + 1;
            }

        }
    }

    public static void ao() {
        aq[6] = getSelectedButtonIdx();
        aq[7] = getAw1();
        aq[8] = getAw2();
        ag(4, true);
        setSelectedButtonIdx(0);
        ap(0, -1);
        aq[4] = getSelectedButtonIdx();
        aq[0] = 1;
        aq[5] = 0;
        selectSoftLabel(3);
        dq(0);
    }

    public static void ap(int var0, int var1) {
        aq[2] = var0;
        aq[3] = var1;
    }

    public static void dr() {
        StackMap = true;
        aq[0] = 0;
    }

    public static void dq(int var0) {
        switch (var0) {
            case 0:
            default:
                break;
            case 1:
                ag(4, true);
                setSelectedButtonIdx(aq[4]);
                break;
            case 2:
                openExplanation(aq[2], aq[3]);
                break;
            case 3:
                ag(2, true);
                setSelectedButtonIdx(1);
                break;
            case 4:
                ag(2, true);
                setSelectedButtonIdx(1);
        }

        aq[5] = 0;
        aq[1] = var0;
    }

    public static boolean isMenuOpen() {
        return aq[0] != 0;
    }

    public static void dw() {
        if (isMenuOpen()) {
            int var10002 = aq[5]++;
            switch (aq[1]) {
                case 0:
                    dq(1);
                    break;
                case 1:
                    dt();
                    break;
                case 2:
                    if (isExplanationOpen()) {
                        scrollExplanation();
                    } else {
                        dq(1);
                    }
                    break;
                case 3:
                    du();
                    break;
                case 4:
                    dv();
            }

        }
    }

    public static void dt() {
        if (isKeyPressed(2097152L)) {
            ag(aq[7], aq[8] != 0);
            setSelectedButtonIdx(aq[6]);
            selectSoftLabel(currentSoftLabelIdx);
            dr();
        } else {
            aq[4] = getSelectedButtonIdx();
            switch (aq(isKeyPressed(1048576L), isKeyPressed(131072L), isKeyPressed(524288L))) {
                case 0:
                    if (0 < aq[3]) {
                        dq(2);
                    }
                    break;
                case 1:
                    T(4);
                    break;
                case 2:
                    dq(4);
                    break;
                case 3:
                    toggleSound();
                    saveGame();
            }

        }
    }

    public static void du() {
        if (isKeyPressed(2097152L)) {
            dq(1);
        } else {
            switch (aq(isKeyPressed(1048576L), isKeyPressed(196608L), isKeyPressed(786432L))) {
                case 0:
                    dr();
                    T(4);
                    break;
                case 1:
                    dq(1);
            }

        }
    }

    public static void dv() {
        if (isKeyPressed(2097152L)) {
            dq(1);
        } else {
            switch (aq(isKeyPressed(1048576L), isKeyPressed(196608L), isKeyPressed(786432L))) {
                case 0:
                    exitGame();
                    break;
                case 1:
                    dq(1);
            }

        }
    }

    public static void drawMenuPages(Graphics g, int x, int y) {
        if (isMenuOpen()) {
            switch (aq[1]) {
                case 0:
                default:
                    break;
                case 1:
                    menuPage(g, x, y);
                    break;
                case 2:
                    if (isExplanationOpen()) {
                        explanationPage(g, x, y);
                    }
                    break;
                case 3:
                    returnToTitlePage(g, x, y);
                    break;
                case 4:
                    closeAppPage(g, x, y);
            }

        }
    }

    public static void menuPage(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        int newX = x + 120;
        int newY = y + 5;
        g.fillRect(x, y, 240, 240);
        // 54: Menu
        drawTextWithBackground(g, getText(54), newX, newY, currentFont.stringWidth(getText(54)) + 8, 2);
        // 55: Read Explanation
        drawButton(g, getText(55), 0, newY + 2 + (currentFontHeight + 6) * 2);
        // 56: Return to Title
        drawButton(g, getText(56), 1, newY + 2 + (currentFontHeight + 6) * 3);
        // 57: Close App
        drawButton(g, getText(57), 2, newY + 2 + (currentFontHeight + 6) * 4);
        // 58: Sound  ON
        // 59: Sound  OFF
        drawButton(g, getText(58 + gameSave[3]), 3, newY + 2 + (currentFontHeight + 6) * 5);

        drawMenuEggs(g, GameApp.canvasWidth / 2, newY + 2 + currentFontHeight + 6 + 2, 86, aq[5]);
    }

    public static void returnToTitlePage(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        int newX = x + 120;
        int newY = y + 5;
        g.fillRect(x, y, 240, 240);
        // 60: Return to Title?
        drawTextWithBackground(g, getText(60), newX, newY, currentFont.stringWidth(getText(60)) + 4, 2);
        // 62: Yes
        drawButton(g, getText(62), 0, newY + 2 + (currentFontHeight + 6) * 2);
        // 63: No
        drawButton(g, getText(63), 1, newY + 2 + (currentFontHeight + 6) * 3);
        drawMenuEggs(g, GameApp.canvasWidth / 2, newY + 2 + currentFontHeight + 6 + 2, 86, aq[5]);
    }

    public static void closeAppPage(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        int newX = x + 120;
        int newY = y + 5;
        g.fillRect(x, y, 240, 240);
        // 61: Close App?
        drawTextWithBackground(g, getText(61), newX, newY, currentFont.stringWidth(getText(61)) + 8, 2);
        // 62: Yes
        drawButton(g, getText(62), 0, newY + 2 + (currentFontHeight + 6) * 2);
        // 63: No
        drawButton(g, getText(63), 1, newY + 2 + (currentFontHeight + 6) * 3);
        drawMenuEggs(g, GameApp.canvasWidth / 2, newY + 2 + currentFontHeight + 6 + 2, 86, aq[5]);
    }

    public static void drawButton(Graphics g, String text, int buttonIdx, int y) {
        int textColor;
        int backgroundColor;
        if (buttonIdx == getSelectedButtonIdx()) {
            backgroundColor = 16056665;
            textColor = 16777215;
        } else {
            backgroundColor = 7786961;
            textColor = 3096512;
        }

        drawBeveledRect(g, (canvasWidth - 220) / 2, y, 220, currentFontHeight + 4, backgroundColor, backgroundColor);
        setColorOfRGBInt(g, textColor);
        drawString(g, text, canvasWidth / 2, y + 2, TextAlign.CENTER);
    }

    public static void drawMenuEggs(Graphics g, int x, int y, int var3, int var4) {
        int var5 = x - var3;

        for (int i = 0; i < 3; var5 += var3) {
            int spriteIndex = 69 + (i + (var4 >> 2) & 1);
            drawSprite(g, spriteIndex, var5 - getSpriteWidth(spriteIndex) / 2, y, 0);
            ++i;
        }

    }

    public static void aY(int var0, int var1, int var2, String var3) {
        ar[0] = var0;
        ar[1] = var1;
        ar[2] = var2;
        as = var3;
        ag(2, true);
        setSelectedButtonIdx(1);
        aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
        selectSoftLabel(6);
        ar[3] = 1;
    }

    public static void dE() {
        ar[3] = 0;
        as = null;
    }

    public static void dF(int var0) {
        O = ar[0];
        dE();
        switch (ar[0]) {
            case 7:
                aL(var0);
                break;
            case 8:
                bC(var0);
                break;
            case 9:
                cd(var0);
                break;
            case 10:
                cy(var0);
                break;
            case 11:
                cN(var0);
        }

    }

    public static boolean dG() {
        return ar[3] != 0;
    }

    public static void dH() {
        if (dG()) {
            if (isKeyPressed(524288L)) {
                at -= (currentFontHeight + 1) * 7;
            } else if (isKeyPressed(131072L)) {
                at += (currentFontHeight + 1) * 7;
            }

            if (isKeyPressed(1L)) {
                au = !au;
            }

            switch (aq(isKeyPressed(1048576L), isKeyPressed(65536L), isKeyPressed(262144L))) {
                case 0:
                    dF(ar[1]);
                    break;
                case 1:
                    dF(ar[2]);
            }

        }
    }

    public static void dI(Graphics g, int x, int y) {
        if (dG()) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
            int lineCount = splitCount(as, "\n");
            int textHeight = (lineCount - 1) * (currentFontHeight + 1) + currentFontHeight + 4;
            int var5 = y + (240 - (textHeight + 4 + 8)) / 2;
            drawBeveledRect(g, (canvasWidth - 232) / 2, var5, 232, textHeight, 16056665, 16056665);
            setColorOfRGBInt(g, 16777215);
            drawString(g, as, canvasWidth / 2, var5 + 2, TextAlign.CENTER);
            // 88: Retry
            cb(g, 0, getText(88), canvasWidth / 2 - 8, var5 + textHeight + 4, 100, 28, 1, 0);
            // 9: Back
            cb(g, 1, getText(9), canvasWidth / 2 + 8, var5 + textHeight + 4, 100, 28, 0, 0);
        }
    }

    public static String getText(int idx) {
        return texts[idx];
    }

    public static boolean loadTexts() {
        DataInputStream stream = null;
        boolean success = true;
        byte var3 = 100;

        try {
            int[] lengths = loadShortArray(128);
            int pos = 128 + (lengths.length + 1) * 2;

            int i;
            for (i = 0; i < var3; ++i) {
                pos += lengths[i];
            }

            stream = Connector.openDataInputStream("scratchpad:///0;pos=" + pos);

            for (i = 0; i < 183; ++i) {
                byte[] data = new byte[lengths[var3 + i]];
                stream.read(data);
                texts[i] = new String(data);
                Object var18 = null;
                System.gc();
            }
        } catch (Exception e) {
            success = false;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                    stream = null;
                    System.gc();
                } catch (Exception e) {
                }
            }

        }

        return success;
    }

    public static void ag(int var0, boolean var1) {
        aw[1] = var0;
        aw[2] = var1 ? 1 : 0;
        aw[13] = 0;
    }

    public static void setSelectedButtonIdx(int idx) {
        aw[0] = idx;
    }

    public static void ai(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
        aw[3] = var0;
        aw[4] = var1;
        aw[5] = var2;
        aw[6] = var3;
        aw[7] = var4;
        aw[8] = var5;
        aw[9] = var6;
        aw[10] = var7;
        aw[11] = var8;
        aw[12] = var9;
    }

    public static void aN(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        ai(var0, var1, var2, var3, 0, var4, var5, var6, var7, 0);
    }

    public static int aq(boolean var0, boolean var1, boolean var2) {
        int var3 = -1;
        if (aw[13] != 0) {
            var3 = getSelectedButtonIdx();
            aw[13] = 0;
            playSound(5, false);
        } else if (var0) {
            aw[13] = 1;
            StackMap = true;
        } else {
            boolean var4 = false;
            int var10002;
            if (var1) {
                var4 = true;
                var10002 = aw[0]--;
                if (aw[0] < 0) {
                    if (aw[2] != 0) {
                        aw[0] = aw[1] - 1;
                    } else {
                        var4 = false;
                        aw[0] = 0;
                    }
                }
            }

            if (var2) {
                var4 = true;
                var10002 = aw[0]++;
                if (aw[1] <= aw[0]) {
                    if (aw[2] != 0) {
                        aw[0] = 0;
                    } else {
                        var4 = false;
                        aw[0] = aw[1] - 1;
                    }
                }
            }

            if (var4) {
                playSound(4, false);
            }

            aw[13] = 0;
        }

        return var3;
    }

    public static int getSelectedButtonIdx() {
        return aw[0];
    }

    public static int getAw1() {
        return aw[1];
    }

    public static int getAw2() {
        return aw[2];
    }

    public static void bk(Graphics var0, int var1, int[] var2, int var3) {
        cb(var0, var1, getText(bm(var2, var1, 4)), rootX + bm(var2, var1, 0), rootY + bm(var2, var1, 1), bm(var2, var1, 2), bm(var2, var1, 3), bm(var2, var1, 5), var3);
    }

    public static void aw(Graphics var0, int var1, int[] var2, int var3) {
        dJ(var0, var1, ax(var2, var1, 4), ax(var2, var1, 5), rootX + ax(var2, var1, 0), rootY + ax(var2, var1, 1), ax(var2, var1, 2), ax(var2, var1, 3), ax(var2, var1, 6), var3);
    }

    public static int bm(int[] var0, int var1, int var2) {
        return var0[var1 * 6 + var2];
    }

    public static int ax(int[] var0, int var1, int var2) {
        return var0[var1 * 7 + var2];
    }

    public static void cb(Graphics g, int var1, String str, int x, int y, int var5, int var6, int var7, int var8) {
        int var9;
        if (var7 == 2) {
            var9 = x - var5 / 2;
        } else if (var7 == 0) {
            var9 = x;
        } else {
            var9 = x - var5;
        }

        boolean var15 = false;
        int var10;
        int var11;
        int var12;
        int var13;
        int var14;
        if (var1 == getSelectedButtonIdx()) {
            var10 = aw[3];
            var11 = aw[4];
            var12 = aw[5];
            var13 = aw[6];
            var14 = aw[7];
            var15 = aw[13] != 0;
        } else {
            var10 = aw[8];
            var11 = aw[9];
            var12 = aw[10];
            var13 = aw[11];
            var14 = aw[12];
        }

        switch (var8) {
            case 0:
                dK(g, str, var9, y, var5, var6, var10, var11, var12, var13, var14, var15);
                break;
            case 1:
                dL(g, str, var9, y, var5, var6, var10, var11, var12, var13, var14, var15);
        }

    }

    public static void dJ(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
        int var10;
        if (var8 == 2) {
            var10 = var4 - var6 / 2;
        } else if (var8 == 0) {
            var10 = var4;
        } else {
            var10 = var4 - var6;
        }

        boolean var15 = false;
        int var11;
        int var12;
        int var13;
        int var14;
        if (var1 == getSelectedButtonIdx()) {
            var11 = aw[3];
            var12 = aw[4];
            var13 = aw[7];
            var15 = aw[13] != 0;
            var14 = var2;
        } else {
            var11 = aw[8];
            var12 = aw[9];
            var13 = aw[12];
            var14 = var3;
        }

        switch (var9) {
            case 0:
                drawPillShapeWithSprite(var0, var14, var10, var5, var6, var7, var11, var12, var13, var15);
                break;
            case 1:
                dN(var0, var14, var10, var5, var6, var7, var11, var12, var13, var15);
        }

    }

    public static void dK(Graphics g, String text, int x, int y, int var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11) {
        drawPillShape(g, x, y, var4, var5, var6, var7, var10, var11);
        if (var11) {
            y += 2;
        }

        int textHeight = splitCount(text, "\n");
        textHeight = currentFontHeight + (textHeight - 1) * (currentFontHeight + 1);
        setColorOfRGBInt(g, var9);
        drawString(g, text, x + var4 / 2, y + (var5 - textHeight) / 2, TextAlign.CENTER);
    }

    public static void drawPillShapeWithSprite(Graphics g, int spriteIdx, int x, int y, int w, int h, int outlineColor, int color, int unstyledColor, boolean isStyled) {
        drawPillShape(g, x, y, w, h, outlineColor, color, unstyledColor, isStyled);
        if (isStyled) {
            y += 2;
        }

        drawSprite(g, spriteIdx, x + w / 2, y + (h - getSpriteHeight(spriteIdx)) / 2, 2);
    }

    public static void drawPillShape(Graphics g, int x, int y, int w, int h, int outlineColor, int color, int unstyledColor, boolean isStyled) {
        if (!isStyled) {
            setColorOfRGBInt(g, unstyledColor);
            g.fillArc(x - 1, y + 2 - 1, h + 2, h + 2, 180, 90);
            g.fillArc(x + w - h - 2, y + 2 - 1, h + 2, h + 2, -90, 90);
            g.fillRect(x + h / 2, y + h + 1, w - h, 2);
        } else {
            y += 2;
        }

        setColorOfRGBInt(g, outlineColor);
        g.fillArc(x - 1, y - 1, h + 2, h + 2, 90, 180);
        g.fillArc(x + w - h - 2, y - 1, h + 2, h + 2, -90, 180);
        g.fillRect(x + h / 2, y - 1, w - h, 1);
        g.fillRect(x + h / 2, y + h, w - h, 1);

        setColorOfRGBInt(g, color);
        g.fillArc(x, y, h, h, 90, 180);
        g.fillArc(x + w - h - 1, y, h, h, -90, 180);
        g.fillRect(x + h / 2, y, w - h, h);
    }

    public static void dL(Graphics g, String text, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11) {
        dP(g, var2, var3, var4, var5, var6, var7, var10, var11);
        if (var11) {
            var3 += 2;
        }

        int textHeight = splitCount(text, "\n");
        textHeight = currentFontHeight + (textHeight - 1) * (currentFontHeight + 1);
        setColorOfRGBInt(g, var9);
        drawString(g, text, var2 + var4 / 2, var3 + (var5 - textHeight) / 2, TextAlign.CENTER);
    }

    public static void dN(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9) {
        dP(var0, var2, var3, var4, var5, var6, var7, var8, var9);
        if (var9) {
            var3 += 2;
        }

        drawSprite(var0, var1, var2 + var4 / 2, var3 + (var5 - getSpriteHeight(var1)) / 2, 2);
    }

    public static void dP(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
        if (!var8) {
            drawBeveledRect(var0, var1, var2 + var4 - 2, var3, 4, var7, var7);
        } else {
            var2 += 2;
        }

        drawBeveledRect(var0, var1, var2, var3, var4, var5, var6);
    }

    public static void clearDigitEditor() {
        for (int i = 0; i < 4; ++i) {
            digitEditorState[i] = 0;
        }

        setCursorIndex(0);
    }

    public static void setCursorIndex(int index) {
        digitEditorState[5] = digitEditorState[4];
        digitEditorState[4] = index;
    }

    public static void parseAndStoreDownloadedPassword(byte[] passwordData) {
        for (int i = 0; i < 10; ++i) {
            setDigitBankA(i, passwordData[i]);
        }

    }

    public static void setDigitBankA(int digitIndex, int digit) {
        digit %= 10;
        int packedInt = digitEditorState[0 + digitIndex / 8];
        int shift = 4 * (7 - digitIndex % 8);
        packedInt &= ~(15 << shift);
        packedInt |= digit << shift;
        digitEditorState[0 + digitIndex / 8] = packedInt;
    }

    public static void setDigitBankB(int digitIndex, int digit) {
        digit %= 10;
        int packedInt = digitEditorState[2 + digitIndex / 8];
        int shift = 4 * (7 - digitIndex % 8);
        packedInt &= ~(15 << shift);
        packedInt |= digit << shift;
        digitEditorState[2 + digitIndex / 8] = packedInt;
    }

    public static void generateDerivedCodeInBankB() {
        int keyDigit = getDigitBankA(1);

        for (int i = 0; i < 10; ++i) {
            setDigitBankB(digitShuffleTable[keyDigit * 10 + i], getDigitBankA(i));
        }

    }

    public static int getDigitBankA(int digitIndex) {
        int digit = digitEditorState[0 + digitIndex / 8] >> 4 * (7 - digitIndex % 8) & 15;
        return digit;
    }

    public static int getDigitBankB(int digitIndex) {
        int digit = digitEditorState[2 + digitIndex / 8] >> 4 * (7 - digitIndex % 8) & 15;
        return digit;
    }

    public static int getCursorIndex() {
        return digitEditorState[4];
    }

    public static int getPreviousCursorIndex() {
        return digitEditorState[5];
    }

    public static boolean handleDigitEditorInput(boolean moveLeft, boolean moveRight, boolean jumpBack5, boolean jumpForward5, boolean incrementDigit, int directDigit) {
        int cursorIndex = digitEditorState[4];
        boolean allFilled = false;

        if (incrementDigit) {
            playSound(5, false);
            setDigitBankA(cursorIndex, (getDigitBankA(cursorIndex) + 1) % 10);
        } else if (moveRight) {
            ++cursorIndex;
            if (10 <= cursorIndex) {
                cursorIndex = 0;
                allFilled = true;
            }

            setCursorIndex(cursorIndex);
        } else if (moveLeft) {
            --cursorIndex;
            if (cursorIndex < 0) {
                cursorIndex = 9;
                allFilled = true;
            }

            setCursorIndex(cursorIndex);
        } else if (jumpBack5) {
            cursorIndex -= 5;
            if (cursorIndex < 0) {
                allFilled = true;
                cursorIndex += 10;
            }

            setCursorIndex(cursorIndex);
        } else if (jumpForward5) {
            cursorIndex += 5;
            if (10 <= cursorIndex) {
                allFilled = true;
                cursorIndex -= 10;
            }

            setCursorIndex(cursorIndex);
        } else if (0 <= directDigit) {
            playSound(5, false);
            setDigitBankA(cursorIndex, directDigit);
            ++cursorIndex;
            if (10 <= cursorIndex) {
                cursorIndex = 0;
                allFilled = true;
            }

            setCursorIndex(cursorIndex);
        }

        return allFilled;
    }

    public static int getPressedNumber() {
        byte number = -1;
        if (isKeyPressed(1L)) {
            number = 0;
        } else if (isKeyPressed(2L)) {
            number = 1;
        } else if (isKeyPressed(4L)) {
            number = 2;
        } else if (isKeyPressed(8L)) {
            number = 3;
        } else if (isKeyPressed(16L)) {
            number = 4;
        } else if (isKeyPressed(32L)) {
            number = 5;
        } else if (isKeyPressed(64L)) {
            number = 6;
        } else if (isKeyPressed(128L)) {
            number = 7;
        } else if (isKeyPressed(256L)) {
            number = 8;
        } else if (isKeyPressed(512L)) {
            number = 9;
        }

        return number;
    }

    public static void by(Graphics var0, int var1, int var2, int var3) {
        for (int var4 = 0; var4 < 10; ++var4) {
            bz(var0, var1, var2, var3, var4);
        }

    }

    public static void bz(Graphics var0, int var1, int var2, int var3, int var4) {
        if (var3 == 2) {
            var1 -= 35;
        } else if (var3 == 1) {
            var1 -= 71;
        }

        var1 += var4 * 15;
        if (5 <= var4) {
            var2 += 26;
            var1 -= 75;
        }

        drawString(var0, "" + getDigitBankA(var4), var1, var2, TextAlign.LEFT);
    }

    public static void cc(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        int var8 = var6 % 7;
        if (var8 < 0) {
            var8 += 7;
        }

        for (int var9 = 0; var9 < var7; ++var9) {
            setColorOfRGBInt(var0, az[var8]);
            var0.fillArc(var1, var2, var5 * 2, var5 * 2, 0, 360);
            ++var8;
            var8 %= 7;
            var1 += var3;
            var2 += var4;
        }

    }

    public static void dS(Graphics var0, byte[] var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
        int var8 = (var1[var2 + 0] & 255) + 7 >>> 3;
        int var9 = var1[var2 + 1] & 255;
        setColorOfRGB(var0, var4 >>> 16 & 255, var4 >>> 8 & 255, var4 & 255);
        var0.setClip(0, 0, canvasWidth + 16, canvasHeight + 16);
        var0.fillRect(canvasWidth, canvasHeight, var3, var3);
        int var10 = var2 + 2;
        int var11;
        if (var7) {
            var11 = -var3;
            var5 += var3 * ((var1[var2 + 0] & 255) - 1);
        } else {
            var11 = var3;
        }

        for (int var12 = 0; var12 < var9; ++var12) {
            int var13 = var5;

            for (int var14 = 0; var14 < var8; ++var14) {
                for (int var15 = 0; var15 < 8; ++var15) {
                    if ((var1[var10] >>> 7 - var15 & 1) != 0) {
                        var0.fillRect(var13, var6, var3, var3);
                    }

                    var13 += var11;
                }

                ++var10;
            }

            var6 += var3;
        }

    }

    public static void dT(Graphics var0, byte[] var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
        int var8 = (var1[var2 + 0] & 255) + 7 >>> 3;
        int var9 = var1[var2 + 1] & 255;
        setColorOfRGB(var0, var4 >>> 16 & 255, var4 >>> 8 & 255, var4 & 255);
        int var10 = var2 + 2;
        int var11;
        if (var7) {
            var11 = -var3;
            var5 += var3 * ((var1[var2 + 0] & 255) - 1);
        } else {
            var11 = var3;
        }

        for (int var12 = 0; var12 < var9; ++var12) {
            int var13 = var5;

            for (int var14 = 0; var14 < var8; ++var14) {
                for (int var15 = 0; var15 < 8; ++var15) {
                    if ((var1[var10] >>> 7 - var15 & 1) != 0) {
                        var0.fillRect(var13, var6, var3, var3);
                    }

                    var13 += var11;
                }

                ++var10;
            }

            var6 += var3;
        }

    }

    public static void setDigitSentToServer(int index, int digit) {
        digitsSentToServer[index] = (byte) digit;
    }

    public static void setSomeFlagSentToServer() {
        setDigitSentToServer(0, 16);
    }

    public static DataInputStream sendDigitsToServer(int length) throws Exception {
        return sendDataToServer(digitsSentToServer, length);
    }

    public static DataInputStream sendDataToServer(byte[] data, int length) throws Exception {
        String encodedData = urlEncodeData(data, length);
        String url = "http://tamapark.gs.keitaiarchive.org/cgi-bin/iaserver.cgi?uid=NULLGWDOCOMO&data=" + encodedData;
        log("senddata:" + url);

        DataInputStream input = null;
        HttpConnection http = null;
        DataOutputStream output = null;

        try {
            http = (HttpConnection) Connector.open(url, 1, true);
            http.setRequestMethod("GET");
            http.connect();
            input = http.openDataInputStream();
            byte[] buffer = new byte[1024];
            output = Connector.openDataOutputStream("scratchpad:///0;pos=85258");

            int bytesToWrite;

            for (int remainingBytes = (int) http.getLength(); 0 < remainingBytes; remainingBytes -= bytesToWrite) {
                if (remainingBytes < buffer.length) {
                    bytesToWrite = remainingBytes;
                } else {
                    bytesToWrite = buffer.length;
                }

                log("dlsize:" + remainingBytes + " writeSize:" + bytesToWrite);
                input.read(buffer, 0, bytesToWrite);
                output.write(buffer, 0, bytesToWrite);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
            }

            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
            }

            try {
                if (http != null) {
                    http.close();
                }
            } catch (Exception e) {
            }

        }

        return Connector.openDataInputStream("scratchpad:///0;pos=85258");
    }

    public static String urlEncodeData(byte[] data, int length) throws Exception {
        boolean uppercase = false;
        int outputLength;
        if (uppercase) {
            outputLength = length * 2;
        } else {
            outputLength = length * 3;
        }

        byte[] output = new byte[outputLength];
        int outputIdx = 0;

        for (int i = 0; i < length; ++i) {
            if (!uppercase) {
                output[outputIdx] = 37;
                ++outputIdx;
            }

            int byteValue = data[i] & 255;

            for (int nibbleIndex = 1; nibbleIndex >= 0; --nibbleIndex) {
                byte hexDigit = (byte) (byteValue >> 4 * nibbleIndex & 15);
                if (hexDigit >= 10) {
                    hexDigit = (byte) (hexDigit - 10 + 65);
                } else {
                    hexDigit = (byte) (hexDigit + 48);
                }

                output[outputIdx] = hexDigit;
                ++outputIdx;
            }
        }

        return new String(output);
    }

    public static String readString(DataInputStream inputStream, int length) throws Exception {
        byte[] buffer = new byte[length];
        inputStream.read(buffer);
        String result = new String(buffer);
        inputStream = null;
        System.gc();
        return result;
    }

    public static Image readImage(DataInputStream inputStream, int expectedLength) throws Exception {
        aB = 0L; // bytesReadInfo ?
        aB |= (long) expectedLength;
        byte[] imageData = new byte[expectedLength];

        int bytesRead;
        for (bytesRead = 0; bytesRead < expectedLength; ++bytesRead) {
            int readValue = inputStream.read();
            if (readValue == -1) {
                break;
            }

            imageData[bytesRead] = (byte) readValue;
        }

        aB |= (long) bytesRead << 32;
        MediaImage mediaImage = MediaManager.getImage(imageData);
        mediaImage.use();
        Image image = mediaImage.getImage();
        imageData = null;
        System.gc();
        return image;
    }

    public static int countQuotedSegments(String text) {
        boolean insideQuote = false;
        int searchIndex = 0;
        int segmentCount = 0;

        while (true) {
            searchIndex = text.indexOf(39, searchIndex);
            if (searchIndex == -1) {
                if (insideQuote) {
                    log("Dialogue error: missing closing tag");
                }

                log("wordsCnt:" + segmentCount);
                return segmentCount;
            }

            log("numindex:" + searchIndex);
            if (insideQuote) {
                ++segmentCount;
                insideQuote = false;
            } else {
                insideQuote = true;
            }

            ++searchIndex;
        }
    }

    public static String findNthQuote(String text, int n) {
        int currentIndex = 0;
        boolean insideQuote = false;
        int wordStart = 0;
        int searchIndex = 0;

        String result;
        while (true) {
            searchIndex = text.indexOf(39, searchIndex);
            if (searchIndex == -1) {
                result = "";
                break;
            }

            if (insideQuote) {
                if (n == currentIndex) {
                    result = text.substring(wordStart, searchIndex);
                    break;
                }

                ++currentIndex;
                ++searchIndex;
                insideQuote = false;
            } else {
                insideQuote = true;
                ++searchIndex;
                wordStart = searchIndex;
            }
        }

        return result;
    }

    public static void unknownOperationOnServerResponse(DataInputStream var0) throws Exception {
        // Maybe just a decompilation artifact?
        // Original: public static void aW(DataInputStream var0) throws Exception
    }

    public static IrRemoteControlFrame[] createIrRemoteControlFrames(int n) {
        IrRemoteControlFrame[] res = new IrRemoteControlFrame[n];

        for (int i = 0; i < n; ++i) {
            res[i] = new IrRemoteControlFrame();
        }

        return res;
    }

    public static void aO(int var0, int var1, int var2, int var3) {
        aC[2] = var0;
        aC[3] = var1;
        aC[4] = var2;
        aC[5] = var3;
        dX(1);
    }

    public static void dX(int var0) {
        switch (var0) {
            case 0:
                irRemoteControl.stop();
                break;
            case 1:
                selectSoftLabel(6);
                ag(1, false);
                setSelectedButtonIdx(0);
                aN(16777215, 7786961, 6594720, 16777215, 16777215, 6594720, 16763955, 13158600);
                break;
            case 2:
                selectSoftLabel(6);
                ag(1, false);
                setSelectedButtonIdx(0);
                break;
            case 3:
                selectSoftLabel(1);
                ag(3, true);
                setSelectedButtonIdx(0);
                break;
            case 4:
                irRemoteControl.stop();
                selectSoftLabel(6);
                ag(1, false);
                setSelectedButtonIdx(0);
                break;
            case 5:
                irRemoteControl.stop();
                selectSoftLabel(6);
                ag(1, false);
                setSelectedButtonIdx(0);
        }

        aC[1] = 0;
        aC[0] = var0;
    }

    public static void eb() {
        generateDerivedCodeInBankB();
        dY(0, 96);
        dY(1, 8);
        dY(2, 0);
        dY(3, 0);

        int var1;
        for (int var0 = 0; var0 < 10; var0 += 2) {
            var1 = dZ(getDigitBankB(var0)) << 4 | dZ(getDigitBankB(var0 + 1));
            dY(4 + var0 / 2, var1);
        }

        dY(9, 0);
        dY(10, 0);
        dY(11, 0);
        dY(12, 0);
        dY(13, 0);
        dY(14, 0);
        var1 = 0;

        for (int var2 = 0; var2 < 15; ++var2) {
            var1 += ea(aE[var2]);
        }

        dY(15, ea(var1 & 255));
    }

    public static void dY(int var0, int var1) {
        aE[var0] = (byte) var1;
    }

    public static int dZ(int var0) {
        return (var0 & 1) << 3 | (var0 & 2) << 1 | (var0 & 4) >> 1 | (var0 & 8) >> 3;
    }

    public static int ea(int var0) {
        return (var0 & 1) << 7 | (var0 & 2) << 5 | (var0 & 4) << 3 | (var0 & 8) << 1 | (var0 & 16) >> 1 | (var0 & 32) >> 3 | (var0 & 64) >> 5 | (var0 & 128) >> 7;
    }

    public static boolean bc() {
        return aC[0] != 0;
    }

    public static void bd() {
        if (bc()) {
            int var10002 = aC[1]++;
            switch (aC[0]) {
                case 1:
                    sendIrFrames();
                    break;
                case 2:
                    ed();
                    break;
                case 3:
                    ee();
                    break;
                case 4:
                case 5:
                    ef();
            }

        }
    }

    public static void sendIrFrames() {
        try {
            irRemoteControl.stop();
            eb();
            irRemoteControl.setCarrier(131, 131);
            irRemoteControl.setCode0(0, 470, 730);
            irRemoteControl.setCode1(0, 470, 1330);
            irFrames[0].setFrameData(aE, 128);
            irFrames[0].setRepeatCount(3);
            irFrames[0].setFrameDuration(2500);
            irFrames[0].setStartHighDuration(9600);
            irFrames[0].setStartLowDuration(2400);
            irFrames[0].setStopHighDuration(1200);
            irRemoteControl.send(1, irFrames);
            aG = (new Date()).getTime();
            dX(2);
        } catch (Exception e) {
            dX(5);
        }

    }

    public static void ed() {
        if (aq(isKeyPressed(1048576L), false, false) != -1) {
            dX(4);
        } else {
            if (750L < (new Date()).getTime() - aG) {
                irRemoteControl.stop();
                playSound(6, false);
                dX(3);
            }

        }
    }

    public static void ee() {
        if (isKeyPressed(2097152L)) {
            ao();
            ap(aC[3], aC[4]);
        } else {
            switch (aq(isKeyPressed(1048576L), isKeyPressed(131072L), isKeyPressed(524288L))) {
                case 0:
                    if (aC[2] == 7) {
                        dX(0);
                        aL(0);
                    } else {
                        dX(0);
                        T(4);
                    }
                    break;
                case 1:
                    dX(0);
                    break;
                case 2:
                    dX(1);
            }

        }
    }

    public static void ef() {
        if (aq(isKeyPressed(1048576L), false, false) != -1) {
            dX(0);
        }

    }

    public static void sendViaIR(Graphics g, int x, int y) {
        if (bc()) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
            switch (aC[0]) {
                case 2:
                    sendViaIR_Sending(g, x, y);
                    break;
                case 3:
                    sendViaIR_SendingComplete(g, x, y);
                    break;
                case 4:
                    sendViaIR_Interrupted(g, x, y);
                    break;
                case 5:
                    sendViaIR_TransmissionFailed(g, x, y);
            }

        }
    }

    public static void sendViaIR_Sending(Graphics g, int x, int y) {
        // 98: Sending...
        drawTextWithBackground(g, getText(98), canvasWidth / 2, y + 5, currentFont.stringWidth(getText(98)) + 8, 2);
        drawSprite(g, 67, canvasWidth / 2 + (aC[1] * 8 - 60), y + 50, 2);
        // 38: End
        cb(g, 0, getText(38), canvasWidth / 2, y + 50 + getSpriteHeight(67) + 10, currentFont.stringWidth(getText(38)) + 8, currentFontHeight + 4, 2, 0);
        // 18: OK
        aD(g, aC[5], canvasWidth / 2, y + 50 + getSpriteHeight(67) + 10 + (currentFontHeight + 4) / 2, currentFont.stringWidth(getText(18)) + 20, aC[1]);
    }

    public static void sendViaIR_SendingComplete(Graphics g, int x, int y) {
        // 99: Sending complete!
        drawTextWithBackground(g, getText(99), canvasWidth / 2, y + 5, currentFont.stringWidth(getText(99)) + 8, 2);
        drawSprite(g, 68, canvasWidth / 2, y + 50, 2);
        int[] var3;
        if (aC[2] == 7) {
            var3 = aH;
        } else {
            var3 = aI;
        }

        for (int i = 0; i < 3; ++i) {
            bk(g, i, var3, 0);
        }

        aD(g, aC[5], x + bm(var3, getSelectedButtonIdx(), 0), y + bm(var3, getSelectedButtonIdx(), 1) + bm(var3, getSelectedButtonIdx(), 3) / 2, bm(var3, getSelectedButtonIdx(), 2) - 10, aC[1]);
    }

    public static void sendViaIR_Interrupted(Graphics g, int x, int y) {
        // 96: Interrupted...
        drawTextWithBackground(g, getText(96), canvasWidth / 2, y + 5, currentFont.stringWidth(getText(96)) + 8, 2);
        ek(g, x, y);
    }

    public static void sendViaIR_TransmissionFailed(Graphics g, int x, int y) {
        // 97: Transmission failed
        drawTextWithBackground(g, getText(97), canvasWidth / 2, y + 5, currentFont.stringWidth(getText(97)) + 8, 2);
        ek(g, x, y);
    }

    public static void ek(Graphics g, int x, int y) {
        drawSprite(g, 29, canvasWidth / 2, y + 50, 2);
        // 18: OK
        cb(g, 0, getText(18), canvasWidth / 2, y + 50 + getSpriteHeight(29) + 10, currentFont.stringWidth(getText(18)) + 8, currentFontHeight + 4, 2, 0);
        aD(g, aC[5], canvasWidth / 2, y + 50 + getSpriteHeight(29) + 10 + (currentFontHeight + 4) / 2, currentFont.stringWidth(getText(18)) + 20, aC[1]);
    }

    public static void exitGame() {
        running = false;
    }

    public static void T(int var0) {
        T = true;
        switch (O) {
            case 8:
                clearDownloadedImagesAndTexts();
                break;
            case 9:
                clearGotchiKingImages();
                break;
            case 10:
                cA();
                break;
            case 11:
                clearDownloadedTextsAndImages();
        }

        switch (var0) {
            case 2:
                w = 0;
                break;
            case 3:
                w = 0;
                break;
            case 4:
                // Resources loaded successfully
                selectSoftLabel(1);
                T = false;
                ak();
                break;
            case 5:
                selectSoftLabel(1);
                az();
                break;
            case 6:
                selectSoftLabel(1);
                aH();
                break;
            case 7:
                selectSoftLabel(1);
                aM();
                break;
            case 8:
                selectSoftLabel(1);
                bD();
                break;
            case 9:
                selectSoftLabel(1);
                ce();
                break;
            case 10:
                selectSoftLabel(1);
                cz();
                break;
            case 11:
                selectSoftLabel(1);
                cO();
                break;
            default:
                // Failed to load resources
                T = false;
                selectSoftLabel(6);
        }

        dr();
        exitExplanation();
        dE();
        O = var0;
        Exceptions = false;
        StackMap = true;
    }

    public static void b() {
        o();
        if (isKeyPressed(1024L)) {
            toggleSound();
            saveGame();
        }

        if (isMenuOpen()) {
            dw();
        } else if (dG()) {
            dH();
        } else {
            switch (O) {
                case -3:
                case -2:
                case -1:
                    exitGameOnSelect();
                    break;
                case 0:
                    if (--aJ <= 0) {
                        gameSave[4] = 3;
                        loadGameSave();
                        T(1);
                    }
                    break;
                case 1:
                    T(2);
                    break;
                case 2:
                    checkGameData();
                    break;
                case 3:
                    loadResources();
                    break;
                case 4:
                    an();
                    break;
                case 5:
                    aB();
                    break;
                case 6:
                    aJ();
                    break;
                case 7:
                    aT();
                    break;
                case 8:
                    bK();
                    break;
                case 9:
                    gotchiKingFlow();
                    break;
                case 10:
                    cF();
                    break;
                case 11:
                    cY();
                    break;
                default:
                    if (isKeyPressed(1048576L)) {
                        running = false;
                    }
            }

        }
    }

    public static void Exceptions(Graphics g) {
        try {
            if (isMenuOpen()) {
                drawMenuPages(g, rootX, rootY);
                return;
            }

            if (dG()) {
                dI(g, rootX, rootY);
                return;
            }

            switch (O) {
                case -3:
                    showError(g, "Authenticating", rootX, rootY);
                    break;
                case -2:
                    showError(g, "Communicating", rootX, rootY);
                    break;
                case -1:
                    showError(g, "Preparing", rootX, rootY);
                case 0:
                case 1:
                default:
                    break;
                case 2:
                    downloadingPage(g, rootX, rootY);
                    break;
                case 3:
                    ad(g, rootX, rootY);
                    break;
                case 4:
                    au(g, rootX, rootY);
                    break;
                case 5:
                    aF(g, rootX, rootY);
                    break;
                case 6:
                    travelModePage(g, rootX, rootY);
                    break;
                case 7:
                    shoppingCenterPage(g, rootX, rootY);
                    break;
                case 8:
                    parentCallPage(g, rootX, rootY);
                    break;
                case 9:
                    gotchiKingPage(g, rootX, rootY);
                    break;
                case 10:
                    travelMemoryPage(g, rootX, rootY);
                    break;
                case 11:
                    exchangePlazaPage(g, rootX, rootY);
            }
        } catch (Exception e) {
            log("disp error" + e.toString());
        }

    }

    public static void clearOutsideGameArea(Graphics g) {
        setColorOfRGB(g, 255, 255, 255);
        if (rootX > 0) {
            g.fillRect(0, 0, rootX, canvasHeight);
            g.fillRect(rootX + 240, 0, rootX, canvasHeight);
        }

        if (rootY > 0) {
            g.fillRect(0, 0, GameApp.canvasWidth, rootY);
            g.fillRect(0, rootY + 240, GameApp.canvasWidth, rootY);
        }

    }

    public void mediaAction(MediaPresenter source, int type, int param) {
        if (type == 3) {
            StackMap(loopedSoundIdx, true);
        }

    }

    public void resume() {
        try {
            timer.stop();
            startTimerWithRetry();
        } catch (Exception ex) {
        }

        try {
            Thread.sleep(50L);
        } catch (InterruptedException ex) {
        }

        Z();
        Z = true;
        StackMap = true;
    }

    public void timerExpired(Timer timer) {
        try {
            GameApp.timer.stop();
            if (!this.c) {
                this.c = true;
                if (Code == 0) {
                    updateInputState();
                    a();
                    b();
                    if ((d & 10) == 0) {
                        System.gc();
                    }

                    rand(2);
                    ++d;
                    Code = 1;
                }

                if (Code == 1) {
                    refresh();
                }

                if (!running) {
                    IApplication.getCurrentApp().terminate();
                }

                this.c = false;
            }

            startTimerWithRetry();
        } catch (Exception ex) {
        }

    }

    public void initCanvas() {
        canvas = new GameScreen(this);
        canvas.setBackground(Graphics.getColorOfName(0));
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        rootX = (canvasWidth - 240) / 2;
        rootY = (canvasHeight - 240) / 2;
        k = false;
        Display.setCurrent(canvas);
    }

    public void start() {
    }
}
