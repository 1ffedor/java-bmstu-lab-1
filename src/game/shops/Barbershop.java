package game.shops;

import game.players.ComputerPlayer;
import game.players.Npc;
import game.players.Player;
import game.ui.CustomLogger;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import java.util.concurrent.Semaphore;

public class Barbershop extends Shop {
    public static final String NAME = "Парикмахерская «Отрезанное ухо»";
    public static final int COST = 30;
    private static final int ID = 5;

    private final Semaphore waiters = new Semaphore(2);


    public Barbershop() {
        super(ID, NAME, COST);
        initServices();
    }

    private void initServices() {
        services.put("Просто стрижка", 0);
        services.put("Модная стрижка", 0);
    }

    @Override
    public String getOptions() {
        return "1: Просто стрижка (бонусов нет, 10 мин)\n" +
                "2: Модная стрижка (уменьшение HP вражеского замка, 30 мин)";
    }

    @Override
    public String getServiceName(int serviceNumber) {
        switch (serviceNumber) {
            case 1: return "Просто стрижка";
            case 2: return "Модная стрижка";
        }
        return "услуга";
    }

    @Override
    public void handleChoiceByServiceName(Player player, String serviceName) {
        if (Objects.equals(serviceName, "Просто  перекус")) {
            try {
                defaultHaircut(player);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                fashionableHaircut(player);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void handleChoice(Player player, int choice) {
        try {
            if (!waiters.tryAcquire()) {
                CustomLogger.warn("Все парикмахеры заняты! Попробуйте позже.");
//                if (!(player instanceof Npc)) {
//                    CustomLogger.warn("Все официанты заняты! Попробуйте позже.");
//                }
                return;
            }
            switch (choice) {
                case 0:
                    getStatus();
                    return;
                case 1:
                    defaultHaircut(player);
                    break;
                case 2:
                    fashionableHaircut(player);
                    break;
                default:
                    CustomLogger.warn("Неверный вариант!");
//                    waiters.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void defaultHaircut(Player player) throws InterruptedException {
        CustomLogger.info(String.format("%s заказал услугу простая стрижка (без бонусов)", player.getName()));
        processService(player, 15, 0);
    }

    private void fashionableHaircut(Player player) throws InterruptedException {
        CustomLogger.info(String.format("%s заказал услугу модная стрижка (уменьшение HP вражеского замка)",
                player.getName()));
        processService(player, 30, -10);
    }

    public String getStatus() {
        return String.format("Свободно парикмахеров: %d/%d", waiters.availablePermits(), 2);
    }


    @Override
    public void applyBonus(Player player, int bonus) {
        // Применяем бонус
//                player.applyMovementBonus(bonus);
        if (!(player instanceof Npc)) {
            player.applyEnemyCastleBonus(bonus);
        }
        CustomLogger.info(String.format(
                "Услуга завершена! %d к HP вражеского замка '%s'", bonus, player.getName()));
    }

    @Override
    public void enter(Player player, int choice) throws InterruptedException {
        switch (choice) {
            case 1:
                defaultHaircut(player);
            case 2:
                fashionableHaircut(player);
        }
    }
}