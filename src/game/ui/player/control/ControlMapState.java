package game.ui.player.control;

import game.gamemap.MapLoader;
import game.players.Player;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;
import game.ui.player.manage.ManageMapsState;

import java.io.Serializable;
import java.util.Scanner;

public class ControlMapState implements MenuState, Serializable {
    private MenuContext context;
    private Player player;

    public ControlMapState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln(String.format("Основное меню -> Управление картами -> %s", context.get("mapName")));
        CustomLogger.outln("0: Назад в меню");
        CustomLogger.outln("1: Редактировать карту");
        CustomLogger.outln("2: Удалить карту");
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
                    context.setState(new EditMapState(context));
                    return;
                case 2:
                    boolean result = MapLoader.deleteMap(MapLoader.getAllMaps().get(choice - 1));
                    if (result) {
                        context.setState(new ManageMapsState(context));
                        return;
                    }
                    CustomLogger.outln("Ошибка при удалении карты");
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