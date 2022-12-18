package day18;

import java.util.*;

/*
      | y
      |
      |
      |_________ x
     /
    /
   / z

 */
public class Cube {
    private final Position3D position;
    private final EnumMap<Type, Side> sides;

    public Cube(Position3D position) {
        this.position = position;
        this.sides = new EnumMap(Type.class);
        this.initSides();
    }

    private void initSides() {
        for (Type type : Type.values()) {
            Side side = switch (type) {
                case BACK -> new Side(Axis.Z, position.z());
                case FRONT -> new Side(Axis.Z, position.z() + 1);
                case LEFT -> new Side(Axis.X, position.x());
                case RIGHT -> new Side(Axis.X, position.x() + 1);
                case BOTTOM -> new Side(Axis.Y, position.y());
                case TOP -> new Side(Axis.Y, position.y() + 1);
            };
            this.sides.put(type, side);
        }
    }

    public Set<Type> getConnectedSideTypes(Cube otherCube) {
        Set<Type> connectedTypes = new HashSet<>();
        int count = 0;

        // check back side with front side of other cube
        Side backSide = this.sides.get(Type.BACK);
        Side otherFrontSide = otherCube.sides.get(Type.FRONT);
        if (backSide.equals(otherFrontSide)
                && this.position.x() == otherCube.position.x()
                    && this.position.y() == otherCube.position.y()) {
            count++;
            connectedTypes.add(Type.BACK);
        }

        // check front side with back side of other cube
        Side frontSide = this.sides.get(Type.FRONT);
        Side otherBackSide = otherCube.sides.get(Type.BACK);
        if (frontSide.equals(otherBackSide)
                && this.position.x() == otherCube.position.x()
                && this.position.y() == otherCube.position.y()) {
            count++;
            connectedTypes.add(Type.FRONT);
        }

        // check left side with right side of other cube
        Side leftSide = this.sides.get(Type.LEFT);
        Side otherRightSide = otherCube.sides.get(Type.RIGHT);
        if (leftSide.equals(otherRightSide)
                && this.position.y() == otherCube.position.y()
                && this.position.z() == otherCube.position.z()) {
            count++;
            connectedTypes.add(Type.LEFT);
        }

        // check right side with left side of other cube
        Side rightSide = this.sides.get(Type.RIGHT);
        Side otherLeftSide = otherCube.sides.get(Type.LEFT);
        if (rightSide.equals(otherLeftSide)
                && this.position.y() == otherCube.position.y()
                && this.position.z() == otherCube.position.z()) {
            count++;
            connectedTypes.add(Type.RIGHT);
        }

        // check top side with bottom side of other cube
        Side topSide = this.sides.get(Type.TOP);
        Side otherBottomSide = otherCube.sides.get(Type.BOTTOM);
        if (topSide.equals(otherBottomSide)
                && this.position.x() == otherCube.position.x()
                && this.position.z() == otherCube.position.z()) {
            count++;
            connectedTypes.add(Type.TOP);
        }

        // check bottom side with top side of other cube
        Side bottomSide = this.sides.get(Type.BOTTOM);
        Side otherTopSide = otherCube.sides.get(Type.TOP);
        if (bottomSide.equals(otherTopSide)
                && this.position.x() == otherCube.position.x()
                && this.position.z() == otherCube.position.z()) {
            count++;
            connectedTypes.add(Type.BOTTOM);
        }

        //System.out.println("Cube " + this + " has " + count + " sides connected to " + otherCube);
        return connectedTypes;
    }

    @Override
    public String toString() {
        return "Cube{" +
                "position=" + position +
                ", sides=" + sides +
                '}';
    }
}
