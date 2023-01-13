package day17;

import day09.Position;

import java.util.List;

import static java.util.List.of;

public enum RockType {
    MINUS(of(new Position(0 ,0), new Position(1, 0), new Position(2, 0), new Position(3, 0))),
    PLUS(of(new Position(1 ,0), new Position(0, 1), new Position(1, 1), new Position(2, 1), new Position(1, 2))),
    REVERSE_L(of(new Position(0 ,0), new Position(1, 0), new Position(2, 0), new Position(2, 1), new Position(2, 2))),
    VERTICAL_BAR(of(new Position(0 ,0), new Position(0, 1), new Position(0, 2), new Position(0, 3))),
    SQUARE(of(new Position(0 ,0), new Position(1, 0), new Position(0, 1), new Position(1, 1)));

    public List<Position> getPositions() {
        return positions;
    }

    private final List<Position> positions;

    RockType(List<Position> positions) {
        this.positions = positions;
    }
}
