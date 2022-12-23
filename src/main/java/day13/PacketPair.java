package day13;

public record PacketPair(Packet leftPacket, Packet rightPacket) {
    public boolean areInputsInTheRightOrder() {
        // extract left Value
        Value left = leftPacket.getValue();

        // extract right Value
        Value right = rightPacket.getValue();

        return left.compare(right) == 1;
    }
}
