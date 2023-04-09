package day23;

import utils.Position;

public class Elve {
    private Position position;

    public Position position() {return this.position;}

    public void updatePosition(Position newPosition) {
        this.position = newPosition;
    }

    public Elve(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Elve{" +
                "position=" + position +
                '}';
    }
}
