package pl.kamil.TetriChess.board_elements.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.vavr.Tuple2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Field;
import pl.kamil.TetriChess.board_elements.Team;

import java.util.Objects;
import java.util.Optional;

public class Knigth extends Figure {
    private String figureId;
    private Texture figureTexture;
    private Vector2 position = new Vector2();
    private Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie
    private static final int value = 1;

    public Knigth(String knightId, Texture knightTexture, float positionX, float positionY, Team team) {
        this.figureId = knightId;
        this.figureTexture = knightTexture;
        this.position.set(positionX, positionY);
        this.team = team;
    }

    @Override
    public boolean isMoveLegal(Vector2 initialPosition,
                               Vector2 finalPosition,
                               Figure selectedFigure,
                               BoardManager board,
                               boolean isCheckingExpose
    ) {
        if (!isNotBlocked(initialPosition, board)) return false;
//        if (!isCheckingExpose && isMoveExposingKingToCheck(board, initialPosition, finalPosition)) return false;
        // transition
        if (!isTransitionLegal(initialPosition, finalPosition)) return false;
        // checking if smth is standing on path
        if (!(selectedFigure.isPathBlocksFree(initialPosition, finalPosition, this, board))._2) return false;

        Tuple2<Vector2, Boolean> isPathFigureFree = selectedFigure.isPathFigureFree(initialPosition, finalPosition, this, board);
        Optional<Figure> figure = board.findFigureByCoordinatesAndReturn(isPathFigureFree._1().x, isPathFigureFree._1().y);
        // check if we found figure
        if (!isPathFigureFree._2()) {
            // check if found figure is same team
            if (figure.isEmpty() || figure.get().getTeam().equals(selectedFigure.getTeam())) return false;
            if (finalPosition.x != isPathFigureFree._1().x || finalPosition.y != isPathFigureFree._1().y) return false;

            // beating if figure is not king
            if (!figure.get().getFigureId().equals("K")) {
                board.setCapturedFigureId(figure.get().getFigureId());
                board.setCapture(true);
                return true;
            }
        }
        return true;
    }

    boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition) {
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

    protected Tuple2<Vector2, Boolean> isPathFigureFree(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, BoardManager board) {
        String foundSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) finalPosition.x, (int) finalPosition.y);
        if (!Objects.equals(foundSignature, "-1")) {
            // checking figure
            Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
            Optional<Figure> foundFigure = board.findFigureByCoordinatesAndReturn(fieldCoordinates.x, fieldCoordinates.y);
            if (foundFigure.isPresent()) {
                return new Tuple2<>(new Vector2(fieldCoordinates.x, fieldCoordinates.y), false);
            }
        }
        return new Tuple2<>(new Vector2(), true);
    }

    protected Tuple2<Vector2, Boolean> isPathBlocksFree(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, BoardManager board) {
        String foundSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) finalPosition.x, (int) finalPosition.y);
        if (!Objects.equals(foundSignature, "-1")) {
            // checking figure
            Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
            Field field = board.getFieldsMap().get(foundSignature);
            if (field.getBlockedState() == Field.BlockedState.BLOCKED) {
                return new Tuple2<>(new Vector2(fieldCoordinates.x, fieldCoordinates.y), false);
            }
        }
        return new Tuple2<>(new Vector2(), true);
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
