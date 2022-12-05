package day05;

import java.util.ArrayList;
import java.util.List;

public class Stack {
    private final int stackNumber;
    private List<Crate> crates;


    public Stack(int stackNumber) {
        this.stackNumber = stackNumber;
        this.crates = new ArrayList<>();
    }

    public int getStackNumber() {
        return stackNumber;
    }

    public List<Crate> getCrates() {
        return crates;
    }

    public void addCrate(char crateIdentifier) {
        this.crates.add(new Crate(crateIdentifier));
    }

    public void addCrateOnTop(Crate crate) {
        this.crates.add(0, crate);
    }

    @Override
    public String toString() {
        return "Stack{" +
                "stackNumber=" + stackNumber +
                ", crates=" + crates +
                '}';
    }
}
