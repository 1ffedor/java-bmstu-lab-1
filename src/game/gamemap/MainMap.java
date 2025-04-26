package game.gamemap;

import game.gamemap.cells.Cell;
import game.objects.Attacking;
import game.objects.MapObject;
import game.objects.Moving;
import game.players.Player;
import game.ui.CustomLogger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainMap implements Serializable {
    // ширина высота
    // массив клеток
    private static final long serialVersionUID = 1L;
    private ArrayList<ArrayList<Cell>> cells;
    private HashMap<Player, ArrayList<Cell>> playerCells = new HashMap<>();
    private MapManager mapManager;
    private int width;
    private int height;

    public IntPair computerCastlePosition = null;


    public MainMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.mapManager = new MapManager();  // менеджер карты
        fillCells();
    }

    public void setComputerCastlePosition(IntPair computerCastlePosition) {
        this.computerCastlePosition = computerCastlePosition;
    }

    public IntPair getComputerCastlePosition() {return computerCastlePosition;}

    private void fillCells() {
        fillEmptyCells();
//        fillRoadCells();
    }

    private void fillEmptyCells() {
        // заполняем клетки
        cells = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int col = 0; col < width; col++) {
                Cell cell = new Cell(y, col);
                row.add(cell);
            }
            cells.add(row);
        }
    }

    public void fillPlayersZones(Player firstPlayer, Player secondPlayer) {
        // заполнить зону игрок
        fillFirstPlayerCells(firstPlayer);
        fillSecondPlayerCells(secondPlayer);
    }

    public void fillFirstPlayerCells(Player firstPlayer) {
        playerCells.put(firstPlayer, new ArrayList<>());
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Cell cell = this.getCell(row, col);
                if (Objects.equals(cell.getSymbol(), Cell.EMPTY)) {
                    cell.setPlayersZone(firstPlayer);
                    playerCells.get(firstPlayer).add(cell);
                }
            }
        }
    }

    public void fillSecondPlayerCells(Player secondPlayer) {
        playerCells.put(secondPlayer, new ArrayList<>());
        for (int row = height - 1; row >= height - 5; row--) {
            for (int col = width - 1; col >= width - 5; col--) {
                Cell cell = this.getCell(row, col);
                if (Objects.equals(cell.getSymbol(), Cell.EMPTY)) {
                    cell.setPlayersZone(secondPlayer);
                    playerCells.get(secondPlayer).add(cell);
                }
            }
        }
    }

    public Cell getFreePlayerCell(Player player) {
        // получить свободную от объектов клетку в зоне игрока
        ArrayList<Cell> heroCells = playerCells.get(player);
        for (Cell cell : heroCells) {
            if (mapManager.getMapObjectFromCell(cell) == null) {
                // если нет объекта на карте - свободна
                return cell;
            }
        }
        return null;
    }

    public HashMap<Player, ArrayList<Cell>> getPlayerCells() {
        return playerCells;
    }

    public ArrayList<MapObject> getMapObjectsByPlayer(Player player) {
        // получить объекты игрока
        return mapManager.getMapObjectsByPlayer(player);
    }

    public ArrayList<MapObject> getMapObjectsByPlayersEnemy(Player player) {
        // получить объекты противника
        return mapManager.getMapObjectsByPlayersEnemy(player);
    }

    public ArrayList<MapObject> getMapObjectNeighbours(MapObject mapObject) {
        // все соседи объекта
        ArrayList<MapObject> neighbours = new ArrayList<>();
        Cell cell = mapManager.getCellFromMapObject(mapObject);  // получаем клетку объекта
        ArrayList<Cell> cellNeighbours = getCellNeighbours(cell);  // находим все соседние клетки
        for (Cell cellNeighbour : cellNeighbours) {
            // идём по соседним клеткам
            if (mapManager.getMapObjectFromCell(cellNeighbour) != null) {
                neighbours.add(mapManager.getMapObjectFromCell(cellNeighbour));
            }
        }
        return neighbours;
    }

    private ArrayList<Cell> getCellNeighbours(Cell cell) {
        // получим соседние клетки
        // получим координаты клетки и просто вернём с соседними индексами
        ArrayList<Cell> neighbours = new ArrayList<>();
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                Cell neighbourCell = getCell(cell.getRow() + dy, cell.getCol() + dx);
                if (neighbourCell != null && !neighbourCell.equals(cell)) {
                    neighbours.add(neighbourCell);
                }
            }
        }
        return neighbours;
    }

    public void setMapObject(MapObject mapObject, int row, int col) {
        // разместить объект на карте
        // получаем клетку
        Cell cell = getCell(row, col);
        cell.setMapObjectColor(mapObject.getSymbol());
        mapManager.setMapObjectToCell(mapObject, cell);  // добавим объект на карту
    }

    public void removeMapObject(MapObject mapObject) {
        // удалить объект из клетки
        mapManager.removeMapObject(mapObject);
    }

    public MapObject getMapObjectByCoords(int row, int col) {
        // получить объект по координатам
        Cell cell = getCell(row, col);
        return mapManager.getMapObjectFromCell(cell);
    }

    public Cell getCellFromMapObject(MapObject mapObject) {
        // получить клетку, в которой находится объект
        return mapManager.getCellFromMapObject(mapObject);
    }

    public void moveObject(MapObject movingObject, int dx, int dy) {
        // переместить объект
        Cell oldCell = mapManager.getCellFromMapObject(movingObject);
        Cell newCell = getCell(oldCell.getRow() + dy, oldCell.getCol() + dx);  // получим новую клетку
        if (!isCellAvailable(newCell)) {
            // клетка недоступна
            CustomLogger.error("Клетка недоступна");
            return;
        }
        interactWithCell(movingObject, newCell);
    }

    public void moveObjectToCell(MapObject mapObject, Cell newCell) {
        // переместить подвижный объект в клетку
        if (!isCellAvailable(newCell)) {
            // клетка недоступна
            CustomLogger.error("Клетка недоступна");
            return;
        }
        interactWithCell(mapObject, newCell);
    }

