package game.ui.player;

import game.Game;
import game.GameLoader;
import game.players.Player;
import game.ui.InputScanner;
import game.ui.CustomLogger;
import game.ui.player.manage.ManageArmyState;
import game.ui.player.manage.ManageBuildingsState;
import game.ui.player.manage.ManageMapsState;
import game.ui.player.store.BuildingsStoreState;

import java.io.Serializable;
import java.util.Scanner;

public class MainMenuState implements MenuState, Serializable {
    private MenuContext context;
    private Player player;

    public MainMenuState(MenuContext context, Player player) {
        this.context = context;
        this.player = player;
    }

    @Override
    public void display() {
        player.map.display();
        CustomLogger.outln(String.format("Основное меню | Баланс: %s", player.getBalanceString()));
        CustomLogger.outln("0: Завершить ход");
        CustomLogger.outln("1: Управлять армией");
        CustomLogger.outln("2: Управлять зданиями");
        CustomLogger.outln("3: Купить здание");
        CustomLogger.outln("4: Сохранить игру");
        CustomLogger.outln("5: Управление картами");
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
//        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    context.finish();
                    break;
                case 1:
                    context.setState(new ManageArmyState(context, player));
                    break;
                case 2:
                    context.setState(new ManageBuildingsState(context, player));
                    break;
                case 3:
                    context.setState(new BuildingsStoreState(context, player));
                    break;
                case 4:
                    Game game = context.get(Game.class);
                    GameLoader.saveGame(context.get(Game.class), player.getName() + ".sav");
                    break;
                case 5:
                    // редактор карт
                    context.setState(new ManageMapsState(context));
                    break;
                default:
                    CustomLogger.warn("Неверный выбор. Попробуйте снова.");
            }
        }
        else {
            CustomLogger.warn("Введите число!");
            scanner.next(); // Пропускаем некорректный ввод
        }
    }
}