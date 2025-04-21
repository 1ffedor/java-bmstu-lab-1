package game.ui.player.store;

import game.buildings.Forge;
import game.buildings.Inferno;
import game.buildings.Tavern;
import game.players.Player;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.MainMenuState;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;

import java.util.Scanner;

public class BuildingsStoreState implements MenuState {
    private final Player player;
    private MenuContext context;

    public BuildingsStoreState(MenuContext context, Player player) {
        this.context = context;
        this.player = player;
    }

    @Override
    public void display() {
        player.map.display();
        CustomLogger.outln(String.format("Основное меню -> Покупка здания | Баланс: %s", player.getBalanceString()));
        CustomLogger.outln("0: Назад в меню");
        CustomLogger.outln(String.format("1: %s \uD83E\uDE99%s | %s", Tavern.name, Tavern.cost, "(покупка Героев)"));
        CustomLogger.outln(String.format("2: %s \uD83E\uDE99%s | %s", Inferno.name, Inferno.cost, "(покупка Юнитов)"));
        CustomLogger.outln(String.format("3: %s \uD83E\uDE99%S | %s", Forge.name, Forge.cost, "(покупка Баллисты)"));
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    context.setState(new MainMenuState(context, player));
                    return;
                case 1:
                    player.buyBuilding(new Tavern());
                    return;
                case 2:
                    player.buyBuilding(new Inferno());
                    return;
                case 3:
                    player.buyBuilding(new Forge());
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