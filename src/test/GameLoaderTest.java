import game.Game;
import game.GameLoader;
import game.gamemap.MainMap;
import game.ui.player.MenuContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import players.ComputerPlayerTest;
import utils.LogConfig;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class GameLoaderTest {

    private static final Logger LOGGER = LogConfig.getLogger(ComputerPlayerTest.class, Level.INFO);  // инит логера


    @Test
    void testSaveGame() throws IOException {
        LOGGER.log(Level.INFO, "Тест начат");

        Game game = new Game(new MainMap(10, 10), new MenuContext());
        GameLoader.saveGame(game, "test.sav");
        assertNotNull(GameLoader.loadGame("test.sav"));

        LOGGER.log(Level.INFO, "Тест завершен");
    }

    @Test
    void testLoadGame() throws IOException {
        LOGGER.log(Level.INFO, "Тест начат");

        Game game = new Game(new MainMap(10, 10), new MenuContext());
        GameLoader.saveGame(game, "test.sav");
        assertNotNull(GameLoader.loadGame("test.sav"));

        LOGGER.log(Level.INFO, "Тест завершен");
    }

    @Test
    void testDeleteGame() throws IOException {
        LOGGER.log(Level.INFO, "Тест начат");

        Game game = new Game(new MainMap(10, 10), new MenuContext());
        GameLoader.saveGame(game, "test.sav");
        GameLoader.deleteGame("test.sav");
        assertNull(GameLoader.loadGame("test.sav"));

        LOGGER.log(Level.INFO, "Тест завершен");
    }
}