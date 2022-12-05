package day05;

public class Crate {

    private final char identifier;


    public Crate(char identifier) {
        this.identifier = identifier;
    }

    public char getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "Crate{" +
                "identifier=" + identifier +
                '}';
    }
}
