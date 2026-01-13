import com.nttdocomo.ui.Canvas;
import com.nttdocomo.ui.Graphics;

public class GameScreen extends Canvas {
    public GameScreen(GameApp var1) {
    }

    public void processEvent(int var1, int var2) {
        GameApp.Code(var1, var2);
    }

    public void paint(Graphics var1) {
        if (GameApp.Code == 2) {
            GameApp.Code = 3;
            if (GameApp.Exceptions) {
                var1.lock();
                GameApp.I = GameApp.StackMap;
                GameApp.StackMap = false;
                if (GameApp.Z) {
                    GameApp.Z = false;
                }

                var1.setFont(GameApp.currentFont);
                GameApp.Exceptions(var1);
                var1.unlock(true);
            } else {
                GameApp.Exceptions = true;
            }

            GameApp.Code = 0;
        }

    }
}
