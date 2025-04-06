package pl.kamil.TetriChess.board_elements.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.vavr.Tuple2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Knight extends Figure {
    private String figureId;
    private Texture figureTexture;
    private Vector2 position = new Vector2();
    private Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie
    private static final int value = 1;

    public Knight(String knightId, Texture knightTexture, float positionX, float positionY, Team team) {
        this.figureId = knightId;
        this.figureTexture = knightTexture;
        this.position.set(positionX, positionY);
        this.team = team;
    }

    public boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selctedFigure) {
        float moveDistanceY = Math.abs(finalPosition.y - initialPosition.y);
        float moveDistanceX = Math.abs(finalPosition.x - initialPosition.x);
        return (moveDistanceY == 1.0 && moveDistanceX == 2.0 ||
            moveDistanceY == 1.0 && moveDistanceX == -2.0 ||
            moveDistanceY == -1.0 && moveDistanceX == 2.0 ||
            moveDistanceY == -1.0 && moveDistanceX == -2.0 ||
            moveDistanceY == 2.0 && moveDistanceX == 1.0 ||
            moveDistanceY == 2.0 && moveDistanceX == -1.0 ||
            moveDistanceY == -2.0 && moveDistanceX == 1.0 ||
            moveDistanceY == -2.0 && moveDistanceX == -1.0);
    }

    @Override
    public boolean isSpecificMoveLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, Figure foundFigure, BoardManager boardManager) {
        return true;
    }

    @Override
    public List<Vector2> writeDownPossibleMoves() {
        List<Vector2> possibleMoves = new ArrayList<>();
        // check possible moves in every direction
        // to right and up
        Vector2 initialPosition = new Vector2(position.x + 2, position.y + 1);
        if (initialPosition.y < 8 && initialPosition.x < 8) {
            possibleMoves.add(initialPosition);
        }
        // to right and down
        initialPosition = new Vector2(position.x + 2, position.y - 1);
        if (initialPosition.y >= 0 && initialPosition.x < 8) {
            possibleMoves.add(initialPosition);
        }
        // to down and right
        initialPosition = new Vector2(position.x + 1, position.y - 2);
        if (initialPosition.y >= 0 && initialPosition.x < 8) {
            possibleMoves.add(initialPosition);
        }
        // to down and left
        initialPosition = new Vector2(position.x - 1, position.y - 2);
        if (initialPosition.y >= 0 && initialPosition.x >= 0) {
            possibleMoves.add(initialPosition);
        }
        // to left and down
        initialPosition = new Vector2(position.x - 2, position.y - 1);
        if (initialPosition.y >= 0 && initialPosition.x >= 0) {
            possibleMoves.add(initialPosition);
        }
        // to left and up
        initialPosition = new Vector2(position.x - 2, position.y + 1);
        if (initialPosition.y < 8 && initialPosition.x >= 0) {
            possibleMoves.add(initialPosition);
        }
        // to up and left
        initialPosition = new Vector2(position.x - 1, position.y + 2);
        if (initialPosition.y < 8 && initialPosition.x >= 0) {
            possibleMoves.add(initialPosition);
        }
        // to up and right
        initialPosition = new Vector2(position.x + 1, position.y + 2);
        if (initialPosition.y < 8 && initialPosition.x < 8) {
            possibleMoves.add(initialPosition);
        }
        return possibleMoves;
    }

    public Optional<Figure> isPathFigureFree(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, BoardManager board) {
        String foundSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) finalPosition.x, (int) finalPosition.y);
        if (!Objects.equals(foundSignature, "-1")) {
            // checking figure
            Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
            Optional<Figure> foundFigure = board.findFigureByCoordinatesAndReturn(fieldCoordinates.x, fieldCoordinates.y);
            if (foundFigure.isPresent()) {
                return foundFigure;
            }
        }
        return Optional.empty();
    }

    protected Tuple2<Vector2, Boolean> isPathBlocksFree(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, BoardManager board) {
        String foundSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) finalPosition.x, (int) finalPosition.y);
        return checkIsFieldBlocked(board, foundSignature);
    }

    public String getFigureId() {
        return figureId;
    }

    public Texture getFigureTexture() {
        return figureTexture;
    }

    @Override
    public Vector2 setPosition(float x, float y) {
        return (Vector2) position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Team getTeam() {
        return team;
    }
}
