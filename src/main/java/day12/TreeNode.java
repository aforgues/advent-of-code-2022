package day12;

import day09.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeNode {
    private final HillMap hillMap;
    private final int depth;
    private final TreeNode parent;
    private List<TreeNode> children;

    public int getDepth() {
        return depth;
    }

    public TreeNode(HillMap hillMap, int depth, TreeNode parent) {
        this.hillMap = hillMap;
        this.depth = depth;
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    public TreeNode addChild(Position nextEligiblePosition) {
        HillMap childHillMap = new HillMap(this.hillMap, nextEligiblePosition);
        TreeNode child = new TreeNode(childHillMap, this.depth + 1, this);
        this.children.add(child);
        return child;
    }

    public Square getCurrentSquare() {
        return this.hillMap.getCurrentSquare();
    }

    public boolean isAtTargetPosition() {
        return this.hillMap.isAtTargetPosition();
    }

    public List<Square> getNextEligibleSquares() {
        return this.hillMap.getNextEligibleSquares();
    }

    public void displayPathInConsole() {
        //System.out.println("Depth : " + this.depth);
        //System.out.println(this.hillMap.getCurrentSquare().position());

        List<Position> path = new ArrayList<>();

        path = computeTreePath(path);
        Collections.reverse(path);
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

    private List<Position> computeTreePath(List<Position> path) {
        path.add(this.hillMap.getCurrentSquare().position());
        if (this.parent != null) {
            path = this.parent.computeTreePath(path);
        }
        return path;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "hillMap=" + hillMap.getCurrentSquare() +
                ", depth=" + depth +
                ", parentCurrentSquare=" + (parent != null ? parent.hillMap.getCurrentSquare().toString() : "no parent") +
                ", children=" + children +
                '}';
    }
}
