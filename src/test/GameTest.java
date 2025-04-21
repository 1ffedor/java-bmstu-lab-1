//import game.Game;
//import game.gamemap.MainMap;
//import game.players.ComputerPlayer;
//import game.players.MainPlayer;
//import game.players.Player;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import players.ComputerPlayerTest;
//import utils.LogConfig;
//
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GameTest {
//
//    private Game game;
//    private static final Logger LOGGER = LogConfig.getLogger(ComputerPlayerTest.class, Level.INFO);  // инит логера
//
//
//    @BeforeEach
//    void setUp() {
//        game = new Game(); // Инициализация игры перед каждым тестом
//    }
//
//    @Test
//    void testGameActive() {
//        // Проверяем, что игра активна после инициализации
//        LOGGER.log(Level.INFO, "Тест начат");
//
//        assertTrue(game.isGameActive(), "Игра должна быть активна после инициализации");
//        LOGGER.log(Level.INFO, "Тест пройден");
//    }
//
//    @Test
//    void testInitMap() {
//        // Проверяем, что карта инициализирована
//        LOGGER.log(Level.INFO, "Тест начат");
//
//        MainMap map = game.getMap();
//        assertNotNull(map, "Карта должна быть инициализирована");
//        assertEquals(Game.MAP_WIDTH, map.getWidth(), "Ширина карты должна быть " + Game.MAP_WIDTH);
//        assertEquals(Game.MAP_HEIGHT, map.getHeight(), "Высота карты должна быть " + Game.MAP_HEIGHT);
//        LOGGER.log(Level.INFO, "Тест пройден");
//    }
//
//    @Test
//    void testInitPlayers() {
//        // проверка на корректное создание игроков
//        LOGGER.log(Level.INFO, "Тест начат");
//
//        assertNotNull(game.getMainPlayer(), "Основной игрок должен быть создан");
//        assertNotNull(game.getComputerPlayer(), "Компьютерный игрок должен быть создан");
//
//        // Проверяем, что игроки добавлены в список players
//        ArrayList<Player> players = game.getPlayers();
//        assertEquals(2, players.size(), "В списке players должно быть 2 игрока");
//        assertInstanceOf(MainPlayer.class, players.get(0), "Первый игрок должен быть MainPlayer");
//        assertInstanceOf(ComputerPlayer.class, players.get(1), "Второй игрок должен быть ComputerPlayer");
//        LOGGER.log(Level.INFO, "Тест пройден");
//    }
//
//    @Test
//    void testInitDefaultHeroes() {
//        // проверка на наличие дефолтных героев
//        LOGGER.log(Level.INFO, "Тест начат");
//
//        assertNotNull(game.getMainPlayer().getMovingMapObjects(),
//                "Дефолтные герои игрока должен быть созданы");
//        assertNotNull(game.getComputerPlayer().getMovingMapObjects(),
//                "Дефолтные герои компьютера должны быть созданы");
//        LOGGER.log(Level.INFO, "Тест пройден");
//    }
//
//    @AfterEach
//    void tearDown() {
//        LOGGER.log(Level.INFO, "тесты " + this.getClass().getName() + " пройдены");
//    }
//}