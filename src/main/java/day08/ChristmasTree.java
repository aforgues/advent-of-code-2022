package day08;

public class ChristmasTree {
    private final int rowPosition;
    private final int columnPosition;
    private final int size;

    public int getRowPosition() {
        return rowPosition;
    }
    public int getColumnPosition() {
        return columnPosition;
    }
    public int getSize() {
        return size;
    }

    public ChristmasTree(int rowPosition, int columnPosition, int size) {
        this.rowPosition = rowPosition;
        this.columnPosition = columnPosition;
        this.size = size;
    }

    public boolean isOnTheEdge(int numberOfTreeLines, int numberOfTreesOnALine) {
        return (this.rowPosition == 0 || this.rowPosition == numberOfTreeLines - 1)
                || (this.columnPosition == 0 || this.columnPosition == numberOfTreesOnALine - 1);
    }

    @Override
    public String toString() {
        return "ChristmasTree{" +
                "rowPosition=" + rowPosition +
                ", columnPosition=" + columnPosition +
                ", size=" + size +
                '}';
    }
}
