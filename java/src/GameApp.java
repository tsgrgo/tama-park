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

public class GameApp extends IApplication implements TimerListener, MediaListener {
    // @formatter:off
    private static final int PAGE_AUTH_ERROR      = -3;
    private static final int PAGE_COM_ERROR       = -2;
    private static final int PAGE_PREP_ERROR      = -1;
    private static final int PAGE_NONE_0          = 0;
    private static final int PAGE_NONE_1          = 1;
    private static final int PAGE_DOWNLOADING     = 2;
    private static final int PAGE_LOADING         = 3;
    private static final int PAGE_TITLE           = 4;
    private static final int PAGE_MAILBOX_MODE    = 5;
    private static final int PAGE_TRAVEL_MODE     = 6;
    private static final int PAGE_SHOPPING_CENTER = 7;
    private static final int PAGE_PARENT_CALL     = 8;
    private static final int PAGE_GOTCHI_KING     = 9;
    private static final int PAGE_TRAVEL_MEMORY   = 10;
    private static final int PAGE_EXCHANGE_PLAZA  = 11;

    private static final int SOFT_LABEL_START = 0;
    private static final int SOFT_LABEL_MENU  = 1;
    private static final int SOFT_LABEL_CLOSE = 2;
    private static final int SOFT_LABEL_BACK  = 3;
    private static final int SOFT_LABEL_TITLE = 4;
    private static final int SOFT_LABEL_HELP  = 5;
    private static final int SOFT_LABEL_EMPTY = 6;

    private static final int ALIGN_LEFT   = 0;
    private static final int ALIGN_RIGHT  = 1;
    private static final int ALIGN_CENTER = 2;

    private static final long KEY_0 = 1L;
    private static final long KEY_1 = 2L;
    private static final long KEY_2 = 4L;
    private static final long KEY_3 = 8L;
    private static final long KEY_4 = 16L;
    private static final long KEY_5 = 32L;
    private static final long KEY_6 = 64L;
    private static final long KEY_7 = 128L;
    private static final long KEY_8 = 256L;
    private static final long KEY_9 = 512L;
    private static final long KEY_ASTERISK = 1024L; // '*'
    private static final long KEY_POUND    = 2048L; // '#'
    private static final long KEY_LEFT     = 65536L;
    private static final long KEY_UP       = 131072L;
    private static final long KEY_RIGHT    = 262144L;
    private static final long KEY_DOWN     = 524288L;
    private static final long KEY_SELECT   = 1048576L;
    private static final long KEY_SOFT1    = 2097152L;
    private static final long KEY_SOFT2    = 4194304L;
    private static final long KEY_UP_LEFT           = 196608L;
    private static final long KEY_DOWN_RIGHT        = 786432L;
    private static final long KEY_DOWN_RIGHT_SELECT = 1835008L;
    // @formatter:on

    public static int drawState; // 0: Idle, 2: Request pending, 3: Currently drawing
    public static boolean running;
    public static boolean resumedDraw;
    public static int fps;
    public static boolean drawOnNextPaint;
    public static boolean fullDraw;
    public static boolean fullDrawOnNextPaint;
    public static int loadGameSaveDelay;
    public static Timer timer;
    public static GameApp mediaListener;
    public static Canvas canvas;
    public static int canvasWidth;
    public static int canvasHeight;
    public static int rootX;
    public static int rootY;
    public static Font currentFont;
    public static int currentFontHeight;
    public static int garbageCollectTimer;
    public static int gameDataVersion;
    public static boolean inputStateFlag;
    public static int currentPage;
    public static boolean travelMemoryDebug;
    public static long totalMemory;
    public static AudioPresenter[] audioPresenters;
    public static MediaSound[] mediaSounds;
    public static int loopedSoundId;
    public static int previousMusicId;
    public static boolean shouldRestartMusic;
    public static boolean previousMusicParam;
    public static long[] inputState;
    public static int keyHeldTime;
    public static int timeSinceLastInput;
    public static int[] systemAttributeState;
    public static int[] gameSave; // [?, resDownloaded, gameDataVersion, isSoundEnabled, ? ..]
    public static int currentFontIdx;
    public static int rngState;
    public static int previousSoftLabelIdx;
    public static int currentSoftLabelIdx;
    public static String[] softLabels;
    public static int loadingProgress;
    public static Image[] images;
    public static int[] imageSizes;
    public static int[] titleScreenState; // [?, animationFinished, ...]
    public static int[] titleScreenLayout;
    public static int[] mailboxModeState;
    public static int[] mailboxModeLayout;
    public static int[] travelModeState;
    public static int[] travelModeLayout;
    public static int[] shoppingCenterState; // [itemType, ...]
    public static int[] shoppingCenterLayoutTable;
    public static int[] shoppingCenterItemColors;
    public static int[] itemTicketLayout;
    public static int[] parentCallState;
    public static int[] allowanceTicketLayout;
    public static Image[] parentCallImages;
    public static String parentCallText;
    public static String parentCallQuote;
    public static int[] gotchiKingState;
    public static int[] gotchiKingInviteTicketLayout;
    public static Image[] gotchiKingImages;
    public static int[] imagesToTemporarilyDispose;
    public static int[] travelMemoryState; // [?, flowStep, ...]
    public static int[] memoryPhotoLayout;
    public static String[] travelMemoryTexts;
    public static Image travelMemoryPhoto;
    public static int[] exchangePlazaState; // [?, ?, colorIdx?, ...]
    public static int[] exchangeTicketLayout;
    public static Image[] exchangePlazaImages;
    public static String[] exchangePlazaTexts;
    public static int[] regionSelectLayout;
    public static int[] explanationState; // [index, numberOfPages, current, isOpen]
    public static int[] menuState; // [isMenuOpen, menuPage, explanationIndex, numberOfExplanationPages...]
    public static int[] errorState; // [pageToGoBack, flowStepToGoBack, flowStepToGoBack, showErrorPage] ? = some action id?
    public static String errorPageText;
    public static long imageReadInfo;
    public static boolean errorPageUnusedToggle;
    public static int errorPageUnusedCounter;
    public static String[] texts;
    public static int[] buttonState; // [0: selectedButtonIdx, 1: numberOfButtons, 2: canLoopAround, 3: selectedOutlineColor, 4: selectedColor, 5, 6: selectedTextColor, 7: selectedShadowColor, 8: outlineColor, 9: color, 10, 11: textColor, 12: shadowColor, 13: isPressed]
    public static int[] codeInputState;
    public static int[] digitShuffleTable;
    public static int[] rainbowColors;
    public static byte[] bytesSentToServer;
    public static byte[] bytesToSendViaIr;
    public static IrRemoteControlFrame[] irFrames;
    public static IrRemoteControl irRemoteControl;
    public static int[] irState; // [transmissionState, ?, currentPage, ...]
    public static long irSendTimestamp;
    public static int[] generalIrSendLayout;
    public static int[] shoppingCenterIrSendLayout;
    public boolean executingTimerExpired;

    static {
        running = true;
        resumedDraw = false;
        drawOnNextPaint = false;
        fullDraw = false;
        loadGameSaveDelay = 8;
        garbageCollectTimer = 0;
        gameDataVersion = 0;
        inputStateFlag = false;
        currentPage = 0;
        totalMemory = Runtime.getRuntime().totalMemory();

        audioPresenters = new AudioPresenter[2];
        loopedSoundId = -1;
        previousMusicId = -1;
        inputState = new long[7];
        keyHeldTime = 0;
        timeSinceLastInput = 0;
        systemAttributeState = new int[2];
        gameSave = new int[7];
        rngState = 0;
        previousSoftLabelIdx = 6;
        currentSoftLabelIdx = 6;
        softLabels = new String[]{"Start", "Menu", "Close", "Back", "Title", "Help", ""};
        images = new Image[93];
        titleScreenState = new int[5];
        titleScreenLayout = new int[]{
                120, 176, 186, 26, 26, 25, 2,
                120, 208, 186, 26, 16, 15, 2
        };
        mailboxModeState = new int[3];
        mailboxModeLayout = new int[]{
                120, 132, 230, 26, 20, 19, 2,
                120, 168, 230, 26, 18, 17, 2,
                120, 204, 230, 26, 11, 10, 2
        };
        travelModeState = new int[3];
        travelModeLayout = new int[]{
                120, 146, 220, 26, 22, 21, 2,
                120, 186, 220, 26, 14, 13, 2
        };
        shoppingCenterState = new int[4];
        shoppingCenterLayoutTable = new int[]{
                120, 202, 220, 24, 14, 2,
                120, 168, 220, 24, 13, 2,
                120, 134, 220, 24, 12, 2,
                120, 100, 220, 24, 11, 2,
                120, 66, 220, 24, 10, 2
        };
        shoppingCenterItemColors = new int[]{11025351, 1648446, 11025351, 1648446, 7053048, 1648446, 7053048, 1648446, 10873427, 2323575, 10873427, 2323575, 16777041, 16734720, 16777041, 16734720, 16021161, 16777215, 16021161, 16777215};
        itemTicketLayout = new int[]{
                120, 144, 190, 28, 93, 2,
                120, 176, 190, 28, 16, 2,
                120, 208, 190, 28, 15, 2
        };
        parentCallState = new int[7];
        allowanceTicketLayout = new int[]{
                120, 165, 170, 28, 93, 2,
                120, 198, 170, 28, 15, 2
        };
        parentCallImages = new Image[2];
        gotchiKingState = new int[6];
        gotchiKingInviteTicketLayout = new int[]{
                120, 165, 170, 28, 93, 2,
                120, 198, 170, 28, 15, 2
        };
        gotchiKingImages = new Image[2];
        imagesToTemporarilyDispose = new int[]{58, 59, 60, 71, 89, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 72, 89};
        travelMemoryState = new int[4];
        memoryPhotoLayout = new int[]{
                120, 165, 170, 28, 15, 2,
                120, 198, 170, 28, 35, 2
        };
        travelMemoryTexts = new String[2];
        exchangePlazaState = new int[5];
        exchangeTicketLayout = new int[]{
                120, 165, 170, 28, 93, 2,
                120, 198, 170, 28, 15, 2
        };
        exchangePlazaImages = new Image[2];
        exchangePlazaTexts = new String[3];
        regionSelectLayout = new int[]{
                0, 0, 1, 6, 6, 1, 80, 41,
                0, 28, 2, 0, 0, 2, 81, 43,
                0, 64, 3, 1, 1, 3, 82, 45,
                -44, 44, 4, 2, 2, 4, 83, 47,
                -64, 60, 5, 3, 3, 5, 84, 49,
                -108, 60, 6, 4, 4, 6, 85, 51,
                -164, 60, 0, 5, 5, 0, 86, 53
        };
        explanationState = new int[4];
        menuState = new int[9];
        errorState = new int[4];
        texts = new String[183];
        buttonState = new int[14];
        codeInputState = new int[6];
        digitShuffleTable = new int[]{3, 8, 4, 0, 1, 5, 6, 7, 2, 9, 3, 8, 4, 0, 1, 5, 6, 7, 2, 9, 4, 8, 1, 6, 2, 3, 9, 5, 0, 7, 4, 8, 1, 6, 2, 3, 9, 5, 0, 7, 5, 8, 0, 6, 2, 7, 3, 9, 1, 4, 5, 8, 0, 6, 2, 7, 3, 9, 1, 4, 5, 8, 7, 1, 6, 3, 0, 2, 9, 4, 5, 8, 7, 1, 6, 3, 0, 2, 9, 4, 6, 8, 5, 3, 0, 9, 7, 2, 4, 1, 6, 8, 5, 3, 0, 9, 7, 2, 4, 1};
        rainbowColors = new int[]{15947864, 16777041, 10873427, 7053048, 16777215, 11025351, 16021161};
        bytesSentToServer = new byte[14];
        bytesToSendViaIr = new byte[16];
        irFrames = createIrRemoteControlFrames(1);
        irRemoteControl = IrRemoteControl.getIrRemoteControl();
        irState = new int[6];
        generalIrSendLayout = new int[]{
                120, 144, 170, 28, 15, 2,
                120, 176, 170, 28, 94, 2,
                120, 208, 170, 28, 95, 2
        };
        shoppingCenterIrSendLayout = new int[]{
                120, 144, 170, 28, 16, 2,
                120, 176, 170, 28, 94, 2,
                120, 208, 170, 28, 95, 2
        };
    }

    public GameApp() {
        mediaListener = this;
        String[] args = this.getArgs();
        this.initCanvas();

        try {
            PhoneSystem.setAttribute(0, 1);
        } catch (Exception ignored) {
        }

        setCurrentFont(2);
        fps = 8;
        timer = new Timer();
        timer.setRepeat(true);
        timer.setTime(1000 / fps);
        timer.setListener(this);
        timer.start();
    }

    public static void repaint() {
        drawState = 2;
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
                } catch (Exception ignored) {
                }

