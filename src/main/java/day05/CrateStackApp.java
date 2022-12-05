package day05;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CrateStackApp {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day05/crate_stacks_rearrangements.txt";
        //String path = "src/main/resources/day05/crate_stacks_rearrangements_test.txt";
        CrateStackApp app = new CrateStackApp(path);

        // First exercice
        app.computeCrateMover9000();

        // second exercice
        app.computeCrateMover9001();
    }


    private final String filePath;
    private List<Stack> stacks;
    private List<Move> moves;

    public CrateStackApp(String filePath) {
        this.filePath = filePath;
        this.init();
    }

    private void init() {
        this.stacks = new ArrayList<>();
        this.moves = new ArrayList<>();
    }

    private void computeCrateMover9000() throws FileNotFoundException {
        this.init();
        this.parsePuzzle();

        // apply rules to stacks
        for (Move move : this.moves) {
            Stack sourceStack = getStackByNumber(move.fromStackNumber());
            Stack targetStack = getStackByNumber(move.toStackNumber());
            for (int i = 0; i < move.nbCrateToMove(); i++) {
                targetStack.addCrateOnTop(sourceStack.getCrates().get(0));
                sourceStack.getCrates().remove(0);
            }
            System.out.println(this.stacks);
        }
        String totalScore = extractCrateIdentifierFromTopOfStacks();
        System.out.println("totalScore : " + totalScore);
    }

    private void computeCrateMover9001() throws FileNotFoundException {
        this.init();
        this.parsePuzzle();

        // apply new rules to stacks
        for (Move move : this.moves) {
            Stack sourceStack = getStackByNumber(move.fromStackNumber());
            Stack targetStack = getStackByNumber(move.toStackNumber());
            targetStack.getCrates().addAll(0, sourceStack.getCrates().subList(0, move.nbCrateToMove()));
            if (move.nbCrateToMove() > 0) {
                sourceStack.getCrates().subList(0, move.nbCrateToMove()).clear();
            }
            System.out.println(this.stacks);
        }

        String totalScore = extractCrateIdentifierFromTopOfStacks();
        System.out.println("totalScore : " + totalScore);
    }

    private void parsePuzzle() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        boolean parsingStacks = true;
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);

            if (parsingStacks) {
                if (content.contains("1   2")) {
                    scanner.next(); // skip next empty  line
                    parsingStacks = false;
                    System.out.println(this.stacks);
                    continue;
                }
                // Stack 1 is at index 1, Stack 2 is at index 5 (+4), Stack 3 is at index 9 (+4)...
                for (int i = 1; i<content.length(); i+=4) {
                    char crateIdentifier = content.charAt(i);
                    if (crateIdentifier == ' ') {
                        continue;
                    }
                    int stackNumber = i/4 + 1;
                    appendCrateToStack(stackNumber, crateIdentifier);
                }
            }
            // parsing rules
            else {
                Scanner scan = new Scanner(content);
                scan.next(); // skip move
                int nbCrateToMove = scan.nextInt();
                scan.next(); // skip from
                int fromStackNumber = scan.nextInt();
                scan.next(); // skip to
                int toStackNumber = scan.nextInt();

                Move move = new Move(nbCrateToMove, fromStackNumber, toStackNumber);
                this.moves.add(move);
            }
        }
    }

    private String extractCrateIdentifierFromTopOfStacks() {
        return this.stacks.stream()
                .sorted(Comparator.comparingInt(Stack::getStackNumber))
                .map(stack -> stack.getCrates().get(0))
                .map(crate -> String.valueOf(crate.getIdentifier()))
                .reduce("", (a, b) -> a + b);
    }

    private void appendCrateToStack(int stackNumber, char crateIdentifier) {
        Stack stack = getStackByNumber(stackNumber);
        if (stack == null) {
            stack = new Stack(stackNumber);
            this.stacks.add(stack);
        }
        stack.addCrate(crateIdentifier);
    }

    private Stack getStackByNumber(int stackNumber) {
        return this.stacks.stream().filter(stack -> stack.getStackNumber() == stackNumber).findFirst().orElse(null);
    }

}
