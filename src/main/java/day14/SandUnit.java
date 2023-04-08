package day14;

import utils.Position;

public class SandUnit {
    private Position position;
    private State state;

    public Position getPosition() {
        return position;
    }
    public boolean isMoving() {
        return this.state == State.MOVING;
    }

    public SandUnit(Position position) {
        this.position = position;
        this.state = State.MOVING;
    }

    public void fall() {
        if (this.state == State.MOVING) {
            this.position = new Position(this.position.x(), this.position.y() + 1);
        }
        else {
            throw new IllegalStateException("SandUnit cannot fall anymore as it state is already in " + this.state);
        }
    }

    public void fallLeft() {
        if (this.state == State.MOVING) {
            this.position = new Position(this.position.x() - 1, this.position.y() + 1);
        }
        else {
            throw new IllegalStateException("SandUnit cannot fall anymore as it state is already in " + this.state);
        }
    }

    public void fallRight() {
        if (this.state == State.MOVING) {
            this.position = new Position(this.position.x() + 1, this.position.y() + 1);
        }
        else {
            throw new IllegalStateException("SandUnit cannot fall anymore as it state is already in " + this.state);
        }
    }

    public void rest() {
        this.state = State.REST;
    }
}
