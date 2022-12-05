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
        app.computeScore();

        // second exercice
        //app.computeScoreV2();
    }


    private final String filePath;
    private List<Stack> stacks;

    public CrateStackApp(String filePath) {
        this.filePath = filePath;
        this.stacks = new ArrayList<>();
    }

    private void computeScore() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        String totalScore;

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

                // apply rules to stacks
                for (int i = 0; i<nbCrateToMove; i++) {
                    Stack sourceStack = getStackByNumber(fromStackNumber);
                    Stack targetStack = getStackByNumber(toStackNumber);
                    targetStack.addCrateOnTop(sourceStack.getCrates().get(0));
                    sourceStack.getCrates().remove(0);
                }
                System.out.println(this.stacks);
            }
        }
        totalScore = this.stacks.stream()
                .sorted(Comparator.comparingInt(Stack::getStackNumber))
                .map(stack -> stack.getCrates().get(0))
                .map(crate -> String.valueOf(crate.getIdentifier()))
                        .reduce("", (a, b) -> a + b);
        System.out.println("totalScore : " + totalScore);
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

    /*private void computeScoreV2() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        int totalScore = 0;
        while (scanner.hasNext()) {
            String content = scanner.next();

        }
        System.out.println("totalScore : " + totalScore);
    }*/
}
