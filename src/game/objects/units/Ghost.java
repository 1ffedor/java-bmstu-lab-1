package game.objects.units;

import game.objects.Attacking;
import game.objects.Buying;
import game.objects.MapObject;
import game.objects.Moving;
import game.players.Player;

public class Ghost extends MapObject implements Attacking, Moving, Buying {

    public static final String name = "\uD83D\uDC7BПризрак";
    public static final String symbol = "\uD83D\uDC7B";
    public static final int cost = 15;
    public static final int defHp = 15;
    public static final int defDamage = 1500;
    public static final int defEnergy = 400;
    public static final int typeId = 5;
    private int energy;

    // класс солдата
    public Ghost(Player player) {
        super(name, symbol, defHp, player);
        this.energy = defEnergy;
    }

    @Override
    public boolean canAttackCastle() {
        return false;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public int getDamage() {
        return defDamage;
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    public int getDefEnergy() {
        return defEnergy;
    }
}
