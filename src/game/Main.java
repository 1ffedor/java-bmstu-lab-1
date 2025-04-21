package game;

import game.ui.CustomLogger;
import game.ui.InputScanner;

public class Main {
    public static void main(String[] args) {
        InputScanner inputScanner = new InputScanner(System.in);

        Game game = new GameLoader().initGame();
        try {
            game.startGame();
        } catch (NullPointerException e) {
            CustomLogger.outln(e.getMessage());
            // не получили экземпляр игры
            return;
        }
    }
}