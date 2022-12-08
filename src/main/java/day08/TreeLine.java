package day08;

import java.util.ArrayList;
import java.util.List;

public class TreeLine {
    private List<ChristmasTree> christmasTrees;

    public List<ChristmasTree> getChristmasTrees() {
        return this.christmasTrees;
    }

    public TreeLine(int rowPosition, String content) {
        this.christmasTrees = new ArrayList<>();

        int rowColumn = 0;
        for (char c : content.toCharArray()) {
            this.christmasTrees.add(new ChristmasTree(rowPosition, rowColumn, Character.getNumericValue(c)));
            rowColumn++;
        }
    }

    public int size() {
        return this.christmasTrees.size();
    }

    public ChristmasTree getChristmasTree(int column) {
        return this.christmasTrees.get(column);
    }

    @Override
    public String toString() {
        return "TreeLine{" +
                "christmasTrees=" + christmasTrees +
                '}';
    }
}
