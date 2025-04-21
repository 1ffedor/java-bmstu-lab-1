package gamemap;

import game.gamemap.Cell;
import game.gamemap.MainMap;
import game.objects.MapObject;
import game.objects.heroes.Gollum;
import game.objects.heroes.Knight;
import game.objects.units.Soldier;
import game.players.ComputerPlayer;
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

class MainMapTest {

    private MainMap map;
    private Player player1;
    private Player player2;
    private static final Logger LOGGER = LogConfig.getLogger(MainMapTest.class, Level.INFO);  // инит логера

    @BeforeEach
    void setUp() {
        LOGGER.log(Level.INFO, "Инициализация тестовых данных");
        map = new MainMap(10, 10);
        player1 = new MainPlayer("Игрок", 100, map) {};
        player2 = new ComputerPlayer("Компьютер", 100, map) {};
    }

    @Test
    void testFillRoadCells() {
        LOGGER.log(Level.INFO, "Тест начат");

        map.fillRoadCells();
        for (int i = 0; i < map.getWidth(); i++) {
            Cell cell = map.getCell(i, i);
            assertEquals(Cell.ROAD, cell.getType());
        }

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testFillFirstPlayerCells() {
        LOGGER.log(Level.INFO, "Тест начат");

        map.fillFirstPlayerCells(player1);
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Cell cell = map.getCell(row, col);
                if (!cell.getType().equals(Cell.ROAD)) {
                    assertTrue(map.getPlayerCells().get(player1).contains(cell),
                            "Клетка должна быть зоной игрока 1");
                }
            }
        }

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testFillSecondPlayerCells() {
        LOGGER.log(Level.INFO, "Тест начат");

        map.fillSecondPlayerCells(player2);
        for (int row = map.getHeight() - 1; row >= map.getHeight() - 5; row--) {
            for (int col = map.getWidth() - 1; col >= map.getWidth() - 5; col--) {
                Cell cell = map.getCell(row, col);
                if (!cell.getType().equals(Cell.ROAD)) {
                    assertTrue(map.getPlayerCells().get(player2).contains(cell),
                            "Клетка должна быть зоной игрока 2");
                }
            }
        }

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testGetFreePlayerCell() {
        LOGGER.log(Level.INFO, "Тест начат");

        map.fillFirstPlayerCells(player1);
        Cell freeCell = map.getFreePlayerCell(player1);
        assertNotNull(freeCell, "Должна быть хотя бы 1 свободная клетка");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testSetAndGetMapObject() {
        LOGGER.log(Level.INFO, "Тест начат");

        MapObject testObject = new MapObject("TestObject", "T", 100, player1) {};
        map.setMapObject(testObject, 2, 2);
        MapObject retrievedObject = map.getMapObjectByCoords(2, 2);
        assertEquals(testObject, retrievedObject, "Объект должен находиться в клетке");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testSetMapObject() {
        LOGGER.log(Level.INFO, "Тест начат");

        MapObject testObject = new Knight(player1);
        map.setMapObject(testObject, 2, 2);
        assertEquals(map.getMapObjectByCoords(2, 2), testObject, "Объект не должен быть null");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testGetCell() {
        LOGGER.log(Level.INFO, "Тест начат");

        assertNotNull(map.getCell(1, 1), "Клетка должна существовать");
        assertNull(map.getCell(-1, -1), "Клетка не должна существовать");
        assertNull(map.getCell(1010, 1123490), "Клетка не должна существовать");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testMoveObject() {
        LOGGER.log(Level.INFO, "Тест начат");

        MapObject testObject = new Knight(player1);
        map.setMapObject(testObject, 2, 2);
        map.moveObject(testObject, 1, 1);
        assertNull(map.getMapObjectByCoords(2, 2), "Объект должен не должен быть null");
        assertEquals(testObject, map.getMapObjectByCoords(3, 3), "Объект должен не должен быть null");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testHandeEmptyCell() {
        LOGGER.log(Level.INFO, "Тест начат");

        Knight knight = new Knight(player1);
        knight.setEnergy(0);
        map.setMapObject(knight, 0, 1);
        Cell cell = map.getCell(1, 1);
//        map.handleEmptyCell(knight, cell);
//        assertEquals(map.getCellFromMapObject(knight), map.getCell(0, 1),
//                "Объект должен оставаться в клетке");
        knight.setEnergy(100);
        map.handleEmptyCell(knight, cell);
        assertEquals(100 - cell.getPenalty(player1), knight.getEnergy(),
                String.format("Энергия должна быть %d", knight.getEnergy()));

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testMoveNullCell() {
        LOGGER.log(Level.INFO, "Тест начат");

        Knight knight = new Knight(player1);
        map.setMapObject(knight, 0, 1);
        map.moveObject(knight, 0, -1);
        assertEquals(map.getCellFromMapObject(knight), map.getCell(0, 1),
                "Объект должен оставаться в клетке");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testHandleFriendlyCell() {
        LOGGER.log(Level.INFO, "Тест начат");

        Knight knight = new Knight(player1);
        Soldier soldier = new Soldier(player1);
        map.setMapObject(knight, 0, 1);
        map.setMapObject(soldier, 0, 2);
        map.moveObject(knight, 1, 0);
        assertEquals(map.getCellFromMapObject(knight), map.getCell(0, 1),
                "Объект должен оставаться в клетке");
        assertEquals(map.getCellFromMapObject(soldier), map.getCell(0, 2),
                "Объект должен оставаться в клетке");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testHandleEnemyCell() {
        LOGGER.log(Level.INFO, "Тест начат");

        Knight knight = new Knight(player1);
        Gollum gollum = new Gollum(player2);
        map.setMapObject(knight, 0, 1);
        map.setMapObject(gollum, 0, 2);
        map.moveObject(knight, 1, 0);
        assertEquals(Gollum.defHp - knight.getDamage(), gollum.getHp(), "Hp должны уменьшиться");
        assertEquals(map.getCellFromMapObject(knight), map.getCell(0, 1),
                "Рыцарь должен остаться в клетке");
        assertEquals(map.getCellFromMapObject(gollum), map.getCell(0, 2),
                "Голем должен остаться в клетке");
        gollum.changeHp(gollum.getHp() + 1);
        map.moveObject(knight, 1, 0);
        assertNull(map.getCellFromMapObject(gollum), "Голем должен пропасть из клетки");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testDisplayEmptyMap() {
        LOGGER.log(Level.INFO, "Тест начат");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        map.display();

        StringBuilder expectedOutput = new StringBuilder();
        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                Cell cell = map.getCell(row, col);
                expectedOutput.append(cell.getSymbol()).append(" ");
            }
            expectedOutput.append("\n");
        }
        String expected = expectedOutput.toString().trim().replace("\r", "");
        String actualOutput = outputStream.toString().trim().replace("\r", "");
        assertEquals(expected, actualOutput, "Пустая карта должна отображаться корректно");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testDisplayMapWithObjects() {
        LOGGER.log(Level.INFO, "Тест начат");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        map.setMapObject(new Knight(player1), 0, 1);
        map.setMapObject(new Knight(player2), 8, 1);
        map.display();

        StringBuilder expectedOutput = new StringBuilder();
        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                Cell cell = map.getCell(row, col);
                if ((row == 0 && col == 1) || (row == 8 && col == 1)) {
                    expectedOutput.append(Knight.symbol).append(" ");
                }
                else {
                    expectedOutput.append(cell.getSymbol()).append(" ");
                }
            }
            expectedOutput.append("\n");
        }
        String expected = expectedOutput.toString().trim().replace("\r", "");
        String actualOutput = outputStream.toString().trim().replace("\r", "");
        assertEquals(expected, actualOutput, "Карта должна отображаться корректно");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testDisplayMapWithMovingObjects() {
        LOGGER.log(Level.INFO, "Тест начат");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Knight knight = new Knight(player1);
        Knight knight2 = new Knight(player2);
        map.setMapObject(knight, 0, 1);
        map.setMapObject(knight2, 8, 1);
        map.moveObject(knight, 1, 0);
        map.display();

        StringBuilder expectedOutput = new StringBuilder();
        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                Cell cell = map.getCell(row, col);
                expectedOutput.append(cell.getSymbol()).append(" ");
            }
            expectedOutput.append("\n");
        }
        String expected = expectedOutput.toString().trim().replace("\r", "");
        String actualOutput = outputStream.toString().trim().replace("\r", "");
        assertTrue(actualOutput.contains(expected), "Карта должна отображаться корректно");

        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @AfterEach
    void tearDown() {
        LOGGER.log(Level.INFO, "тесты" + this.getClass().getName() + " пройдены");
    }
}