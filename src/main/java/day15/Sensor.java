package day15;

import day09.Position;

import java.util.ArrayList;
import java.util.List;

public record Sensor(Position position, Beacon closestBeacon) {
    public List<Position> getUnreachableBeaconPositions(int row) {
        List<Position> positions = new ArrayList<>();
        int distance = getDistanceFromClosestBeacon();
        //System.out.println("distance : " + distance);

        if (row >= (position.y() - distance) && row <= position.y() + distance) {
            //for (int y = position.y() - distance; y <= position.y() + distance; y++) {
            int y = row;
                for (int x = position.x() - distance; x <= position.x() + distance; x++) {
                    Position pos = new Position(x, y);
                    if (manathanDistance(position, pos) <= distance) {
                        positions.add(pos);
                    }
                }
            //}
        }

        return positions;
    }

    public static int manathanDistance(Position p1, Position p2) {
        return Math.abs(p1.x() - p2.x()) + Math.abs(p1.y() - p2.y());
    }

    public int getDistanceFromClosestBeacon() {
        return manathanDistance(this.position, this.closestBeacon.position());
    }
}
