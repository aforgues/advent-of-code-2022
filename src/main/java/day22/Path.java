package day22;

import day08.Direction;

import java.util.ArrayList;
import java.util.List;

public record Path(List<Move> moves) {
    public static Path from(String pathToFollow) {
        List<Move> moves = new ArrayList<>();

        String subPath = pathToFollow;
        int indexOfRight;
        int indexOfLeft;

        do {
            indexOfRight = subPath.indexOf(Turning.RIGHT.getOrientation());
            indexOfLeft = subPath.indexOf(Turning.LEFT.getOrientation());

            Move move;
            if (indexOfLeft != -1 || indexOfRight != -1) {
                if (indexOfLeft != -1 && indexOfRight == -1 || indexOfLeft != -1 && indexOfLeft < indexOfRight) {
                    //System.out.println("subpath = " + subPath);
                    move = new Move(Turning.LEFT, Integer.parseInt(subPath.substring(0, indexOfLeft)));
                    subPath = subPath.substring(indexOfLeft + 1);
                } else {
                    move = new Move(Turning.RIGHT, Integer.parseInt(subPath.substring(0, indexOfRight)));
                    subPath = subPath.substring(indexOfRight + 1);
                }
                moves.add(move);
            }
        } while (indexOfLeft != -1 || indexOfRight != -1);

        // Add last move
        moves.add(new Move(null, Integer.parseInt(subPath)));

        return new Path(moves);
    }
}
