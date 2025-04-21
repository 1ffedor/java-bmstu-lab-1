package players;

import game.gamemap.MainMap;
import game.players.Player;
import utils.LogConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TestPlayer extends Player {

    private static final Logger LOGGER = LogConfig.getLogger(ComputerPlayerTest.class, Level.INFO);  // инит логера


    public TestPlayer(String name, int balance, int num, MainMap map) {
        super(name, balance, num, map);
    }

    @Override
    public void action() {
    }

    @Override
    protected void initCastle() {
    }

    @Override
    public String getZoneSymbol() {
        return "T";
    }

    @Override
    public void postAttack() {
    }
}