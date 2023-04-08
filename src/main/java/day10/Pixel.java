package day10;

import utils.Position;

import java.util.Objects;

public class Pixel {
    private final Position position;
    private final State state;

    public Pixel(Position position, State state) {
        this.position = position;
        this.state = state;
    }

    public Position getPosition() {
        return this.position;
    }

    public State getState() {
        return this.state;
    }

    @Override
    public String toString() {
        return "Pixel{" +
                "position=" + position +
                ", state=" + state +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pixel pixel = (Pixel) o;
        return Objects.equals(position, pixel.position) && state == pixel.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, state);
    }
}
