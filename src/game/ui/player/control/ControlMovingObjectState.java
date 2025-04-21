package game.ui.player.control;

import game.objects.Attacking;
import game.objects.MapObject;
import game.objects.Moving;
import game.players.Player;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.manage.ManageArmyState;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;

import java.util.Scanner;

public class ControlMovingObjectState implements MenuState {
    private final Player player;
    private final MapObject movingObject;
    private MenuContext context;

    public ControlMovingObjectState(MenuContext context, Player player, MapObject movingObject) {
        this.context = context;
        this.player = player;
        this.movingObject = movingObject;
    }

    @Override
    public void display() {
        player.map.display();
        CustomLogger.outln(String.format("Основное меню -> Управление армией -> %s %s\n" +
                "Здоровье: %s;     Энергия: %s;    Сила: %s", movingObject.getName(),
                player.map.getCellFromMapObject(movingObject).getStringPosition(),
                movingObject.getHp(), ((Moving) movingObject).getEnergy(), ((Attacking) movingObject).getDamage()));
        CustomLogger.outln("1: ↖️    2: ⬆️    3: ↗️    \n" +
                "4: ⬅️          ️    6: ➡️    \n" +
                "7: ↙️    8: ⬇️    9: ↘️    \n" +
                "5: завершить ход   0: назад в меню");
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    context.setState(new ManageArmyState(context, player));
                    return;
                case 5:
                    player.finishAttack();  // ?
                    context.finish();
                    return;
                case 1:
                    player.map.moveObject(movingObject, -1, -1);
                    return;
                case 2:
                    player.map.moveObject(movingObject, 0, -1);
                    return;
                case 3:
                    player.map.moveObject(movingObject, 1, -1);
                    return;
                case 4:
                    player.map.moveObject(movingObject, -1, 0);
                    return;
                case 6:
                    player.map.moveObject(movingObject, 1, 0);
                    return;
                case 7:
                    player.map.moveObject(movingObject, -1, 1);
                    return;
                case 8:
                    player.map.moveObject(movingObject, 0, 1);
                    return;
                case 9:
                    player.map.moveObject(movingObject, 1, 1);
                    return;
                default:
                    CustomLogger.warn("Неверный вариант!");
            }
        }
        else {
            CustomLogger.warn("Введите число!");
            scanner.next(); // Пропускаем некорректный ввод
        }
    }
}