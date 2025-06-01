package dungeon.engine;
import java.io.*;
import java.util.*;

public class HighScore {
    private final String highScoreFile = "highscore.txt";

    private List<Score> scores = new ArrayList<>();

    public HighScore() {
        loadScores();
    }
    public void addScore(String name, int score){
        scores.add(new Score(name,score));
        scores.sort(Collections.reverseOrder());
        int keptScores = 5;
        if (scores.size()> keptScores){
            scores = scores.subList(0, keptScores);
        }
        saveScores();
    }
    public List<Score> getScores(){
        return scores;
    }

    private void loadScores() {
        scores.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(highScoreFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    scores.add(new Score(name, score));
                }
            }
        } catch (IOException e) {
            System.out.println("No high score file found. Starting fresh.");
        }
    }

    private void saveScores() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(highScoreFile))) {
            for (Score s : scores) {
                bw.write(s.name + "," + s.score);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper class to represent a score
    public static class Score implements Comparable<Score> {
        String name;
        int score;

        Score(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public int compareTo(Score other) {
            return Integer.compare(this.score, other.score); // ascending
        }

        @Override
        public String toString() {
            return name + ": " + score;
        }
    }
}
