package day12;

import utils.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeNode {
    private final Position currentPosition;
    private final int depth;
    private final TreeNode parent;
    private List<TreeNode> children;

    public int getDepth() {
        return depth;
    }
    public Position getCurrentPosition() {
        return currentPosition;
    }

    public TreeNode(Position currentPosition, int depth, TreeNode parent) {
        this.currentPosition = currentPosition;
        this.depth = depth;
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    public TreeNode addChild(Position nextEligiblePosition) {
        TreeNode child = new TreeNode(nextEligiblePosition, this.depth + 1, this);
        this.children.add(child);
        return child;
    }



    private List<Position> computeTreePath(List<Position> path) {
        path.add(this.currentPosition);
        if (this.parent != null) {
            path = this.parent.computeTreePath(path);
        }
        return path;
    }

    public List<Position> getPath() {
        List<Position> path = new ArrayList<>();

        path = computeTreePath(path);
        Collections.reverse(path);

        return path;
    }

    public String displayTree(HillMap hillMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ".repeat(depth));
        sb.append(currentPosition).append(" at elevation=").append(hillMap.getSquareByPosition(currentPosition).elevation()).append("\n");
        for (TreeNode child : children) {
            sb.append(child.displayTree(hillMap));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "currentPosition=" + currentPosition +
                ", depth=" + depth +
                ", parentPosition=" + (parent != null ? parent.currentPosition : "no parent") +
                ", children=" + children +
                '}';
    }
}
