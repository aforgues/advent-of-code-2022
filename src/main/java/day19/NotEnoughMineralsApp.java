package day19;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class NotEnoughMineralsApp {

    private static final boolean TEST = false;

    public static void main(String[] args) throws FileNotFoundException {
        String path = TEST ? "src/main/resources/day19/puzzle_input_test.txt" : "src/main/resources/day19/puzzle_input.txt";
        NotEnoughMineralsApp app = new NotEnoughMineralsApp(path);

        // First part
        app.computeScore();
    }

    private final String filePath;
    private final List<Blueprint> blueprints;

    public NotEnoughMineralsApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.blueprints = new ArrayList<>();
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            this.blueprints.add(Blueprint.fromPuzzleInput(content));
        }
        //System.out.println(this.blueprints);
    }

    private void computeScore() {
        Instant start = Instant.now();

        AtomicInteger score = new AtomicInteger();

        this.blueprints.parallelStream().forEach(blueprint -> {
        //for (Blueprint blueprint : this.blueprints) {
            Instant startBlueprint = Instant.now();
            BlueprintExplorer blueprintExplorer = new BlueprintExplorer(blueprint);
            int nbMaxOpenedGeodes = blueprintExplorer.explore();
            Instant endBlueprint = Instant.now();
            System.out.println("Maximum number of geodes we could open for blueprint " + blueprint.id() + " is " + nbMaxOpenedGeodes + " => quality level of " + blueprint.id() * nbMaxOpenedGeodes + " computed in " + Duration.between(startBlueprint, endBlueprint).getSeconds() + "s");
            score.addAndGet(blueprint.id() * nbMaxOpenedGeodes);
        //}
        });

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }
}
