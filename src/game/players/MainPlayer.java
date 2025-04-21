package game.players;

import game.Game;
import game.GameLoader;
import game.gamemap.MainMap;
import game.objects.MapObject;
import game.objects.Ballista;
import game.objects.stationary.PlayerCastle;
import game.ui.CustomLogger;
import game.ui.player.MainMenuState;
import game.ui.player.control.ControlBallistaState;

public class MainPlayer extends Player {
    public String zoneSymbol = "\uD83D\uDFE6";

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
        // запрашиваем действие
        GameLoader.saveGame(context.get(Game.class), getName() + ".sav");
        context.setState(new MainMenuState(context, this)); // Начинаем с главного меню
        context.addToStorage(Player.class, this);
        context.run(); // Запускаем меню
//        while (isActiveAttack()) {
//            MainMenu.init(this).start();
//            postAttack();
//        }
//        while (isActiveStep()) {  // статус 0, то есть ход продолжается
//            MenuContext context = new MenuContext();
//            context.setState(new MainMenuState(context)); // Начинаем с главного меню
//            context.run(); // Запускаем меню
//        }
        CustomLogger.outln("Конец хода");
    }

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
