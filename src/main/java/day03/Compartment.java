package day03;

import java.util.ArrayList;
import java.util.List;

public class Compartment {
    private List<Character> items;

    public Compartment(String itemsAsString) {
        this.items = new ArrayList<>();
        for (char item : itemsAsString.toCharArray()) {
            items.add(Character.valueOf(item));
        }
    }

    @Override
    public String toString() {
        return "Compartment{" +
                "items=" + items +
                '}';
    }

    public List<Character> getItems() {
        return this.items;
    }
}
