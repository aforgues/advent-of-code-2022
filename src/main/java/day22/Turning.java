package day22;

public enum Turning {
    LEFT ("L"),
    RIGHT("R");

    private final String orientation;

    public String getOrientation() {
        return orientation;
    }

    Turning(String orientation) {
        this.orientation = orientation;
    }
}
