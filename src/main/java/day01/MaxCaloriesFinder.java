package day01;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class MaxCaloriesFinder {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/day01/calories_registry.txt";
        //String path = "src/main/resources/day01/calories_test.txt";
        MaxCaloriesFinder finder = new MaxCaloriesFinder(path);

        // First exercice
        finder.findMaxCaloryOfFirstElf();

        // second exerice
        finder.findMaxCaloriesofTopThreeElves();
    }

    private String filePath;

    public MaxCaloriesFinder(String filePath) {
        this.filePath = filePath;
    }

    public void findMaxCaloryOfFirstElf() throws FileNotFoundException {
        Map<Integer, Integer> caloriesByElfNumber = parseCaloriesByElf();

        Integer maxCalories = (Integer) caloriesByElfNumber.values()
                .stream()
                .max(Comparator.naturalOrder())
                .stream().findFirst()
                .orElse(null);
        System.out.println("Max calories : " + maxCalories);
    }

    public void findMaxCaloriesofTopThreeElves() throws FileNotFoundException {
        Map<Integer, Integer> caloriesByElfNumber = parseCaloriesByElf();

        Integer sumMaxCalories = caloriesByElfNumber.values()
                .stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce(0, Integer::sum);

        System.out.println("Sum of max calories : " + sumMaxCalories);
    }

    private Map<Integer, Integer> parseCaloriesByElf() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(this.filePath));
        scanner.useDelimiter("\n");

        Map caloriesByElfNumber = new LinkedHashMap<Integer, Integer>();
        int elfCounter = 1;

        while (scanner.hasNext()) {
            String newCaloriesAsText = scanner.next();
            if ("".equals(newCaloriesAsText)) {
                System.out.println(elfCounter + ";" + caloriesByElfNumber.get(elfCounter));
                elfCounter++;
                continue;
            }
            Integer newCalories = Integer.parseInt(newCaloriesAsText);
            Integer currentCalories = (Integer) caloriesByElfNumber.get(elfCounter);
            if (currentCalories == null) {
                caloriesByElfNumber.put(elfCounter, newCalories);
            }
            else {
                caloriesByElfNumber.put(elfCounter, currentCalories.intValue() + newCalories.intValue());
            }
        }
        System.out.println(elfCounter + ";" + caloriesByElfNumber.get(elfCounter));

        return caloriesByElfNumber;
    }
}
