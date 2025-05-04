package pl.kamil.TetriChess.ai;

public class TranspositionEntry {
    private final int eval;
    private final int depth;

    public TranspositionEntry(int eval, int depth) {
        this.eval = eval;
        this.depth = depth;
    }
    public int getEval() {
        return eval;
    }

    public int getDepth() {
        return depth;
    }
}
