package game.ui.player.creating;

import game.gamemap.cells.CellType;
import game.gamemap.cells.CellTypeLoader;
import game.ui.CustomLogger;
import game.ui.InputScanner;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;

import java.io.Serializable;
import java.util.Scanner;

public class NewCellSymbolState implements MenuState, Serializable {
    private MenuContext context;

    public NewCellSymbolState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln("Редактор клеток -> Новый тип клетки");
        CustomLogger.out("Введите символ для нового типа клетки:");
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        char symbol = scanner.nextLine().charAt(0);
        try {
            CellType cellType = CellTypeLoader.loadCellTypesFromXml().stream()
                    .filter(c -> c.getSymbol() == symbol)
                    .findFirst()
                    .orElse(null);
            if (cellType == null) {
                context.addToStorage("сellTypeSymbol", symbol);
                context.setState(new NewCellColorState(context));
                CustomLogger.outln("");
                return;
            }
            CustomLogger.error("Клетка с таким символом уже есть!");
        } catch (Exception e) {
            CustomLogger.error("Ошибка при создании клетки");
        }
    }
}