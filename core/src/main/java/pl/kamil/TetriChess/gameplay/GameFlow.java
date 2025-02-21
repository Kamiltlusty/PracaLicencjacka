package pl.kamil.TetriChess.gameplay;

import pl.kamil.TetriChess.objects.Board;
import pl.kamil.TetriChess.objects.Team;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.side_panel.ShapesManager;

public class GameFlow {
    private Team active = Team.WHITE;
    private boolean isCheck = false;
    private boolean isCheckmate = false;
    private final Board board;
    private final ShapesManager shapesManager;


    public GameFlow(Assets assets) {
        // crate new game/board
        this.board = new Board(assets);
        this.shapesManager = new ShapesManager(assets);
        shapesManager.generateShapes();
    }

    private void setActive() {
        this.active = active.equals(Team.WHITE) ? Team.BLACK : Team.WHITE;
    }

    public Team getActive() {
        return active;
    }

    public ShapesManager getShapesManager() {
        return shapesManager;
    }

    public Board getBoard() {
        return board;
    }

    public void prepare() {
        setActive();
        shapesManager.getShapes().poll();
        shapesManager.generateShapes();
    }
}
