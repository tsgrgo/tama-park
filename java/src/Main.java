import com.nttdocomo.ui.Display;

import javax.microedition.io.Connector;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Connector.setScratchpadFile(new File(""));

        GameApp app = new GameApp();
        Display.init();
    }
}