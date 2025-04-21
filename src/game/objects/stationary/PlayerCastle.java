package game.objects.stationary;

import game.players.Player;

public class PlayerCastle extends Castle {
    private static final String name = "Замок игрока";
    private static final String symbol = "\uD83C\uDFE0";

    public PlayerCastle(Player player) {
        super(name, symbol, player);
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

}
