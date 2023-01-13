package day17;

import day09.Position;

import java.util.ArrayList;
import java.util.List;

public class Rock {
    private List<Position> positions;
    private RockStatus status;

    public Rock(final List<Position> positions, int highestRockLevel, int unitsFromTheLeftWall, int unitsFromTheHighestRockOrFloor) {
        this.status = RockStatus.MOVING;
        this.positions = new ArrayList<>();
        for (Position position : positions) {
            this.positions.add(new Position(position.x() + unitsFromTheLeftWall, position.y() + highestRockLevel + unitsFromTheHighestRockOrFloor));
        }
    }

    public static Rock generateFloor(int size) {
        List<Position> floorPositions = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            floorPositions.add(new Position(x, -1));
        }
        Rock floor = new Rock(floorPositions, 0, 0, 0);
        floor.stop();
        return floor;
    }

    public List<Position> positions() {
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

    public List<Position> simulateNextFallingPositions() {
        List<Position> newPositions = new ArrayList<>();
        for (Position position : this.positions) {
            newPositions.add(new Position(position.x(), position.y() - 1));
        }
        return newPositions;
    }

    public int computeLowestLevel() {
        return this.positions.stream().map(Position::y).reduce(Integer.MAX_VALUE, Integer::min);
    }

    public void shift(GasJet gasJet) {
        this.positions = simulateNextShiftedPositions(gasJet);
    }

    public List<Position> simulateNextShiftedPositions(GasJet gasJet) {
        List<Position> newPositions = new ArrayList<>();
        for (Position position : this.positions) {
            newPositions.add(new Position(position.x() + (gasJet == GasJet.PUSH_LEFT ? -1 : 1), position.y()));
        }
        return newPositions;
    }
}
