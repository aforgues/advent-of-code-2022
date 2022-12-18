package day18;


import java.io.File;
import java.io.FileNotFoundException;

import java.util.*;

public class BoilingBouldersApp {

    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day18/lava_droplet_scan.txt";
        //String path = "src/main/resources/day18/lava_droplet_scan_test.txt";
        BoilingBouldersApp app = new BoilingBouldersApp(path);

        // First exercise
        app.computeScore();
    }

    private final String filePath;
    private List<Cube> grid;

    public BoilingBouldersApp(String filePath) {
        this.filePath = filePath;
        this.grid = new ArrayList<>();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            String[] coords = content.split(",");
            Position3D position3D = new Position3D(Integer.parseInt(coords[0]),
                                                    Integer.parseInt(coords[1]),
                                                    Integer.parseInt(coords[2]));
            this.grid.add(new Cube(position3D));
        }
        System.out.println(grid);
    }

    private void computeScore() throws FileNotFoundException {
        this.parseFile();

        int score = 0;
        //Compute unconnected sides of each cube
        for (Cube cube : this.grid) {
            Set<Type> allConnectedTypes = new HashSet<>(6);
            for (Cube otherCube : this.grid) {
                if (otherCube.equals(cube))
                    continue;
                Set<Type> connectedTypes = cube.getConnectedSideTypes(otherCube);
                allConnectedTypes.addAll(connectedTypes);
                //System.out.println("Cube " + cube + " has " + connectedTypes + " sides connected to " + otherCube);
            }
            score += (6 - allConnectedTypes.size());
            System.out.println("Cube " + cube + " has " + (6 - allConnectedTypes.size()) + " sides exposed");
        }

        System.out.println("Score : " + score);
    }
}
