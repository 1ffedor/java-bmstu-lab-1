package game.players;

import game.Game;
import game.buildings.Tavern;
import game.gamemap.IntPair;
import game.objects.Attacking;
import game.objects.MapObject;
import game.objects.heroes.Gollum;
import game.objects.heroes.Knight;
import game.objects.stationary.PlayerCastle;
import game.gamemap.MainMap;
import game.objects.units.Ghost;
import game.objects.units.Soldier;
import utils.LogConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

import static game.Game.MAP_HEIGHT;
import static game.Game.MAP_WIDTH;

public class ComputerPlayer extends Player {
    private static final Logger LOGGER =
            LogConfig.getLogger(Game.class, "computerPlayer", Level.WARNING);  // инит логера

    public String zoneSymbol = "\uD83D\uDFE8";

    public ComputerPlayer(String name, int balance, MainMap map) {
        super(name, balance,  1, map);
        initCastle();
    }

    @Override
    protected void initCastle() {
        castle = new PlayerCastle(this);
        map.setMapObject(castle, MAP_WIDTH - 1, MAP_HEIGHT - 1);
    }

    @Override
    public String getZoneSymbol() {
        return zoneSymbol;
    }

    @Override
    public void postAttack() {
        finishStep();
    }

    @Override
    public void action() {
        // действие компьютера
        // есть возможность атаковать - атакует
        // всё время перемещается к замку
        // герой сидит в замке до последнего
        LOGGER.log(Level.WARNING, "Начало хода");
        if (!isHaveBuilding(new Tavern())) {
            buyBuilding(new Tavern());
        }
        buyMapObjects();  // покупка героев до талого
        attackNeighbors();  // атакуем соседей
        moveToOppositeCastle();  // перемещаемся к замку врага
    }

    public void buyMapObjects() {
        // покупка героев до талого
        LOGGER.log(Level.WARNING, "Покупка героев");
        while (balance - Soldier.cost > 0) {  // - Knight.cost
            if (balance - Gollum.cost > 0) {
                buyObject(Gollum.cost, Gollum.typeId);
            }
            if (balance - Knight.cost > 0) {
                buyObject(Knight.cost, Knight.typeId);
            }
            if (balance - Ghost.cost > 0) {
                buyObject(Ghost.cost, Ghost.typeId);
            }
            if (balance - Soldier.cost > 0) {
                buyObject(Soldier.cost, Soldier.typeId);
            }
        }
    }

    public void attackNeighbors() {
        // атаковать соседей
        LOGGER.log(Level.WARNING, "Атака соседей");
        for (MapObject movingObject : getMovingMapObjects()) {
            // получаем всех соседей героя
            for (MapObject enemyObject : map.getMapObjectNeighbours(movingObject)) {
                // проверим на то, что это не наш объект
                if (!enemyObject.getPlayer().equals(this)) {
                    ((Attacking) movingObject).attack(enemyObject, movingObject.getPlayer(),
                            ((Attacking) movingObject).getDamage());
                    return;
                }
            }
        }
    }

    public void moveToOppositeCastle() {
        // двигаться к замку противника
//        Hero nearestHero = map.getNearest();  // ближайший к замку герой
        LOGGER.log(Level.WARNING, "Движение к замку противника");
        while (isActiveStep())
            for (MapObject movingObject : getMovingMapObjects()) {
            {
                if (!isActiveStep()) {
                     return ;
                }
                IntPair pair = map.getCellFromMapObject(movingObject).getDiagonalCoordinates();  // ближайшая диагональная клетка
                if (map.getMapObjectByCoords(pair.row(), pair.col()) == null) {
                    map.moveObjectToCell(movingObject, map.getCell(pair.row(), pair.col()));  // перемещаем героя
                }
                attackNeighbors();
            }
        }
    }
}
