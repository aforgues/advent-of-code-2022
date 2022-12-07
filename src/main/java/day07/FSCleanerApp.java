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
        app.computeScoreV2();
    }

    private static final long MAX_DISK_SPACE = 70000000;
    private static final long MIN_NEED_UNUSED_SPACE = 30000000;

    private final String filePath;
    private MyDirectory currentDirectory;

    public FSCleanerApp(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.currentDirectory = new MyDirectory("/", null); // init root directory
        this.computeDirectoryTree();
    }

    private void computeDirectoryTree() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        String currentCommand;


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
    }


    private void computeScoreV1() {

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

    private long computeTotalSizeParsingDirectoriesInTreeWithAtMost(MyDirectory directory, long maxSize) {
        long totalSize = 0;
        if (directory.getTotalSize() < maxSize) {
            System.out.println("Matching directory : " + directory);
            System.out.println("Matching directory totalSize : " + directory.getTotalSize());
            totalSize += directory.getTotalSize();
        }
        for (MyDirectory subDirectory : directory.getSubDirectories()) {
            totalSize += computeTotalSizeParsingDirectoriesInTreeWithAtMost(subDirectory, maxSize);
        }
        return totalSize;
    }

    private void computeScoreV2() {
        // go up to the root
        while (currentDirectory.getParentDirectory() != null) {
            currentDirectory = currentDirectory.getParentDirectory();
        }
        System.out.println(currentDirectory);
        System.out.println("Total size : " + currentDirectory.getTotalSize());

        // compute current unused space
        long currentUnusedSpace = MAX_DISK_SPACE - currentDirectory.getTotalSize();
        System.out.println("Current unused space : " + currentUnusedSpace);

        long remainingDiskSpaceToFree = MIN_NEED_UNUSED_SPACE - currentUnusedSpace;
        System.out.println("Remaining disk space to free : " + remainingDiskSpaceToFree);

        long score = computeSmallestDirectorySizeInTreeWithAtLeast(currentDirectory, remainingDiskSpaceToFree);
        System.out.println("Final score : " + score);
    }

    private long computeSmallestDirectorySizeInTreeWithAtLeast(MyDirectory directory, long minSize) {
        long minDirectorySize = MIN_NEED_UNUSED_SPACE;
        if (directory.getTotalSize() >= minSize) {
            System.out.println("Matching directory : " + directory);
            System.out.println("Matching directory totalSize : " + directory.getTotalSize());
            minDirectorySize = Math.min(minDirectorySize, directory.getTotalSize());
        }
        for (MyDirectory subDirectory : directory.getSubDirectories()) {
            minDirectorySize = Math.min(minDirectorySize, computeSmallestDirectorySizeInTreeWithAtLeast(subDirectory, minSize));
        }
        return minDirectorySize;
    }
}
