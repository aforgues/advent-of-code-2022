package day09;

import day08.Direction;
import utils.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class BridgeMap {
    private List<Position> ropeTailPositions;
    private final int numberOfTailKnots;
    private Position ropeHeadPosition;
    private TailPath tailPath;

    public int getUniqueTailPathCount() {
        return new HashSet<>(this.tailPath.getPositions()).size();
    }

    public BridgeMap(Position ropeTailPosition, int numberOfTailKnots, Position ropeHeadPosition) {
        this.numberOfTailKnots = numberOfTailKnots;
        this.ropeTailPositions = new ArrayList<>(numberOfTailKnots);
        for (int i = 0; i < numberOfTailKnots; i++) {
            this.ropeTailPositions.add(ropeTailPosition);
        }
        this.ropeHeadPosition = ropeHeadPosition;
        this.tailPath = new TailPath(this.getLastTailPosition());
    }

    public Position getLastTailPosition() {
        return this.ropeTailPositions.get(this.numberOfTailKnots - 1);
    }

    public void printMapInConsole() {
        // FIXME : not displaying negative tailPath positions ...
        int maxX = Math.max(this.tailPath.getPositions().stream().map(Position::x).max(Comparator.comparingInt(i -> i)).get(), this.ropeHeadPosition.x());
        int maxY = Math.max(this.tailPath.getPositions().stream().map(Position::y).max(Comparator.comparingInt(i -> i)).get(), this.ropeHeadPosition.y());
        for (int y = maxY; y >=0; y--) {
            for (int x = 0; x <= maxX; x++) {
                Position currentPosition = new Position(x, y);
                if (ropeHeadPosition.equals(currentPosition)) {
                    System.out.print("H");
                }
                else if (ropeTailPositions.size() > 0 && ropeTailPositions.get(0).equals(currentPosition)) {
                    System.out.print("1");
                }
                else if (ropeTailPositions.size() > 1 && ropeTailPositions.get(1).equals(currentPosition)) {
                    System.out.print("2");
                }
                else if (ropeTailPositions.size() > 2 && ropeTailPositions.get(2).equals(currentPosition)) {
                    System.out.print("3");
                }
                else if (ropeTailPositions.size() > 3 && ropeTailPositions.get(3).equals(currentPosition)) {
                    System.out.print("4");
                }
                else if (ropeTailPositions.size() > 4 && ropeTailPositions.get(4).equals(currentPosition)) {
                    System.out.print("5");
                }
                else if (ropeTailPositions.size() > 5 && ropeTailPositions.get(5).equals(currentPosition)) {
                    System.out.print("6");
                }
                else if (ropeTailPositions.size() > 6 && ropeTailPositions.get(6).equals(currentPosition)) {
                    System.out.print("7");
                }
                else if (ropeTailPositions.size() > 7 && ropeTailPositions.get(7).equals(currentPosition)) {
                    System.out.print("8");
                }
                else if (ropeTailPositions.size() >  8 && ropeTailPositions.get(8).equals(currentPosition)) {
                    System.out.print("9");
                }
                else if (new Position(0,0).equals(currentPosition)) {
                    System.out.print("s");
                }
                else if (this.tailPath.getPositions().contains(currentPosition)) {
                    System.out.print("#");
                }
                else {
                    System.out.print(".");
                }

            }
            System.out.println();
        }
    }

    public void moveHead(Move move) {
        this.move(0, move);
    }

    private void move(int knotNumberToMove, Move move) {
        boolean movingHead = (knotNumberToMove == 0);
        Position ropePositionToMove = movingHead ? this.ropeHeadPosition : this.ropeTailPositions.get(knotNumberToMove - 1);

        for (int i = 1; i <= move.getNbStep(); i++) {
            int xVariation = 0;
            int yVariation = 0;
            switch (move.getDirection()) {
                case UP -> yVariation++;
                case DOWN -> yVariation--;
                case RIGHT -> xVariation++;
                case LEFT -> xVariation--;
            }
            ropePositionToMove = new Position(ropePositionToMove.x() + xVariation,
                                               ropePositionToMove.y() + yVariation);
            if (movingHead)
                this.ropeHeadPosition = ropePositionToMove;
            else
                this.ropeTailPositions.set(knotNumberToMove - 1, ropePositionToMove);

            //this.printMapInConsole();

            if (movingHead)
                this.computeNewTailPositionsAndMoveIt();
        }
    }

    private void computeNewTailPositionsAndMoveIt() {
        for (int i = 1; i <= this.numberOfTailKnots; i++) {
            Position leaderKnot = i == 1 ? this.ropeHeadPosition : this.ropeTailPositions.get(i-2);
            Position followerKnot = this.ropeTailPositions.get(i-1);

            if (!areAdjacent(leaderKnot, followerKnot)) {
                // same column
                if (leaderKnot.x() == followerKnot.x()) {
                    if (leaderKnot.y() > followerKnot.y()) {
                        this.move(i, new Move(Direction.UP, 1));
                    } else {
                        this.move(i, new Move(Direction.DOWN, 1));
                    }
                }

                // same line
                else if (leaderKnot.y() == followerKnot.y()) {
                    if (leaderKnot.x() > followerKnot.x()) {
                        this.move(i, new Move(Direction.RIGHT, 1));
                    } else {
                        this.move(i, new Move(Direction.LEFT, 1));
                    }
                }

                // diagonal
                else {
                    if (leaderKnot.y() > followerKnot.y()) {
                        this.move(i, new Move(Direction.UP, 1));
                    } else {
                        this.move(i, new Move(Direction.DOWN, 1));
                    }

                    if (leaderKnot.x() > followerKnot.x()) {
                        this.move(i, new Move(Direction.RIGHT, 1));
                    } else {
                        this.move(i, new Move(Direction.LEFT, 1));
                    }
                }
                // keep track of the last tail position only
                if (this.numberOfTailKnots == i) {
                    this.tailPath.addPosition(getLastTailPosition());
                }
            }
        }
    }

    private static boolean areAdjacent(Position leadingKnot, Position followingKnot) {
        // same column
        if (leadingKnot.x() == followingKnot.x()) {
            return Math.abs(leadingKnot.y() - followingKnot.y()) <= 1;
        }

        // same line
        if (leadingKnot.y() == followingKnot.y()) {
            return Math.abs(leadingKnot.x() - followingKnot.x()) <= 1;
        }

        // Adjacent in diagonal
        return Math.abs(leadingKnot.y() - followingKnot.y()) <= 1
                && Math.abs(leadingKnot.x() - followingKnot.x()) <= 1;
    }

    @Override
    public String toString() {
        return "BridgeMap{" +
                "ropeTailPositions=" + ropeTailPositions +
                ", ropeHeadPosition=" + ropeHeadPosition +
                ", tailPath=" + tailPath +
                '}';
    }
}
