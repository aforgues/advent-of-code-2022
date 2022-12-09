package day09;

import day08.Direction;

public class Move {
    private final Direction direction;
    private final int nbStep;


    public Direction getDirection() {
        return direction;
    }

    public int getNbStep() {
        return nbStep;
    }

    public Move(Direction direction, int nbStep) {
        this.direction = direction;
        this.nbStep = nbStep;
    }

    public Move(String content) {
        String[] move = content.split(" ");
        this.direction = switch (move[0]) {
            case "U" -> Direction.UP;
            case "D" -> Direction.DOWN;
            case "L" -> Direction.LEFT;
            case "R" -> Direction.RIGHT;
            default -> throw new IllegalArgumentException("Unknown direction : " + move[0]);
        };
        this.nbStep = Integer.parseInt(move[1]);
    }

    @Override
    public String toString() {
        return "Move{" +
                "direction=" + direction +
                ", nbStep=" + nbStep +
                '}';
    }
}
