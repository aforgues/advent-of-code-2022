package day09;

import day08.Direction;

import java.util.HashSet;

public class BridgeMap {
    private Position ropeTailPosition;
    private Position ropeHeadPosition;
    private TailPath tailPath;

    public int getUniqueTailPathCount() {
        return new HashSet<>(this.tailPath.getPositions()).size();
    }

    public BridgeMap(Position ropeTailPosition, Position ropeHeadPosition) {
        this.ropeTailPosition = ropeTailPosition;
        this.ropeHeadPosition = ropeHeadPosition;
        this.tailPath = new TailPath(this.ropeTailPosition);
    }

    public void printMapInConsole() {
        for (int y = Math.max(this.ropeHeadPosition.y(), this.ropeTailPosition.y()); y >=0; y--) {
            for (int x = 0; x <= Math.max(this.ropeHeadPosition.x(), this.ropeTailPosition.x()); x++) {
                if (ropeHeadPosition.equals(new Position(x, y))) {
                    System.out.print("H");
                }
                else if (ropeTailPosition.equals(new Position(x, y))) {
                    System.out.print("T");
                }
                else if (new Position(0,0).equals(new Position(x, y))) {
                    System.out.print("s");
                }
                else {
                    System.out.print(".");
                }

            }
            System.out.println();
        }
    }

    public void moveHead(Move move) {
        this.move(true, move);
    }

    private void move(boolean moveHead, Move move) {
        Position ropePositionToMove = moveHead ? this.ropeHeadPosition : this.ropeTailPosition;
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
            if (moveHead)
                this.ropeHeadPosition = ropePositionToMove;
            else
                this.ropeTailPosition = ropePositionToMove;

            //this.printMapInConsole();

            if (moveHead)
                this.comptuteNewTailPositionAndMoveIt();
        }
    }

    private void comptuteNewTailPositionAndMoveIt() {
        if (! areTailAndHeadAdjacent()) {
            // same column
            if (this.ropeHeadPosition.x() == this.ropeTailPosition.x()) {
                if (this.ropeHeadPosition.y() > this.ropeTailPosition.y()) {
                    this.move(false, new Move(Direction.UP, 1));
                }
                else {
                    this.move(false, new Move(Direction.DOWN, 1));
                }
            }

            // same line
            else if (this.ropeHeadPosition.y() == this.ropeTailPosition.y()) {
                if (this.ropeHeadPosition.x() > this.ropeTailPosition.x()) {
                    this.move(false, new Move(Direction.RIGHT, 1));
                }
                else {
                    this.move(false, new Move(Direction.LEFT, 1));
                }
            }

            // diagonal
            else {
                if (this.ropeHeadPosition.y() > this.ropeTailPosition.y()) {
                    this.move(false, new Move(Direction.UP, 1));
                }
                else {
                    this.move(false, new Move(Direction.DOWN, 1));
                }

                if (this.ropeHeadPosition.x() > this.ropeTailPosition.x()) {
                    this.move(false, new Move(Direction.RIGHT, 1));
                }
                else {
                    this.move(false, new Move(Direction.LEFT, 1));
                }
            }
            this.tailPath.addPosition(this.ropeTailPosition);
        }
    }

    private boolean areTailAndHeadAdjacent() {
        // same column
        if (this.ropeHeadPosition.x() == this.ropeTailPosition.x()) {
            return Math.abs(this.ropeHeadPosition.y() - this.ropeTailPosition.y()) <= 1;
        }

        // same line
        if (this.ropeHeadPosition.y() == this.ropeTailPosition.y()) {
            return Math.abs(this.ropeHeadPosition.x() - this.ropeTailPosition.x()) <= 1;
        }

        // Adjacent in diagonal
        return Math.abs(this.ropeHeadPosition.y() - this.ropeTailPosition.y()) <= 1
                && Math.abs(this.ropeHeadPosition.x() - this.ropeTailPosition.x()) <= 1;
    }

    @Override
    public String toString() {
        return "BridgeMap{" +
                "ropeTailPosition=" + ropeTailPosition +
                ", ropeHeadPosition=" + ropeHeadPosition +
                ", tailPath=" + tailPath +
                '}';
    }
}
