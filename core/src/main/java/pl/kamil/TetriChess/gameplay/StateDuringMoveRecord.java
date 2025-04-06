package pl.kamil.TetriChess.gameplay;

import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.BoardUtils;
import pl.kamil.TetriChess.board_elements.Team;
import pl.kamil.TetriChess.board_elements.figures.Figure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateDuringMoveRecord {
    private Team active;
    private boolean whiteInCheck;
    private boolean blackInCheck;
    private boolean isCheckmate;
    private final BoardManager boardManager;
    private final BoardUtils boardUtils;
    private Map<String, Figure> boardState;

    public StateDuringMoveRecord(Team active, boolean whiteInCheck, boolean blackInCheck, boolean isCheckmate, BoardManager boardManager, BoardUtils boardUtils) {
        this.active = active;
        this.whiteInCheck = whiteInCheck;
        this.blackInCheck = blackInCheck;
        this.isCheckmate = isCheckmate;
        this.boardManager = boardManager;
        this.boardUtils = boardUtils;
        boardState = new HashMap<>();
        boardState = readAllFiguresPositions();
    }

    private Map<String, Figure> readAllFiguresPositions() {
        List<Figure> figuresList = boardManager.getFiguresList();
        Map<String, Figure> figuresMap = new HashMap<>();
        for (Figure figure : figuresList) {
            Vector2 figurePosition = figure.getPosition();
            String signature = boardUtils.findFieldSignature((int) figurePosition.x, (int) figurePosition.y);
            figuresMap.put(signature, figure);
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
}
