package day08;

import day22.RotationType;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public Direction rotate(RotationType rotationType) {
        return switch (rotationType) {
            case ROTATE_90 -> switch (this) {
                case UP -> LEFT;
                case RIGHT -> UP;
                case DOWN -> RIGHT;
                case LEFT -> DOWN;
            };
            case ROTATE_180 -> this.rotate(RotationType.ROTATE_90).rotate(RotationType.ROTATE_90);
            case ROTATE_270 -> this.rotate(RotationType.ROTATE_180).rotate(RotationType.ROTATE_90);
            default -> this;
        };
    }
}
