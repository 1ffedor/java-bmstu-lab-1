package game.gamemap.cells;

import game.gamemap.IntPair;
import game.gamemap.MainMap;
import game.players.Player;

import java.io.Serializable;
import java.util.Objects;


public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private char symbol;  // тип клетки
    private String color;
    public static final char EMPTY = '#';
    public static final String EMPTY_COLOR = "⬜\uFE0F";
    public static final char ROAD = '+';
    public static final String OBSTACLE = "*";
    private int row;
    private int col;
    private String mapObjectColor;
    public MainMap map;
    public Player playersZone;
    public int penalty;
    public String description = null;

    public Cell(int row, int col) {
        // конструктор клетки
        this.row = row;
        this.col = col;
        this.mapObjectColor = null;
    }

    public void setMapObjectColor(String mapObjectColor) {
        this.mapObjectColor = mapObjectColor;
    }

    public void removeMapObjectSymbol() {
        this.mapObjectColor = null;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getColor() {
        if (mapObjectColor != null) {
            return mapObjectColor;
        }
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setPlayersZone(Player playersZone) {
        // установить клетку зоной какого0либо игрока
        this.playersZone = playersZone;
        this.color = playersZone.getZoneSymbol();
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public int getPenalty() {return penalty;}

    public int getPenaltyForPlayer(Player player) {
        // узнать пенальти для конкретного игрока
        penalty = 1;
        if (Objects.equals(symbol, ROAD)) {
            // дорога
            return 2;
        }
        if (Objects.equals(symbol, EMPTY)) {
            // свободная территория
            return 4;
        }
        if (!playersZone.equals(player)) {
            // чужая территория
            return penalty + 4;
        }
        return penalty;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public void setColor(String color) {
        this.color = color;
    }

//    public char getSymbol() {
//        return symbol;
//    }

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
