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
        app.computeScore();

        // second exercice
        //app.computeScoreV2();
    }


    private final String filePath;

    public StartOfPacketMarkerApp(String filePath) {
        this.filePath = filePath;
    }

    private void computeScore() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            for (int i = 4; i <= content.length(); i++) {
                String startOfPacketMarker = content.substring(i-4, i);
                if (areAllCharacterDifferent(startOfPacketMarker)) {
                    System.out.println("Start of packet marker is : " + i);
                    break;
                }
            }
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

    /*private void computeScoreV2() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
        }
    }*/
}
