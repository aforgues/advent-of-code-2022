package day04;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SectionAssignmentChecker {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day04/section_assignment_pairs.txt";
        //String path = "src/main/resources/day04/section_assignment_pairs_test.txt";
        SectionAssignmentChecker app = new SectionAssignmentChecker(path);

        // First exercice
        app.computeScore();

        // second exercice
        //app.computeScoreV2();
    }


    private final String filePath;

    public SectionAssignmentChecker(String filePath) {
        this.filePath = filePath;
    }

    private void computeScore() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        int totalScore = 0;
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            AssignmentsPair pair = new AssignmentsPair(content);
            System.out.println(pair);
            if (pair.isOneRangeFullyContainsTheOtherOne())
                totalScore ++;
        }
        System.out.println("totalScore : " + totalScore);
    }

    /*private void computeScoreV2() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        int totalScore = 0;
        while (scanner.hasNext()) {
            String content = scanner.next();

            //totalScore +=
        }
        System.out.println("totalScore : " + totalScore);
    }*/
}
