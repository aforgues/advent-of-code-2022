package day16;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProboscideaVolcaniumApp {

    public static final boolean TEST = false;

    public static void main(String[] args) throws FileNotFoundException {
        String path = TEST ? "src/main/resources/day16/puzzle_input_test.txt" : "src/main/resources/day16/puzzle_input.txt";
        ProboscideaVolcaniumApp app = new ProboscideaVolcaniumApp(path);

        // First part
        app.computeScore();

        // Second part
        //app.computeScoreV2();
    }

    private final String filePath;
    private final List<Valve> valves;

    public ProboscideaVolcaniumApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.valves = new ArrayList<>();
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            this.valves.add(Valve.fromContent(content));
        }
        System.out.println(this.valves);
    }

    private void computeScore() {
        Instant start = Instant.now();

        TunnelWalkthrough tunnelWalkthrough = new TunnelWalkthrough(this.valves, 30, false);
        //int score = tunnelWalkthrough.exploreBFS("AA");
        int score = tunnelWalkthrough.exploreDFS("AA");

        Instant end = Instant.now();
        System.out.println("Score part 1 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private void computeScoreV2() {
        TunnelWalkthrough tunnelWalkthrough = new TunnelWalkthrough(this.valves, 26, true);
        //int score = tunnelWalkthrough.exploreBFS("AA");
        int score = tunnelWalkthrough.exploreDFS("AA");

        System.out.println("Score part 2: " + score);
    }
}
