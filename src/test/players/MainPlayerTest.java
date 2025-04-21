package players;

import game.Game;
import game.gamemap.MainMap;
import game.objects.MapObject;
import game.objects.heroes.Knight;
import game.players.ComputerPlayer;
import game.players.MainPlayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.LogConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class MainPlayerTest {
    private MainPlayer mainPlayer;
    private ComputerPlayer computerPlayer;
    private MainMap map;
    private static final Logger LOGGER = LogConfig.getLogger(ComputerPlayerTest.class, Level.INFO);  // инит логера


    @BeforeEach
    void setUp() {
        map = new MainMap(Game.MAP_WIDTH, Game.MAP_HEIGHT);
        mainPlayer = new MainPlayer("Игрок 1", 100, map);
        computerPlayer = new ComputerPlayer("Игрок 1", 100, map);
        map.fillPlayersZones(mainPlayer, computerPlayer);
    }

    @Test
    void testWin() {
        // проверка на возможность выиграть
        LOGGER.log(Level.INFO, "Тест начат");

        Knight knight1 = new Knight(mainPlayer);
        Knight knight2 = new Knight(computerPlayer);
        map.setMapObject(knight1, 8, 8);
        map.setMapObject(knight2, 9, 8);
        MapObject computerCastle = map.getMapObjectByCoords(9, 9);
        computerCastle.changeHp(computerCastle.getHp() - knight1.getDamage());
        map.moveObject(knight1, 1, 1);
        assertEquals(-1, computerPlayer.getStatus(), "Статус должен быть -1");
        LOGGER.log(Level.INFO, "Тест пройден");
    }

    @AfterEach
    void tearDown() {
        LOGGER.log(Level.INFO, "тесты" + this.getClass().getName() + " пройдены");
    }
}
