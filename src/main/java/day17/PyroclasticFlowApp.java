package day17;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PyroclasticFlowApp {

    private static final boolean TEST = false;

    public static void main(String[] args) throws FileNotFoundException {
        String path = TEST ? "src/main/resources/day17/puzzle_input_test.txt" : "src/main/resources/day17/puzzle_input.txt";
        PyroclasticFlowApp app = new PyroclasticFlowApp(path);

        // First part
        app.computeScore();
    }

    private final String filePath;
    private final List<GasJet> gasJets;

    public PyroclasticFlowApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.gasJets = new ArrayList<>();
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            for (char c : content.toCharArray()) {
                this.gasJets.add(c == '<' ? GasJet.PUSH_LEFT : GasJet.PUSH_RIGHT);
            }
            System.out.println(this.gasJets);
        }
    }

    private void computeScore() {
        Instant start = Instant.now();

        Chamber chamber = new Chamber(7, this.gasJets);

        int score = chamber.runRocksFallingWithGasJets(2022);
        chamber.displayInConsole();

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
