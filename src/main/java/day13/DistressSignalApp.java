package day13;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DistressSignalApp {

    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day13/packets_received.txt";
        //String path = "src/main/resources/day13/packets_received_test.txt";
        DistressSignalApp app = new DistressSignalApp(path);

        // First exercise
        app.computeScore();
    }

    private final String filePath;
    private List<PacketPair> pairs;

    public DistressSignalApp(String filePath) {
        this.filePath = filePath;
        this.pairs = new ArrayList<>();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String leftContent = scanner.next();
            String rightContent = scanner.next();
            System.out.println("Left : " + leftContent);
            System.out.println("Right : " + rightContent);
            PacketPair pair = new PacketPair(new Packet(leftContent), new Packet(rightContent));
            this.pairs.add(pair);

            // skip blank line
            if (scanner.hasNext())
                scanner.next();
        }

        System.out.println(this.pairs);
    }

    private void computeScore() throws FileNotFoundException {
        this.parseFile();

        int score = 0;

        for (int i = 1; i <= this.pairs.size(); i++) {
            System.out.println("== Pair " + i + " ==");
            PacketPair pair = this.pairs.get(i-1);
            if (pair.areInputsInTheRightOrder()) {
                System.out.println("Pair " + i + " is in the right order");
                score += i;
            }
        }

        System.out.println("Score : " + score);
    }
}
