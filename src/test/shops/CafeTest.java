package shops;

import game.Game;
import game.gamemap.MainMap;
import game.objects.heroes.Knight;
import game.players.ComputerPlayer;
import game.players.MainPlayer;
import game.players.Npc;
import game.players.Player;
import game.shops.Cafe;
import game.shops.Hotel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.LogConfig;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class CafeTest {
    private static final Logger LOGGER = LogConfig.getLogger(Game.class, Level.INFO);  // инит логера
    private Cafe cafe;
    private Player player;
    private Knight knight;
    private Npc npc;
    private ComputerPlayer computerPlayer;
    private ByteArrayOutputStream outputStream;
    private MainMap map;

    @BeforeEach
    void setUp() {
        cafe = new Cafe();
        map = new MainMap(Game.MAP_WIDTH, Game.MAP_HEIGHT);
        player = new MainPlayer("TestPlayer", 100, map);
//        player2 = new ComputerPlayer("TestComputer", 100, map);
        knight = new Knight(player);
        npc = new Npc("NPC", 100, map);
        map.setMapObject(knight, 2,2);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Перенаправляем вывод в outputStream
        LOGGER.log(Level.INFO, "Сетап установлен");
    }

    @Test
    void testApplyBonus() {
        // применение бонуса
        int oldEnergy = knight.getEnergy();
        cafe.applyBonus(player, 10);
        assertEquals(oldEnergy + 10, knight.getEnergy(), "Энергия должна увеличиться");
    }

    @Test
    void testApplyBonusNpc() {
        // применение бонуса
        int oldEnergy = knight.getEnergy();
        cafe.applyBonus(npc, 10);
        assertNotEquals(oldEnergy + 10, knight.getEnergy(), "Энергия не должна увеличиться");
    }

    @AfterEach
    void tearDown() {
        LOGGER.log(Level.INFO, "тесты" + this.getClass().getName() + " пройдены");
    }
}