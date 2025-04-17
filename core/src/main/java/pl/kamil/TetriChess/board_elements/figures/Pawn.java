package pl.kamil.TetriChess.board_elements.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;
import pl.kamil.TetriChess.gameplay.StateBeforeMoveRecord;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Figure {
    private String figureId;
    private Texture figureTexture;
    private Vector2 position = new Vector2();
    private Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie
    private final int value;

    public Pawn(String pawnId, Texture pawnTexture, float positionX, float positionY, Team team) {
        this.figureId = pawnId;
        this.figureTexture = pawnTexture;
        this.position.set(positionX, positionY);
        this.team = team;
        this.value = 1;
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

    @Override
    public List<Vector2> writeDownPossibleMoves() {
        Team team = getTeam();
        List<Vector2> possibleMoves = new ArrayList<>();
        Vector2 initialPosition;
        if (team == Team.WHITE) {
            if (moveCounter == 0) {
                initialPosition = new Vector2(position.x, position.y + 2);
                if (initialPosition.y < 8) {
                    possibleMoves.add(initialPosition);
                }
            }
            // to up and left diagonally
            initialPosition = new Vector2(position.x - 1, position.y + 1);
            if (initialPosition.y < 8 && initialPosition.x >= 0) {
                possibleMoves.add(initialPosition);
            }
            // to up
            initialPosition = new Vector2(position.x, position.y + 1);
            if (initialPosition.y < 8) {
                possibleMoves.add(initialPosition);
            }
            // to up and right diagonally
            initialPosition = new Vector2(position.x + 1, position.y + 1);
            if (initialPosition.y < 8 && initialPosition.x < 8) {
                possibleMoves.add(initialPosition);
            }
        } else {
            if (moveCounter == 0) {
                initialPosition = new Vector2(position.x, position.y - 2);
                if (initialPosition.y >= 0) {
                    possibleMoves.add(initialPosition);
                }
            }
            // to down and left diagonally
            initialPosition = new Vector2(position.x - 1, position.y - 1);
            if (initialPosition.y >= 0 && initialPosition.x >= 0) {
                possibleMoves.add(initialPosition);
            }
            // to down
            initialPosition = new Vector2(position.x, position.y - 1);
            if (initialPosition.y >= 0) {
                possibleMoves.add(initialPosition);
            }
            // to down and right diagonally
            initialPosition = new Vector2(position.x + 1, position.y - 1);
            if (initialPosition.y >= 0 && initialPosition.x < 8) {
                possibleMoves.add(initialPosition);
            }
        }
        return possibleMoves;
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

    public boolean isBeatingLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, Figure foundFigure, BoardManager boardManager, StateBeforeMoveRecord record) {
        // check if we found figure but we cant beat if final position is not equal figure position
        // check if found figure is same team
        if (foundFigure.getTeam().equals(selectedFigure.getTeam())) return false;
        // not letting pawn beat vertically
        if (finalPosition.x == initialPosition.x) return false;
        // don't let figure beat if it is not last chosen field
        if (finalPosition.x != foundFigure.getPosition().x ||
            finalPosition.y != foundFigure.getPosition().y)
            return false;
        boardManager.setCapturedFigureId(foundFigure.getFigureId());
        boardManager.setCapture(true);
        return true;
    }

    public String getFigureId() {
        return figureId;
    }

    @Override
    public int getValue() {
        return value;
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
