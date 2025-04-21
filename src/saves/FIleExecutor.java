package saves;

import game.ui.CustomLogger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FIleExecutor {
    // сохранение игр
    public static void createFile(String filePath) {
        // создать файл
        try {
            FileWriter fw = new FileWriter(filePath);
            fw.close();
//            Files.createFile(Paths.get(filePath)); //
        } catch (IOException e) {
            CustomLogger.outln(e.getMessage());
        }
    }
}
