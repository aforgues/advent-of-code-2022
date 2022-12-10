package day10;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CathodeRayTubeApp {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day10/instructions.txt";
        //String path = "src/main/resources/day10/instructions_test.txt";
        CathodeRayTubeApp app = new CathodeRayTubeApp(path);

        // First exercice
        app.computeScoreV1();

        // second exercice
        app.computeScoreV2();
    }


    private final String filePath;
    private List<Instruction> instructions;

    public CathodeRayTubeApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.instructions = new ArrayList<>();
        this.parseFile();
    }

    private void parseFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);
            this.instructions.add(new Instruction(content));
        }
        System.out.println(this.instructions);
    }

    private void computeScoreV1() {
        int cycleCount = 0;
        int register = 1;
        List<Integer> cyclesToCheck = List.of(20, 60, 100, 140, 180, 220);

        int totalSignalStrength = 0;
        for (Instruction instruction : this.instructions) {
            System.out.println("Instruction : " + instruction);
            for (int i = 0; i < instruction.getCommand().getNbCycle(); i++) {
                cycleCount ++;

                System.out.println("Cycle current count : " + cycleCount);

                System.out.println("Register value (X) : " + register);

                if (cyclesToCheck.contains(cycleCount)) {
                    System.out.println("SIGNAL STRENGTH at cycle " + cycleCount + " : " + cycleCount * register);
                    totalSignalStrength += cycleCount * register;
                }
                //add to register at the end of the second / last cycle
                if (instruction.getCommand() == Command.ADDX && i == instruction.getCommand().getNbCycle() - 1) {
                    register += instruction.getRegisterIncreaseValue().get();
                }
            }
        }

        System.out.println("Total nb of cycles : " + cycleCount);
        System.out.println("Register final value (X) : " + register);
        System.out.println("Total signal strength : " + totalSignalStrength);
    }

    private void computeScoreV2() {
        // TODO
    }
}
