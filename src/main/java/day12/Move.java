package day12;

public enum Move {
    UP("^"),
    DOWN("v"),
    LEFT("<"),
    RIGHT(">");

    private final String sign;

    Move(String sign) {
        this.sign = sign;
    }
}
