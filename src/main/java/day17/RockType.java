package day17;

import java.util.List;

import static java.util.List.of;

public enum RockType {
    MINUS(of(new BigPosition(0 ,0), new BigPosition(1, 0), new BigPosition(2, 0), new BigPosition(3, 0))),
    PLUS(of(new BigPosition(1 ,0), new BigPosition(0, 1), new BigPosition(1, 1), new BigPosition(2, 1), new BigPosition(1, 2))),
    REVERSE_L(of(new BigPosition(0 ,0), new BigPosition(1, 0), new BigPosition(2, 0), new BigPosition(2, 1), new BigPosition(2, 2))),
    VERTICAL_BAR(of(new BigPosition(0 ,0), new BigPosition(0, 1), new BigPosition(0, 2), new BigPosition(0, 3))),
    SQUARE(of(new BigPosition(0 ,0), new BigPosition(1, 0), new BigPosition(0, 1), new BigPosition(1, 1)));

    public List<BigPosition> getPositions() {
        return positions;
    }

    private final List<BigPosition> positions;

    RockType(List<BigPosition> positions) {
        this.positions = positions;
    }
}
