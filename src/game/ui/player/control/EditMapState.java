package game.ui.player.control;

import game.Game;
import game.gamemap.cells.Cell;
import game.gamemap.MainMap;
import game.gamemap.MapLoader;
import game.gamemap.cells.CellType;
import game.gamemap.cells.CellTypeLoader;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class EditMapState implements MenuState, Serializable {
    private MenuContext context;

    public EditMapState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln(String.format("Основное меню -> Управление картами -> %s -> Редактирование карты",
                context.get("mapName")));
        MapLoader.displayMap(context.getString("mapName"));
        try {
            List<CellType> cellTypes = CellTypeLoader.loadCellTypesFromXml();  // все типы клеток
            String symbolsDescription = cellTypes.stream()
                    .map(c -> String.format("%c - %s", c.getSymbol(),
                            c.getDescription() != null ? c.getDescription() : "нет описания"))
                    .collect(Collectors.joining("\n"));
            CustomLogger.outln(String.format("Введите новые символы карты построчно:\n%s", symbolsDescription));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
//        Scanner scanner = new Scanner(System.in);
        MainMap map = new MainMap(Game.MAP_WIDTH, Game.MAP_HEIGHT);
        for (int y = 0; y < Game.MAP_HEIGHT; y++) {
            CustomLogger.out(String.format("Строка %d:", y));
            String line = scanner.nextLine();
            if (line.length() != Game.MAP_HEIGHT) {
                CustomLogger.error("Введенных символов недостаточно. Повторите Ввод");
                y--;
                continue;
            }
            for (int x = 0; x < Game.MAP_WIDTH; x++) {
                char symbol = line.charAt(x);
                Cell cell = map.getCell(y, x);
                cell.setSymbol(symbol);
            }
        }
        MapLoader.saveMap(context.getString("mapName"), map);
        CustomLogger.outln("Редактирование завершено");
        context.setState(new ControlMapState(context));
    }
}