package pl.kamil.TetriChess.board_elements.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.vavr.Tuple2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;

import java.util.Optional;

public class Queen extends Figure {
    private String figureId;
    private Texture figureTexture;
    private Vector2 position = new Vector2();
    private Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie
    private static final int value = 1;

    public Queen(String queenId, Texture queenTexture, float positionX, float positionY, Team team) {
        this.figureId = queenId;
        this.figureTexture = queenTexture;
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
        boolean isLegal = true;
        isLegal = isNotBlocked(initialPosition, board);
        if (!isLegal) return false;
        if (!isCheckingExpose) {
            isLegal = !isMoveExposingKingToCheck(board, initialPosition, finalPosition);
        }
        if (!isLegal) return false;
        // transition
        isLegal = isTransitionLegal(initialPosition, finalPosition);
        if (!isLegal) return false;
        // checking if smth is standing on path
        Tuple2<Vector2, Boolean> isPathBlocksFree = selectedFigure.isPathBlocksFree(initialPosition, finalPosition, this, board);
        if (!isPathBlocksFree._2) return false;
        Tuple2<Vector2, Boolean> isPathFigureFree = selectedFigure.isPathFigureFree(initialPosition, finalPosition, this, board);
        Optional<Figure> figure = board.findFigureByCoordinatesAndReturn(isPathFigureFree._1().x, isPathFigureFree._1().y);
        // check if we found figure
        if (!isPathFigureFree._2()) {
            // check if found figure is same team
            if (figure.get().getTeam().equals(selectedFigure.getTeam())) {
                isLegal = false;
            } else {
                if (finalPosition.x != isPathFigureFree._1().x || finalPosition.y != isPathFigureFree._1().y) {
                    isLegal = false;
                } else {
                    // beating if figure is not king
                    if (!figure.get().getFigureId().equals("K")) {
                        board.figuresList.remove(figure.get());
                    }
                }
            }
        }
        return isLegal;
    }

    boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition) {
        boolean isLegal = true;

        float moveDistanceY = Math.abs(finalPosition.y - initialPosition.y);
        float moveDistanceX = Math.abs(finalPosition.x - initialPosition.x);
        System.out.println("moveDistanceY: " + moveDistanceY);
        System.out.println("moveDistanceX: " + moveDistanceX);
        if (moveDistanceY == moveDistanceX ||
            moveDistanceX != 0.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 0.0 && moveDistanceY != 0.0) {
        } else isLegal = false;

        return isLegal;
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
