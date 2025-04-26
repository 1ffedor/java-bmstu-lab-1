package game.ui.player.manage;

import game.gamemap.MapLoader;
import game.gamemap.cells.CellType;
import game.gamemap.cells.CellTypeLoader;
import game.players.Player;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.MainMenuState;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;
import game.ui.player.control.ControlCellTypeState;
import game.ui.player.control.ControlMapState;
import game.ui.player.creating.NewCellSymbolState;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

public class ManageCellsTypesState implements MenuState, Serializable {
    private MenuContext context;

    public ManageCellsTypesState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln(String.format("Основное меню -> Редактор клеток"));
        CustomLogger.outln("0: Назад в меню");
        CustomLogger.outln("1: Новый тип клетки");
        int ind = 2;
        try {
            for (CellType cellType : CellTypeLoader.loadCellTypesFromXml()) {
                if (!cellType.isCastle()) {
                    CustomLogger.outln(ind + ": " + cellType.getSymbol() + " - " + cellType.getDescription());
                    ind++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
//        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            if (choice == 0) {
                context.setState(new MainMenuState(context, context.get(Player.class)));
                return;
            }
            if (choice == 1) {  // новый тип клетки
                context.setState(new NewCellSymbolState(context));
                return;
            }
            try {
                if (1 < choice && choice <= CellTypeLoader.loadCellTypesFromXml().size() + 2) {
                    // choice -1
                    CustomLogger.info("Выбрана клетка: " + CellTypeLoader.loadCellTypesFromXml().get(choice - 2).getSymbol());
                    CellType cellType = CellTypeLoader.loadCellTypesFromXml().get(choice - 2);
                    char cellSymbol = cellType.getSymbol();
                    context.addToStorage("cellType", cellSymbol);
                    context.setState(new ControlCellTypeState(context));
                    return;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            CustomLogger.warn("Неверный выбор. Попробуйте снова.");
        }
        else {
            CustomLogger.warn("Введите число!");
            scanner.next(); // Пропускаем некорректный ввод
        }
    }
}