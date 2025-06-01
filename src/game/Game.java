package game;

import game.gamemap.MainMap;
import game.players.ComputerPlayer;
import game.players.MainPlayer;
import game.players.Npc;
import game.players.Player;
import game.shops.Barbershop;
import game.shops.Cafe;
import game.shops.Hotel;
import game.shops.Shop;
import game.ui.CustomLogger;
import game.ui.player.MenuContext;
import utils.LogConfig;
import utils.ScoreManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.TimeUnit;
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
    private ArrayList<Shop> shops = new ArrayList<>();

    private volatile boolean isTurnActive = false;
    public static final long TURN_TIME_LIMIT_MS = 3_000; // 1 минута на ход
    private transient Thread turnTimerThread;

    public Game(MainMap map, MenuContext context) {
        this.map = map;
        this.context = context;
        initGame();
    }

    private void initGame() {
        LOGGER.log(Level.INFO, "Игра инициализирована");
//        initMap();
        initShops();
        initNpcExecutor();
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
        context.addToStorage("computerPlayer", computerPlayer);
    }

    public void initNpcExecutor() {
        ScheduledExecutorService npcEx = Executors.newScheduledThreadPool(3);
        for (int i = 0; i < 3; i++) {
//            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(32));
            npcEx.scheduleWithFixedDelay(() -> {
                Npc npc = new Npc(String.format("NPC"), 100, null);
                try {
                    if (new Random().nextInt(10) >= 5) {
                        int shopNum = new Random().nextInt(shops.size());
                        int choice = new Random().nextInt(3);
                        npc.processShop(shops.get(shopNum), choice);
                        TimeUnit.SECONDS.sleep(15);
                        npc.updateRating(shops.get(shopNum), choice);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, 3, 25, TimeUnit.SECONDS);
        }
    }

    public void initShops() {
        Hotel hotel = new Hotel();
        Cafe cafe = new Cafe();
        Barbershop barbershop = new Barbershop();
//        Shop barber = new BarberShop();
        shops.add(cafe);
        shops.add(hotel);
        shops.add(barbershop);
        context.addToStorage("cafe", cafe);
        context.addToStorage("hotel", hotel);
        context.addToStorage("barbershop", barbershop);
    }

    public void startGame() {
        // начало игры
        while (gameActive) {  // играем, пока не будет game active false
            // начало раунда -> класс раунда -> класс менюшек -> класс вывода в консоль -> класс
            newRound();
        }
    }

    public synchronized void newRound() {
        for (Player player : players) {
            checkGameStatus();
            CustomLogger.outln(String.format("\nХод игрока: %s. Баланс: %s",
                    player.getName(), player.getBalanceString()));
            startPlayerTurn(player);
        }
    }

    private synchronized void startPlayerTurn(Player player) {
        isTurnActive = true;
        startTurnTimer(player);
        player.startStep();  // Этот метод блокирует выполнение, пока игрок не завершит ход
        // stopTurnTimer() теперь вызывается в forceEndTurn или после нормального завершения хода
    }

    private synchronized void startTurnTimer(Player player) {
        turnTimerThread = new Thread(() -> {
            try {
                Thread.sleep(TURN_TIME_LIMIT_MS);
                synchronized (this) {
                    CustomLogger.outln("\nВремя на ход истекло!");
                    forceEndTurn(player);
//                    if (isTurnActive) {
//
//                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        turnTimerThread.start();
    }

    private void stopTurnTimer() {
        isTurnActive = false;
        if (turnTimerThread != null) {
            turnTimerThread.interrupt();
        }
    }

    private synchronized void forceEndTurn(Player player) {
        if (isTurnActive) {
            isTurnActive = false;
            if (player instanceof MainPlayer) {
                ((MainPlayer)player).interruptCurrentAction();
            } else {
                ((ComputerPlayer)player).interruptCurrentAction();
            }
            player.finishStep();
            if (turnTimerThread != null) {
                turnTimerThread.interrupt();
            }
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

