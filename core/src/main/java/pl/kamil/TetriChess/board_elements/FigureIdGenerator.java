package pl.kamil.TetriChess.board_elements;

public class FigureIdGenerator {
    private static long nextId = 1;

    public static long generate() {
        return nextId++;
    }
}
