package day22;

import utils.Position;

import java.util.ArrayList;
import java.util.List;

public record Row(List<Tile> tilesInRow) {
    public Tile getFirstNonEmptyTile() {
        return this.tilesInRow.stream().filter(tile -> tile.type() != Type.EMPTY).findFirst().get();
    }

    public Tile getLastNonEmptyTile() {
        List<Tile> tiles = this.tilesInRow.stream().filter(tile -> tile.type() != Type.EMPTY).toList();
        return tiles.get(tiles.size() - 1);
    }

    public static Row from(String content, int row) {
        List<Tile> tilesInRow = new ArrayList<>();
        int column = 1;
        for (char c : content.toCharArray()) {
            Tile tile = new Tile(
                    switch (c) {
                        case ' ' -> Type.EMPTY;
                        case '.' -> Type.OPEN_TILE;
                        case '#' -> Type.SOLID_WALL;
                        default -> throw new IllegalArgumentException("Unknown tile type : " + c);
                    },
                    new Position(column, row));
            tilesInRow.add(tile);
            column++;
        }
        return new Row(tilesInRow);
    }
}
