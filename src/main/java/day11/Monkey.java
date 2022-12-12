package day11;

import java.util.*;

public class Monkey {
    private final int number;
    private List<Item> items;
    private Operation operation;
    private int divisionTestValue;
    private int validTestTargetMonkeyNumber;
    private int invalidTestTargetMonkeyNumber;
    private int inspectionCounter;

    public int getInspectionCounter() {
        return inspectionCounter;
    }
    public int getNumber() {
        return this.number;
    }
    public List<Item> getItems() {
        return this.items;
    }
    public int getDivisionTestValue() {
        return divisionTestValue;
    }

    public Monkey(int number) {
        this.number = number;
        this.items = new ArrayList<>();
        this.inspectionCounter = 0;
    }

    public void initItems(String content) {
        for (String s : content.split(", ")) {
            this.items.add(new Item(Integer.parseInt(s)));
        }
    }

    public void initInspectOperation(String operation) {
        String[] operationArr = operation.split(" ");
        this.operation = new Operation(operationArr[1], operationArr[0], operationArr[2]);
    }

    public void initInspectTest(String test) {
        this.divisionTestValue = Integer.parseInt(test);
    }

    public void initValidTestTargetMonkey(String targetMonkey) {
        this.validTestTargetMonkeyNumber = Integer.parseInt(targetMonkey);
    }

    public void initInvalidTestTargetMonkey(String targetMonkey) {
        this.invalidTestTargetMonkeyNumber = Integer.parseInt(targetMonkey);
    }

    public Map<Integer, List<Item>> inspectItems(int reliefDivisor, int worryManageableDivisor) {
        Map<Integer, List<Item>> itemsByMonkeyNumberToMove = new HashMap<>();
        List<Item> allItemsToMove = new ArrayList<>();
        for (Item item : this.items) {
            System.out.println("  Monkey inspects an item with a worry level of " + item.worryLevel());
            long newWorryLevel = this.operation.execute(item.worryLevel());
            System.out.println("    Worry level is updated to " + newWorryLevel);
            long reliefWorryLevel = newWorryLevel / reliefDivisor;
            System.out.println("    Monkey gets bored with item. Worry level is divided by " + reliefDivisor + " to " + reliefWorryLevel);
            reliefWorryLevel = reliefWorryLevel % worryManageableDivisor;
            System.out.println("    Update worry level with modulo " + worryManageableDivisor + " to " + reliefWorryLevel);
            boolean isTestValid = reliefWorryLevel % this.divisionTestValue == 0;
            System.out.println("    Current worry level is " + (! isTestValid ? "not " : "") + "divisible by " + this.divisionTestValue);
            int targetMonkey = isTestValid ? this.validTestTargetMonkeyNumber : this.invalidTestTargetMonkeyNumber;
            System.out.println("    Item with worry level " + reliefWorryLevel + " is thrown to monkey " + targetMonkey);
            List<Item> itemsToMove = itemsByMonkeyNumberToMove.computeIfAbsent(targetMonkey, it -> new ArrayList<>());
            itemsToMove.add(new Item(reliefWorryLevel));
            allItemsToMove.add(item);

            this.inspectionCounter++;
            System.out.println("  Inspection counter : " + this.inspectionCounter);
        }

        System.out.println(itemsByMonkeyNumberToMove);

        // Remove item from this monkey then add to the new owner
        this.items.removeAll(allItemsToMove);

        return itemsByMonkeyNumberToMove;
    }

    @Override
    public String toString() {
        return "Monkey{" +
                "number=" + number +
                ", items=" + items +
                ", operation=" + operation +
                ", divisionTestValue=" + divisionTestValue +
                ", validTestTargetMonkeyNumber=" + validTestTargetMonkeyNumber +
                ", invalidTestTargetMonkeyNumber=" + invalidTestTargetMonkeyNumber +
                ", inspectionCounter=" + inspectionCounter +
                '}';
    }
}
