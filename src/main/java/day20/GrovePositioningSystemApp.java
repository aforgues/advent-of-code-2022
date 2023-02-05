package day20;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Scanner;

public class GrovePositioningSystemApp {

    private static final boolean TEST = false;
    private final EncryptedFile encryptedFile;

    public static void main(String[] args) throws FileNotFoundException {
        String path = TEST ? "src/main/resources/day20/puzzle_input_test.txt" : "src/main/resources/day20/puzzle_input.txt";
        GrovePositioningSystemApp app = new GrovePositioningSystemApp(path);

        // First part
        app.computeScore();
    }

    private final String filePath;

    public GrovePositioningSystemApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.encryptedFile = new EncryptedFile();
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            this.encryptedFile.add(Integer.parseInt(content));
        }
        System.out.println(this.encryptedFile);
    }

    private void computeScore() {
        Instant start = Instant.now();

        int score = this.encryptedFile.computeGroveCoordinate();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
