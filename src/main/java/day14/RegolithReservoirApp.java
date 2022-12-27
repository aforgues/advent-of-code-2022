package day14;

import day09.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RegolithReservoirApp {

    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day14/puzzle_input.txt";
        //String path = "src/main/resources/day14/puzzle_input_test.txt";
        RegolithReservoirApp app = new RegolithReservoirApp(path);

        // First exercise
        app.computeScore();
    }

    private final String filePath;
    private final List<RockPath> rockPaths;

    public RegolithReservoirApp(String filePath) {
        this.filePath = filePath;
        this.rockPaths = new ArrayList<>();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            this.rockPaths.add(buildNewRockPath(content));
        }
        System.out.println(this.rockPaths);
    }

    private RockPath buildNewRockPath(String content) {
        List<Shape> shapes = new ArrayList<>();
        String[] pointsInPath = content.split(" -> ");
        for (int i = 0; i < pointsInPath.length - 1; i++) {
            Position startPosition = Position.getInstance(pointsInPath[i]);
            Position endPosition = Position.getInstance(pointsInPath[i + 1]);
            shapes.add(new Shape(startPosition, endPosition));
        }

        return new RockPath(shapes);
    }

    private void computeScore() throws FileNotFoundException {
        this.parseFile();

        Cave cave = new Cave(this.rockPaths, new Position(500, 0));
        cave.displayInConsole();
        int score = cave.throwSandUnits();
        cave.displayInConsole();

        System.out.println("Score : " + score);
    }
}
