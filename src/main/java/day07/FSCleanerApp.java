package day07;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class FSCleanerApp {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day07/commands_buffer.txt";
        //String path = "src/main/resources/day07/commands_buffer_test.txt";
        FSCleanerApp app = new FSCleanerApp(path);

        // First exercice
        app.computeScoreV1();

        // second exercice
        //app.computeScoreV2();
    }


    private final String filePath;

    public FSCleanerApp(String filePath) {
        this.filePath = filePath;
    }

    private void computeScoreV1() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        String currentCommand;
        MyDirectory currentDirectory = new MyDirectory("/", null); // init root directory

        while (scanner.hasNext()) {
            String content = scanner.next();
            System.out.println(content);

            // command
            if (content.startsWith("$")) {
                currentCommand = content.substring(2);
                System.out.println("new command line : " + currentCommand);
                String[] commandArgs = currentCommand.split(" ");
                System.out.println("args : " + Arrays.asList(commandArgs));
                switch (commandArgs[0]) {
                    case "cd" :
                        MyDirectory directory;
                        if (commandArgs[1].equals("/")) {
                            break;
                        }
                        else if (commandArgs[1].equals("..")) {
                            currentDirectory = currentDirectory.getParentDirectory();
                            break;
                        }
                        // find subDirectory inside the current directory
                        else {
                            directory = currentDirectory.findSubDirectoryByName(commandArgs[1]);
                        }

                        // change directory
                        currentDirectory = directory;
                        break;
                    default :
                        continue; // it should be a ls command, so skip to next line

                }
            }
            // result(s) of previous command
            else {
                String[] commandResults = content.split((" "));
                if (commandResults[0].equals("dir")) {
                    currentDirectory.addSubDirectory(new MyDirectory(commandResults[1], currentDirectory));
                }
                else {
                    currentDirectory.addFile(commandResults[1], Long.parseLong(commandResults[0]));
                }

            }
            System.out.println(currentDirectory);
        }

        System.out.println(currentDirectory);

        // go up to the root
        while (currentDirectory.getParentDirectory() != null) {
            currentDirectory = currentDirectory.getParentDirectory();
        }
        System.out.println(currentDirectory);
        System.out.println("Total size : " + currentDirectory.getTotalSize());

        // Parse all directories to compute total directory size and keep those with at most 100000 size
        long score = computeTotalSizeParsingDirectoriesInTreeWithAtMost(currentDirectory, 100000);
        System.out.println("Final score : " + score);
    }

    private long computeTotalSizeParsingDirectoriesInTreeWithAtMost(MyDirectory currentDirectory, long maxSize) {
        long totalSize = 0;
        if (currentDirectory.getTotalSize() < maxSize) {
            System.out.println("Matching directory : " + currentDirectory);
            System.out.println("Matching directory totalSize : " + currentDirectory.getTotalSize());
            totalSize += currentDirectory.getTotalSize();
        }
        for (MyDirectory subDirectory : currentDirectory.getSubDirectories()) {
            totalSize += computeTotalSizeParsingDirectoriesInTreeWithAtMost(subDirectory, maxSize);
        }
        return totalSize;
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
