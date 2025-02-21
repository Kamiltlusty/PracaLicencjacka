package pl.kamil.TetriChess.side_panel;

import io.vavr.Tuple4;

import java.util.Random;

public class ShapeValuesGenerator {
    Random rand = new Random();

    public Tuple4<Integer, Character, Integer, Integer> generate() {
        return new Tuple4<>(generateShape(), generateLetter(), generateNumber(), generateRotation());
    }

    private Integer generateRotation() {
        // generate number [1, 3]
        return rand.nextInt(3) + 1;
    }

    private Character generateLetter() {
        // generate letter [A, H]
        return (char) (rand.nextInt(6) + 'A');
    }

    private Integer generateNumber() {
        // generate number [2, 5]
        return rand.nextInt(4) + 2;
    }

    private Integer generateShape() {
        // generate number [1, 5]
        return rand.nextInt(5) + 1;
    }
}
