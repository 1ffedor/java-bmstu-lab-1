package game.objects.heroes;

import game.objects.Attacking;
import game.objects.Buying;
import game.objects.MapObject;
import game.objects.Moving;
import game.players.Player;

public class Knight extends MapObject implements Moving, Attacking, Buying {

    public static final String name = "\uD83D\uDE08Рыцарь";
    public static final String symbol = "\uD83D\uDE08";
    public static final int cost = 40;
    public static final int defHp = 40;
    public static final int defDamage = 20;
    public static final int defEnergy = 8;
    public static final int typeId = 1;
    private int energy;

    public Knight(Player player) {
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
