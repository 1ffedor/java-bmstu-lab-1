package game;

import game.gamemap.MainMap;
import game.players.ComputerPlayer;
import game.players.MainPlayer;
import game.players.Player;
import game.ui.CustomLogger;
import game.ui.player.MenuContext;
import utils.LogConfig;
import utils.ScoreManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogConfig.getLogger(Game.class, "game", Level.INFO);  // инит логера
    private MainMap map;
    private MainPlayer mainPlayer;
    private ComputerPlayer computerPlayer;
    private ArrayList<Player> players;
    private boolean gameActive;
    public static final int MAP_WIDTH = 10;
    public static final int MAP_HEIGHT = 10;
    private MenuContext context;

    private final long TURN_TIME_LIMIT_MS = 60_000; // 1 минута на ход
    private transient Thread turnTimerThread;

    public Game(MainMap map, MenuContext context) {
        this.map = map;
        this.context = context;
        initGame();
    }

    private void initGame() {
        LOGGER.log(Level.INFO, "Игра инициализирована");
//        initMap();
        initPlayers();
        gameActive = true;
    }

    private void initMap() {
        map = new MainMap(MAP_WIDTH, MAP_HEIGHT);  // инициализируем карту
    }

    private void initPlayers() {
        players = new ArrayList<>();
        mainPlayer = new MainPlayer(context.getString("playerName"), 100, map);
        computerPlayer = new ComputerPlayer("Компьютер", 100, map);
        mainPlayer.addContext(context);
        computerPlayer.addContext(context);
        players.add(mainPlayer);
        players.add(computerPlayer);

        map.fillPlayersZones(mainPlayer, computerPlayer);

        mainPlayer.initHeroes();
        computerPlayer.initHeroes();
    }

    public void startGame() {
        // начало игры
        while (gameActive) {  // играем, пока не будет game active false
            // начало раунда -> класс раунда -> класс менюшек -> класс вывода в консоль -> класс
            newRound();
        }
    }

    public void newRound() {
        // новый раунд
        for (Player player : players) {
            checkGameStatus();  // проверка на завершение игры
            CustomLogger.outln(String.format("\nХод игрока: %s. Баланс: %s", player.getName(), player.getBalanceString()));
            player.startStep();
        }
    }

    public void checkGameStatus() {
        // проверка на завершение
        for (Player player : players) {
            if (player.getStatus() == -1) {
                finishGame(player);
            }
        }
    }

    public void finishGame(Player loser) {
        // завершить игру
        gameActive = false;
        CustomLogger.outln(String.format("Игра окончена! Проиграл - %s%n", loser.getName()));
        GameLoader.deleteGame(context.getString("playerName") + ".sav");
        ScoreManager.addScore(mainPlayer.getName(), mainPlayer.getBalance());
        ScoreManager.printTopScores();
        System.exit(0);
    }

    public MainMap getMap() {
        return map;
    }

    public Player getMainPlayer() {
        return mainPlayer;
    }

    public Player getComputerPlayer() {
        return computerPlayer;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public boolean isGameActive() {
        return gameActive;
    }
}

