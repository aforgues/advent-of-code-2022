package day11;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MonkeyInTheMiddleApp {

    private static final String MONKEY = "Monkey";
    private static final String STARTING_ITEMS = "  Starting items: ";
    private static final String OPERATION = "  Operation: new = ";
    private static final String TEST_DIVISIBLE_BY = "  Test: divisible by ";
    private static final String IF_TRUE_THROW_TO_MONKEY = "    If true: throw to monkey ";
    private static final String IF_FALSE_THROW_TO_MONKEY = "    If false: throw to monkey ";
    private static final int NUMBER_OF_ROUNDS = 20;

    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day11/monkey_notes.txt";
        //String path = "src/main/resources/day11/monkey_notes_test.txt";
        MonkeyInTheMiddleApp app = new MonkeyInTheMiddleApp(path);

        // First exercise
        app.computeScore();
    }


    private final String filePath;
    private List<Monkey> monkeys;

    public MonkeyInTheMiddleApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.monkeys = new ArrayList<>();
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        int currentMonkeyNumber = 0;

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            if (content.startsWith(MONKEY)) {
                currentMonkeyNumber = Integer.parseInt(content.substring(MONKEY.length()+1, content.indexOf(":")));
                this.monkeys.add(new Monkey(currentMonkeyNumber));
            }
            else if (content.startsWith(STARTING_ITEMS)) {
                this.monkeys.get(currentMonkeyNumber).initItems(content.substring(STARTING_ITEMS.length()));
            }
            else if (content.startsWith(OPERATION)) {
                this.monkeys.get(currentMonkeyNumber).initInspectOperation(content.substring(OPERATION.length()));
            }
            else if (content.startsWith(TEST_DIVISIBLE_BY)) {
                this.monkeys.get(currentMonkeyNumber).initInspectTest(content.substring(TEST_DIVISIBLE_BY.length()));
            }
            else if (content.startsWith(IF_TRUE_THROW_TO_MONKEY)) {
                this.monkeys.get(currentMonkeyNumber).initValidTestTargetMonkey(content.substring(IF_TRUE_THROW_TO_MONKEY.length()));
            }
            else if (content.startsWith(IF_FALSE_THROW_TO_MONKEY)) {
                this.monkeys.get(currentMonkeyNumber).initInvalidTestTargetMonkey(content.substring(IF_FALSE_THROW_TO_MONKEY.length()));
            }
        }
        System.out.println(this.monkeys);
    }

    private void computeScore() {
        int score;

        // N rounds
        for (int i = 1; i <= NUMBER_OF_ROUNDS; i++) {
            System.out.println("### ROUND NUMBER : " + i + " ###");
            for (Monkey monkey : this.monkeys) {
                System.out.println("Monkey " + monkey.getNumber() + ":");
                Map<Integer, List<Item>> itemsToMoveByMonkeyNumber = monkey.inspectItems();
                for (int monkeyNumber : itemsToMoveByMonkeyNumber.keySet()) {
                    List<Item> itemsToMove = itemsToMoveByMonkeyNumber.get(monkeyNumber);
                    this.monkeys.get(monkeyNumber).getItems().addAll(itemsToMove);
                }
            }

            for (Monkey monkey : this.monkeys) {
                System.out.println(monkey);
            }
        }

        // extract top 2 monkeys based on inspectionCount
        score = this.monkeys.stream().map(Monkey::getInspectionCounter).sorted(Comparator.reverseOrder()).limit(2).reduce(1, (a, b) -> a * b);
        System.out.println("Level of Monkey business : " + score);
    }
}
