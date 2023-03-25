package day22;

import day09.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonkeyMapTest {

    public MonkeyMap monkeyMap;

    @BeforeEach
    public void init() throws FileNotFoundException {
        MonkeyMapApp app = new MonkeyMapApp("src/main/resources/day22/puzzle_input_test.txt");
        monkeyMap = app.monkeyMap;
    }

    @Test
    public void shouldConvertPosition() {
        assertEquals(new Position(11, 12), monkeyMap.convertPosition(new Position(11, 12), RotationType.IDENTITY));
        assertEquals(new Position(2, 8), monkeyMap.convertPosition(new Position(2, 8), RotationType.IDENTITY));
        assertEquals(new Position(1, 11), monkeyMap.convertPosition(new Position(11, 12), RotationType.ROTATE_90));
        assertEquals(new Position(5, 2), monkeyMap.convertPosition(new Position(2, 8), RotationType.ROTATE_90));
        assertEquals(new Position(6, 1), monkeyMap.convertPosition(new Position(11, 12), RotationType.ROTATE_180));
        assertEquals(new Position(15, 5), monkeyMap.convertPosition(new Position(2, 8), RotationType.ROTATE_180));
        assertEquals(new Position(12, 6), monkeyMap.convertPosition(new Position(11, 12), RotationType.ROTATE_270));
        assertEquals(new Position(8, 15), monkeyMap.convertPosition(new Position(2, 8), RotationType.ROTATE_270));
    }

    @Test
    public void shouldUnconvertPosition() {
        assertEquals(new Position(11, 12), monkeyMap.unconvertPosition(new Position(11, 12), RotationType.IDENTITY));
        assertEquals(new Position(2, 8), monkeyMap.unconvertPosition(new Position(2, 8), RotationType.IDENTITY));
        assertEquals(new Position(11, 12), monkeyMap.unconvertPosition(new Position(1, 11), RotationType.ROTATE_90));
        assertEquals(new Position(2, 8), monkeyMap.unconvertPosition(new Position(5, 2), RotationType.ROTATE_90));
        assertEquals(new Position(11, 12), monkeyMap.unconvertPosition(new Position(6, 1), RotationType.ROTATE_180));
        assertEquals(new Position(2, 8), monkeyMap.unconvertPosition(new Position(15, 5), RotationType.ROTATE_180));
        assertEquals(new Position(11, 12), monkeyMap.unconvertPosition(new Position(12, 6), RotationType.ROTATE_270));
        assertEquals(new Position(2, 8), monkeyMap.unconvertPosition(new Position(8, 15), RotationType.ROTATE_270));
    }
}
