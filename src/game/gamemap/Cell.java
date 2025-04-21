package game.gamemap;

import game.players.Player;

import java.io.Serializable;
import java.util.Objects;


public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type;  // тип клетки
    public static final String EMPTY = "⬜\uFE0F";
    public static final String ROAD = "\uD83D\uDFE5";
    public static final String OBSTACLE = "*";
    private int row;
    private int col;
    private String mapObjectSymbol;
    public MainMap map;
    public Player playersZone;

    public Cell(String type, int row, int col) {
        // конструктор клетки
        this.type = type;
        this.row = row;
        this.col = col;
        this.mapObjectSymbol = null;
    }

    public void setMapObjectSymbol(String mapObjectSymbol) {
        this.mapObjectSymbol = mapObjectSymbol;
    }

    public void removeMapObjectSymbol() {
        this.mapObjectSymbol = null;
    }

    public String getSymbol() {
        if (mapObjectSymbol != null) {
            return mapObjectSymbol;
        }
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPlayersZone(Player playersZone) {
        // установить клетку зоной какого0либо игрока
        this.playersZone = playersZone;
        this.type = playersZone.getZoneSymbol();
    }

    public int getPenalty(Player player) {
        // узнать пенальти для конкретного игрока
        if (Objects.equals(type, ROAD)) {
            // дорога
            return 2;
        }
        if (Objects.equals(type, EMPTY)) {
            // свободная территория
            return 4;
        }
        if (!playersZone.equals(player)) {
            // чужая территория
            return 4;
        }
        return 1;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getStringPosition() {
        // получить позицию строкой в человеческом виде
        return String.format("(%s, %s)", col + 1, row + 1);
    }

    private boolean isOnDiagonal() {
        // проверить, находится ли клетка на диагонали
        return row == col;
    }

    public IntPair getDiagonalCoordinates() {
        // получить ближайшие координаты диагонали
        if (isOnDiagonal()) {
            return new IntPair(row - 1, col - 1);
        }
        if (col <= row) {
            return new IntPair(row - 1, col);
        }
        return new IntPair(row, col - 1);
    }
}
