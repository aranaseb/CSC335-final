import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Provides leaderboard reading and writing functionality
 * @author Winston Reese
 */

public class Leaderboard {

    private final List<Integer> scores;

    private final String filename;

    public Leaderboard(String filename) {
        /**
         * Reads an existing stored leaderboard
         *
         * @pre the file passed contains integers on each line, sorted with the
         * highest integer at the top, and the lowest at the bottom
         */
        this.scores = new ArrayList<Integer>();
        this.filename = filename;

        Scanner file;

        try {
            file = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            System.out.println("File was not found!");
            return;
        }

        while (file.hasNextInt()) {
            scores.add(file.nextInt());
        }
    }

    public Leaderboard() {
        /**
         * Creates a leaderboard if one didn't exist previously
         */
        scores = new ArrayList<Integer>();
        filename = "leaderboard.txt";
    }

    public void addScore(int score) {
        /**
         * Adds a score to the leaderboard
         * @param score - the new score to be added
         */
        scores.add(score);
        Collections.sort(scores);
    }

    public void save() {
        /**
         * Writes scores back to the file they were accessed from
         */
        Path file = Paths.get(filename);

        List<String> stringScores = new ArrayList<String>();
        for (int score: scores) {
            stringScores.add(Integer.toString(score));
        }

        try {
            Files.write(file, stringScores, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Failed to save scores!");
        }
    }
}
