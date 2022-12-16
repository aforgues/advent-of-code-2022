package day12;

import day09.Position;

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
        throw new IllegalArgumentException("Unable to define Move base on postions from " + from + " and to " + to);
    }
}
