package day14;

import day09.Position;

import java.util.ArrayList;
import java.util.List;

public class Cave {
    private final List<RockPath> rockPaths;
    private final Position sandSourcePosition;
    private List<SandUnit> sandUnits;

    public Cave(List<RockPath> rockPaths, Position sandSourcePosition) {
        this.rockPaths = rockPaths;
        this.sandSourcePosition = sandSourcePosition;
        this.sandUnits = new ArrayList<>();
    }

    public void displayInConsole() {
        int minX = computeMinXFromRockPaths();
        int maxX = computeMaxXFromRockPaths();
        int maxY = computeMaxYFromRockPaths();
        int minY = 0;

        StringBuilder sb = new StringBuilder();

        sb.append("Min X = ").append(computeMinXFromRockPaths()).append("\n");
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                Position pos = new Position(x, y);
                if (this.sandUnits.stream().anyMatch(sandUnit -> sandUnit.getPosition().equals(pos) && sandUnit.isMoving())) {
                    sb.append("$");
                }
                else if (pos.equals(this.sandSourcePosition)) {
                    sb.append("+");
                }
                else if (this.isBlockedByRock(pos)) {
                    sb.append("#");
                }
                else if (this.isBlockedBySand(pos)) {
                    sb.append("o");
                }
                else {
                    sb.append(".");
                }
            }
            sb.append(" ").append(y).append("\n");
        }

        System.out.println(sb);
    }

    private boolean isBlockedByRock(Position position) {
        return this.rockPaths.stream().anyMatch(rockPath -> rockPath.contains(position));
    }

    private boolean isBlockedBySand(Position position) {
        return this.sandUnits.stream().anyMatch(sandUnit -> sandUnit.getPosition().equals(position) && ! sandUnit.isMoving());
    }

    private int computeMinXFromRockPaths() {
        return this.rockPaths.stream()
                .flatMap(rockPath -> rockPath.shapes().stream())
                .map(shape -> Math.min(shape.getStartPosition().x(), shape.getEndPosition().x()))
                .reduce(Integer.MAX_VALUE, Math::min);
    }

    private int computeMaxXFromRockPaths() {
        return this.rockPaths.stream()
                .flatMap(rockPath -> rockPath.shapes().stream())
                .map(shape -> Math.max(shape.getStartPosition().x(), shape.getEndPosition().x()))
                .reduce(0, Math::max);
    }

    private int computeMaxYFromRockPaths() {
        return this.rockPaths.stream()
                .flatMap(rockPath -> rockPath.shapes().stream())
                .map(shape -> Math.max(shape.getStartPosition().y(), shape.getEndPosition().y()))
                .reduce(0, Math::max);
    }

    /*
     Throw as many SandUnit in the cave as possible as long as each sansUnit is Rest upon the Rocks
     Return the number of SandUnit once the next one falls into the void
     */
    public int throwSandUnits() {
        int nbSandUnit = 0;
        try {
            while (true) {
                throwOneSandUnit();
                nbSandUnit++;
            }
        } catch (SandUnitFallingInTheVoidException e) {
            System.out.println(e.getMessage());
        }

        return nbSandUnit;
    }

    private void throwOneSandUnit() throws SandUnitFallingInTheVoidException {
        SandUnit sandUnit = new SandUnit(this.sandSourcePosition);
        sandUnits.add(sandUnit);
        //displayInConsole();
        do {
            //displayInConsole();

            // try immediatly below
            Position belowSandUnit = new Position(sandUnit.getPosition().x(), sandUnit.getPosition().y() + 1);
            if (this.isBlockedByRock(belowSandUnit) || this.isBlockedBySand(belowSandUnit)) {

                // try left diagonally
                Position belowLeftSandUnit = new Position(sandUnit.getPosition().x() - 1, sandUnit.getPosition().y() + 1);
                if (this.isBlockedByRock(belowLeftSandUnit) || this.isBlockedBySand(belowLeftSandUnit)) {

                    // try right diagonnally
                    Position belowRightSandUnit = new Position(sandUnit.getPosition().x() + 1, sandUnit.getPosition().y() + 1);
                    if (this.isBlockedByRock(belowRightSandUnit) || this.isBlockedBySand(belowRightSandUnit)) {
                        sandUnit.rest();
                    }
                    else {
                        sandUnit.fallRight();
                    }
                }
                else {
                    sandUnit.fallLeft();
                }
            }
            else {
                sandUnit.fall();
            }

            if (sandUnit.getPosition().y() >= computeMaxYFromRockPaths())
                throw new SandUnitFallingInTheVoidException("Ahhhhhh !!! SandUnit " + sandUnit.getPosition() + " is falling into the void !!");

        } while (sandUnit.isMoving());

        //displayInConsole();
    }
}
