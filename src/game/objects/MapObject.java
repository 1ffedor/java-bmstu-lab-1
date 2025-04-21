package game.objects;

import game.players.Player;

import java.io.Serializable;

public abstract class MapObject implements Serializable {
    private static final long serialVersionUID = 1L;
    protected final String name;
    protected String symbol;
    protected Player player;
    protected int hp;

    public MapObject(String name, String symbol, int hp, Player player) {
        this.name = name;
        this.player = player;
        this.hp = hp;
        setSymbol(symbol);
    }

    private void setSymbol(String symbol) {
        this.symbol = symbol;
        if (player != null) {
            if (player.num == 1) {
                this.symbol = symbol.toLowerCase();
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        // получить символ объекта
        return symbol;
    }

    public Player getPlayer() {
        // получить хозяина объекта
        return player;
    }

    public void changeHp(int damage) {
        // изменить число хп
        hp -= damage;
        if (hp <= 0) {
            // смерть -> удалить из списка юнитов у хозяина, удалить из клетки
//            todo: removeFromCell();  // удалить из клетки
            removeFromMap();
            removeFromPlayer(); // удалить у игрока
        }
    }

    public void removeFromMap() {
        // удалить
        player.map.removeMapObject(this);
    }

    public boolean isALive() {
        return hp > 0;
    }

    public int getHp() {
        return hp;
    }

    public void removeFromPlayer() {
        player.map.removeMapObject(this);  // удалим из списка
    };
}
