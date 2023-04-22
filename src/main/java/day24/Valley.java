package day24;

import utils.Position;

import java.util.List;

public record Valley(List<Position> walls) {
    public void displayInConsole(List<Blizzard> blizzards, Position expeditionPosition) {
        StringBuffer sb = new StringBuffer();
        for (int row = 1; row <= getMaxRowInValley(); row++) {
            for (int column = 1; column <= getMaxColumnInValley(); column++) {
                Position currentPosition = new Position(column, row);
                if (expeditionPosition.equals(currentPosition)) {
                    sb.append("E");
                }
                else if (this.walls.stream().filter(position -> position.equals(currentPosition)).count() == 1) {
                    sb.append("#");
                }
                else {
                    List<Blizzard> blizzardsAtCurrentPosition = blizzards.stream().filter(blizzard -> blizzard.currentPosition().equals(currentPosition)).toList();
                    if (blizzardsAtCurrentPosition.isEmpty()) {
                        sb.append(".");
                    }
                    else if (blizzardsAtCurrentPosition.size() > 1){
                        sb.append(blizzardsAtCurrentPosition.size());
                    }
                    else {
                        sb.append(blizzardsAtCurrentPosition.get(0).moveType().getSign());
                    }
                }
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public int getMaxRowInValley() {
        return this.walls.stream().map(Position::y).reduce(0, Integer::max);
    }

    public int getMaxColumnInValley() {
        return this.walls.stream().map(Position::x).reduce(0, Integer::max);
    }

    public boolean isPositionInsideTheValley(Position position) {
        if (this.walls.stream().anyMatch(wallPosition -> wallPosition.equals(position)))
            return false;
        return position.x() >= 1 && position.x() <= getMaxColumnInValley()
                && position.y() >= 1 && position.y() <= getMaxRowInValley();
    }
}
