package game.players;

import game.Game;
import game.GameLoader;
import game.gamemap.MainMap;
import game.objects.MapObject;
import game.objects.Ballista;
import game.objects.Moving;
import game.objects.stationary.Castle;
import game.objects.stationary.PlayerCastle;
import game.ui.CustomLogger;
import game.ui.player.MainMenuState;
import game.ui.player.control.ControlBallistaState;

public class MainPlayer extends Player {
    public String zoneSymbol = "\uD83D\uDFE6";
    private volatile boolean actionInterrupted = false;
    private long turnStartTime;

    public MainPlayer(String name, int balance, MainMap map) {
        super(name, balance,0, map);
        initCastle();
    }

    @Override
    protected void initCastle() {
        castle = new PlayerCastle(this);
        map.setMapObject(castle, 0, 0);
    }

    public void postAttack() {
        // атака перед завершением хода
        // проверим на наличие баллисты
        for (MapObject attackingObject : getAttackingObjects()) {
            if (attackingObject instanceof Ballista) {
                context.setState(new ControlBallistaState(context, this, (Ballista) attackingObject));
                context.run();
            }
        }
        finishStep();
    }

    @Override
    public String getZoneSymbol() {
        return zoneSymbol;
    }

    @Override
    public void action() {
        actionInterrupted = false;
        turnStartTime = System.currentTimeMillis();

        try {
            GameLoader.saveGame(context.get(Game.class), getName() + ".sav");
            context.setState(new MainMenuState(context, this));
            context.addToStorage(Player.class, this);
            context.run();

            if (context.isInterrupted()) {
                CustomLogger.outln("Ход завершен по таймауту");
                finishStep();
            }
        } catch (Exception e) {
            if (actionInterrupted || context.isInterrupted()) {
                CustomLogger.outln("Ход завершен по таймауту");
                finishStep();
            }
        }
    }

    public void interruptCurrentAction() {
        actionInterrupted = true;
        finishStep();
        context.finish();
        if (context != null) {
            context.interrupt();
        }
    }

    public void applyMovementBonus(int bonus) {
        for (MapObject mapObject : getMovingMapObjects()) {
            if (mapObject instanceof Moving) {
                ((Moving) mapObject).setEnergy(((Moving) mapObject).getEnergy() + bonus);
            }
        }
    }

    public void applyEnemyCastleBonus(int bonus) {
        ComputerPlayer computerPlayer = (ComputerPlayer) context.get("computerPlayer");
        Castle castle = computerPlayer.getCastle();
        castle.changeHp(bonus);
    }

    public long getRemainingTime() {
        return Math.max(0, Game.TURN_TIME_LIMIT_MS - (System.currentTimeMillis() - turnStartTime));
    }

//    @Override
//    public void action() {
//        // запрашиваем действие
//        GameLoader.saveGame(context.get(Game.class), getName() + ".sav");
//        context.setState(new MainMenuState(context, this)); // Начинаем с главного меню
//        context.addToStorage(Player.class, this);
//        context.run(); // Запускаем меню
////        while (isActiveAttack()) {
////            MainMenu.init(this).start();
////            postAttack();
////        }
////        while (isActiveStep()) {  // статус 0, то есть ход продолжается
////            MenuContext context = new MenuContext();
////            context.setState(new MainMenuState(context)); // Начинаем с главного меню
////            context.run(); // Запускаем меню
////        }
//        CustomLogger.outln("Конец хода");
//    }

    @Override
    public void finishStep() {
        // завершить ход
        try {
            context.finish();
        } catch (Exception e) {

        }
        setMovingObjectsDefaultEnergy();
        if (status == -1) {
            return;
        }
        status = 2;
    }
}
