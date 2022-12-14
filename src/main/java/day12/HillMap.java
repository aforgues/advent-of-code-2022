package day12;

import day09.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HillMap {
    private final List<Square> squares;
    private Position targetPosition;
    private Position currentPosition;

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
        List<Square> nextEligibleSquares = new ArrayList<>();
        Square currentSquare = this.getCurrentSquare();
        Elevation currentElevation = currentSquare.elevation();

        // check each direction
        for (Move move : Move.values()) {
            Position nextEligiblePosition = switch (move) {
                case RIGHT -> new Position(this.currentPosition.x() + 1, this.currentPosition.y());
                case LEFT -> new Position(this.currentPosition.x() - 1, this.currentPosition.y());
                case DOWN -> new Position(this.currentPosition.x(), this.currentPosition.y() + 1);
                case UP -> new Position(this.currentPosition.x(), this.currentPosition.y() - 1);
            };
            if (nextEligiblePosition.x() < 0 || nextEligiblePosition.x() > getMaxColumnIndex())
                continue;
            if (nextEligiblePosition.y() < 0 || nextEligiblePosition.y() > getMaxRowIndex())
                continue;
            Square nextEligibleSquare = getSquareByPosition(nextEligiblePosition);
            int distance = nextEligibleSquare.elevation().value() - currentElevation.value();
            if ((distance == 0 || distance == 1)
            && ! nextEligibleSquare.isVisited()) {
                nextEligibleSquares.add(nextEligibleSquare);
            }
        }
        return nextEligibleSquares;
    }

    private int getMaxRowIndex() {
        return this.squares.stream().map(s -> s.position().y()).max(Comparator.naturalOrder()).get();
    }

    private int getMaxColumnIndex() {
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
