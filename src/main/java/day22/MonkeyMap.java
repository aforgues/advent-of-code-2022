package day22;

import day09.Position;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public record MonkeyMap(Map<Integer, Row> rowsByRowNumber) {
    public List<Tile> getTilesAtColumn(int column) {
        return this.rowsByRowNumber.values().stream().map(Row::tilesInRow).flatMap(Collection::stream).filter(tile -> tile.position().x() == column).toList();
    }

    public void drawMapInConsole(Collection<Position> exploredPositions) {
        StringBuffer sb = new StringBuffer();
        for (int row = 1; row < rowsByRowNumber.size() + 1; row++) {
            for (int column = 1; column < this.getMaxColumns() + 1; column++) {
                Position currentPosition = new Position(column, row);
                if (exploredPositions.contains(currentPosition)) {
                    sb.append("E");
                }
                else if (this.matchTile(currentPosition, Type.OPEN_TILE)) {
                    sb.append(".");
                }
                else if (this.matchTile(currentPosition, Type.SOLID_WALL)) {
                    sb.append("#");
                }
                else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    private int getMaxColumns() {
        return this.rowsByRowNumber.values()
                .stream()
                .map(row -> row.tilesInRow().size())
                .reduce(0, Integer::max);
    }

    private boolean matchTile(Position currentPosition, Type tileType) {
        return this.rowsByRowNumber.values()
                .stream()
                .map(Row::tilesInRow)
                .flatMap(Collection::stream)
                .filter(tile -> tile.type() == tileType)
                .anyMatch(tile -> tile.position().equals(currentPosition));
    }

    // Check that a tile exists at this position and return it, being OPEN_TILE or SOLID_WALL
    public Tile getNonEmptyTileAt(Position position) {
        if (position.x() < 1 || position.x() > getMaxColumns())
            return null;
        if (position.y() < 1 || position.y() > this.rowsByRowNumber.size())
            return null;

        return this.rowsByRowNumber.get(position.y()).tilesInRow()
                .stream()
                .filter(tile -> tile.type() == Type.OPEN_TILE || tile.type() == Type.SOLID_WALL)
                .filter(tile -> tile.position().equals(position))
                .findFirst()
                .orElse(null);
    }

    public Position convertPosition(Position source, RotationType rotationType) {
        if (rotationType == RotationType.ROTATE_270) {
            int targetX = source.y();
            int targetY = getMaxColumns() - source.x() + 1;
            return new Position(targetX, targetY);
        }
        else if (rotationType == RotationType.ROTATE_180) {
            int targetX = getMaxColumns() - source.x() + 1;
            int targetY = this.rowsByRowNumber.size() - source.y() + 1;
            return new Position(targetX, targetY);
        }
        else if (rotationType == RotationType.ROTATE_90) {
            int targetX = this.rowsByRowNumber.size() - source.y() + 1;
            int targetY = source.x();
            return new Position(targetX, targetY);
        }
        return source;
    }

    public Position unconvertPosition(Position source, RotationType rotationType) {
        // We have to :
        // - invert ROTATE_90 with ROTATE_270
        // - invert max column and max rows in each recomputing as the unconvert happen after flipping the Monkey Map (but not for ROTATE_180)
        if (rotationType == RotationType.ROTATE_90) {
            int targetX = source.y();
            int targetY = this.rowsByRowNumber.size() - source.x() + 1;
            return new Position(targetX, targetY);
        }
        else if (rotationType == RotationType.ROTATE_180) {
            int targetX = getMaxColumns() - source.x() + 1;
            int targetY = this.rowsByRowNumber.size() - source.y() + 1;
            return new Position(targetX, targetY);
        }
        else if (rotationType == RotationType.ROTATE_270) {
            int targetX = getMaxColumns() - source.y() + 1;
            int targetY = source.x();
            return new Position(targetX, targetY);
        }

        return source;
    }
}
