package day12;

import day09.Position;

import java.util.List;

public record Node(Position position, List<Node> parents) {
}
