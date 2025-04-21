package game.ui.player.manage;

import game.objects.MapObject;
import game.players.Player;
import game.ui.CustomLogger;
import game.ui.player.MainMenuState;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;
import game.ui.player.control.ControlMovingObjectState;

import java.util.Scanner;

public class ManageArmyState implements MenuState {
    private final Player player;
    private MenuContext context;

    public ManageArmyState(MenuContext context, Player player) {
        this.context = context;
        this.player = player;
    }

    @Override
    public void display() {
        player.map.display();
        CustomLogger.outln("Основное меню -> Управление армией");
        CustomLogger.outln("0: Назад в меню");
        int ind = 1;
        for (MapObject movingObject : player.getMovingMapObjects()) {
            CustomLogger.outln(String.format("%s: %s %s", ind, movingObject.getName(),
                    player.map.getCellFromMapObject(movingObject).getStringPosition()));
            ind++;
        }
    }

    @Override
    public void handleInput() {
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            if (choice == 0) {
                context.setState(new MainMenuState(context, player));
                return;
            }
            if (choice <= player.getMovingMapObjects().size()) {
                context.setState(new ControlMovingObjectState(
                        context, player, player.getMovingMapObjects().get(choice - 1)));
                return;
            }
            CustomLogger.warn("Неверный вариант!");
        }
        else {
            CustomLogger.warn("Введите число!");
            scanner.next(); // Пропускаем некорректный ввод
        }
    }
}