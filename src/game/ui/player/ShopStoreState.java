package game.ui.player;

import game.buildings.Building;
import game.players.Npc;
import game.players.Player;
import game.shops.*;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.control.ControlShopState;

import java.util.Random;
import java.util.Scanner;

public class ShopStoreState implements MenuState {
    private final Player player;
    private MenuContext context;

    public ShopStoreState(MenuContext context, Player player) {
        this.context = context;
        this.player = player;
    }

    @Override
    public void display() {
        player.map.display();
        CustomLogger.outln(String.format("Основное меню -> Здания с услугами"));
        CustomLogger.outln("0: Назад в меню");
        CustomLogger.outln(String.format("1: %s %s", Cafe.NAME, "(увеличение энергии)"));
        CustomLogger.outln(String.format("2: %s %s", Hotel.NAME, "(увеличение здоровья)"));
        CustomLogger.outln(String.format("3: %s %s", Barbershop.NAME, "(уменьшение HP замка врага)"));
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
                    context.setState(new ControlShopState(context, player, (Shop) context.get("cafe")));
                    return;
                case 2:
                    context.setState(new ControlShopState(context, player, (Shop) context.get("hotel")));
                    return;
                case 3:
                    context.setState(new ControlShopState(context, player, (Shop) context.get("barbershop")));
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