package utils;

import java.util.Arrays;

public enum Move {
    UP("^"),
    DOWN("v"),
    LEFT("<"),
    RIGHT(">");

    private final String sign;

    public String getSign() {
        return sign;
    }

    Move(String sign) {
        this.sign = sign;
    }

    public static Move from(String sign) {
        return Arrays.stream(Move.values()).filter(move -> move.sign.equals(sign)).findFirst().orElse(null);
    }

    public static Move from(Position from, Position to) {
        if (to.x() == from.x()) {
            if (to.y() > from.y())
                return DOWN;
            return UP;
        }
        if (to.y() == from.y()) {
            if (to.x() > from.x())
                return RIGHT;
            return LEFT;
        }
        throw new IllegalArgumentException("Unable to define Move base on positions from " + from + " and to " + to);
    }
}
