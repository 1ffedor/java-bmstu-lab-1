package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import game.ui.CustomLogger;

import java.io.*;
import java.util.*;

public class ScoreManager {
    private static final String SCORES_FILE = "saves/scores.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static class PlayerScore {
        public String playerName;
        public int score;

        public PlayerScore(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
        }
    }

    public static void addScore(String playerName, int score) {
        List<PlayerScore> scores = loadScores();
        scores.add(new PlayerScore(playerName, score));
        saveScores(scores);
    }

    public static List<PlayerScore> loadScores() {
        File file = new File(SCORES_FILE);
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, new TypeToken<List<PlayerScore>>(){}.getType());
        } catch (IOException e) {
            CustomLogger.error("Ошибка чтения файла рекордов");
            return new ArrayList<>();
        }
    }

    // Сохранить все рекорды в файл
    private static void saveScores(List<PlayerScore> scores) {
        try (Writer writer = new FileWriter(SCORES_FILE)) {
            gson.toJson(scores, writer);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении рекордов: " + e.getMessage());
        }
    }

    public static void printTopScores() {
        CustomLogger.outln("Таблица рекордов:");
        List<PlayerScore> topScores = loadScores();
        for (int i = 0; i < topScores.size(); i++) {
            PlayerScore score = topScores.get(i);
            CustomLogger.outln(String.format("%s: %s - %s", i + 1, score.playerName, score.score));
        }
    }
}