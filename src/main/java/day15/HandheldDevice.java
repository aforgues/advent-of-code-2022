package day15;

import day09.Position;

import java.util.*;

public class HandheldDevice {
    private final List<Sensor> sensors;
    private final Set<Position> unreachableBeaconPositions;

    public HandheldDevice(List<Sensor> sensors) {
        this.sensors = sensors;
        this.unreachableBeaconPositions = new HashSet<>();
    }

    public void displayInConsole() {
        int minX = Math.min(computeMinXFromSensors(), computeMinXFromUnreachableBeaconPositions());
        int maxX = Math.max(computeMaxXFromSensors(), computeMaxXFromUnreachableBeaconPositions());
        int minY = Math.min(computeMinYFromSensors(), computeMinYFromUnreachableBeaconPositions());
        int maxY = Math.max(computeMaxYFromSensors(), computeMaxYFromUnreachableBeaconPositions());

        StringBuilder sb = new StringBuilder();

        sb.append("Min X = ").append(minX).append("\n");
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                Position pos = new Position(x, y);
                if (this.matchWithSensor(pos)) {
                    sb.append("S");
                }
                else if (this.matchWithBeacon(pos)) {
                    sb.append("B");
                }
                else if (this.matchWithUnreachableBeaconPosition(pos)) {
                    sb.append("#");
                }
                else {
                    sb.append(".");
                }
            }
            sb.append(" ").append(y).append("\n");
        }

        System.out.println(sb);

    }

    private boolean matchWithSensor(Position pos) {
        return this.sensors.stream().anyMatch(sensor -> sensor.position().equals(pos));
    }

    private boolean matchWithBeacon(Position pos) {
        return this.sensors.stream().map(Sensor::closestBeacon).anyMatch(beacon -> beacon.position().equals(pos));
    }

    private boolean matchWithUnreachableBeaconPosition(Position pos) {
        return this.unreachableBeaconPositions.stream().anyMatch(position -> position.equals(pos));
    }

    private int computeMinXFromSensors() {
        return this.sensors.stream()
                .map(sensor -> Math.min(sensor.position().x(), sensor.closestBeacon().position().x()))
                .reduce(Integer.MAX_VALUE, Math::min);
    }

    private int computeMaxXFromSensors() {
        return this.sensors.stream()
                .map(sensor -> Math.max(sensor.position().x(), sensor.closestBeacon().position().x()))
                .reduce(0, Math::max);
    }

    private int computeMinYFromSensors() {
        return this.sensors.stream()
                .map(sensor -> Math.min(sensor.position().y(), sensor.closestBeacon().position().y()))
                .reduce(Integer.MAX_VALUE, Math::min);
    }

    private int computeMaxYFromSensors() {
        return this.sensors.stream()
                .map(sensor -> Math.max(sensor.position().y(), sensor.closestBeacon().position().y()))
                .reduce(0, Math::max);
    }

    private int computeMinXFromUnreachableBeaconPositions() {
        return this.unreachableBeaconPositions.stream()
                .map(Position::x)
                .reduce(Integer.MAX_VALUE, Math::min);
    }

    private int computeMaxXFromUnreachableBeaconPositions() {
        return this.unreachableBeaconPositions.stream()
                .map(Position::x)
                .reduce(0, Math::max);
    }

    private int computeMinYFromUnreachableBeaconPositions() {
        return this.unreachableBeaconPositions.stream()
                .map(Position::y)
                .reduce(Integer.MAX_VALUE, Math::min);
    }

    private int computeMaxYFromUnreachableBeaconPositions() {
        return this.unreachableBeaconPositions.stream()
                .map(Position::y)
                .reduce(0, Math::max);
    }

    public void computeSensorsExclusionZones(int row) {
        this.unreachableBeaconPositions.addAll(simulateUnreachableBeaconPositionsAtRow(row));
        this.sensors.stream().map(Sensor::position).toList().forEach(unreachableBeaconPositions::remove);
        this.sensors.stream().map(s -> s.closestBeacon().position()).toList().forEach(unreachableBeaconPositions::remove);
    }

    private Set<Position> simulateUnreachableBeaconPositionsAtRow(int row) {
        Set<Position> unreachableBeaconPositionsAtRow = Collections.synchronizedSet(new HashSet<>());
        this.sensors.parallelStream().forEach(sensor -> unreachableBeaconPositionsAtRow.addAll(sensor.getUnreachableBeaconPositions(row)));
        /*for (Sensor sensor : this.sensors) {
            //System.out.println("Sensor : " + sensor);
            unreachableBeaconPositionsAtRow.addAll(sensor.getUnreachableBeaconPositions(row));
            //this.displayInConsole();
        }*/
        return unreachableBeaconPositionsAtRow;
    }

    public Optional<Position> searchDistressBeaconAtRow(int row, int minX, int maxX) {
        Set<Position> potentialDistressBeaconPositions = new HashSet<>();
        Set<Position> unreachableBeaconPositionsAtRow = simulateUnreachableBeaconPositionsAtRow(row);
        for (int x = minX; x <= maxX; x++) {
            Position pos = new Position(x, row);
            if (! unreachableBeaconPositionsAtRow.contains(pos))
                potentialDistressBeaconPositions.add(pos);
        }
        return potentialDistressBeaconPositions.stream().findFirst();
    }

    public long countUnreachableBeaconPositionAtRow(int row) {
        return this.unreachableBeaconPositions.stream()
                .filter(position -> position.y() == row)
                .count();
    }
}
