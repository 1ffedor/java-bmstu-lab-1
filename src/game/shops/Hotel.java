package game.shops;

import game.objects.MapObject;
import game.players.Npc;
import game.players.Player;
import game.ui.CustomLogger;

import java.util.Objects;
import java.util.Random;

import java.util.concurrent.Semaphore;

public class Hotel extends Shop {
    public static final String NAME = "Отель «У погибшего альпиниста»";
    public static final int COST = 30;
    private static final int ID = 5;
    private final Random random = new Random();

    private final Semaphore waiters = new Semaphore(5);
    private final int VISITORS_PER_WAITER = 4;

    public Hotel() {
        super(ID, NAME, COST);
    }

    @Override
    public String getOptions() {
        return "1: Короткий отдых – +2 к здоровью (1 день)\n" +
                "2: Длинный отдых – +3 к здоровью (3 дня)";
    }

    @Override
    public String getServiceName(int serviceNumber) {
        switch (serviceNumber) {
            case 1: return "Короткий отдых";
            case 2: return "Длинный отдых";
        }
        return "услуга";
    }

    @Override
    public void handleChoiceByServiceName(Player player, String serviceName) {
        if (Objects.equals(serviceName, "Просто  перекус")) {
            try {
                shortOtd(player);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                longOtd(player);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void handleChoice(Player player, int choice) {
        try {
            if (!waiters.tryAcquire()) {
                if (!(player instanceof Npc)) {
                    CustomLogger.warn("Все номера заняты! Попробуйте позже.");
                }
                return;
            }
            switch (choice) {
                case 0:
                    getStatus();
                    return;
                case 1:
                    shortOtd(player);
                    break;
                case 2:
                    longOtd(player);
                    break;
                default:
                    CustomLogger.warn("Неверный вариант!");
//                    waiters.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void shortOtd(Player player) throws InterruptedException {
        CustomLogger.info(String.format("%s заказал короткий отдых (+2 к здоровью)", player.getName()));
        processService(player, 150, 2);
    }

    private void longOtd(Player player) throws InterruptedException {
        CustomLogger.info(String.format("%s заказал длинный отдых (+3 к здоровью)", player.getName()));
        processService(player, 300, 3);
    }

    public String getStatus() {
        return String.format("Свободно номеров: %d/%d", waiters.availablePermits(), 5);
    }


    @Override
    public void applyBonus(Player player, int bonus) {
        // Применяем бонус
//                player.applyMovementBonus(bonus);
        if (!(player instanceof Npc)) {
            for (MapObject mapObject : player.getMovingMapObjects()) {
                mapObject.changeHp(-bonus);
            }
        }
        CustomLogger.info(String.format(
                "Услуга завершена! +%d к здоровью '%s'", bonus, player.getName()));
    }

    @Override
    public void enter(Player player, int choice) throws InterruptedException {
        switch (choice) {
            case 1:
                shortOtd(player);
            case 2:
                longOtd(player);
        }
    }
}