package day17;

import java.util.ArrayList;
import java.util.List;

public class Rock {
    private List<BigPosition> positions;
    private RockStatus status;

    public Rock(final List<BigPosition> positions, long highestRockLevel, int unitsFromTheLeftWall, int unitsFromTheHighestRockOrFloor) {
        this.status = RockStatus.MOVING;
        this.positions = new ArrayList<>();
        for (BigPosition position : positions) {
            this.positions.add(new BigPosition(position.x() + unitsFromTheLeftWall, position.y() + highestRockLevel + unitsFromTheHighestRockOrFloor));
        }
    }

    public static Rock generateFloor(int size) {
        List<BigPosition> floorPositions = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            floorPositions.add(new BigPosition(x, -1));
        }
        Rock floor = new Rock(floorPositions, 0, 0, 0);
        floor.stop();
        return floor;
    }

    public List<BigPosition> positions() {
        return this.positions;
    }

    public RockStatus status() {
        return this.status;
    }

    public void stop() {
        this.status = RockStatus.STOPPED;
    }

    public void fall() {
        this.positions = simulateNextFallingPositions();
    }

    public List<BigPosition> simulateNextFallingPositions() {
        List<BigPosition> newPositions = new ArrayList<>();
        for (BigPosition position : this.positions) {
            newPositions.add(new BigPosition(position.x(), position.y() - 1));
        }
        return newPositions;
    }

    public long computeLowestLevel() {
        return this.positions.stream().map(BigPosition::y).reduce(Long.MAX_VALUE, Long::min);
    }

    public void shift(GasJet gasJet) {
        this.positions = simulateNextShiftedPositions(gasJet);
    }

    public List<BigPosition> simulateNextShiftedPositions(GasJet gasJet) {
        List<BigPosition> newPositions = new ArrayList<>();
        for (BigPosition position : this.positions) {
            newPositions.add(new BigPosition(position.x() + (gasJet == GasJet.PUSH_LEFT ? -1 : 1), position.y()));
        }
        return newPositions;
    }
}
