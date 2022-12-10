package day10;

public enum State {
    LIT("#"),
    DARK(".");

    private final String value;

    State(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
