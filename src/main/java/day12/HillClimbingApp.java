package day12;

import utils.Move;
import utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class HillClimbingApp {

    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day12/heightmap.txt";
        //String path = "src/main/resources/day12/heightmap_test.txt";
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

        TreeNode root = new TreeNode(this.hillMap.getStartingPosition(), 0, null);

        //int score = findMaxElevationSquareDFS(root);
        int score = findMaxElevationSquareBFS(root);

        System.out.println("Score : " + score);
    }

    private int findMaxElevationSquareDFS(TreeNode currentTreeNode) {
        int nbMinStepsToTarget = Integer.MAX_VALUE;
        //System.out.println("CurrentTreeNode = " + currentTreeNode);

        if (isAtTargetPosition(currentTreeNode.getCurrentPosition())) {
            nbMinStepsToTarget = currentTreeNode.getDepth();
        }
        for (Square nextEligibleSquare : this.hillMap.getNextEligibleSquares(currentTreeNode.getCurrentPosition())) {
            if (!currentTreeNode.getPath().contains(nextEligibleSquare.position())) {
                TreeNode child = currentTreeNode.addChild(nextEligibleSquare.position());
                nbMinStepsToTarget = Math.min(nbMinStepsToTarget, findMaxElevationSquareDFS(child));
            }
        }
        return nbMinStepsToTarget;
    }

    private boolean isAtTargetPosition(Position position) {
        return this.hillMap.isAtTargetPosition(position);
    }

    /*
 1  procedure BFS(G, root) is
 2      let Q be a queue
 3      label root as explored
 4      Q.enqueue(root)
 5      while Q is not empty do
 6          v := Q.dequeue()
 7          if v is the goal then
 8              return v
 9          for all edges from v to w in G.adjacentEdges(v) do
10              if w is not labeled as explored then
11                  label w as explored
12                  w.parent := v
13                  Q.enqueue(w)
     */
    private int findMaxElevationSquareBFS(TreeNode rootTreeNode) {
        Queue<TreeNode> queue = new LinkedList<>();


        // Trying to maintain a global map of explored positions instead of local ones through Node::getPath
        //Map<Position, Boolean> exploredPosition = new HashMap<>();
        //Position exploringPosition = rootTreeNode.getCurrentPosition();
        //exploredPosition.put(exploringPosition, true);


        queue.add(rootTreeNode);
        int count = 0;
        while (! queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.println(count++ + " :: Handling node at " + node.getCurrentPosition() + " at tree depth " + node.getDepth() + " with queue size of " + queue.size());
            //System.out.println("Tree from root is :\n" + rootTreeNode.displayTree(this.hillMap));
            //displayPathInConsole(node);
            if (isAtTargetPosition(node.getCurrentPosition())) {
                System.out.println("Shortest path is in " + node.getDepth() + " steps");
                displayPathInConsole(node);
                return node.getDepth();
            }
            List<Square> nextEligibleSquares = this.hillMap.getNextEligibleSquares(node.getCurrentPosition());
            //System.out.println("Adding " + nextEligibleSquares.size() + " new children to queue : " + nextEligibleSquares.stream().map(Square::position).toList());
            for (Square nextEligibleSquare : nextEligibleSquares) {
                //if (exploredPosition.get(nextEligibleSquare.position()) == null) {
                //    exploredPosition.put(nextEligibleSquare.position(), true);
                    if (!node.getPath().contains(nextEligibleSquare.position())) {
                        TreeNode child = node.addChild(nextEligibleSquare.position());
                        queue.add(child);
                    }
                //}
            }
        }
        return -1;
    }

    public void displayPathInConsole(TreeNode node) {
        //System.out.println("Depth : " + node.getDepth());
        //System.out.println(this.hillMap.getStartingPosition());

        List<Position> path = node.getPath();
        //System.out.println("Path size : " + path.size());
        //System.out.println("Path : " + path);

        StringBuilder display = new StringBuilder();
        for (int y = 0; y <= this.hillMap.getMaxRowIndex(); y++) {
            for (int x = 0; x <= this.hillMap.getMaxColumnIndex(); x++) {
                int index = path.indexOf(new Position(x, y));
                //System.out.println("x,y=" + x + "," + y + " => index in path : " + index);
                if (index == -1) {
                    display.append(".");
                }
                else {
                    Position p = path.get(index);
                    if (index == 0) {
                        display.append("S");
                    }
                    else if (p.equals(this.hillMap.getTargetPosition())) {
                        display.append("E");
                    }
                    else if (index == path.size() - 1) {
                        display.append("?");
                    }
                    else {
                        Move move = Move.from(p, path.get(index + 1));
                        display.append(move.getSign());
                    }
                }
            }
            display.append("\n");
        }
        System.out.println(display);
    }
}
