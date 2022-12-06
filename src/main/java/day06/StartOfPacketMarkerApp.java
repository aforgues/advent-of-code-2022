package day06;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class StartOfPacketMarkerApp {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day06/datastream_buffer.txt";
        //String path = "src/main/resources/day06/datastream_buffer_test.txt";
        StartOfPacketMarkerApp app = new StartOfPacketMarkerApp(path);

        // First exercice
        app.computeStartOfPacketMarker();

        // second exercice
        app.computeStartOfMessageMarker();
    }


    private final String filePath;

    public StartOfPacketMarkerApp(String filePath) {
        this.filePath = filePath;
    }

    private void computeStartOfPacketMarker() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            String marker = computeMarkerFromDistinctCharacterNumber(content, 4);
            System.out.println("Start of packet marker is : " + marker);
        }
    }

    private boolean areAllCharacterDifferent(String startOfPacketMarker) {
        for(char c : startOfPacketMarker.toCharArray()) {
            String temp = startOfPacketMarker.replaceFirst("" + c, "");
            if (temp.contains("" + c))
                return false;
        }
        return true;
    }

    private void computeStartOfMessageMarker() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            String marker = computeMarkerFromDistinctCharacterNumber(content, 14);
            System.out.println("Start of message marker is : " + marker);
        }
    }

    private String computeMarkerFromDistinctCharacterNumber(String data, int nbDistinctCharacterNumber) {
        for (int i = nbDistinctCharacterNumber; i <= data.length(); i++) {
            String startOfPacketMarker = data.substring(i-nbDistinctCharacterNumber, i);
            if (areAllCharacterDifferent(startOfPacketMarker)) {
                return "" + i;
            }
        }
        return "";
    }
}
