package game.gamemap;

import game.gamemap.cells.Cell;
import game.objects.MapObject;
import game.players.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapManager implements Serializable {
    // менеджер карты для связи клетка-объект
    private Map<Cell, MapObject> cellToObject = new HashMap<>();
    private Map<MapObject, Cell> mapObjectToCell = new HashMap<>();
//    private Map<Player, ArrayList<MapObject>> playerMapObjects = new HashMap<>();

    public void setMapObjectToCell(MapObject mapObject, Cell newCell) {
        // установить объект в клетку
        // todo: проверка на null
        if (mapObjectToCell.containsKey(mapObject)) {
            // удалим объект из старой клетки
            Cell oldCell = mapObjectToCell.get(mapObject);
            oldCell.removeMapObjectSymbol();
            cellToObject.remove(oldCell);
        }

        if (cellToObject.containsKey(newCell)) {
            // удалим объект из новой клетки
            MapObject oldMapObject = cellToObject.get(newCell);
            newCell.removeMapObjectSymbol();
            mapObjectToCell.remove(oldMapObject);
        }

        // создадим новые связи
        cellToObject.put(newCell, mapObject);
        mapObjectToCell.put(mapObject, newCell);
        newCell.setMapObjectColor(mapObject.getSymbol());
    }

    public void removeMapObject(MapObject mapObject) {
        // удалить объект из клетки
        if (mapObjectToCell.containsKey(mapObject)) {
            Cell oldCell = mapObjectToCell.get(mapObject);
            oldCell.removeMapObjectSymbol();
            cellToObject.remove(oldCell);
            mapObjectToCell.remove(mapObject);
        }
    }

    public MapObject getMapObjectFromCell(Cell cell) {
        // получить объект из клетки
        if (cellToObject.containsKey(cell)) {
            return cellToObject.get(cell);
        }
        return null;
    }

    public Cell getCellFromMapObject(MapObject mapObject) {
        // получить клетку, на которой находится объект
        if (mapObjectToCell.containsKey(mapObject)) {
            return mapObjectToCell.get(mapObject);
        }
        return null;
    }

    public boolean isCellEmpty(Cell cell) {
        // проверка клетки на пустоту
        return !cellToObject.containsKey(cell);
    }

    public ArrayList<MapObject> getMapObjectsByPlayer(Player player) {
        // получить объекты игрока
        ArrayList<MapObject> result = new ArrayList<>();
        for (MapObject mapObject : mapObjectToCell.keySet()) {
            // идём по всем объектам карты
            if (mapObject.getPlayer().equals(player)) {
                result.add(mapObject);
            }
        }
        return result;
    }

    public ArrayList<MapObject> getMapObjectsByPlayersEnemy(Player player) {
        // получить объекты противника
        ArrayList<MapObject> result = new ArrayList<>();
        for (MapObject mapObject : mapObjectToCell.keySet()) {
            // идём по всем объектам карты
            if (!mapObject.getPlayer().equals(player)) {
                result.add(mapObject);
            }
        }
        return result;
    }
}
