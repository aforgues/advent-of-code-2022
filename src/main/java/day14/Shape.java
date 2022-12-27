package day14;

import day09.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Shape {
    private final Type type;
    private final Position startPosition;
    private final Position endPosition;
    private final List<Position> rockPositions;

    public Position getStartPosition() {
        return startPosition;
    }
    public Position getEndPosition() {
        return endPosition;
    }

    public Shape(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;

        if (this.startPosition.x() == this.endPosition.x()) {
            this.type = Type.VERTICAL_LINE;
        }
        else if (this.startPosition.y() == this.endPosition.y()) {
            this.type = Type.HORIZONTAL_LINE;
        }
        else {
            throw new IllegalArgumentException("Shape is neither a vertical nor an horizontal line based on start " + startPosition + " and end " + endPosition);
        }

        this.rockPositions = computeLinePositions();
    }

    private List<Position> computeLinePositions() {
        List<Position> positions = new ArrayList<>();

        switch (type) {
            case VERTICAL_LINE -> {
                int sign = Float.valueOf(Math.signum(this.endPosition.y() - this.startPosition.y())).intValue();
                Function<Integer, Boolean> condition = (i -> sign == 1 ? i <= this.endPosition.y() : i >= this.endPosition.y());
                for (int y = this.startPosition.y(); condition.apply(y); y += sign) {
                    positions.add(new Position(this.startPosition.x(), y));
                }
            }
            case HORIZONTAL_LINE -> {
                int sign = Float.valueOf(Math.signum(this.endPosition.x() - this.startPosition.x())).intValue();
                Function<Integer, Boolean> condition = (i -> sign == 1 ? i <= this.endPosition.x() : i >= this.endPosition.x());
                for (int x = this.startPosition.x(); condition.apply(x); x += sign) {
                    positions.add(new Position(x, this.startPosition.y()));
                }
            }
        }

        return positions;
    }

    public boolean contains(Position position) {
        return this.rockPositions.contains(position);
    }

    @Override
    public String toString() {
        return "Shape{" +
                "type=" + type +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                '}';
    }
}
