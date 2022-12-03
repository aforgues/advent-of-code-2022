package day03;

import day02.RockPaperScissors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RuckSacksParser {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day03/rucksacks.txt";
        //String path = "src/main/resources/day03/rucksacks_test.txt";
        RuckSacksParser app = new RuckSacksParser(path);

        // First exercice
        app.computeScore();

        // second exercice
        //scoreComputation.computeStrategyGuideScoreV2();
    }


    private String filePath;

    public RuckSacksParser(String filePath) {
        this.filePath = filePath;
    }

    private void computeScore() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        int totalScore = 0;
        while (scanner.hasNext()) {
            String content = scanner.next();
            RuckSack ruckSack = new RuckSack(content);
            System.out.println(ruckSack);
            Character same = ruckSack.findCommonItem();
            totalScore += ItemPriority.fromItemType(same).getPriority();
        }
        System.out.println("totalScore : " + totalScore);
    }
}
