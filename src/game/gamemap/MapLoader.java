package game.gamemap;

import game.gamemap.cells.Cell;
import game.gamemap.cells.CellType;
import game.gamemap.cells.CellTypeLoader;
import game.ui.CustomLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapLoader {
    private static final String MAPS_DIR = "src/game/gamemap/maps/";

    public static List<String> getAllMaps() {
        // получить все карты которые есть в папке
        File mapsFolder = new File(MAPS_DIR);
        if (!mapsFolder.exists() || !mapsFolder.isDirectory()) {
            System.err.println("Директория с картами не найдена: " + MAPS_DIR);
            return new ArrayList<>();
        }
        ArrayList<String> maps = new ArrayList<>();
        for (String fileName : Objects.requireNonNull(mapsFolder.list())) {
            if (fileName.endsWith(".txt")) {
                maps.add(fileName);
            }
        }
        return maps;
    }

    public static MainMap loadMap(String fileName) {
        File mapFile = new File(MAPS_DIR + fileName);
        if (!mapFile.exists()) {
            CustomLogger.error("Файл карты не найден: " + fileName);
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(mapFile))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            if (lines.isEmpty()) {
                CustomLogger.error("Файл карты пуст: " + fileName);
                return null;
            }
            int height = lines.size();
            int width = lines.get(0).length();
            MainMap map = new MainMap(width, height);  // грузим карту
            List<CellType> cellTypes = CellTypeLoader.loadCellTypesFromXml();  // все типы клеток
            for (int y = 0; y < height; y++) {
                String currentLine = lines.get(y);
                if (currentLine.length() != width) {
                    System.err.println("Несоответствие ширины карты в строке " + y);
                    return null;
                }
                for (int x = 0; x < width; x++) {
                    char symbol = currentLine.charAt(x);
                    Cell cell = map.getCell(y, x);
                    CellType cellType = cellTypes.stream()
                            .filter(c -> c.getSymbol() == symbol)
                            .findFirst()
                            .orElse(null);
                    if (cellType == null) {
                        cell.setSymbol('#');
                        cell.setColor(Cell.EMPTY_COLOR);
                    }
                    else {
                        cell.setSymbol(cellType.getSymbol());
                        cell.setColor(cellType.getColor());
                        cell.setPenalty(cellType.getPenalty());
                        if (cellType.isCastle()) {
                            map.setComputerCastlePosition(new IntPair(y, x));
                        }
                        if (cellType.getDescription() != null) {
                            cell.setDescription(cellType.getDescription());
                        }
                    }
                }
            }
            return map;

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла карты: " + e.getMessage());
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean saveMap(String fileName, MainMap map) {
        File mapFile = new File(MAPS_DIR + fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(mapFile))) {
            for (int y = 0; y < map.getHeight(); y++) {
                StringBuilder line = new StringBuilder();
                for (int x = 0; x < map.getWidth(); x++) {
                    Cell cell = map.getCell(y, x);
                    line.append(cell.getSymbol());
                }
                writer.write(line.toString());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении карты: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteMap(String fileName) {
        File mapFile = new File(MAPS_DIR + fileName);
        if (!mapFile.exists()) {
            CustomLogger.error("Файл карты не найден: " + fileName);
            return false;
        }
        if (mapFile.delete()) {
            CustomLogger.info("Карта успешно удалена: " + fileName);
            return true;
        } else {
            CustomLogger.error("Не удалось удалить карту: " + fileName);
            return false;
        }
    }

    public static void displayMap(String fileName) {
        File mapFile = new File(MAPS_DIR + fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(mapFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Заменяем символы для лучшей читаемости (опционально)
                CustomLogger.outln(line);
            }
        } catch (IOException e) {
            CustomLogger.error("Ошибка при чтении карты: " + e.getMessage());
        }
    }
}