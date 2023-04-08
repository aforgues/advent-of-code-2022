package day10;

import utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Sprite {
    private final static int FIXED_VERTICAL_POSITION = 0;

    private List<Pixel> pixels;

    public List<Pixel> getPixels() {
        return pixels;
    }

    public Sprite(int registerValue) {
        this.pixels = new ArrayList<>(3);
        this.pixels.add(new Pixel(new Position(registerValue - 1, FIXED_VERTICAL_POSITION), State.LIT));
        this.pixels.add(new Pixel(new Position(registerValue, FIXED_VERTICAL_POSITION), State.LIT));
        this.pixels.add(new Pixel(new Position(registerValue + 1, FIXED_VERTICAL_POSITION), State.LIT));
    }

    public void move(int registerValue) {
        this.pixels.set(0, new Pixel(new Position(registerValue - 1, FIXED_VERTICAL_POSITION), State.LIT));
        this.pixels.set(1, new Pixel(new Position(registerValue, FIXED_VERTICAL_POSITION), State.LIT));
        this.pixels.set(2, new Pixel(new Position(registerValue + 1, FIXED_VERTICAL_POSITION), State.LIT));
    }

    public boolean match(Position position) {
        return this.getPixels().contains(new Pixel(new Position(position.x(), FIXED_VERTICAL_POSITION), State.LIT));
    }
}
