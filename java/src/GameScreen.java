import com.nttdocomo.ui.Canvas;
import com.nttdocomo.ui.Graphics;

public class GameScreen extends Canvas {
    public GameScreen(GameApp app) {
    }

    @Override
    public void processEvent(int type, int param) {
        GameApp.processEvent(type, param);
    }

    @Override
    public void paint(Graphics g) {
        if (GameApp.drawState == 2) {
            GameApp.drawState = 3;
            if (GameApp.Exceptions) {
                g.lock();
                GameApp.fullDraw = GameApp.fullDrawOnNextPaint;
                GameApp.fullDrawOnNextPaint = false;
                if (GameApp.resumedDraw) {
                    GameApp.resumedDraw = false;
                }

                g.setFont(GameApp.currentFont);
                GameApp.draw(g);
                g.unlock(true);
            } else {
                GameApp.Exceptions = true;
            }

            GameApp.drawState = 0;
        }

    }
}
