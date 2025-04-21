package game.objects.heroes;

import game.objects.Attacking;
import game.objects.Buying;
import game.objects.MapObject;
import game.objects.Moving;
import game.players.Player;

public class Gollum extends MapObject implements Moving, Attacking, Buying {

    public static final String name = "\uD83D\uDC79Голем";
    private static final String symbol = "\uD83D\uDC79";
    public static final int cost = 60;
    public static final int defHp = 60;
    private static final int defDamage = 25;
    private static final int defEnergy = 12;
    public static final int typeId = 2;
    private int energy;


    public Gollum(Player player) {
        super(name, symbol, defHp, player);
        this.energy = defEnergy;
    }

    @Override
    public boolean canAttackCastle() {
        return true;
    }

    @Override
    public String getName() {
        return name;
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
