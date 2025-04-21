package utils;

import java.util.logging.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LogConfig {
    private static final String LOG_DIR = "src/logs";
    private static final Map<String, Handler> handlers = new HashMap<>();

    public static Logger getLogger(Class<?> clazz, Level level) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setLevel(level);
        return logger;
    }

    public static Logger getLogger(Class<?> clazz, String logFileName, Level level) {
        Logger logger = getLogger(clazz, level);
        try {
            // Если обработчик для этого файла уже создан - используем его
            if (!handlers.containsKey(logFileName)) {
                Files.createDirectories(Paths.get(LOG_DIR)); // получаем все файлы в папке
                FileHandler fileHandler = new FileHandler(
                        Paths.get(LOG_DIR, logFileName + ".log").toString(), true);
                fileHandler.setFormatter(new SimpleFormatter());
                handlers.put(logFileName, fileHandler);
            }
            for (Handler h : logger.getHandlers()) {
                logger.removeHandler(h);
            }
            logger.addHandler(handlers.get(logFileName));
            logger.setUseParentHandlers(false); // не выводить в консоль

        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Ошибка создания лог файла", e);
        }
        return logger;
    }
}