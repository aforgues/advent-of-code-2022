package day22;

import day09.Position;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record MonkeyMap(Map<Integer, Row> rowsByRowNumber) {
    public List<Tile> getTilesAtColumn(int column) {
        return this.rowsByRowNumber.values().stream().map(Row::tilesInRow).flatMap(Collection::stream).filter(tile -> tile.position().x() == column).toList();
    }

    public void drawMapInConsole(Set<Position> exploredPositions) {
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
}
