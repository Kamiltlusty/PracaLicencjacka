package pl.kamil.TetriChess.gameplay;

import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.CheckType;
import pl.kamil.TetriChess.board_elements.Team;
import pl.kamil.TetriChess.board_elements.figures.Figure;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.side_panel.Shape;
import pl.kamil.TetriChess.side_panel.ShapesManager;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class GameFlow {
    private final BoardManager boardManager;
    private final ShapesManager shapesManager;
    private Shape activeShape;

    private Team active = Team.WHITE;
    private CheckType checkType = CheckType.NONE;
    private boolean whiteInCheck = false;
    private boolean blackInCheck = false;
    private boolean isCheckmate = false;

    private boolean touchDownOccurred;
    private boolean touchDraggedOccurred;
    private boolean touchUpOccurred;
    private final Deque<StateRecord> stateRecordDeque = new ArrayDeque<>();


    public GameFlow(Assets assets) {
        // crate new game/board
        this.boardManager = new BoardManager(assets, this);
        this.shapesManager = new ShapesManager(assets);

        // generate shapes
        shapesManager.generateShapes();
        // set initially active shape
        this.activeShape = null;

        // set initially mouse touch occurrences
        setInitialMouseTouchOccurrence();
    }

    public void onTouchDown(int screenX, int transformedY) {
        this.touchDownOccurred = true;
        Vector2 fieldCoordinates = boardManager.detectFigureOnClick(screenX, transformedY);
        // saves all pointer positions for usage in touchDragged and touchUp
        boardManager.savePointerPositionInBoardManager(screenX, transformedY, fieldCoordinates);
    }

    public void onTouchDragged(int screenX, float transformedY) {
        this.touchDownOccurred = true;
        boardManager.moveFigureOverBoard(screenX, transformedY);
    }

    public void onTouchUp(int screenX, int transformedY) {
        this.touchUpOccurred = true;
        StateRecord record = new StateRecord(
            getActive(),
            isWhiteInCheck(),
            isBlackInCheck(),
            isCheckmate()
        );
        stateRecordDeque.addFirst(record);

        // check if move is legal on copy of game board
        boolean isValid = boardManager.validateMove(screenX, transformedY, record);
        // if move turned out to be legal execute it
        if (isValid) {
            if (boardManager.isCapture()) {
                removeCapturedFigure();
            }
            boardManager.getSelectedFigure().setPosition(
                boardManager.getFinalFieldPosition().x,
                boardManager.getFinalFieldPosition().y
            );
            boardManager.getSelectedFigure().setMoveCounter(boardManager.getSelectedFigure().getMoveCounter() + 1);
            this.prepare();
        } else {
            boardManager.UndoFigurePlacement();
            boardManager.setCapture(false);
            boardManager.setCapturedFigureId(null);
            boardManager.setSelectedFigureAsEmpty();
        }
        // prepare for next move by setting fields to initial state

        // if everything is ok puts figure on place else return false

//        if (!isValid) {
//            boardManager.UndoFigurePlacement();
//            boardManager.setCapture(false);
//            boardManager.setCapturedFigureId(null);
//            boardManager.setSelectedFigureAsEmpty();
//        } else {
//            if (boardManager.isCapture()) {
//                removeCapturedFigure();
//            }
//            figure.get().setMoveCounter(figure.get().getMoveCounter() + 1);
//            gameFlow.isCheck();
//            // prepare for next move
//            this.prepare();
//        }
        touchDownOccurred = false;
        touchDraggedOccurred = false;
        touchUpOccurred = false;
    }

    private void removeCapturedFigure() {
        Optional<Figure> capturedFigure = boardManager.getFiguresList().stream()
            .filter(f -> f.getFigureId().equals(boardManager.getCapturedFigureId()))
            .filter(f -> !f.getTeam().equals(this.getActive()))
            .findFirst();
        if (capturedFigure.isPresent()) {
            boardManager.getFiguresList().remove(capturedFigure.get());
        } else throw new RuntimeException("Figure that should be in figuresList was not found");
    }

    public void setInitialMouseTouchOccurrence() {
        touchDownOccurred = false;
        touchDraggedOccurred = false;
        touchUpOccurred = false;
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

    public boolean isTouchDownOccurred() {
        return touchDownOccurred;
    }

    public void setTouchDownOccurred(boolean touchDownOccurred) {
        this.touchDownOccurred = touchDownOccurred;
    }

    public boolean isTouchDraggedOccurred() {
        return touchDraggedOccurred;
    }

    public void setTouchDraggedOccurred(boolean touchDraggedOccurred) {
        this.touchDraggedOccurred = touchDraggedOccurred;
    }

    public boolean isTouchUpOccurred() {
        return touchUpOccurred;
    }

    public void setTouchUpOccurred(boolean touchUpOccurred) {
        this.touchUpOccurred = touchUpOccurred;
    }

    public void setActive(Team active) {
        this.active = active;
    }

    public boolean isCheckmate() {
        return isCheckmate;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public void setCheckType(CheckType checkType) {
        this.checkType = checkType;
    }
}
