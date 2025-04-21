package game.objects.stationary;

import game.objects.Static;
import game.players.Player;
import game.objects.MapObject;

public abstract class Castle extends MapObject implements Static {
    private  final static int hp = 150;

    public Castle(String name, String symbol, Player player) {
        super(name, symbol, hp, player);
    }

    @Override
    public void removeFromPlayer() {
        player.castle = null;  // потеря замка - поражение
        player.setStatus(-1);
    }
}


