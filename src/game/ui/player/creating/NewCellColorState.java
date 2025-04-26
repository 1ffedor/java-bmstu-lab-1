package game.ui.player.creating;

import game.gamemap.cells.CellType;
import game.gamemap.cells.CellTypeLoader;
import game.ui.CustomLogger;
import game.ui.InputScanner;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;

import java.io.Serializable;
import java.util.Scanner;

public class NewCellColorState implements MenuState, Serializable {
    private MenuContext context;

    public NewCellColorState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln("Редактор клеток -> Новый тип клетки");
        CustomLogger.out("Введите цвет (эмодзи) для нового типа клетки:");
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        String color = scanner.nextLine();
        try {
            CellType cellType = CellTypeLoader.loadCellTypesFromXml().stream()
                    .filter(c -> c.getColor() == color)
                    .findFirst()
                    .orElse(null);
            if (cellType == null) {
                context.addToStorage("cellTypeColor", color);
                context.setState(new NewCellPenaltyState(context));
                CustomLogger.outln("");
                return;
            }
            CustomLogger.error("Клетка с таким эмодзи уже есть!");
        } catch (Exception e) {
            CustomLogger.error("Ошибка при создании клетки");
        }
    }
}