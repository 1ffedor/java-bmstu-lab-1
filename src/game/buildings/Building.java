package game.buildings;


import game.players.Player;

import java.io.Serializable;

public abstract class Building implements Serializable {
    private final String name;
    private final int cost;
    private int id;

    public Building(int id, String name, int cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public int getId() {
        // получить id объекта
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCostString() {
        return String.format("\uD83E\uDE99%d", cost);
    }

    public int getCost() {
        return cost;
    }

    public abstract String getOptions();

    public abstract void handleChoice(Player player, int choice);
}
