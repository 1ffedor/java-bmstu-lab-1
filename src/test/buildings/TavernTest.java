package buildings;

import game.Game;
import game.buildings.Tavern;
import game.gamemap.MainMap;
import game.objects.heroes.Gollum;
import game.objects.heroes.Knight;
import game.players.MainPlayer;
import game.players.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.LogConfig;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class TavernTest {

    private Tavern tavern;
    private Player player;
    private ByteArrayOutputStream outputStream;
    private MainMap map;
    private static final Logger LOGGER = LogConfig.getLogger(TavernTest.class, Level.INFO);  // инит логера

    @BeforeEach
    void setUp() {
        tavern = new Tavern();
        map = new MainMap(Game.MAP_WIDTH, Game.MAP_HEIGHT);
        player = new MainPlayer("TestPlayer", 100, map);
        map.fillFirstPlayerCells(player);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testHandleChoiceBuyKnight() {
        LOGGER.log(Level.INFO, "Тест начат");

        tavern.handleChoice(player, 1);
        assertEquals(100 - Knight.cost, player.getBalance(),
                "Баланс игрока должен уменьшиться на стоимость Knight");
        assertFalse(outputStream.toString().contains("Неверный вариант!"),
                "Не должно быть сообщения об ошибке");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testHandleChoiceBuyGollum() {
        LOGGER.log(Level.INFO, "Тест начат");

        tavern.handleChoice(player, 2);
        assertEquals(100 - Gollum.cost, player.getBalance(),
                "Баланс игрока должен уменьшиться на стоимость Gollum");
        assertFalse(outputStream.toString().contains("Неверный вариант!"),
                "Не должно быть сообщения об ошибке");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testHandleChoiceInvalidOption() {
        LOGGER.log(Level.INFO, "Тест начат");

        tavern.handleChoice(player, 99);
        assertTrue(outputStream.toString().contains("Неверный вариант!"),
                "Должно быть сообщение об ошибке");
        assertEquals(100, player.getBalance(),
                "Баланс игрока не должен измениться");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @AfterEach
    void tearDown() {
        LOGGER.log(Level.INFO, "тесты" + this.getClass().getName() + " пройдены");
    }
}