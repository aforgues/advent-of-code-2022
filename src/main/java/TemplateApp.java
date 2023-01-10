import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Scanner;

public class TemplateApp {

    private static final boolean TEST = true;

    public static void main(String[] args) throws FileNotFoundException {
        String path = TEST ? "src/main/resources/dayXX/puzzle_input_test.txt" : "src/main/resources/dayXX/puzzle_input.txt";
        TemplateApp app = new TemplateApp(path);

        // First part
        app.computeScore();
    }

    private final String filePath;

    public TemplateApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
        }
    }

    private void computeScore() {
        Instant start = Instant.now();

        int score = 0;

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