                try {
                    Thread.sleep(1000L);
                } catch (Exception ignored) {
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

    public static void playSoundInternal(int soundIdx, int presenterIdx, boolean flag) {
        stopSound(presenterIdx);

        try {
            if (gameSave[3] == 0) {
                audioPresenters[presenterIdx].setSound(mediaSounds[soundIdx]);
                if (flag) {
                    // Not sure why this is empty...
                }

                audioPresenters[presenterIdx].play();
            }
        } catch (Exception e) {
            log("playsound:" + soundIdx + " " + e);
        }

    }

    public static void playSound(int soundIdx, boolean flag) {
        playSoundInternal(soundIdx, flag ? 0 : 1, flag);
    }

    public static void stopSound(int presenterIdx) {
        try {
            Thread.sleep(100L);
            audioPresenters[presenterIdx].stop();
        } catch (Exception ignored) {
        }

    }

    public static void stopAllSounds() {
        for (int i = 0; i < audioPresenters.length; ++i) {
            stopSound(i);
        }

    }

    public static void playMusic(int musicId, boolean musicParam) {
        if (gameSave[3] == 0 && musicId >= 0) {
            stopSound(0);
            playSoundInternal(musicId, 0, musicParam);
        }

        loopedSoundId = musicId;
        previousMusicId = musicId;
        previousMusicParam = musicParam;
    }

    public static void checkMusic() {
        if (shouldRestartMusic) {
            restartMusic();
            shouldRestartMusic = false;
        }

    }

    public static void restartMusicOnNext() {
        shouldRestartMusic = true;
    }

    public static void toggleSound() {
        gameSave[3] = 1 - gameSave[3];
        stopAllSounds();
        restartMusic();
    }

    public static void restartMusic() {
        if (previousMusicParam) {
            playMusic(previousMusicId, previousMusicParam);
        }

    }

    public static boolean loadSounds() {
        DataInputStream stream = null;
        int currentIndex = 0;

        boolean success;
        try {
            mediaSounds = new MediaSound[7];
            int[] sizes = loadShortArray(128);
            int pos = 0;

            for (int i = 0; i < 93; ++i) {
                pos += sizes[i];
            }

            stream = Connector.openDataInputStream("scratchpad:///0;pos=" + (pos + 128 + 568));

            for (int i = 0; i < 7; ++i) {
                currentIndex = i;
                byte[] data = new byte[sizes[i + 93]];
                stream.read(data);

                for (int j = 0; j < data.length; ++j) {
                }

                mediaSounds[i] = MediaManager.getSound(data);
                mediaSounds[i].use();
                log("loadsound:" + i);
                data = null;
                System.gc();
            }

            initAudioPresenters();
            success = true;
        } catch (Exception e) {
            log("loadsounderr i:" + currentIndex);
            success = false;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception ignored) {
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

    public static void processEvent(int type, int param) {
        try {
            if (type == 0) {
                inputState[0] = (long) (canvas.getKeypadState() & Integer.MAX_VALUE);
                inputState[4]++;
            }
        } catch (Exception ignored) {
        }

    }

    public static void updateInputState() {
        long var0 = 0L;
        long var2 = 0L;

        if (inputState[4] == 0L) {
            inputState[0] = 0L;
        }

        inputState[3] = inputState[6];
        inputState[6] = inputState[0] | var0 | var2 << 32;
        inputState[5] = inputState[6] & (inputState[6] ^ inputState[3]);
        if (inputStateFlag) {
            if (inputState[4] != 0L) {
                inputState[5] |= var2 << 32 & 844424930131968L;
            }
        } else if (inputState[4] != 0L) {
            inputState[5] |= var0 & 655360L;
        }

        if ((inputState[3] ^ inputState[6]) == 0L && inputState[6] != 0L) {
            ++keyHeldTime;
        } else {
            keyHeldTime = 0;
        }

        inputState[4] = 0L;
        if ((inputState[6] & 9851624207876096L) == 0L) {
            ++timeSinceLastInput;
        } else {
            timeSinceLastInput = 0;
        }

        inputState[2] = inputState[6];
        inputState[1] = inputState[5];
    }

    public static void systemAttributeHelper() {
        try {
            systemAttributeState[0] = 0;
            systemAttributeState[1] = 0;
            PhoneSystem.setAttribute(1, 0);
        } catch (Exception ignored) {
        }

    }

    public static void setSomeSystemAttribute() {
        if (systemAttributeState[0] > 0) {
            if (gameSave[5] == 0 && systemAttributeState[1] == 0) {
                try {
                    systemAttributeState[1] = 1;
                    PhoneSystem.setAttribute(1, 1);
                } catch (Exception ignored) {
                }
            }

            if (--systemAttributeState[0] <= 0) {
                systemAttributeHelper();
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
        } catch (Exception ignored) {
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
        } catch (Exception ignored) {
        }

    }

    public static void downloadGameData(String path, int size, int pos) throws Exception {
        byte[] buffer = new byte[10240];
        repaint();
        pos += gameSave[1] * 10240;

        for (int i = gameSave[1]; i < (size - 1) / 10240 + 1; ++i) {
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
            ++loadingProgress;
            repaint();
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

    public static void drawString(Graphics g, String str, int x, int y, int align) {
        drawMultilineString(g, str, x, y, currentFont.getHeight() + 1, align);
    }

    public static void drawMultilineString(Graphics g, String str, int x, int y, int lineHeight, int align) {
        int fromIndex = 0;
        boolean hasNewLine = true;

        for (y += currentFont.getHeight(); hasNewLine; y += lineHeight) {
            int toIndex = str.indexOf("\n", fromIndex);
            if (toIndex == -1) {
                toIndex = str.length();
                hasNewLine = false;
            }

            int newX = x;
            if (align == ALIGN_CENTER) {
                newX = x - currentFont.stringWidth(str.substring(fromIndex, toIndex)) / 2;
            } else if (align == ALIGN_RIGHT) {
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
    }

    public static void setCurrentFont(int fontIndex) {
        switch (fontIndex) {
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
        currentFontIdx = fontIndex;
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
        int delimiterIndex;
        for (count = 0; hasMoreDelimiters; startIndex = delimiterIndex + delimiter.length()) {
            delimiterIndex = text.indexOf(delimiter, startIndex);
            if (delimiterIndex == -1) {
                delimiterIndex = text.length();
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

    public static void selectSoftLabel(int index) {
        if (previousSoftLabelIdx != index) {
            try {
                currentSoftLabelIdx = previousSoftLabelIdx;
                setSoftLabel(0, softLabels[index]);
                previousSoftLabelIdx = index;
            } catch (Exception ignored) {
            }

        }
    }

    public static boolean downloadGameDataIfNeeded() {
        try {
            if (gameDataVersion != gameSave[2]) {
                gameSave[1] = 0;
                gameSave[2] = gameDataVersion;
                saveGame();
            }

            if (gameSave[1] != 255) {
                loadingProgress = gameSave[1];
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
            goToPage(PAGE_LOADING);
        } else {
            goToPage(PAGE_COM_ERROR);
        }

    }

    public static void loadResources() {
        int result = loadImages(128, 0, 93);
        if (result == -1) {
            goToPage(PAGE_PREP_ERROR);
        } else if (loadSounds() && loadTexts()) {
            goToPage(PAGE_TITLE);
        } else {
            goToPage(PAGE_PREP_ERROR);
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
                ++loadingProgress;
                pos += sizes[i];
                repaint();

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

    public static void loadImage(int index) throws Exception {
        if (images[index] == null) {
            short baseOffset = 128;
            int pos = baseOffset + (imageSizes.length + 1) * 2;

            for (int i = 0; i < index; ++i) {
                pos += imageSizes[i];
            }

            MediaImage mediaImage = MediaManager.getImage("scratchpad:///0;pos=" + pos);
            mediaImage.use();
            images[index] = mediaImage.getImage();
        }

    }

    public static void disposeImage(int index) {
        if (images[index] != null) {
            images[index].dispose();
            images[index] = null;
        }

    }

    public static void downloadingPage(Graphics g, int x, int y) {
        int barX = (GameApp.canvasWidth - 200) / 2;
        int barY = y + 168;
        setColorOfRGBInt(g, 16777215);
        g.fillRect(x, y, 240, 240);
        setColorOfRGBInt(g, 16763955);
        g.drawRect(barX, barY, 200, 40);
        drawString(g, "Downloading", GameApp.canvasWidth / 2, barY - currentFontHeight - 4, ALIGN_CENTER);
        int progressBarWidth = 200 * loadingProgress / 8;
        g.fillRect(barX, barY, progressBarWidth, 40);
    }

    public static void loadingPage(Graphics g, int x, int y) {
        if (3 < loadingProgress) {
            try {
                Thread.sleep(300L);
            } catch (Exception ignored) {
            }

            loadingAnimation(g, x, y, loadingProgress * 8 / 93, loadingProgress);
        }

    }

    public static void exitGameOnSelect() {
        if (isKeyPressed(KEY_SELECT)) {
            running = false;
        }

    }

    public static void showError(Graphics g, String str, int x, int y) {
        setColorOfRGBInt(g, 0);
        g.fillRect(x, y, 240, 240);
        setColorOfRGBInt(g, 16777215);
        drawString(g, str, GameApp.canvasWidth / 2, y + 30, ALIGN_CENTER);
        drawString(g, "An error has occured", GameApp.canvasWidth / 2, y + 31 + currentFontHeight, ALIGN_CENTER);
        drawString(g, "Confirm:Exit", GameApp.canvasWidth / 2, y + 240 - 10 - currentFontHeight, ALIGN_CENTER);
    }

    public static void resetTitleScreenState() {
        setButtonConfig(2, true);
        setSelectedButtonIndex(0);
        setButtonTheme(15947864, 15947864, 16353930, 16777215, 12138328, 16777215, 16777215, 16353930, 16777215, 13816530);
        playMusic(1, true);
        titleScreenState[2] = 0;
        titleScreenState[3] = 0;
        titleScreenState[4] = 0;
        nextTitleScreenState(0);
    }

    public static void nextTitleScreenState(int state) {
        switch (state) {
            case 0:
            case 1:
            case 2:
            default:
                fullDrawOnNextPaint = true;
                titleScreenState[2] = 0;
                titleScreenState[1] = state;
        }
    }

    public static void titleScreenFlow() {
        titleScreenState[2]++;
        switch (titleScreenState[1]) {
            case 0:
                nextTitleScreenState(1);
                return;
            case 1:
                titleScreenAnimatedFlow();
                break;
            case 2:
                titleScreenFinishedFlow();
        }

    }

    public static void titleScreenAnimatedFlow() {
        int ready = -48 + titleScreenState[2] * 8;
        if (isKeyPressed(KEY_SELECT) || 0 <= ready) {
            nextTitleScreenState(2);
        }

    }

    public static void titleScreenFinishedFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(100, 5);
        } else {
            if (titleScreenState[3] <= 32) {
                titleScreenState[4] += 2;
            } else {
                titleScreenState[4] -= 2;
            }
            titleScreenState[3] += titleScreenState[4];
            switch (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN))) {
                case 0:
                    goToPage(PAGE_MAILBOX_MODE);
                    return;
                case 1:
                    goToPage(PAGE_TRAVEL_MODE);
                    return;
                default:
                    titleScreenState[0] = getSelectedButtonIndex();
            }
        }
    }

    public static void titleScreen(Graphics g, int x, int y) {
        switch (titleScreenState[1]) {
            case 1:
                titleScreenAnimated(g, x, y);
                break;
            case 2:
                titleScreenFinished(g, x, y);
        }

    }

    public static void titleScreenAnimated(Graphics g, int x, int y) {
        int planetX = (canvasWidth - getSpriteWidth(72)) / 2;
        int planetY = y + 54;
        int logoOffsetY = -48 + titleScreenState[2] * 8;
        setColorOfRGBInt(g, 6728679);
        g.fillRect(x, y, 240, 240);

        // Tamagotchi Park
        drawSprite(g, 23, x, y + logoOffsetY, 0);

        // Eggs
        drawSprite(g, 6, x, y + 116, 0);
        drawSprite(g, 6, x + 120, y + 116, 0);

        setColorOfRGBInt(g, 7456538);
        g.fillRect(x, y + 158, 240, 82);

        // Planet
        drawSprite(g, 72, planetX, planetY, 0);
        drawSprite(g, 73 + (titleScreenState[2] >> 4 & 1), planetX + 23, planetY + 66, 0);
    }

    public static void titleScreenFinished(Graphics g, int x, int y) {
        int planetX = (canvasWidth - getSpriteWidth(72)) / 2;
        int planetY = y + 54;
        if (fullDraw) {
            setColorOfRGBInt(g, 6728679);
            g.fillRect(x, y, 240, 240);

            // Tamagotchi Planet
            drawSprite(g, 23, x, y, 0);
            drawBackgroundCity(g, 6, titleScreenState[3], x, y + 116, 240);
            setColorOfRGBInt(g, 7456538);
            g.fillRect(x, y + 158, 240, 82);

            // Bandai
            drawSprite(g, 9, canvasWidth / 2, y + 158, 2);

            // Planet
            drawSprite(g, 72, planetX, planetY, 0);
            drawSprite(g, 73 + (titleScreenState[2] >> 4 & 1), planetX + 23, planetY + 66, 0);

            for (int i = 0; i < 2; ++i) {
                drawLayoutSpriteButton(g, i, titleScreenLayout, 0);
            }
        } else {
            setColorOfRGBInt(g, 7456538);
            g.fillRect(x, y + 158 + getSpriteHeight(9), 240, 240 - (158 + getSpriteHeight(9)));
            setColorOfRGBInt(g, 6728679);
            g.fillRect(x, y + 116, 240, getSpriteHeight(6));

            drawBackgroundCity(g, 6, titleScreenState[3], x, y + 116, 240);

            // Planet
            drawSprite(g, 72, planetX, planetY, 0);
            drawSprite(g, 73 + (titleScreenState[2] >> 4 & 1), planetX + 23, planetY + 66, 0);

            for (int i = 0; i < 2; ++i) {
                drawLayoutSpriteButton(g, i, titleScreenLayout, 0);
            }
        }

        int eggsY = getValueFrom7Table(titleScreenLayout, getSelectedButtonIndex(), 1);
        eggsY += getValueFrom7Table(titleScreenLayout, getSelectedButtonIndex(), 3) / 2;
        eggsY -= 10;
        drawSprite(g, 75, x + 2, y + eggsY, 0);
        drawSprite(g, 75, x + 240 - (getSpriteWidth(75) - 10), y + eggsY, 0);
    }

    public static void drawBackgroundCity(Graphics g, int spriteIndex, int offset, int x, int y, int screenWidth) {
        while (0 < offset) {
            offset -= getSpriteWidth(spriteIndex);
        }

        while (offset < screenWidth) {
            drawSprite(g, spriteIndex, x + offset, y, 0);
            offset += getSpriteWidth(spriteIndex);
        }

    }

    public static void resetMailboxModeState() {
        setButtonConfig(3, true);
        setSelectedButtonIndex(0);
        setButtonTheme(15947864, 15947864, 16353930, 16777215, 12138328, 16777215, 16777215, 16353930, 16777215, 13816530);
        playMusic(2, true);
        nextMailboxModeState(0);
    }

    public static void nextMailboxModeState(int state) {
        mailboxModeState[2] = 0;
        mailboxModeState[1] = state;
    }

    public static void mailboxModeFlow() {
        mailboxModeState[2]++;
        switch (mailboxModeState[1]) {
            case 0:
                nextMailboxModeState(1);
                break;
            case 1:
                mailboxModeSelectFlow();
        }

    }

    public static void mailboxModeSelectFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(105, 3);
        } else {
            switch (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN))) {
                case 0:
                    goToPage(PAGE_SHOPPING_CENTER);
                    return;
                case 1:
                    goToPage(PAGE_PARENT_CALL);
                    return;
                case 2:
                    goToPage(PAGE_GOTCHI_KING);
                    return;
                default:
                    mailboxModeState[0] = getSelectedButtonIndex();
            }
        }
    }

    public static void mailboxModePage(Graphics g, int x, int y) {
        int chatBubbleHeight = (currentFontHeight + 1) * 2 + currentFontHeight;
        short chatBubbleWidth = 184;
        if (fullDraw) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            setColorOfRGBInt(g, 6728679);
            g.fillRect(x, y + 78, 240, getSpriteHeight(6));

            // City background
            drawSprite(g, 6, x, y + 78, 0);
            drawSprite(g, 6, x + 120, y + 78, 0);

            drawBeveledRect(g, x + 3, y + 3, chatBubbleWidth, chatBubbleHeight + 4, 0, 16056665);
            setColorOfRGBInt(g, 16777215);
            // 29: Connect to Tama Planet by phone!
            drawString(g, getText(29), x + 3 + 2, y + 3 + 2, ALIGN_LEFT);

            for (int i = 0; i < 3; ++i) {
                drawLayoutSpriteButton(g, i, mailboxModeLayout, 0);
            }
        } else {
            for (int i = 0; i < 3; ++i) {
                drawLayoutSpriteButton(g, i, mailboxModeLayout, 0);
            }
        }

        int pairDistance = getValueFrom7Table(mailboxModeLayout, getSelectedButtonIndex(), 2) / 2;
        pairDistance -= 10;

        int pairPositionY = getValueFrom7Table(mailboxModeLayout, getSelectedButtonIndex(), 1) + y;
        pairPositionY += getValueFrom7Table(mailboxModeLayout, getSelectedButtonIndex(), 3) / 2;

        drawMirroredTamagotchiPair(g, 64, canvasWidth / 2, pairPositionY, (pairDistance - getSpriteWidth(64)) * 2, mailboxModeState[2]);

        if (fullDraw) {
            // Bubble tip
            drawSprite(g, 90, x + 3 + chatBubbleWidth - 1, y + 3 + chatBubbleHeight / 2 - 5, 0);
            // Planet
            drawSprite(g, 89, x + 240 + 2 - getSpriteWidth(89), y + 54, 0);
            clearOutsideGameArea(g);
        }

    }

    public static void resetTravelModeSate() {
        setButtonConfig(2, true);
        setSelectedButtonIndex(0);
        setButtonTheme(15947864, 15947864, 16353930, 16777215, 12138328, 16777215, 16777215, 16353930, 16777215, 13816530);
        playMusic(0, true);
        nextTravelModeState(0);
    }

    public static void nextTravelModeState(int state) {
        travelModeState[1] = state;
        travelModeState[2] = 0;
    }

    public static void travelModeFlow() {
        travelModeState[2]++;
        switch (travelModeState[1]) {
            case 0:
                nextTravelModeState(1);
                break;
            case 1:
                travelModeSelectFlow();
        }

    }

    public static void travelModeSelectFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(108, 2);
        } else {
            switch (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN))) {
                case 0:
                    goToPage(PAGE_TRAVEL_MEMORY);
                    return;
                case 1:
                    goToPage(PAGE_EXCHANGE_PLAZA);
                    return;
                default:
                    travelModeState[0] = getSelectedButtonIndex();
            }
        }
    }

    public static void travelModePage(Graphics g, int x, int y) {
        int chatBubbleHeight = (currentFontHeight + 1) * 2 + currentFontHeight;
        short chatBubbleWidth = 192;

        if (fullDraw) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            setColorOfRGBInt(g, 6728679);
            g.fillRect(x, y + 78, 240, getSpriteHeight(6));

            // Background city
            drawSprite(g, 6, x, y + 78, 0);
            drawSprite(g, 6, x + 120, y + 78, 0);

            drawBeveledRect(g, x + 3, y + 3, chatBubbleWidth, chatBubbleHeight + 8, 0, 16056665);
            setColorOfRGBInt(g, 16777215);
            // 40: Send your Tama on a trip with your phone!
            drawString(g, getText(40), x + 3 + 6, y + 3 + 4, ALIGN_LEFT);

            for (int i = 0; i < 2; ++i) {
                drawLayoutSpriteButton(g, i, travelModeLayout, 0);
            }
        } else {
            for (int i = 0; i < 2; ++i) {
                drawLayoutSpriteButton(g, i, travelModeLayout, 0);
            }
        }

        int pairDistance = getValueFrom7Table(travelModeLayout, getSelectedButtonIndex(), 2) / 2;
        pairDistance -= 10;

        int pairPositionY = getValueFrom7Table(travelModeLayout, getSelectedButtonIndex(), 1) + y;
        pairPositionY += getValueFrom7Table(travelModeLayout, getSelectedButtonIndex(), 3) / 2;

        drawMirroredTamagotchiPair(g, 64, canvasWidth / 2, pairPositionY, (pairDistance - getSpriteWidth(64)) * 2, travelModeState[2]);

        if (fullDraw) {
            drawSprite(g, 90, x + 3 + chatBubbleWidth - 1, y + 3 + chatBubbleHeight / 2 - 5, 0);
            drawSprite(g, 57, x + 240 - getSpriteWidth(57) - 2, y + 68, 0);
        }

    }

    public static void resetShoppingCenterState() {
        nextShoppingCenterState(0);
    }

    public static void nextShoppingCenterState(int state) {
        switch (state) {
            case 0:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                break;
            case 1:
                setButtonConfig(5, true);
                setSelectedButtonIndex(4);
                setButtonTheme2(16750848, 16750848, 16763955, 16777215, 16750848, 16777164, 16750848, 16750848);
                selectSoftLabel(SOFT_LABEL_MENU);
                break;
            case 2:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                break;
            case 3:
                selectSoftLabel(SOFT_LABEL_MENU);
                setButtonConfig(3, true);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 6594720, 16777215, 16777215, 6594720, 16763955, 13158600);
                break;
            case 4:
                startSendingViaIr(currentPage, 175, 2, 61);
        }

        fullDrawOnNextPaint = true;
        shoppingCenterState[3] = 0;
        shoppingCenterState[2] = 0;
        shoppingCenterState[1] = state;
    }

    public static void shoppingCenterFlow() {
        shoppingCenterState[2]++;
        switch (shoppingCenterState[1]) {
            case 0:
                nextShoppingCenterState(1);
                break;
            case 1:
                shoppingCenterItemTypeSelectFlow();
                break;
            case 2:
                downloadShoppingCenterPassword();
                break;
            case 3:
                shoppingCenterItemTicketFlow();
                break;
            case 4:
                shoppingCenterSendViaIrFlow();
        }

    }

