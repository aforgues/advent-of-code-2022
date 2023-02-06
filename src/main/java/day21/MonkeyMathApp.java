package day21;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MonkeyMathApp {

    private static final boolean TEST = false;

    public static void main(String[] args) throws FileNotFoundException {
        String path = TEST ? "src/main/resources/day21/puzzle_input_test.txt" : "src/main/resources/day21/puzzle_input.txt";
        MonkeyMathApp app = new MonkeyMathApp(path);

        // First part
        app.computeScore();
    }

    private final String filePath;
    private final Map<String, MonkeyJob> monkeyJobByMonkeyNames;

    public MonkeyMathApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        monkeyJobByMonkeyNames = new HashMap<>();
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            monkeyJobByMonkeyNames.put(content.split(":")[0], MonkeyJob.from(content.split(": ")[1]));
        }
    }

    private void computeScore() {
        Instant start = Instant.now();

        // Get root job
        MonkeyJob rootJob = this.monkeyJobByMonkeyNames.get("root");

        long score = rootJob.computeNumber(this.monkeyJobByMonkeyNames);

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
