package game.objects.stationary;

import game.players.Player;

public class ComputerCastle extends Castle {
    private static final String name = "Замок комьютера";
    private static final String symbol = "\uD83C\uDFE0";

    public ComputerCastle(Player player) {
        super(name, symbol, player);
    }

    @Override
    public String getSymbol() {
        return symbol;
    }
}
