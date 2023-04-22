package day24;

import utils.Move;
import utils.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public record TreeNode(TreeNode parent, int currentMinute, Position currentExpeditionPosition) {

    public TreeNode addChild(Position nextPositionToExplore) {
        return new TreeNode(this, this.currentMinute + 1, nextPositionToExplore);
    }

    public boolean hasReachedValleyExit(Valley valley) {
        return this.currentExpeditionPosition.equals(new Position(valley.getMaxColumnInValley() - 1, valley.getMaxRowInValley()));
    }

    public List<Position> getPotentialNextExpeditionPositions(List<Blizzard> nextBlizzards, Valley valley) {
        List<Position> potentialNextExpeditionPositions = new ArrayList<>();

        // Move up, down, left, right
        for (Move move : Move.values()) {
            Position nextExpeditionPosition = this.currentExpeditionPosition.moveTo(move);

            if (isEligibleToBeAnExpeditionPosition(nextExpeditionPosition, nextBlizzards, valley)) {
                potentialNextExpeditionPositions.add(nextExpeditionPosition);
            }
        }

        // Wait in place
        if (isEligibleToBeAnExpeditionPosition(this.currentExpeditionPosition, nextBlizzards, valley)) {
            potentialNextExpeditionPositions.add(this.currentExpeditionPosition);
        }

        return potentialNextExpeditionPositions;
    }

    // No collision with any blizzard or any wall of the valley
    private static boolean isEligibleToBeAnExpeditionPosition(Position nextExpeditionPosition, List<Blizzard> nextBlizzards, Valley valley) {
        return (nextBlizzards.stream().noneMatch(blizzard -> blizzard.currentPosition().equals(nextExpeditionPosition))
                && valley.isPositionInsideTheValley(nextExpeditionPosition));
    }

    public List<Position> getExpeditionPath() {
        List<Position> path = new ArrayList<>();
        path.add(this.currentExpeditionPosition);
        TreeNode myParent = this.parent;
        while (myParent != null) {
            path.add(myParent.currentExpeditionPosition);
            myParent = myParent.parent;
        }

        Collections.reverse(path);
        return path;
    }

    public void displayValleyInConsole(Valley valley, Map<Integer, List<Blizzard>> blizzardsByMinute) {
        if (parent == null) {
            System.out.println("Initial state:");
        } else {
            String move = parent.currentExpeditionPosition().equals(this.currentExpeditionPosition) ? ", wait:" : ", move " + Move.from(parent.currentExpeditionPosition(), this.currentExpeditionPosition).name().toLowerCase();
            System.out.println("Minute " + currentMinute + move);
        }
        valley.displayInConsole(blizzardsByMinute.get(this.currentMinute), this.currentExpeditionPosition);
    }

    public void displayFullExpeditionInConsole(Valley valley, Map<Integer, List<Blizzard>> blizzardsByMinute) {
        List<TreeNode> path = new ArrayList<>();
        path.add(this);
        TreeNode myParent = this.parent;
        while (myParent != null) {
            path.add(myParent);
            myParent = myParent.parent;
        }

        Collections.reverse(path);
        path.forEach(treeNode -> treeNode.displayValleyInConsole(valley, blizzardsByMinute));
    }

    public NodeKey getNodeKey() {
        return new NodeKey(this.currentMinute, this.currentExpeditionPosition);
    }
}
