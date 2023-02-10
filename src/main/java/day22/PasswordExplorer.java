package day22;

import day08.Direction;
import day09.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PasswordExplorer {
    private final MonkeyMap monkeyMap;
    private final Path pathToExplore;
    private Set<Position> exploredPositions;
    private Direction lastDirectionExplored;

    public PasswordExplorer(MonkeyMap monkeyMap, Path pathToExplore) {
        this.monkeyMap = monkeyMap;
        this.pathToExplore = pathToExplore;
        this.exploredPositions = new HashSet<>();
    }

    public int explore() {
        Position currentPosition = this.computeFirstOpenTilePositionAtRow(1);
        this.exploredPositions.add(currentPosition);
        System.out.println("Start exploring monkey map, starting at " + currentPosition);

        // follow the path now
        for (Move move : this.pathToExplore.moves()) {
            System.out.println("Moving in the monkey map in " + move);
            currentPosition = this.computeNextPosition(currentPosition, move);
            System.out.println("Next position is : " + currentPosition);
            //this.monkeyMap.drawMapInConsole(this.exploredPositions);
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
            Position tempNextPosition = switch (move.direction()) {
                case RIGHT -> new Position(nextPosition.x() + 1, nextPosition.y());
                case LEFT  -> new Position(nextPosition.x() - 1, nextPosition.y());
                case UP    -> new Position(nextPosition.x(), nextPosition.y() - 1);
                case DOWN  -> new Position(nextPosition.x(), nextPosition.y() + 1);
            };

            if (shouldWrapAround(tempNextPosition)) {
                //System.out.println("Warning : should wrap around for next position " + tempNextPosition);
                tempNextPosition = this.computeNextWrappedAroundPosition(tempNextPosition, move);
                if (tempNextPosition != null) {
                    nextPosition = tempNextPosition;
                    //System.out.println("Next wrapped position is " + nextPosition);
                }
                else {
                    //System.out.println("Next wrapped position " + tempNextPosition + " is blocked (solid wall)");
                }
            }
            else {
                if (canMoveToNextPosition(tempNextPosition)) {
                    nextPosition = tempNextPosition;
                    //System.out.println("Next position " + nextPosition + " is accessible");
                }
                else {
                    //System.out.println("Next position " + tempNextPosition + " is blocked (solid wall)");
                }
            }

            this.exploredPositions.add(nextPosition);
            this.lastDirectionExplored = move.direction();
        }

        return nextPosition;
    }

    private boolean canMoveToNextPosition(Position nextPosition) {
        return this.monkeyMap.rowsByRowNumber().get(nextPosition.y()).tilesInRow().stream().noneMatch(tile -> tile.position().equals(nextPosition) && tile.type() == Type.SOLID_WALL);
    }

    private boolean shouldWrapAround(Position nextPosition) {
        // Empty tile or no tile
        return this.monkeyMap.rowsByRowNumber().get(nextPosition.y()) == null
                || this.monkeyMap.rowsByRowNumber().get(nextPosition.y()).tilesInRow().stream().filter(tile -> tile.position().equals(nextPosition) && tile.type() == Type.EMPTY).count() == 1
                || this.monkeyMap.rowsByRowNumber().get(nextPosition.y()).tilesInRow().stream().noneMatch(tile -> tile.position().equals(nextPosition));
    }

    private Position computeNextWrappedAroundPosition(Position nextPosition, Move move) {
        return switch (move.direction()) {
            case RIGHT -> computeFirstOpenTilePositionAtRow(nextPosition.y());
            case LEFT -> computeLastOpenTilePositionAtRow(nextPosition.y());
            case DOWN -> computeFirstOpenTilePositionAtColumn(nextPosition.x());
            case UP -> computeLastOpenTilePositionAtColumn(nextPosition.x());
        };
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
