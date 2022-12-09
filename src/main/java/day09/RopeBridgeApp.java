package day09;

import day08.Direction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RopeBridgeApp {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day09/rope_bridge_moves.txt";
        //String path = "src/main/resources/day09/rope_bridge_moves_test.txt";
        RopeBridgeApp app = new RopeBridgeApp(path);

        // First exercice
        app.computeScoreV1();

        // second exercice
        app.computeScoreV2();
    }


    private final String filePath;
    private List<Move> moves;
    private BridgeMap bridgeMap;

    public RopeBridgeApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.moves = new ArrayList<>();
        this.bridgeMap = new BridgeMap(new Position(0, 0), new Position(0, 0));
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
        System.out.println(this.bridgeMap);
        this.bridgeMap.printMapInConsole();
    }

    private void computeScoreV1() {
        for (Move move : this.moves) {
            System.out.println(move);
            this.bridgeMap.moveHead(move);
            //System.out.println(this.bridgeMap);
            //this.bridgeMap.printMapInConsole();
        }
        System.out.println(this.bridgeMap);
        //this.bridgeMap.printMapInConsole();

        System.out.println("Final score : " + this.bridgeMap.getUniqueTailPathCount());
    }

    private void computeScoreV2() {
        //int score = 0;
        //System.out.println("Final score : " + score);
    }
}
