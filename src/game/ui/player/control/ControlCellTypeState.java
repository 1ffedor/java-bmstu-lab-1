package game.ui.player.control;

import game.gamemap.MapLoader;
import game.gamemap.cells.CellType;
import game.gamemap.cells.CellTypeLoader;
import game.gamemap.cells.CellTypeSaver;
import game.players.Player;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;
import game.ui.player.manage.ManageCellsTypesState;
import game.ui.player.manage.ManageMapsState;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ControlCellTypeState implements MenuState, Serializable {
    private MenuContext context;

    public ControlCellTypeState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln(String.format("Основное меню -> Управление клетками -> %s", context.get("cellType")));
        CustomLogger.outln("0: Назад в меню");
        CustomLogger.outln("1: Удалить клетку");
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    context.setState(new ManageMapsState(context));
                    return;
                case 1:
                    List<CellType> newCellTypeList = null;
                    try {
                        newCellTypeList = CellTypeLoader.loadCellTypesFromXml()
                                .stream()
                                .filter(c -> c.getSymbol() != (Character) context.get("cellType"))
                                .toList();
                    } catch (Exception e) {
                        CustomLogger.outln("Ошибка при удалении типа клетки");
                    }
                    try {
                        CellTypeSaver.saveCellTypesToXml(newCellTypeList);
                        context.setState(new ManageCellsTypesState(context));
                        CustomLogger.info(String.format("Клетка: '%s' успешно удалена", context.get("cellType")));
                        return;
                    } catch (Exception e) {
                        CustomLogger.outln("Ошибка при удалении типа клетки");
                    }
                    return;
                default:
                    CustomLogger.warn("Неверный выбор. Попробуйте снова.");
            }
        }
        else {
            CustomLogger.warn("Введите число!");
            scanner.next(); // Пропускаем некорректный ввод
        }
    }
}