package game.ui.player.control;

import game.players.Player;
import game.shops.Shop;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.ShopStoreState;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;

import java.util.Map;
import java.util.Scanner;

public class ControlShopState implements MenuState {
    private final Player player;
    private final Shop shop;
    private MenuContext context;

    public ControlShopState(MenuContext context, Player player, Shop shop) {
        this.context = context;
        this.player = player;
        this.shop = shop;
    }

    @Override
    public void display() {
        player.map.display();
        CustomLogger.outln(String.format(
                "Основное меню -> Управление зданиями с услугами -> %s \nВыберите действие:",
                shop.getName()));
        CustomLogger.outln("-1: Посмотреть статус");
        CustomLogger.outln("0: Назад в меню");
        for (Map.Entry<String, Integer> entry : shop.getOptionsByRating()) {
            CustomLogger.outln(String.format("%s (рейтинг: %s)", entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            if (choice == -1) {
                CustomLogger.outln(shop.getStatus());
                return;
            }
            if (choice == 0) {
                context.setState(new ShopStoreState(context, player));
                return;
            }
        }
        else {
            String serviceName = scanner.nextLine();
            context.setState(new ControlShopServiceState(context, player, shop, serviceName));
//            shop.handleChoice(player, choice);

//            CustomLogger.warn("Введите число!");
//            scanner.next(); // Пропускаем некорректный ввод
        }
    }
}