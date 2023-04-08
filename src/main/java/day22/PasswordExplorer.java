package day22;

import day08.Direction;
import utils.Position;

import java.util.*;
import java.util.function.Function;

public class PasswordExplorer {
    private static final boolean DEBUG = false;

    private final MonkeyMap monkeyMap;
    private final Path pathToExplore;
    private final WrappingAroundMode wrappingAroundMode;
    private final int faceSize;

    private final List<Position> exploredPositions;
    private Direction currentDirection = Direction.RIGHT;
    private Direction lastDirectionExplored;

    public PasswordExplorer(MonkeyMap monkeyMap, Path pathToExplore, WrappingAroundMode wrappingAroundMode, int faceSize) {
        this.monkeyMap = monkeyMap;
        this.pathToExplore = pathToExplore;
        this.exploredPositions = new ArrayList<>();
        this.wrappingAroundMode = wrappingAroundMode;
        this.faceSize = faceSize;
    }

    public int explore() {
        Position currentPosition = this.computeFirstOpenTilePositionAtRow(1);
        this.exploredPositions.add(currentPosition);
        System.out.println("Start exploring monkey map, starting at " + currentPosition);

        // follow the path now
        int i = 1;
        for (Move move : this.pathToExplore.moves()) {
            System.out.println(i++ + "/" + this.pathToExplore.moves().size() + " : Moving " + this.currentDirection + " in the monkey map in " + move);
            currentPosition = this.computeNextPosition(currentPosition, move);
            System.out.println("Next position is : " + currentPosition);
            if (DEBUG)
                this.monkeyMap.drawMapInConsole(this.exploredPositions);
        }

        this.monkeyMap.drawMapInConsole(this.exploredPositions);

        return computeFinalPassword(currentPosition, this.lastDirectionExplored);
    }

    private int computeFinalPassword(Position lastPositionExplored, Direction lastDirectionExplored) {
        return 1000 * lastPositionExplored.y() + 4 * lastPositionExplored.x() + computePasswordDirectionValue(lastDirectionExplored);
    }

    private int computePasswordDirectionValue(Direction lastDirectionExplored) {
        return switch (lastDirectionExplored) {
            case RIGHT -> 0;
            case DOWN -> 1;
            case LEFT -> 2;
            case UP -> 3;
        };
    }

    private Position computeNextPosition(Position currentPosition, Move move) {
        Position nextPosition = currentPosition;
        for (int step = 1; step <= move.nbStep(); step++) {
            Position tempNextPosition = switch (this.currentDirection) {
                case RIGHT -> new Position(nextPosition.x() + 1, nextPosition.y());
                case LEFT  -> new Position(nextPosition.x() - 1, nextPosition.y());
                case UP    -> new Position(nextPosition.x(), nextPosition.y() - 1);
                case DOWN  -> new Position(nextPosition.x(), nextPosition.y() + 1);
            };

            if (shouldWrapAround(tempNextPosition)) {
                if (DEBUG)
                    System.out.println("Warning : should wrap around for next position " + tempNextPosition);
                tempNextPosition = this.computeNextWrappedAroundPosition(tempNextPosition);
                if (tempNextPosition != null) {
                    nextPosition = tempNextPosition;
                    if (DEBUG)
                        System.out.println("Next wrapped position is " + nextPosition);
                }
                else {
                    if (DEBUG)
                        System.out.println("Next wrapped position is blocked (solid wall)");
                }
            }
            else {
                if (canMoveToNextPosition(tempNextPosition)) {
                    nextPosition = tempNextPosition;
                    if (DEBUG)
                        System.out.println("Next position " + nextPosition + " is accessible");
                }
                else {
                    if (DEBUG)
                        System.out.println("Next position " + tempNextPosition + " is blocked (solid wall)");
                }
            }

            this.exploredPositions.add(nextPosition);

            this.lastDirectionExplored = this.currentDirection;
        }
        this.currentDirection = computeNextDirection(move.nextRelativeOrientation());

        return nextPosition;
    }

    private Direction computeNextDirection(Turning nextRelativeOrientation) {
        return switch (this.currentDirection) {
            case RIGHT -> (nextRelativeOrientation == Turning.RIGHT ? Direction.DOWN : Direction.UP);
            case DOWN  -> (nextRelativeOrientation == Turning.RIGHT ? Direction.LEFT : Direction.RIGHT);
            case LEFT  -> (nextRelativeOrientation == Turning.RIGHT ? Direction.UP : Direction.DOWN);
            case UP    -> (nextRelativeOrientation == Turning.RIGHT ? Direction.RIGHT : Direction.LEFT);
        };
    }


