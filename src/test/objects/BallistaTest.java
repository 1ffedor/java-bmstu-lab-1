package objects;

import game.gamemap.MainMap;
import game.objects.Ballista;
import game.objects.MapObject;
import game.objects.heroes.Knight;
import game.players.ComputerPlayer;
import game.players.MainPlayer;
import game.players.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.LogConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;



public class BallistaTest {

    private MainMap map;
    private Player player1;
    private Player player2;
    private Ballista ballista;
    private static final Logger LOGGER = LogConfig.getLogger(BallistaTest.class, Level.INFO);  // инит логера


    @BeforeEach
    void setUp() {
        map = new MainMap(10, 10);
        player1 = new MainPlayer("Игрок", 100,  map);
        player2 = new ComputerPlayer("Компьютер", 100, map);
        ballista = new Ballista(player1);
        map.setMapObject(ballista, 2,2);
        map.fillPlayersZones(player1, player2);
    }

    @Test
    void testCanAttackCastle() {
        LOGGER.log(Level.INFO, "Тест начат");
        assertFalse(ballista.canAttackCastle(), "Баллиста не должна мочь атаковать замок");
    }

    @Test
    void testAttack() {
        // проверка на атаку противника
        LOGGER.log(Level.INFO, "Тест начат");

        Knight knight = new Knight(player2);
        map.setMapObject(knight, 8, 8);
        int old_hp = knight.getHp();
        ballista.attack(knight, player1, ballista.getDamage());
        assertEquals(old_hp - ballista.getDamage(), knight.getHp(),
                "Hp должны уменьшиться");
        assertFalse(player1.isActiveStep(), "Игрок должен завершить ход");
    }

    @Test
    void testAttackCastle() {
        // проверка на атаку замка врага
        LOGGER.log(Level.INFO, "Тест начат");

        MapObject enemyCastle = map.getMapObjectByCoords(9, 9);
        int old_hp = enemyCastle.getHp();
        ballista.attack(enemyCastle, player1, ballista.getDamage());
        assertEquals(old_hp, enemyCastle.getHp(), "Hp не должны изменяться");
    }

    @AfterEach
    void tearDown() {
        LOGGER.log(Level.INFO, "тесты" + this.getClass().getName() + " пройдены");
    }
}
