import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TemplateApp {

    public static void main(String[] args) throws FileNotFoundException {
        //String path = "src/main/resources/dayXX/puzzle_input.txt";
        String path = "src/main/resources/dayXX/puzzle_input_test.txt";
        TemplateApp app = new TemplateApp(path);

        // First exercise
        app.computeScore();
    }

    private final String filePath;

    public TemplateApp(String filePath) {
        this.filePath = filePath;
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
        }
    }

    private void computeScore() throws FileNotFoundException {
        this.parseFile();

        int score = 0;

        System.out.println("Score : " + score);
    }
}
