package game.ui.player.manage;

import game.gamemap.MapLoader;
import game.players.Player;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.MainMenuState;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;
import game.ui.player.control.ControlMapState;

import java.io.Serializable;
import java.util.Scanner;

public class ManageMapsState implements MenuState, Serializable {
    private MenuContext context;

    public ManageMapsState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln(String.format("Основное меню -> Управление картами"));
        CustomLogger.outln("0: Назад в меню");
        int ind = 1;
        for (String mapName : MapLoader.getAllMaps()) {
            CustomLogger.outln(ind + ": " + mapName);
            ind++;
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
            if (0 < choice && choice <= MapLoader.getAllMaps().size()) {
                // choice -1
                CustomLogger.info("Выбрана карта: " + MapLoader.getAllMaps().get(choice - 1));
                String mapName = MapLoader.getAllMaps().get(choice - 1);
                context.addToStorage("mapName", mapName);
                context.setState(new ControlMapState(context));
                return;
            }
            CustomLogger.warn("Неверный выбор. Попробуйте снова.");
        }
        else {
            CustomLogger.warn("Введите число!");
            scanner.next(); // Пропускаем некорректный ввод
        }
    }
}