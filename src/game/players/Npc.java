package game.players;

import game.gamemap.MainMap;
import game.shops.Cafe;
import game.shops.Shop;

import java.util.Random;

public class Npc extends Player {
    public String zoneSymbol = "\uD83D\uDFE6";
    private volatile boolean actionInterrupted = false;
    private long turnStartTime;

    public Npc(String name, int balance, MainMap map) {
        super(name, balance,0, map);
    }

    public Npc(String name, int balance, int num, MainMap map) {
        super(name, balance, num, map);
    }

    @Override
    protected void initCastle() {
    }

    @Override
    public String getZoneSymbol() {
        return zoneSymbol;
    }

    @Override
    public void postAttack() {

    }

    @Override
    public long getRemainingTime() {
        return 0;
    }

    @Override
    public void applyMovementBonus(int bonus) {

    }

    @Override
    public void applyEnemyCastleBonus(int bonus) {

    }

    @Override
    public void action() {

    }

    public void updateRating(Shop shop, int choice) {
        String serviceName = shop.getServiceName(choice);
        shop.updateServiceRating(name, serviceName, new Random().nextInt(1, 5));
    }

    public void processShop(Shop shop, int choice) throws InterruptedException {
        String serviceName = shop.getServiceName(choice);
        shop.handleChoiceByServiceName(this, serviceName);
    }
}
