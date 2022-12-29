package day15;

import day09.Position;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class HandheldDevice {
    private final List<Sensor> sensors;
    private final Set<Position> unreachableBeaconPositions;
    private final Set<Position> potentialDistressBeaconPositions;

    public HandheldDevice(List<Sensor> sensors) {
        this.sensors = sensors;
        this.unreachableBeaconPositions = new HashSet<>();
        this.potentialDistressBeaconPositions = Collections.synchronizedSet(new HashSet<>());

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
                else if (this.matchWithPotentialDistressBeaconPosition(pos)) {
                    sb.append("P");
                }
                else {
                    sb.append(".");
                }
            }
            sb.append(" ").append(y).append("\n");
        }

        System.out.println(sb);

    }

    private boolean matchWithPotentialDistressBeaconPosition(Position pos) {
        return this.potentialDistressBeaconPositions.stream().anyMatch(position -> position.equals(pos));
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
        this.sensors.stream().map(Sensor::position).toList().forEach(this.unreachableBeaconPositions::remove);
        this.sensors.stream().map(s -> s.closestBeacon().position()).toList().forEach(this.unreachableBeaconPositions::remove);
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

    public long countUnreachableBeaconPositionAtRow(int row) {
        return this.unreachableBeaconPositions.stream()
                .filter(position -> position.y() == row)
                .count();
    }

    // PART 2 - First implementation based on part 1 (need to explore every 4000000 rows)
    //-----------------------------------------------------------------------------------

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

    // PART 2 - Second implementation based on much explicit targeted potential positions
    //-----------------------------------------------------------------------------------

    public void computeSensorsPotentialDistressBeaconPositions(int minCoordinate, int maxCoordinate) {
        this.sensors.parallelStream().forEach(sensor -> this.potentialDistressBeaconPositions.addAll(this.computePotentialDistressBeaconPositionAroundSensor(sensor, minCoordinate, maxCoordinate)));
        this.sensors.stream().map(Sensor::position).toList().forEach(this.potentialDistressBeaconPositions::remove);
        this.sensors.stream().map(s -> s.closestBeacon().position()).toList().forEach(this.potentialDistressBeaconPositions::remove);
    }

    public Position searchDistressBeaconPosition() {
        AtomicReference<Position> distressBeaconPosition = new AtomicReference<>();
        this.potentialDistressBeaconPositions.parallelStream().forEach(potentialDistressBeaconPosition -> {
                // check intersection with all sensors exclusion zone, using distance and not list of exclusion zone positions
                boolean found = this.sensors.stream().allMatch(sensor -> Sensor.manathanDistance(potentialDistressBeaconPosition, sensor.position()) > sensor.getDistanceFromClosestBeacon());
                if (found) {
                    distressBeaconPosition.set(potentialDistressBeaconPosition);
                    //System.out.println("Found Distress signal : " + potentialDistressBeaconPosition);
                }
        });
        return distressBeaconPosition.get();
    }


    /*
       If the sensor S is at a distance D (2 in example below) from its closest beacon B, then :
         - all the unreachable positions (marked as #) are at a max distance of D
         - all the potential distress beacon positions (marked as P below) are at D+1 (3)

       .....P.....
       ....P#P....
       ...P###P...
       ..P##S##P..
       ...PB##P...
       ....P#P....
       .....P.....
     */
    private List<Position> computePotentialDistressBeaconPositionAroundSensor(Sensor sensor, int minCoordinate, int maxCoordinate) {
        List<Position> potentialDistressBeaconPositions = new ArrayList<>();
        int radius = sensor.getDistanceFromClosestBeacon() + 1;

        // Let's start at the top
        int x = sensor.position().x();
        int y = sensor.position().y() - radius;

        // Go down to the right and then to the bottom
        while (y < (sensor.position().y() + radius)) {
            // filter on Distress signal predefined zone (minCoordinate, maxCoordinate)
            if (x >= minCoordinate && x <= maxCoordinate && y >= minCoordinate && y <= maxCoordinate)
                potentialDistressBeaconPositions.add(new Position(x, y));
            if (y < sensor.position().y())
                x++;
            else
                x--;
            y++;
        }

        // Go up to the left then to the top
        while (y > (sensor.position().y() - radius)) {
            // filter on Distress signal predefined zone (minCoordinate, maxCoordinate)
            if (x >= minCoordinate && x <= maxCoordinate && y >= minCoordinate && y <= maxCoordinate)
                potentialDistressBeaconPositions.add(new Position(x, y));
            if (y > sensor.position().y())
                x--;
            else
                x++;
            y--;
        }

        return potentialDistressBeaconPositions;
    }
}
