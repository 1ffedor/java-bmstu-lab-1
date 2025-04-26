package game.ui.player;

import game.Game;
import game.gamemap.MainMap;
import game.gamemap.MapLoader;
import game.ui.CustomLogger;
import game.ui.InputScanner;
import game.ui.player.creating.NewMapNameState;

import java.io.Serializable;
import java.util.Scanner;

public class ChooseMapState implements MenuState, Serializable {
    private MenuContext context;

    public ChooseMapState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln("Новая игра -> Выбор карты");
        CustomLogger.outln("0: Создать новую карту");
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
                // новая карта
                context.setState(new NewMapNameState(context));
                return;
            }
            if (0 < choice && choice <= MapLoader.getAllMaps().size()) {
                // choice -1
                CustomLogger.info("Выбрана карта: " + MapLoader.getAllMaps().get(choice - 1));
                MainMap map = MapLoader.loadMap(MapLoader.getAllMaps().get(choice - 1));
                context.addToStorage(Game.class, new Game(map, context));
                context.finish();
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