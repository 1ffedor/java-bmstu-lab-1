package game.ui.player;

import game.ui.CustomLogger;

public interface MenuState {
    void display();
    void handleInput();

    default void onInterrupt() {
        // Базовая реализация обработки прерывания
        CustomLogger.warn("Действие прервано по таймауту");
    }
}