    private boolean canMoveToNextPosition(Position nextPosition) {
        return this.monkeyMap.rowsByRowNumber().get(nextPosition.y()).tilesInRow().stream().noneMatch(tile -> tile.position().equals(nextPosition) && tile.type() == Type.SOLID_WALL);
    }

    private boolean shouldWrapAround(Position nextPosition) {
        // Out of map or empty tile or no tile
        return this.monkeyMap.rowsByRowNumber().get(nextPosition.y()) == null
                || this.monkeyMap.rowsByRowNumber().get(nextPosition.y()).tilesInRow().stream().filter(tile -> tile.position().equals(nextPosition) && tile.type() == Type.EMPTY).count() == 1
                || this.monkeyMap.rowsByRowNumber().get(nextPosition.y()).tilesInRow().stream().noneMatch(tile -> tile.position().equals(nextPosition));
    }

    private Position computeNextWrappedAroundPosition(Position nextPosition) {
        if (this.wrappingAroundMode == WrappingAroundMode.FLAT) {
            return switch (this.currentDirection) {
                case RIGHT -> computeFirstOpenTilePositionAtRow(nextPosition.y());
                case LEFT -> computeLastOpenTilePositionAtRow(nextPosition.y());
                case DOWN -> computeFirstOpenTilePositionAtColumn(nextPosition.x());
                case UP -> computeLastOpenTilePositionAtColumn(nextPosition.x());
            };
        }
        else {
            // first extract current position
            Position currentPosition = (Position) this.exploredPositions.toArray()[this.exploredPositions.size() - 1];
            if (DEBUG)
                System.out.println("Current position before wrapping is " + currentPosition);
            return computeNextWrappedAroundPositionInCubeMode(currentPosition);
        }
    }

    private Position computeNextWrappedAroundPositionInCubeMode(Position currentPosition) {
        return switch (this.currentDirection) {
            case RIGHT -> computeNextWrappedAroundPositionInCubeModeWithRotationType(currentPosition, RotationType.IDENTITY);
            case LEFT -> computeNextWrappedAroundPositionInCubeModeWithRotationType(currentPosition, RotationType.ROTATE_180);
            case DOWN -> computeNextWrappedAroundPositionInCubeModeWithRotationType(currentPosition, RotationType.ROTATE_270);
            case UP -> computeNextWrappedAroundPositionInCubeModeWithRotationType(currentPosition, RotationType.ROTATE_90);
        };
    }

    int computeRelativeValue(int value) {
        int faceNumber = this.computeFaceNumberForValue(value);
        return value - (faceNumber - 1) * this.faceSize;
    }

    // If we are on a face at position X and we move in cube mode, changing direction then the next position might be the symmetric of current one
    // For example, we might go in (4x4) example from position (12,6) on Face number 4 to the right direction, wrapping around to Face number 6 going Down on the position (15,9)
    // In this case, the symmetric value of y (6) will be used to define the next value for x (15) by adding starting x value (12) to symmetric of y base 4 (6 % 4 = 2) which is 3
    int computeSymmetricRelativeValue(int value) {
        int baseValue = value % this.faceSize;
        int tempValue = this.faceSize - baseValue + 1;
        return tempValue <= this.faceSize ? tempValue : tempValue % this.faceSize;
    }

    // For example, with (4x4) face size,
    // - an x or y value of 6 should return 7
    // - an x or y value of 1 should return 4
    // - an x or y value of 11 should return 10
    int computeSymmetricValueOnSameFace(int value) {
        int faceNumber = this.computeFaceNumberForValue(value);
        return this.faceSize * (faceNumber - 1) + this.computeSymmetricRelativeValue(value);
    }

    // Compute face number : first (if between 1 and faceSize), second (if between faceSize + 1 and 2 * faceSize) ...
    private int computeFaceNumberForValue(int value) {
        return value % this.faceSize == 0 ? value / this.faceSize : (value / this.faceSize + 1);
    }

    int computeFirstValueOnNextFace(int value) {
        int faceNumber = this.computeFaceNumberForValue(value);
        return this.faceSize * faceNumber + 1;
    }

    int computeLastValueOnPreviousFace(int value) {
        int faceNumber = this.computeFaceNumberForValue(value);
        return this.faceSize * (faceNumber - 1);
    }

