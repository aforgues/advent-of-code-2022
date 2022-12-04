package day04;

import java.util.ArrayList;
import java.util.List;

public class Assignment {
    private final int firstSectionId;
    private final int lastSectionId;

    public Assignment(String sections) {
        String[] sectionsArr = sections.split("-");
        this.firstSectionId = Integer.parseInt(sectionsArr[0]);
        this.lastSectionId = Integer.parseInt(sectionsArr[1]);
    }

    public List<Integer> getFullRange() {
        List<Integer> range = new ArrayList<>();
        for (int i = this.firstSectionId; i<= lastSectionId; i++) {
            range.add(i);
        }
        return range;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "firstSectionId=" + firstSectionId +
                ", lastSectionId=" + lastSectionId +
                '}';
    }
}
