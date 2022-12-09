package day09;

import java.util.ArrayList;
import java.util.List;

public class TailPath {
    private List<Position> positions;

    public List<Position> getPositions() {
        return positions;
    }

    public TailPath(Position initialPosition) {
        this.positions = new ArrayList<>();
        this.positions.add(initialPosition);
    }

    public void addPosition(Position newPosition) {
        this.positions.add(newPosition);
    }

    @Override
    public String toString() {
        return "TailPath{" +
                "positions=" + positions +
                '}';
    }
}
