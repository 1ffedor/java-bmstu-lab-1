package game.ui.player.control;

import game.objects.Ballista;
import game.objects.MapObject;
import game.players.Player;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;

import java.util.Scanner;


public class ControlBallistaState implements MenuState {
    private final Player player;
    private final Ballista ballista;
    private MenuContext context;

    public ControlBallistaState(MenuContext context, Player player, Ballista ballista) {
        this.context = context;
        this.player = player;
        this.ballista = ballista;
    }

    @Override
    public void display() {
        player.map.display();
        CustomLogger.outln(String.format("Пост атака -> Управление Баллистой -> %s %s\n" +
                        "Здоровье: %s;     Сила: %s", ballista.getName(),
                player.map.getCellFromMapObject(ballista).getStringPosition(),
                ballista.getHp(), ballista.getDamage()));
        StringBuilder enemies = new StringBuilder();
        CustomLogger.outln("0: Завершить ход");
        int index = 1;
        for (MapObject enemyAttackingObject : player.getEnemyPlayerAttackingObjects()) {
            // идём по объектам врага
            CustomLogger.outln(String.format("%s: %s %s", index, enemyAttackingObject.getName(),
                    player.map.getCellFromMapObject(enemyAttackingObject).getStringPosition()));
            index++;
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
                context.finish();
                return;
            }
            if (choice <= player.getEnemyPlayerAttackingObjects().size()) {
                ballista.attack(player.getEnemyPlayerAttackingObjects().get(choice - 1), player, ballista.getDamage());
                context.finish();
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