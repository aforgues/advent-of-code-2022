package day22;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.*;

public class MonkeyMapApp {

    private static final boolean TEST = false;

    public static void main(String[] args) throws FileNotFoundException {
        String path = TEST ? "src/main/resources/day22/puzzle_input_test.txt" : "src/main/resources/day22/puzzle_input.txt";
        MonkeyMapApp app = new MonkeyMapApp(path);

        // First part
        app.computeScore();
    }

    private final String filePath;
    private MonkeyMap monkeyMap;
    private Path path;

    public MonkeyMapApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        Map<Integer, Row> rows = new HashMap<>();
        int row = 1;
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);

            if (! content.isEmpty()) {
                rows.put(row, Row.from(content, row));
                row++;
            }
            else {
                String pathToFollow = scanner.next();
                this.path = Path.from(pathToFollow);
            }
        }
        monkeyMap = new MonkeyMap(rows);
        //System.out.println(monkeyMap);
        monkeyMap.drawMapInConsole(Collections.emptySet());
        //System.out.println("Path fo follow : " + path);
    }

    private void computeScore() {
        Instant start = Instant.now();

        int score = new PasswordExplorer(this.monkeyMap, this.path).explore();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
