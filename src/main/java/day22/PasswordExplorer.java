package day22;

import day08.Direction;
import day09.Position;

import java.util.*;

public class PasswordExplorer {
    private static final boolean DEBUG = false;

    private final MonkeyMap monkeyMap;
    private final Path pathToExplore;
    private final WrappingAroundMode wrappingAroundMode;

    private Set<Position> exploredPositions;
    private Direction currentDirection = Direction.RIGHT;
    private Direction lastDirectionExplored;

    public PasswordExplorer(MonkeyMap monkeyMap, Path pathToExplore, WrappingAroundMode wrappingAroundMode) {
        this.monkeyMap = monkeyMap;
        this.pathToExplore = pathToExplore;
        this.exploredPositions = new LinkedHashSet<>();
        this.wrappingAroundMode = wrappingAroundMode;
    }

    public int explore() {
        Position currentPosition = this.computeFirstOpenTilePositionAtRow(1);
        this.exploredPositions.add(currentPosition);
        System.out.println("Start exploring monkey map, starting at " + currentPosition);

        // follow the path now
        for (Move move : this.pathToExplore.moves()) {
            System.out.println("Moving " + this.currentDirection + " in the monkey map in " + move);
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
            // first extract current position and identify on which face of the cube it is
            Position currentPosition = (Position) this.exploredPositions.toArray()[this.exploredPositions.size() - 1];
            if (DEBUG)
                System.out.println("Current position before wrapping is " + currentPosition);
            int cubeFaceSize = this.monkeyMap.rowsByRowNumber().size() / 3;
            if (DEBUG)
                System.out.println("CubeFace Size is " + cubeFaceSize);
            int faceId;
            if (currentPosition.x() <= cubeFaceSize) {
                faceId = 2;
            }
            else if (currentPosition.x() <= 2 * cubeFaceSize) {
                faceId = 3;
            }
            else if (currentPosition.x() > 3 * cubeFaceSize) {
                faceId = 6;
            }
            else {
                if (currentPosition.y() <= cubeFaceSize) {
                    faceId = 1;
                }
                else if (currentPosition.y() <= 2 * cubeFaceSize) {
                    faceId = 4;
                }
                else {
                    faceId = 5;
                }
            }
            if (DEBUG)
                System.out.println("Face Id is " + faceId);

            // Update currentDirection linked to face wrapping around and return matching wrapped around tile position
            Position nextWrappedPosition;
            switch (faceId) {
                case 1 :
                    switch (this.currentDirection) {
                        case RIGHT -> {
                            nextWrappedPosition = this.computeLastOpenTilePositionAtRow(this.monkeyMap.rowsByRowNumber().size() - currentPosition.y() + 1);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.LEFT; // on face 6, last tile at row
                            }
                            return nextWrappedPosition;
                        }
                        case LEFT -> {
                            nextWrappedPosition = this.computeFirstOpenTilePositionAtColumn(currentPosition.y() + cubeFaceSize);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.DOWN; // on face 3, first tile at column
                            }
                            return nextWrappedPosition;
                        }
                        case UP -> {
                            nextWrappedPosition = this.computeFirstOpenTilePositionAtColumn(cubeFaceSize - currentPosition.x() % cubeFaceSize + 1);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.DOWN; // on face 2, first tile at column
                            }
                            return nextWrappedPosition;
                        }
                        default -> throw new IllegalStateException("Should not wrap around from face " + faceId + " in the direction " + this.currentDirection);
                    }
                case 2 :
                    switch (this.currentDirection) {
                        case UP -> {
                            nextWrappedPosition = this.computeFirstOpenTilePositionAtColumn(cubeFaceSize - currentPosition.x() + 1 + 2 * cubeFaceSize);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.DOWN; // on face 1, first tile at column
                            }
                            return nextWrappedPosition;
                        }
                        case LEFT -> {
                            nextWrappedPosition = this.computeLastOpenTilePositionAtColumn(cubeFaceSize - currentPosition.y() + 1 + cubeFaceSize);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.UP; // on face 6, last tile at column
                            }
                            return nextWrappedPosition;
                        }
                        case DOWN -> {
                            nextWrappedPosition = this.computeLastOpenTilePositionAtColumn(cubeFaceSize - currentPosition.x() + 1);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.UP; // on face 5, last tile at column
                            }
                            return nextWrappedPosition;
                        }
                        default -> throw new IllegalStateException("Should not wrap around from face " + faceId + " in the direction " + this.currentDirection);
                    }
                case 3 :
                    switch (this.currentDirection) {
                        case UP -> {
                            nextWrappedPosition = this.computeFirstOpenTilePositionAtRow(currentPosition.x() % cubeFaceSize);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.RIGHT; // on face 1, first tile at row
                            }
                            return nextWrappedPosition;
                        }
                        case DOWN -> {
                            nextWrappedPosition = this.computeFirstOpenTilePositionAtRow(cubeFaceSize - currentPosition.x() % cubeFaceSize + 1 + 2 * cubeFaceSize);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.RIGHT; // on face 5, first tile at row
                            }
                            return nextWrappedPosition;
                        }
                        default -> throw new IllegalStateException("Should not wrap around from face " + faceId + " in the direction " + this.currentDirection);
                    }
                case 4 :
                    switch (this.currentDirection) {
                        case RIGHT -> {
                            nextWrappedPosition = this.computeFirstOpenTilePositionAtColumn(cubeFaceSize - currentPosition.y() % cubeFaceSize + 1 + 3 * cubeFaceSize);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.DOWN; // on face 6, first tile at column
                            }
                            return nextWrappedPosition;
                        }
                        default -> throw new IllegalStateException("Should not wrap around from face " + faceId + " in the direction " + this.currentDirection);
                    }
                case 5 :
                    switch (this.currentDirection) {
                        case LEFT -> {
                            nextWrappedPosition = this.computeLastOpenTilePositionAtColumn(cubeFaceSize - currentPosition.y() % cubeFaceSize + 1 + cubeFaceSize);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.UP; // on face 3, last tile at column
                            }
                            return nextWrappedPosition;
                        }
                        case DOWN -> {
                            nextWrappedPosition = this.computeLastOpenTilePositionAtColumn(cubeFaceSize - currentPosition.x() % cubeFaceSize + 1);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.UP; // on face 2, last tile at column
                            }
                            return nextWrappedPosition;
                        }
                        default -> throw new IllegalStateException("Should not wrap around from face " + faceId + " in the direction " + this.currentDirection);
                    }
                case 6 :
                    switch (this.currentDirection) {
                        case UP -> {
                            nextWrappedPosition = this.computeLastOpenTilePositionAtRow(cubeFaceSize - currentPosition.x() % cubeFaceSize + 1 + cubeFaceSize);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.LEFT; // on face 4, last tile at row
                            }
                            return nextWrappedPosition;
                        }
                        case RIGHT -> {
                            nextWrappedPosition = this.computeLastOpenTilePositionAtRow(cubeFaceSize - currentPosition.y() % cubeFaceSize + 1);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.LEFT; // on face 1, last tile at row
                            }
                            return nextWrappedPosition;
                        }
                        case DOWN -> {
                            nextWrappedPosition = this.computeFirstOpenTilePositionAtRow(cubeFaceSize - currentPosition.x() % cubeFaceSize + 1 + cubeFaceSize);
                            if (nextWrappedPosition != null) {
                                this.currentDirection = Direction.RIGHT; // on face 2, first tile at row
                            }
                            return nextWrappedPosition;
                        }
                        default -> throw new IllegalStateException("Should not wrap around from face " + faceId + " in the direction " + this.currentDirection);
                    }
                default :
                    throw new IllegalStateException("Should not wrap around from unkown face " + faceId);
            }
        }
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
