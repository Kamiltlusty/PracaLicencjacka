package pl.kamil.TetriChess.board_elements.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Figure {
    private final String figureId;
    private final Texture figureTexture;
    private final Vector2 position = new Vector2();
    private final Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie
    private final int value;

    public Queen(String queenId, Texture queenTexture, float positionX, float positionY, Team team) {
        this.figureId = queenId;
        this.figureTexture = queenTexture;
        this.position.set(positionX, positionY);
        this.team = team;
        this.value = 90;
    }

    public boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure) {
        float moveDistanceY = Math.abs(finalPosition.y - initialPosition.y);
        float moveDistanceX = Math.abs(finalPosition.x - initialPosition.x);
        return moveDistanceY == moveDistanceX ||
            moveDistanceX != 0.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 0.0 && moveDistanceY != 0.0;
    }

    @Override
    public boolean isSpecificMoveLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, Figure foundFigure, BoardManager boardManager) {
        return true;
    }

    @Override
    public List<Vector2> writeDownPossibleMoves() {
        List<Vector2> possibleMoves = new ArrayList<>();
        // check possible moves in every direction right down left up
        // to right
        Vector2 initialPosition = new Vector2(position.x + 1, position.y);
        while (initialPosition.x < 8) {
            possibleMoves.add(initialPosition);
            initialPosition = new Vector2(initialPosition.x + 1, initialPosition.y);
        }
        // to down
        initialPosition = new Vector2(position.x, position.y - 1);
        while (initialPosition.y >= 0) {
            possibleMoves.add(initialPosition);
            initialPosition = new Vector2(initialPosition.x, initialPosition.y - 1);
        }
        // to left
        initialPosition = new Vector2(position.x - 1, position.y);
        while (initialPosition.x >= 0) {
            possibleMoves.add(initialPosition);
            initialPosition = new Vector2(initialPosition.x - 1, initialPosition.y);
        }
        // to up
        initialPosition = new Vector2(position.x, position.y + 1);
        while (initialPosition.y < 8) {
            possibleMoves.add(initialPosition);
            initialPosition = new Vector2(initialPosition.x, initialPosition.y + 1);
        }
        // diagonally right up
        initialPosition = new Vector2(position.x + 1, position.y + 1);
        while (initialPosition.y < 8 && initialPosition.x < 8) {
            possibleMoves.add(initialPosition);
            initialPosition = new Vector2(initialPosition.x + 1, initialPosition.y + 1);
        }
        // diagonally right down
        initialPosition = new Vector2(position.x + 1, position.y - 1);
        while (initialPosition.y >= 0 && initialPosition.x < 8) {
            possibleMoves.add(initialPosition);
            initialPosition = new Vector2(initialPosition.x + 1, initialPosition.y - 1);
        }
        // diagonally left down
        initialPosition = new Vector2(position.x - 1, position.y - 1);
        while (initialPosition.y >= 0 && initialPosition.x >= 0) {
            possibleMoves.add(initialPosition);
            initialPosition = new Vector2(initialPosition.x - 1, initialPosition.y - 1);
        }
        // diagonally left up
        initialPosition = new Vector2(position.x - 1, position.y + 1);
        while (initialPosition.y < 8 && initialPosition.x >= 0) {
            possibleMoves.add(initialPosition);
            initialPosition = new Vector2(initialPosition.x - 1, initialPosition.y + 1);
        }
        return possibleMoves;
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
