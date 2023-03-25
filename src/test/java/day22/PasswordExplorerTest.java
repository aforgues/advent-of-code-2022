package day22;

import day09.Position;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordExplorerTest {

    @Test
    public void shouldComputeRelativeValue() {
        PasswordExplorer passwordExplorer = new PasswordExplorer(null, null, null, 4);

        assertEquals(1, passwordExplorer.computeRelativeValue(1));
        assertEquals(2, passwordExplorer.computeRelativeValue(2));
        assertEquals(4, passwordExplorer.computeRelativeValue(4));
        assertEquals(1, passwordExplorer.computeRelativeValue(5));
        assertEquals(2, passwordExplorer.computeRelativeValue(6));
        assertEquals(4, passwordExplorer.computeRelativeValue(8));
    }

    @Test
    public void shoudComputeSymmetricRelativeValue() {
        PasswordExplorer passwordExplorer = new PasswordExplorer(null, null, null, 4);

        assertEquals(4, passwordExplorer.computeSymmetricRelativeValue(1));
        assertEquals(3, passwordExplorer.computeSymmetricRelativeValue(2));
        assertEquals(1, passwordExplorer.computeSymmetricRelativeValue(4));
        assertEquals(4, passwordExplorer.computeSymmetricRelativeValue(5));
        assertEquals(3, passwordExplorer.computeSymmetricRelativeValue(6));
        assertEquals(1, passwordExplorer.computeSymmetricRelativeValue(8));
    }

    @Test
    public void shouldComputeSymmetricValueOnSameFace() {
        PasswordExplorer passwordExplorer = new PasswordExplorer(null, null, null, 4);

        assertEquals(4, passwordExplorer.computeSymmetricValueOnSameFace(1));
        assertEquals(1, passwordExplorer.computeSymmetricValueOnSameFace(4));
        assertEquals(8, passwordExplorer.computeSymmetricValueOnSameFace(5));
        assertEquals(7, passwordExplorer.computeSymmetricValueOnSameFace(6));
        assertEquals(5, passwordExplorer.computeSymmetricValueOnSameFace(8));
        assertEquals(10, passwordExplorer.computeSymmetricValueOnSameFace(11));
    }

    @Test
    public void shouldComputeFirstValueOneNextFace() {
        PasswordExplorer passwordExplorer = new PasswordExplorer(null, null, null, 4);

        assertEquals(5, passwordExplorer.computeFirstValueOnNextFace(1));
        assertEquals(5, passwordExplorer.computeFirstValueOnNextFace(4));
        assertEquals(9, passwordExplorer.computeFirstValueOnNextFace(5));
        assertEquals(9, passwordExplorer.computeFirstValueOnNextFace(6));
        assertEquals(9, passwordExplorer.computeFirstValueOnNextFace(8));
        assertEquals(13, passwordExplorer.computeFirstValueOnNextFace(11));
    }

    @Test
    public void shouldComputeLastValueOnPreviousFace() {
        PasswordExplorer passwordExplorer = new PasswordExplorer(null, null, null, 4);

        assertEquals(0, passwordExplorer.computeLastValueOnPreviousFace(1));
        assertEquals(0, passwordExplorer.computeLastValueOnPreviousFace(4));
        assertEquals(4, passwordExplorer.computeLastValueOnPreviousFace(5));
        assertEquals(4, passwordExplorer.computeLastValueOnPreviousFace(6));
        assertEquals(4, passwordExplorer.computeLastValueOnPreviousFace(8));
        assertEquals(8, passwordExplorer.computeLastValueOnPreviousFace(11));
    }

    @Test
    public void shouldComputeNextWrappedAroundPositionInCubeModeWithRotationType() throws FileNotFoundException {
        MonkeyMapApp app = new MonkeyMapApp("src/main/resources/day22/puzzle_input.txt");
        PasswordExplorer passwordExplorer = new PasswordExplorer(app.monkeyMap, app.path, WrappingAroundMode.CUBE, 50);

        assertEquals(new Position(104,50), passwordExplorer.computeNextWrappedAroundPositionInCubeModeWithRotationType(new Position(100, 54), RotationType.IDENTITY), "Check on Face 1");
        assertEquals(new Position(51,65), passwordExplorer.computeNextWrappedAroundPositionInCubeModeWithRotationType(new Position(15, 101), RotationType.ROTATE_90), "Check on Face 2");
        //assertEquals(new Position(51,65), passwordExplorer.computeNextWrappedAroundPositionInCubeModeWithRotationType(new Position(15, 101), RotationType.ROTATE_90), "Check on Face 3"); // No use case with 50x50 puzzle
        assertEquals(new Position(150,4), passwordExplorer.computeNextWrappedAroundPositionInCubeModeWithRotationType(new Position(100, 147), RotationType.IDENTITY), "Check on Face 4");
        //assertEquals(new Position(150,4), passwordExplorer.computeNextWrappedAroundPositionInCubeModeWithRotationType(new Position(100, 147), RotationType.IDENTITY), "Check on Face 5"); // No use case with 50x50 puzzle
        assertEquals(new Position(100,147), passwordExplorer.computeNextWrappedAroundPositionInCubeModeWithRotationType(new Position(150, 4), RotationType.IDENTITY), "Check on Face 6");
        //assertEquals(new Position(100,147), passwordExplorer.computeNextWrappedAroundPositionInCubeModeWithRotationType(new Position(150, 4), RotationType.IDENTITY), "Check on Face 7"); // No use case with 50x50 puzzle
        //assertEquals(new Position(100,147), passwordExplorer.computeNextWrappedAroundPositionInCubeModeWithRotationType(new Position(150, 4), RotationType.IDENTITY), "Check on Face 8"); // No use case with 50x50 puzzle
        assertEquals(new Position(112,1), passwordExplorer.computeNextWrappedAroundPositionInCubeModeWithRotationType(new Position(12, 200), RotationType.ROTATE_270), "Check on Face 9");
        //assertEquals(new Position(112,1), passwordExplorer.computeNextWrappedAroundPositionInCubeModeWithRotationType(new Position(12, 200), RotationType.ROTATE_270), "Check on Face 10"); // No use case with 50x50 puzzle
        //assertEquals(new Position(112,1), passwordExplorer.computeNextWrappedAroundPositionInCubeModeWithRotationType(new Position(12, 200), RotationType.ROTATE_270), "Check on Face 11"); // No use case with 50x50 puzzle
    }
}
