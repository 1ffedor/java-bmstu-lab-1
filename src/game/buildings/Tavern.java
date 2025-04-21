package game.buildings;

import game.objects.heroes.Gollum;
import game.objects.heroes.Knight;
import game.players.Player;
import game.ui.CustomLogger;

public class Tavern extends Building {
    // таверна
    public static final String name = "Таверна";
    public static final int cost = 50;
    private static final int id = 1;

    public Tavern() {
        super(id, name, cost);
    }

    @Override
    public String getOptions() {
        return String.format("1: %s \uD83E\uDE99%d\n" +
                "2: %s \uD83E\uDE99%d", Knight.name, Knight.cost, Gollum.name, Gollum.cost);
    }

    @Override
    public void handleChoice(Player player, int choice) {
        switch (choice) {
            case 1:
                player.buyObject(Knight.cost, Knight.typeId);
                return;
            case 2:
                player.buyObject(Gollum.cost, Gollum.typeId);
                return;
            default:
                CustomLogger.warn("Неверный вариант!");
        }
    }
}
