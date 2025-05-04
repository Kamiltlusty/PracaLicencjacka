package pl.kamil.TetriChess.ai;

import java.util.HashMap;
import java.util.Map;

public class TranspositionTable {
    private final Map<String, TranspositionEntry> table = new HashMap<>();

    public void put(String hash, int eval, int depth) {
        TranspositionEntry existing = table.get(hash);
        if (existing == null || existing.getDepth() <= depth) {
            table.put(hash, new TranspositionEntry(eval, depth));
        }
    }

    public TranspositionEntry get(String hash) {
        return table.get(hash);
    }

    public boolean contains(String hash) {
        return table.containsKey(hash);
    }
}
