package shops;

import game.Game;
import game.gamemap.MainMap;
import game.objects.heroes.Knight;
import game.players.ComputerPlayer;
import game.players.MainPlayer;
import game.players.Npc;
import game.players.Player;
import game.shops.Barbershop;
import game.shops.Cafe;
import game.ui.player.MenuContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.LogConfig;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class BarbershopTest {
    private static final Logger LOGGER = LogConfig.getLogger(Game.class, Level.INFO);  // инит логера
    private Barbershop barbershop;
    private Player player;
    private ComputerPlayer player2;
    private Knight knight;
    private Npc npc;
    private ComputerPlayer computerPlayer;
    private ByteArrayOutputStream outputStream;
    private MainMap map;
    private MenuContext context;


    @BeforeEach
    void setUp() {
        barbershop = new Barbershop();
        map = new MainMap(Game.MAP_WIDTH, Game.MAP_HEIGHT);
        player = new MainPlayer("TestPlayer", 100, map);
        player2 = new ComputerPlayer("TestComputer", 100, map);
        knight = new Knight(player);
        context = new MenuContext();
        player.addContext(context);
        context.addToStorage("computerPlayer", player2);
        npc = new Npc("NPC", 100, map);
        map.setMapObject(knight, 2,2);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Перенаправляем вывод в outputStream
        LOGGER.log(Level.INFO, "Сетап установлен");
    }

    @Test
    void testApplyBonus() {
        // применение бонуса
        int oldHp = player2.getCastle().getHp();
        barbershop.applyBonus(player, 10);
        assertEquals(oldHp - 10, player2.getCastle().getHp(), "HP должны уменьшиться");
    }

    @Test
    void testApplyBonusNpc() {
        // применение бонуса
        int oldHp = player.getCastle().getHp();
        barbershop.applyBonus(player, 10);
        assertEquals(oldHp, player.getCastle().getHp(), "HP не должны уменьшиться");
    }

    @AfterEach
    void tearDown() {
        LOGGER.log(Level.INFO, "тесты" + this.getClass().getName() + " пройдены");
    }
}