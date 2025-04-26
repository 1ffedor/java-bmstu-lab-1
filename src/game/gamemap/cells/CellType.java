package game.gamemap.cells;

public class CellType {
    private char symbol;
    private int penalty;
    private String color;
    private String description;
    private boolean is_castle;

    public CellType(char symbol, int penalty, String color, String description, boolean is_castle) {
        this.symbol = symbol;
        this.penalty = penalty;
        this.description = description;
        this.color = color;
        this.is_castle = is_castle;
    }

    // Геттеры
    public char getSymbol() {
        return symbol;
    }

    public int getPenalty() {
        return penalty;
    }

    public String getColor() {return color;}

    public String getDescription() {
        return description;
    }

    public boolean isCastle() {return is_castle;}
}