package day08;

import java.util.*;

public class ForestMap {
    private Map<Integer, TreeLine> treeLines;

    public Map<Integer, TreeLine> getTreeLines() {
        return this.treeLines;
    }

    public ForestMap() {
        this.treeLines = new TreeMap<>();
    }

    public void addLine(int rowPosition, String content) {
        TreeLine treeLine = new TreeLine(rowPosition, content);
        this.treeLines.put(rowPosition, treeLine);
    }

    public int size() {
        return this.treeLines.size();
    }

    public List<ChristmasTree> getEdgeTrees(ChristmasTree christmasTree, Direction direction) {
        int column = christmasTree.getColumnPosition();
        int row = christmasTree.getRowPosition();

        List<ChristmasTree> edgeTrees = new ArrayList<>();
        switch (direction) {
            case LEFT -> {
                for (int i = column - 1; i >= 0; i--) {
                    edgeTrees.add(getChristmasTree(i, row));
                }
            }
            //ChristmasTree leftTree = getChristmasTree(column - 1, row);

            case RIGHT -> {
                for (int i = column + 1; i < this.getTreeLines().get(0).size(); i++) {
                    edgeTrees.add(getChristmasTree(i, row));
                }
            }
            //ChristmasTree rightTree = getChristmasTree(column + 1, row);

            case UP -> {
                for (int i = row - 1; i >= 0; i--) {
                    edgeTrees.add(getChristmasTree(column, i));
                }
            }
            //ChristmasTree upTree = getChristmasTree(column, row - 1);

            case DOWN -> {
                for (int i = row + 1; i < this.size(); i++) {
                    edgeTrees.add(getChristmasTree(column, i));
                }
            }
            //ChristmasTree downTree = getChristmasTree(column, row + 1);
        }

        return edgeTrees;
    }

    private ChristmasTree getChristmasTree(int column, int row) {
        TreeLine line = this.treeLines.get(row);
        return line.getChristmasTree(column);
    }

    @Override
    public String toString() {
        return "ForestMap{" +
                "treeLines=" + treeLines +
                '}';
    }
}
