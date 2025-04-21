package buildings;

import game.Game;
import game.buildings.Inferno;
import game.gamemap.MainMap;
import game.objects.units.Ghost;
import game.objects.units.Soldier;
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

class InfernoTest {

    private Inferno inferno;
    private Player player;
    private ByteArrayOutputStream outputStream;
    private MainMap map;
    private static final Logger LOGGER = LogConfig.getLogger(Game.class, Level.INFO);  // инит логера

    @BeforeEach
    void setUp() {
        inferno = new Inferno();
        map = new MainMap(Game.MAP_WIDTH, Game.MAP_HEIGHT);
        player = new MainPlayer("TestPlayer", 100, map);
        map.fillFirstPlayerCells(player);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testHandleChoiceBuySoldier() {
        LOGGER.log(Level.INFO, "Тест начат");

        inferno.handleChoice(player, 1);
        assertEquals(100 - Soldier.cost, player.getBalance(), "Баланс игрока должен уменьшиться на стоимость Soldier");
        assertFalse(outputStream.toString().contains("Неверный вариант!"), "Не должно быть сообщения об ошибке");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testHandleChoiceBuyGhost() {
        LOGGER.log(Level.INFO, "Тест начат");

        inferno.handleChoice(player, 2);
        assertEquals(100 - Ghost.cost, player.getBalance(), "Баланс игрока должен уменьшиться на стоимость Ghost");
        assertFalse(outputStream.toString().contains("Неверный вариант!"), "Не должно быть сообщения об ошибке");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testHandleChoiceInvalidOption() {
        LOGGER.log(Level.INFO, "Тест начат");

        inferno.handleChoice(player, 435699);
        assertTrue(outputStream.toString().contains("Неверный вариант!"), "Должно быть сообщение об ошибке");
        assertEquals(100, player.getBalance(), "Баланс игрока не должен измениться");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @AfterEach
    void tearDown() {
        LOGGER.log(Level.INFO, "тесты" + this.getClass().getName() + " пройдены");
    }
}