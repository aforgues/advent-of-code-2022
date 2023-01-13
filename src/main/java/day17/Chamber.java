package day17;

import day09.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Chamber {
    private static final boolean DEBUG = false;
    private static final int unitsFromTheLeftWall = 2;
    private static final int unitsFromTheHighestRockOrFloor = 3;

    private final int size;
    private final List<GasJet> gasJets;
    private Iterator<GasJet> gasJetIterator;
    private List<Rock> rocks;
    private int highestStoppedRockLevel;

    public Chamber(int size, List<GasJet> gasJets) {
        this.size = size;
        this.gasJets = gasJets;
        this.gasJetIterator = this.gasJets.iterator();
        this.rocks = new ArrayList<>();
        this.rocks.add(Rock.generateFloor(size));
        this.highestStoppedRockLevel = -1;
    }

    public int runRocksFallingWithGasJets(int nbRockFalling) {
        Iterator<RockType> rockTypeIterator = Arrays.stream(RockType.values()).iterator();
        while (rockTypeIterator.hasNext()) {
            RockType type = rockTypeIterator.next();
            this.addRock(type);
            this.fallRock();

            // do a circular
            if (! rockTypeIterator.hasNext())
                rockTypeIterator = Arrays.stream(RockType.values()).iterator();

            if (this.rocks.size() == nbRockFalling + 1) // because of the floor
                break;
        }
        return this.highestStoppedRockLevel + 1;
    }

    private void addRock(RockType rockType) {
        Rock rock  = new Rock(rockType.getPositions(), highestStoppedRockLevel +1, unitsFromTheLeftWall, unitsFromTheHighestRockOrFloor);
        this.rocks.add(rock);
        recomputeHighestStoppedRockLevel();
        if (DEBUG) {
            System.out.println("A new rock begins falling");
            this.displayInConsole();
        }
    }

    private int computeHighestLevel() {
        return this.rocks.stream().map(Rock::positions).flatMap(List::stream).map(Position::y).reduce(0, Integer::max);
    }

    private void recomputeHighestStoppedRockLevel() {
        this.highestStoppedRockLevel = this.rocks.stream().filter(rock -> rock.status() == RockStatus.STOPPED).map(Rock::positions).flatMap(List::stream).map(Position::y).reduce(this.highestStoppedRockLevel, Integer::max);
        if (DEBUG)
            System.out.println("Recomputed highest stopped rock level : " + this.highestStoppedRockLevel);
    }

    private void fallRock() {
        List<Rock> movingRocks = this.rocks.stream().filter(rock -> rock.status() == RockStatus.MOVING).toList();
        movingRocks.forEach(rock ->  {
            do {
                // Alternate until rock is stopped between :
                // - First being pushed by jet of gas
                GasJet gasJet = nextGasJet();
                if (canRockShift(rock, gasJet)) {
                    if (DEBUG)
                        System.out.println("Jet of gas pushes rock " + (gasJet == GasJet.PUSH_LEFT ? "left" : "right"));
                    rock.shift(gasJet);
                    if (DEBUG)
                        this.displayInConsole();
                }
                else {
                    if (DEBUG) {
                        System.out.println("Jet of gas pushes rock " + (gasJet == GasJet.PUSH_LEFT ? "left" : "right") + ", but nothing happens ");
                        this.displayInConsole();
                    }
                }

                // - Then falling one unit down
                if (canRockFall(rock)) {
                    if (DEBUG)
                        System.out.println("Rock falls 1 unit");
                    rock.fall();
                    if (DEBUG)
                        this.displayInConsole();
                } else {
                    if (DEBUG)
                        System.out.println("Rock falls 1 unit, causing it to come to rest");
                    rock.stop();
                    this.recomputeHighestStoppedRockLevel();
                    if (DEBUG)
                        this.displayInConsole();
                }
            } while (rock.status() == RockStatus.MOVING);
        });
    }

    private GasJet nextGasJet() {
        if (this.gasJetIterator.hasNext())
            return this.gasJetIterator.next();

        this.gasJetIterator = this.gasJets.iterator();
        return nextGasJet();
    }

    private boolean canRockShift(Rock rock, GasJet gasJet) {
        List<Position> nextRockShiftedPositions = rock.simulateNextShiftedPositions(gasJet);

        // check collision with walls
        long collisionsWithWall = nextRockShiftedPositions.stream().filter(position -> position.x() < 0 || position.x() >= this.size).count();
        if (collisionsWithWall > 0)
            return false;

        // check collision with stopped rocks
        return checkNoCollisionWithStoppedRock(nextRockShiftedPositions);
    }

    private boolean canRockFall(Rock rock) {
        int minY = rock.computeLowestLevel() - 1;
        if (minY > this.highestStoppedRockLevel)
            return true;

        // check if we have collision between falling rock and stopped rocks / floor
        return checkNoCollisionWithStoppedRock(rock.simulateNextFallingPositions());
    }

    private boolean checkNoCollisionWithStoppedRock(List<Position> nextRockPositions) {
        // check collision with stopped rocks
        List<Rock> stoppedRocks = this.rocks.stream().filter(r -> r.status() == RockStatus.STOPPED).toList();
        for (Position position : nextRockPositions) {
            if (stoppedRocks.stream().map(Rock::positions).flatMap(List::stream).filter(p -> p.y() == position.y()).anyMatch(p -> p.equals(position)))
                return false;
        }

        return true;
    }

    public void displayInConsole() {
        StringBuilder sb = new StringBuilder();

        for (int y = this.computeHighestLevel(); y >= 0; y--) {
            sb.append("|");
            for (int x = 0; x < this.size; x++) {
                Position position = new Position(x, y);
                Rock rock = matchWithRock(position);
                if (rock != null) {
                    switch (rock.status()) {
                        case MOVING -> sb.append("@");
                        case STOPPED -> sb.append("#");
                    }
                }
                else {
                    sb.append(".");
                }
            }
            sb.append("|");
            sb.append("\n");
        }
        sb.append("+");
        sb.append("-".repeat(Math.max(0, this.size)));
        sb.append("+");

        System.out.println(sb);
    }

    private Rock matchWithRock(Position currentPosition) {
        return this.rocks.stream().filter(rock -> rock.positions().contains(currentPosition)).findFirst().orElse(null);
    }
}
