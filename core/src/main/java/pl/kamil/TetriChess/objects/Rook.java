package pl.kamil.TetriChess.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Rook implements Figure {
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
                               Field field
    ) {
        boolean isLegal = true;
        // transition
        isLegal = isTransitionLegal(initialPosition, finalPosition);
        // beating

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
