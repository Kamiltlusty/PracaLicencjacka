package pl.kamil.TetriChess.board_elements.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.vavr.Tuple2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;
import pl.kamil.TetriChess.gameplay.StateRecord;

import java.util.Optional;

public class Pawn extends Figure {
    private String figureId;
    private Texture figureTexture;
    private Vector2 position = new Vector2();
    private Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie
    private static final int value = 1;
    private boolean hasBeat;

    public Pawn(String pawnId, Texture pawnTexture, float positionX, float positionY, Team team) {
        this.figureId = pawnId;
        this.figureTexture = pawnTexture;
        this.position.set(positionX, positionY);
        this.team = team;
        this.moveCounter = 0;
    }

    @Override
    public boolean isSpecificMoveLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, Figure foundFigure, BoardManager boardManager) {
        // check whether pawn does not move diagonally
        if (initialPosition.x == finalPosition.x) return true;
        // don't let pawn move diagonally without beating
        if (foundFigure == null) return false;
        // check whether figure to capture is opposite team
        return !foundFigure.getTeam().equals(selectedFigure.getTeam());
    }

    public boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure) {
        boolean isLegal = true;
        float moveDistanceY = finalPosition.y - initialPosition.y;
        float moveDistanceX = Math.abs(finalPosition.x - initialPosition.x);
        if (selectedFigure.getTeam() == Team.WHITE) {
            if (moveCounter == 0) {
                if (!(moveDistanceY == 2.0 && moveDistanceX == 0.0 ||
                    moveDistanceY == 1.0 && moveDistanceX <= 1.0)) return false;
            } else if (moveDistanceY != 1.0 || moveDistanceX > 1.0) return false;
        } else {
            if (moveCounter == 0) {
                if (!(moveDistanceY == -2.0 && moveDistanceX == 0.0 ||
                    moveDistanceY == -1.0 && moveDistanceX <= 1.0)) return false;
            } else if (moveDistanceY != -1.0 || moveDistanceX > 1.0) return false;
        }
        return isLegal;
    }

    public boolean isBeatingLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, Figure foundFigure, BoardManager boardManager, StateRecord record) {
        // check if we found figure but we cant beat if final position is not equal figure position
        // check if found figure is same team
        if (foundFigure.getTeam().equals(selectedFigure.getTeam())) return false;
        // not letting pawn beat vertically
        if (finalPosition.x == initialPosition.x) return false;
        // don't let figure beat if it is not last chosen field
        if (finalPosition.x != foundFigure.getPosition().x || finalPosition.y != foundFigure.getPosition().y)
            return false;
        boardManager.setCapturedFigureId(foundFigure.getFigureId());
        boardManager.setCapture(true);
        record.setCapturedFigureId(foundFigure.getFigureId());
        record.setCapture(true);
        return true;
    }

    public String getFigureId() {
        return figureId;
    }

    public Texture getFigureTexture() {
        return figureTexture;
    }

    @Override
    public Vector2 setPosition(float x, float y) {
        return position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Team getTeam() {
        return team;
    }
}
