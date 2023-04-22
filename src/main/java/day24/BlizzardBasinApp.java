package day24;

import utils.Move;
import utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlizzardBasinApp {

    private static final boolean TEST = false;

    public static void main(String[] args) throws FileNotFoundException {
        String path = TEST ? "src/main/resources/day24/puzzle_input_test.txt" : "src/main/resources/day24/puzzle_input.txt";
        BlizzardBasinApp app = new BlizzardBasinApp(path);

        // First part
        app.computeScore();
    }

    private final String filePath;
    private Valley valley;
    private final List<Blizzard> blizzards;

    public BlizzardBasinApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.blizzards = new ArrayList<>();
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        List<Position> walls = new ArrayList<>();

        int row = 1;
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            parseLine(content, row, walls);
            row++;
        }
        this.valley = new Valley(walls);
        System.out.println(this.valley);
        System.out.println(this.blizzards);
    }

    private void parseLine(String content, int rowNumber, List<Position> walls) {
        for (int index = 0; index < content.length(); index++) {
            char c = content.charAt(index);
            var currentPosition = new Position(index + 1, rowNumber);
            if (c == '#') {
                walls.add(currentPosition);
            }
            else if (c != '.') {
                this.blizzards.add(new Blizzard(currentPosition, Move.from(Character.toString(c))));
            }
        }
    }

    private void computeScore() {
        Instant start = Instant.now();

        int score = new Expedition(this.valley, this.blizzards).exploreBFS();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
