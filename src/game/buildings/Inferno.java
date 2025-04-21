package game.buildings;

import game.objects.units.Ghost;
import game.objects.units.Soldier;
import game.players.Player;
import game.ui.CustomLogger;

public class Inferno extends Building {
    // таверна
    public static final String name = "Инферно";
    public static final int cost = 10;
    private static final int id = 2;

    public Inferno() {
        super(id, name, cost);
    }

    @Override
    public String getOptions() {
        return String.format("1: %s \uD83E\uDE99%d\n" +
                "2: %s \uD83E\uDE99%d", Soldier.name, Soldier.cost, Ghost.name, Ghost.cost);
    }

    @Override
    public void handleChoice(Player player, int choice) {
        switch (choice) {
            case 1:
                player.buyObject(Soldier.cost, Soldier.typeId);
                return;
            case 2:
                player.buyObject(Ghost.cost, Ghost.typeId);
                return;
            default:
                CustomLogger.warn("Неверный вариант!");
        }
    }
}
