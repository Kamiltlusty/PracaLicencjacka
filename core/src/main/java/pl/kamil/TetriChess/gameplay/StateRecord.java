package pl.kamil.TetriChess.gameplay;

import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.Team;
import pl.kamil.TetriChess.board_elements.figures.Figure;

public class StateRecord {
    private Team active;
    private boolean whiteInCheck;
    private boolean blackInCheck;
    private boolean isCheckmate;
    private Figure selectedFigure;
    private String capturedFigureId;
    private boolean isCapture;
    private Vector2 initialFieldPosition;
    private Vector2 finalFieldPosition;


    public StateRecord(Team active, boolean whiteInCheck, boolean blackInCheck, boolean isCheckmate) {
        this.active = active;
        this.whiteInCheck = whiteInCheck;
        this.blackInCheck = blackInCheck;
        this.isCheckmate = isCheckmate;
        this.selectedFigure = null;
        this.capturedFigureId = null;
        this.isCapture = false;
        this.initialFieldPosition = new Vector2();
        this.finalFieldPosition = new Vector2();
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

    public Figure getSelectedFigure() {
        return selectedFigure;
    }

    public String getCapturedFigureId() {
        return capturedFigureId;
    }

    public boolean isCapture() {
        return isCapture;
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

    public void setSelectedFigure(Figure selectedFigure) {
        this.selectedFigure = selectedFigure;
    }

    public void setCapturedFigureId(String capturedFigureId) {
        this.capturedFigureId = capturedFigureId;
    }

    public void setCapture(boolean capture) {
        isCapture = capture;
    }

    public Vector2 getInitialFieldPosition() {
        return initialFieldPosition;
    }

    public void setInitialFieldPosition(float x, float y) {
        this.initialFieldPosition = initialFieldPosition;
    }

    public Vector2 getFinalFieldPosition() {
        return finalFieldPosition;
    }

    public void setFinalFieldPosition(Vector2 finalFieldPosition) {
        this.finalFieldPosition = finalFieldPosition;
    }
}
