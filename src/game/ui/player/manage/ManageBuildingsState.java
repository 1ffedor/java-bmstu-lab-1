package game.ui.player.manage;

import game.buildings.Building;
import game.players.Player;
import game.ui.CustomLogger;
import game.ui.player.MainMenuState;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;
import game.ui.player.control.ControlBuildingState;

import java.util.Scanner;

public class ManageBuildingsState implements MenuState {
    private final Player player;
    private MenuContext context;

    public ManageBuildingsState(MenuContext context, Player player) {
        this.context = context;
        this.player = player;
    }

    @Override
    public void display() {
        player.map.display();
        CustomLogger.outln("Основное меню -> Управление зданиями");
        CustomLogger.outln("0: Назад в меню");
        int ind = 1;
        for (Building building : player.getBuildings()) {
            CustomLogger.outln(String.format("%s: %s", ind, building.getName()));
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
            if (choice <= player.getBuildings().size()) {
                context.setState(new ControlBuildingState(
                        context, player, player.getBuildings().get(choice - 1)));
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