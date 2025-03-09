package pl.kamil.TetriChess.gameplay;

import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.side_panel.Shape;
import pl.kamil.TetriChess.side_panel.ShapesManager;

public class GameFlow {
    private Team active = Team.WHITE;
    private boolean isCheck = false;
    private boolean isCheckmate = false;
    private final BoardManager board;
    private final ShapesManager shapesManager;
    private Shape activeShape;


    public GameFlow(Assets assets) {
        // crate new game/board
        this.board = new BoardManager(assets);
        this.shapesManager = new ShapesManager(assets);
        shapesManager.generateShapes();
        this.activeShape = null;
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

    public BoardManager getBoard() {
        return board;
    }

    public Shape getActiveShape() {
        return activeShape;
    }

    public void prepare() {
        setActive();
        board.setAllFieldsFree();
        activeShape = shapesManager.getShapes().pollFirst();
        shapesManager.generateShapes();
    }
}
