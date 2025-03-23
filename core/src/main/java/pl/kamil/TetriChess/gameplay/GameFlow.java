package pl.kamil.TetriChess.gameplay;

import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.side_panel.Shape;
import pl.kamil.TetriChess.side_panel.ShapesManager;

public class GameFlow {
    private Team active = Team.WHITE;
    private boolean whiteInCheck = false;
    private boolean blackInCheck = false;
    private boolean isCheckmate = false;
    private final BoardManager boardManager;
    private final ShapesManager shapesManager;
    private Shape activeShape;


    public GameFlow(Assets assets) {
        // crate new game/board
        this.boardManager = new BoardManager(assets, this);
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

    public BoardManager getBoardManager() {
        return boardManager;
    }

    public Shape getActiveShape() {
        return activeShape;
    }

    public void prepare() {
        if (isCheckmate) System.exit(0);
        setActive();
        boardManager.setCapture(false);
        boardManager.setCapturedFigureId(null);
        boardManager.setSelectedFigureAsEmpty();
        boardManager.setAllFieldsFree();
        activeShape = shapesManager.getShapes().pollFirst();
        shapesManager.generateShapes();
    }


    public boolean isWhiteInCheck() {
        return whiteInCheck;
    }

    public void setWhiteInCheck(boolean whiteInCheck) {
        this.whiteInCheck = whiteInCheck;
    }

    public boolean isBlackInCheck() {
        return blackInCheck;
    }

    public void setBlackInCheck(boolean blackInCheck) {
        this.blackInCheck = blackInCheck;
    }
}
