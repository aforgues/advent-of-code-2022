package day11;

import java.util.Arrays;

public enum OperationType {
    MULTIPLY("*"),
    PLUS("+");

    private final String operator;

    OperationType(String operator) {
        this.operator = operator;
    }

    public static OperationType from(String operator) {
        return Arrays.stream(OperationType.values()).filter(type -> type.operator.equals(operator)).findFirst().get();
    }
}
