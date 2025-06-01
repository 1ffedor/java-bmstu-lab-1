package game.shops;

import game.players.Npc;

import java.util.Random;

public class NpcVisitor implements Runnable {
    private final Shop shop;
    private final String npcName;
    private final Random random = new Random();

    public NpcVisitor(Shop shop, String npcName) {
        this.shop = shop;
        this.npcName = npcName;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            Thread t1 = new Thread(this);
            try {
                int choice = random.nextInt(2) + 1;
                t1.sleep(random.nextInt(5000) + 3000);
                shop.enter(new Npc("NPC 1", 100, null), choice);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}