//    protected void interactWithCell(MapObject movableObject, Cell newCell) {
//        // взаимодействие с клеткой
//        MapObject cellObject = mapManager.getMapObjectFromCell(newCell);  // получить объект с клетки
//        if (cellObject == null) {
//            // клетка пуста - перемещаемся в неё
//            if (!movableObject.haveEnergyToMove(newCell.getPenalty(movableObject.getPlayer()))) {
//                Logger.warn("Не хватило энергии для перемещения.");
//                movableObject.getPlayer().postAttack();
//                return;
//            }
//            movableObject.changeEnergy(newCell.getPenalty(movableObject.getPlayer()));  // изменяем энергию
//            mapManager.setMapObjectToCell(movableObject, newCell);  // установим объект в клетку
//            Logger.outln(String.format("Перемещение '%s' в клетку: %s", movableObject.getName(), newCell.getStringPosition()));
//            return;
//        }
//        if (cellObject.getPlayer().equals(movableObject.getPlayer())) {
//            // владелец объекта на клетке совпадает
//            Logger.error(String.format("Клетка %s занята объектом '%s'", newCell.getStringPosition(), cellObject.getName()));
//            return;
//        }
//        movableObject.attack(cellObject);

    protected void interactWithCell(MapObject mapObject, Cell newCell) {
        if (!(mapObject instanceof Moving) || !(mapObject instanceof Attacking)) {
            CustomLogger.error("Объект не поддерживает необходимые интерфейсы.");
            return;
        }
        MapObject cellObject = mapManager.getMapObjectFromCell(newCell);
        if (cellObject == null) {
            handleEmptyCell(mapObject, newCell);
        } else if (cellObject.getPlayer().equals(mapObject.getPlayer())) {
            handleFriendlyCell(cellObject, newCell);
        } else {
            handleEnemyCell(mapObject, cellObject);
        }
    }

    public void handleEmptyCell(MapObject movable, Cell newCell) {
        if (!((Moving) movable).haveEnergyToMove(newCell.getPenaltyForPlayer(movable.getPlayer()))) {
            CustomLogger.warn("Не хватило энергии для перемещения.");
            movable.getPlayer().postAttack();
            return;
        }
        ((Moving) movable).changeEnergy(newCell.getPenaltyForPlayer(movable.getPlayer()));
        mapManager.setMapObjectToCell(movable, newCell);
        CustomLogger.outln(String.format("Перемещение '%s' в клетку: %s", (movable).getName(), newCell.getStringPosition()));
    }

    private void handleFriendlyCell(MapObject cellObject, Cell newCell) {
        CustomLogger.error(String.format("Клетка %s занята объектом '%s'", newCell.getStringPosition(), cellObject.getName()));
    }

    private void handleEnemyCell(MapObject mapObject, MapObject cellObject) {
        ((Attacking) mapObject).attack(cellObject, mapObject.getPlayer(), ((Attacking) mapObject).getDamage());
    }

    public Cell getCell(int row, int col) {
        // получить клетку по координатам
        try {
            Cell cell = cells.get(row).get(col);
            return cell;
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isCellAvailable(Cell cell) {
        // проверить клетку на доступность (нет ли препятствий и тд)
        if (cell == null) {
            // клетка не существует
            return false;
        }
        if (cell.getPenalty() >= 100) {
            return false;
        }
        return true;
    }

    public void display() {
        // вывод на экран
        for (int x = 0; x < width; x++) {
            ArrayList<Cell> row = cells.get(x);
            for (int y = 0; y < height; y++) {
                Cell cell = row.get(y);
                CustomLogger.out(cell.getColor() + " ");
            }
            CustomLogger.outln("");
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
