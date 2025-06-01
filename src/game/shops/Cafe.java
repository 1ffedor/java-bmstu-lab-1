package game.shops;

import game.players.Npc;
import game.players.Player;
import game.ui.CustomLogger;

import java.util.Objects;
import java.util.Random;

import java.util.concurrent.Semaphore;

public class Cafe extends Shop {
    public static final String NAME = "Кафе «Сырники от тети Глаши»";
    public static final int COST = 30;
    private static final int ID = 5;
    private final Random random = new Random();

    private final Semaphore waiters = new Semaphore(3);
    private final int VISITORS_PER_WAITER = 4;

    public Cafe() {
        super(ID, NAME, COST);
        initServices();
    }

    private void initServices() {
        services.put("Просто перекус", 0);
        services.put("Плотный обед", 0);
    }

    @Override
    public String getOptions() {
        return "1: Просто перекус (+2 к перемещению, 15 мин)\n" +
                "2: Плотный обед (+3 к перемещению, 30 мин)";
    }

        @Override
    public void handleChoice(Player player, int choice) {
        try {
            if (!waiters.tryAcquire()) {
                if (!(player instanceof Npc)) {
                    CustomLogger.warn("Все официанты заняты! Попробуйте позже.");
                }
                return;
            }
            switch (choice) {
                case 1:
                    orderSnack(player);
                    break;
                case 2:
                    orderMeal(player);
                    break;
                default:
                    CustomLogger.warn("Неверный вариант!");
//                    waiters.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void orderSnack(Player player) throws InterruptedException {
        CustomLogger.info(String.format("%s заказал перекус (+2 к перемещению)", player.getName()));
        processService(player, 15, 2);
    }

    private void orderMeal(Player player) throws InterruptedException {
        CustomLogger.info(String.format("%s заказал плотный обед (+2 к перемещению)", player.getName()));
        processService(player, 30, 3);
    }

    public String getStatus() {
        return String.format("Свободно официантов: %d/%d", waiters.availablePermits(), 3);
    }

    @Override
    public String getServiceName(int serviceNumber) {
        switch (serviceNumber) {
            case 1: return "Просто перекус";
            case 2: return "Плотный обед";
        }
        return "услуга";
    }

    @Override
    public void handleChoiceByServiceName(Player player, String serviceName) {
        if (Objects.equals(serviceName, "Просто  перекус")) {
            try {
                orderSnack(player);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                orderMeal(player);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void applyBonus(Player player, int bonus) {
        // Применяем бонус
//                player.applyMovementBonus(bonus);
        if (!(player instanceof Npc)) {
            player.applyMovementBonus(bonus);
        }
        CustomLogger.info(String.format(
                "Услуга завершена! +%d к перемещению для всех юнитов '%s'", bonus, player.getName()));
    }

    @Override
    public void enter(Player player, int choice) throws InterruptedException {
        switch (choice) {
            case 1:
                orderSnack(player);
            case 2:
                orderMeal(player);
        }
    }
}