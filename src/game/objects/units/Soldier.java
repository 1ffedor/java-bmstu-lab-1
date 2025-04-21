package game.objects.units;

import game.objects.Attacking;
import game.objects.Buying;
import game.objects.MapObject;
import game.objects.Moving;
import game.players.Player;

public class Soldier extends MapObject implements Attacking, Moving, Buying {

    public static final String name = "\uD83D\uDE42Солдат";
    public static final String symbol = "\uD83D\uDE42";
    public static final int cost = 10;
    public static final int defHp = 10;
    public static final int defDamage = 5;
    public static final int defEnergy = 4;
    public static final int typeId = 3;
    private int energy;


    // класс солдата
    public Soldier(Player player) {
        super(name, symbol, defHp, player);
        energy = defEnergy;
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
