package players;

import game.Game;
import game.buildings.Building;
import game.buildings.Forge;
import game.gamemap.MainMap;
import game.objects.*;
import game.objects.heroes.Knight;
import game.objects.units.Soldier;
import game.players.MainPlayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.LogConfig;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private MainMap map;
    private MainPlayer player;

    private static final Logger LOGGER = LogConfig.getLogger(PlayerTest.class, Level.INFO);  // инит логера


    @BeforeEach
    void setUp() {
        map = new MainMap(Game.MAP_WIDTH, Game.MAP_HEIGHT);
        player = new MainPlayer("TestPlayer", 100, map);
        map.fillFirstPlayerCells(player);
    }

    @Test
    void testGetName() {
        LOGGER.log(Level.INFO, "Тест начат");

        assertEquals("TestPlayer", player.getName(), "Имя игрока должно быть TestPlayer");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testStatus() {
        LOGGER.log(Level.INFO, "Тест начат");

        player.setStatus(1);
        assertEquals(1, player.getStatus(), "Статус должен быть 0");

        player.setStatus(-1);
        assertEquals(-1, player.getStatus(), "Статус должен быть -1");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testInitHeroById() {
        LOGGER.log(Level.INFO, "Тест начат");

        MapObject knight = player.initHeroById(1);
        assertInstanceOf(Knight.class, knight, "Должен быть инициализирован объект Knight");

        MapObject soldier = player.initHeroById(3);
        assertInstanceOf(Soldier.class, soldier, "Должен быть инициализирован объект Soldier");

        assertNull(player.initHeroById(999), "Не должно быть инициализировано ни одного объекта");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

//    @Test
//    void testStartStepAndFinishStep() {
//        LOGGER.log(Level.INFO, "Тест начат");
//
//        player.startStep();
//        assertEquals(0, player.getStatus(), "Стасу должен быть 0");
//
//        player.finishStep();
//        assertEquals(2, player.getStatus(), "Статус должен быть -1");
//        LOGGER.log(Level.INFO, "Тест пройден");
//    }

    @Test
    void testGetBalance() {
        assertEquals(100, player.getBalance(), "Баланс должен быть 100");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testUpdatingBalance() {
        LOGGER.log(Level.INFO, "Тест начат");

        int old_balance = player.getBalance();
        player.updateBalance(10);
        assertEquals(old_balance + 10, player.getBalance(), String.format("Баланс должен быть %d",
                old_balance + 10));
        old_balance = player.getBalance();
        player.updateBalance(-60);
        assertEquals(old_balance - 60 , player.getBalance(),
                String.format("Баланс должен быть %d", old_balance -60));
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testSpawnObject() {
        LOGGER.log(Level.INFO, "Тест начат");

        player.updateBalance(-player.getBalance() + Knight.cost);
        player.spawnObject(Knight.typeId);
        assertEquals(0, player.getBalance(), String.format("Баланс игрока должен быть %d", 0));
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testBuyObject() {
        LOGGER.log(Level.INFO, "Тест начат");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        player.updateBalance(-player.getBalance());
        player.buyObject(9999, Knight.typeId);
        System.setOut(originalOut);
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Недостаточно денег для покупки"),
                "Сообщение об ошибке должно быть выведено в консоль");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testIsHaveBallista() {
        LOGGER.log(Level.INFO, "Тест начат");

        assertFalse(player.isHaveBallista(), "Баллисты не должно быть");
        player.spawnObject(Ballista.typeId);
        assertTrue(player.isHaveBallista(), "Баллиста должна быть");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testBuyBallista() {
        LOGGER.log(Level.INFO, "Тест начат");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        player.updateBalance(Ballista.cost);
        player.buyObject(Ballista.cost, Ballista.typeId);
        player.buyObject(Ballista.cost, Ballista.typeId);
        System.setOut(originalOut);
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("заспавнен"), "Сообщение об ошибке должно быть выведено в консоль");
        assertTrue(consoleOutput.contains("уже куплен"), "Сообщение об ошибке должно быть выведено в консоль");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testAddBuilding() {
        LOGGER.log(Level.INFO, "Тест начат");

        Building building = new Forge();
        player.addBuilding(building);
        assertTrue(player.isHaveBuilding(building), "Здание Кузница должно быть куплено");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @Test
    void testBuyBuilding() {
        // тест покупки здания
        LOGGER.log(Level.INFO, "Тест начат");

        Building building = new Forge();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        player.updateBalance(-player.getBalance());
        player.buyBuilding(building);
        System.setOut(originalOut);
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Недостаточно денег для покупки"), "Сообщение должно быть выведено");
        player.updateBalance(building.getCost());
        player.buyBuilding(building);
        assertEquals(0, player.getBalance(), "Баланс должен уменьшится");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @AfterEach
    void tearDown() {
        LOGGER.log(Level.INFO, "тесты" + this.getClass().getName() + " пройдены");
    }
}