    public static void shoppingCenterItemTypeSelectFlow() {
        shoppingCenterState[3]++;
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(110, 5);
        } else {
            int selectedButton = getSelectedButtonIndex();
            int pressedButton = getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_DOWN), isKeyPressed(KEY_UP));
            shoppingCenterState[0] = getSelectedButtonIndex();
            if (selectedButton != shoppingCenterState[0]) {
                shoppingCenterState[3] = 0;
            }

            if (pressedButton != -1) {
                nextShoppingCenterState(2);
            }

        }
    }

    public static void downloadShoppingCenterPassword() {
        prepareShoppingCenterSentData();
        DataInputStream inputStream = null;

        try {
            inputStream = sendPreparedDataToServer(4);
            unknownOperationOnServerResponse(inputStream);

            int errorMessageLength = inputStream.read();

            if (errorMessageLength > 0) {
                String errorMessage = readString(inputStream, errorMessageLength);
                showErrorPage(currentPage, 2, 1, errorMessage);
            } else {
                byte[] passwordData = new byte[10];
                inputStream.read(passwordData);
                parseAndStoreDownloadedPassword(passwordData);
                nextShoppingCenterState(3);
            }
        } catch (Exception e) {
            log("e:" + e);
            // 92: Communication has failed. Would you like to try again?
            showErrorPage(currentPage, 2, 1, getText(92));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ignored) {
                }
            }

            playSound(6, false);
        }

    }

    public static void prepareShoppingCenterSentData() {
        setFirstByteSentToServer();
        setByteSentToServer(1, 0);
        setByteSentToServer(2, 3);

        // shoppingCenterState[0] + 1 --> selected item category
        // 1 - Elite Items
        // 2 - Luxury Items
        // 3 - Fancy Items
        // 4 - Market Items
        // 5 - Common Items
        setByteSentToServer(3, shoppingCenterState[0] + 1);
    }

    public static void shoppingCenterItemTicketFlow() {
        shoppingCenterState[3]++;
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(115, 6);
        } else {
            if (6 < shoppingCenterState[2]) {
                switch (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN))) {
                    case 0:
                        nextShoppingCenterState(4);
                        break;
                    case 1:
                        nextShoppingCenterState(0);
                        break;
                    case 2:
                        goToPage(PAGE_TITLE);
                }
            }

        }
    }

    public static void shoppingCenterSendViaIrFlow() {
        if (!hasStartedSendingViaIr()) {
            nextShoppingCenterState(3);
        } else {
            sendViaIrFlow();
        }
    }

    public static void shoppingCenterPage(Graphics g, int x, int y) {
        switch (shoppingCenterState[1]) {
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
        if (fullDraw) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // 2: Shopping Center
            drawTextWithBackground(g, getText(2), canvasWidth / 2, y + 2, currentFont.stringWidth(getText(2)) + 8, 2);
        }

        for (int i = 0; i < 5; ++i) {
            int colorBaseIndex = i * 4;
            setButtonTheme(shoppingCenterItemColors[colorBaseIndex + 0], shoppingCenterItemColors[colorBaseIndex + 0], shoppingCenterItemColors[colorBaseIndex + 0], shoppingCenterItemColors[colorBaseIndex + 1], shoppingCenterItemColors[colorBaseIndex + 0], shoppingCenterItemColors[colorBaseIndex + 2], shoppingCenterItemColors[colorBaseIndex + 2], shoppingCenterItemColors[colorBaseIndex + 2], shoppingCenterItemColors[colorBaseIndex + 3], shoppingCenterItemColors[colorBaseIndex + 2]);
            drawLayoutTextButton(g, i, shoppingCenterLayoutTable, 0);
        }

        // 26: Use your Gotchi Points from your Keitama to buy Tamagotchi goods! Choose the rank of the item you want and press OK!
        drawFullWidthScrollingText(g, x, getText(26), y + 2 + 34 + 1, shoppingCenterState[2], 12, 16056665, 16777215);

        for (int i = 0; i < 5; ++i) {
            int distance = getValueFrom6Table(shoppingCenterLayoutTable, i, 2) - 66;
            byte baseSpriteId;
            if (i == getSelectedButtonIndex()) {
                baseSpriteId = 61;
                distance -= abs((shoppingCenterState[3] & 31) - 16);
            } else {
                baseSpriteId = 62;
            }

            int spriteId = baseSpriteId + (shoppingCenterState[3] >> 3 & 1);
            int centerY = y + getValueFrom6Table(shoppingCenterLayoutTable, i, 1);
            centerY += getValueFrom6Table(shoppingCenterLayoutTable, i, 3) / 2 + 2;
            drawTamagotchiPair(g, spriteId, x + getValueFrom6Table(shoppingCenterLayoutTable, i, 0), centerY, distance, shoppingCenterState[2], 0);
        }

    }

    public static void shoppingCenterIssuingItemTicket(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        setColorOfRGBInt(g, 6728679);
        g.fillRect(x, y + 110, 240, getSpriteHeight(6));

        // Background city
        drawSprite(g, 6, x, y + 110, 0);
        drawSprite(g, 6, x + 120, y + 110, 0);

        // 76: Issuing Item Ticket
        drawTextWithBackground(g, getText(76), canvasWidth / 2, y + 2, 200, 2);
        drawSprite(g, 76, canvasWidth / 2, y + 100, 2);
    }

    public static void shoppingCenterItemTicket(Graphics g, int x, int y) {
        if (fullDraw) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // 79: Item Ticket
            drawTextWithBackground(g, getText(79), canvasWidth / 2, y + 3, currentFont.stringWidth(getText(79)) + 8, 2);
        } else {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y + getValueFrom6Table(itemTicketLayout, 0, 1), 240, 240 - (getValueFrom6Table(itemTicketLayout, 0, 1) + getSpriteHeight(0)));
        }

        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);

        // 64: Enter the Ticket No. in your Keitama
        drawFullWidthScrollingText(g, x, getText(64), y + 3 + currentFontHeight + 12, shoppingCenterState[2], 12, 16056665, 16777215);
        int codeInputY = y + 3 + currentFontHeight + 12 + currentFontHeight + 4;
        if (fullDraw) {
            drawCodeInputBackground(g, canvasWidth / 2, codeInputY, 2, 0, 16770972, 16750748, 16770972);
            drawDownloadUploadAnimations(g, canvasWidth / 2, codeInputY + 10, 56, shoppingCenterState[2] >> 1, false);
        } else {
            drawDownloadUploadAnimationsWithBackground(g, canvasWidth / 2, codeInputY + 10, 56, shoppingCenterState[2] >> 1, false, 16770972);
        }

        drawCodeInput(g, canvasWidth / 2, codeInputY + 4, 62, false, fullDraw);

        for (int i = 0; i < 3; ++i) {
            drawLayoutTextButton(g, i, itemTicketLayout, 0);
        }

        drawMirroredTamagotchiPair(g, 61, x + getValueFrom6Table(itemTicketLayout, getSelectedButtonIndex(), 0), y + getValueFrom6Table(itemTicketLayout, getSelectedButtonIndex(), 1) + getValueFrom6Table(itemTicketLayout, getSelectedButtonIndex(), 3) / 2, getValueFrom6Table(itemTicketLayout, getSelectedButtonIndex(), 2) - 10, shoppingCenterState[2]);
    }

    public static void shoppingCenterSendViaIR(Graphics g, int x, int y) {
        sendViaIR(g, x, y);
    }

    public static void drawFullWidthScrollingText(Graphics g, int x, String text, int y, int time, int speed, int textColor, int backgroundColor) {
        drawScrollingText(g, text, x, y, 240, time, speed, textColor, backgroundColor);
    }

    public static void drawScrollingText(Graphics g, String text, int x, int y, int width, int time, int speed, int textColor, int backgroundColor) {
        setColorOfRGBInt(g, backgroundColor);
        g.fillRect(x, y, width, currentFontHeight);
        setColorOfRGBInt(g, textColor);
        int stringWidth = currentFont.stringWidth(text);
        drawString(g, text, x + width - time * speed % (width + stringWidth), y, ALIGN_LEFT);
    }

    public static void drawDownloadUploadAnimations(Graphics g, int x, int y, int distance, int time, boolean dir) {
        drawSprite(g, 32, x - (distance + 6), y + 18, 0);
        drawSprite(g, (!dir ? 77 : 78) + time % 3 * 2, x - (distance + 5), y, 0);
        drawSprite(g, 33, x + (distance - 3), y + 20, 0);
        drawSprite(g, (dir ? 83 : 84) + time % 3 * 2, x + (distance - 3), y, 0);
    }

    public static void drawDownloadUploadAnimationsWithBackground(Graphics g, int x, int y, int distance, int time, boolean dir, int backgroundColor) {
        setColorOfRGBInt(g, backgroundColor);
        g.fillRect(x - (distance + 6), y, getSpriteWidth(32), 18 + getSpriteHeight(32));
        g.fillRect(x + (distance - 3), y, getSpriteWidth(33), 20 + getSpriteHeight(33));
        drawDownloadUploadAnimations(g, x, y, distance, time, dir);
    }

    public static void drawTextWithBackground(Graphics g, String text, int x, int y, int width, int align) {
        int height = calculateTextHeight(text);
        byte borderColor = 0;
        int color = 16056665;
        int textColor = 16777215;
        if (align == 2) {
            x -= width / 2;
        } else if (align == 1) {
            x -= width;
        }

        drawBeveledRect(g, x, y, width, height, borderColor, color);
        setColorOfRGBInt(g, textColor);
        drawString(g, text, x + width / 2, y + 2, ALIGN_CENTER);
    }

    public static int calculateTextHeight(String text) {
        return (currentFontHeight + 1) * (splitCount(text, "\n") - 1) + currentFontHeight + 4;
    }

    public static void drawCodeInputBackground(Graphics g, int x, int y, int align, int outerBorderColor, int outerFillColor, int innerBorderColor, int innerFillColor) {
        if (align == 2) {
            x -= 88;
        } else if (align == 1) {
            x -= 176;
        }

        drawBeveledRect(g, x, y, 176, 70, outerBorderColor, outerFillColor);
        drawBeveledRect(g, x + 3, y + 3, 170, 64, innerBorderColor, innerFillColor);
    }

    public static void drawCodeInputBackgroundWithHorizontalLine(Graphics g, int x, int y, int outerBorderColor, int outerFillColor, int innerBorderColor, int innerFillColor, int lineColor) {
        byte width = 32;
        int newY = y + 24;

        setColorOfRGBInt(g, lineColor);
        g.fillRect(x, newY, width, 2);
        g.fillRect(x, newY + 2 + 1, width, 16);
        g.fillRect(x, newY + 2 + 1 + 16 + 1, width, 2);
        g.fillRect(x + width + 176, newY, width, 2);
        g.fillRect(x + width + 176, newY + 2 + 1, width, 16);
        g.fillRect(x + width + 176, newY + 2 + 1 + 16 + 1, width, 2);

        drawCodeInputBackground(g, canvasWidth / 2, y, 2, outerBorderColor, outerFillColor, innerBorderColor, innerFillColor);
    }

    public static void drawMirroredTamagotchiPair(Graphics g, int baseSpriteId, int centerX, int centerY, int distance, int time) {
        drawTamagotchiPair(g, baseSpriteId, centerX, centerY, distance, time, 1);
    }

    public static void drawTamagotchiPair(Graphics g, int baseSpriteId, int centerX, int centerY, int distance, int time, int offsetAnimation) {
        drawSprite(g, baseSpriteId + (offsetAnimation & -(time >> 2 & 1)), centerX - distance / 2 - getSpriteWidth(baseSpriteId) - 2 - 2, centerY - getSpriteHeight(baseSpriteId) / 2, 0);
        drawSprite(g, baseSpriteId + (offsetAnimation & -((time >> 2) + 1 & 1)), centerX + distance / 2 + 2 + 2, centerY - getSpriteHeight(baseSpriteId) / 2, 0);
    }

    public static void drawCodeInput(Graphics g, int x, int y, int height, boolean showCursor, boolean redraw) {
        int rightX = x - 35;
        int topY = y + (height - 49) / 2;

        int cursorIndex = getCursorIndex();
        int previousCursorIndex = getPreviousCursorIndex();

        setColorOfRGBInt(g, 16777215);

        if (redraw) {
            g.fillRect(rightX - 2, y, 75, height);
        } else {
            g.fillRect(rightX + previousCursorIndex % 5 * 15, topY + previousCursorIndex / 5 * 26, 11, 24);
            g.fillRect(rightX + cursorIndex % 5 * 15, topY + cursorIndex / 5 * 26, 11, 24);
        }

        if (showCursor) {
            setColorOfRGBInt(g, 13619071);
            g.fillRect(rightX + cursorIndex % 5 * 15, topY + cursorIndex / 5 * 26, 11, 24);
        }

        setColorOfRGBInt(g, 0);

        if (redraw) {
            drawAllDigits(g, x, topY, 2);
        } else {
            drawDigit(g, x, topY, 2, previousCursorIndex);
            drawDigit(g, x, topY, 2, cursorIndex);
        }

    }

    public static void resetParentCallState() {
        clearCodeInput();
        setCursorIndex(0);
        nextParentCallState(0);
    }

    public static void nextParentCallState(int state) {
        switch (state) {
            case 0:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                break;
            case 1:
                selectSoftLabel(SOFT_LABEL_MENU);
                setButtonConfig(2, true);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                parentCallState[0] = 0;
                break;
            case 2:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                break;
            case 3:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                setButtonConfig(1, false);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 4:
                selectSoftLabel(SOFT_LABEL_MENU);
                setButtonConfig(2, true);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 5:
                startSendingViaIr(currentPage, 177, 2, 55);
        }

        fullDrawOnNextPaint = true;
        parentCallState[6] = 0;
        parentCallState[1] = state;
    }

    public static void clearDownloadedParentCallData() {
        for (int i = 0; i < 2; ++i) {
            if (parentCallImages[i] != null) {
                parentCallImages[i].dispose();
                parentCallImages[i] = null;
            }
        }

        parentCallText = null;
        parentCallQuote = null;
        System.gc();
    }

    public static void parentCallFlow() {
        parentCallState[6]++;
        switch (parentCallState[1]) {
            case 0:
                nextParentCallState(1);
                break;
            case 1:
                parentCallCodeInputFlow();
                break;
            case 2:
                downloadParentCallData();
                break;
            case 3:
                parentCallExplanationFlow();
                break;
            case 4:
                parentCallAllowanceTicketFlow();
                break;
            case 5:
                parentCallSendViaIrFlow();
        }

    }

    public static void parentCallCodeInputFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(121, 6);
        } else {
            if (parentCallState[0] != 0) {
                if (getPressedButtonIndex(isKeyPressed(KEY_SELECT), false, false) != -1) {
                    nextParentCallState(2);
                } else if (!isKeyPressed(KEY_SELECT)) {
                    if (isKeyPressed(KEY_UP_LEFT)) {
                        setCursorIndex(9);
                        parentCallState[0] = 0;
                    } else if (isKeyPressed(KEY_DOWN_RIGHT)) {
                        setCursorIndex(0);
                        parentCallState[0] = 0;
                    }
                }
            } else if (handleCodeInput(isKeyPressed(KEY_LEFT), isKeyPressed(KEY_RIGHT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN), isKeyPressed(KEY_SELECT), getPressedNumber())) {
                parentCallState[0] = 1;
            }

            setSelectedButtonIndex(parentCallState[0]);
        }
    }

    public static void downloadParentCallData() {
        clearDownloadedParentCallData();
        prepareParentCallSentData();
        DataInputStream inputStream = null;

        try {
            inputStream = sendPreparedDataToServer(13);
            unknownOperationOnServerResponse(inputStream);

            int errorMessageLength = inputStream.read();

            if (errorMessageLength > 0) {
                String errorMessage = readString(inputStream, errorMessageLength);
                showErrorPage(currentPage, 2, 1, errorMessage);
            } else {
                byte[] passwordData = new byte[10];
                inputStream.read(passwordData);

                for (int i = 0; i < 2; ++i) {
                    int imageSize = inputStream.readUnsignedShort();
                    parentCallImages[i] = readImage(inputStream, imageSize);
                }

                int textSize = inputStream.readUnsignedShort();
                parentCallText = readString(inputStream, textSize);

                parentCallState[2] = 0;
                parentCallState[4] = 0;
                parentCallState[5] = countQuotedSegments(parentCallText);
                parentCallQuote = findNthQuote(parentCallText, parentCallState[4]);
                parseAndStoreDownloadedPassword(passwordData);
                nextParentCallState(3);
            }
        } catch (Exception e) {
            log("e:" + e);
            // 92: Communication has failed. Would you like to try again?
            showErrorPage(currentPage, 2, 1, getText(92));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ignored) {
                }
            }

            playSound(6, false);
        }

    }

    public static void prepareParentCallSentData() {
        setFirstByteSentToServer();
        setByteSentToServer(1, 1);
        setByteSentToServer(2, 3);

        for (int i = 0; i < 10; ++i) {
            setByteSentToServer(3 + i, getDigitBankA(i));
        }

    }

    public static void parentCallExplanationFlow() {
        parentCallState[3]++;
        if (6 < parentCallState[6]) {
            if (-1 != getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_LEFT), isKeyPressed(KEY_RIGHT))) {
                parentCallState[4]++;
                parentCallState[3] = 0;
                if (parentCallState[5] <= parentCallState[4]) {
                    generateDerivedCodeInBankB();
                    if (3 < getDigitBankB(2)) {
                        goToPage(PAGE_TITLE);
                    } else {
                        nextParentCallState(4);
                    }
                } else {
                    parentCallQuote = findNthQuote(parentCallText, parentCallState[4]);
                }
            }
        } else {
            int quoteWidth = currentFont.stringWidth(parentCallQuote);
            if (quoteWidth + 232 <= parentCallState[3] * 12) {
                parentCallState[3] = 0;
            }
        }

    }

    public static void parentCallAllowanceTicketFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(127, 6);
        } else {
            if (6 < parentCallState[6]) {
                switch (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN))) {
                    case 0:
                        nextParentCallState(5);
                        break;
                    case 1:
                        goToPage(PAGE_TITLE);
                }
            }

        }
    }

    public static void parentCallSendViaIrFlow() {
        if (!hasStartedSendingViaIr()) {
            nextParentCallState(4); // Go back
        } else {
            sendViaIrFlow();
        }
    }

    public static void parentCallPage(Graphics g, int x, int y) {
        switch (parentCallState[1]) {
            case 0:
            default:
                break;
            case 1:
                parentCallCodeInput(g, x, y);
                break;
            case 2:
                connectingToTamaPlanet(g, x, y);
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
        int buttonY = y + 240 - 42;
        if (fullDraw) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240 - getSpriteHeight(0));
            // 17: Connect with your parent on Tamagotchi Planet!
            drawTextWithBackground(g, getText(17), canvasWidth / 2, y + 3, 232, 2);
        }

        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 65: Enter the Address No. shown on your Keitama
        drawFullWidthScrollingText(g, x, getText(65), y + 3 + textHeight + 3, parentCallState[6], 12, 16056665, 16777215);
        int inputY = y + 3 + textHeight + 3 + currentFontHeight + 4 + 20;
        boolean showCursor = 0 == getSelectedButtonIndex() & (parentCallState[6] & 4) != 0;
        drawCodeInputWithSimpleBackground(g, canvasWidth / 2, inputY, showCursor);
        // 18: OK
        drawTextButton(g, 1, getText(18), canvasWidth / 2, buttonY, 100, 28, 2, 0);
        if (fullDraw) {
            drawDownloadUploadAnimations(g, canvasWidth / 2, inputY + 5, 56, parentCallState[6] >> 1, true);
        } else {
            drawDownloadUploadAnimationsWithBackground(g, canvasWidth / 2, inputY + 5, 56, parentCallState[6] >> 1, true, 16777076);
        }

        if (1 == getSelectedButtonIndex()) {
            drawMirroredTamagotchiPair(g, 55, canvasWidth / 2, y + 240 - 42 + 14, 120, parentCallState[6]);
        }

    }

    public static void connectingToTamaPlanet(Graphics g, int x, int y) {
        loadingAnimation(g, x, y, 8, 0);
        // Connecting to Tama Planet
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
        int titleY = y + 4;
        // 23: Parent Call
        drawTextWithBackground(g, getText(23), canvasWidth / 2, titleY, currentFont.stringWidth(getText(23)) + 8, 2);
        int imageY = titleY + currentFontHeight + 8 + 6;
        drawSprite(g, 58, x, imageY, 0); // House
        drawImage(g, parentCallImages[parentCallState[6] >> 3 & 1], x + 144, imageY, 0);
        int scrollingTextY = imageY + 130 + 1;
        int scrollingTextX = (canvasWidth - 232) / 2;
        drawScrollingText(g, parentCallQuote, scrollingTextX, scrollingTextY, 232, parentCallState[3], 12, 16777215, 16056665);
        setColorOfRGBInt(g, 0);
        g.drawRect(scrollingTextX, scrollingTextY - 1, 231, currentFontHeight + 1);
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, scrollingTextY, 4, currentFontHeight);
        g.fillRect(scrollingTextX, scrollingTextY - 1, 1, 1);
        g.fillRect(scrollingTextX, scrollingTextY + currentFontHeight, 1, 1);
        g.fillRect(scrollingTextX + 232, scrollingTextY, 4, currentFontHeight);
        g.fillRect(scrollingTextX - 1, scrollingTextY - 1, 1, 1);
        g.fillRect(scrollingTextX + 232 - 1, scrollingTextY + currentFontHeight, 1, 1);

        byte textIndex;
        if (parentCallState[5] - 1 <= parentCallState[4]) {
            textIndex = 22;
        } else {
            textIndex = 87;
        }

        // 7: Explanation
        drawTextButton(g, 0, getText(textIndex), canvasWidth / 2, y + 240 - 36, 160, 28, 2, 0);
        drawSprite(g, 27, x + 1, y + 1, 0);
        drawSprite(g, 28, x + 240 - 1 - getSpriteWidth(28), y + 1, 0);
        drawSprite(g, 37 + (parentCallState[6] >> 1 & 1), x + 240 - 44, y + imageY + 1, 0);
        drawSprite(g, 12, x + 240 - 38, imageY + 9, 0);
        drawSprite(g, 92, x + 140, scrollingTextY - 16, 0);
        drawMirroredTamagotchiPair(g, 55, canvasWidth / 2, y + 240 - 36 + getSpriteHeight(55) / 2, 160, parentCallState[6]);
    }

    public static void parentCallAllowanceTicket(Graphics g, int x, int y) {
        // 67: Allowance Ticket
        int textHeight = calculateTextHeight(getText(67));
        if (fullDraw) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // 67: Allowance Ticket
            drawTextWithBackground(g, getText(67), canvasWidth / 2, y + 3, currentFont.stringWidth(getText(67)) + 8, 2);
        } else {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y + getValueFrom6Table(allowanceTicketLayout, 0, 1) - 5, 240, 240 - (getValueFrom6Table(allowanceTicketLayout, 0, 1) - 5 + getSpriteHeight(0)));
        }

        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);

        // 66: Enter the Allowance Ticket No. in your Keitama!
        drawFullWidthScrollingText(g, x, getText(66), y + 3 + textHeight + 3, parentCallState[6], 12, 16056665, 16777215);
        int inputY = y + 3 + textHeight + 3 + currentFontHeight + 12;
        drawCodeInputWithSimpleBackground(g, canvasWidth / 2, inputY, false);

        for (int i = 0; i < 2; ++i) {
            drawLayoutTextButton(g, i, allowanceTicketLayout, 0);
        }

        drawDownloadUploadAnimationsWithBackground(g, canvasWidth / 2, inputY + 5, 56, parentCallState[6] >> 1, false, 16777076);
        drawMirroredTamagotchiPair(g, 55, x + getValueFrom6Table(allowanceTicketLayout, getSelectedButtonIndex(), 0), y + getValueFrom6Table(allowanceTicketLayout, getSelectedButtonIndex(), 1) + getValueFrom6Table(allowanceTicketLayout, getSelectedButtonIndex(), 3) / 2, getValueFrom6Table(allowanceTicketLayout, getSelectedButtonIndex(), 2), parentCallState[6]);
    }

    public static void parentCallSendViaIR(Graphics g, int x, int y) {
        sendViaIR(g, x, y);
    }

    public static void drawCodeInputWithSimpleBackground(Graphics g, int x, int y, boolean showCursor) {
        int newX = x - 88;
        if (fullDraw) {
            setColorOfRGBInt(g, 16777076);
            g.drawRect(newX, y, 176, 70);
            g.fillRect(newX + 2, y + 2, 173, 67);
        }

        drawCodeInput(g, x, y + 2, 67, showCursor, fullDraw);
    }

    public static void loadingAnimation(Graphics g, int x, int y, int circleCount, int colorOffset) {
        setColorOfRGBInt(g, 11367);
        g.fillRect(x, y, 240, 240);
        drawSprite(g, 1, x, y + 240 - getSpriteHeight(1), 0);
        byte xOffset = 0;
        drawSprite(g, 3, x + 240 + xOffset, y + 10, 1);
        drawRainbowCircles(g, x + 50, y + 108, 12, -8, 5, colorOffset, circleCount);
    }

    public static void resetGotchiKingState() {
        for (int i = 0; i < imagesToTemporarilyDispose.length; ++i) {
            disposeImage(imagesToTemporarilyDispose[i]);
        }

        clearCodeInput();
        setCursorIndex(0);
        nextGotchiKingState(0);
    }

    public static void nextGotchiKingState(int nextStateId) {
        switch (nextStateId) {
            case 0:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                break;
            case 1:
                selectSoftLabel(SOFT_LABEL_MENU);
                gotchiKingState[0] = 0;
                setButtonConfig(2, true);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 2:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                break;
            case 3:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                setButtonConfig(1, false);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 4:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                break;
            case 5:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                setButtonConfig(1, false);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 6:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                break;
            case 7:
                selectSoftLabel(SOFT_LABEL_MENU);
                setButtonConfig(2, true);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 8:
                startSendingViaIr(currentPage, 179, 2, 39);
        }

        fullDrawOnNextPaint = true;
        gotchiKingState[3] = 0;
        gotchiKingState[1] = nextStateId;
    }

    public static void clearDownloadedGotchiKingData() {
        for (int i = 0; i < 2; ++i) {
            if (gotchiKingImages[i] != null) {
                gotchiKingImages[i].dispose();
                gotchiKingImages[i] = null;
            }
        }

        System.gc();

        for (int i = 0; i < imagesToTemporarilyDispose.length; ++i) {
            try {
                loadImage(imagesToTemporarilyDispose[i]);
            } catch (Exception ignored) {
            }
        }

        System.gc();
    }

    public static void gotchiKingFlow() {
        gotchiKingState[3]++;
        switch (gotchiKingState[1]) {
            case 0:
                nextGotchiKingState(1);
                break;
            case 1:
                gotchiKingCodeInputFlow();
                break;
            case 2:
                downloadGotchiKingData();
                break;
            case 3:
                gotchiKingBroadcastFlow1();
                break;
            case 4:
                gotchiKingBroadcastFlow2();
                break;
            case 5:
                gotchiKingInviteFlow();
                break;
            case 6:
                gotchiKingIssuingInvitationFlow();
                break;
            case 7:
                gotchiKingInviteTicketFlow();
                break;
            case 8:
                gotchiKingSendViaIrFlow();
        }

    }

    public static void gotchiKingCodeInputFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(133, 6);
        } else {
            if (gotchiKingState[0] != 0) {
                if (getPressedButtonIndex(isKeyPressed(KEY_SELECT), false, false) != -1) {
                    nextGotchiKingState(2);
                } else if (!isKeyPressed(KEY_SELECT)) {
                    if (isKeyPressed(KEY_UP_LEFT)) {
                        setCursorIndex(9);
                        gotchiKingState[0] = 0;
                    } else if (isKeyPressed(KEY_DOWN_RIGHT)) {
                        setCursorIndex(0);
                        gotchiKingState[0] = 0;
                    }
                }
            } else if (handleCodeInput(isKeyPressed(KEY_LEFT), isKeyPressed(KEY_RIGHT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN), isKeyPressed(KEY_SELECT), getPressedNumber())) {
                gotchiKingState[0] = 1;
            }

            setSelectedButtonIndex(gotchiKingState[0]);
        }
    }

    public static void downloadGotchiKingData() {
        clearDownloadedGotchiKingData();
        prepareGotchiKingSentData();
        DataInputStream inputStream = null;

        try {
            inputStream = sendPreparedDataToServer(13);
            unknownOperationOnServerResponse(inputStream);

            int errorMessageLength = inputStream.read();

            if (errorMessageLength > 0) {
                String errorMessage = readString(inputStream, errorMessageLength);
                showErrorPage(currentPage, 2, 1, errorMessage);
            } else {
                byte[] passwordData = new byte[10];
                inputStream.read(passwordData);

                for (int i = 0; i < 2; ++i) {
                    int imageSize = inputStream.readUnsignedShort();
                    gotchiKingImages[i] = readImage(inputStream, imageSize);
                }

                gotchiKingState[4] = 0;
                gotchiKingState[5] = 0;
                gotchiKingState[2] = 0;
                parseAndStoreDownloadedPassword(passwordData);
                nextGotchiKingState(3);
            }
        } catch (Exception e) {
            log("e:" + e);
            // 92: Communication has failed. Would you like to try again?
            showErrorPage(currentPage, 2, 1, getText(92));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ignored) {
                }
            }

            playSound(6, false);
        }

    }

    public static void prepareGotchiKingSentData() {
        setFirstByteSentToServer();
        setByteSentToServer(1, 2);
        setByteSentToServer(2, 3);

        for (int i = 0; i < 10; ++i) {
            // Copy input digits
            setByteSentToServer(3 + i, getDigitBankA(i));
        }

    }

    public static void gotchiKingBroadcastFlow1() {
        if (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_LEFT), isKeyPressed(KEY_RIGHT)) != -1) {
            nextGotchiKingState(4);
        }

    }

    public static void gotchiKingBroadcastFlow2() {
        if (45 <= gotchiKingState[3] || isKeyPressed(KEY_SELECT)) {
            nextGotchiKingState(5);
        }

    }

    public static void gotchiKingInviteFlow() {
        if (6 <= gotchiKingState[5]) {
            gotchiKingState[5] = 0;
            gotchiKingState[4]++;
            gotchiKingState[4] %= 2;
        } else {
            gotchiKingState[5]++;
        }

        if (6 < gotchiKingState[3] && getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_LEFT), isKeyPressed(KEY_RIGHT)) != -1) {
            nextGotchiKingState(6);
        }

    }

    public static void gotchiKingIssuingInvitationFlow() {
        if (gotchiKingState[3] > 30) {
            playSound(6, false);
            nextGotchiKingState(7);
        }

    }

    public static void gotchiKingInviteTicketFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(139, 6);
        } else {
            if (6 < gotchiKingState[3]) {
                switch (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN))) {
                    case 0:
                        nextGotchiKingState(8);
                        break;
                    case 1:
                        goToPage(PAGE_TITLE);
                }
            }

        }
    }

    public static void gotchiKingSendViaIrFlow() {
        if (!hasStartedSendingViaIr()) {
            nextGotchiKingState(7); // Go back
        } else {
            sendViaIrFlow();
        }
    }

    public static void gotchiKingPage(Graphics g, int x, int y) {
        switch (gotchiKingState[1]) {
            case 0:
            default:
                break;
            case 1:
                gotchiKingCodeInput(g, x, y);
                break;
            case 2:
                gotchiKingConnecting(g, x, y);
                break;
            case 3:
            case 4:
                gotchiKingBroadcast(g, x, y);
                break;
            case 5:
                gotchiKingInvite(g, x, y);
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
        if (fullDraw) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240 - getSpriteHeight(0));
            // 30: Connect with the Gotchi King on Tamagotchi Planet!
            drawTextWithBackground(g, getText(30), canvasWidth / 2, y + 2, 230, 2);
        }

        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);

        // 68: Enter the Gotchi King Address No. shown on your Keitama
        drawFullWidthScrollingText(g, x, getText(68), y + 2 + textHeight + 2, gotchiKingState[3], 12, 16056665, 16777215);
        boolean showCursor = 0 == getSelectedButtonIndex() & (gotchiKingState[3] & 4) != 0;
        int inputY = y + 2 + textHeight + 2 + currentFontHeight + 3;
        drawCodeInputWithSimpleBackground(g, canvasWidth / 2, inputY, showCursor);
        // 18: OK
        drawTextButton(g, 1, getText(18), canvasWidth / 2, y + 240 - 42, 100, 28, 2, 0);
        if (fullDraw) {
        }

        if (fullDraw) {
            drawDownloadUploadAnimations(g, canvasWidth / 2, inputY + 5, 56, gotchiKingState[3] >> 1, true);
        } else {
            drawDownloadUploadAnimationsWithBackground(g, canvasWidth / 2, inputY + 5, 56, gotchiKingState[3] >> 1, true, 16777076);
        }

        if (1 == getSelectedButtonIndex()) {
            drawMirroredTamagotchiPair(g, 39, canvasWidth / 2, y + 240 - 42 + getSpriteHeight(39) / 2, 100, gotchiKingState[3]);
        }

    }

    public static void gotchiKingConnecting(Graphics g, int x, int y) {
        connectingToTamaPlanet(g, x, y);
    }

    public static void gotchiKingBroadcast(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        int var3 = y + 68;
        setColorOfRGBInt(g, 6532583);
        g.fillRect(x, var3, 240, 118);
        int titleY = y + 4;
        // 31: Gotchi King Broadcast
        drawTextWithBackground(g, getText(31), canvasWidth / 2, titleY, 200, 2);
        int var5 = var3 + 15;
        if (gotchiKingState[1] == 4) {
            int var6;
            if (20 < gotchiKingState[3]) {
                var6 = 20;
            } else {
                var6 = gotchiKingState[3];
            }

            var5 -= var6 * 6 / 20;
        }

        drawSprite(g, 6, x, var3 + 76, 0);
        drawSprite(g, 6, x + 120, var3 + 76, 0);
        drawSprite(g, 66, canvasWidth / 2, var3, 2);
        drawBeveledRect(g, (canvasWidth - 36) / 2, var3 + 21, 36, 8, 3805255, 16315136);
        drawBeveledRect(g, (canvasWidth - 36) / 2, var5, 36, 8, 3805255, 16315136);
        if (gotchiKingState[1] == 3) {
            // 75: Enter
            drawTextButton(g, 0, getText(75), canvasWidth / 2, y + 240 - 44, 100, 28, 2, 0);
            drawMirroredTamagotchiPair(g, 39, canvasWidth / 2, y + 240 - 44 + getSpriteHeight(39) / 2, 100, gotchiKingState[3]);
        }

        drawSprite(g, 27, x + 1, y + 1, 0);
        drawSprite(g, 28, x + 240 - 1 - getSpriteWidth(28), y + 1, 0);
        drawSprite(g, 37 + (gotchiKingState[3] >> 1 & 1), x + 240 - 44, var3 + 2, 0);
        drawSprite(g, 12, x + 240 - 38, var3 + 10, 0);
    }

    public static void gotchiKingInvite(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        int titleY = y + 4;
        // 31: Gotchi King Broadcast
        drawTextWithBackground(g, getText(31), canvasWidth / 2, titleY, 200, 2);
        int imageY = y + 68;
        int imageIndex = gotchiKingState[4];
        drawImage(g, gotchiKingImages[imageIndex], x + 0, imageY, 0);
        // 32: Invite
        drawTextButton(g, 0, getText(32), canvasWidth / 2, y + 240 - 44, 160, 28, 2, 0);
        // Antennas
        drawSprite(g, 27, x + 1, y + 1, 0);
        drawSprite(g, 28, x + 240 - 1 - getSpriteWidth(28), y + 1, 0);
        // Blue Z
        drawSprite(g, 37 + (gotchiKingState[3] >> 1 & 1), x + 240 - 44, imageY + 2, 0);
        // LIVE
        drawSprite(g, 12, x + 240 - 38, imageY + 10, 0);
        drawMirroredTamagotchiPair(g, 39, canvasWidth / 2, y + 240 - 44 + getSpriteHeight(39) / 2, 160, gotchiKingState[3]);
    }

    public static void gotchiKingIssuingInvitation(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 77: Issuing Invitation
        drawTextWithBackground(g, getText(77), canvasWidth / 2, y + 2, 200, 2);
        byte stepX = 16;
        byte radius = 6;
        byte circleCount = 11;
        int width = radius + stepX * (circleCount - 1);
        drawRainbowCircles(g, (canvasWidth - width) / 2, y + 68, stepX, 0, radius, gotchiKingState[3], circleCount);
        drawSprite(g, 34, canvasWidth / 2, y + 90, 2);
    }

    public static void gotchiKingInviteTicket(Graphics g, int x, int y) {
        // 69: Invite Ticket
        int textHeight = calculateTextHeight(getText(69));
        int newY;
        if (fullDraw) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // 69: Invite Ticket
            drawTextWithBackground(g, getText(69), canvasWidth / 2, y + 3, currentFont.stringWidth(getText(69)) + 8, 2);
        } else {
            newY = y + getValueFrom6Table(gotchiKingInviteTicketLayout, 0, 1);
            newY += getValueFrom6Table(gotchiKingInviteTicketLayout, 0, 3) / 2;
            newY -= getSpriteHeight(39) / 2;
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, newY, 240, 240 - (newY + getSpriteHeight(39) / 2));
        }

        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 70: Enter the Invitation Ticket No. in your Keitama!
        drawFullWidthScrollingText(g, x, getText(70), y + 3 + textHeight + 3, gotchiKingState[3], 12, 16056665, 16777215);

        for (int i = 0; i < 2; ++i) {
            drawLayoutTextButton(g, i, gotchiKingInviteTicketLayout, 0);
        }

        int codeInputY = y + 3 + textHeight + 3 + currentFontHeight + 12;
        if (fullDraw) {
            drawCodeInputBackground(g, canvasWidth / 2, codeInputY, 2, 0, 16756418, 13722050, 16756418);
            drawDownloadUploadAnimations(g, canvasWidth / 2, codeInputY + 10, 56, gotchiKingState[3] >> 1, false);
        } else {
            drawDownloadUploadAnimationsWithBackground(g, canvasWidth / 2, codeInputY + 10, 56, gotchiKingState[3] >> 1, false, 16756418);
        }

        drawCodeInput(g, canvasWidth / 2, codeInputY + 4, 62, false, fullDraw);
        drawMirroredTamagotchiPair(g, 39, x + getValueFrom6Table(gotchiKingInviteTicketLayout, getSelectedButtonIndex(), 0), y + getValueFrom6Table(gotchiKingInviteTicketLayout, getSelectedButtonIndex(), 1) + getValueFrom6Table(gotchiKingInviteTicketLayout, getSelectedButtonIndex(), 3) / 2 + 1, getValueFrom6Table(gotchiKingInviteTicketLayout, getSelectedButtonIndex(), 2), gotchiKingState[3]);
    }

    public static void gotchiKingSendViaIR(Graphics g, int x, int y) {
        sendViaIR(g, x, y);
    }

    public static void resetTravelMemoryState() {
        clearCodeInput();
        setCursorIndex(0);
        nextTravelMemoryState(0);
    }

    public static void nextTravelMemoryState(int nextStateId) {
        switch (nextStateId) {
            case 0:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                break;
            case 1:
                selectSoftLabel(SOFT_LABEL_MENU);
                travelMemoryState[0] = 0;
                setButtonConfig(2, true);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 2:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                setButtonConfig(1, false);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 3:
                selectSoftLabel(SOFT_LABEL_MENU);
                setButtonConfig(2, true);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 4:
                selectSoftLabel(SOFT_LABEL_MENU);
        }

        fullDrawOnNextPaint = true;
        travelMemoryState[2] = 0;
        travelMemoryState[1] = nextStateId;
    }

    public static void clearDownloadedTravelMemoryData() {
        if (travelMemoryPhoto != null) {
            travelMemoryPhoto.dispose();
            travelMemoryPhoto = null;
        }

        for (int i = 0; i < 2; ++i) {
            travelMemoryTexts[i] = null;
        }

        System.gc();
    }

    public static void travelMemoryFlow() {
        travelMemoryState[2]++;
        switch (travelMemoryState[1]) {
            case 0:
                nextTravelMemoryState(1);
                break;
            case 1:
                travelMemoryCodeInputFlow();
                break;
            case 2:
                downloadTravelMemoryData();
                break;
            case 3:
                displayMemoryPhotoFlow();
                break;
            case 4:
                travelMemoryNoopFlow();
        }

    }

    public static void travelMemoryCodeInputFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(145, 6);
        } else {
            if (travelMemoryState[0] != 0) {
                if (getPressedButtonIndex(isKeyPressed(KEY_SELECT), false, false) != -1) {
                    nextTravelMemoryState(2);
                } else if (!isKeyPressed(KEY_SELECT)) {
                    if (isKeyPressed(KEY_UP_LEFT)) {
                        setCursorIndex(9);
                        travelMemoryState[0] = 0;
                    } else if (isKeyPressed(KEY_DOWN_RIGHT)) {
                        setCursorIndex(0);
                        travelMemoryState[0] = 0;
                    }
                }
            } else if (handleCodeInput(isKeyPressed(KEY_LEFT), isKeyPressed(KEY_RIGHT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN), isKeyPressed(KEY_SELECT), getPressedNumber())) {
                travelMemoryState[0] = 1;
            }

            setSelectedButtonIndex(travelMemoryState[0]);
        }
    }

    public static void downloadTravelMemoryData() {
        clearDownloadedTravelMemoryData();
        prepareTravelMemorySentData();
        DataInputStream inputStream = null;

        try {
            inputStream = sendPreparedDataToServer(13);

            int errorMessageLength = inputStream.read();

            if (errorMessageLength > 0) {
                String errorMessage = readString(inputStream, errorMessageLength);
                showErrorPage(currentPage, 2, 1, errorMessage);
            } else {
                int text1Size = inputStream.read();
                travelMemoryTexts[0] = readString(inputStream, text1Size);

                // travelMemoryState[3] seems unused :(
                travelMemoryState[3] = inputStream.readUnsignedShort();

                int imageSize = inputStream.readUnsignedShort();
                travelMemoryPhoto = readImage(inputStream, imageSize);

                int text2Size = inputStream.readUnsignedShort();
                travelMemoryTexts[1] = readString(inputStream, text2Size);

                nextTravelMemoryState(3);
            }
        } catch (Exception e) {
            log("e:" + e);
            // 92: Communication has failed. Would you like to try again?
            showErrorPage(currentPage, 2, 1, getText(92));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ignored) {
                }
            }

            playSound(6, false);
        }

    }

    public static void prepareTravelMemorySentData() {
        setFirstByteSentToServer();
        byte digit = 4;
        if (travelMemoryDebug) {
            digit = 9;
        }

        setByteSentToServer(1, digit);
        setByteSentToServer(2, 4);

        for (int i = 0; i < 10; ++i) {
            // Copy input digits
            setByteSentToServer(3 + i, getDigitBankA(i));
        }

    }

    public static void displayMemoryPhotoFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(151, 7);
        } else {
            if (6 < travelMemoryState[2]) {
                switch (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN))) {
                    case 0:
                        goToPage(PAGE_TITLE);
                        break;
                    case 1:
                        launchCurrentApp("http://tamapark.gs.keitaiarchive.org/cgi-bin/album.cgi?uid=NULLGWDOCOMO&op=latest");
                }
            }

        }
    }

    public static void travelMemoryNoopFlow() {
    }

    public static void travelMemoryPage(Graphics g, int x, int y) {
        switch (travelMemoryState[1]) {
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
                travelMemoryNoop(g, x, y);
        }

    }

    public static void travelMemoryCodeInput(Graphics g, int x, int y) {
        // 36: Let's look at travel memories from your trips!
        int textHeight = calculateTextHeight(getText(36));
        int codeInputY = y + 2 + textHeight + 2 + currentFontHeight + 3;

        if (fullDraw) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // 36: Let's look at travel memories from your trips!
            drawTextWithBackground(g, getText(36), canvasWidth / 2, y + 2, 230, 2);
            if (travelMemoryDebug) {
                setColorOfRGBInt(g, 16777215);
                g.fillRect(x, y + 240 - 4, 4, 4);
            }
        } else {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y + 240 - 42, 240, 193);
        }

        if (fullDraw) {
            drawCodeInputBackgroundWithHorizontalLine(g, x, codeInputY, 0, 16777215, 2210832, 10873360, 7786961);
            drawDownloadUploadAnimations(g, canvasWidth / 2, codeInputY + 4 + 2, 56, travelMemoryState[2] >> 1, true);
        } else {
            drawDownloadUploadAnimationsWithBackground(g, canvasWidth / 2, codeInputY + 4 + 2, 56, travelMemoryState[2] >> 1, true, 10873360);
        }

        boolean showCursor = 0 == getSelectedButtonIndex() & (travelMemoryState[2] & 4) != 0;
        drawCodeInput(g, canvasWidth / 2, codeInputY + 4, 62, showCursor, fullDraw);
        // 71: Enter the Travel No. shown on your Keitama
        drawFullWidthScrollingText(g, x, getText(71), y + 2 + textHeight + 2, travelMemoryState[2], 12, 16056665, 16777215);
        // 18: OK
        drawTextButton(g, 1, getText(18), canvasWidth / 2, y + 240 - 42, 100, 28, 2, 0);
        if (1 == getSelectedButtonIndex()) {
            drawMirroredTamagotchiPair(g, 35, canvasWidth / 2, y + 240 - 42 + 14 + 1, 100, travelMemoryState[2]);
        }

    }

    public static void printingPhoto(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        int newY = y + 4;
        // 37: Printing Photo
        drawTextWithBackground(g, getText(37), canvasWidth / 2, y + 2, 200, 2);
        setColorOfRGBInt(g, 16770939);
        g.fillRect(x, y + 46, 240, 108);
        // Photo house
        drawSprite(g, 60, canvasWidth / 2, y + 46, 2);
        byte stepX = 16;
        byte radius = 6;
        byte circleCount = 11;
        int width = radius + stepX * (circleCount - 1);
        drawRainbowCircles(g, (canvasWidth - width) / 2, y + 46 + 108 + 8, stepX, 0, radius, 0, circleCount);
    }

    public static void drawMemoryPhoto(Graphics g, int x, int y) {
        int fontHeight = currentFontHeight;
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);

        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);

        int stringWidth = currentFont.stringWidth(travelMemoryTexts[0]) + 8;
        if (stringWidth < travelMemoryPhoto.getWidth()) {
            stringWidth = travelMemoryPhoto.getWidth();
        }

        drawTextWithBackground(g, travelMemoryTexts[0], canvasWidth / 2, y + 2, stringWidth, 2);
        stringWidth = currentFontHeight;
        drawImage(g, travelMemoryPhoto, canvasWidth / 2, y + 2 + fontHeight + 4 + 4, 2);
        drawFullWidthScrollingText(g, x, travelMemoryTexts[1], y + 2 + fontHeight + 4 + 4 + travelMemoryPhoto.getHeight() + 7, travelMemoryState[2], 12, 16056665, 16777215);

        for (int i = 0; i < 2; ++i) {
            drawLayoutTextButton(g, i, memoryPhotoLayout, 0);
        }

        drawMirroredTamagotchiPair(g, 35, x + getValueFrom6Table(memoryPhotoLayout, getSelectedButtonIndex(), 0), y + getValueFrom6Table(memoryPhotoLayout, getSelectedButtonIndex(), 1) + getValueFrom6Table(memoryPhotoLayout, getSelectedButtonIndex(), 3) / 2 + 1, getValueFrom6Table(memoryPhotoLayout, getSelectedButtonIndex(), 2), travelMemoryState[2]);
    }

    public static void travelMemoryNoop(Graphics g, int x, int y) {
    }

    public static int getRegionSelectLayout(int column, int row) {
        return regionSelectLayout[column * 8 + row];
    }

    public static void resetExchangePlazaState() {
        clearCodeInput();
        setButtonConfig(2, true);
        setSelectedButtonIndex(exchangePlazaState[0]);
        nextExchangePlazaState(0);
    }

    public static void nextExchangePlazaState(int stateId) {
        switch (stateId) {
            case 0:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                break;
            case 1:
                selectSoftLabel(SOFT_LABEL_MENU);
                exchangePlazaState[0] = 0;
                setButtonConfig(2, true);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 2:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                setButtonConfig(1, false);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 3:
                exchangePlazaState[3] = 0;
                selectSoftLabel(SOFT_LABEL_MENU);
                break;
            case 4:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                setButtonConfig(1, false);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 5:
                selectSoftLabel(SOFT_LABEL_MENU);
                setButtonConfig(1, false);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 6:
                selectSoftLabel(SOFT_LABEL_MENU);
                setButtonConfig(1, false);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 7:
                selectSoftLabel(SOFT_LABEL_MENU);
                setButtonConfig(2, true);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 8:
                startSendingViaIr(currentPage, 181, 2, 30);
        }

        fullDrawOnNextPaint = true;
        exchangePlazaState[2] = 0;
        exchangePlazaState[1] = stateId;
    }

    public static void clearDownloadedExchangePlazaData() {

        for (int i = 0; i < 2; ++i) {
            if (exchangePlazaImages[i] != null) {
                exchangePlazaImages[i].dispose();
                exchangePlazaImages[i] = null;
            }
        }

        for (int i = 0; i < 3; ++i) {
            exchangePlazaTexts[i] = null;
        }

        System.gc();
    }

    public static void exchangePlazaFlow() {
        exchangePlazaState[2]++;
        switch (exchangePlazaState[1]) {
            case 0:
                nextExchangePlazaState(1);
                break;
            case 1:
                exchangePlazaCodeInputFlow();
                break;
            case 2:
                exchangePlazaLoadingScreenFlow();
                break;
            case 3:
                exchangePlazaRegionSelectFlow();
                break;
            case 4:
                downloadExchangePlazaData();
                break;
            case 5:
                exchangePlazaExchangeFailedFlow();
                break;
            case 6:
                exchangePlazaExchangeSuccessFlow();
                break;
            case 7:
                exchangePlazaExchangeTicketFlow();
                break;
            case 8:
                exchangePlazaSendViaIrFlow();
        }

    }

    public static void exchangePlazaCodeInputFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(158, 6);
        } else {
            if (exchangePlazaState[0] != 0) {
                if (getPressedButtonIndex(isKeyPressed(KEY_SELECT), false, false) != -1) {
                    nextExchangePlazaState(2);
                } else if (!isKeyPressed(KEY_SELECT)) {
                    if (isKeyPressed(KEY_UP_LEFT)) {
                        setCursorIndex(9);
                        exchangePlazaState[0] = 0;
                    } else if (isKeyPressed(KEY_DOWN_RIGHT)) {
                        setCursorIndex(0);
                        exchangePlazaState[0] = 0;
                    }
                }
            } else if (handleCodeInput(isKeyPressed(KEY_LEFT), isKeyPressed(KEY_RIGHT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN), isKeyPressed(KEY_SELECT), getPressedNumber())) {
                exchangePlazaState[0] = 1;
            }

            setSelectedButtonIndex(exchangePlazaState[0]);
        }
    }

    public static void exchangePlazaLoadingScreenFlow() {
        if (exchangePlazaState[2] > 10) {
            playSound(6, false);
            nextExchangePlazaState(3);
        }

    }

    public static void exchangePlazaRegionSelectFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(164, 2);
        } else {
            exchangePlazaState[4] = exchangePlazaState[3];
            if (isKeyPressed(KEY_LEFT)) {
                playSound(4, false);
                exchangePlazaState[3] = getRegionSelectLayout(exchangePlazaState[3], 2);
            } else if (isKeyPressed(KEY_UP)) {
                playSound(4, false);
                exchangePlazaState[3] = getRegionSelectLayout(exchangePlazaState[3], 3);
            } else if (isKeyPressed(KEY_RIGHT)) {
                playSound(4, false);
                exchangePlazaState[3] = getRegionSelectLayout(exchangePlazaState[3], 4);
            } else if (isKeyPressed(KEY_DOWN)) {
                playSound(4, false);
                exchangePlazaState[3] = getRegionSelectLayout(exchangePlazaState[3], 5);
            } else if (isKeyPressed(KEY_SELECT)) {
                playSound(5, false);
                nextExchangePlazaState(4);
            }

        }
    }

    public static void downloadExchangePlazaData() {
        clearDownloadedExchangePlazaData();
        prepareExchangePlazaSentData();
        DataInputStream inputStream = null;

        try {
            inputStream = sendPreparedDataToServer(14);
            unknownOperationOnServerResponse(inputStream);

            int textSize = inputStream.read();

            if (textSize > 0) {
                String errorMessageOr1 = readString(inputStream, textSize);

                if (errorMessageOr1.compareTo("1") == 0) {
                    int imageSize = inputStream.readUnsignedShort();
                    exchangePlazaImages[0] = readImage(inputStream, imageSize);
                    nextExchangePlazaState(5);
                } else {
                    showErrorPage(currentPage, 4, 1, errorMessageOr1);
                }

            } else {
                byte[] passwordData = new byte[10];
                inputStream.read(passwordData);

                int size = inputStream.read();
                exchangePlazaTexts[2] = readString(inputStream, size);

                size = inputStream.read();
                exchangePlazaTexts[0] = readString(inputStream, size);

                size = inputStream.read();
                exchangePlazaTexts[1] = readString(inputStream, size) + getText(49); // // 49: -san

                size = inputStream.readUnsignedShort();
                exchangePlazaImages[0] = readImage(inputStream, size);

                size = inputStream.readUnsignedShort();
                exchangePlazaImages[1] = readImage(inputStream, size);

                parseAndStoreDownloadedPassword(passwordData);
                nextExchangePlazaState(6);
            }
        } catch (Exception e) {
            log("e:" + e);
            // 92: Communication has failed. Would you like to try again?
            showErrorPage(currentPage, 4, 1, getText(92));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ignored) {
                }
            }

            playSound(6, false);
        }

    }

    public static void prepareExchangePlazaSentData() {
        setFirstByteSentToServer();
        setByteSentToServer(1, 5);
        setByteSentToServer(2, 3);

        for (int i = 0; i < 10; ++i) {
            setByteSentToServer(3 + i, getDigitBankA(i));
        }

        setByteSentToServer(13, exchangePlazaState[3] + 1);
    }

    public static void exchangePlazaExchangeFailedFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(167, 1);
        } else {
            if (6 < exchangePlazaState[2] && getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_LEFT), isKeyPressed(KEY_RIGHT)) != -1) {
                nextExchangePlazaState(3);
            }

        }
    }

    public static void exchangePlazaExchangeSuccessFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(166, 1);
        } else {
            if (6 < exchangePlazaState[2] && getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_LEFT), isKeyPressed(KEY_RIGHT)) != -1) {
                nextExchangePlazaState(7);
            }

        }
    }

    public static void exchangePlazaExchangeTicketFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(168, 7);
        } else {
            if (6 < exchangePlazaState[2]) {
                switch (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN))) {
                    case 0:
                        nextExchangePlazaState(8);
                        break;
                    case 1:
                        goToPage(PAGE_TITLE);
                }
            }

        }
    }

    public static void exchangePlazaSendViaIrFlow() {
        if (!hasStartedSendingViaIr()) {
            nextExchangePlazaState(7); // Go back
        } else {
            sendViaIrFlow();
        }
    }

    public static void exchangePlazaPage(Graphics g, int x, int y) {
        switch (exchangePlazaState[1]) {
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
        int codeInputY = y + 2 + textHeight + 2 + currentFontHeight + 3;

        if (fullDraw) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // 41: Trade the regional specialty items you've collected!
            drawTextWithBackground(g, getText(41), canvasWidth / 2, y + 2, 230, 2);
        } else {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y + 240 - 52, 240, 188);
        }

        if (fullDraw) {
            drawCodeInputBackgroundWithHorizontalLine(g, x, codeInputY, 0, 16777215, 3429838, 6728678, 7786961);
            drawDownloadUploadAnimations(g, canvasWidth / 2, codeInputY + 4 + 2, 56, exchangePlazaState[2] >> 1, true);
        } else {
            drawDownloadUploadAnimationsWithBackground(g, canvasWidth / 2, codeInputY + 4 + 2, 56, exchangePlazaState[2] >> 1, true, 6728678);
        }

        // 72: Enter the Exchange No. shown on your Keitama
        drawFullWidthScrollingText(g, x, getText(72), y + 2 + textHeight + 2, exchangePlazaState[2], 12, 16056665, 16777215);
        boolean showCursor = 0 == getSelectedButtonIndex() & (exchangePlazaState[2] & 4) != 0;
        drawCodeInput(g, canvasWidth / 2, codeInputY + 4, 62, showCursor, fullDraw);
        // 18: OK
        drawTextButton(g, 1, getText(18), canvasWidth / 2, y + 240 - 42, 100, 28, 2, 0);
        if (1 == getSelectedButtonIndex()) {
            drawMirroredTamagotchiPair(g, 30, canvasWidth / 2, y + 240 - 42 + 7 + 1, 100, exchangePlazaState[2]);
        }

    }

    public static void exchangePlazaLoadingScreen(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 73: To Exchange Plaza!
        drawTextWithBackground(g, getText(73), canvasWidth / 2, y + 2, currentFont.stringWidth(getText(73)) + 8, 2);
        // Tama park card
        drawSprite(g, 59, canvasWidth / 2, y + 50, 2);
        short width = 176;
        drawRainbowCircles(g, (canvasWidth - width) / 2, y + 160, 16, 0, 6, exchangePlazaState[2], 11);
    }

    public static void exchangePlazaRegionSelect(Graphics g, int x, int y) {
        int selectorX = x + 168;
        int selectorY = y + 76;
        if (fullDraw) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // Tama park banner
            drawSprite(g, 71, x, y + 240 - getSpriteHeight(71), 0);
            // 6: Exchange Plaza
            drawTextWithBackground(g, getText(6), canvasWidth / 2, y + 3, currentFont.stringWidth(getText(6)) + 8, 2);
            // 74: Choose a region to trade with and press OK!
            drawFullWidthScrollingText(g, x, getText(74), y + 3 + calculateTextHeight(getText(6)) + 3, exchangePlazaState[2], 12, 16056665, 16777215);

            drawTextWithBackground(g, getText(getRegionSelectLayout(exchangePlazaState[3], 6)), x + 3, selectorY, 110, 0);

            for (int column = 0; column < 7; ++column) {
                int spriteIndex = getRegionSelectLayout(column, 7);
                if (exchangePlazaState[3] == column && (exchangePlazaState[2] & 4) != 0) {
                    ++spriteIndex;
                }

                drawSprite(g, spriteIndex, selectorX + getRegionSelectLayout(column, 0), selectorY + getRegionSelectLayout(column, 1), 0);
            }
        } else {
            drawFullWidthScrollingText(g, x, getText(74), y + 3 + calculateTextHeight(getText(6)) + 3, exchangePlazaState[2], 12, 16056665, 16777215);
            if (exchangePlazaState[4] != exchangePlazaState[3]) {
                drawTextWithBackground(g, getText(getRegionSelectLayout(exchangePlazaState[3], 6)), x + 3, selectorY, 110, 0);
                drawSprite(g, getRegionSelectLayout(exchangePlazaState[4], 7), selectorX + getRegionSelectLayout(exchangePlazaState[4], 0), selectorY + getRegionSelectLayout(exchangePlazaState[4], 1), 0);
                int spriteIndex = getRegionSelectLayout(exchangePlazaState[3], 7);
                if ((exchangePlazaState[2] & 4) != 0) {
                    ++spriteIndex;
                }

                drawSprite(g, spriteIndex, selectorX + getRegionSelectLayout(exchangePlazaState[3], 0), selectorY + getRegionSelectLayout(exchangePlazaState[3], 1), 0);
            } else if ((exchangePlazaState[2] & 3) == 0) {
                int spriteIndex = getRegionSelectLayout(exchangePlazaState[3], 7);
                if ((exchangePlazaState[2] & 4) != 0) {
                    ++spriteIndex;
                }

                drawSprite(g, spriteIndex, selectorX + getRegionSelectLayout(exchangePlazaState[3], 0), selectorY + getRegionSelectLayout(exchangePlazaState[3], 1), 0);
            }
        }

    }

    public static void exchangePlazaLoadingShip(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        setColorOfRGBInt(g, 6728679);
        g.fillRect(x, y + 114, 240, getSpriteHeight(6));
        // Background city
        drawSprite(g, 6, x, y + 114, 0);
        drawSprite(g, 6, x + 120, y + 114, 0);
        // 43: Landing ship...
        drawTextWithBackground(g, getText(43), canvasWidth / 2, y + 2, currentFont.stringWidth(getText(43)) + 8, 2);
        // Ufo
        drawSprite(g, 76, canvasWidth / 2, y + 102, 2);
    }

    public static void exchangePlazaExchangeFailed(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 89: Exchanged Failed
        drawTextWithBackground(g, getText(89), canvasWidth / 2, y + 2, currentFont.stringWidth(getText(89)) + 8, 2);
        int backgroundY = y + 62;
        // Dotted background
        drawSprite(g, 4, x + 0, backgroundY, 0);
        drawSprite(g, 4, x + 120, backgroundY, 0);
        drawImage(g, exchangePlazaImages[0], canvasWidth / 2, backgroundY - 12, 2);
        int messageBoxHeight = (currentFontHeight + 1) * 2 + currentFontHeight;
        int messageBoxY = y + 240 - (messageBoxHeight + 4) - 2;
        // 88: Retry
        drawTextButton(g, 0, getText(88), canvasWidth / 2, messageBoxY - 44, 100, 28, 2, 0);
        drawBeveledRect(g, (canvasWidth - 232) / 2, messageBoxY, 232, messageBoxHeight + 4, 16056665, 16056665);
        setColorOfRGBInt(g, 16777215);
        // 90: No trading partners were found in that region.
        drawString(g, getText(90), canvasWidth / 2, messageBoxY + 2, ALIGN_CENTER);
        drawMirroredTamagotchiPair(g, 30, canvasWidth / 2, messageBoxY - 44 + getSpriteHeight(30) / 2, 100, exchangePlazaState[2]);
    }

    public static void exchangePlazaExchangeSuccess(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);
        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
        // 48: Exchange Success!
        drawTextWithBackground(g, getText(48), canvasWidth / 2, y + 2, currentFont.stringWidth(getText(48)) + 8, 2);
        drawImage(g, exchangePlazaImages[0], x + 0, y + 42, 0);
        drawImage(g, exchangePlazaImages[1], x + 120, y + 42, 0);
        int messageBoxHeight = (currentFontHeight + 1) * 2 + currentFontHeight;
        int messageBoxY = y + 240 - (messageBoxHeight + 4) - 2;
        // 18: OK
        drawTextButton(g, 0, getText(18), canvasWidth / 2, messageBoxY - 38, 100, 28, 2, 0);
        drawBeveledRect(g, (canvasWidth - 232) / 2, messageBoxY, 232, messageBoxHeight + 4, 16056665, 16056665);
        setColorOfRGBInt(g, 16777215);
        drawString(g, exchangePlazaTexts[0], canvasWidth / 2, messageBoxY + 2, ALIGN_CENTER);
        drawString(g, exchangePlazaTexts[1], canvasWidth / 2, messageBoxY + 2 + currentFontHeight + 1, ALIGN_CENTER);
        drawString(g, exchangePlazaTexts[2], canvasWidth / 2, messageBoxY + 2 + (currentFontHeight + 1) * 2, ALIGN_CENTER);
        drawMirroredTamagotchiPair(g, 30, canvasWidth / 2, messageBoxY - 38 + getSpriteHeight(30) / 2, 100, exchangePlazaState[2]);
    }

    public static void exchangePlazaExchangeTicket(Graphics g, int x, int y) {
        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);

        // 51: Exchange Ticket
        int textHeight = calculateTextHeight(getText(51));
        drawTextWithBackground(g, getText(51), canvasWidth / 2, y + 2, currentFont.stringWidth(getText(51)) + 8, 2);

        // 91: Enter the Exchange Ticket No. in your Keitama!
        drawFullWidthScrollingText(g, x, getText(91), y + 2 + textHeight + 2, exchangePlazaState[2], 12, 16056665, 16777215);

        for (int i = 0; i < 2; ++i) {
            drawLayoutTextButton(g, i, exchangeTicketLayout, 0);
        }

        int codeInputY = y + 3 + textHeight + 3 + currentFontHeight + 12;
        drawCodeInputBackgroundWithHorizontalLine(g, x, codeInputY, 0, 16777215, 16730112, 16751616, 7786961);
        drawCodeInput(g, canvasWidth / 2, codeInputY + 4, 62, false, true);
        drawDownloadUploadAnimations(g, canvasWidth / 2, codeInputY + 10, 56, exchangePlazaState[2] >> 1, false);
        drawMirroredTamagotchiPair(g, 30, x + getValueFrom6Table(exchangeTicketLayout, getSelectedButtonIndex(), 0), y + getValueFrom6Table(exchangeTicketLayout, getSelectedButtonIndex(), 1) + getValueFrom6Table(exchangeTicketLayout, getSelectedButtonIndex(), 3) / 2 + 1, getValueFrom6Table(exchangeTicketLayout, getSelectedButtonIndex(), 2), exchangePlazaState[2]);
    }

    public static void exchangePlazaSendViaIR(Graphics g, int x, int y) {
        sendViaIR(g, x, y);
    }

    public static void openExplanation(int index, int numberOfPages) {
        explanationState[0] = index;
        explanationState[1] = numberOfPages;
        explanationState[2] = 0;
        explanationState[3] = 1;
        selectSoftLabel(SOFT_LABEL_BACK);
    }

    public static void closeExplanation() {
        explanationState[3] = 0;
    }

    public static void scrollExplanation() {
        if (isKeyPressed(KEY_SOFT1)) {
            closeExplanation();
        } else {
            if (isKeyPressed(KEY_DOWN_RIGHT_SELECT)) {
                explanationState[2]++;
                if (explanationState[1] <= explanationState[2]) {
                    closeExplanation();
                }
            } else if (isKeyPressed(KEY_UP_LEFT)) {
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
        if (!isExplanationOpen()) return;

        setColorOfRGBInt(g, 16763955);
        g.fillRect(x, y, 240, 240);

        // Bottom decoration
        drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);

        int lineX = x + 4;
        int lineY = y + (240 - (currentFontHeight + 1) * 7) / 2;

        drawBeveledRect(g, x + 2, lineY - 1, 236, (currentFontHeight + 1) * 7 + 2, 16056665, 16056665);

        String text = getText(explanationState[0] + explanationState[2]);
        int lineCount = splitCount(text, "\n");

        setColorOfRGBInt(g, 16777215);

        for (int i = 0; i < lineCount; ++i) {
            String line = substringBetweenDelimiters(text, i, 1, "\n");
            int colorIndex = line.indexOf("$");

            if (colorIndex == -1) {
                drawString(g, line, lineX, lineY, ALIGN_LEFT);
            } else {
                int currentLineX = lineX;

                do {
                    String linePart = line.substring(0, colorIndex);
                    drawString(g, linePart, currentLineX, lineY, ALIGN_LEFT);

                    currentLineX += currentFont.stringWidth(linePart);
                    if (line.length() - 1 <= colorIndex) {
                        break;
                    }

                    byte baseColor = 0;
                    int parsedColor = baseColor | Integer.parseInt(line.substring(colorIndex + 1, colorIndex + 1 + 3)) << 16;
                    parsedColor |= Integer.parseInt(line.substring(colorIndex + 4, colorIndex + 4 + 3)) << 8;
                    parsedColor |= Integer.parseInt(line.substring(colorIndex + 7, colorIndex + 7 + 3));

                    setColorOfRGBInt(g, parsedColor);

                    line = line.substring(colorIndex + 10);
                    colorIndex = line.indexOf("$");

                } while (colorIndex != -1);

                drawString(g, line, currentLineX, lineY, ALIGN_LEFT);
            }

            lineY += currentFontHeight + 1;
        }
    }

    public static void openMenu() {
        menuState[6] = getSelectedButtonIndex();
        menuState[7] = getCurrentNumberOfButtons();
        menuState[8] = getCanButtonsLoopAround();
        setButtonConfig(4, true);
        setSelectedButtonIndex(0);
        setCurrentExplanation(0, -1);
        menuState[4] = getSelectedButtonIndex();
        menuState[0] = 1; // isMenuOpen
        menuState[5] = 0;
        selectSoftLabel(SOFT_LABEL_BACK);
        nextMenuState(0);
    }

    public static void setCurrentExplanation(int index, int numberOfPages) {
        menuState[2] = index;
        menuState[3] = numberOfPages;
    }

    public static void closeMenu() {
        fullDrawOnNextPaint = true;
        menuState[0] = 0; // isMenuOpen
    }

    public static void nextMenuState(int nextState) {
        switch (nextState) {
            case 0:
            default:
                break;
            case 1:
                setButtonConfig(4, true);
                setSelectedButtonIndex(menuState[4]);
                break;
            case 2:
                openExplanation(menuState[2], menuState[3]);
                break;
            case 3:
                setButtonConfig(2, true);
                setSelectedButtonIndex(1);
                break;
            case 4:
                setButtonConfig(2, true);
                setSelectedButtonIndex(1);
        }

        menuState[5] = 0;
        menuState[1] = nextState;
    }

    public static boolean isMenuOpen() {
        return menuState[0] != 0;
    }

    public static void menuFlow() {
        if (isMenuOpen()) {
            menuState[5]++;
            switch (menuState[1]) {
                case 0:
                    nextMenuState(1);
                    break;
                case 1:
                    mainMenuFlow();
                    break;
                case 2:
                    if (isExplanationOpen()) {
                        scrollExplanation();
                    } else {
                        nextMenuState(1);
                    }
                    break;
                case 3:
                    returnToTitlePageFlow();
                    break;
                case 4:
                    closeAppPageFlow();
            }

        }
    }

    public static void mainMenuFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            setButtonConfig(menuState[7], menuState[8] != 0);
            setSelectedButtonIndex(menuState[6]);
            selectSoftLabel(currentSoftLabelIdx);
            closeMenu();
        } else {
            menuState[4] = getSelectedButtonIndex();
            switch (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN))) {
                case 0:
                    if (0 < menuState[3]) {
                        nextMenuState(2);
                    }
                    break;
                case 1:
                    goToPage(PAGE_TITLE);
                    break;
                case 2:
                    nextMenuState(4);
                    break;
                case 3:
                    toggleSound();
                    saveGame();
            }

        }
    }

    public static void returnToTitlePageFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            nextMenuState(1);
        } else {
            switch (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_UP_LEFT), isKeyPressed(KEY_DOWN_RIGHT))) {
                case 0:
                    closeMenu();
                    goToPage(PAGE_TITLE);
                    break;
                case 1:
                    nextMenuState(1);
            }

        }
    }

    public static void closeAppPageFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            nextMenuState(1);
        } else {
            switch (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_UP_LEFT), isKeyPressed(KEY_DOWN_RIGHT))) {
                case 0:
                    exitGame();
                    break;
                case 1:
                    nextMenuState(1);
            }

        }
    }

    public static void drawMenuPages(Graphics g, int x, int y) {
        if (isMenuOpen()) {
            switch (menuState[1]) {
                case 0:
                default:
                    break;
                case 1:
                    mainMenu(g, x, y);
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

    public static void mainMenu(Graphics g, int x, int y) {
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

        drawMenuEggs(g, GameApp.canvasWidth / 2, newY + 2 + currentFontHeight + 6 + 2, 86, menuState[5]);
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
        drawMenuEggs(g, GameApp.canvasWidth / 2, newY + 2 + currentFontHeight + 6 + 2, 86, menuState[5]);
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
        drawMenuEggs(g, GameApp.canvasWidth / 2, newY + 2 + currentFontHeight + 6 + 2, 86, menuState[5]);
    }

    public static void drawButton(Graphics g, String text, int buttonIdx, int y) {
        int textColor;
        int backgroundColor;
        if (buttonIdx == getSelectedButtonIndex()) {
            backgroundColor = 16056665;
            textColor = 16777215;
        } else {
            backgroundColor = 7786961;
            textColor = 3096512;
        }

        drawBeveledRect(g, (canvasWidth - 220) / 2, y, 220, currentFontHeight + 4, backgroundColor, backgroundColor);
        setColorOfRGBInt(g, textColor);
        drawString(g, text, canvasWidth / 2, y + 2, ALIGN_CENTER);
    }

    public static void drawMenuEggs(Graphics g, int x, int y, int gap, int time) {
        int eggX = x - gap;

        for (int i = 0; i < 3; eggX += gap) {
            int spriteIndex = 69 + (i + (time >> 2) & 1);
            drawSprite(g, spriteIndex, eggX - getSpriteWidth(spriteIndex) / 2, y, 0);
            ++i;
        }

    }

    public static void showErrorPage(int pageId, int actionId1, int actionId2, String errorMessage) {
        errorState[0] = pageId;
        errorState[1] = actionId1;
        errorState[2] = actionId2;
        errorPageText = errorMessage;
        setButtonConfig(2, true);
        setSelectedButtonIndex(1);
        setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
        selectSoftLabel(SOFT_LABEL_EMPTY);
        errorState[3] = 1; // show error page
    }

    public static void closeErrorPage() {
        errorState[3] = 0; // hide error page
        errorPageText = null;
    }

    public static void openPreviousPageWithFlowStep(int flowStep) {
        currentPage = errorState[0];
        closeErrorPage();
        switch (errorState[0]) {
            case 7:
                nextShoppingCenterState(flowStep);
                break;
            case 8:
                nextParentCallState(flowStep);
                break;
            case 9:
                nextGotchiKingState(flowStep);
                break;
            case 10:
                nextTravelMemoryState(flowStep);
                break;
            case 11:
                nextExchangePlazaState(flowStep);
        }

    }

    public static boolean shouldShowErrorPage() {
        return errorState[3] != 0;
    }

    public static void errorPageFlow() {
        if (shouldShowErrorPage()) {
            if (isKeyPressed(KEY_DOWN)) {
                errorPageUnusedCounter -= (currentFontHeight + 1) * 7;
            } else if (isKeyPressed(KEY_UP)) {
                errorPageUnusedCounter += (currentFontHeight + 1) * 7;
            }

            if (isKeyPressed(KEY_0)) {
                errorPageUnusedToggle = !errorPageUnusedToggle;
            }

            switch (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_LEFT), isKeyPressed(KEY_RIGHT))) {
                case 0:
                    openPreviousPageWithFlowStep(errorState[1]);
                    break;
                case 1:
                    openPreviousPageWithFlowStep(errorState[2]);
            }

        }
    }

    public static void errorPage(Graphics g, int x, int y) {
        if (shouldShowErrorPage()) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // Bottom decoration
            drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
            int lineCount = splitCount(errorPageText, "\n");
            int textHeight = (lineCount - 1) * (currentFontHeight + 1) + currentFontHeight + 4;
            int newY = y + (240 - (textHeight + 4 + 8)) / 2;
            drawBeveledRect(g, (canvasWidth - 232) / 2, newY, 232, textHeight, 16056665, 16056665);
            setColorOfRGBInt(g, 16777215);
            drawString(g, errorPageText, canvasWidth / 2, newY + 2, ALIGN_CENTER);
            // 88: Retry
            drawTextButton(g, 0, getText(88), canvasWidth / 2 - 8, newY + textHeight + 4, 100, 28, 1, 0);
            // 9: Back
            drawTextButton(g, 1, getText(9), canvasWidth / 2 + 8, newY + textHeight + 4, 100, 28, 0, 0);
        }
    }

    public static String getText(int idx) {
        return texts[idx];
    }

    public static boolean loadTexts() {
        DataInputStream stream = null;
        boolean success = true;
        byte indexOffset = 100;

        try {
            int[] lengths = loadShortArray(128);
            int pos = 128 + (lengths.length + 1) * 2;

            for (int i = 0; i < indexOffset; ++i) {
                pos += lengths[i];
            }

            stream = Connector.openDataInputStream("scratchpad:///0;pos=" + pos);

            for (int i = 0; i < 183; ++i) {
                byte[] data = new byte[lengths[indexOffset + i]];
                stream.read(data);
                texts[i] = new String(data);
                data = null;
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
                } catch (Exception ignored) {
                }
            }

        }

        return success;
    }

    public static void setButtonConfig(int numberOfButtons, boolean canLoopAround) {
        buttonState[1] = numberOfButtons;
        buttonState[2] = canLoopAround ? 1 : 0;
        buttonState[13] = 0; // not pressed
    }

    public static void setSelectedButtonIndex(int index) {
        buttonState[0] = index;
    }

    public static void setButtonTheme(int selectedOutlineColor, int selectedColor, int unused1, int selectedTextColor, int selectedShadowColor, int outlineColor, int color, int unused2, int textColor, int shadowColor) {
        buttonState[3] = selectedOutlineColor;
        buttonState[4] = selectedColor;
        buttonState[5] = unused1;
        buttonState[6] = selectedTextColor;
        buttonState[7] = selectedShadowColor;
        buttonState[8] = outlineColor;
        buttonState[9] = color;
        buttonState[10] = unused2;
        buttonState[11] = textColor;
        buttonState[12] = shadowColor;
    }

    public static void setButtonTheme2(int selectedOutlineColor, int selectedColor, int unused1, int selectedTextColor, int outlineColor, int color, int unused2, int textColor) {
        setButtonTheme(selectedOutlineColor, selectedColor, unused1, selectedTextColor, 0, outlineColor, color, unused2, textColor, 0);
    }

    public static int getPressedButtonIndex(boolean pressButton, boolean decrementButtonIndex, boolean incrementButtonIndex) {
        int selected = -1;
        if (buttonState[13] != 0) {
            selected = getSelectedButtonIndex();
            buttonState[13] = 0; // not pressed
            playSound(5, false);
        } else if (pressButton) {
            buttonState[13] = 1; // pressed
            fullDrawOnNextPaint = true;
        } else {
            boolean buttonIndexChanged = false;

            if (decrementButtonIndex) {
                buttonIndexChanged = true;
                buttonState[0]--;
                if (buttonState[0] < 0) {
                    if (buttonState[2] != 0) {
                        // selectedButtonIdx = numberOfButtons - 1
                        buttonState[0] = buttonState[1] - 1;
                    } else {
                        buttonIndexChanged = false;
                        buttonState[0] = 0;
                    }
                }
            }

            if (incrementButtonIndex) {
                buttonIndexChanged = true;
                buttonState[0]++;
                if (buttonState[1] <= buttonState[0]) {
                    if (buttonState[2] != 0) {
                        buttonState[0] = 0;
                    } else {
                        buttonIndexChanged = false;
                        buttonState[0] = buttonState[1] - 1;
                    }
                }
            }

            if (buttonIndexChanged) {
                playSound(4, false);
            }

            buttonState[13] = 0; // not pressed
        }

        return selected;
    }

    public static int getSelectedButtonIndex() {
        return buttonState[0];
    }

    public static int getCurrentNumberOfButtons() {
        return buttonState[1];
    }

    public static int getCanButtonsLoopAround() {
        return buttonState[2];
    }

    public static void drawLayoutTextButton(Graphics g, int buttonIndex, int[] layout, int rounding) {
        drawTextButton(g, buttonIndex, getText(getValueFrom6Table(layout, buttonIndex, 4)), rootX + getValueFrom6Table(layout, buttonIndex, 0), rootY + getValueFrom6Table(layout, buttonIndex, 1), getValueFrom6Table(layout, buttonIndex, 2), getValueFrom6Table(layout, buttonIndex, 3), getValueFrom6Table(layout, buttonIndex, 5), rounding);
    }

    public static void drawLayoutSpriteButton(Graphics g, int buttonIndex, int[] layout, int rounding) {
        drawSpriteButton(g, buttonIndex, getValueFrom7Table(layout, buttonIndex, 4), getValueFrom7Table(layout, buttonIndex, 5), rootX + getValueFrom7Table(layout, buttonIndex, 0), rootY + getValueFrom7Table(layout, buttonIndex, 1), getValueFrom7Table(layout, buttonIndex, 2), getValueFrom7Table(layout, buttonIndex, 3), getValueFrom7Table(layout, buttonIndex, 6), rounding);
    }

    public static int getValueFrom6Table(int[] table, int row, int column) {
        return table[row * 6 + column];
    }

    public static int getValueFrom7Table(int[] table, int row, int column) {
        return table[row * 7 + column];
    }

    public static void drawTextButton(Graphics g, int buttonIndex, String text, int x, int y, int width, int height, int align, int rounding) {
        int newX;
        if (align == 2) {
            newX = x - width / 2;
        } else if (align == 0) {
            newX = x;
        } else {
            newX = x - width;
        }

        boolean isPressed = false;
        int outlineColor;
        int color;
        int unused;
        int textColor;
        int shadowColor;
        if (buttonIndex == getSelectedButtonIndex()) {
            outlineColor = buttonState[3];
            color = buttonState[4];
            unused = buttonState[5];
            textColor = buttonState[6];
            shadowColor = buttonState[7];
            isPressed = buttonState[13] != 0;
        } else {
            outlineColor = buttonState[8];
            color = buttonState[9];
            unused = buttonState[10];
            textColor = buttonState[11];
            shadowColor = buttonState[12];
        }

        switch (rounding) {
            case 0:
                drawRoundedTextButton(g, text, newX, y, width, height, outlineColor, color, unused, textColor, shadowColor, isPressed);
                break;
            case 1:
                drawRectangularTextButton(g, text, newX, y, width, height, outlineColor, color, unused, textColor, shadowColor, isPressed);
        }

    }

    public static void drawSpriteButton(Graphics g, int buttonIndex, int pressedSprite, int sprite, int x, int y, int width, int height, int align, int rounding) {
        int newX;
        if (align == 2) {
            newX = x - width / 2;
        } else if (align == 0) {
            newX = x;
        } else {
            newX = x - width;
        }

        boolean isHighlighted = false;
        int outlineColor;
        int innerColor;
        int unstyledColor;
        int spriteIndex;
        if (buttonIndex == getSelectedButtonIndex()) {
            outlineColor = buttonState[3];
            innerColor = buttonState[4];
            unstyledColor = buttonState[7];
            isHighlighted = buttonState[13] != 0;
            spriteIndex = pressedSprite;
        } else {
            outlineColor = buttonState[8];
            innerColor = buttonState[9];
            unstyledColor = buttonState[12];
            spriteIndex = sprite;
        }

        switch (rounding) {
            case 0:
                drawRoundedSpriteButton(g, spriteIndex, newX, y, width, height, outlineColor, innerColor, unstyledColor, isHighlighted);
                break;
            case 1:
                drawRectangularSpriteButton(g, spriteIndex, newX, y, width, height, outlineColor, innerColor, unstyledColor, isHighlighted);
        }

    }

    public static void drawRoundedTextButton(Graphics g, String text, int x, int y, int width, int height, int outlineColor, int color, int unused, int textColor, int shadowColor, boolean isPressed) {
        drawRoundedButtonBackground(g, x, y, width, height, outlineColor, color, shadowColor, isPressed);
        if (isPressed) {
            y += 2;
        }

        int textHeight = splitCount(text, "\n");
        textHeight = currentFontHeight + (textHeight - 1) * (currentFontHeight + 1);
        setColorOfRGBInt(g, textColor);
        drawString(g, text, x + width / 2, y + (height - textHeight) / 2, ALIGN_CENTER);
    }

    public static void drawRoundedSpriteButton(Graphics g, int spriteIndex, int x, int y, int width, int height, int outlineColor, int color, int shadowColor, boolean isPressed) {
        drawRoundedButtonBackground(g, x, y, width, height, outlineColor, color, shadowColor, isPressed);
        if (isPressed) {
            y += 2;
        }

        drawSprite(g, spriteIndex, x + width / 2, y + (height - getSpriteHeight(spriteIndex)) / 2, 2);
    }

    public static void drawRoundedButtonBackground(Graphics g, int x, int y, int width, int height, int outlineColor, int color, int shadowColor, boolean isPressed) {
        if (!isPressed) {
            setColorOfRGBInt(g, shadowColor);
            g.fillArc(x - 1, y + 2 - 1, height + 2, height + 2, 180, 90);
            g.fillArc(x + width - height - 2, y + 2 - 1, height + 2, height + 2, -90, 90);
            g.fillRect(x + height / 2, y + height + 1, width - height, 2);
        } else {
            y += 2;
        }

        setColorOfRGBInt(g, outlineColor);
        g.fillArc(x - 1, y - 1, height + 2, height + 2, 90, 180);
        g.fillArc(x + width - height - 2, y - 1, height + 2, height + 2, -90, 180);
        g.fillRect(x + height / 2, y - 1, width - height, 1);
        g.fillRect(x + height / 2, y + height, width - height, 1);

        setColorOfRGBInt(g, color);
        g.fillArc(x, y, height, height, 90, 180);
        g.fillArc(x + width - height - 1, y, height, height, -90, 180);
        g.fillRect(x + height / 2, y, width - height, height);
    }

    public static void drawRectangularTextButton(Graphics g, String text, int x, int y, int width, int height, int borderColor, int color, int unused, int textColor, int shadowColor, boolean isPressed) {
        drawRectangularButtonBackground(g, x, y, width, height, borderColor, color, shadowColor, isPressed);
        if (isPressed) {
            y += 2;
        }

        int textHeight = splitCount(text, "\n");
        textHeight = currentFontHeight + (textHeight - 1) * (currentFontHeight + 1);
        setColorOfRGBInt(g, textColor);
        drawString(g, text, x + width / 2, y + (height - textHeight) / 2, ALIGN_CENTER);
    }

    public static void drawRectangularSpriteButton(Graphics g, int spriteIndex, int x, int y, int width, int height, int borderColor, int color, int shadowColor, boolean isPressed) {
        drawRectangularButtonBackground(g, x, y, width, height, borderColor, color, shadowColor, isPressed);
        if (isPressed) {
            y += 2;
        }

        drawSprite(g, spriteIndex, x + width / 2, y + (height - getSpriteHeight(spriteIndex)) / 2, 2);
    }

    public static void drawRectangularButtonBackground(Graphics g, int x, int y, int width, int height, int borderColor, int color, int shadowColor, boolean isPressed) {
        if (!isPressed) {
            drawBeveledRect(g, x, y + height - 2, width, 4, shadowColor, shadowColor);
        } else {
            y += 2;
        }

        drawBeveledRect(g, x, y, width, height, borderColor, color);
    }

    public static void clearCodeInput() {
        for (int i = 0; i < 4; ++i) {
            codeInputState[i] = 0;
        }

        setCursorIndex(0);
    }

    public static void setCursorIndex(int index) {
        codeInputState[5] = codeInputState[4];
        codeInputState[4] = index;
    }

    public static void parseAndStoreDownloadedPassword(byte[] passwordData) {
        for (int i = 0; i < 10; ++i) {
            setDigitBankA(i, passwordData[i]);
        }

    }

    public static void setDigitBankA(int digitIndex, int digit) {
        digit %= 10;
        int packedInt = codeInputState[0 + digitIndex / 8];
        int shift = 4 * (7 - digitIndex % 8);
        packedInt &= ~(15 << shift);
        packedInt |= digit << shift;
        codeInputState[0 + digitIndex / 8] = packedInt;
    }

    public static void setDigitBankB(int digitIndex, int digit) {
        digit %= 10;
        int packedInt = codeInputState[2 + digitIndex / 8];
        int shift = 4 * (7 - digitIndex % 8);
        packedInt &= ~(15 << shift);
        packedInt |= digit << shift;
        codeInputState[2 + digitIndex / 8] = packedInt;
    }

    public static void generateDerivedCodeInBankB() {
        int keyDigit = getDigitBankA(1);

        for (int i = 0; i < 10; ++i) {
            setDigitBankB(digitShuffleTable[keyDigit * 10 + i], getDigitBankA(i));
        }

    }

    public static int getDigitBankA(int digitIndex) {
        int digit = codeInputState[0 + digitIndex / 8] >> 4 * (7 - digitIndex % 8) & 15;
        return digit;
    }

    public static int getDigitBankB(int digitIndex) {
        int digit = codeInputState[2 + digitIndex / 8] >> 4 * (7 - digitIndex % 8) & 15;
        return digit;
    }

    public static int getCursorIndex() {
        return codeInputState[4];
    }

    public static int getPreviousCursorIndex() {
        return codeInputState[5];
    }

    public static boolean handleCodeInput(boolean moveLeft, boolean moveRight, boolean moveUp, boolean moveDown, boolean incrementDigit, int directDigit) {
        int cursorIndex = codeInputState[4];
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
        } else if (moveUp) {
            cursorIndex -= 5;
            if (cursorIndex < 0) {
                allFilled = true;
                cursorIndex += 10;
            }

            setCursorIndex(cursorIndex);
        } else if (moveDown) {
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
        if (isKeyPressed(KEY_0)) {
            number = 0;
        } else if (isKeyPressed(KEY_1)) {
            number = 1;
        } else if (isKeyPressed(KEY_2)) {
            number = 2;
        } else if (isKeyPressed(KEY_3)) {
            number = 3;
        } else if (isKeyPressed(KEY_4)) {
            number = 4;
        } else if (isKeyPressed(KEY_5)) {
            number = 5;
        } else if (isKeyPressed(KEY_6)) {
            number = 6;
        } else if (isKeyPressed(KEY_7)) {
            number = 7;
        } else if (isKeyPressed(KEY_8)) {
            number = 8;
        } else if (isKeyPressed(KEY_9)) {
            number = 9;
        }

        return number;
    }

    public static void drawAllDigits(Graphics g, int x, int y, int align) {
        for (int i = 0; i < 10; ++i) {
            drawDigit(g, x, y, align, i);
        }

    }

    public static void drawDigit(Graphics g, int x, int y, int align, int index) {
        if (align == 2) {
            x -= 35;
        } else if (align == 1) {
            x -= 71;
        }

        x += index * 15;
        if (5 <= index) {
            y += 26;
            x -= 75;
        }

        drawString(g, "" + getDigitBankA(index), x, y, ALIGN_LEFT);
    }

    public static void drawRainbowCircles(Graphics g, int x, int y, int stepX, int stepY, int radius, int startColorOffset, int circleCount) {
        int colorIndex = startColorOffset % 7;
        if (colorIndex < 0) {
            colorIndex += 7;
        }

        for (int i = 0; i < circleCount; ++i) {
            setColorOfRGBInt(g, rainbowColors[colorIndex]);
            g.fillArc(x, y, radius * 2, radius * 2, 0, 360);
            ++colorIndex;
            colorIndex %= 7;
            x += stepX;
            y += stepY;
        }

    }


    public static void drawPixelBitmap(Graphics g, byte[] bitmapData, int headerOffset, int pixelScale, int color, int x, int y, boolean flipHorizontally) {
        int pixelWidth = (bitmapData[headerOffset + 0] & 255) + 7 >>> 3;
        int pixelHeight = bitmapData[headerOffset + 1] & 255;
        setColorOfRGB(g, color >>> 16 & 255, color >>> 8 & 255, color & 255);
        g.setClip(0, 0, canvasWidth + 16, canvasHeight + 16);
        g.fillRect(canvasWidth, canvasHeight, pixelScale, pixelScale);
        int dataIndex = headerOffset + 2;
        int xStep;
        if (flipHorizontally) {
            xStep = -pixelScale;
            x += pixelScale * ((bitmapData[headerOffset + 0] & 255) - 1);
        } else {
            xStep = pixelScale;
        }

        for (int row = 0; row < pixelHeight; ++row) {
            int currentX = x;

            for (int byteInRow = 0; byteInRow < pixelWidth; ++byteInRow) {
                for (int bit = 0; bit < 8; ++bit) {
                    if ((bitmapData[dataIndex] >>> 7 - bit & 1) != 0) {
                        g.fillRect(currentX, y, pixelScale, pixelScale);
                    }

                    currentX += xStep;
                }

                ++dataIndex;
            }

            y += pixelScale;
        }

    }

    public static void drawPixelBitmap2(Graphics g, byte[] bitmapData, int headerOffset, int pixelScale, int color, int x, int y, boolean flipHorizontally) {
        int pixelWidth = (bitmapData[headerOffset + 0] & 255) + 7 >>> 3;
        int pixelHeight = bitmapData[headerOffset + 1] & 255;
        setColorOfRGB(g, color >>> 16 & 255, color >>> 8 & 255, color & 255);
        int dataIndex = headerOffset + 2;
        int xStep;
        if (flipHorizontally) {
            xStep = -pixelScale;
            x += pixelScale * ((bitmapData[headerOffset + 0] & 255) - 1);
        } else {
            xStep = pixelScale;
        }

        for (int row = 0; row < pixelHeight; ++row) {
            int currentX = x;

            for (int byteInRow = 0; byteInRow < pixelWidth; ++byteInRow) {
                for (int bit = 0; bit < 8; ++bit) {
                    if ((bitmapData[dataIndex] >>> 7 - bit & 1) != 0) {
                        g.fillRect(currentX, y, pixelScale, pixelScale);
                    }

                    currentX += xStep;
                }

                ++dataIndex;
            }

            y += pixelScale;
        }

    }

    public static void setByteSentToServer(int index, int data) {
        bytesSentToServer[index] = (byte) data;
    }

    public static void setFirstByteSentToServer() {
        setByteSentToServer(0, 16);
    }

    public static DataInputStream sendPreparedDataToServer(int length) throws Exception {
        return sendDataToServer(bytesSentToServer, length);
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
            } catch (Exception ignored) {
            }

            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception ignored) {
            }

            try {
                if (http != null) {
                    http.close();
                }
            } catch (Exception ignored) {
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
        imageReadInfo = 0L;
        imageReadInfo |= (long) expectedLength;
        byte[] imageData = new byte[expectedLength];

        int bytesRead;
        for (bytesRead = 0; bytesRead < expectedLength; ++bytesRead) {
            int readValue = inputStream.read();
            if (readValue == -1) {
                break;
            }

            imageData[bytesRead] = (byte) readValue;
        }

        imageReadInfo |= (long) bytesRead << 32;
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

    public static void unknownOperationOnServerResponse(DataInputStream inputStream) throws Exception {
        // This is a no-op. Maybe just a decompilation artifact?
        // (public static void aW(DataInputStream var0) throws Exception)
    }

    public static IrRemoteControlFrame[] createIrRemoteControlFrames(int n) {
        IrRemoteControlFrame[] res = new IrRemoteControlFrame[n];

        for (int i = 0; i < n; ++i) {
            res[i] = new IrRemoteControlFrame();
        }

        return res;
    }

    public static void startSendingViaIr(int currentPage, int var1, int var2, int var3) {
        irState[2] = currentPage;
        irState[3] = var1;
        irState[4] = var2;
        irState[5] = var3;
        nextIrState(1);
    }

    public static void nextIrState(int state) {
        switch (state) {
            case 0:
                irRemoteControl.stop();
                break;
            case 1:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                setButtonConfig(1, false);
                setSelectedButtonIndex(0);
                setButtonTheme2(16777215, 7786961, 6594720, 16777215, 16777215, 6594720, 16763955, 13158600);
                break;
            case 2:
                selectSoftLabel(SOFT_LABEL_EMPTY);
                setButtonConfig(1, false);
                setSelectedButtonIndex(0);
                break;
            case 3:
                selectSoftLabel(SOFT_LABEL_MENU);
                setButtonConfig(3, true);
                setSelectedButtonIndex(0);
                break;
            case 4:
                irRemoteControl.stop();
                selectSoftLabel(SOFT_LABEL_EMPTY);
                setButtonConfig(1, false);
                setSelectedButtonIndex(0);
                break;
            case 5:
                irRemoteControl.stop();
                selectSoftLabel(SOFT_LABEL_EMPTY);
                setButtonConfig(1, false);
                setSelectedButtonIndex(0);
        }

        irState[1] = 0;
        irState[0] = state;
    }

    public static void prepareDataSentViaIr() {
        generateDerivedCodeInBankB();
        setByteSentViaIr(0, 96);
        setByteSentViaIr(1, 8);
        setByteSentViaIr(2, 0);
        setByteSentViaIr(3, 0);

        for (int i = 0; i < 10; i += 2) {
            int value = reverse4Bits(getDigitBankB(i)) << 4 | reverse4Bits(getDigitBankB(i + 1));
            setByteSentViaIr(4 + i / 2, value);
        }

        setByteSentViaIr(9, 0);
        setByteSentViaIr(10, 0);
        setByteSentViaIr(11, 0);
        setByteSentViaIr(12, 0);
        setByteSentViaIr(13, 0);
        setByteSentViaIr(14, 0);

        int checksum = 0;
        for (int i = 0; i < 15; ++i) {
            checksum += reverse8Bits(bytesToSendViaIr[i]);
        }

        setByteSentViaIr(15, reverse8Bits(checksum & 255));
    }

    public static void setByteSentViaIr(int index, int value) {
        bytesToSendViaIr[index] = (byte) value;
    }

    public static int reverse4Bits(int data) {
        return (data & 1) << 3 | (data & 2) << 1 | (data & 4) >> 1 | (data & 8) >> 3;
    }

    public static int reverse8Bits(int data) {
        return (data & 1) << 7 | (data & 2) << 5 | (data & 4) << 3 | (data & 8) << 1 | (data & 16) >> 1 | (data & 32) >> 3 | (data & 64) >> 5 | (data & 128) >> 7;
    }

    public static boolean hasStartedSendingViaIr() {
        return irState[0] != 0;
    }

    public static void sendViaIrFlow() {
        if (hasStartedSendingViaIr()) {
            irState[1]++;
            switch (irState[0]) {
                case 1:
                    sendIrFrames();
                    break;
                case 2:
                    sendViaIrSendingFlow();
                    break;
                case 3:
                    sendViaIrSendingCompleteFlow();
                    break;
                case 4:
                case 5:
                    sendViaIrErrorFlow();
            }

        }
    }

    public static void sendIrFrames() {
        try {
            irRemoteControl.stop();
            prepareDataSentViaIr();
            irRemoteControl.setCarrier(131, 131);
            irRemoteControl.setCode0(0, 470, 730);
            irRemoteControl.setCode1(0, 470, 1330);
            irFrames[0].setFrameData(bytesToSendViaIr, 128);
            irFrames[0].setRepeatCount(3);
            irFrames[0].setFrameDuration(2500);
            irFrames[0].setStartHighDuration(9600);
            irFrames[0].setStartLowDuration(2400);
            irFrames[0].setStopHighDuration(1200);
            irRemoteControl.send(1, irFrames);
            irSendTimestamp = (new Date()).getTime();
            nextIrState(2);
        } catch (Exception e) {
            nextIrState(5);
        }

    }

    public static void sendViaIrSendingFlow() {
        if (getPressedButtonIndex(isKeyPressed(KEY_SELECT), false, false) != -1) {
            nextIrState(4);
        } else {
            if (750L < (new Date()).getTime() - irSendTimestamp) {
                irRemoteControl.stop();
                playSound(6, false);
                nextIrState(3);
            }

        }
    }

    public static void sendViaIrSendingCompleteFlow() {
        if (isKeyPressed(KEY_SOFT1)) {
            openMenu();
            setCurrentExplanation(irState[3], irState[4]);
        } else {
            switch (getPressedButtonIndex(isKeyPressed(KEY_SELECT), isKeyPressed(KEY_UP), isKeyPressed(KEY_DOWN))) {
                case 0:
                    if (irState[2] == 7) {
                        nextIrState(0);
                        nextShoppingCenterState(0);
                    } else {
                        nextIrState(0);
                        goToPage(PAGE_TITLE);
                    }
                    break;
                case 1:
                    nextIrState(0);
                    break;
                case 2:
                    nextIrState(1);
            }

        }
    }

    public static void sendViaIrErrorFlow() {
        if (getPressedButtonIndex(isKeyPressed(KEY_SELECT), false, false) != -1) {
            nextIrState(0);
        }

    }

    public static void sendViaIR(Graphics g, int x, int y) {
        if (hasStartedSendingViaIr()) {
            setColorOfRGBInt(g, 16763955);
            g.fillRect(x, y, 240, 240);
            // Bottom decoration
            drawSprite(g, 0, x, y + 240 - getSpriteHeight(0), 0);
            switch (irState[0]) {
                case 2:
                    sendViaIrSending(g, x, y);
                    break;
                case 3:
                    sendViaIrComplete(g, x, y);
                    break;
                case 4:
                    sendViaIrInterrupted(g, x, y);
                    break;
                case 5:
                    sendViaIrFailed(g, x, y);
            }

        }
    }

    public static void sendViaIrSending(Graphics g, int x, int y) {
        // 98: Sending...
        drawTextWithBackground(g, getText(98), canvasWidth / 2, y + 5, currentFont.stringWidth(getText(98)) + 8, 2);
        drawSprite(g, 67, canvasWidth / 2 + (irState[1] * 8 - 60), y + 50, 2);
        // 38: End
        drawTextButton(g, 0, getText(38), canvasWidth / 2, y + 50 + getSpriteHeight(67) + 10, currentFont.stringWidth(getText(38)) + 8, currentFontHeight + 4, 2, 0);
        // 18: OK
        drawMirroredTamagotchiPair(g, irState[5], canvasWidth / 2, y + 50 + getSpriteHeight(67) + 10 + (currentFontHeight + 4) / 2, currentFont.stringWidth(getText(18)) + 20, irState[1]);
    }

    public static void sendViaIrComplete(Graphics g, int x, int y) {
        // 99: Sending complete!
        drawTextWithBackground(g, getText(99), canvasWidth / 2, y + 5, currentFont.stringWidth(getText(99)) + 8, 2);
        drawSprite(g, 68, canvasWidth / 2, y + 50, 2);
        int[] layout;

        if (irState[2] == 7) {
            layout = shoppingCenterIrSendLayout;
        } else {
            layout = generalIrSendLayout;
        }

        for (int i = 0; i < 3; ++i) {
            drawLayoutTextButton(g, i, layout, 0);
        }

        drawMirroredTamagotchiPair(g, irState[5], x + getValueFrom6Table(layout, getSelectedButtonIndex(), 0), y + getValueFrom6Table(layout, getSelectedButtonIndex(), 1) + getValueFrom6Table(layout, getSelectedButtonIndex(), 3) / 2, getValueFrom6Table(layout, getSelectedButtonIndex(), 2) - 10, irState[1]);
    }

    public static void sendViaIrInterrupted(Graphics g, int x, int y) {
        // 96: Interrupted...
        drawTextWithBackground(g, getText(96), canvasWidth / 2, y + 5, currentFont.stringWidth(getText(96)) + 8, 2);
        transmissionErrorPage(g, x, y);
    }

    public static void sendViaIrFailed(Graphics g, int x, int y) {
        // 97: Transmission failed
        drawTextWithBackground(g, getText(97), canvasWidth / 2, y + 5, currentFont.stringWidth(getText(97)) + 8, 2);
        transmissionErrorPage(g, x, y);
    }

    public static void transmissionErrorPage(Graphics g, int x, int y) {
        drawSprite(g, 29, canvasWidth / 2, y + 50, 2);
        // 18: OK
        drawTextButton(g, 0, getText(18), canvasWidth / 2, y + 50 + getSpriteHeight(29) + 10, currentFont.stringWidth(getText(18)) + 8, currentFontHeight + 4, 2, 0);
        drawMirroredTamagotchiPair(g, irState[5], canvasWidth / 2, y + 50 + getSpriteHeight(29) + 10 + (currentFontHeight + 4) / 2, currentFont.stringWidth(getText(18)) + 20, irState[1]);
    }

    public static void exitGame() {
        running = false;
    }

    public static void goToPage(int nextPage) {
        switch (currentPage) {
            case PAGE_PARENT_CALL:
                clearDownloadedParentCallData();
                break;
            case PAGE_GOTCHI_KING:
                clearDownloadedGotchiKingData();
                break;
            case PAGE_TRAVEL_MEMORY:
                clearDownloadedTravelMemoryData();
                break;
            case PAGE_EXCHANGE_PLAZA:
                clearDownloadedExchangePlazaData();
        }

        switch (nextPage) {
            case PAGE_DOWNLOADING:
                loadingProgress = 0;
                break;
            case PAGE_LOADING:
                loadingProgress = 0;
                break;
            case PAGE_TITLE:
                // Resources loaded successfully
                selectSoftLabel(SOFT_LABEL_MENU);
                resetTitleScreenState();
                break;
            case PAGE_MAILBOX_MODE:
                selectSoftLabel(SOFT_LABEL_MENU);
                resetMailboxModeState();
                break;
            case PAGE_TRAVEL_MODE:
                selectSoftLabel(SOFT_LABEL_MENU);
                resetTravelModeSate();
                break;
            case PAGE_SHOPPING_CENTER:
                selectSoftLabel(SOFT_LABEL_MENU);
                resetShoppingCenterState();
                break;
            case PAGE_PARENT_CALL:
                selectSoftLabel(SOFT_LABEL_MENU);
                resetParentCallState();
                break;
            case PAGE_GOTCHI_KING:
                selectSoftLabel(SOFT_LABEL_MENU);
                resetGotchiKingState();
                break;
            case PAGE_TRAVEL_MEMORY:
                selectSoftLabel(SOFT_LABEL_MENU);
                resetTravelMemoryState();
                break;
            case PAGE_EXCHANGE_PLAZA:
                selectSoftLabel(SOFT_LABEL_MENU);
                resetExchangePlazaState();
                break;
            default:
                // Failed to load resources
                selectSoftLabel(SOFT_LABEL_EMPTY);
        }

        closeMenu();
        closeExplanation();
        closeErrorPage();
        currentPage = nextPage;
        drawOnNextPaint = false;
        fullDrawOnNextPaint = true;
    }

    public static void controlFlow() {
        checkMusic();
        if (isKeyPressed(KEY_ASTERISK)) {
            toggleSound();
            saveGame();
        }

        if (isMenuOpen()) {
            menuFlow();
        } else if (shouldShowErrorPage()) {
            errorPageFlow();
        } else {
            switch (currentPage) {
                case PAGE_AUTH_ERROR:
                case PAGE_COM_ERROR:
                case PAGE_PREP_ERROR:
                    exitGameOnSelect();
                    break;
                case PAGE_NONE_0:
                    if (--loadGameSaveDelay <= 0) {
                        gameSave[4] = 3;
                        loadGameSave();
                        goToPage(PAGE_NONE_1);
                    }
                    break;
                case PAGE_NONE_1:
                    goToPage(PAGE_DOWNLOADING);
                    break;
                case PAGE_DOWNLOADING:
                    checkGameData();
                    break;
                case PAGE_LOADING:
                    loadResources();
                    break;
                case PAGE_TITLE:
                    titleScreenFlow();
                    break;
                case PAGE_MAILBOX_MODE:
                    mailboxModeFlow();
                    break;
                case PAGE_TRAVEL_MODE:
                    travelModeFlow();
                    break;
                case PAGE_SHOPPING_CENTER:
                    shoppingCenterFlow();
                    break;
                case PAGE_PARENT_CALL:
                    parentCallFlow();
                    break;
                case PAGE_GOTCHI_KING:
                    gotchiKingFlow();
                    break;
                case PAGE_TRAVEL_MEMORY:
                    travelMemoryFlow();
                    break;
                case PAGE_EXCHANGE_PLAZA:
                    exchangePlazaFlow();
                    break;
                default:
                    if (isKeyPressed(KEY_SELECT)) {
                        running = false;
                    }
            }

        }
    }

    public static void draw(Graphics g) {
        try {
            if (isMenuOpen()) {
                drawMenuPages(g, rootX, rootY);
                return;
            }

            if (shouldShowErrorPage()) {
                errorPage(g, rootX, rootY);
                return;
            }

            switch (currentPage) {
                case PAGE_AUTH_ERROR:
                    showError(g, "Authenticating", rootX, rootY);
                    break;
                case PAGE_COM_ERROR:
                    showError(g, "Communicating", rootX, rootY);
                    break;
                case PAGE_PREP_ERROR:
                    showError(g, "Preparing", rootX, rootY);
                case PAGE_NONE_0:
                case PAGE_NONE_1:
                default:
                    break;
                case PAGE_DOWNLOADING:
                    downloadingPage(g, rootX, rootY);
                    break;
                case PAGE_LOADING:
                    loadingPage(g, rootX, rootY);
                    break;
                case PAGE_TITLE:
                    titleScreen(g, rootX, rootY);
                    break;
                case PAGE_MAILBOX_MODE:
                    mailboxModePage(g, rootX, rootY);
                    break;
                case PAGE_TRAVEL_MODE:
                    travelModePage(g, rootX, rootY);
                    break;
                case PAGE_SHOPPING_CENTER:
                    shoppingCenterPage(g, rootX, rootY);
                    break;
                case PAGE_PARENT_CALL:
                    parentCallPage(g, rootX, rootY);
                    break;
                case PAGE_GOTCHI_KING:
                    gotchiKingPage(g, rootX, rootY);
                    break;
                case PAGE_TRAVEL_MEMORY:
                    travelMemoryPage(g, rootX, rootY);
                    break;
                case PAGE_EXCHANGE_PLAZA:
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
            playMusic(loopedSoundId, true);
        }

    }

    public void resume() {
        try {
            timer.stop();
            startTimerWithRetry();
        } catch (Exception ignored) {
        }

        try {
            Thread.sleep(50L);
        } catch (InterruptedException ignored) {
        }

        restartMusicOnNext();
        resumedDraw = true;
        fullDrawOnNextPaint = true;
    }

    public void timerExpired(Timer timer) {
        try {
            GameApp.timer.stop();
            if (!this.executingTimerExpired) {
                this.executingTimerExpired = true;
                if (drawState == 0) {
                    updateInputState();
                    setSomeSystemAttribute();
                    controlFlow();
                    if ((garbageCollectTimer & 10) == 0) {
                        System.gc();
                    }

                    rand(2);
                    ++garbageCollectTimer;
                    drawState = 1;
                }

                if (drawState == 1) {
                    repaint();
                }

                if (!running) {
                    IApplication.getCurrentApp().terminate();
                }

                this.executingTimerExpired = false;
            }

            startTimerWithRetry();
        } catch (Exception ignored) {
        }

    }

    public void initCanvas() {
        canvas = new GameScreen(this);
        canvas.setBackground(Graphics.getColorOfName(0));
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        rootX = (canvasWidth - 240) / 2;
        rootY = (canvasHeight - 240) / 2;
        inputStateFlag = true;
        Display.setCurrent(canvas);
    }

    public void start() {
    }
}
