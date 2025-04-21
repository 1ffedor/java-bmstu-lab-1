package game;

import game.ui.player.GetPlayerNameState;
import game.ui.player.MenuContext;

import java.io.*;

public class GameLoader {
    // класс для загрузки игры и тп
    private static final String SAVES_DIR = "saves/";

    private Game game;

    public Game initGame() {
        MenuContext context = new MenuContext();
        context.setState(new GetPlayerNameState(context));
        context.run();
        game = context.get(Game.class);  // получаем экземпляр игры
        context.finish();
        return game;
    }

    public static boolean saveGame(Game game, String filename) {
        // создаем папку для сохранений
        new File(SAVES_DIR).mkdirs();
        try (ObjectOutputStream gameStream = new ObjectOutputStream(new FileOutputStream(SAVES_DIR + filename)))
        {
            gameStream.writeObject(game);
            return true;
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
    }

    public static Game loadGame(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(SAVES_DIR + filename))) {
            return (Game) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка загрузки игры: " + e.getMessage());
            return null;
        }
    }

    public static boolean deleteGame(String filename) {
        File saveFile = new File(SAVES_DIR + filename);
        if (saveFile.delete()) {
//            CustomLogger.outln("Сохранение успешно удалено");
            return true;
        } else {
//            CustomLogger.outln("Сохранение не удалено");
            return false;
        }
    }
}
