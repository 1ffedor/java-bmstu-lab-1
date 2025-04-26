package game.ui.player.creating;

import game.gamemap.cells.CellType;
import game.gamemap.cells.CellTypeLoader;
import game.ui.CustomLogger;
import game.ui.InputScanner;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;

import java.io.Serializable;
import java.util.Scanner;

public class NewCellPenaltyState implements MenuState, Serializable {
    private MenuContext context;

    public NewCellPenaltyState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln("Редактор клеток -> Новый тип клетки");
        CustomLogger.out("Введите штраф для нового типа клетки:");
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        int penalty = scanner.nextInt();
        try {
            if (0 <= penalty) {
                context.addToStorage("сellTypePenalty", penalty);
                context.setState(new NewCellDescriptionState(context));
                CustomLogger.outln("");
                return;
            }
            CustomLogger.error("Штраф не может быть отрицательным!");
        } catch (Exception e) {
            CustomLogger.error("Ошибка при создании клетки");
        }
    }
}