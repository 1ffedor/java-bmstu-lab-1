package game.players;

import game.buildings.Building;
import game.gamemap.IntPair;
import game.objects.*;
import game.objects.heroes.Gollum;
import game.objects.heroes.Knight;
import game.objects.units.Ghost;
import game.objects.units.Soldier;
import game.ui.CustomLogger;
import game.gamemap.cells.Cell;
import game.gamemap.MainMap;
import game.objects.stationary.Castle;
import game.ui.player.MenuContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.util.function.Supplier;

public abstract class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int balance;
    protected int status;
    protected ArrayList<Building> buildings;  // здания игрока
    protected MenuContext context;

    public String name;
    public Castle castle;
    public MainMap map;
    public int num;


    public Player(String name, int balance, int num, MainMap map) {
        this.name = name;
        this.balance = balance;
        this.map = map;
        this.num = num;
        this.buildings = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public String getBalanceString() {
        return String.format("\uD83E\uDE99%d", getBalance());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void initHeroes() {
        initDefaultHero();
    }

    private void initDefaultHero() {
        spawnObject(1);
    }

    public void addContext(MenuContext context) {
        this.context = context;
    }

    public MapObject initHeroById(int heroId) {
        // инициализировать героя по его id
        if (heroId == 1) {
            return new Knight(this);
        }
        if (heroId == 2) {
            return new Gollum(this);
        }
        if (heroId == 3) {
            return new Soldier(this);
        }
        if (heroId == 4) {
            return new Ballista(this);
        }
        if (heroId == 5) {
            return new Ghost(this);
        }
        return null;
    }

    public void updateStatus() {
        if (status == -1) {
            return;
        }
        status = 0;  // начало хода
    }

    public void startStep() {
        // ход игрока
        updateStatus();
        action();
    }

    public void finishStep() {
        // завершить ход

        setMovingObjectsDefaultEnergy();
        updateBalance(10);
        if (status == -1) {
            return;
        }
        status = 2;
    }

    public List<MapObject> getMovingMapObjects() {
        // получить все подвижные объекты игрока
        List<MapObject> movingObjects = new ArrayList<>();
        for (MapObject mapObject : map.getMapObjectsByPlayer(this)) {
            if (mapObject instanceof Moving) {
                movingObjects.add(mapObject);
            }
        }
        return movingObjects;
    }

    public List<MapObject> getAttackingObjects() {
        // получить все атакующие объекты игрока
        List<MapObject> attackingObjects = new ArrayList<>();
        for (MapObject mapObject : map.getMapObjectsByPlayer(this)) {
            if (mapObject instanceof Attacking) {
                attackingObjects.add(mapObject);
            }
        }
        return attackingObjects;
    }

    public ArrayList<Supplier<String>> getAttackingObjectsNames() {
        // получить названия атакующих объектов, т.е армии
        ArrayList<Supplier<String>> names = new ArrayList<>();
        for (MapObject attackingObject : getAttackingObjects()) {
            names.add(
                    () -> String.format("%s %s", attackingObject.getName(),
                            map.getCellFromMapObject(attackingObject).getStringPosition()));
        }
        return names;
    }

    public ArrayList<Supplier<String>> getMovingObjectsNames() {
        // получить названия движимых объектов, т.е армии
        ArrayList<Supplier<String>> names = new ArrayList<>();
        for (MapObject movingObject : getMovingMapObjects()) {
            names.add(
                    () -> String.format("%s %s", movingObject.getName(),
                            map.getCellFromMapObject(movingObject).getStringPosition()));
        }
        return names;
    }

    public ArrayList<Building> getBuildings() {
        // получить здания игрока
        return buildings;
    }

    public ArrayList<Supplier<String>> getBuildingsNames() {
        // получить названия зданий
        ArrayList<Supplier<String>> names = new ArrayList<>();
        for (Building building : buildings) {
            names.add(building::getName);
        }
        return names;
    }

    public void addBuilding(Building building) {
        // добавить здание
        buildings.add(building);
    }

    public void buyBuilding(Building building) {
        // покупка здания

        if (isHaveBuilding(building)) {
            CustomLogger.warn(String.format("Здание '%s' уже куплено!", building.getName()));
            return;
        }
        if (balance < building.getCost()) {
            CustomLogger.error("Недостаточно денег для покупки");
            return;
        }
        updateBalance(-building.getCost());
        addBuilding(building);
        CustomLogger.info(String.format("Здание '%s' куплено!", building.getName()));
    }

    public boolean isHaveBuilding(Building checkableBuilding) {
        // проверка на наличие этого здания у игрока
        for (Building building : buildings) {
            if (building.getId() == checkableBuilding.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean isHaveBallista() {
        // проверка на наличие этого объекта у игрока
        for (MapObject mapObject : getAttackingObjects()) {
            if (mapObject instanceof Ballista) {
                return true;
            }
        }
        return false;
    }

    public boolean isActiveStep() {
        // проверка на то, что ход активен
        return status != 2;
    }

    public boolean isActiveAttack() {
        // проверка на то что еще не было атаки
        return status == 0;
    }

    protected void setMovingObjectsDefaultEnergy() {
        // установить героям дефолт энергию
        for (MapObject movingObject : getMovingMapObjects()) {
            ((Moving) movingObject).setDefaultEnergy();
        }
    }

    public void buyObject(int cost, int heroId) {
        MapObject mapObject = initHeroById(heroId);
        if (mapObject instanceof Ballista) {
            if (isHaveBallista()) {
                CustomLogger.error(String.format("Объект %s уже куплен!", mapObject.getName()));
                return;
            }
        }
        if (balance < cost) {
            CustomLogger.error("Недостаточно денег для покупки");
            return;
        }
        spawnObject(heroId);
    }

    public void spawnObject(int heroId) {
        // заспавнить объект в зоне игрока
        Cell cell = map.getFreePlayerCell(this);  // получаем свободную клетку
        MapObject spawnObject = initHeroById(heroId);
        if (cell != null) {
            map.setMapObject(spawnObject, cell.getRow(), cell.getCol());
            updateBalance(-((Buying) spawnObject).getCost());  // уменьшаем баланс
            CustomLogger.info(String.format("Объект '%s' заспавнен в клетке %s",
                    spawnObject.getName(), cell.getStringPosition()));
            return;
        }
        CustomLogger.error(String.format("Не удалось заспавнить объект '%s': нет доступных клеток", spawnObject.getName()));
    }

    public void updateBalance(int delta) {
        // обновим баланс
        balance += delta;
    }

    public ArrayList<MapObject> getEnemyPlayerAttackingObjects() {
        // получить атакующие объекты врага
        ArrayList<MapObject> attackingObjects = new ArrayList<>();
        for (MapObject mapObject : map.getMapObjectsByPlayersEnemy(this)) {
            if (mapObject instanceof Attacking) {
                attackingObjects.add(mapObject);
            }
        }
        return attackingObjects;
    }

    public abstract void action();

    protected abstract void initCastle();

    public abstract String getZoneSymbol();

    public abstract void postAttack();

    public void finishAttack() {
        // закончить атаку
        status = 1;
        postAttack();
    }
}
