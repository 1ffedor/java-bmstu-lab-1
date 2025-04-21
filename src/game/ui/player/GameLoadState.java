package game.ui.player;

import game.Game;
import game.GameLoader;
import game.ui.CustomLogger;
import game.ui.InputScanner;

import java.util.Scanner;

public class GameLoadState implements MenuState {
    private MenuContext context;

    public GameLoadState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln("Выберите действие");
        CustomLogger.outln("1: Новая игра");  // новая игра -> запрос имени
        CustomLogger.outln("2: Загрузить старую игру");
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
//        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:  // создаем новую игру
                    // просим выбрать карту
                    context.setState(new ChooseMapState(context));
//                    // просто начинаем новую игру
//                    context.finish();
                    return;
                case 2:  // загрузка старой игры
                    String playerName = context.getString("playerName");
                    Game game = GameLoader.loadGame(playerName + ".sav");
                    if (game == null) {
                        CustomLogger.error("Не найдены сохраненные игры.");
                        return;
                    }
                    context.addToStorage(Game.class, game);
                    context.finish();
                    return;
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