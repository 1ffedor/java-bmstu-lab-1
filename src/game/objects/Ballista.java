package game.objects;

import game.objects.stationary.Castle;
import game.players.Player;
import game.ui.CustomLogger;

import static java.lang.Math.min;

public class Ballista extends MapObject implements Attacking, Buying {

    public static final String name = "\uD83D\uDE80Баллиста";
    public static final String symbol = "\uD83D\uDE80";
    public static final int cost = 15;
    public static final int defHp = 15;
    public static final int defDamage = 5;
    public static final int typeId = 4;
    private static final boolean isHero = false;

    // класс баллисты
    public Ballista(Player player) {
        super(name, symbol, defHp, player);
    }

    @Override
    public void attack(MapObject enemyObject, Player player, int damage) {
        // атака
        if (enemyObject instanceof Castle && !canAttackCastle()) {
            CustomLogger.warn(String.format("%s не может атаковать замок!", getName()));
            return;
        }
        player.updateBalance(min(damage, enemyObject.getHp()));  // увеличиваем баланс
        enemyObject.changeHp(damage);  // наносим дамаг
        if (!enemyObject.isALive()) {
            // враг мёртв
            player.map.removeMapObject(enemyObject);  // удаляем вражеский объект
            CustomLogger.outln(String.format("Успешная атака! %s -> %s | Объект %s уничтожен!",
                    getName(), enemyObject.getName(), enemyObject.getName()));
            player.finishStep();
            return;
        }
        CustomLogger.outln(String.format("Успешная атака! %s -> %s | Нанесено урона: %s. | Осталось жизней: %s",
                getName(), enemyObject.getName(), damage, enemyObject.getHp()));
        player.finishStep();
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
}
