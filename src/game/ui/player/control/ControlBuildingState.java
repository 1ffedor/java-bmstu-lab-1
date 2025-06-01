package game.ui.player.control;

import game.buildings.Building;
import game.players.Player;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.manage.ManageBuildingsState;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;

import java.util.Scanner;

public class ControlBuildingState implements MenuState {
    private final Player player;
    private final Building building;
    private MenuContext context;

    public ControlBuildingState(MenuContext context, Player player, Building building) {
        this.context = context;
        this.player = player;
        this.building = building;
    }

    @Override
    public void display() {
        player.map.display();
        CustomLogger.outln(String.format(
                "Основное меню -> Управление зданиями -> %s | Баланс: %s\nВыберите действие:",
                building.getName(), player.getBalanceString()));
        CustomLogger.outln("0: Назад в меню");
        CustomLogger.outln(building.getOptions());
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            if (choice == 0) {
                context.setState(new ManageBuildingsState(context, player));
                return;
            }
            building.handleChoice(player, choice);
        }
        else {
            CustomLogger.warn("Введите число!");
            scanner.next(); // Пропускаем некорректный ввод
        }
    }
}