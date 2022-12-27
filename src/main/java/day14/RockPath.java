package day14;

import day09.Position;

import java.util.List;

public record RockPath(List<Shape> shapes) {

    public boolean contains(Position position) {
        return this.shapes.stream().anyMatch(shape -> shape.contains(position));
    }
}
