import javax.microedition.io.Connector;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Connector.setScratchpadFile(new File(""));

        GameApp app = new GameApp();

        // --------------------------

        Frame f = new Frame("Game");
        f.add(GameApp.canvas.unwrap());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        GameApp.canvas.unwrap().requestFocus();

        f.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                f.dispose();
                System.exit(0);
            }
        });
    }
}