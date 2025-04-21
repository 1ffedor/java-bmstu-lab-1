//import game.Main;
//import game.gamemap.MainMap;
//import game.gamemap.MapLoader;
//import org.junit.jupiter.api.Test;
//import players.ComputerPlayerTest;
//import utils.LogConfig;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import static org.junit.jupiter.api.AssertionsKt.assertNotNull;
//
//public class MapLoaderTest {
//    private static final Logger LOGGER = LogConfig.getLogger(ComputerPlayerTest.class, Level.INFO);  // инит логера
//
//    @Test
//    public void testLoadMap() {
//        LOGGER.log(Level.INFO, "Тест начат");
//
//        MainMap mainMap = new MainMap(10, 10);
//        MapLoader.saveMap("testMap.txt", mainMap);
//        MainMap loadedMap = MapLoader.loadMap("testMap.txt");
//        assertNotNull(loadedMap, "Карта не должна быть null");
//        LOGGER.log(Level.INFO, "Тест завершен");
//    }
//
//    @Test
//    public void testSaveMap() {
//        LOGGER.log(Level.INFO, "Тест начат");
//
//        MainMap mainMap = new MainMap(10, 10);
//        MapLoader.saveMap("testMap.txt", mainMap);
//        MainMap loadedMap = MapLoader.loadMap("testMap.txt");
//        assertNotNull(loadedMap, "Карта не должна быть null");
//        LOGGER.log(Level.INFO, "Тест завершен");
//    }
//
//    @Test
//    public void deleteMap() {
//        LOGGER.log(Level.INFO, "Тест начат");
//
//        MainMap mainMap = new MainMap(10, 10);
//        MapLoader.saveMap("testMap.txt", mainMap);
//        MainMap loadedMap = MapLoader.loadMap("testMap.txt");
//        assertNotNull(loadedMap, "Карта не должна быть null");
//        LOGGER.log(Level.INFO, "Тест завершен");
//    }
//}