    /**
     * If my position is currently on face F and that I go to the RIGHT,
     * Then the next wrapped around next position in cube mode could be either :
     *  - in face 1 on the upper right : moving direction to the UP (starting with the last tile of the column "x + y%faceSize")
     *  - in face 2 on the down right : moving direction to DOWN (starting with the first tile of the column "x + symmetric(y%faceSize)")
     *  - in face 3 ...
     *  - in face 4 ...
     *  - in face 5 ...
     *  - in face 6 ...
     *  - in face 7 ...
     *  - in face 8 ...
     *  - in face 9 ...
     *  - in face 10 as X ...
     *  - in face 11 as Y ...
     *
     *  |9| |7| |4|
     *  |X| | | |1|
     *  |5| | |F| ->
     *  |Y| | | |2|
     *  |8| |6| |3|
     */
    Position computeNextWrappedAroundPositionInCubeModeWithRotationType(Position currentPosition, RotationType rotationType) {
        try {
            // Check if there is some tiles on face 1
            // on the upper right : moving direction to the UP (starting with the last tile of the column "x + y%faceSize")
            Position nextPosition = this.checkAndReturnNextPositionAndUpdateCurrentDirection(currentPosition,
                    (p) -> new Position(p.x() + this.computeRelativeValue(p.y()), this.computeLastValueOnPreviousFace(p.y())), Direction.UP, rotationType);
            if (nextPosition != null) return nextPosition;

            // Check if there is some tiles on face 2
            // on the down right : moving direction to DOWN (starting with the first tile of the column "x + symmetric(y%faceSize)")
            nextPosition = this.checkAndReturnNextPositionAndUpdateCurrentDirection(currentPosition,
                    (p) -> new Position(p.x() + this.computeSymmetricRelativeValue(p.y()), this.computeFirstValueOnNextFace(p.y())), Direction.DOWN, rotationType);
            if (nextPosition != null) return nextPosition;

            // Check if there is some tiles on face 3
            // on the down right under face 2 : moving direction to LEFT (starting with the last tile of the row "symOnSameFace(y) + 2*faceSize")
            nextPosition = this.checkAndReturnNextPositionAndUpdateCurrentDirection(currentPosition,
                    (p) -> new Position(p.x() + this.faceSize, this.computeSymmetricValueOnSameFace(p.y()) + 2 * this.faceSize), Direction.LEFT, rotationType);
            if (nextPosition != null) return nextPosition;

            // Check if there is some tiles on face 4
            // on the up right above face 1 : moving direction to LEFT (starting with the last tile of the row "symOnSameFace(y) - 2*faceSize")
            nextPosition = this.checkAndReturnNextPositionAndUpdateCurrentDirection(currentPosition,
                    (p) -> new Position(p.x() + this.faceSize, this.computeSymmetricValueOnSameFace(p.y()) - 2 * this.faceSize), Direction.LEFT, rotationType);
            if (nextPosition != null) return nextPosition;

            // Check if there is some tiles on face 5
            // on the left : moving direction to RIGHT (starting with the first tile of the column "x - 4*faceSize + 1")
            nextPosition = this.checkAndReturnNextPositionAndUpdateCurrentDirection(currentPosition,
                    (p) -> new Position(p.x() - 4 * this.faceSize + 1, p.y()), Direction.RIGHT, rotationType);
            if (nextPosition != null) return nextPosition;

            // Check if there is some tiles on face 6
            // on the down left (at same row level of face 3) : moving direction to LEFT (starting with the last tile of the row "symmetric(y) + 2*faceSize")
            nextPosition = this.checkAndReturnNextPositionAndUpdateCurrentDirection(currentPosition,
                    (p) -> new Position(p.x() - this.faceSize, this.computeSymmetricValueOnSameFace(p.y()) + 2 * this.faceSize), Direction.LEFT, rotationType);
            if (nextPosition != null) return nextPosition;

            // Check if there is some tiles on face 7
            // on the up left (at same row level of face 4) : moving direction to LEFT (starting with the last tile of the row "symmetric(y) - 2*faceSize")
            nextPosition = this.checkAndReturnNextPositionAndUpdateCurrentDirection(currentPosition,
                    (p) -> new Position(p.x() - this.faceSize, this.computeSymmetricValueOnSameFace(p.y()) - 2 * this.faceSize), Direction.LEFT, rotationType);
            if (nextPosition != null) return nextPosition;

            // Check if there is some tiles on face 8
            // on the down left (at same row level of face 3 and 6, and same column level of face 5) : moving direction to RIGHT (starting with the first tile of the row "symmetric(y) + 2*faceSize")
            nextPosition = this.checkAndReturnNextPositionAndUpdateCurrentDirection(currentPosition,
                    (p) -> new Position(p.x() - 4 * this.faceSize + 1, p.y() + 2 * this.faceSize), Direction.RIGHT, rotationType);
            if (nextPosition != null) return nextPosition;


            // Check if there is some tiles on face 9
            // on the up left (at same row level of face 4 and 7, and same column level of face 5) : moving direction to RIGHT (starting with the first tile of the row "symmetric(y) - 2*faceSize")
            nextPosition = this.checkAndReturnNextPositionAndUpdateCurrentDirection(currentPosition,
                    (p) -> new Position(p.x() - 4 * this.faceSize + 1, p.y() - 2 * this.faceSize), Direction.RIGHT, rotationType);
            if (nextPosition != null) return nextPosition;

            // Check if there is some tiles on face 10
            // on the bottom left (at same row level of face 2, and same column level of face 5) : moving direction to UP (starting with the last tile of the column)
            nextPosition = this.checkAndReturnNextPositionAndUpdateCurrentDirection(currentPosition,
                    (p) -> new Position(p.x() - 4 * this.faceSize + this.computeSymmetricRelativeValue(p.y()), this.computeLastValueOnPreviousFace(p.y()) + 2 * this.faceSize), Direction.UP, rotationType);
            if (nextPosition != null) return nextPosition;

            // Check if there is some tiles on face 11
            // on the bottom left (at same row level of face 1, and same column level of face 5) : moving direction to DOWN (starting with the first tile of the column)
            nextPosition = this.checkAndReturnNextPositionAndUpdateCurrentDirection(currentPosition,
                    (p) -> new Position(p.x() - 4 * this.faceSize + this.computeSymmetricRelativeValue(p.y()), this.computeFirstValueOnNextFace(p.y()) - 2 * this.faceSize), Direction.DOWN, rotationType);
            if (nextPosition != null) return nextPosition;
        }
        catch (MonkeyMapTileWithSolidWallException e) {
            return null;
        }

        throw new IllegalStateException("No next wrapped around position going " + this.currentDirection.name().toLowerCase() + " found from current position : " + currentPosition);
    }

