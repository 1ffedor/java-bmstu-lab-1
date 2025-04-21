package game.ui.player;

import game.Game;
import game.ui.CustomLogger;
import game.ui.InputScanner;
import saves.FIleExecutor;

import java.io.Serializable;
import java.util.Scanner;

public class GetPlayerNameState implements MenuState, Serializable {
    private MenuContext context;

    public GetPlayerNameState(MenuContext context) {
        this.context = context;
    }

    @Override
    public void display() {
        CustomLogger.outln("Добро пожаловать в игру.");
        CustomLogger.out("Введите ваше имя: ");
    }

    @Override
    public void handleInput() {
        InputScanner inputScanner = new InputScanner(System.in);
        Scanner scanner = inputScanner.getScanner();
        if (true) {   // scanner.hasNext()  or scanner.hasString (паттерн имени)
            String name = scanner.next();
            // создаем файлик с именем игрока
//            FIleExecutor.createFile("src/saves/" + name + ".json");
            context.addToStorage("playerName", name);  // сохраняем имя игрока
            context.setState(new GameLoadState(context));
            CustomLogger.outln("");
            return;
        }
        else {
            CustomLogger.warn("Неверное имя!");
        }
    }
}