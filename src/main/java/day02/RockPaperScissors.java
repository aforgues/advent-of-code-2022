package day02;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;

public class RockPaperScissors {

    public enum Score {
        LOSE(0, "X"),
        DRAW(3, "Y"),
        WIN(6, "Z");

        private int points;
        private String choice;

        Score(int points, String choice) {
            this.points = points;
            this.choice = choice;
        }

        public int getPoints() {
            return points;
        }

        public static Score fromChoice(String choice) {
            return Arrays.stream(values()).filter(s -> s.choice.equals(choice)).findFirst().get();
        }
    }

    public enum Shape {
        ROCK("A", "X", 1),
        PAPER("B", "Y", 2),
        SCISSORS("C", "Z", 3);

        private String elveChoice;
        private String myChoice;
        private int score;

        Shape(String elveChoice, String myChoice, int score) {

            this.elveChoice = elveChoice;
            this.myChoice = myChoice;
            this.score = score;
        }

        public int getScore() {
            return score;
        }

        public int computeMyRoundScore(Shape elveChoice) {
            // same choice => it'a a draw (3)
            if (elveChoice == this)
                return Score.DRAW.getPoints();

            return switch (elveChoice) {
                // Paper defeats Rock
                case ROCK -> this == PAPER ? Score.WIN.getPoints() : Score.LOSE.getPoints();
                // Scissors defeats Paper
                case PAPER -> this == SCISSORS ? Score.WIN.getPoints() : Score.LOSE.getPoints();
                // Rock defeats Scissors
                case SCISSORS -> this == ROCK ? Score.WIN.getPoints() : Score.LOSE.getPoints();
            };
        }

        public Shape computeMyChoiceBasedOnElveChoiceFromFinalScore(Score score) {
            // It's a draw => same choice
            if (score == Score.DRAW)
                return this;

            // checking elve choice
            return switch (this) {
                case ROCK -> score == Score.WIN ? PAPER : SCISSORS;
                case PAPER -> score == Score.WIN ? SCISSORS : ROCK;
                case SCISSORS -> score == Score.WIN ? ROCK : PAPER;
            };
        }

        public static Shape fromElveChoice(String choice) {
            return Arrays.stream(values()).filter(s -> s.elveChoice.equals(choice)).findFirst().get();
        }

        public static Shape fromMyChoice(String choice) {
            return Arrays.stream(values()).filter(s -> s.myChoice.equals(choice)).findFirst().get();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day02/rockpaperscissors_strategyguide.txt";
        //String path = "src/main/resources/day02/rockpaperscissors_test.txt";
        RockPaperScissors scoreComputation = new RockPaperScissors(path);

        // First exercice
        scoreComputation.computeStrategyGuideScore();

        // second exercice
        scoreComputation.computeStrategyGuideScoreV2();
    }

    private String filePath;

    public RockPaperScissors(String filePath) {
        this.filePath = filePath;
    }

    private int computeStrategyGuideScore() throws FileNotFoundException {
        return computeStrategyGuideScoreCommon(choices -> Shape.fromMyChoice(choices[1]));
    }

    private int computeStrategyGuideScoreV2() throws FileNotFoundException {
        Function<String[], Shape> computeMyChoice = choices -> {
            Shape elveChoice = Shape.fromElveChoice(choices[0]);
            Score score = Score.fromChoice(choices[1]);
            return elveChoice.computeMyChoiceBasedOnElveChoiceFromFinalScore(score);
        };

        return computeStrategyGuideScoreCommon(computeMyChoice);
    }

    private int computeStrategyGuideScoreCommon(Function<String[], Shape> computeMyChoice) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        int totalScore = 0;
        while (scanner.hasNext()) {
            String newChoices = scanner.next();
            String[] choices = newChoices.split(" ");
            Shape elveChoice = Shape.fromElveChoice(choices[0]);
            Shape myChoice = computeMyChoice.apply(choices);
            System.out.println("elveChoice : " + elveChoice + ", myChoice : " + myChoice);
            int roundScore = myChoice.computeMyRoundScore(elveChoice);
            int roundTotalScore = myChoice.getScore() + roundScore;
            System.out.println("Round total score : " + roundScore + " (" + myChoice.getScore() + " + " + roundScore + ")");

            totalScore += roundTotalScore;
        }
        System.out.println("totalScore : " + totalScore);

        return totalScore;
    }
}
