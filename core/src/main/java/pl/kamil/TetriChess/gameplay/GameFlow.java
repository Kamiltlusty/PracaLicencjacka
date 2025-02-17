package pl.kamil.TetriChess.gameplay;

import pl.kamil.TetriChess.objects.Board;
import pl.kamil.TetriChess.objects.Team;
import pl.kamil.TetriChess.resources.Assets;

public class GameFlow {
    private Team active = Team.WHITE;
    private boolean isCheck = false;
    private boolean isCheckmate = false;
    private final Board board;

    public GameFlow(Assets assets) {
        // crate new game/board
        this.board = new Board(assets);
    }

    public void setActive() {
        this.active = active.equals(Team.WHITE) ? Team.BLACK : Team.WHITE;
    }

    public Team getActive() {
        return active;
    }

    public Board getBoard() {
        return board;
    }
}
