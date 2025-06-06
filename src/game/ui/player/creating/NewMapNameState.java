package game.ui.player.creating;

import game.ui.CustomLogger;
import game.ui.InputScanner;
import game.ui.player.MenuContext;
import game.ui.player.MenuState;

import java.io.Serializable;
import java.util.Scanner;

public class NewMapNameState implements MenuState, Serializable {
    private MenuContext context;

    public NewMapNameState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln("Новая игра -> Выбор карты -> Создать новую карту");
        CustomLogger.out("Введите название карты:");
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        String name = scanner.nextLine();
        context.addToStorage("mapName", name);
        context.setState(new NewMapState(context));
        CustomLogger.outln("");
    }
}