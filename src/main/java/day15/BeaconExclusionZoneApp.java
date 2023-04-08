package day15;

import utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class BeaconExclusionZoneApp {

    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day15/puzzle_input.txt";
        //String path = "src/main/resources/day15/puzzle_input_test.txt";
        BeaconExclusionZoneApp app = new BeaconExclusionZoneApp(path);

        // First part
        app.computeScore();

        // Second part
        //app.computeScoreV2();
        app.computeScoreV2_2();
    }

    private final String filePath;
    private final List<Sensor> sensors;
    private final HandheldDevice device;

    public BeaconExclusionZoneApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.sensors = new ArrayList<>();
        this.parseFile();
        this.device = new HandheldDevice(this.sensors);
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);

            String sensorPosAsString = content.substring("Sensor at ".length(), content.indexOf(":"));
            Scanner ss = new Scanner(sensorPosAsString);
            ss.useDelimiter(", ");
            int sensorX = Integer.parseInt(ss.next().split("=")[1]);
            int sensorY = Integer.parseInt(ss.next().split("=")[1]);
            Position sensorPosition = new Position(sensorX, sensorY);
            //System.out.println(sensorPosition);

            String beaconPosAsString = content.substring(content.lastIndexOf(" at "));
            Scanner sb = new Scanner(beaconPosAsString);
            sb.useDelimiter(", ");
            int beaconX = Integer.parseInt(sb.next().split("=")[1]);
            int beaconY = Integer.parseInt(sb.next().split("=")[1]);
            Position beaconPosition = new Position(beaconX, beaconY);
            //System.out.println(beaconPosition);

            this.sensors.add(new Sensor(sensorPosition, new Beacon(beaconPosition)));
        }

        System.out.println(this.sensors);
    }

    private void computeScore() {
        //int rowToAnalyse = 10;
        int rowToAnalyse = 2000000;

        //device.displayInConsole();
        this.device.computeSensorsExclusionZones(rowToAnalyse);
        //device.displayInConsole();

        long score = this.device.countUnreachableBeaconPositionAtRow(rowToAnalyse);

        System.out.println("Score : " + score);
    }

    // Brute force solution that need lot of time of computation (it works for the example, but I did not wait long enough for the target puzzle input)
    private void computeScoreV2() {
        final int minX = 0;
        //final int maxX = 20;
        final int maxX = 4000000;
        AtomicInteger count = new AtomicInteger(0);
        IntStream.rangeClosed(minX, maxX).parallel().forEach(row -> {
            //for (int row = minX; row <= maxX; row++) {
            Optional<Position> distressBeacon = this.device.searchDistressBeaconAtRow(row, minX, maxX);
            if (distressBeacon.isPresent()) {
                System.out.println("Distress beacon found at " + distressBeacon.get());
                System.out.println("Tuning frequency is : " + (distressBeacon.get().x() * 4000000 + distressBeacon.get().y()));
                return;
            }
            //}
            int currentCount = count.incrementAndGet();
            System.out.println("Already explored " + currentCount + " over " + maxX + " rows : " + (Integer.valueOf(currentCount).doubleValue()/maxX)*100 + "%");
        });
    }

    private void computeScoreV2_2() {
        final int minX = 0;
        //final int maxX = 20;
        final int maxX = 4000000;

        this.device.computeSensorsPotentialDistressBeaconPositions(minX, maxX);
        //this.device.displayInConsole();
        Position distressBeaconPosition = this.device.searchDistressBeaconPosition();
        System.out.println("Distress beacon found at " + distressBeaconPosition);
        System.out.println("Tuning frequency is : " + (distressBeaconPosition.x() * 4000000L + distressBeaconPosition.y()));
    }

}
