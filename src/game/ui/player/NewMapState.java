package game.ui.player;

import game.Game;
import game.gamemap.Cell;
import game.gamemap.MainMap;
import game.gamemap.MapLoader;
import game.ui.CustomLogger;
import game.ui.InputScanner;

import java.io.Serializable;
import java.util.Scanner;

public class NewMapState implements MenuState, Serializable {
    private MenuContext context;

    public NewMapState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln("Новая игра -> Выбор карты -> Создать новую карту");
        CustomLogger.outln("Введите карту построчно (# - пустота, + - дорога):");
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        MainMap map = new MainMap(Game.MAP_WIDTH, Game.MAP_HEIGHT);
        for (int y = 0; y < Game.MAP_HEIGHT; y++) {
            CustomLogger.out(String.format("Строка %d: ", y));
            String line = scanner.nextLine();
            if (line.length() != Game.MAP_HEIGHT) {
                CustomLogger.error("Введенных символов недостаточно. Повторите Ввод");
                y--;
                continue;
            }
            for (int x = 0; x < Game.MAP_WIDTH; x++) {
                char symbol = line.charAt(x);
                Cell cell = map.getCell(y, x);
                switch (symbol) {
                    case '#':
                        cell.setType(Cell.EMPTY);
                        break;
                    case '+':
                        cell.setType(Cell.ROAD);
                        break;
                    default:
                        cell.setType(Cell.EMPTY);
                        break;
                }
            }
        }
        MapLoader.saveMap(context.getString("mapName") + ".txt", map);
    }
}