    private Position checkAndReturnNextPositionAndUpdateCurrentDirection(Position currentPosition, Function<Position, Position> nextWrappedAroundPositionWhileGoingRightInCubeModeFunc, Direction newDirectionWhileGoingRight, RotationType rotationType) throws MonkeyMapTileWithSolidWallException {
        // First convert current position with rotation type in order to be in the same setup as going to the right
        Position convertedCurrentPosition = this.monkeyMap.convertPosition(currentPosition, rotationType);

        // Then apply function to find nexPositionWhileGoingRight
        Position nextPositionWhileGoingRight = nextWrappedAroundPositionWhileGoingRightInCubeModeFunc.apply(convertedCurrentPosition);

        // Then unconvert target position to come back to the original direction setup comparing to going to the right
        Tile tile = this.monkeyMap.getNonEmptyTileAt(this.monkeyMap.unconvertPosition(nextPositionWhileGoingRight, rotationType));
        if (tile != null) {
            if (tile.type() == Type.OPEN_TILE) {
                this.currentDirection = newDirectionWhileGoingRight.rotate(rotationType);
                return tile.position();
            }
            throw new MonkeyMapTileWithSolidWallException();
        }
        return null;
    }

    private Position computeFirstOpenTilePositionAtRow(int row) {
        Tile firstNonEmptyTile = this.monkeyMap.rowsByRowNumber().get(row).getFirstNonEmptyTile();
        return firstNonEmptyTile.type() == Type.OPEN_TILE ? firstNonEmptyTile.position() : null;
    }

    private Position computeLastOpenTilePositionAtRow(int row) {
        Tile lastNonEmptyTile = this.monkeyMap.rowsByRowNumber().get(row).getLastNonEmptyTile();
        return lastNonEmptyTile.type() == Type.OPEN_TILE ? lastNonEmptyTile.position() : null;
    }

    private Position computeFirstOpenTilePositionAtColumn(int column) {
        Tile firstNonEmptyTile = this.monkeyMap.getTilesAtColumn(column).stream().filter(tile -> tile.type() != Type.EMPTY).findFirst().get();
        return firstNonEmptyTile.type() == Type.OPEN_TILE ? firstNonEmptyTile.position() : null;
    }

    private Position computeLastOpenTilePositionAtColumn(int column) {
        List<Tile> filteredTiles = this.monkeyMap.getTilesAtColumn(column).stream().filter(tile -> tile.type() != Type.EMPTY).toList();
        Tile lastNonEmptyTile = filteredTiles.get(filteredTiles.size() - 1);
        return lastNonEmptyTile.type() == Type.OPEN_TILE ? lastNonEmptyTile.position() : null;
    }
}
