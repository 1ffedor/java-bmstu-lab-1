package game.ui.player.creating;

import game.gamemap.cells.CellType;
import game.gamemap.cells.CellTypeLoader;
import game.gamemap.cells.CellTypeSaver;
import game.ui.CustomLogger;
import game.ui.InputScanner;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;
import game.ui.player.manage.ManageCellsTypesState;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

public class NewCellDescriptionState implements MenuState, Serializable {
    private MenuContext context;

    public NewCellDescriptionState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln("Редактор клеток -> Новый тип клетки");
        CustomLogger.out("Введите описание для нового типа клетки:");
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        String description = scanner.nextLine();
        try {
            context.addToStorage("cellTypeDescription", description);
            List<CellType> newCellTypeList = null;
            try {
                newCellTypeList = CellTypeLoader.loadCellTypesFromXml();
                newCellTypeList.add(new CellType(
                        (char) context.get("сellTypeSymbol"),
                        (int) context.get("сellTypePenalty"),
                        (String) context.get("cellTypeColor"),
                        (String) context.get("cellTypeDescription"), false));
                CellTypeSaver.saveCellTypesToXml(newCellTypeList);
                CustomLogger.info(String.format("Клетка: %s успешно создана!", context.get("сellTypeColor")));
                context.setState(new ManageCellsTypesState(context));
            } catch (Exception e) {
                CustomLogger.outln("Ошибка при создании нового типа клетки");
            }
        } catch (Exception e) {
            CustomLogger.error("Ошибка при создании клетки");
        }
    }
}