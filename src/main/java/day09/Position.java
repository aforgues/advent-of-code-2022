package day09;

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
}
