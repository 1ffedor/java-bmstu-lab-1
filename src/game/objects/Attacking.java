package game.objects;

import game.objects.stationary.Castle;
import game.players.Player;
import game.ui.CustomLogger;

import static java.lang.Math.min;

public interface Attacking {

    boolean canAttackCastle();

    String getName();

    int getCost();

    int getDamage();

    default String getCostString() {
        return String.format("\uD83E\uDE99%d", getCost());
    }

    default void attack(MapObject enemyObject, Player player, int damage) {
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
            player.finishAttack();
            return;
        }
        CustomLogger.outln(String.format("Успешная атака! %s -> %s | Нанесено урона: %s. | Осталось жизней: %s",
                getName(), enemyObject.getName(), damage, enemyObject.getHp()));
        player.finishAttack();
    }
}
