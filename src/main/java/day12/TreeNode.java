package day12;

import day09.Position;

import java.util.ArrayList;
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

    public boolean isAtTargetPosition() {
        return this.hillMap.isAtTargetPosition();
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

    public List<Square> getNextEligibleSquares() {
        return this.hillMap.getNextEligibleSquares();
    }
}
