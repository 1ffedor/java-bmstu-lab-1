package game.ui;

public class CustomLogger {
    // error - урон
    // то что тесты в консоль, то что в игре - в файл

    public CustomLogger() {
    }

    public static void out(String message) {
        System.out.print(message);  // выводим инфо сообщение
    }

    public static void outln(String message) {
        System.out.println(message);
    }

    public static void info(String message) {
        // инфо сообщение
        outln("\uD83D\uDFE2" + message);
    }

    public static void warn(String message) {
        // предупреждение
        outln("\uD83D\uDFE1" + message);
    }

    public static void error(String message) {
        // ошибка
        outln("\uD83D\uDD34" + message);
    }
}
