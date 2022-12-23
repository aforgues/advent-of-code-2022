package day13;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DistressSignalApp {

    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day13/packets_received.txt";
        //String path = "src/main/resources/day13/packets_received_test.txt";
        DistressSignalApp app = new DistressSignalApp(path);

        // First exercise
        app.computeScoreV1();

        // Second exercise
        app.computeScoreV2();
    }

    private final String filePath;
    private final List<PacketPair> pairs;

    public DistressSignalApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.pairs = new ArrayList<>();
        this.parseFile();
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

    private void computeScoreV1() {


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

    private void computeScoreV2() {
        Set<Value> sortedValues = new TreeSet<>();

        for (PacketPair pair : this.pairs) {
            sortedValues.add(pair.leftPacket().getValue());
            sortedValues.add(pair.rightPacket().getValue());
        }

        // Add divisors
        Value firstDivisorPacketValue = new Value("[[2]]");
        Value secondDivisorPacketValue = new Value("[[6]]");
        sortedValues.addAll(List.of(firstDivisorPacketValue, secondDivisorPacketValue));

        //System.out.println(sortedValues);
        //sortedValues.stream().forEach(v -> System.out.println(v.displayValue()));

        List<Value> sortedValuesAsList = sortedValues.stream().toList();
        System.out.println("First divider packet is at index " + (sortedValuesAsList.indexOf(firstDivisorPacketValue) + 1));
        System.out.println("Second divider packet is at index " + (sortedValuesAsList.indexOf(secondDivisorPacketValue) + 1));

        System.out.println("Decoder key for distress signal is : " + (sortedValuesAsList.indexOf(firstDivisorPacketValue) + 1) * (sortedValuesAsList.indexOf(secondDivisorPacketValue) + 1));
    }

}
