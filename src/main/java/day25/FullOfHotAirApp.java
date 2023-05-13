package day25;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FullOfHotAirApp {
    private static final boolean TEST = false;
    private static final String PUZZLE_INPUT_FILE_NAME = TEST ? "puzzle_input_test.txt" : "puzzle_input.txt";

    public static void main(String[] args) throws FileNotFoundException {
        FullOfHotAirApp app = new FullOfHotAirApp();

        // First part
        app.computeScore();
    }

    private final String filePath;
    private Bob bob;

    public FullOfHotAirApp() throws FileNotFoundException {
        String BASE_PATH = "src/main/resources/" + this.getClass().getPackageName() + "/";
        this.filePath = BASE_PATH + PUZZLE_INPUT_FILE_NAME;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        List<SNAFU> snafus = new ArrayList<>();
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            snafus.add(SNAFU.from(content));
        }
        this.bob = new Bob(snafus);
        System.out.println(this.bob);
    }

    private void computeScore() {
        Instant start = Instant.now();

        String score = this.bob.sum().stringValue();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
