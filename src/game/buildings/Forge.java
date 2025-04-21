package game.buildings;

import game.objects.Ballista;
import game.players.Player;
import game.ui.CustomLogger;

public class Forge extends Building {
    // кузница
    public static final String name = "Кузница";
    public static final int cost = 20;
    private static final int id = 3;

    public Forge() {
        super(id, name, cost);
    }

    @Override
    public String getOptions() {
        return String.format("1: %s \uD83E\uDE99%d", Ballista.name, Ballista.cost);
    }

    @Override
    public void handleChoice(Player player, int choice) {
        switch (choice) {
            case 1:
                player.buyObject(Ballista.cost, Ballista.typeId);
                return;
            default:
                CustomLogger.warn("Неверный вариант!");
        }
    }
}
