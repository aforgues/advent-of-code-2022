package day23;

import utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UnstableDiffusionApp {

    private static final boolean TEST = false;

    public static void main(String[] args) throws FileNotFoundException {
        String path = TEST ? "src/main/resources/day23/puzzle_input_test.txt" : "src/main/resources/day23/puzzle_input.txt";
        UnstableDiffusionApp app = new UnstableDiffusionApp(path);

        // First part
        app.computeScore();

        // Second part
        app.computeScorePart2();
    }

    private final String filePath;
    private Grove grove;

    public UnstableDiffusionApp(String filePath) {
        this.filePath = filePath;
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        List<Elve> elves = new ArrayList<>();

        int y = 1;
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            for (int i = 0; i < content.length(); i++) {
                if (content.charAt(i) == '#') {
                    elves.add(new Elve(new Position(i+1, y)));
                }
            }
            y++;
        }
        this.grove = new Grove(elves);
        System.out.println(this.grove);
    }

    private void computeScore() throws FileNotFoundException {
        this.parseFile();
        Instant start = Instant.now();

        int score = new DiffusionProcess(grove).launch();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private void computeScorePart2() throws FileNotFoundException {
        this.parseFile();
        Instant start = Instant.now();

        int score = new DiffusionProcess(grove).launchUntilNoElvesMove();

        Instant end = Instant.now();

        System.out.println("Score part 2 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
