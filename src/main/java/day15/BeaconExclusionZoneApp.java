package day15;

import day09.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BeaconExclusionZoneApp {

    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day15/puzzle_input.txt";
        //String path = "src/main/resources/day15/puzzle_input_test.txt";
        BeaconExclusionZoneApp app = new BeaconExclusionZoneApp(path);

        // First exercise
        app.computeScore();
    }

    private final String filePath;
    private final List<Sensor> sensors;

    public BeaconExclusionZoneApp(String filePath) {
        this.filePath = filePath;
        this.sensors = new ArrayList<>();
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

    private void computeScore() throws FileNotFoundException {
        this.parseFile();

        HandheldDevice device = new HandheldDevice(this.sensors);

        //int rowToAnalyse = 10;
        int rowToAnalyse = 2000000;

        //device.displayInConsole();
        device.computeSensorsExclusionZones(rowToAnalyse);
        //device.displayInConsole();

        long score = device.countUnreachableBeaconPositionAtRow(rowToAnalyse);

        System.out.println("Score : " + score);
    }
}
