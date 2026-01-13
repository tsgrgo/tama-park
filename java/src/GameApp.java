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
    public static boolean aL;
    public static int Code;
    public static boolean e;
    public static boolean Z;
    public static int aK;
    public static boolean Exceptions;
    public static boolean I;
    public static boolean StackMap;
    public static int aJ;
    public static Timer b;
    public static GameApp mediaListener;
    public static Canvas f;
    public static int g;
    public static int h;
    public static int i;
    public static int j;
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
    public static AudioPresenter[] l;
    public static MediaSound[] o;
    public static int a;
    public static int aS;
    public static int aT;
    public static int p;
    public static boolean r;
    public static boolean q;
    public static long[] s;
    public static int t;
    public static int u;
    public static int[] v;
    public static int[] n;
    public static int currentFontIdx;
    public static int A;
    public static short[] aU;
    public static int[] aV;
    public static boolean[] aW;
    public static int[] aX;
    public static int B;
    public static int C;
    public static String[] D;
    public static int w;
    public static Image[] x;
    public static int[] F;
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
    public static Image[] V;
    public static String W;
    public static String X;
    public static int[] ab;
    public static int[] ae;
    public static Image[] ac;
    public static int[] aa;
    public static int[] af;
    public static int[] aj;
    public static String[] ah;
    public static Image ag;
    public static int[] al;
    public static int[] ao;
    public static Image[] am;
    public static String[] an;
    public static int[] ak;
    public static int[] ap;
    public static int[] aq;
    public static int[] ar;
    public static String as;
    public static long aB;
    public static long ad;
    public static boolean au;
    public static int at;
    public static String[] av;
    public static int[] aw;
    public static int[] ax;
    public static int[] ay;
    public static int[] az;
    public static int[] aZ;
    public static byte[] ba;
    public static byte[] bb;
    public static byte[] aA;
    public static String bc;
    public static String bd;
    public static byte[] aE;
    public static IrRemoteControlFrame[] aF;
    public static IrRemoteControl aD;
    public static int[] aC;
    public static long aG;
    public static int[] aI;
    public static int[] aH;
    public boolean c;

    static {
        new Object();
        e = true;
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
        l = new AudioPresenter[2];
        a = -1;
        aS = 0;
        aT = -1;
        p = -1;
        s = new long[7];
        t = 0;
        u = 0;
        v = new int[2];
        n = new int[7];
        A = 0;
        aU = new short[]{0, 4, 8, 13, 17, 22, 26, 31, 35, 40, 44, 48, 53, 57, 61, 66, 70, 74, 79, 83, 87, 91, 95, 100, 104, 108, 112, 116, 120, 124, 127, 131, 135, 139, 143, 146, 150, 154, 157, 161, 164, 167, 171, 174, 177, 181, 184, 187, 190, 193, 196, 198, 201, 204, 207, 209, 212, 214, 217, 219, 221, 223, 226, 228, 230, 232, 233, 235, 237, 238, 240, 242, 243, 244, 246, 247, 248, 249, 250, 251, 252, 252, 253, 254, 254, 255, 255, 255, 255, 255, 256};
        aV = new int[]{0, 17, 34, 52, 69, 87, 105, 122, 140, 158, 176, 194, 212, 230, 249, 267, 286, 305, 324, 344, 363, 383, 404, 424, 445, 466, 487, 509, 531, 554, 577, 600, 624, 649, 674, 700, 726, 753, 781, 809, 839, 869, 900, 932, 965, 999, 1035, 1072, 1110, 1150, 1191, 1234, 1279, 1327, 1376, 1428, 1482, 1539, 1600, 1664, 1732, 1804, 1880, 1962, 2050, 2144, 2246, 2355, 2475, 2605, 2747, 2904, 3077, 3270, 3487, 3732, 4010, 4331, 4704, 5144, 5671, 6313, 7115, 8144, 9514, 11430, 14300, 19081, 28636, 57289};
        aW = new boolean[]{false, false};
        aX = new int[2];
        String[] var10000 = new String[2];
        B = 6;
        C = 6;
        D = new String[]{"Start", "Menu", "Close", "Back", "Title", "Help", ""};
        x = new Image[93];
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
        V = new Image[2];
        ab = new int[6];
        ae = new int[]{120, 165, 170, 28, 93, 2, 120, 198, 170, 28, 15, 2};
        ac = new Image[2];
        aa = new int[]{58, 59, 60, 71, 89, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 72, 89};
        af = new int[4];
        aj = new int[]{120, 165, 170, 28, 15, 2, 120, 198, 170, 28, 35, 2};
        ah = new String[2];
        al = new int[5];
        ao = new int[]{120, 165, 170, 28, 93, 2, 120, 198, 170, 28, 15, 2};
        am = new Image[2];
        an = new String[3];
        ak = new int[]{0, 0, 1, 6, 6, 1, 80, 41, 0, 28, 2, 0, 0, 2, 81, 43, 0, 64, 3, 1, 1, 3, 82, 45, -44, 44, 4, 2, 2, 4, 83, 47, -64, 60, 5, 3, 3, 5, 84, 49, -108, 60, 6, 4, 4, 6, 85, 51, -164, 60, 0, 5, 5, 0, 86, 53};
        ap = new int[4];
        aq = new int[9];
        ar = new int[4];
        av = new String[183];
        aw = new int[14];
        ax = new int[6];
        ay = new int[]{3, 8, 4, 0, 1, 5, 6, 7, 2, 9, 3, 8, 4, 0, 1, 5, 6, 7, 2, 9, 4, 8, 1, 6, 2, 3, 9, 5, 0, 7, 4, 8, 1, 6, 2, 3, 9, 5, 0, 7, 5, 8, 0, 6, 2, 7, 3, 9, 1, 4, 5, 8, 0, 6, 2, 7, 3, 9, 1, 4, 5, 8, 7, 1, 6, 3, 0, 2, 9, 4, 5, 8, 7, 1, 6, 3, 0, 2, 9, 4, 6, 8, 5, 3, 0, 9, 7, 2, 4, 1, 6, 8, 5, 3, 0, 9, 7, 2, 4, 1};
        az = new int[]{15947864, 16777041, 10873427, 7053048, 16777215, 11025351, 16021161};
        aZ = new int[1];
        ba = new byte[1];
        bb = new byte[]{65, 80, 68, 65, 84, 65};
        aA = new byte[14];
        bc = "";
        bd = "";
        aE = new byte[16];
        aF = dW(1);
        aD = IrRemoteControl.getIrRemoteControl();
        aC = new int[6];
        aI = new int[]{120, 144, 170, 28, 15, 2, 120, 176, 170, 28, 94, 2, 120, 208, 170, 28, 95, 2};
        aH = new int[]{120, 144, 170, 28, 16, 2, 120, 176, 170, 28, 94, 2, 120, 208, 170, 28, 95, 2};
    }

    public GameApp() {
        mediaListener = this;
        String[] var1 = this.getArgs();
        this.e();

        try {
            PhoneSystem.setAttribute(0, 1);
        } catch (Exception var3) {
        }

        setCurrentFont(2);
        aK = 8;
        b = new Timer();
        b.setRepeat(true);
        b.setTime(1000 / aK);
        b.setListener(this);
        b.start();
    }

    public static void d() {
        Code = 2;
        f.repaint();
    }

    public static void I() {
        int var0 = 0;

        while(var0 < 10) {
            try {
                b.start();
                return;
            } catch (Exception var4) {
                try {
                    b.stop();
                } catch (Exception var3) {
                }

                try {
                    Thread.sleep(1000L);
                } catch (Exception var2) {
                }

                ++var0;
            }
        }

    }

    public static void f(String var0) {
        System.out.println(var0);
    }

    public static String h(int var0) {
        String var2 = "";
        if (var0 < 0) {
            var0 += 256;
        }

        for(int var1 = 0; var1 < 2; ++var1) {
            var2 = g(var0 % 16) + var2;
            var0 /= 16;
        }

        return var2;
    }

    public static String g(int var0) {
        String var1 = "";
        if (var0 < 10) {
            var1 = "" + var0;
        } else {
            switch(var0) {
                case 10:
                    var1 = "A";
                    break;
                case 11:
                    var1 = "B";
                    break;
                case 12:
                    var1 = "C";
                    break;
                case 13:
                    var1 = "D";
                    break;
                case 14:
                    var1 = "E";
                    break;
                case 15:
                    var1 = "F";
            }
        }

        return var1;
    }

    public static void i() {
        l[0] = AudioPresenter.getAudioPresenter(0);
        l[0].setAttribute(133, 0);
        l[0].setMediaListener(mediaListener);
        l[1] = AudioPresenter.getAudioPresenter(1);
        l[1].setAttribute(133, 1);
    }

    public static void k(int var0, int var1, boolean var2) {
        j(var1);

        try {
            if (n[3] == 0) {
                l[var1].setSound(o[var0]);
                if (var2) {
                }

                l[var1].play();
            }
        } catch (Exception var4) {
            f("playsound:" + var0 + " " + var4);
        }

    }

    public static void l(int var0, boolean var1) {
        k(var0, var1 ? 0 : 1, var1);
    }

    public static void j(int var0) {
        try {
            Thread.sleep(100L);
            l[var0].stop();
        } catch (Exception var2) {
        }

    }

    public static void m() {
        for(int var0 = 0; var0 < l.length; ++var0) {
            j(var0);
        }

    }

    public static void StackMap(int var0, boolean var1) {
        if (n[3] == 0 && var0 >= 0) {
            j(0);
            k(var0, 0, var1);
        }

        a = var0;
        p = var0;
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

    public static void p() {
        n[3] = 1 - n[3];
        m();
        n();
    }

    public static void n() {
        if (q) {
            StackMap(p, q);
        }

    }

    public static boolean loadSound() {
        DataInputStream var0 = null;
        byte var2 = 0;

        boolean var1;
        try {
            o = new MediaSound[7];
            int[] var3 = q(128);
            int var4 = 0;

            int var18;
            for(var18 = 0; var18 < 93; ++var18) {
                var4 += var3[var18];
            }

            var0 = Connector.openDataInputStream("scratchpad:///0;pos=" + (var4 + 128 + 568));

            for(var18 = 0; var18 < 7; ++var18) {
                byte[] var5 = new byte[var3[var18 + 93]];
                var0.read(var5);

                for(int var6 = 0; var6 < var5.length; ++var6) {
                }

                o[var18] = MediaManager.getSound(var5);
                o[var18].use();
                f("loadsound:" + var18);
                Object var19 = null;
                System.gc();
            }

            i();
            var1 = true;
        } catch (Exception var16) {
            f("loadsounderr i:" + var2);
            var1 = false;
        } finally {
            if (var0 != null) {
                try {
                    var0.close();
                } catch (Exception var15) {
                }
            }

        }

        return var1;
    }

    public static boolean s(long var0) {
        return (s[1] & var0) != 0L;
    }

    public static boolean t(long var0) {
        return (s[2] & var0) != 0L;
    }

    public static void Code(int var0, int var1) {
        try {
            if (0 == var0) {
                s[0] = (long)(f.getKeypadState() & Integer.MAX_VALUE);
                s[4]++;
            }
        } catch (Exception var3) {
        }

    }

    public static void J() {
        long var0 = 0L;
        long var2 = 0L;
        long var4 = 0L;
        var2 = 0L;
        if (s[4] == 0L) {
            s[0] = 0L;
        }

        s[3] = s[6];
        s[6] = s[0] | var0 | var2 << 32;
        s[5] = s[6] & (s[6] ^ s[3]);
        long[] var10000;
        if (k) {
            if (s[4] != 0L) {
                var10000 = s;
                var10000[5] |= var2 << 32 & 844424930131968L;
            }
        } else if (s[4] != 0L) {
            var10000 = s;
            var10000[5] |= var0 & 655360L;
        }

        if ((s[3] ^ s[6]) == 0L && s[6] != 0L) {
            ++t;
        } else {
            t = 0;
        }

        s[4] = 0L;
        if ((s[6] & 9851624207876096L) == 0L) {
            ++u;
        } else {
            u = 0;
        }

        s[2] = s[6];
        s[1] = s[5];
    }

    public static void u() {
        try {
            v[0] = 0;
            v[1] = 0;
            PhoneSystem.setAttribute(1, 0);
        } catch (Exception var1) {
        }

    }

    public static void a() {
        if (v[0] > 0) {
            if (n[5] == 0 && v[1] == 0) {
                try {
                    v[1] = 1;
                    PhoneSystem.setAttribute(1, 1);
                } catch (Exception var1) {
                }
            }

            if (--v[0] <= 0) {
                u();
            }
        }

    }

    public static void v() {
        try {
            DataInputStream var0 = Connector.openDataInputStream("scratchpad:///0;pos=0");

            for(int var1 = 0; var1 < n.length; ++var1) {
                n[var1] = var0.readInt();
            }

            var0.close();
            var0 = null;
            System.gc();
        } catch (Exception var2) {
        }

    }

    public static void w() {
        try {
            DataOutputStream var0 = Connector.openDataOutputStream("scratchpad:///0;pos=0");

            for(int var1 = 0; var1 < n.length; ++var1) {
                var0.writeInt(n[var1]);
            }

            var0.close();
            var0 = null;
            System.gc();
        } catch (Exception var2) {
        }

    }

    public static void x(String var0, int var1, int var2) throws Exception {
        byte[] var6 = new byte[10240];
        d();
        var2 += n[1] * 10240;

        for(int var7 = n[1]; var7 < (var1 - 1) / 10240 + 1; ++var7) {
            HttpConnection var3 = (HttpConnection)Connector.open(mediaListener.getSourceURL() + var0 + var7 + ".bin", 1, true);
            var3.setRequestMethod("GET");
            var3.connect();
            System.gc();
            DataInputStream var5 = new DataInputStream(var3.openInputStream());
            int var9 = (int)var3.getLength();
            int var8 = var5.read(var6, 0, var9);
            var5.close();
            var3.close();
            if (var8 != var9) {
                throw new Exception("http load error!");
            }

            DataOutputStream var4 = Connector.openDataOutputStream("scratchpad:///0;pos=" + var2);
            var4.write(var6, 0, var9);
            var4.close();
            var2 += var9;
            int var10002 = n[1]++;
            w();
            ++w;
            d();
        }

        Object var10 = null;
        System.gc();
    }

    public static int[] q(int var0) throws Exception {
        return y(var0, 2);
    }

    public static int[] y(int var0, int var1) throws Exception {
        DataInputStream var2 = new DataInputStream(Connector.openInputStream("scratchpad:///0;pos=" + var0));
        short var3 = var2.readShort();
        int[] var4 = new int[var3];
        int var5;
        if (var1 == 2) {
            for(var5 = 0; var5 < var3; ++var5) {
                var4[var5] = var2.readShort();
            }
        } else if (var1 == 4) {
            for(var5 = 0; var5 < var3; ++var5) {
                var4[var5] = var2.readInt();
            }
        }

        var2.close();
        return var4;
    }

    public static void A(Graphics var0, int var1, int var2, int var3, int var4) {
        z(var0, x[var1], var2, var3, var4);
    }

    public static void z(Graphics var0, Image var1, int var2, int var3, int var4) {
        if (var4 == 2) {
            var2 -= var1.getWidth() / 2;
        } else if (var4 == 1) {
            var2 -= var1.getWidth();
        } else if (var4 == 3) {
            var2 -= var1.getWidth() / 2;
            var3 -= var1.getHeight() / 2;
        }

        var0.drawImage(var1, var2, var3);
    }

    public static int B(int var0) {
        return x[var0].getWidth();
    }

    public static int C(int var0) {
        return x[var0].getHeight();
    }

    public static void setColorOfRGB(Graphics graphics, int r, int g, int b) {
        graphics.setColor(Graphics.getColorOfRGB(r, g, b));
    }

    public static void setColorOfRGBInt(Graphics g, int rgb) {
        g.setColor(Graphics.getColorOfRGB(rgb >> 16 & 255, rgb >> 8 & 255, rgb & 255));
    }

    public static void drawString(Graphics g, String str, int x, int y, int var4) {
        drawStringWithLineHeight(g, str, x, y, currentFont.getHeight() + 1, var4);
    }

    public static void drawStringWithLineHeight(Graphics g, String str, int x, int y, int lineHeight, int var5) {
        boolean var6 = false;
        int fromIndex = 0;
        boolean hasNewLine = true;

        for(y += currentFont.getHeight(); hasNewLine; y += lineHeight) {
            int toIndex = str.indexOf("\n", fromIndex);
            if (toIndex == -1) {
                toIndex = str.length();
                hasNewLine = false;
            }

            int newX = x;
            if (var5 == 2) {
                newX = x - currentFont.stringWidth(str.substring(fromIndex, toIndex)) / 2;
            } else if (var5 == 1) {
                newX = x - currentFont.stringWidth(str.substring(fromIndex, toIndex));
            }

            g.drawString(str.substring(fromIndex, toIndex), newX, y - currentFont.getDescent());
            fromIndex = toIndex + 1;
        }

    }

    public static void H(Graphics g, int var1, int var2, int var3, int var4, int var5, int var6) {
        if (var3 >= 2 && var4 >= 2) {
            setColorOfRGBInt(g, var5);
            g.fillRect(var1 + 1, var2, var3 - 2, 2);
            g.fillRect(var1 + 1, var2 + var4 - 2, var3 - 2, 2);
            g.fillRect(var1, var2 + 1, 2, var4 - 2);
            g.fillRect(var1 + var3 - 2, var2 + 1, 2, var4 - 2);
            if (var3 >= 4 && var4 >= 4) {
                setColorOfRGBInt(g, var6);
                g.fillRect(var1 + 1, var2 + 2, 1, var4 - 4);
                g.fillRect(var1 + var3 - 2, var2 + 2, 1, var4 - 4);
                g.fillRect(var1 + 2, var2 + 1, var3 - 4, var4 - 2);
            }
        }
    }

    public static void K(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        int[] var9 = new int[]{var5, var6, var5, var7};

        for(int var8 = 0; var8 < 4; ++var8) {
            var0.setColor(var9[var8]);
            var0.fillRect(var1 + var8, var2 + var8, var3 - var8 * 2, var4 - var8 * 2);
        }

        Object var10 = null;
    }

    public static void setCurrentFont(int i) {
        switch(i) {
            case 0:
                currentFont = Font.getFont(1895826432);
                break;
            case 1:
                currentFont = Font.getFont(1895825664);
                break;
            case 2:
                currentFont = Font.getFont(1895825920);
                break;
            case 3:
                currentFont = Font.getFont(1896940032);
        }

        currentFontHeight = currentFont.getHeight();
        currentFontIdx = i;
    }

    public static int c(int var0) {
        A = A * 1103515245 + 12345;
        A &= 32767;
        return A * var0 / '耀';
    }

    public static int M(int var0) {
        if (var0 < 0) {
            var0 *= -1;
        }

        return var0;
    }

    public static int N(String var0, String var1) {
        boolean var3 = false;
        int var4 = 0;
        boolean var5 = true;

        int var2;
        int var6;
        for(var2 = 0; var5; var4 = var6 + var1.length()) {
            var6 = var0.indexOf(var1, var4);
            if (var6 == -1) {
                var6 = var0.length();
                var5 = false;
            }

            ++var2;
        }

        return var2;
    }

    public static String O(String var0, int var1, int var2, String var3) {
        int var5 = 0;

        int var4;
        for(var4 = 0; var4 < var1; ++var4) {
            var5 = var0.indexOf(var3, var5);
            if (var5 == -1) {
                f("subStringLine:Invalid line selection");
                return "";
            }

            var5 += var3.length();
        }

        int var6 = var5;

        for(var4 = 0; var4 < var2; ++var4) {
            var5 = var0.indexOf(var3, var5);
            if (var5 == -1) {
                return var0.substring(var6);
            }

            var5 += var3.length();
        }

        if (0 < var2) {
            var5 -= var3.length();
        }

        return var0.substring(var5, var5);
    }

    public static boolean P(String var0) {
        boolean var1 = true;

        try {
            String[] var2 = new String[]{var0};
            IApplication.getCurrentApp().launch(1, var2);
        } catch (Exception var3) {
            var1 = false;
        }

        return var1;
    }

    public static void Q(int var0, String var1) {
        if (var0 == 0) {
            f.setSoftLabel(0, var1);
        } else if (var0 == 1) {
            f.setSoftLabel(1, var1);
        }

    }

    public static void R(int var0) {
        if (B != var0) {
            try {
                C = B;
                Q(0, D[var0]);
                B = var0;
            } catch (Exception var2) {
            }

        }
    }

    public static boolean S() {
        try {
            if (E != n[2]) {
                n[1] = 0;
                n[2] = E;
                w();
            }

            if (n[1] != 255) {
                w = n[1];
                x("", 72483, 128);
                n[1] = 255;
                w();
            }

            return true;
        } catch (Exception var1) {
            return false;
        }
    }

    public static void U() {
        if (S()) {
            T(3);
        } else {
            T(-2);
        }

    }

    public static void X() {
        int var0 = V(128, 0, 93);
        if (var0 == -1) {
            T(-1);
        } else if (loadSound() && W()) {
            T(4);
        } else {
            T(-1);
        }
    }

    public static int V(int var0, int var1, int var2) {
        try {
            Thread.sleep(200L);
        } catch (Exception var10) {
        }

        try {
            int[] var4 = q(128);
            F = var4;
            var0 += (var4.length + 1) * 2;

            int var5;
            for(var5 = 0; var5 < var1; ++var5) {
                var0 += var4[var5];
            }

            DataInputStream var6 = new DataInputStream(Connector.openInputStream("scratchpad:///0;pos=" + var0));

            for(var5 = var1; var5 < var2; ++var5) {
                byte[] var3 = new byte[var4[var5]];
                var6.read(var3);
                MediaImage var7 = MediaManager.getImage(var3);
                var7.use();
                x[var5] = var7.getImage();
                Object var12 = null;
                ++w;
                var0 += var4[var5];
                d();

                try {
                    Thread.sleep(50L);
                } catch (Exception var9) {
                }
            }

            Object var13 = null;
            var6.close();
            System.gc();
            return var0;
        } catch (Exception var11) {
            return -1;
        }
    }

    public static void Y(int var0) throws Exception {
        if (x[var0] == null) {
            short var1 = 128;
            int var4 = var1 + (F.length + 1) * 2;

            for(int var2 = 0; var2 < var0; ++var2) {
                var4 += F[var2];
            }

            MediaImage var3 = MediaManager.getImage("scratchpad:///0;pos=" + var4);
            var3.use();
            x[var0] = var3.getImage();
        }

    }

    public static void aa(int var0) {
        if (x[var0] != null) {
            x[var0].dispose();
            x[var0] = null;
        }

    }

    public static void ab(Graphics var0, int var1, int var2) {
        int var3 = (g - 200) / 2;
        int var4 = var2 + 168;
        setColorOfRGBInt(var0, 16777215);
        var0.fillRect(var1, var2, 240, 240);
        setColorOfRGBInt(var0, 16763955);
        var0.drawRect(var3, var4, 200, 40);
        drawString(var0, "Downloading", g / 2, var4 - currentFontHeight - 4, 2);
        int var5 = 200 * w / 8;
        var0.fillRect(var3, var4, var5, 40);
    }

    public static void ad(Graphics var0, int var1, int var2) {
        if (3 < w) {
            try {
                Thread.sleep(300L);
            } catch (Exception var4) {
            }

            ac(var0, var1, var2, w * 8 / 93, w);
        }

    }

    public static void ae() {
        if (s(1048576L)) {
            e = false;
        }

    }

    public static void af(Graphics g, String str, int x, int y) {
        setColorOfRGBInt(g, 0);
        g.fillRect(x, y, 240, 240);
        setColorOfRGBInt(g, 16777215);
        drawString(g, str, GameApp.g / 2, y + 30, 2);
        drawString(g, "An error has occured", GameApp.g / 2, y + 31 + currentFontHeight, 2);
        drawString(g, "Confirm:Exit", GameApp.g / 2, y + 240 - 10 - currentFontHeight, 2);
    }

    public static void ak() {
        ag(2, true);
        ah(0);
        ai(15947864, 15947864, 16353930, 16777215, 12138328, 16777215, 16777215, 16353930, 16777215, 13816530);
        StackMap(1, true);
        G[2] = 0;
        G[3] = 0;
        G[4] = 0;
        aj(0);
    }

    public static void aj(int var0) {
        switch(var0) {
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
        switch(G[1]) {
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
        if (s(1048576L) || 0 <= var0) {
            aj(2);
        }

    }

    public static void am() {
        if (s(2097152L)) {
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
            switch(aq(s(1048576L), s(131072L), s(524288L))) {
                case 0:
                    T(5);
                    return;
                case 1:
                    T(6);
                    return;
                default:
                    G[0] = ar();
            }
        }
    }

    public static void au(Graphics var0, int var1, int var2) {
        switch(G[1]) {
            case 1:
                as(var0, var1, var2);
                break;
            case 2:
                at(var0, var1, var2);
        }

    }

    public static void as(Graphics var0, int var1, int var2) {
        int var3 = (g - B(72)) / 2;
        int var4 = var2 + 54;
        int var5 = -48 + G[2] * 8;
        setColorOfRGBInt(var0, 6728679);
        var0.fillRect(var1, var2, 240, 240);
        A(var0, 23, var1, var2 + var5, 0);
        A(var0, 6, var1, var2 + 116, 0);
        A(var0, 6, var1 + 120, var2 + 116, 0);
        setColorOfRGBInt(var0, 7456538);
        var0.fillRect(var1, var2 + 158, 240, 82);
        A(var0, 72, var3, var4, 0);
        A(var0, 73 + (G[2] >> 4 & 1), var3 + 23, var4 + 66, 0);
    }

    public static void at(Graphics var0, int var1, int var2) {
        int var3 = (g - B(72)) / 2;
        int var4 = var2 + 54;
        int var5;
        if (I) {
            setColorOfRGBInt(var0, 6728679);
            var0.fillRect(var1, var2, 240, 240);
            A(var0, 23, var1, var2, 0);
            av(var0, 6, G[3], var1, var2 + 116, 240);
            setColorOfRGBInt(var0, 7456538);
            var0.fillRect(var1, var2 + 158, 240, 82);
            A(var0, 9, g / 2, var2 + 158, 2);
            A(var0, 72, var3, var4, 0);
            A(var0, 73 + (G[2] >> 4 & 1), var3 + 23, var4 + 66, 0);

            for(var5 = 0; var5 < 2; ++var5) {
                aw(var0, var5, H, 0);
            }
        } else {
            setColorOfRGBInt(var0, 7456538);
            var0.fillRect(var1, var2 + 158 + C(9), 240, 240 - (158 + C(9)));
            setColorOfRGBInt(var0, 6728679);
            var0.fillRect(var1, var2 + 116, 240, C(6));
            av(var0, 6, G[3], var1, var2 + 116, 240);
            A(var0, 72, var3, var4, 0);
            A(var0, 73 + (G[2] >> 4 & 1), var3 + 23, var4 + 66, 0);

            for(var5 = 0; var5 < 2; ++var5) {
                aw(var0, var5, H, 0);
            }
        }

        var5 = ax(H, ar(), 1);
        var5 += ax(H, ar(), 3) / 2;
        var5 -= 10;
        A(var0, 75, var1 + 2, var2 + var5, 0);
        A(var0, 75, var1 + 240 - (B(75) - 10), var2 + var5, 0);
    }

    public static void av(Graphics var0, int var1, int var2, int var3, int var4, int var5) {
        while(0 < var2) {
            var2 -= B(var1);
        }

        while(var2 < var5) {
            A(var0, var1, var3 + var2, var4, 0);
            var2 += B(var1);
        }

    }

    public static void az() {
        ag(3, true);
        ah(0);
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
        switch(K[1]) {
            case 0:
                ay(1);
                break;
            case 1:
                aA();
        }

    }

    public static void aA() {
        if (s(2097152L)) {
            ao();
            ap(105, 3);
        } else {
            switch(aq(s(1048576L), s(131072L), s(524288L))) {
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
                    K[0] = ar();
            }
        }
    }

    public static void aF(Graphics var0, int var1, int var2) {
        int var3 = (currentFontHeight + 1) * 2 + currentFontHeight;
        short var4 = 184;
        int var5;
        if (I) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240);
            setColorOfRGBInt(var0, 6728679);
            var0.fillRect(var1, var2 + 78, 240, C(6));
            A(var0, 6, var1, var2 + 78, 0);
            A(var0, 6, var1 + 120, var2 + 78, 0);
            H(var0, var1 + 3, var2 + 3, var4, var3 + 4, 0, 16056665);
            setColorOfRGBInt(var0, 16777215);
            drawString(var0, aC(29), var1 + 3 + 2, var2 + 3 + 2, 0);

            for(var5 = 0; var5 < 3; ++var5) {
                aw(var0, var5, L, 0);
            }
        } else {
            for(var5 = 0; var5 < 3; ++var5) {
                aw(var0, var5, L, 0);
            }
        }

        var5 = ax(L, ar(), 2) / 2;
        var5 -= 10;
        int var6 = ax(L, ar(), 1) + var2;
        var6 += ax(L, ar(), 3) / 2;
        aD(var0, 64, g / 2, var6, (var5 - B(64)) * 2, K[2]);
        if (I) {
            A(var0, 90, var1 + 3 + var4 - 1, var2 + 3 + var3 / 2 - 5, 0);
            A(var0, 89, var1 + 240 + 2 - B(89), var2 + 54, 0);
            aE(var0);
        }

    }

    public static void aH() {
        ag(2, true);
        ah(0);
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
        switch(M[1]) {
            case 0:
                aG(1);
                break;
            case 1:
                aI();
        }

    }

    public static void aI() {
        if (s(2097152L)) {
            ao();
            ap(108, 2);
        } else {
            switch(aq(s(1048576L), s(131072L), s(524288L))) {
                case 0:
                    T(10);
                    return;
                case 1:
                    T(11);
                    return;
                default:
                    M[0] = ar();
            }
        }
    }

    public static void aK(Graphics var0, int var1, int var2) {
        int var3 = (currentFontHeight + 1) * 2 + currentFontHeight;
        short var4 = 192;
        int var5;
        if (I) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240);
            setColorOfRGBInt(var0, 6728679);
            var0.fillRect(var1, var2 + 78, 240, C(6));
            A(var0, 6, var1, var2 + 78, 0);
            A(var0, 6, var1 + 120, var2 + 78, 0);
            H(var0, var1 + 3, var2 + 3, var4, var3 + 8, 0, 16056665);
            setColorOfRGBInt(var0, 16777215);
            drawString(var0, aC(40), var1 + 3 + 6, var2 + 3 + 4, 0);

            for(var5 = 0; var5 < 2; ++var5) {
                aw(var0, var5, N, 0);
            }
        } else {
            for(var5 = 0; var5 < 2; ++var5) {
                aw(var0, var5, N, 0);
            }
        }

        var5 = ax(N, ar(), 2) / 2;
        var5 -= 10;
        int var6 = ax(N, ar(), 1) + var2;
        var6 += ax(N, ar(), 3) / 2;
        aD(var0, 64, g / 2, var6, (var5 - B(64)) * 2, M[2]);
        if (I) {
            A(var0, 90, var1 + 3 + var4 - 1, var2 + 3 + var3 / 2 - 5, 0);
            A(var0, 57, var1 + 240 - B(57) - 2, var2 + 68, 0);
        }

    }

    public static void aM() {
        aL(0);
    }

    public static void aL(int var0) {
        switch(var0) {
            case 0:
                R(6);
                break;
            case 1:
                ag(5, true);
                ah(4);
                aN(16750848, 16750848, 16763955, 16777215, 16750848, 16777164, 16750848, 16750848);
                R(1);
                break;
            case 2:
                R(6);
                break;
            case 3:
                R(1);
                ag(3, true);
                ah(0);
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
        switch(P[1]) {
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
        if (s(2097152L)) {
            ao();
            ap(110, 5);
        } else {
            int var0 = ar();
            int var1 = aq(s(1048576L), s(524288L), s(131072L));
            P[0] = ar();
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
        DataInputStream var0 = null;

        try {
            var0 = aV(4);
            aW(var0);
            int var1 = var0.read();
            if (0 < var1) {
                String var2 = aX(var0, var1);
                aY(O, 2, 1, var2);
            } else {
                byte[] var14 = new byte[10];
                var0.read(var14);
                aZ(var14);
                aL(3);
            }
        } catch (Exception var12) {
            f("e:" + var12);
            aY(O, 2, 1, aC(92));
        } finally {
            if (var0 != null) {
                try {
                    var0.close();
                } catch (Exception var11) {
                }
            }

            l(6, false);
        }

    }

    public static void aU() {
        ba();
        bb(1, 0);
        bb(2, 3);
        bb(3, P[0] + 1);
    }

    public static void aR() {
        int var10002 = P[3]++;
        if (s(2097152L)) {
            ao();
            ap(115, 6);
        } else {
            if (6 < P[2]) {
                switch(aq(s(1048576L), s(131072L), s(524288L))) {
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

    public static void bi(Graphics var0, int var1, int var2) {
        switch(P[1]) {
            case 0:
            default:
                break;
            case 1:
                be(var0, var1, var2);
                break;
            case 2:
                bf(var0, var1, var2);
                break;
            case 3:
                bg(var0, var1, var2);
                break;
            case 4:
                bh(var0, var1, var2);
        }

    }

    public static void be(Graphics var0, int var1, int var2) {
        if (I) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240);
            bj(var0, aC(2), g / 2, var2 + 2, currentFont.stringWidth(aC(2)) + 8, 2);
        }

        int var4;
        for(int var3 = 0; var3 < 5; ++var3) {
            var4 = var3 * 4;
            ai(Q[var4 + 0], Q[var4 + 0], Q[var4 + 0], Q[var4 + 1], Q[var4 + 0], Q[var4 + 2], Q[var4 + 2], Q[var4 + 2], Q[var4 + 3], Q[var4 + 2]);
            bk(var0, var3, R, 0);
        }

        bl(var0, var1, aC(26), var2 + 2 + 34 + 1, P[2], 12, 16056665, 16777215);

        for(var4 = 0; var4 < 5; ++var4) {
            int var5 = bm(R, var4, 1) + 3;
            int var6 = bm(R, var4, 2) - 66;
            byte var7;
            boolean var8;
            if (var4 == ar()) {
                var7 = 61;
                var8 = false;
                var6 -= M((P[3] & 31) - 16);
            } else {
                var7 = 62;
                var8 = false;
            }

            int var10 = var7 + (P[3] >> 3 & 1);
            int var9 = var2 + bm(R, var4, 1);
            var9 += bm(R, var4, 3) / 2 + 2;
            bn(var0, var10, var1 + bm(R, var4, 0), var9, var6, P[2], 0);
        }

    }

    public static void bf(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        var0.fillRect(var1, var2, 240, 240);
        A(var0, 0, var1, var2 + 240 - C(0), 0);
        setColorOfRGBInt(var0, 6728679);
        var0.fillRect(var1, var2 + 110, 240, C(6));
        A(var0, 6, var1, var2 + 110, 0);
        A(var0, 6, var1 + 120, var2 + 110, 0);
        bj(var0, aC(76), g / 2, var2 + 2, 200, 2);
        A(var0, 76, g / 2, var2 + 100, 2);
    }

    public static void bg(Graphics var0, int var1, int var2) {
        if (I) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240);
            bj(var0, aC(79), g / 2, var2 + 3, currentFont.stringWidth(aC(79)) + 8, 2);
        } else {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2 + bm(S, 0, 1), 240, 240 - (bm(S, 0, 1) + C(0)));
        }

        A(var0, 0, var1, var2 + 240 - C(0), 0);
        bl(var0, var1, aC(64), var2 + 3 + currentFontHeight + 12, P[2], 12, 16056665, 16777215);
        int var3 = var2 + 3 + currentFontHeight + 12 + currentFontHeight + 4;
        if (I) {
            bo(var0, g / 2, var3, 2, 0, 16770972, 16750748, 16770972);
            bp(var0, g / 2, var3 + 10, 56, P[2] >> 1, false);
        } else {
            bq(var0, g / 2, var3 + 10, 56, P[2] >> 1, false, 16770972);
        }

        br(var0, g / 2, var3 + 4, 62, false, I);

        for(int var4 = 0; var4 < 3; ++var4) {
            bk(var0, var4, S, 0);
        }

        aD(var0, 61, var1 + bm(S, ar(), 0), var2 + bm(S, ar(), 1) + bm(S, ar(), 3) / 2, bm(S, ar(), 2) - 10, P[2]);
    }

    public static void bh(Graphics var0, int var1, int var2) {
        bs(var0, var1, var2);
    }

    public static void bl(Graphics var0, int var1, String var2, int var3, int var4, int var5, int var6, int var7) {
        bt(var0, var2, var1, var3, 240, var4, var5, var6, var7);
    }

    public static void bt(Graphics var0, String var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        setColorOfRGBInt(var0, var8);
        var0.fillRect(var2, var3, var4, currentFontHeight);
        setColorOfRGBInt(var0, var7);
        int var9 = currentFont.stringWidth(var1);
        drawString(var0, var1, var2 + var4 - var5 * var6 % (var4 + var9), var3, 0);
    }

    public static void bp(Graphics var0, int var1, int var2, int var3, int var4, boolean var5) {
        A(var0, 32, var1 - (var3 + 6), var2 + 18, 0);
        A(var0, (!var5 ? 77 : 78) + var4 % 3 * 2, var1 - (var3 + 5), var2, 0);
        A(var0, 33, var1 + (var3 - 3), var2 + 20, 0);
        A(var0, (var5 ? 83 : 84) + var4 % 3 * 2, var1 + (var3 - 3), var2, 0);
    }

    public static void bq(Graphics var0, int var1, int var2, int var3, int var4, boolean var5, int var6) {
        setColorOfRGBInt(var0, var6);
        var0.fillRect(var1 - (var3 + 6), var2, B(32), 18 + C(32));
        var0.fillRect(var1 + (var3 - 3), var2, B(33), 20 + C(33));
        bp(var0, var1, var2, var3, var4, var5);
    }

    public static void bj(Graphics var0, String var1, int var2, int var3, int var4, int var5) {
        int var6 = bu(var1);
        byte var7 = 0;
        int var8 = 16056665;
        int var9 = 16777215;
        if (var5 == 2) {
            var2 -= var4 / 2;
        } else if (var5 == 1) {
            var2 -= var4;
        }

        H(var0, var2, var3, var4, var6, var7, var8);
        setColorOfRGBInt(var0, var9);
        drawString(var0, var1, var2 + var4 / 2, var3 + 2, 2);
    }

    public static int bu(String var0) {
        return (currentFontHeight + 1) * (N(var0, "\n") - 1) + currentFontHeight + 4;
    }

    public static void bo(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        if (var3 == 2) {
            var1 -= 88;
        } else if (var3 == 1) {
            var1 -= 176;
        }

        H(var0, var1, var2, 176, 70, var4, var5);
        H(var0, var1 + 3, var2 + 3, 170, 64, var6, var7);
    }

    public static void bv(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        byte var8 = 32;
        int var9 = var2 + 24;
        setColorOfRGBInt(var0, var7);
        var0.fillRect(var1, var9, var8, 2);
        var0.fillRect(var1, var9 + 2 + 1, var8, 16);
        var0.fillRect(var1, var9 + 2 + 1 + 16 + 1, var8, 2);
        var0.fillRect(var1 + var8 + 176, var9, var8, 2);
        var0.fillRect(var1 + var8 + 176, var9 + 2 + 1, var8, 16);
        var0.fillRect(var1 + var8 + 176, var9 + 2 + 1 + 16 + 1, var8, 2);
        bo(var0, g / 2, var2, 2, var3, var4, var5, var6);
    }

    public static void aD(Graphics var0, int var1, int var2, int var3, int var4, int var5) {
        bn(var0, var1, var2, var3, var4, var5, 1);
    }

    public static void bn(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6) {
        A(var0, var1 + (var6 & -(var5 >> 2 & 1)), var2 - var4 / 2 - B(var1) - 2 - 2, var3 - C(var1) / 2, 0);
        A(var0, var1 + (var6 & -((var5 >> 2) + 1 & 1)), var2 + var4 / 2 + 2 + 2, var3 - C(var1) / 2, 0);
    }

    public static void br(Graphics var0, int var1, int var2, int var3, boolean var4, boolean var5) {
        int var6 = var1 - 35;
        int var7 = var2 + (var3 - 49) / 2;
        int var8 = bw();
        int var9 = bx();
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
        bA();
        bB(0);
        bC(0);
    }

    public static void bC(int var0) {
        T = true;
        switch(var0) {
            case 0:
                R(6);
                break;
            case 1:
                R(1);
                T = false;
                ag(2, true);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                U[0] = 0;
                break;
            case 2:
                R(6);
                break;
            case 3:
                R(6);
                ag(1, false);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 4:
                R(1);
                ag(2, true);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 5:
                aO(O, 177, 2, 55);
        }

        StackMap = true;
        U[6] = 0;
        U[1] = var0;
    }

    public static void bE() {
        for(int var0 = 0; var0 < 2; ++var0) {
            if (V[var0] != null) {
                V[var0].dispose();
                V[var0] = null;
            }
        }

        W = null;
        X = null;
        System.gc();
    }

    public static void bK() {
        int var10002 = U[6]++;
        switch(U[1]) {
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
        if (s(2097152L)) {
            ao();
            ap(121, 6);
        } else {
            if (U[0] != 0) {
                if (aq(s(1048576L), false, false) != -1) {
                    bC(2);
                } else if (!s(1048576L)) {
                    if (s(196608L)) {
                        bB(9);
                        U[0] = 0;
                    } else if (s(786432L)) {
                        bB(0);
                        U[0] = 0;
                    }
                }
            } else if (bM(s(65536L), s(262144L), s(131072L), s(524288L), s(1048576L), bL())) {
                U[0] = 1;
            }

            ah(U[0]);
        }
    }

    public static void bG() {
        bE();
        bN();
        DataInputStream var0 = null;

        try {
            var0 = aV(13);
            aW(var0);
            int var1 = var0.read();
            if (0 < var1) {
                String var2 = aX(var0, var1);
                aY(O, 2, 1, var2);
            } else {
                byte[] var16 = new byte[10];
                var0.read(var16);

                int var3;
                for(int var4 = 0; var4 < 2; ++var4) {
                    var3 = var0.readUnsignedShort();
                    V[var4] = bO(var0, var3);
                }

                var3 = var0.readUnsignedShort();
                W = aX(var0, var3);
                U[2] = 0;
                U[4] = 0;
                U[5] = bP(W);
                X = bQ(W, U[4]);
                aZ(var16);
                bC(3);
            }
        } catch (Exception var14) {
            f("e:" + var14);
            aY(O, 2, 1, aC(92));
        } finally {
            if (var0 != null) {
                try {
                    var0.close();
                } catch (Exception var13) {
                }
            }

            l(6, false);
        }

    }

    public static void bN() {
        ba();
        bb(1, 1);
        bb(2, 3);

        for(int var0 = 0; var0 < 10; ++var0) {
            bb(3 + var0, bR(var0));
        }

    }

    public static void bH() {
        int var10002 = U[3]++;
        if (6 < U[6]) {
            if (-1 != aq(s(1048576L), s(65536L), s(262144L))) {
                var10002 = U[4]++;
                U[3] = 0;
                if (U[5] <= U[4]) {
                    bS();
                    if (3 < bT(2)) {
                        T(4);
                    } else {
                        bC(4);
                    }
                } else {
                    X = bQ(W, U[4]);
                }
            }
        } else {
            int var0 = currentFont.stringWidth(X);
            if (var0 + 232 <= U[3] * 12) {
                U[3] = 0;
            }
        }

    }

    public static void bI() {
        if (s(2097152L)) {
            ao();
            ap(127, 6);
        } else {
            if (6 < U[6]) {
                switch(aq(s(1048576L), s(131072L), s(524288L))) {
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

    public static void bZ(Graphics var0, int var1, int var2) {
        switch(U[1]) {
            case 0:
            default:
                break;
            case 1:
                bU(var0, var1, var2);
                break;
            case 2:
                bV(var0, var1, var2);
                break;
            case 3:
                bW(var0, var1, var2);
                break;
            case 4:
                bX(var0, var1, var2);
                break;
            case 5:
                bY(var0, var1, var2);
        }

    }

    public static void bU(Graphics var0, int var1, int var2) {
        int var3 = bu(aC(17));
        int var4 = var2 + 240 - 42;
        if (I) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240 - C(0));
            bj(var0, aC(17), g / 2, var2 + 3, 232, 2);
        }

        A(var0, 0, var1, var2 + 240 - C(0), 0);
        bl(var0, var1, aC(65), var2 + 3 + var3 + 3, U[6], 12, 16056665, 16777215);
        int var5 = var2 + 3 + var3 + 3 + currentFontHeight + 4 + 20;
        boolean var6 = 0 == ar() & (U[6] & 4) != 0;
        ca(var0, g / 2, var5, var6);
        cb(var0, 1, aC(18), g / 2, var4, 100, 28, 2, 0);
        if (I) {
            bp(var0, g / 2, var5 + 5, 56, U[6] >> 1, true);
        } else {
            bq(var0, g / 2, var5 + 5, 56, U[6] >> 1, true, 16777076);
        }

        if (1 == ar()) {
            aD(var0, 55, g / 2, var2 + 240 - 42 + 14, 120, U[6]);
        }

    }

    public static void bV(Graphics var0, int var1, int var2) {
        ac(var0, var1, var2, 8, 0);
        A(var0, 24, var1 + 126, var2 + 100, 0);
        int var3 = B(24);

        for(int var4 = 0; var4 < 3; ++var4) {
            A(var0, 7, var1 + 126 - 6 * var4, var2 + 118, 0);
            A(var0, 8, var1 + 126 + var3 + 6 * var4, var2 + 118, 0);
        }

    }

    public static void bW(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        var0.fillRect(var1, var2, 240, 240);
        int var3 = var2 + 4;
        bj(var0, aC(23), g / 2, var3, currentFont.stringWidth(aC(23)) + 8, 2);
        int var4 = var3 + currentFontHeight + 8 + 6;
        A(var0, 58, var1, var4, 0);
        z(var0, V[U[6] >> 3 & 1], var1 + 144, var4, 0);
        int var5 = var4 + 130 + 1;
        int var6 = (g - 232) / 2;
        bt(var0, X, var6, var5, 232, U[3], 12, 16777215, 16056665);
        setColorOfRGBInt(var0, 0);
        var0.drawRect(var6, var5 - 1, 231, currentFontHeight + 1);
        setColorOfRGBInt(var0, 16763955);
        var0.fillRect(var1, var5, 4, currentFontHeight);
        var0.fillRect(var6, var5 - 1, 1, 1);
        var0.fillRect(var6, var5 + currentFontHeight, 1, 1);
        var0.fillRect(var6 + 232, var5, 4, currentFontHeight);
        var0.fillRect(var6 - 1, var5 - 1, 1, 1);
        var0.fillRect(var6 + 232 - 1, var5 + currentFontHeight, 1, 1);
        byte var7;
        if (U[5] - 1 <= U[4]) {
            var7 = 22;
        } else {
            var7 = 87;
        }

        cb(var0, 0, aC(var7), g / 2, var2 + 240 - 36, 160, 28, 2, 0);
        A(var0, 27, var1 + 1, var2 + 1, 0);
        A(var0, 28, var1 + 240 - 1 - B(28), var2 + 1, 0);
        A(var0, 37 + (U[6] >> 1 & 1), var1 + 240 - 44, var2 + var4 + 1, 0);
        A(var0, 12, var1 + 240 - 38, var4 + 9, 0);
        A(var0, 92, var1 + 140, var5 - 16, 0);
        aD(var0, 55, g / 2, var2 + 240 - 36 + C(55) / 2, 160, U[6]);
    }

    public static void bX(Graphics var0, int var1, int var2) {
        int var3 = bu(aC(67));
        if (I) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240);
            bj(var0, aC(67), g / 2, var2 + 3, currentFont.stringWidth(aC(67)) + 8, 2);
        } else {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2 + bm(Y, 0, 1) - 5, 240, 240 - (bm(Y, 0, 1) - 5 + C(0)));
        }

        A(var0, 0, var1, var2 + 240 - C(0), 0);
        bl(var0, var1, aC(66), var2 + 3 + var3 + 3, U[6], 12, 16056665, 16777215);
        int var4 = var2 + 3 + var3 + 3 + currentFontHeight + 12;
        ca(var0, g / 2, var4, false);

        for(int var5 = 0; var5 < 2; ++var5) {
            bk(var0, var5, Y, 0);
        }

        bq(var0, g / 2, var4 + 5, 56, U[6] >> 1, false, 16777076);
        aD(var0, 55, var1 + bm(Y, ar(), 0), var2 + bm(Y, ar(), 1) + bm(Y, ar(), 3) / 2, bm(Y, ar(), 2), U[6]);
    }

    public static void bY(Graphics var0, int var1, int var2) {
        bs(var0, var1, var2);
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
        A(var0, 1, var1, var2 + 240 - C(1), 0);
        byte var5 = 0;
        A(var0, 3, var1 + 240 + var5, var2 + 10, 1);
        cc(var0, var1 + 50, var2 + 108, 12, -8, 5, var4, var3);
    }

    public static void ce() {
        for(int var0 = 0; var0 < aa.length; ++var0) {
            aa(aa[var0]);
        }

        bA();
        bB(0);
        cd(0);
    }

    public static void cd(int var0) {
        T = true;
        switch(var0) {
            case 0:
                R(6);
                break;
            case 1:
                R(1);
                T = false;
                ab[0] = 0;
                ag(2, true);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 2:
                R(6);
                break;
            case 3:
                R(6);
                ag(1, false);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 4:
                R(6);
                break;
            case 5:
                R(6);
                ag(1, false);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 6:
                R(6);
                break;
            case 7:
                R(1);
                ag(2, true);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 8:
                aO(O, 179, 2, 39);
        }

        StackMap = true;
        ab[3] = 0;
        ab[1] = var0;
    }

    public static void cf() {
        for(int var0 = 0; var0 < 2; ++var0) {
            if (ac[var0] != null) {
                ac[var0].dispose();
                ac[var0] = null;
            }
        }

        System.gc();

        for(int var1 = 0; var1 < aa.length; ++var1) {
            try {
                Y(aa[var1]);
            } catch (Exception var3) {
            }
        }

        System.gc();
    }

    public static void co() {
        int var10002 = ab[3]++;
        switch(ab[1]) {
            case 0:
                cd(1);
                break;
            case 1:
                cg();
                break;
            case 2:
                ch();
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
        if (s(2097152L)) {
            ao();
            ap(133, 6);
        } else {
            if (ab[0] != 0) {
                if (aq(s(1048576L), false, false) != -1) {
                    cd(2);
                } else if (!s(1048576L)) {
                    if (s(196608L)) {
                        bB(9);
                        ab[0] = 0;
                    } else if (s(786432L)) {
                        bB(0);
                        ab[0] = 0;
                    }
                }
            } else if (bM(s(65536L), s(262144L), s(131072L), s(524288L), s(1048576L), bL())) {
                ab[0] = 1;
            }

            ah(ab[0]);
        }
    }

    public static void ch() {
        cf();
        cp();
        DataInputStream var0 = null;

        try {
            ad = 0L;
            var0 = aV(13);
            aW(var0);
            int var1 = var0.read();
            if (0 < var1) {
                String var2 = aX(var0, var1);
                aY(O, 2, 1, var2);
            } else {
                byte[] var16 = new byte[10];
                var0.read(var16);

                for(int var4 = 0; var4 < 2; ++var4) {
                    int var3 = var0.readUnsignedShort();
                    ac[var4] = bO(var0, var3);
                }

                ab[4] = 0;
                ab[5] = 0;
                ab[2] = 0;
                aZ(var16);
                cd(3);
            }
        } catch (Exception var14) {
            f("e:" + var14);
            aY(O, 2, 1, aC(92));
        } finally {
            if (var0 != null) {
                try {
                    var0.close();
                } catch (Exception var13) {
                }
            }

            l(6, false);
        }

    }

    public static void cp() {
        ba();
        bb(1, 2);
        bb(2, 3);

        for(int var0 = 0; var0 < 10; ++var0) {
            bb(3 + var0, bR(var0));
        }

    }

    public static void ci() {
        if (aq(s(1048576L), s(65536L), s(262144L)) != -1) {
            cd(4);
        }

    }

    public static void cj() {
        if (45 <= ab[3] || s(1048576L)) {
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

        if (6 < ab[3] && aq(s(1048576L), s(65536L), s(262144L)) != -1) {
            cd(6);
        }

    }

    public static void cl() {
        if (30 < ab[3]) {
            l(6, false);
            cd(7);
        }

    }

    public static void cm() {
        if (s(2097152L)) {
            ao();
            ap(139, 6);
        } else {
            if (6 < ab[3]) {
                switch(aq(s(1048576L), s(131072L), s(524288L))) {
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

    public static void cx(Graphics var0, int var1, int var2) {
        switch(ab[1]) {
            case 0:
            default:
                break;
            case 1:
                cq(var0, var1, var2);
                break;
            case 2:
                cr(var0, var1, var2);
                break;
            case 3:
            case 4:
                cs(var0, var1, var2);
                break;
            case 5:
                ct(var0, var1, var2);
                break;
            case 6:
                cu(var0, var1, var2);
                break;
            case 7:
                cv(var0, var1, var2);
                break;
            case 8:
                cw(var0, var1, var2);
        }

    }

    public static void cq(Graphics var0, int var1, int var2) {
        int var3 = bu(aC(30));
        if (I) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240 - C(0));
            bj(var0, aC(30), g / 2, var2 + 2, 230, 2);
        }

        A(var0, 0, var1, var2 + 240 - C(0), 0);
        bl(var0, var1, aC(68), var2 + 2 + var3 + 2, ab[3], 12, 16056665, 16777215);
        boolean var4 = 0 == ar() & (ab[3] & 4) != 0;
        int var5 = var2 + 2 + var3 + 2 + currentFontHeight + 3;
        ca(var0, g / 2, var5, var4);
        cb(var0, 1, aC(18), g / 2, var2 + 240 - 42, 100, 28, 2, 0);
        if (I) {
        }

        if (I) {
            bp(var0, g / 2, var5 + 5, 56, ab[3] >> 1, true);
        } else {
            bq(var0, g / 2, var5 + 5, 56, ab[3] >> 1, true, 16777076);
        }

        if (1 == ar()) {
            aD(var0, 39, g / 2, var2 + 240 - 42 + C(39) / 2, 100, ab[3]);
        }

    }

    public static void cr(Graphics var0, int var1, int var2) {
        bV(var0, var1, var2);
    }

    public static void cs(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        var0.fillRect(var1, var2, 240, 240);
        int var3 = var2 + 68;
        setColorOfRGBInt(var0, 6532583);
        var0.fillRect(var1, var3, 240, 118);
        int var4 = var2 + 4;
        bj(var0, aC(31), g / 2, var4, 200, 2);
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

        A(var0, 6, var1, var3 + 76, 0);
        A(var0, 6, var1 + 120, var3 + 76, 0);
        A(var0, 66, g / 2, var3, 2);
        H(var0, (g - 36) / 2, var3 + 21, 36, 8, 3805255, 16315136);
        H(var0, (g - 36) / 2, var5, 36, 8, 3805255, 16315136);
        if (ab[1] == 3) {
            cb(var0, 0, aC(75), g / 2, var2 + 240 - 44, 100, 28, 2, 0);
            aD(var0, 39, g / 2, var2 + 240 - 44 + C(39) / 2, 100, ab[3]);
        }

        A(var0, 27, var1 + 1, var2 + 1, 0);
        A(var0, 28, var1 + 240 - 1 - B(28), var2 + 1, 0);
        A(var0, 37 + (ab[3] >> 1 & 1), var1 + 240 - 44, var3 + 2, 0);
        A(var0, 12, var1 + 240 - 38, var3 + 10, 0);
    }

    public static void ct(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        var0.fillRect(var1, var2, 240, 240);
        int var3 = var2 + 4;
        bj(var0, aC(31), g / 2, var3, 200, 2);
        int var4 = var2 + 68;
        int var5 = ab[4];
        z(var0, ac[var5], var1 + 0, var4, 0);
        cb(var0, 0, aC(32), g / 2, var2 + 240 - 44, 160, 28, 2, 0);
        A(var0, 27, var1 + 1, var2 + 1, 0);
        A(var0, 28, var1 + 240 - 1 - B(28), var2 + 1, 0);
        A(var0, 37 + (ab[3] >> 1 & 1), var1 + 240 - 44, var4 + 2, 0);
        A(var0, 12, var1 + 240 - 38, var4 + 10, 0);
        aD(var0, 39, g / 2, var2 + 240 - 44 + C(39) / 2, 160, ab[3]);
    }

    public static void cu(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        var0.fillRect(var1, var2, 240, 240);
        A(var0, 0, var1, var2 + 240 - C(0), 0);
        bj(var0, aC(77), g / 2, var2 + 2, 200, 2);
        byte var3 = 16;
        byte var4 = 6;
        byte var5 = 11;
        int var6 = var4 + var3 * (var5 - 1);
        cc(var0, (g - var6) / 2, var2 + 68, var3, 0, var4, ab[3], var5);
        A(var0, 34, g / 2, var2 + 90, 2);
    }

    public static void cv(Graphics var0, int var1, int var2) {
        int var3 = bu(aC(69));
        int var4;
        if (I) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240);
            bj(var0, aC(69), g / 2, var2 + 3, currentFont.stringWidth(aC(69)) + 8, 2);
        } else {
            var4 = var2 + bm(ae, 0, 1);
            var4 += bm(ae, 0, 3) / 2;
            var4 -= C(39) / 2;
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var4, 240, 240 - (var4 + C(39) / 2));
        }

        A(var0, 0, var1, var2 + 240 - C(0), 0);
        bl(var0, var1, aC(70), var2 + 3 + var3 + 3, ab[3], 12, 16056665, 16777215);

        for(var4 = 0; var4 < 2; ++var4) {
            bk(var0, var4, ae, 0);
        }

        int var5 = var2 + 3 + var3 + 3 + currentFontHeight + 12;
        if (I) {
            bo(var0, g / 2, var5, 2, 0, 16756418, 13722050, 16756418);
            bp(var0, g / 2, var5 + 10, 56, ab[3] >> 1, false);
        } else {
            bq(var0, g / 2, var5 + 10, 56, ab[3] >> 1, false, 16756418);
        }

        br(var0, g / 2, var5 + 4, 62, false, I);
        aD(var0, 39, var1 + bm(ae, ar(), 0), var2 + bm(ae, ar(), 1) + bm(ae, ar(), 3) / 2 + 1, bm(ae, ar(), 2), ab[3]);
    }

    public static void cw(Graphics var0, int var1, int var2) {
        bs(var0, var1, var2);
    }

    public static void cz() {
        bA();
        bB(0);
        cy(0);
    }

    public static void cy(int var0) {
        T = true;
        switch(var0) {
            case 0:
                R(6);
                break;
            case 1:
                R(1);
                T = false;
                af[0] = 0;
                ag(2, true);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 2:
                R(6);
                ag(1, false);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 3:
                R(1);
                ag(2, true);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 4:
                R(1);
        }

        StackMap = true;
        af[2] = 0;
        af[1] = var0;
    }

    public static void cA() {
        if (ag != null) {
            ag.dispose();
            ag = null;
        }

        for(int var0 = 0; var0 < 2; ++var0) {
            ah[var0] = null;
        }

        System.gc();
    }

    public static void cF() {
        int var10002 = af[2]++;
        switch(af[1]) {
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
        if (s(2097152L)) {
            ao();
            ap(145, 6);
        } else {
            if (af[0] != 0) {
                if (aq(s(1048576L), false, false) != -1) {
                    cy(2);
                } else if (!s(1048576L)) {
                    if (s(196608L)) {
                        bB(9);
                        af[0] = 0;
                    } else if (s(786432L)) {
                        bB(0);
                        af[0] = 0;
                    }
                }
            } else if (bM(s(65536L), s(262144L), s(131072L), s(524288L), s(1048576L), bL())) {
                af[0] = 1;
            }

            ah(af[0]);
        }
    }

    public static void cC() {
        cA();
        cG();
        DataInputStream var0 = null;

        try {
            var0 = aV(13);
            aW(var0);
            int var1 = var0.read();
            if (0 < var1) {
                String var2 = aX(var0, var1);
                aY(O, 2, 1, var2);
            } else {
                int var14 = var0.read();
                ah[0] = aX(var0, var14);
                af[3] = var0.readUnsignedShort();
                var14 = var0.readUnsignedShort();
                ag = bO(var0, var14);
                var14 = var0.readUnsignedShort();
                ah[1] = aX(var0, var14);
                cy(3);
            }
        } catch (Exception var12) {
            f("e:" + var12);
            aY(O, 2, 1, aC(92));
        } finally {
            if (var0 != null) {
                try {
                    var0.close();
                } catch (Exception var11) {
                }
            }

            l(6, false);
        }

    }

    public static void cG() {
        ba();
        byte var0 = 4;
        if (ai) {
            var0 = 9;
        }

        bb(1, var0);
        bb(2, 4);

        for(int var1 = 0; var1 < 10; ++var1) {
            bb(3 + var1, bR(var1));
        }

    }

    public static void cD() {
        if (s(2097152L)) {
            ao();
            ap(151, 7);
        } else {
            if (6 < af[2]) {
                switch(aq(s(1048576L), s(131072L), s(524288L))) {
                    case 0:
                        T(4);
                        break;
                    case 1:
                        P("http://tamapark.gs.keitaiarchive.org/cgi-bin/album.cgi?uid=NULLGWDOCOMO&op=latest");
                }
            }

        }
    }

    public static void cE() {
    }

    public static void cL(Graphics var0, int var1, int var2) {
        switch(af[1]) {
            case 0:
            default:
                break;
            case 1:
                cH(var0, var1, var2);
                break;
            case 2:
                cI(var0, var1, var2);
                break;
            case 3:
                cJ(var0, var1, var2);
                break;
            case 4:
                cK(var0, var1, var2);
        }

    }

    public static void cH(Graphics var0, int var1, int var2) {
        int var3 = bu(aC(36));
        int var4 = var2 + 2 + var3 + 2 + currentFontHeight + 3;
        boolean var5 = true;
        boolean var6 = true;
        if (I) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240);
            bj(var0, aC(36), g / 2, var2 + 2, 230, 2);
            if (ai) {
                setColorOfRGBInt(var0, 16777215);
                var0.fillRect(var1, var2 + 240 - 4, 4, 4);
            }
        } else {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2 + 240 - 42, 240, 193);
        }

        if (I) {
            bv(var0, var1, var4, 0, 16777215, 2210832, 10873360, 7786961);
            bp(var0, g / 2, var4 + 4 + 2, 56, af[2] >> 1, true);
        } else {
            bq(var0, g / 2, var4 + 4 + 2, 56, af[2] >> 1, true, 10873360);
        }

        boolean var7 = 0 == ar() & (af[2] & 4) != 0;
        br(var0, g / 2, var4 + 4, 62, var7, I);
        bl(var0, var1, aC(71), var2 + 2 + var3 + 2, af[2], 12, 16056665, 16777215);
        cb(var0, 1, aC(18), g / 2, var2 + 240 - 42, 100, 28, 2, 0);
        if (1 == ar()) {
            aD(var0, 35, g / 2, var2 + 240 - 42 + 14 + 1, 100, af[2]);
        }

    }

    public static void cI(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        var0.fillRect(var1, var2, 240, 240);
        A(var0, 0, var1, var2 + 240 - C(0), 0);
        int var3 = var2 + 4;
        bj(var0, aC(37), g / 2, var2 + 2, 200, 2);
        setColorOfRGBInt(var0, 16770939);
        var0.fillRect(var1, var2 + 46, 240, 108);
        A(var0, 60, g / 2, var2 + 46, 2);
        byte var4 = 16;
        byte var5 = 6;
        byte var6 = 11;
        int var7 = var5 + var4 * (var6 - 1);
        cc(var0, (g - var7) / 2, var2 + 46 + 108 + 8, var4, 0, var5, 0, var6);
    }

    public static void cJ(Graphics var0, int var1, int var2) {
        int var3 = currentFontHeight;
        setColorOfRGBInt(var0, 16763955);
        var0.fillRect(var1, var2, 240, 240);
        A(var0, 0, var1, var2 + 240 - C(0), 0);
        int var4 = currentFont.stringWidth(ah[0]) + 8;
        if (var4 < ag.getWidth()) {
            var4 = ag.getWidth();
        }

        bj(var0, ah[0], g / 2, var2 + 2, var4, 2);
        var4 = currentFontHeight;
        z(var0, ag, g / 2, var2 + 2 + var3 + 4 + 4, 2);
        bl(var0, var1, ah[1], var2 + 2 + var3 + 4 + 4 + ag.getHeight() + 7, af[2], 12, 16056665, 16777215);

        for(int var5 = 0; var5 < 2; ++var5) {
            bk(var0, var5, aj, 0);
        }

        aD(var0, 35, var1 + bm(aj, ar(), 0), var2 + bm(aj, ar(), 1) + bm(aj, ar(), 3) / 2 + 1, bm(aj, ar(), 2), af[2]);
    }

    public static void cK(Graphics var0, int var1, int var2) {
    }

    public static int cM(int var0, int var1) {
        return ak[var0 * 8 + var1];
    }

    public static void cO() {
        bA();
        ag(2, true);
        ah(al[0]);
        cN(0);
    }

    public static void cN(int var0) {
        T = true;
        switch(var0) {
            case 0:
                R(6);
                break;
            case 1:
                R(1);
                T = false;
                al[0] = 0;
                ag(2, true);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 2:
                R(6);
                ag(1, false);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 3:
                al[3] = 0;
                R(1);
                break;
            case 4:
                R(6);
                ag(1, false);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 5:
                R(1);
                ag(1, false);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 6:
                R(1);
                ag(1, false);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 7:
                R(1);
                ag(2, true);
                ah(0);
                aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
                break;
            case 8:
                aO(O, 181, 2, 30);
        }

        StackMap = true;
        al[2] = 0;
        al[1] = var0;
    }

    public static void cP() {
        int var0;
        for(var0 = 0; var0 < 2; ++var0) {
            if (am[var0] != null) {
                am[var0].dispose();
                am[var0] = null;
            }
        }

        for(var0 = 0; var0 < 3; ++var0) {
            an[var0] = null;
        }

        System.gc();
    }

    public static void cY() {
        int var10002 = al[2]++;
        switch(al[1]) {
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
        if (s(2097152L)) {
            ao();
            ap(158, 6);
        } else {
            if (al[0] != 0) {
                if (aq(s(1048576L), false, false) != -1) {
                    cN(2);
                } else if (!s(1048576L)) {
                    if (s(196608L)) {
                        bB(9);
                        al[0] = 0;
                    } else if (s(786432L)) {
                        bB(0);
                        al[0] = 0;
                    }
                }
            } else if (bM(s(65536L), s(262144L), s(131072L), s(524288L), s(1048576L), bL())) {
                al[0] = 1;
            }

            ah(al[0]);
        }
    }

    public static void cR() {
        if (10 < al[2]) {
            l(6, false);
            cN(3);
        }

    }

    public static void cS() {
        if (s(2097152L)) {
            ao();
            ap(164, 2);
        } else {
            al[4] = al[3];
            if (s(65536L)) {
                l(4, false);
                al[3] = cM(al[3], 2);
            } else if (s(131072L)) {
                l(4, false);
                al[3] = cM(al[3], 3);
            } else if (s(262144L)) {
                l(4, false);
                al[3] = cM(al[3], 4);
            } else if (s(524288L)) {
                l(4, false);
                al[3] = cM(al[3], 5);
            } else if (s(1048576L)) {
                l(5, false);
                cN(4);
            }

        }
    }

    public static void cT() {
        cP();
        cZ();
        DataInputStream var0 = null;

        try {
            var0 = aV(14);
            aW(var0);
            int var1 = var0.read();
            int var3;
            if (0 < var1) {
                String var2 = aX(var0, var1);
                if (var2.compareTo("1") == 0) {
                    var3 = var0.readUnsignedShort();
                    am[0] = bO(var0, var3);
                    cN(5);
                } else {
                    aY(O, 4, 1, var2);
                }
            } else {
                byte[] var15 = new byte[10];
                var0.read(var15);
                var3 = var0.read();
                an[2] = aX(var0, var3);
                var3 = var0.read();
                an[0] = aX(var0, var3);
                var3 = var0.read();
                an[1] = aX(var0, var3) + aC(49);
                var3 = var0.readUnsignedShort();
                am[0] = bO(var0, var3);
                var3 = var0.readUnsignedShort();
                am[1] = bO(var0, var3);
                aZ(var15);
                cN(6);
            }
        } catch (Exception var13) {
            f("e:" + var13);
            aY(O, 4, 1, aC(92));
        } finally {
            if (var0 != null) {
                try {
                    var0.close();
                } catch (Exception var12) {
                }
            }

            l(6, false);
        }

    }

    public static void cZ() {
        ba();
        bb(1, 5);
        bb(2, 3);

        for(int var0 = 0; var0 < 10; ++var0) {
            bb(3 + var0, bR(var0));
        }

        bb(13, al[3] + 1);
    }

    public static void cU() {
        if (s(2097152L)) {
            ao();
            ap(167, 1);
        } else {
            if (6 < al[2] && aq(s(1048576L), s(65536L), s(262144L)) != -1) {
                cN(3);
            }

        }
    }

    public static void cV() {
        if (s(2097152L)) {
            ao();
            ap(166, 1);
        } else {
            if (6 < al[2] && aq(s(1048576L), s(65536L), s(262144L)) != -1) {
                cN(7);
            }

        }
    }

    public static void cW() {
        if (s(2097152L)) {
            ao();
            ap(168, 7);
        } else {
            if (6 < al[2]) {
                switch(aq(s(1048576L), s(131072L), s(524288L))) {
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

    public static void di(Graphics var0, int var1, int var2) {
        switch(al[1]) {
            case 0:
            default:
                break;
            case 1:
                da(var0, var1, var2);
                break;
            case 2:
                db(var0, var1, var2);
                break;
            case 3:
                dc(var0, var1, var2);
                break;
            case 4:
                dd(var0, var1, var2);
                break;
            case 5:
                de(var0, var1, var2);
                break;
            case 6:
                df(var0, var1, var2);
                break;
            case 7:
                dg(var0, var1, var2);
                break;
            case 8:
                dh(var0, var1, var2);
        }

    }

    public static void da(Graphics var0, int var1, int var2) {
        int var3 = bu(aC(41));
        int var4 = var2 + 2 + var3 + 2 + currentFontHeight + 3;
        boolean var5 = true;
        boolean var6 = true;
        if (I) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240);
            bj(var0, aC(41), g / 2, var2 + 2, 230, 2);
        } else {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2 + 240 - 52, 240, 188);
        }

        if (I) {
            bv(var0, var1, var4, 0, 16777215, 3429838, 6728678, 7786961);
            bp(var0, g / 2, var4 + 4 + 2, 56, al[2] >> 1, true);
        } else {
            bq(var0, g / 2, var4 + 4 + 2, 56, al[2] >> 1, true, 6728678);
        }

        bl(var0, var1, aC(72), var2 + 2 + var3 + 2, al[2], 12, 16056665, 16777215);
        boolean var7 = 0 == ar() & (al[2] & 4) != 0;
        br(var0, g / 2, var4 + 4, 62, var7, I);
        cb(var0, 1, aC(18), g / 2, var2 + 240 - 42, 100, 28, 2, 0);
        if (1 == ar()) {
            aD(var0, 30, g / 2, var2 + 240 - 42 + 7 + 1, 100, al[2]);
        }

    }

    public static void db(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        var0.fillRect(var1, var2, 240, 240);
        A(var0, 0, var1, var2 + 240 - C(0), 0);
        bj(var0, aC(73), g / 2, var2 + 2, currentFont.stringWidth(aC(73)) + 8, 2);
        A(var0, 59, g / 2, var2 + 50, 2);
        short var3 = 176;
        cc(var0, (g - var3) / 2, var2 + 160, 16, 0, 6, al[2], 11);
    }

    public static void dc(Graphics var0, int var1, int var2) {
        int var3 = var1 + 168;
        int var4 = var2 + 76;
        int var5;
        if (I) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240);
            A(var0, 71, var1, var2 + 240 - C(71), 0);
            bj(var0, aC(6), g / 2, var2 + 3, currentFont.stringWidth(aC(6)) + 8, 2);
            bl(var0, var1, aC(74), var2 + 3 + bu(aC(6)) + 3, al[2], 12, 16056665, 16777215);
            bj(var0, aC(cM(al[3], 6)), var1 + 3, var4, 110, 0);

            for(var5 = 0; var5 < 7; ++var5) {
                int var6 = cM(var5, 7);
                if (al[3] == var5 && (al[2] & 4) != 0) {
                    ++var6;
                }

                A(var0, var6, var3 + cM(var5, 0), var4 + cM(var5, 1), 0);
            }
        } else {
            bl(var0, var1, aC(74), var2 + 3 + bu(aC(6)) + 3, al[2], 12, 16056665, 16777215);
            if (al[4] != al[3]) {
                bj(var0, aC(cM(al[3], 6)), var1 + 3, var4, 110, 0);
                A(var0, cM(al[4], 7), var3 + cM(al[4], 0), var4 + cM(al[4], 1), 0);
                var5 = cM(al[3], 7);
                if ((al[2] & 4) != 0) {
                    ++var5;
                }

                A(var0, var5, var3 + cM(al[3], 0), var4 + cM(al[3], 1), 0);
            } else if ((al[2] & 3) == 0) {
                var5 = cM(al[3], 7);
                if ((al[2] & 4) != 0) {
                    ++var5;
                }

                A(var0, var5, var3 + cM(al[3], 0), var4 + cM(al[3], 1), 0);
            }
        }

    }

    public static void dd(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        var0.fillRect(var1, var2, 240, 240);
        A(var0, 0, var1, var2 + 240 - C(0), 0);
        setColorOfRGBInt(var0, 6728679);
        var0.fillRect(var1, var2 + 114, 240, C(6));
        A(var0, 6, var1, var2 + 114, 0);
        A(var0, 6, var1 + 120, var2 + 114, 0);
        bj(var0, aC(43), g / 2, var2 + 2, currentFont.stringWidth(aC(43)) + 8, 2);
        A(var0, 76, g / 2, var2 + 102, 2);
    }

    public static void de(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        var0.fillRect(var1, var2, 240, 240);
        A(var0, 0, var1, var2 + 240 - C(0), 0);
        bj(var0, aC(89), g / 2, var2 + 2, currentFont.stringWidth(aC(89)) + 8, 2);
        int var3 = var2 + 62;
        A(var0, 4, var1 + 0, var3, 0);
        A(var0, 4, var1 + 120, var3, 0);
        z(var0, am[0], g / 2, var3 - 12, 2);
        int var4 = (currentFontHeight + 1) * 2 + currentFontHeight;
        int var5 = var2 + 240 - (var4 + 4) - 2;
        cb(var0, 0, aC(88), g / 2, var5 - 44, 100, 28, 2, 0);
        H(var0, (g - 232) / 2, var5, 232, var4 + 4, 16056665, 16056665);
        setColorOfRGBInt(var0, 16777215);
        drawString(var0, aC(90), g / 2, var5 + 2, 2);
        aD(var0, 30, g / 2, var5 - 44 + C(30) / 2, 100, al[2]);
    }

    public static void df(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        var0.fillRect(var1, var2, 240, 240);
        A(var0, 0, var1, var2 + 240 - C(0), 0);
        bj(var0, aC(48), g / 2, var2 + 2, currentFont.stringWidth(aC(48)) + 8, 2);
        z(var0, am[0], var1 + 0, var2 + 42, 0);
        z(var0, am[1], var1 + 120, var2 + 42, 0);
        int var3 = (currentFontHeight + 1) * 2 + currentFontHeight;
        int var4 = var2 + 240 - (var3 + 4) - 2;
        cb(var0, 0, aC(18), g / 2, var4 - 38, 100, 28, 2, 0);
        H(var0, (g - 232) / 2, var4, 232, var3 + 4, 16056665, 16056665);
        setColorOfRGBInt(var0, 16777215);
        drawString(var0, an[0], g / 2, var4 + 2, 2);
        drawString(var0, an[1], g / 2, var4 + 2 + currentFontHeight + 1, 2);
        drawString(var0, an[2], g / 2, var4 + 2 + (currentFontHeight + 1) * 2, 2);
        aD(var0, 30, g / 2, var4 - 38 + C(30) / 2, 100, al[2]);
    }

    public static void dg(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        var0.fillRect(var1, var2, 240, 240);
        int var3 = bu(aC(51));
        bj(var0, aC(51), g / 2, var2 + 2, currentFont.stringWidth(aC(51)) + 8, 2);
        bl(var0, var1, aC(91), var2 + 2 + var3 + 2, al[2], 12, 16056665, 16777215);

        for(int var4 = 0; var4 < 2; ++var4) {
            bk(var0, var4, ao, 0);
        }

        int var5 = var2 + 3 + var3 + 3 + currentFontHeight + 12;
        bv(var0, var1, var5, 0, 16777215, 16730112, 16751616, 7786961);
        br(var0, g / 2, var5 + 4, 62, false, true);
        bp(var0, g / 2, var5 + 10, 56, al[2] >> 1, false);
        aD(var0, 30, var1 + bm(ao, ar(), 0), var2 + bm(ao, ar(), 1) + bm(ao, ar(), 3) / 2 + 1, bm(ao, ar(), 2), al[2]);
    }

    public static void dh(Graphics var0, int var1, int var2) {
        bs(var0, var1, var2);
    }

    public static void dj(int var0, int var1) {
        ap[0] = var0;
        ap[1] = var1;
        ap[2] = 0;
        ap[3] = 1;
        R(3);
    }

    public static void dk() {
        ap[3] = 0;
    }

    public static void dl() {
        if (s(2097152L)) {
            dk();
        } else {
            int var10002;
            if (s(1835008L)) {
                var10002 = ap[2]++;
                if (ap[1] <= ap[2]) {
                    dk();
                }
            } else if (s(196608L)) {
                var10002 = ap[2]--;
                if (ap[2] < 0) {
                    ap[2] = 0;
                }
            }

        }
    }

    public static boolean dm() {
        return ap[3] != 0;
    }

    public static void dn(Graphics var0, int var1, int var2) {
        if (dm()) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240);
            A(var0, 0, var1, var2 + 240 - C(0), 0);
            int var3 = var1 + 4;
            int var4 = var2 + (240 - (currentFontHeight + 1) * 7) / 2;
            H(var0, var1 + 2, var4 - 1, 236, (currentFontHeight + 1) * 7 + 2, 16056665, 16056665);
            String var5 = aC(ap[0] + ap[2]);
            int var6 = N(var5, "\n");
            setColorOfRGBInt(var0, 16777215);

            for(int var7 = 0; var7 < var6; ++var7) {
                String var8 = O(var5, var7, 1, "\n");
                int var9 = var8.indexOf("$");
                if (var9 == -1) {
                    drawString(var0, var8, var3, var4, 0);
                } else {
                    int var10 = var3;

                    do {
                        String var11 = var8.substring(0, var9);
                        drawString(var0, var11, var10, var4, 0);
                        var10 += currentFont.stringWidth(var11);
                        if (var8.length() - 1 <= var9) {
                            break;
                        }

                        byte var12 = 0;
                        int var13 = var12 | Integer.parseInt(var8.substring(var9 + 1, var9 + 1 + 3)) << 16;
                        var13 |= Integer.parseInt(var8.substring(var9 + 4, var9 + 4 + 3)) << 8;
                        var13 |= Integer.parseInt(var8.substring(var9 + 7, var9 + 7 + 3));
                        setColorOfRGBInt(var0, var13);
                        var8 = var8.substring(var9 + 10);
                        var9 = var8.indexOf("$");
                    } while(var9 != -1);

                    drawString(var0, var8, var10, var4, 0);
                }

                var4 += currentFontHeight + 1;
            }

        }
    }

    public static void ao() {
        aq[6] = ar();
        aq[7] = dorr();
        aq[8] = dp();
        ag(4, true);
        ah(0);
        ap(0, -1);
        aq[4] = ar();
        aq[0] = 1;
        aq[5] = 0;
        R(3);
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
        switch(var0) {
            case 0:
            default:
                break;
            case 1:
                ag(4, true);
                ah(aq[4]);
                break;
            case 2:
                dj(aq[2], aq[3]);
                break;
            case 3:
                ag(2, true);
                ah(1);
                break;
            case 4:
                ag(2, true);
                ah(1);
        }

        aq[5] = 0;
        aq[1] = var0;
    }

    public static boolean ds() {
        return aq[0] != 0;
    }

    public static void dw() {
        if (ds()) {
            int var10002 = aq[5]++;
            switch(aq[1]) {
                case 0:
                    dq(1);
                    break;
                case 1:
                    dt();
                    break;
                case 2:
                    if (dm()) {
                        dl();
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
        if (s(2097152L)) {
            ag(aq[7], aq[8] != 0);
            ah(aq[6]);
            R(C);
            dr();
        } else {
            aq[4] = ar();
            switch(aq(s(1048576L), s(131072L), s(524288L))) {
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
                    p();
                    w();
            }

        }
    }

    public static void du() {
        if (s(2097152L)) {
            dq(1);
        } else {
            switch(aq(s(1048576L), s(196608L), s(786432L))) {
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
        if (s(2097152L)) {
            dq(1);
        } else {
            switch(aq(s(1048576L), s(196608L), s(786432L))) {
                case 0:
                    dx();
                    break;
                case 1:
                    dq(1);
            }

        }
    }

    public static void dB(Graphics var0, int var1, int var2) {
        if (ds()) {
            switch(aq[1]) {
                case 0:
                default:
                    break;
                case 1:
                    dy(var0, var1, var2);
                    break;
                case 2:
                    if (dm()) {
                        dn(var0, var1, var2);
                    }
                    break;
                case 3:
                    dz(var0, var1, var2);
                    break;
                case 4:
                    dA(var0, var1, var2);
            }

        }
    }

    public static void dy(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        int var3 = var1 + 120;
        int var4 = var2 + 5;
        var0.fillRect(var1, var2, 240, 240);
        bj(var0, aC(54), var3, var4, currentFont.stringWidth(aC(54)) + 8, 2);
        dC(var0, aC(55), 0, var4 + 2 + (currentFontHeight + 6) * 2);
        dC(var0, aC(56), 1, var4 + 2 + (currentFontHeight + 6) * 3);
        dC(var0, aC(57), 2, var4 + 2 + (currentFontHeight + 6) * 4);
        dC(var0, aC(58 + n[3]), 3, var4 + 2 + (currentFontHeight + 6) * 5);
        dD(var0, g / 2, var4 + 2 + currentFontHeight + 6 + 2, 86, aq[5]);
    }

    public static void dz(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        int var3 = var1 + 120;
        int var4 = var2 + 5;
        var0.fillRect(var1, var2, 240, 240);
        bj(var0, aC(60), var3, var4, currentFont.stringWidth(aC(60)) + 4, 2);
        dC(var0, aC(62), 0, var4 + 2 + (currentFontHeight + 6) * 2);
        dC(var0, aC(63), 1, var4 + 2 + (currentFontHeight + 6) * 3);
        dD(var0, g / 2, var4 + 2 + currentFontHeight + 6 + 2, 86, aq[5]);
    }

    public static void dA(Graphics var0, int var1, int var2) {
        setColorOfRGBInt(var0, 16763955);
        int var3 = var1 + 120;
        int var4 = var2 + 5;
        var0.fillRect(var1, var2, 240, 240);
        bj(var0, aC(61), var3, var4, currentFont.stringWidth(aC(61)) + 8, 2);
        dC(var0, aC(62), 0, var4 + 2 + (currentFontHeight + 6) * 2);
        dC(var0, aC(63), 1, var4 + 2 + (currentFontHeight + 6) * 3);
        dD(var0, g / 2, var4 + 2 + currentFontHeight + 6 + 2, 86, aq[5]);
    }

    public static void dC(Graphics var0, String var1, int var2, int var3) {
        int var4;
        int var5;
        if (var2 == ar()) {
            var5 = 16056665;
            var4 = 16777215;
        } else {
            var5 = 7786961;
            var4 = 3096512;
        }

        H(var0, (g - 220) / 2, var3, 220, currentFontHeight + 4, var5, var5);
        setColorOfRGBInt(var0, var4);
        drawString(var0, var1, g / 2, var3 + 2, 2);
    }

    public static void dD(Graphics var0, int var1, int var2, int var3, int var4) {
        int var5 = var1 - var3;

        for(int var6 = 0; var6 < 3; var5 += var3) {
            int var7 = 69 + (var6 + (var4 >> 2) & 1);
            A(var0, var7, var5 - B(var7) / 2, var2, 0);
            ++var6;
        }

    }

    public static void aY(int var0, int var1, int var2, String var3) {
        ar[0] = var0;
        ar[1] = var1;
        ar[2] = var2;
        as = var3;
        ag(2, true);
        ah(1);
        aN(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
        R(6);
        ar[3] = 1;
    }

    public static void dE() {
        ar[3] = 0;
        as = null;
    }

    public static void dF(int var0) {
        O = ar[0];
        dE();
        switch(ar[0]) {
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
            if (s(524288L)) {
                at -= (currentFontHeight + 1) * 7;
            } else if (s(131072L)) {
                at += (currentFontHeight + 1) * 7;
            }

            if (s(1L)) {
                au = !au;
            }

            switch(aq(s(1048576L), s(65536L), s(262144L))) {
                case 0:
                    dF(ar[1]);
                    break;
                case 1:
                    dF(ar[2]);
            }

        }
    }

    public static void dI(Graphics var0, int var1, int var2) {
        if (dG()) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240);
            A(var0, 0, var1, var2 + 240 - C(0), 0);
            int var3 = N(as, "\n");
            int var4 = (var3 - 1) * (currentFontHeight + 1) + currentFontHeight + 4;
            int var5 = var2 + (240 - (var4 + 4 + 8)) / 2;
            H(var0, (g - 232) / 2, var5, 232, var4, 16056665, 16056665);
            setColorOfRGBInt(var0, 16777215);
            drawString(var0, as, g / 2, var5 + 2, 2);
            cb(var0, 0, aC(88), g / 2 - 8, var5 + var4 + 4, 100, 28, 1, 0);
            cb(var0, 1, aC(9), g / 2 + 8, var5 + var4 + 4, 100, 28, 0, 0);
        }
    }

    public static String aC(int var0) {
        return av[var0];
    }

    public static boolean W() {
        DataInputStream var1 = null;
        boolean var2 = true;
        byte var3 = 100;

        try {
            int[] var4 = q(128);
            int var5 = 128 + (var4.length + 1) * 2;

            int var0;
            for(var0 = 0; var0 < var3; ++var0) {
                var5 += var4[var0];
            }

            var1 = Connector.openDataInputStream("scratchpad:///0;pos=" + var5);

            for(var0 = 0; var0 < 183; ++var0) {
                byte[] var6 = new byte[var4[var3 + var0]];
                var1.read(var6);
                av[var0] = new String(var6);
                Object var18 = null;
                System.gc();
            }
        } catch (Exception var16) {
            var2 = false;
        } finally {
            if (var1 != null) {
                try {
                    var1.close();
                    var1 = null;
                    System.gc();
                } catch (Exception var15) {
                }
            }

        }

        return var2;
    }

    public static void ag(int var0, boolean var1) {
        aw[1] = var0;
        aw[2] = var1 ? 1 : 0;
        aw[13] = 0;
    }

    public static void ah(int var0) {
        aw[0] = var0;
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
            var3 = ar();
            aw[13] = 0;
            l(5, false);
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
                l(4, false);
            }

            aw[13] = 0;
        }

        return var3;
    }

    public static int ar() {
        return aw[0];
    }

    public static int dorr() {
        return aw[1];
    }

    public static int dp() {
        return aw[2];
    }

    public static void bk(Graphics var0, int var1, int[] var2, int var3) {
        cb(var0, var1, aC(bm(var2, var1, 4)), i + bm(var2, var1, 0), j + bm(var2, var1, 1), bm(var2, var1, 2), bm(var2, var1, 3), bm(var2, var1, 5), var3);
    }

    public static void aw(Graphics var0, int var1, int[] var2, int var3) {
        dJ(var0, var1, ax(var2, var1, 4), ax(var2, var1, 5), i + ax(var2, var1, 0), j + ax(var2, var1, 1), ax(var2, var1, 2), ax(var2, var1, 3), ax(var2, var1, 6), var3);
    }

    public static int bm(int[] var0, int var1, int var2) {
        return var0[var1 * 6 + var2];
    }

    public static int ax(int[] var0, int var1, int var2) {
        return var0[var1 * 7 + var2];
    }

    public static void cb(Graphics var0, int var1, String var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        int var9;
        if (var7 == 2) {
            var9 = var3 - var5 / 2;
        } else if (var7 == 0) {
            var9 = var3;
        } else {
            var9 = var3 - var5;
        }

        boolean var15 = false;
        int var10;
        int var11;
        int var12;
        int var13;
        int var14;
        if (var1 == ar()) {
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

        switch(var8) {
            case 0:
                dK(var0, var2, var9, var4, var5, var6, var10, var11, var12, var13, var14, var15);
                break;
            case 1:
                dL(var0, var2, var9, var4, var5, var6, var10, var11, var12, var13, var14, var15);
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
        if (var1 == ar()) {
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

        switch(var9) {
            case 0:
                dM(var0, var14, var10, var5, var6, var7, var11, var12, var13, var15);
                break;
            case 1:
                dN(var0, var14, var10, var5, var6, var7, var11, var12, var13, var15);
        }

    }

    public static void dK(Graphics var0, String var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11) {
        dO(var0, var2, var3, var4, var5, var6, var7, var10, var11);
        if (var11) {
            var3 += 2;
        }

        int var12 = N(var1, "\n");
        var12 = currentFontHeight + (var12 - 1) * (currentFontHeight + 1);
        setColorOfRGBInt(var0, var9);
        drawString(var0, var1, var2 + var4 / 2, var3 + (var5 - var12) / 2, 2);
    }

    public static void dM(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9) {
        dO(var0, var2, var3, var4, var5, var6, var7, var8, var9);
        if (var9) {
            var3 += 2;
        }

        A(var0, var1, var2 + var4 / 2, var3 + (var5 - C(var1)) / 2, 2);
    }

    public static void dO(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
        if (!var8) {
            setColorOfRGBInt(var0, var7);
            var0.fillArc(var1 - 1, var2 + 2 - 1, var4 + 2, var4 + 2, 180, 90);
            var0.fillArc(var1 + var3 - var4 - 2, var2 + 2 - 1, var4 + 2, var4 + 2, -90, 90);
            var0.fillRect(var1 + var4 / 2, var2 + var4 + 1, var3 - var4, 2);
        } else {
            var2 += 2;
        }

        setColorOfRGBInt(var0, var5);
        var0.fillArc(var1 - 1, var2 - 1, var4 + 2, var4 + 2, 90, 180);
        var0.fillArc(var1 + var3 - var4 - 2, var2 - 1, var4 + 2, var4 + 2, -90, 180);
        var0.fillRect(var1 + var4 / 2, var2 - 1, var3 - var4, 1);
        var0.fillRect(var1 + var4 / 2, var2 + var4, var3 - var4, 1);
        setColorOfRGBInt(var0, var6);
        var0.fillArc(var1, var2, var4, var4, 90, 180);
        var0.fillArc(var1 + var3 - var4 - 1, var2, var4, var4, -90, 180);
        var0.fillRect(var1 + var4 / 2, var2, var3 - var4, var4);
    }

    public static void dL(Graphics var0, String var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11) {
        dP(var0, var2, var3, var4, var5, var6, var7, var10, var11);
        if (var11) {
            var3 += 2;
        }

        int var12 = N(var1, "\n");
        var12 = currentFontHeight + (var12 - 1) * (currentFontHeight + 1);
        setColorOfRGBInt(var0, var9);
        drawString(var0, var1, var2 + var4 / 2, var3 + (var5 - var12) / 2, 2);
    }

    public static void dN(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9) {
        dP(var0, var2, var3, var4, var5, var6, var7, var8, var9);
        if (var9) {
            var3 += 2;
        }

        A(var0, var1, var2 + var4 / 2, var3 + (var5 - C(var1)) / 2, 2);
    }

    public static void dP(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
        if (!var8) {
            H(var0, var1, var2 + var4 - 2, var3, 4, var7, var7);
        } else {
            var2 += 2;
        }

        H(var0, var1, var2, var3, var4, var5, var6);
    }

    public static void bA() {
        for(int var0 = 0; var0 < 4; ++var0) {
            ax[var0] = 0;
        }

        bB(0);
    }

    public static void bB(int var0) {
        ax[5] = ax[4];
        ax[4] = var0;
    }

    public static void aZ(byte[] var0) {
        for(int var1 = 0; var1 < 10; ++var1) {
            dQ(var1, var0[var1]);
        }

    }

    public static void dQ(int var0, int var1) {
        var1 %= 10;
        int var2 = ax[0 + var0 / 8];
        int var3 = 4 * (7 - var0 % 8);
        var2 &= ~(15 << var3);
        var2 |= var1 << var3;
        ax[0 + var0 / 8] = var2;
    }

    public static void dR(int var0, int var1) {
        var1 %= 10;
        int var2 = ax[2 + var0 / 8];
        int var3 = 4 * (7 - var0 % 8);
        var2 &= ~(15 << var3);
        var2 |= var1 << var3;
        ax[2 + var0 / 8] = var2;
    }

    public static void bS() {
        int var0 = bR(1);

        for(int var1 = 0; var1 < 10; ++var1) {
            dR(ay[var0 * 10 + var1], bR(var1));
        }

    }

    public static int bR(int var0) {
        int var1 = ax[0 + var0 / 8] >> 4 * (7 - var0 % 8) & 15;
        return var1;
    }

    public static int bT(int var0) {
        int var1 = ax[2 + var0 / 8] >> 4 * (7 - var0 % 8) & 15;
        return var1;
    }

    public static int bw() {
        return ax[4];
    }

    public static int bx() {
        return ax[5];
    }

    public static boolean bM(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, int var5) {
        int var6 = ax[4];
        boolean var7 = false;
        if (var4) {
            l(5, false);
            dQ(var6, (bR(var6) + 1) % 10);
        } else if (var1) {
            ++var6;
            if (10 <= var6) {
                var6 = 0;
                var7 = true;
            }

            bB(var6);
        } else if (var0) {
            --var6;
            if (var6 < 0) {
                var6 = 9;
                var7 = true;
            }

            bB(var6);
        } else if (var2) {
            var6 -= 5;
            if (var6 < 0) {
                var7 = true;
                var6 += 10;
            }

            bB(var6);
        } else if (var3) {
            var6 += 5;
            if (10 <= var6) {
                var7 = true;
                var6 -= 10;
            }

            bB(var6);
        } else if (0 <= var5) {
            l(5, false);
            dQ(var6, var5);
            ++var6;
            if (10 <= var6) {
                var6 = 0;
                var7 = true;
            }

            bB(var6);
        }

        return var7;
    }

    public static int bL() {
        byte var0 = -1;
        if (s(1L)) {
            var0 = 0;
        } else if (s(2L)) {
            var0 = 1;
        } else if (s(4L)) {
            var0 = 2;
        } else if (s(8L)) {
            var0 = 3;
        } else if (s(16L)) {
            var0 = 4;
        } else if (s(32L)) {
            var0 = 5;
        } else if (s(64L)) {
            var0 = 6;
        } else if (s(128L)) {
            var0 = 7;
        } else if (s(256L)) {
            var0 = 8;
        } else if (s(512L)) {
            var0 = 9;
        }

        return var0;
    }

    public static void by(Graphics var0, int var1, int var2, int var3) {
        for(int var4 = 0; var4 < 10; ++var4) {
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

        drawString(var0, "" + bR(var4), var1, var2, 0);
    }

    public static void cc(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        int var8 = var6 % 7;
        if (var8 < 0) {
            var8 += 7;
        }

        for(int var9 = 0; var9 < var7; ++var9) {
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
        var0.setClip(0, 0, g + 16, h + 16);
        var0.fillRect(g, h, var3, var3);
        int var10 = var2 + 2;
        int var11;
        if (var7) {
            var11 = -var3;
            var5 += var3 * ((var1[var2 + 0] & 255) - 1);
        } else {
            var11 = var3;
        }

        for(int var12 = 0; var12 < var9; ++var12) {
            int var13 = var5;

            for(int var14 = 0; var14 < var8; ++var14) {
                for(int var15 = 0; var15 < 8; ++var15) {
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

        for(int var12 = 0; var12 < var9; ++var12) {
            int var13 = var5;

            for(int var14 = 0; var14 < var8; ++var14) {
                for(int var15 = 0; var15 < 8; ++var15) {
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

    public static void bb(int var0, int var1) {
        aA[var0] = (byte)var1;
    }

    public static void ba() {
        bb(0, 16);
    }

    public static DataInputStream aV(int var0) throws Exception {
        return dU(aA, var0);
    }

    public static DataInputStream dU(byte[] var0, int var1) throws Exception {
        String var2 = dV(var0, var1);
        String var3 = "http://tamapark.gs.keitaiarchive.org/cgi-bin/iaserver.cgi?uid=NULLGWDOCOMO&data=" + var2;
        f("senddata:" + var3);
        DataInputStream var4 = null;
        HttpConnection var5 = null;
        DataOutputStream var6 = null;

        try {
            var5 = (HttpConnection)Connector.open(var3, 1, true);
            var5.setRequestMethod("GET");
            var5.connect();
            var4 = var5.openDataInputStream();
            byte[] var7 = new byte[1024];
            var6 = Connector.openDataOutputStream("scratchpad:///0;pos=85258");

            int var9;
            for(int var8 = (int)var5.getLength(); 0 < var8; var8 -= var9) {
                if (var8 < var7.length) {
                    var9 = var8;
                } else {
                    var9 = var7.length;
                }

                f("dlsize:" + var8 + " writeSize:" + var9);
                var4.read(var7, 0, var9);
                var6.write(var7, 0, var9);
            }
        } catch (Exception var24) {
            throw var24;
        } finally {
            try {
                if (var6 != null) {
                    var6.close();
                }
            } catch (Exception var23) {
            }

            try {
                if (var4 != null) {
                    var4.close();
                }
            } catch (Exception var22) {
            }

            try {
                if (var5 != null) {
                    var5.close();
                }
            } catch (Exception var21) {
            }

        }

        return Connector.openDataInputStream("scratchpad:///0;pos=85258");
    }

    public static String dV(byte[] var0, int var1) throws Exception {
        boolean var5 = false;
        int var6;
        if (var5) {
            var6 = var1 * 2;
        } else {
            var6 = var1 * 3;
        }

        byte[] var7 = new byte[var6];
        int var4 = 0;

        for(int var2 = 0; var2 < var1; ++var2) {
            if (!var5) {
                var7[var4] = 37;
                ++var4;
            }

            int var3 = var0[var2] & 255;

            for(int var8 = 1; var8 >= 0; --var8) {
                byte var9 = (byte)(var3 >> 4 * var8 & 15);
                if (var9 >= 10) {
                    var9 = (byte)(var9 - 10 + 65);
                } else {
                    var9 = (byte)(var9 + 48);
                }

                var7[var4] = var9;
                ++var4;
            }
        }

        return new String(var7);
    }

    public static String aX(DataInputStream var0, int var1) throws Exception {
        byte[] var2 = new byte[var1];
        var0.read(var2);
        String var3 = new String(var2);
        Object var4 = null;
        System.gc();
        return var3;
    }

    public static Image bO(DataInputStream var0, int var1) throws Exception {
        aB = 0L;
        aB |= (long)var1;
        byte[] var2 = new byte[var1];

        int var3;
        for(var3 = 0; var3 < var1; ++var3) {
            int var4 = var0.read();
            if (var4 == -1) {
                break;
            }

            var2[var3] = (byte)var4;
        }

        aB |= (long)var3 << 32;
        MediaImage var7 = MediaManager.getImage(var2);
        var7.use();
        Image var5 = var7.getImage();
        Object var6 = null;
        System.gc();
        return var5;
    }

    public static int bP(String var0) {
        boolean var1 = false;
        int var2 = 0;
        int var3 = 0;

        while(true) {
            var2 = var0.indexOf(39, var2);
            if (var2 == -1) {
                if (var1) {
                    f("Dialogue error: missing closing tag");
                }

                f("wordsCnt:" + var3);
                return var3;
            }

            f("numindex:" + var2);
            if (var1) {
                ++var3;
                var1 = false;
            } else {
                var1 = true;
            }

            ++var2;
        }
    }

    public static String bQ(String var0, int var1) {
        int var2 = 0;
        boolean var3 = false;
        int var4 = 0;
        int var5 = 0;

        String var6;
        while(true) {
            var5 = var0.indexOf(39, var5);
            if (var5 == -1) {
                var6 = "";
                break;
            }

            if (var3) {
                if (var1 == var2) {
                    var6 = var0.substring(var4, var5);
                    break;
                }

                ++var2;
                ++var5;
                var3 = false;
            } else {
                var3 = true;
                ++var5;
                var4 = var5;
            }
        }

        return var6;
    }

    public static void aW(DataInputStream var0) throws Exception {
    }

    public static IrRemoteControlFrame[] dW(int var0) {
        IrRemoteControlFrame[] var1 = new IrRemoteControlFrame[var0];

        for(int var2 = 0; var2 < var0; ++var2) {
            var1[var2] = new IrRemoteControlFrame();
        }

        return var1;
    }

    public static void aO(int var0, int var1, int var2, int var3) {
        aC[2] = var0;
        aC[3] = var1;
        aC[4] = var2;
        aC[5] = var3;
        dX(1);
    }

    public static void dX(int var0) {
        switch(var0) {
            case 0:
                aD.stop();
                break;
            case 1:
                R(6);
                ag(1, false);
                ah(0);
                aN(16777215, 7786961, 6594720, 16777215, 16777215, 6594720, 16763955, 13158600);
                break;
            case 2:
                R(6);
                ag(1, false);
                ah(0);
                break;
            case 3:
                R(1);
                ag(3, true);
                ah(0);
                break;
            case 4:
                aD.stop();
                R(6);
                ag(1, false);
                ah(0);
                break;
            case 5:
                aD.stop();
                R(6);
                ag(1, false);
                ah(0);
        }

        aC[1] = 0;
        aC[0] = var0;
    }

    public static void eb() {
        bS();
        dY(0, 96);
        dY(1, 8);
        dY(2, 0);
        dY(3, 0);

        int var1;
        for(int var0 = 0; var0 < 10; var0 += 2) {
            var1 = dZ(bT(var0)) << 4 | dZ(bT(var0 + 1));
            dY(4 + var0 / 2, var1);
        }

        dY(9, 0);
        dY(10, 0);
        dY(11, 0);
        dY(12, 0);
        dY(13, 0);
        dY(14, 0);
        var1 = 0;

        for(int var2 = 0; var2 < 15; ++var2) {
            var1 += ea(aE[var2]);
        }

        dY(15, ea(var1 & 255));
    }

    public static void dY(int var0, int var1) {
        aE[var0] = (byte)var1;
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
            switch(aC[0]) {
                case 1:
                    ec();
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

    public static void ec() {
        try {
            aD.stop();
            eb();
            aD.setCarrier(131, 131);
            aD.setCode0(0, 470, 730);
            aD.setCode1(0, 470, 1330);
            aF[0].setFrameData(aE, 128);
            aF[0].setRepeatCount(3);
            aF[0].setFrameDuration(2500);
            aF[0].setStartHighDuration(9600);
            aF[0].setStartLowDuration(2400);
            aF[0].setStopHighDuration(1200);
            aD.send(1, aF);
            aG = (new Date()).getTime();
            dX(2);
        } catch (Exception var1) {
            dX(5);
        }

    }

    public static void ed() {
        if (aq(s(1048576L), false, false) != -1) {
            dX(4);
        } else {
            if (750L < (new Date()).getTime() - aG) {
                aD.stop();
                l(6, false);
                dX(3);
            }

        }
    }

    public static void ee() {
        if (s(2097152L)) {
            ao();
            ap(aC[3], aC[4]);
        } else {
            switch(aq(s(1048576L), s(131072L), s(524288L))) {
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
        if (aq(s(1048576L), false, false) != -1) {
            dX(0);
        }

    }

    public static void bs(Graphics var0, int var1, int var2) {
        if (bc()) {
            setColorOfRGBInt(var0, 16763955);
            var0.fillRect(var1, var2, 240, 240);
            A(var0, 0, var1, var2 + 240 - C(0), 0);
            switch(aC[0]) {
                case 2:
                    eg(var0, var1, var2);
                    break;
                case 3:
                    eh(var0, var1, var2);
                    break;
                case 4:
                    ei(var0, var1, var2);
                    break;
                case 5:
                    ej(var0, var1, var2);
            }

        }
    }

    public static void eg(Graphics var0, int var1, int var2) {
        bj(var0, aC(98), g / 2, var2 + 5, currentFont.stringWidth(aC(98)) + 8, 2);
        A(var0, 67, g / 2 + (aC[1] * 8 - 60), var2 + 50, 2);
        cb(var0, 0, aC(38), g / 2, var2 + 50 + C(67) + 10, currentFont.stringWidth(aC(38)) + 8, currentFontHeight + 4, 2, 0);
        aD(var0, aC[5], g / 2, var2 + 50 + C(67) + 10 + (currentFontHeight + 4) / 2, currentFont.stringWidth(aC(18)) + 20, aC[1]);
    }

    public static void eh(Graphics var0, int var1, int var2) {
        bj(var0, aC(99), g / 2, var2 + 5, currentFont.stringWidth(aC(99)) + 8, 2);
        A(var0, 68, g / 2, var2 + 50, 2);
        int[] var3;
        if (aC[2] == 7) {
            var3 = aH;
        } else {
            var3 = aI;
        }

        for(int var4 = 0; var4 < 3; ++var4) {
            bk(var0, var4, var3, 0);
        }

        aD(var0, aC[5], var1 + bm(var3, ar(), 0), var2 + bm(var3, ar(), 1) + bm(var3, ar(), 3) / 2, bm(var3, ar(), 2) - 10, aC[1]);
    }

    public static void ei(Graphics var0, int var1, int var2) {
        bj(var0, aC(96), g / 2, var2 + 5, currentFont.stringWidth(aC(96)) + 8, 2);
        ek(var0, var1, var2);
    }

    public static void ej(Graphics var0, int var1, int var2) {
        bj(var0, aC(97), g / 2, var2 + 5, currentFont.stringWidth(aC(97)) + 8, 2);
        ek(var0, var1, var2);
    }

    public static void ek(Graphics var0, int var1, int var2) {
        A(var0, 29, g / 2, var2 + 50, 2);
        cb(var0, 0, aC(18), g / 2, var2 + 50 + C(29) + 10, currentFont.stringWidth(aC(18)) + 8, currentFontHeight + 4, 2, 0);
        aD(var0, aC[5], g / 2, var2 + 50 + C(29) + 10 + (currentFontHeight + 4) / 2, currentFont.stringWidth(aC(18)) + 20, aC[1]);
    }

    public static void dx() {
        e = false;
    }

    public static void T(int var0) {
        T = true;
        switch(O) {
            case 8:
                bE();
                break;
            case 9:
                cf();
                break;
            case 10:
                cA();
                break;
            case 11:
                cP();
        }

        switch(var0) {
            case 2:
                w = 0;
                break;
            case 3:
                w = 0;
                break;
            case 4:
                R(1);
                T = false;
                ak();
                break;
            case 5:
                R(1);
                az();
                break;
            case 6:
                R(1);
                aH();
                break;
            case 7:
                R(1);
                aM();
                break;
            case 8:
                R(1);
                bD();
                break;
            case 9:
                R(1);
                ce();
                break;
            case 10:
                R(1);
                cz();
                break;
            case 11:
                R(1);
                cO();
                break;
            default:
                T = false;
                R(6);
        }

        dr();
        dk();
        dE();
        O = var0;
        Exceptions = false;
        StackMap = true;
    }

    public static void b() {
        o();
        if (s(1024L)) {
            p();
            w();
        }

        if (ds()) {
            dw();
        } else if (dG()) {
            dH();
        } else {
            switch(O) {
                case -3:
                case -2:
                case -1:
                    ae();
                    break;
                case 0:
                    if (--aJ <= 0) {
                        n[4] = 3;
                        v();
                        T(1);
                    }
                    break;
                case 1:
                    T(2);
                    break;
                case 2:
                    U();
                    break;
                case 3:
                    X();
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
                    co();
                    break;
                case 10:
                    cF();
                    break;
                case 11:
                    cY();
                    break;
                default:
                    if (s(1048576L)) {
                        e = false;
                    }
            }

        }
    }

    public static void Exceptions(Graphics g) {
        try {
            if (ds()) {
                dB(g, i, j);
                return;
            }

            if (dG()) {
                dI(g, i, j);
                return;
            }

            switch(O) {
                case -3:
                    af(g, "Authenticating", i, j);
                    break;
                case -2:
                    af(g, "Communicating", i, j);
                    break;
                case -1:
                    af(g, "Preparing", i, j);
                case 0:
                case 1:
                default:
                    break;
                case 2:
                    ab(g, i, j);
                    break;
                case 3:
                    ad(g, i, j);
                    break;
                case 4:
                    au(g, i, j);
                    break;
                case 5:
                    aF(g, i, j);
                    break;
                case 6:
                    aK(g, i, j);
                    break;
                case 7:
                    bi(g, i, j);
                    break;
                case 8:
                    bZ(g, i, j);
                    break;
                case 9:
                    cx(g, i, j);
                    break;
                case 10:
                    cL(g, i, j);
                    break;
                case 11:
                    di(g, i, j);
            }
        } catch (Exception var2) {
            f("disp error" + var2.toString());
        }

    }

    public static void aE(Graphics g) {
        setColorOfRGB(g, 255, 255, 255);
        if (i > 0) {
            g.fillRect(0, 0, i, h);
            g.fillRect(i + 240, 0, i, h);
        }

        if (j > 0) {
            g.fillRect(0, 0, GameApp.g, j);
            g.fillRect(0, j + 240, GameApp.g, j);
        }

    }

    public void mediaAction(MediaPresenter var1, int var2, int var3) {
        if (var2 == 3) {
            StackMap(a, true);
        }

    }

    public void resume() {
        try {
            b.stop();
            I();
        } catch (Exception var3) {
        }

        try {
            Thread.sleep(50L);
        } catch (InterruptedException var2) {
        }

        Z();
        Z = true;
        StackMap = true;
    }

    public void timerExpired(Timer var1) {
        try {
            b.stop();
            if (!this.c) {
                this.c = true;
                if (Code == 0) {
                    J();
                    a();
                    b();
                    if ((d & 10) == 0) {
                        System.gc();
                    }

                    c(2);
                    ++d;
                    Code = 1;
                }

                if (Code == 1) {
                    d();
                }

                if (!e) {
                    IApplication.getCurrentApp().terminate();
                }

                this.c = false;
            }

            I();
        } catch (Exception var3) {
        }

    }

    public void e() {
        f = new GameScreen(this);
        f.setBackground(Graphics.getColorOfName(0));
        g = f.getWidth();
        h = f.getHeight();
        i = (g - 240) / 2;
        j = (h - 240) / 2;
        k = false;
        Display.setCurrent(f);
    }

    public void start() {
    }
}
