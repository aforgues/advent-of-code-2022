package day13;

public class Packet {
    private final Value value;

    public Value getValue() {
        return this.value;
    }

    public Packet(String content) {
        this.value = new Value(content);
    }

    @Override
    public String toString() {
        return "Packet{" +
                "value=" + value +
                '}';
    }
}
