package day22;

import day08.Direction;

import java.util.ArrayList;
import java.util.List;

public record Path(List<Move> moves) {
    public static Path from(String pathToFollow) {
        List<Move> moves = new ArrayList<>();

        String subPath = pathToFollow;
        Direction currentDirection = Direction.RIGHT;
        int indexOfRight;
        int indexOfLeft;

        do {
            indexOfRight = subPath.indexOf(Turning.RIGHT.getOrientation());
            indexOfLeft = subPath.indexOf(Turning.LEFT.getOrientation());

            Move move;
            if (indexOfLeft != -1 || indexOfRight != -1) {
                if (indexOfLeft != -1 && indexOfRight == -1 || indexOfLeft != -1 && indexOfLeft < indexOfRight) {
                    //System.out.println("subpath = " + subPath);
                    move = new Move(currentDirection, Integer.parseInt(subPath.substring(0, indexOfLeft)));
                    currentDirection = computeNextDirection(currentDirection, Turning.LEFT);
                    subPath = subPath.substring(indexOfLeft + 1);
                } else {
                    move = new Move(currentDirection, Integer.parseInt(subPath.substring(0, indexOfRight)));
                    currentDirection = computeNextDirection(currentDirection, Turning.RIGHT);
                    subPath = subPath.substring(indexOfRight + 1);
                }
                moves.add(move);
            }
        } while (indexOfLeft != -1 || indexOfRight != -1);

        // Add last move
        moves.add(new Move(currentDirection, Integer.parseInt(subPath)));

        return new Path(moves);
    }

    private static Direction computeNextDirection(Direction currentDirection, Turning orientation) {
        return switch (currentDirection) {
            case RIGHT -> (orientation == Turning.RIGHT ? Direction.DOWN : Direction.UP);
            case DOWN  -> (orientation == Turning.RIGHT ? Direction.LEFT : Direction.RIGHT);
            case LEFT  -> (orientation == Turning.RIGHT ? Direction.UP : Direction.DOWN);
            case UP    -> (orientation == Turning.RIGHT ? Direction.RIGHT : Direction.LEFT);
        };
    }
}
