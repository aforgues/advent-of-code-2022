package day03;

public class RuckSack {
    private Compartment firstPart;
    private Compartment secondPart;
    public RuckSack(String content) {
        this.firstPart = new Compartment(content.substring(0, content.length()/2));
        this.secondPart = new Compartment(content.substring(content.length()/2));
    }

    public Character findCommonItem() {
        for (Character item : firstPart.getItems()) {
            if (secondPart.getItems().contains(item))
                return item;
        }
        return null;
    }

    @Override
    public String toString() {
        return "RuckSack{" +
                "firstPart=" + firstPart +
                ", secondPart=" + secondPart +
                '}';
    }
}
