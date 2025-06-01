package game.shops;

import game.buildings.Building;
import game.players.Player;
import game.ui.CustomLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public abstract class Shop extends Building{
    private final Semaphore waiters = new Semaphore(3);
    private volatile int rating = 1;
    protected HashMap<String, Integer> services = new HashMap<>();


    public Shop(int id, String name, int cost) {
        super(id, name, cost);
    }

    public void updateRating(int delta) {
        rating += delta;
    }

    public int getRating() {
        return rating;
    }

    protected abstract void applyBonus(Player player, int bonus);

    protected void processService(Player player, int gameMinutes, int bonus) throws InterruptedException {
        new Thread(() -> {
            try {
                // Конвертируем игровые минуты в реальные секунды
                long realTimeMs = gameMinutes * 1000;
                Thread.sleep(realTimeMs);
                applyBonus(player, bonus);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                waiters.release();
            }
        }).start();
    }

    public abstract void enter(Player player, int choice) throws InterruptedException;

    public abstract String getStatus();

    public abstract String getServiceName(int serviceNumber);


    public abstract void handleChoiceByServiceName(Player player, String serviceName);

    public List<Map.Entry<String, Integer>> getOptionsByRating() {
        List<Map.Entry<String, Integer>> sortedServices = services.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
        return sortedServices;
    };

    public void updateServiceRating(String playerName, String serviceName, int delta) {
        services.compute(serviceName, (k, rating) -> rating + delta);
        CustomLogger.info(String.format("%s поставил оценку %s услуге %s", playerName, delta, serviceName));
    }
}
