package pl.kamil.TetriChess.gameplay;

import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.BoardUtils;
import pl.kamil.TetriChess.board_elements.Team;
import pl.kamil.TetriChess.board_elements.figures.Figure;
import pl.kamil.TetriChess.side_panel.Shape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateBeforeMoveRecord {
    private Team active;
    private boolean whiteInCheck;
    private boolean blackInCheck;
    private boolean isCheckmate;
    private final BoardManager boardManager;
    private final BoardUtils boardUtils;
    private Map<Figure, String> boardState;
    private Shape activeShape;


    public StateBeforeMoveRecord(Team active, boolean whiteInCheck, boolean blackInCheck, boolean isCheckmate, BoardManager boardManager, BoardUtils boardUtils, Shape activeShape) {
        this.active = active;
        this.whiteInCheck = whiteInCheck;
        this.blackInCheck = blackInCheck;
        this.isCheckmate = isCheckmate;
        this.boardManager = boardManager;
        this.boardUtils = boardUtils;
        this.activeShape = activeShape;
        boardState = new HashMap<>();
        boardState = readAllFiguresPositions();
    }

    private Map<Figure, String> readAllFiguresPositions() {
        List<Figure> figuresList = boardManager.getFiguresList();
        Map<Figure, String> figuresMap = new HashMap<>();
        for (Figure figure : figuresList) {
            Vector2 figurePosition = figure.getPosition();
            String signature = boardUtils.findFieldSignatureByCoordinates((int) figurePosition.x, (int) figurePosition.y);
            figuresMap.put(figure, signature);
        }
        return figuresMap;
    }

    public boolean isWhiteInCheck() {
        return whiteInCheck;
    }

    public boolean isBlackInCheck() {
        return blackInCheck;
    }

    public boolean isCheckmate() {
        return isCheckmate;
    }

    public Team getActive() {
        return active;
    }

    public void setActive(Team active) {
        this.active = active;
    }

    public void setWhiteInCheck(boolean whiteInCheck) {
        this.whiteInCheck = whiteInCheck;
    }

    public void setBlackInCheck(boolean blackInCheck) {
        this.blackInCheck = blackInCheck;
    }

    public void setCheckmate(boolean checkmate) {
        isCheckmate = checkmate;
    }

    public Map<Figure, String> getBoardState() {
        return boardState;
    }

    public Shape getActiveShape() {
        return activeShape;
    }

    public void setActiveShape(Shape activeShape) {
        this.activeShape = activeShape;
    }
}
