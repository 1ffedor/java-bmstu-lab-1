package buildings;

import game.Game;
import game.buildings.Forge;
import game.gamemap.MainMap;
import game.objects.Ballista;
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

class ForgeTest {
    private static final Logger LOGGER = LogConfig.getLogger(Game.class, Level.INFO);  // инит логера
    private Forge forge;
    private Player player;
    private ByteArrayOutputStream outputStream;
    private MainMap map;

    @BeforeEach
    void setUp() {
        forge = new Forge(); // Создаем объект Forge
        map = new MainMap(Game.MAP_WIDTH, Game.MAP_HEIGHT);
        player = new MainPlayer("TestPlayer", 100, map);
        map.fillFirstPlayerCells(player);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Перенаправляем вывод в outputStream
        LOGGER.log(Level.INFO, "Сетап установлен");
    }

    @Test
    void testHandleChoiceBuyBallista() {
        // Вызываем метод handleChoice с выбором 1
        LOGGER.log(Level.INFO, "тест начат");
        forge.handleChoice(player, 1);
        assertEquals(100 - Ballista.cost, player.getBalance(), "Баланс игрока должен уменьшиться на стоимость Ballista");
        assertFalse(outputStream.toString().contains("Неверный вариант!"), "Не должно быть сообщения об ошибке");
        LOGGER.log(Level.INFO, "тест пройден");
//        try {


//        } catch (Throwable e) {
//            LOGGER.log(Level.SEVERE, e.getMessage());
//            assert false;
//        }
    }

    @AfterEach
    void tearDown() {
        LOGGER.log(Level.INFO, "тесты" + this.getClass().getName() + " пройдены");
    }
}