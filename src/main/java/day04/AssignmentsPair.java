package day04;

import java.util.List;

public class AssignmentsPair {
    private final Assignment firstElveAssignment;
    private final Assignment secondElveAssignment;

    public AssignmentsPair(String content) {
        String[] assignmentsAsString = content.split(",");
        this.firstElveAssignment = new Assignment(assignmentsAsString[0]);
        this.secondElveAssignment = new Assignment(assignmentsAsString[1]);
    }

    public boolean isOneRangeFullyContainsTheOtherOne() {
        List<Integer> firstElveRange = this.firstElveAssignment.getFullRange();
        List<Integer> secondElveRange = this.secondElveAssignment.getFullRange();

        return firstElveRange.containsAll(secondElveRange) || secondElveRange.containsAll(firstElveRange);
    }

    @Override
    public String toString() {
        return "AssignmentsPair{" +
                "firstElveAssignment=" + firstElveAssignment +
                ", secondElveAssignment=" + secondElveAssignment +
                '}';
    }
}
