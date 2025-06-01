package shops;

import game.Game;
import game.buildings.Forge;
import game.gamemap.MainMap;
import game.objects.Ballista;
import game.objects.heroes.Knight;
import game.players.ComputerPlayer;
import game.players.MainPlayer;
import game.players.Npc;
import game.players.Player;
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

class HotelTest {
    private static final Logger LOGGER = LogConfig.getLogger(Game.class, Level.INFO);  // инит логера
    private Hotel hotel;
    private Player player;
    private Knight knight;
    private ComputerPlayer computerPlayer;
    private ByteArrayOutputStream outputStream;
    private MainMap map;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        map = new MainMap(Game.MAP_WIDTH, Game.MAP_HEIGHT);
        player = new MainPlayer("TestPlayer", 100, map);
//        player2 = new ComputerPlayer("TestComputer", 100, map);
        knight = new Knight(player);
        map.setMapObject(knight, 2,2);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Перенаправляем вывод в outputStream
        LOGGER.log(Level.INFO, "Сетап установлен");
    }

    @Test
    void testApplyBonus() {
        // применение бонуса
        int old_hp = knight.getHp();
        hotel.applyBonus(player, 10);
        assertEquals(old_hp + 10, knight.getHp(), "Hp должны уменьшиться");
    }

    @Test
    void testApplyBonusNpc() {
        // применение бонуса
        int old_hp = knight.getHp();
        Npc npc = new Npc("NPC", 100, map);
        hotel.applyBonus(npc, 10);
        assertEquals(old_hp, knight.getHp(), "Hp не должны уменьшиться");
    }

    @AfterEach
    void tearDown() {
        LOGGER.log(Level.INFO, "тесты" + this.getClass().getName() + " пройдены");
    }
}