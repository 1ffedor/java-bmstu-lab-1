package game.ui.player.control;

import game.players.Player;
import game.shops.Shop;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.ShopStoreState;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;

import java.util.Scanner;

public class ControlShopServiceState implements MenuState {
    private final Player player;
    private final Shop shop;
    private final String serviceName;
    private MenuContext context;

    public ControlShopServiceState(MenuContext context, Player player, Shop shop, String serviceName) {
        this.context = context;
        this.player = player;
        this.shop = shop;
        this.serviceName = serviceName;
    }

    @Override
    public void display() {
        player.map.display();
        CustomLogger.outln(String.format(
                "Основное меню -> Управление зданиями с услугами -> %s -> %s\nВыберите действие:",
                shop.getName(), serviceName));
        CustomLogger.outln("0: Назад в меню");
        CustomLogger.outln("1: Воспользоваться услугой");
        CustomLogger.outln("2: Оставить отзыв об услуге ");
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    context.setState(new ControlShopState(context, player, shop));
                    return;
                case 1:
                    shop.handleChoiceByServiceName(player, serviceName);
                    return;
                case 2:
                    context.setState(new ShopServiceReviewState(context, player, shop, serviceName));
                    return;
            }
        }
        else {
            CustomLogger.warn("Введите число!");
            scanner.next(); // Пропускаем некорректный ввод
        }
    }
}