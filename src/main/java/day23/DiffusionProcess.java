package day23;

import utils.Direction;
import utils.Position;

import java.util.*;

public class DiffusionProcess {
    private final Grove grove;
    private final List<Direction> nextPossibleDirections;

    public DiffusionProcess(Grove grove) {
        this.grove = grove;
        this.nextPossibleDirections = new ArrayList<>();
        this.nextPossibleDirections.add(Direction.NORTH);
        this.nextPossibleDirections.add(Direction.SOUTH);
        this.nextPossibleDirections.add(Direction.WEST);
        this.nextPossibleDirections.add(Direction.EAST);
    }

    public int launch() {
        System.out.println("Initial grove : ");
        this.grove.displayInConsole();

        // 10 rounds of unstable diffusion
        for (int round = 1; round <= 10; round++) {
            try {
                processRound(round, false);
            } catch (NoElvesMoveException e) {
                e.printStackTrace(); // should never happen
            }
        }

        // Finally, after 10 rounds, count the number of empty ground in smallest rectangle containing all elves
        return this.grove.countEmptyGroundInSmallestRectangle();
    }

    public int launchUntilNoElvesMove() {
        System.out.println("Initial grove : ");
        this.grove.displayInConsole();

        int round = 1;
        while(true) {
            try {
                processRound(round, true);
            } catch (NoElvesMoveException e) {
                System.out.println("No elves move at round " + round);
                this.grove.displayInConsole();
                break;
            }
            round++;
        }

        return round;
    }

    private void processRound(int round, boolean exceptionWhenNoElvesMove) throws NoElvesMoveException {
        Map<Integer, Position> nextPositionByElveIndex = new HashMap<>();

        for (int index = 0; index < this.grove.elves().size(); index++) {
            Elve elve  = this.grove.elves().get(index);

            // - First step check potential movement
            if (! checkGroveFreeAdjacentPositions(elve.position())) {
                // Propose next valid move
                for (Direction nextDirection : this.nextPossibleDirections) {
                    if (this.grove.checkFreeAdjacentPositionsAroundDirection(elve.position(), nextDirection)) {
                        nextPositionByElveIndex.put(index, elve.position().adjacentPosition(nextDirection));
                        break;
                    }
                }
            }
        }

        // - Second step : move every elve at once if possible
        int countElvesMoving = 0;
        for (int index = 0; index < this.grove.elves().size(); index++) {
            Position nextPosition = nextPositionByElveIndex.get(index);
            if (nextPositionByElveIndex.values().stream().filter(position -> position.equals(nextPosition)).count() == 1) {
                this.grove.moveElve(index, nextPosition);
                countElvesMoving++;
            }
        }

        if (exceptionWhenNoElvesMove) {
            if (countElvesMoving == 0)
                throw new NoElvesMoveException();
        }

        System.out.println("Grove after round " + round);
        //this.grove.displayInConsole();

        // End of the round : update next possible directions
        updateNextPossibleDirections();
    }

    private boolean checkGroveFreeAdjacentPositions(Position position) {
        // Check 8 adjacent positions of the given one
        for (Direction direction : Direction.values()) {
            if (! this.grove.checkFreeAdjacentPosition(position, direction)) {
                return false;
            }
        }
        return true;
    }

    private void updateNextPossibleDirections() {
        Direction first = this.nextPossibleDirections.get(0);
        this.nextPossibleDirections.remove(0);
        this.nextPossibleDirections.add(this.nextPossibleDirections.size(), first);
    }
}
