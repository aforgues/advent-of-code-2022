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

    public Set<Type> getConnectedSideTypes(Cube otherCube, boolean includeAlmostConnectedTypes) {
        Set<Type> connectedTypes = new HashSet<>();
        Set<Type> almostConnectedTypes = new HashSet<>();

        // check back side with front side of other cube
        Side backSide = this.sides.get(Type.BACK);
        Side otherFrontSide = otherCube.sides.get(Type.FRONT);
        if (this.position.x() == otherCube.position.x()
                && this.position.y() == otherCube.position.y()) {
            if(backSide.depth() - otherFrontSide.depth() == 1)
                almostConnectedTypes.add(Type.BACK);
            if (backSide.equals(otherFrontSide))
                connectedTypes.add(Type.BACK);
        }

        // check front side with back side of other cube
        Side frontSide = this.sides.get(Type.FRONT);
        Side otherBackSide = otherCube.sides.get(Type.BACK);
        if (this.position.x() == otherCube.position.x()
                && this.position.y() == otherCube.position.y()) {
            if (frontSide.depth() - otherBackSide.depth() == -1)
                almostConnectedTypes.add(Type.FRONT);
            if (frontSide.equals(otherBackSide))
                connectedTypes.add(Type.FRONT);
        }

        // check left side with right side of other cube
        Side leftSide = this.sides.get(Type.LEFT);
        Side otherRightSide = otherCube.sides.get(Type.RIGHT);
        if (this.position.y() == otherCube.position.y()
                && this.position.z() == otherCube.position.z()) {
            if (leftSide.depth() - otherRightSide.depth() == 1)
                almostConnectedTypes.add(Type.LEFT);
            if (leftSide.equals(otherRightSide))
                connectedTypes.add(Type.LEFT);
        }

        // check right side with left side of other cube
        Side rightSide = this.sides.get(Type.RIGHT);
        Side otherLeftSide = otherCube.sides.get(Type.LEFT);
        if (this.position.y() == otherCube.position.y()
                && this.position.z() == otherCube.position.z()) {
            if (rightSide.depth() - otherLeftSide.depth() == -1)
                almostConnectedTypes.add(Type.RIGHT);
            if (rightSide.equals(otherLeftSide))
                connectedTypes.add(Type.RIGHT);
        }

        // check bottom side with top side of other cube
        Side bottomSide = this.sides.get(Type.BOTTOM);
        Side otherTopSide = otherCube.sides.get(Type.TOP);
        if (this.position.x() == otherCube.position.x()
                && this.position.z() == otherCube.position.z()) {
            if (bottomSide.depth() - otherTopSide.depth() == 1)
                almostConnectedTypes.add(Type.BOTTOM);
            if (bottomSide.equals(otherTopSide))
                connectedTypes.add(Type.BOTTOM);
        }

        // check top side with bottom side of other cube
        Side topSide = this.sides.get(Type.TOP);
        Side otherBottomSide = otherCube.sides.get(Type.BOTTOM);
        if (this.position.x() == otherCube.position.x()
                && this.position.z() == otherCube.position.z()) {
            if (topSide.depth() - otherBottomSide.depth() == -1)
                almostConnectedTypes.add(Type.TOP);
            if (topSide.equals(otherBottomSide))
                connectedTypes.add(Type.TOP);
        }

        System.out.println("Cube at " + this.position + " has " + connectedTypes + " sides connected to other cube at " + otherCube.position);
        System.out.println("Cube at " + this.position + " has " + almostConnectedTypes + " sides almost connected to other cube at " + otherCube.position);
        if (includeAlmostConnectedTypes)
            connectedTypes.addAll(almostConnectedTypes);
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
