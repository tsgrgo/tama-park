import com.nttdocomo.ui.Canvas;
import com.nttdocomo.ui.Graphics;

public class GameScreen extends Canvas {
    public GameScreen(GameApp app) {
    }

    @Override
    public void processEvent(int type, int param) {
        GameApp.Code(type, param);
    }

    @Override
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
