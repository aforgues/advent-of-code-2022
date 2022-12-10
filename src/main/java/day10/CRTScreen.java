package day10;

import day09.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CRTScreen {
    public static final int MAX_PIXELS_PER_LINE = 40;
    private List<Pixel> pixels;
    private Sprite sprite;

    public CRTScreen(int registerValue) {
        this.pixels = new ArrayList<>();
        this.sprite = new Sprite(registerValue);
    }

    public void drawPixel(int cycleCount, int registerValue) {
        Position position = new Position(cycleCount % MAX_PIXELS_PER_LINE - 1, cycleCount / MAX_PIXELS_PER_LINE);
        sprite.move(registerValue);

        State state = this.sprite.match(position) ? State.LIT : State.DARK;
        this.pixels.add(new Pixel(position, state));
    }

    public String displaySprite() {
        StringBuilder display = new StringBuilder();
        for (int x = 0; x < MAX_PIXELS_PER_LINE; x++) {
           if (this.sprite.match(new Position(x, 0))) {
                display.append("#");
            }
            else {
                display.append(".");
            }
        }
        return display.toString();
    }

    public String displayCRT() {
        StringBuilder display = new StringBuilder();
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < MAX_PIXELS_PER_LINE; x++) {
                Optional<Pixel> optionalPixel = getPixelAt(x, y);
                if (optionalPixel.isPresent()) {
                    display.append(optionalPixel.get().getState().getValue());
                }
                else {
                    display.append(State.DARK.getValue());
                }
            }
            display.append("\n");
        }
        return display.toString();
    }

    private Optional<Pixel> getPixelAt(int x, int y) {
        return this.pixels.stream().filter(p -> p.getPosition().x() == x && p.getPosition().y() == y).findFirst();
    }
}
