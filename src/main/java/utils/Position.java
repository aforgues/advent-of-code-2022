package utils;

public record Position(int x, int y) {

    // to build a position from a string as "x,y"
    public static Position getInstance(String content) {
        String[] positionAsStringArray = content.split(",");
        return new Position(Integer.parseInt(positionAsStringArray[0]),
                            Integer.parseInt(positionAsStringArray[1]));
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Position adjacentPosition(Direction nextDirection) {
        return switch (nextDirection) {
            case NORTH -> new Position(x, y - 1);
            case NORTH_EAST -> new Position(x + 1, y - 1);
            case EAST -> new Position(x + 1, y);
            case SOUTH_EAST -> new Position(x + 1, y + 1);
            case SOUTH -> new Position(x, y + 1);
            case SOUTH_WEST -> new Position(x - 1, y + 1);
            case WEST -> new Position(x - 1, y);
            case NORTH_WEST -> new Position(x - 1, y - 1);
        };
    }

    public Position moveTo(Move moveType) {
        return switch (moveType) {
            case UP -> new Position(x, y - 1);
            case RIGHT -> new Position(x + 1, y);
            case DOWN -> new Position(x, y + 1);
            case LEFT -> new Position(x - 1, y);
        };
    }
}
