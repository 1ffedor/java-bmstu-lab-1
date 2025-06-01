package game.ui.player.control;

import game.players.Player;
import game.shops.Shop;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.ShopStoreState;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;

import java.util.Scanner;

public class ShopServiceReviewState implements MenuState {
    private final Player player;
    private final Shop shop;
    private MenuContext context;
    private String serviceName;

    public ShopServiceReviewState(MenuContext context, Player player, Shop shop, String serviceName) {
        this.context = context;
        this.player = player;
        this.shop = shop;
        this.serviceName = serviceName;
    }

    @Override
    public void display() {
        player.map.display();
        CustomLogger.outln(String.format(
                "Основное меню -> Управление зданиями с услугами -> %s -> %s\nОставьте отзыв об услуге (число 1-5):",
                shop.getName(), serviceName));
        CustomLogger.outln("0: Назад в меню");
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    context.setState(new ControlShopServiceState(context, player, shop, serviceName));
                    return;
            }
            if (choice > 5) {
                CustomLogger.warn("Введите число от 1 до 5!");
            }
            else {
                shop.updateServiceRating(player.getName(), serviceName, choice);
                context.setState(new ControlShopServiceState(context, player, shop, serviceName));
                return;
            }
        }
        else {
            CustomLogger.warn("Введите число!");
            scanner.next(); // Пропускаем некорректный ввод
        }
    }
}