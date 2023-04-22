package day24;

import utils.Move;
import utils.Position;

public record Blizzard (Position currentPosition, Move moveType) {

    public Position nextPosition(Valley valley) {
        Position nextPosition = this.currentPosition.moveTo(moveType);
        if (! valley.walls().contains(nextPosition)) {
            return nextPosition;
        }
        else {
            return switch (moveType) {
                case RIGHT -> new Position(2, this.currentPosition.y());
                case LEFT -> new Position(valley.getMaxColumnInValley() - 1, this.currentPosition.y());
                case DOWN -> new Position(this.currentPosition.x(), 2);
                case UP -> new Position(this.currentPosition.x(), valley.getMaxRowInValley() - 1);
            };
        }
    }
}
