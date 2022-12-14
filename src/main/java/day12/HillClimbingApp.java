package day12;

import day09.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class HillClimbingApp {

    public static void main(String[] args) throws FileNotFoundException {
        //String path = "src/main/resources/day12/heightmap.txt";
        String path = "src/main/resources/day12/heightmap_test.txt";
        HillClimbingApp app = new HillClimbingApp(path);

        // First exercise
        app.computeScore();
    }

    private final String filePath;
    private HillMap hillMap;

    public HillClimbingApp(String filePath) {
        this.filePath = filePath;
        hillMap = new HillMap();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        int column = 0;
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            for (int row=0; row < content.length(); row++) {
                hillMap.addSquare(new Position(row, column), content.charAt(row));
            }
            column++;
        }
        System.out.println(this.hillMap);
    }

    private void computeScore() throws FileNotFoundException {
        this.parseFile();

        TreeNode root = new TreeNode(this.hillMap, 0, null);

        int score = findMaxElevationSquare(root);

        System.out.println("Score : " + score);
    }

    private int findMaxElevationSquare(TreeNode currentTreeNode) {
        int nbMinStepsToTarget = Integer.MAX_VALUE;
        //System.out.println("CurrentTreeNode = " + currentTreeNode);

        if (currentTreeNode.isAtTargetPosition()) {
            nbMinStepsToTarget = currentTreeNode.getDepth();
        }
        for (Square nextEligibleSquare : currentTreeNode.getNextEligibleSquares()) {
            TreeNode child = currentTreeNode.addChild(nextEligibleSquare.position());
            nbMinStepsToTarget = Math.min(nbMinStepsToTarget, findMaxElevationSquare(child));
        }
        return nbMinStepsToTarget;
    }
}
