package utils;

public enum Direction {
    NORTH("N"),
    NORTH_EAST("NE"),
    EAST("E"),
    SOUTH_EAST("SE"),
    SOUTH("S"),
    SOUTH_WEST("SW"),
    WEST("W"),
    NORTH_WEST("NW");

    private final String abbr;

    Direction(String abbr) {
        this.abbr = abbr;
    }
}
