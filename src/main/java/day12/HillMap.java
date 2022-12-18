package day12;

import day09.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HillMap {
    private final List<Square> squares;
    private Position targetPosition;
    private Position currentPosition;

    public List<Square> getSquares() {
        return this.squares;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public HillMap() {
        this.squares = new ArrayList<>();
    }

    public HillMap(HillMap hillMap, Position newCurrentPosition) {
        this.squares = new ArrayList<>(hillMap.squares);
        this.targetPosition = hillMap.targetPosition;
        this.currentPosition = newCurrentPosition;

        Square currentSquare = this.getCurrentSquare();
        this.squares.set(this.squares.indexOf(currentSquare), new Square(this.currentPosition, currentSquare.elevation(), true));
    }

    public void addSquare(Position position, char elevationChar) {
        Elevation elevation;
        switch (elevationChar) {
            case 'S' -> {
                elevation = Elevation.a;
                this.currentPosition = position;
            }
            case 'E' -> {
                elevation = Elevation.z;
                this.targetPosition = position;
            }
            default -> elevation = Elevation.valueOf(Character.toString(elevationChar));
        }
        this.squares.add(new Square(position, elevation, position.equals(this.currentPosition)));
    }

    public List<Square> getNextEligibleSquares() {
        return this.getNextEligibleSquares(this.currentPosition, this.getMaxColumnIndex(), this.getMaxRowIndex());
    }
    public List<Square> getNextEligibleSquares(Position position, int maxColumnIndex, int maxRowIndex) {
        List<Square> nextEligibleSquares = new ArrayList<>();

        // check each direction
        for (Move move : Move.values()) {
            Position nextEligiblePosition = switch (move) {
                case RIGHT -> new Position(position.x() + 1, position.y());
                case LEFT -> new Position(position.x() - 1, position.y());
                case DOWN -> new Position(position.x(), position.y() + 1);
                case UP -> new Position(position.x(), position.y() - 1);
            };
            if (nextEligiblePosition.x() < 0 || nextEligiblePosition.x() > maxColumnIndex)
                continue;
            if (nextEligiblePosition.y() < 0 || nextEligiblePosition.y() > maxRowIndex)
                continue;
            Square nextEligibleSquare = getSquareByPosition(nextEligiblePosition);
            Square givenSquare = getSquareByPosition(position);
            int distance = nextEligibleSquare.elevation().value() - givenSquare.elevation().value();
            if ((distance == 0 || distance == 1 /*|| distance == -1*/)
            && ! nextEligibleSquare.isVisited()) {
                nextEligibleSquares.add(nextEligibleSquare);
            }
        }
        return nextEligibleSquares;
    }

    public int getMaxRowIndex() {
        return this.squares.stream().map(s -> s.position().y()).max(Comparator.naturalOrder()).get();
    }

    public int getMaxColumnIndex() {
        return this.squares.stream().map(s -> s.position().x()).max(Comparator.naturalOrder()).get();
    }

    public Square getCurrentSquare() {
        return getSquareByPosition(this.currentPosition);
    }

    public boolean isAtTargetPosition() {
        return this.currentPosition.equals(this.targetPosition);
    }

    private Square getSquareByPosition(Position position) {
        return this.squares.stream().filter(s -> s.position().equals(position)).findFirst().get();
    }

    @Override
    public String toString() {
        return "HillMap{" +
                "squares=" + squares +
                ", targetPosition=" + targetPosition +
                ", startingPosition=" + currentPosition +
                '}';
    }
}
