package players;

import game.Game;
import game.buildings.Forge;
import game.gamemap.MainMap;
import game.objects.MapObject;
import game.objects.heroes.Knight;
import game.players.ComputerPlayer;
import game.players.MainPlayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.LogConfig;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class ComputerPlayerTest {
    private MainPlayer mainPlayer;
    private ComputerPlayer computerPlayer;
    private MainMap map;
    private static final Logger LOGGER = LogConfig.getLogger(ComputerPlayerTest.class, Level.INFO);  // инит логера


    @BeforeEach
    void setUp() {
        map = new MainMap(Game.MAP_WIDTH, Game.MAP_HEIGHT);
        mainPlayer = new MainPlayer("Игрок 1", 100, map);
        computerPlayer = new ComputerPlayer("Игрок 1", 100, map);
    }

    @Test
    void testWin() {
        // проверка на возможность выиграть
        LOGGER.log(Level.INFO, "Тест начат");

        map.fillPlayersZones(mainPlayer, computerPlayer);
        Knight knight1 = new Knight(mainPlayer);
        Knight knight2 = new Knight(computerPlayer);
        map.setMapObject(knight1, 0, 1);
        map.setMapObject(knight2, 1, 1);
        MapObject playerCastle = map.getMapObjectByCoords(0, 0);
        playerCastle.changeHp(playerCastle.getHp() - knight2.getDamage());
        map.moveObject(knight2, -1, -1);
        assertEquals(-1, mainPlayer.getStatus(), "Статус должен быть -1");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testAttackNeighbors() {
        // проверка на логику бота
        LOGGER.log(Level.INFO, "Тест начат");

        map.fillPlayersZones(mainPlayer, computerPlayer);
        Knight knight1 = new Knight(mainPlayer);
        int old_hp = knight1.getHp();
        Knight knight2 = new Knight(computerPlayer);
        map.setMapObject(knight1, 8, 8);
        map.setMapObject(knight2, 9, 8);
        computerPlayer.attackNeighbors();
        assertNotEquals(old_hp, knight1.getHp(), "Hp должны уменьшиться");
        assertTrue(knight1.isALive(), "Объект должен остаться жив");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testBuyMapObjects() {
        // проверка на логику бота
        LOGGER.log(Level.INFO, "Тест начат");

        map.fillPlayersZones(mainPlayer, computerPlayer);
        ArrayList<MapObject> old = map.getMapObjectsByPlayer(computerPlayer);
        computerPlayer.buyMapObjects();
        assertNotEquals(old, map.getMapObjectsByPlayer(computerPlayer), "Массивы должны быть разными");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testBuyBuilding() {
        // проверка на логику бота
        LOGGER.log(Level.INFO, "Тест начат");

        Object old = computerPlayer.getBuildings().clone();
        computerPlayer.buyBuilding(new Forge());
        assertNotEquals(old, computerPlayer.getBuildings(), "Массивы должны быть разными");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testMoveToOppositeCastle() {
        // проверка на движение в направлении замка врага
        LOGGER.log(Level.INFO, "Тест начат");

        map.fillPlayersZones(mainPlayer, computerPlayer);
        Knight knight2 = new Knight(computerPlayer);
        map.setMapObject(knight2, 9, 7);
        computerPlayer.moveToOppositeCastle();
        assertNotEquals(map.getCell(9, 7), map.getCellFromMapObject(knight2),
                "Объект должен переместиться");
        assertTrue(map.getCellFromMapObject(knight2).getCol() ==
                map.getCellFromMapObject(knight2).getRow(),
                "Объект должен переместиться на диагональную клетку");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @AfterEach
    void tearDown() {
        LOGGER.log(Level.INFO, "тесты" + this.getClass().getName() + " пройдены");
    }
}
