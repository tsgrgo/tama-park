import com.nttdocomo.ui.Canvas;
import com.nttdocomo.ui.Graphics;

public class GameScreen extends Canvas {
    public GameScreen(GameApp app) {
    }

    public void processEvent(int var1, int var2) {
        GameApp.Code(var1, var2);
    }

    public void paint(Graphics g) {
        if (GameApp.Code == 2) {
            GameApp.Code = 3;
            if (GameApp.Exceptions) {
                g.lock();
                GameApp.I = GameApp.StackMap;
                GameApp.StackMap = false;
                if (GameApp.Z) {
                    GameApp.Z = false;
                }

                g.setFont(GameApp.currentFont);
                GameApp.Exceptions(g);
                g.unlock(true);
            } else {
                GameApp.Exceptions = true;
            }

            GameApp.Code = 0;
        }

    }
}
