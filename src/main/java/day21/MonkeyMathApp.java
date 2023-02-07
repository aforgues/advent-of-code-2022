package day21;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class MonkeyMathApp {

    private static final boolean TEST = false;

    public static void main(String[] args) throws FileNotFoundException {
        String path = TEST ? "src/main/resources/day21/puzzle_input_test.txt" : "src/main/resources/day21/puzzle_input.txt";
        MonkeyMathApp app = new MonkeyMathApp(path);

        // First part
        app.computeScore();

        // Second part
        app.computeScoreV2();
    }

    private final String filePath;
    private final Map<String, MonkeyJob> monkeyJobByMonkeyNames;

    public MonkeyMathApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        monkeyJobByMonkeyNames = new HashMap<>();
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            monkeyJobByMonkeyNames.put(content.split(":")[0], MonkeyJob.from(content.split(": ")[1]));
        }
    }

    private void computeScore() {
        Instant start = Instant.now();

        // Get root job
        MonkeyJob rootJob = this.monkeyJobByMonkeyNames.get("root");

        long score = rootJob.computeNumber(this.monkeyJobByMonkeyNames, null);

        Instant end = Instant.now();

        System.out.println("Score : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private final static String MY_MONKEY_NAME = "humn";

    private void computeScoreV2() {
        Instant start = Instant.now();

        // Get root job
        MonkeyJob rootJob = this.monkeyJobByMonkeyNames.get("root");

        long score = 0;

        Optional<MathOperation> mathOperation = rootJob.getMathOperation();
        if (mathOperation.isPresent()) {
            MonkeyJob leftRootMonkeyJob = this.monkeyJobByMonkeyNames.get(mathOperation.get().getLeftOperand());
            MonkeyJob rightRootMonkeyJob = this.monkeyJobByMonkeyNames.get(mathOperation.get().getRightOperand());

            Long leftScore = null;
            try {
                leftScore = leftRootMonkeyJob.computeNumber(this.monkeyJobByMonkeyNames, MY_MONKEY_NAME);
                //System.out.println("Left : " + leftScore);
            }
            catch (RuntimeException e) {
                //System.out.println("Left root monkey yell at me !!");
            }
            Long rightScore = null;
            try {
                rightScore = rightRootMonkeyJob.computeNumber(this.monkeyJobByMonkeyNames, MY_MONKEY_NAME);
                //System.out.println("Right : " + rightScore);
            }
            catch (RuntimeException e) {
                //System.out.println("Right root monkey yell at me !!");
            }

            if (leftScore == null) {
                System.out.println("Equation to solve : " + mathOperation.get().getLeftOperand() + " = " + rightScore);
                score = reverseComputeMySpecificNumber(mathOperation.get().getLeftOperand(), rightScore);
            }

            if (rightScore == null) {
                System.out.println("Equation to solve : " + leftScore + " = " + mathOperation.get().getRightOperand());
                score = reverseComputeMySpecificNumber(mathOperation.get().getRightOperand(), leftScore);
            }
        }

        Instant end = Instant.now();

        System.out.println("Score part 2 : " + score + " in " + (end.toEpochMilli() - start.toEpochMilli()) + "ms");
    }

    private long reverseComputeMySpecificNumber(String monkeyName, Long currentScore) {
        // Get monkey job
        MonkeyJob job = this.monkeyJobByMonkeyNames.get(monkeyName);

        //System.out.println("Analysing monkey " + monkeyName);

        Optional<MathOperation> mathOperation = job.getMathOperation();
        if (mathOperation.isPresent()) {
            MonkeyJob leftMonkeyJob = this.monkeyJobByMonkeyNames.get(mathOperation.get().getLeftOperand());
            MonkeyJob rightMonkeyJob = this.monkeyJobByMonkeyNames.get(mathOperation.get().getRightOperand());

            Long leftScore;
            try {
                if (mathOperation.get().getLeftOperand().equals(MY_MONKEY_NAME))
                    throw new RuntimeException("It is me !!");
                leftScore = leftMonkeyJob.computeNumber(this.monkeyJobByMonkeyNames, MY_MONKEY_NAME);
                //System.out.println("Left : " + leftScore);

                System.out.println("SubEquation to solve : " + currentScore + " = " + leftScore + " " + mathOperation.get().getType().getOperator() + " " + mathOperation.get().getRightOperand());

                // inverse operation type
                currentScore = switch (mathOperation.get().getType()) {
                    case PLUS -> currentScore - leftScore;
                    case DIVIDE -> leftScore / currentScore;
                    case MINUS -> leftScore - currentScore;
                    case MULTIPLY -> currentScore / leftScore;
                };
                return reverseComputeMySpecificNumber(mathOperation.get().getRightOperand(), currentScore);
            }
            catch (RuntimeException e) {
                //System.out.println("Left monkey yell at me !!");
            }

            Long rightScore;
            try {
                if (mathOperation.get().getRightOperand().equals(MY_MONKEY_NAME))
                    throw new RuntimeException("It is me !!");
                rightScore = rightMonkeyJob.computeNumber(this.monkeyJobByMonkeyNames, MY_MONKEY_NAME);
                //System.out.println("Right : " + rightScore);

                System.out.println("SubEquation to solve : " + currentScore + " = " + mathOperation.get().getLeftOperand() + " " + mathOperation.get().getType().getOperator() + " " + rightScore);

                // inverse operation type
                currentScore = switch (mathOperation.get().getType()) {
                    case DIVIDE -> currentScore * rightScore;
                    case MINUS -> currentScore + rightScore;
                    case PLUS -> currentScore - rightScore;
                    case MULTIPLY -> currentScore / rightScore;
                };
                return reverseComputeMySpecificNumber(mathOperation.get().getLeftOperand(), currentScore);
            }
            catch (RuntimeException e) {
                //System.out.println("Right root monkey yell at me !!");
            }
        }
        return currentScore;
    }
}
