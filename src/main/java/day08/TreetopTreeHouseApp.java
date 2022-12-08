package day08;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class TreetopTreeHouseApp {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day08/trees_map.txt";
        //String path = "src/main/resources/day08/trees_map_test.txt";
        TreetopTreeHouseApp app = new TreetopTreeHouseApp(path);

        // First exercice
        app.computeScoreV1();

        // second exercice
        app.computeScoreV2();
    }


    private final String filePath;
    private ForestMap forestMap;

    public TreetopTreeHouseApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.forestMap = new ForestMap();
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        int rowPosition = 0;
        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            forestMap.addLine(rowPosition, content);
            rowPosition++;
        }
        System.out.println(this.forestMap);
    }

    private void computeScoreV1() {
        int score = 0;

        for (TreeLine treeLine : this.forestMap.getTreeLines().values()) {
            for (ChristmasTree christmasTree : treeLine.getChristmasTrees()) {
                System.out.println(christmasTree);

                // if on edge : visible
                if (christmasTree.isOnTheEdge(this.forestMap.size(), treeLine.size())) {
                    score++;
                    System.out.println("is on edge");
                    continue;
                }

                // get edge trees in all directions
                boolean isVisible = false;
                for (Direction direction : Direction.values()) {
                    List<ChristmasTree> edgeTrees = this.forestMap.getEdgeTrees(christmasTree, direction);
                    System.out.println("Edge trees in " + direction.name() + " : " + edgeTrees);
                    boolean isSmallestIntheDirection = edgeTrees.stream().map(ChristmasTree::getSize).allMatch(size -> size < christmasTree.getSize());
                    System.out.println("is smallest ? " + isSmallestIntheDirection);
                    isVisible |= isSmallestIntheDirection;
                }
                if (isVisible)
                    score++;
            }
        }

        System.out.println("Final score : " + score);
    }

    private void computeScoreV2() {

        //System.out.println("Final score : " + score);
    }
}
