package day23;

import utils.Direction;
import utils.Position;

import java.util.List;

public record Grove(List<Elve> elves) {
    public int countEmptyGroundInSmallestRectangle() {
        int minX = computeMinElveColumnPosition();
        int maX = computeMaxElveColumnPosition();
        int minY = computeMinElveRowPosition();
        int maxY = computeMaxElveRowPosition();
        return (maX - minX + 1) * (maxY - minY + 1) - this.elves.size();
    }

    private int computeMinElveColumnPosition() {
        return this.elves.stream().map(e -> e.position().x()).reduce(Integer.MAX_VALUE, Math::min);
    }

    private int computeMaxElveColumnPosition() {
        return this.elves.stream().map(e -> e.position().x()).reduce(Integer.MIN_VALUE, Math::max);
    }

    private int computeMinElveRowPosition() {
        return this.elves.stream().map(e -> e.position().y()).reduce(Integer.MAX_VALUE, Math::min);
    }

    private int computeMaxElveRowPosition() {
        return this.elves.stream().map(e -> e.position().y()).reduce(Integer.MIN_VALUE, Math::max);
    }

    public void displayInConsole() {
        StringBuffer sb = new StringBuffer();

        for (int y = computeMinElveRowPosition(); y <= computeMaxElveRowPosition(); y++) {
            for (int x = computeMinElveColumnPosition(); x <= computeMaxElveColumnPosition(); x++) {
                Position position = new Position(x, y);
                if (this.elves.stream().map(Elve::position).anyMatch(p -> p.equals(position))) {
                    sb.append("#");
                }
                else {
                    sb.append(".");
                }
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public boolean checkFreeAdjacentPosition(Position position, Direction direction) {
        Position adjacentPosition = position.adjacentPosition(direction);
        return this.elves.stream().map(Elve::position).noneMatch(p -> p.equals(adjacentPosition));
    }

    public boolean checkFreeAdjacentPositionsAroundDirection(Position position, Direction nextDirection) {
        return switch (nextDirection) {
            case NORTH -> checkFreeAdjacentPosition(position, Direction.NORTH) && checkFreeAdjacentPosition(position, Direction.NORTH_WEST) && checkFreeAdjacentPosition(position, Direction.NORTH_EAST);
            case SOUTH -> checkFreeAdjacentPosition(position, Direction.SOUTH) && checkFreeAdjacentPosition(position, Direction.SOUTH_WEST) && checkFreeAdjacentPosition(position, Direction.SOUTH_EAST);
            case WEST -> checkFreeAdjacentPosition(position, Direction.WEST) && checkFreeAdjacentPosition(position, Direction.SOUTH_WEST) && checkFreeAdjacentPosition(position, Direction.NORTH_WEST);
            case EAST -> checkFreeAdjacentPosition(position, Direction.EAST) && checkFreeAdjacentPosition(position, Direction.NORTH_EAST) && checkFreeAdjacentPosition(position, Direction.SOUTH_EAST);
            default -> throw new IllegalArgumentException();
        };
    }

    public void moveElve(int index, Position nextPosition) {
        Elve elve = this.elves.get(index);
        elve.updatePosition(nextPosition);
    }
}
