import com.nttdocomo.ui.Display;

import javax.microedition.io.Connector;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static final String SCRATCHPAD = "ENGLISH_PATCH__Tamagotchi_Park.sp";

    public static void main(String[] args) throws IOException {
        Connector.setScratchpadFile(getScratchpadFile());
        new GameApp();
        Display.init();
    }

    private static File getScratchpadFile() throws IOException {
        Path target = Path.of(System.getProperty("java.io.tmpdir"), Main.SCRATCHPAD);
        if (Files.exists(target)) return target.toFile();

        try (InputStream in = Main.class.getClassLoader().getResourceAsStream(Main.SCRATCHPAD)) {
            if (in == null) throw new IOException("Resource not found: " + Main.SCRATCHPAD);
            Files.copy(in, target);
        }
        return target.toFile();
    }
}