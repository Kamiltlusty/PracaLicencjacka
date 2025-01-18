package pl.kamil.TetriChess.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.vavr.Tuple2;

import java.util.Optional;

public class Rook extends Figure {
    private String figureId;
    private Texture figureTexture;
    private Vector2 position = new Vector2();
    private Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie
    private static final int value = 1;

    public Rook(String rookId, Texture rookTexture, float positionX, float positionY, Team team) {
        this.figureId = rookId;
        this.figureTexture = rookTexture;
        this.position.set(positionX, positionY);
        this.team = team;
    }

    @Override
    public boolean isMoveLegal(Vector2 initialPosition,
                               Vector2 finalPosition,
                               Figure selectedFigure,
                               Board board
    ) {
        boolean isLegal = true;
        // transition
        isLegal = isTransitionLegal(initialPosition, finalPosition);
        if (!isLegal) return false;
        // checking if smth is standing on path
        Tuple2<Vector2, Boolean> isPathFree = selectedFigure.isPathFree(initialPosition, finalPosition, this, board);
        Optional<Figure> figure = board.findFigureByCoordinatesAndReturn(isPathFree._1().x, isPathFree._1().y);
        // check if we found figure
        if (!isPathFree._2()) {
            // check if found figure is same team
            if (figure.get().getTeam().equals(selectedFigure.getTeam())) {
                isLegal = false;
            } else {
                if (finalPosition.x != isPathFree._1().x || finalPosition.y != isPathFree._1().y) {
                    isLegal = false;
                } else {
                    // beating
                    board.figuresList.remove(figure.get());
                }
            }
        }

        return isLegal;
    }

    private boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition) {
        boolean isLegal = true;

        float moveDistanceX = finalPosition.x - initialPosition.x;
        float moveDistanceY = finalPosition.y - initialPosition.y;
        //noinspection DuplicatedCode
        if (!(moveDistanceX == 1.0 && moveDistanceY == 0.0 || moveDistanceX == -1.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 2.0 && moveDistanceY == 0.0 || moveDistanceX == -2.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 3.0 && moveDistanceY == 0.0 || moveDistanceX == -3.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 4.0 && moveDistanceY == 0.0 || moveDistanceX == -4.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 5.0 && moveDistanceY == 0.0 || moveDistanceX == -5.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 6.0 && moveDistanceY == 0.0 || moveDistanceX == -6.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 7.0 && moveDistanceY == 0.0 || moveDistanceX == -7.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 1.0 || moveDistanceX == 0.0 && moveDistanceY == -1.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 2.0 || moveDistanceX == 0.0 && moveDistanceY == -2.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 3.0 || moveDistanceX == 0.0 && moveDistanceY == -3.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 4.0 || moveDistanceX == 0.0 && moveDistanceY == -4.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 5.0 || moveDistanceX == 0.0 && moveDistanceY == -5.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 6.0 || moveDistanceX == 0.0 && moveDistanceY == -6.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 7.0 || moveDistanceX == 0.0 && moveDistanceY == -7.0
        )) {
            isLegal = false;
        }

        return isLegal;
    }

    @Override
    public String getFigureId() {
        return figureId;
    }

    @Override
    public Texture getFigureTexture() {
        return figureTexture;
    }

    @Override
    public Vector2 setPosition(float x, float y) {
        return position.set(x, y);
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public Team getTeam() {
        return team;
    }

}
