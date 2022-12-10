package day09;

import day08.Direction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RopeBridgeApp {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day09/rope_bridge_moves.txt";
        //String path = "src/main/resources/day09/rope_bridge_moves_test.txt";
        //String path = "src/main/resources/day09/rope_bridge_moves_test_v2.txt";
        RopeBridgeApp app = new RopeBridgeApp(path);

        // First exercice
        app.computeScoreV1();

        // second exercice
        app.computeScoreV2();
    }


    private final String filePath;
    private List<Move> moves;

    public RopeBridgeApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.moves = new ArrayList<>();
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            this.moves.add(new Move(content));
        }
        System.out.println(this.moves);
    }

    private void computeScoreV1() {
        computeScore(1);
    }

    private void computeScoreV2() {
        computeScore(9);
    }

    private void computeScore(int numberOfTailKnots) {
        BridgeMap bridgeMap = new BridgeMap(new Position(0, 0), numberOfTailKnots, new Position(0, 0));
        System.out.println(bridgeMap);
        bridgeMap.printMapInConsole();
        for (Move move : this.moves) {
            System.out.println(move);
            bridgeMap.moveHead(move);
            //System.out.println(this.bridgeMap);
            //this.bridgeMap.printMapInConsole();
        }
        System.out.println(bridgeMap);
        bridgeMap.printMapInConsole();

        System.out.println("Final score with 1 tail knot : " + bridgeMap.getUniqueTailPathCount());
    }
}
