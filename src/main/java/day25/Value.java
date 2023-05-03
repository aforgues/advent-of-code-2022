package day25;

import java.util.Arrays;

public enum Value {
    TWO("2", 2),
    ONE("1", 1),
    ZERO("0", 0),
    MINUS("-", -1),
    DOUBLE_MINUS("=", -2);

    private final String digit;
    private final int finalValue;

    public int finalValue() {
        return this.finalValue;
    }

    Value(String digit, int finalValue) {
        this.digit = digit;
        this.finalValue = finalValue;
    }

    public static Value fromDigit(String digit) {
        return Arrays.stream(Value.values()).filter(value -> value.digit.equals(digit)).findFirst().orElseThrow(() -> new IllegalArgumentException("Unable to extract Value from digit " + digit));
    }

    public String digit() {
        return this.digit;
    }
}
