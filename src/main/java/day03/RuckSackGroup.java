package day03;

public class RuckSackGroup {
    private final Compartment firstLine;
    private final Compartment secondLine;
    private final Compartment thirdLine;

    public RuckSackGroup(String first, String second, String third) {
        this.firstLine = new Compartment(first);
        this.secondLine = new Compartment(second);
        this.thirdLine = new Compartment(third);
    }

    public Character findCommonItem() {
        for (Character item : firstLine.getItems()) {
            if (secondLine.getItems().contains(item)) {
                if (thirdLine.getItems().contains(item))
                    return item;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "RuckSackGroup{" +
                "firstLine=" + firstLine +
                ", secondLine=" + secondLine +
                ", thirdLine=" + thirdLine +
                '}';
    }
}
