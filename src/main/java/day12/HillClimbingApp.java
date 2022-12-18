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

        //int score = findMaxElevationSquareDFS(root);
        int score = findMaxElevationSquareBFS(root);
        //int score = findMaxElevationSquareBFSv2(root);

        System.out.println("Score : " + score);
    }

    private int findMaxElevationSquareDFS(TreeNode currentTreeNode) {
        int nbMinStepsToTarget = Integer.MAX_VALUE;
        //System.out.println("CurrentTreeNode = " + currentTreeNode);

        if (currentTreeNode.isAtTargetPosition()) {
            nbMinStepsToTarget = currentTreeNode.getDepth();
        }
        for (Square nextEligibleSquare : currentTreeNode.getNextEligibleSquares()) {
            TreeNode child = currentTreeNode.addChild(nextEligibleSquare.position());
            nbMinStepsToTarget = Math.min(nbMinStepsToTarget, findMaxElevationSquareDFS(child));
        }
        return nbMinStepsToTarget;
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
    private int findMaxElevationSquareBFS(TreeNode currentTreeNode) {
        Queue<TreeNode> queue = new LinkedList<>();

        /*
        // Trying to maintain a global map of explored positions instead of local ones through Square::isVisited flag
        Map<Position, Boolean> exploredPosition = new HashMap<>();
        Position exploringPosition = currentTreeNode.getCurrentSquare().position();
        exploredPosition.put(exploringPosition, true);
        */

        queue.add(currentTreeNode);
        int count = 0;
        while (! queue.isEmpty()) {
            TreeNode node = queue.poll();
            //System.out.println(count++ + " => CurrentTreeNode = " + node);
            System.out.println(count++ + " :: Handling node with current square " + node.getCurrentSquare() + " at tree depth " + node.getDepth() + " with queue size of " + queue.size());
            node.displayPathInConsole();
            if (node.isAtTargetPosition()) {
                System.out.println("Shortest path is in " + node.getDepth() + " steps");
                node.displayPathInConsole();
                return node.getDepth();
            }
            List<Square> nextEligibleSquares = node.getNextEligibleSquares();
            System.out.println("Adding " + nextEligibleSquares.size() + " new children to queue : " + nextEligibleSquares.stream().map(Square::position).toList());
            for (Square nextEligibleSquare : nextEligibleSquares) {
                //if (exploredPosition.get(nextEligibleSquare.position()) == null) {
                  //  exploredPosition.put(nextEligibleSquare.position(), true);
                    TreeNode child = node.addChild(nextEligibleSquare.position());
                    queue.add(child);
                //}
            }
        }
        return -1;
    }

    private int findMaxElevationSquareBFSv2(TreeNode currentTreeNode) {
        Graph graph = new Graph();
        int maxColumnIndex = this.hillMap.getMaxColumnIndex();
        int maxRowIndex = this.hillMap.getMaxRowIndex();

        for (Square square : this.hillMap.getSquares()) {
            //System.out.println("Handling : " + square.position());
            for (Square adj : this.hillMap.getNextEligibleSquares(square.position(), maxColumnIndex, maxRowIndex)) {
                //System.out.println("Adding edge to pos " + square.position() + " : " + adj.position());
                graph.addEdge(square.position(), adj.position());
            }
        }
        List<Position> path = graph.BFS(currentTreeNode.getCurrentSquare().position(), this.hillMap.getTargetPosition());
        System.out.println("Path size is " + path.size());

        return computeMinStepFromTargetToStartingPoint(graph, path.get(path.size() - 1));
    }

    private int computeMinStepFromTargetToStartingPoint(Graph graph, Position position) {
        int minStepFromTargetToStartingPoint = Integer.MAX_VALUE;
        System.out.println(position + " -> parents : " + graph.getParentPositions(position));
        if (position.equals(this.hillMap.getCurrentSquare().position())) {
            return minStepFromTargetToStartingPoint;
        }
        for (Position parent : graph.getParentPositions(position)) {
            if (graph.getParentPositions(position).contains(parent))
                continue;
            minStepFromTargetToStartingPoint = Math.min(minStepFromTargetToStartingPoint, computeMinStepFromTargetToStartingPoint(graph, parent));
        }
        return minStepFromTargetToStartingPoint;
    }
}
